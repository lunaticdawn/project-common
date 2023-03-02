package com.project.cmn.mybatis;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.lang.NonNull;

import java.io.IOException;

@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "project.mybatis", value = "enabled", havingValue = "true")
public class RegistryMyBatisMapper implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private MyBatisConfig myBatisConfig;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.myBatisConfig = MyBatisConfig.init(environment);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        log.info("# RegistryMyBatisMapper");
        registerSqlSessionTemplate(registry);
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("# postProcessBeanFactory");
    }

    /**
     * {@link SqlSessionTemplate}과 {@link MapperScannerConfigurer}를 등록한다.
     *
     * @param registry {@link BeanDefinitionRegistry}
     */
    private void registerSqlSessionTemplate(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition sqlSessionFactory;
        AbstractBeanDefinition sqlSessionTemplate;
        AbstractBeanDefinition mapperScannerConfigurer;

        for (MyBatisItem item : myBatisConfig.getItemList()) {
            if (!item.isEnabled()) {
                continue;
            }

            // SqlSessionFactory 와 SqlSessionTemplate 중 하나만 등록하면 되기 때문에 SqlSessionTemplate 만 등록
            sqlSessionFactory = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class)
                    .addPropertyReference("dataSource", item.getDatasourceName())
                    .addPropertyValue("configLocation", new DefaultResourceLoader().getResource(item.getConfigLocation()))
                    .addPropertyValue("mapperLocations", this.getMapperLocation(item))
                    .addPropertyValue("typeAliasesPackage", String.join(",", item.getTypeAliasesPackages()))
                    .getBeanDefinition();

            sqlSessionTemplate = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class)
                    .addConstructorArgValue(sqlSessionFactory)
                    .setPrimary(item.isPrimary())
                    .getBeanDefinition();

            log.info("# SqlSessionTemplate({}) Register.", item.getSqlSessionTemplateName());
            registry.registerBeanDefinition(item.getSqlSessionTemplateName(), sqlSessionTemplate);

            mapperScannerConfigurer = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class)
                    .addPropertyValue("sqlSessionTemplateBeanName", item.getSqlSessionTemplateName())
                    .addPropertyValue("basePackage", item.getMapperPackage())
                    .addPropertyValue("annotationClass", Mapper.class)
                    .getBeanDefinition();

            log.info("# MapperScannerConfigurer({}) Register", item.getSqlSessionTemplateName() + "-mapper");
            registry.registerBeanDefinition(item.getSqlSessionTemplateName() + "-mapper", mapperScannerConfigurer);
        }
    }

    /**
     * Mapper 위치에 대한 설정 정보를 {@link SqlSessionFactoryBean}에 등록할 수 있는 형태로 변경한다.
     *
     * @param item {@link MyBatisItem}
     * @return Mapper 위치에 대한 설정 정보를 {@link SqlSessionFactoryBean}에 등록할 수 있는 형태로 변경한 {@link Resource} 배열
     */
    private Resource[] getMapperLocation(MyBatisItem item) {
        Resource[] mapperResources = null;
        Resource[] tempMapperResources;

        try {
            for (String mapperLocation : item.getMapperLocations()) {
                tempMapperResources = new PathMatchingResourcePatternResolver().getResources(mapperLocation);

                if (mapperResources == null) {
                    mapperResources = tempMapperResources;
                } else {
                    mapperResources = ArrayUtils.addAll(mapperResources, tempMapperResources);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return mapperResources;
    }
}
