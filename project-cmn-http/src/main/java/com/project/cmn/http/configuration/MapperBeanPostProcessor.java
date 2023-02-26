package com.project.cmn.http.configuration;

import com.project.cmn.datasource.DataSourceItem;
import com.project.cmn.datasource.DataSourcesConfig;
import com.project.cmn.mybatis.MyBatisConfig;
import com.project.cmn.mybatis.MyBatisItem;
import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
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
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class MapperBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {
    private final DataSourcesConfig dataSourcesConfig;
    private final MyBatisConfig myBatisConfig;

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        registerDataSources(registry);
        registerSqlSessionTemplate(registry);
        registerMapperScanner(registry);
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
// 나중에 뭐에 쓸지 고민해봅시다.
    }

    private void registerDataSources(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition beanDefinition;

        for (DataSourceItem item : dataSourcesConfig.getDatasourceItemList()) {
            beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(HikariDataSource.class)
                    .addConstructorArgValue(item.getHikariConfig())
                    .getBeanDefinition();

            log.debug("# HikariDataSource Register {}", item.getDatasourceName());
            registry.registerBeanDefinition(item.getDatasourceName(), beanDefinition);
        }
    }

    private void registerSqlSessionTemplate(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition sqlSessionFacotry;
        AbstractBeanDefinition sqlSessionTemplate;

        for (MyBatisItem item : myBatisConfig.getMybatisItemList()) {
            sqlSessionFacotry = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionFactoryBean.class)
                    .addPropertyReference("dataSource", item.getDatasourceName())
                    .addPropertyValue("configLocation", new ClassPathResource(item.getConfigLocation()))
                    .addPropertyValue("mapperLocations", this.getMapperLocation(item))
                    .addPropertyValue("typeAliasesPackage", this.getTypeAliasesPackage(item))
                    .getBeanDefinition();

            sqlSessionTemplate = BeanDefinitionBuilder.genericBeanDefinition(SqlSessionTemplate.class)
                    .addConstructorArgValue(sqlSessionFacotry)
                    .getBeanDefinition();

            log.debug("# SqlSessionTemplate Register {}", item.getSqlSessionTemplateName());
            registry.registerBeanDefinition(item.getSqlSessionTemplateName(), sqlSessionTemplate);
        }
    }

    private void registerMapperScanner(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition mapperScannerConfigurer;

        for (MyBatisItem item : myBatisConfig.getMybatisItemList()) {
            mapperScannerConfigurer = BeanDefinitionBuilder.genericBeanDefinition(MapperScannerConfigurer.class)
                    .addPropertyValue("sqlSessionTemplateBeanName", item.getSqlSessionTemplateName())
                    .addPropertyValue("basePackage", item.getMapperPackage())
                    .addPropertyValue("annotationClass", Mapper.class)
                    .getBeanDefinition();

            log.debug("# MapperScannerConfigurer Register {}", item.getSqlSessionTemplateName() + "Mapper");
            registry.registerBeanDefinition(item.getSqlSessionTemplateName() + "Mapper", mapperScannerConfigurer);
        }
    }

    private Resource[] getMapperLocation(MyBatisItem item) {
        Resource[] mapperResources = null;
        Resource[] tempMapperResources;

        try {
            for (String mapperLocation : item.getMapperLocations()) {
                tempMapperResources = new PathMatchingResourcePatternResolver().getResources(mapperLocation);

                if (mapperResources == null) {
                    mapperResources = tempMapperResources;
                } else {
                    ArrayUtils.addAll(mapperResources, tempMapperResources);
                }
            }
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }

        return mapperResources;
    }

    private String getTypeAliasesPackage(MyBatisItem item) {
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < item.getTypeAliasesPackages().size(); i++) {
            sb.append(item.getTypeAliasesPackages().get(i));

            if (i != item.getTypeAliasesPackages().size() - 1) {
                sb.append(",");
            }
        }

        return sb.toString();
    }
}
