package com.project.cmn.mybatis;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.mapper.MapperScannerConfigurer;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

@Slf4j
@RequiredArgsConstructor
public class CreateMyBatis implements BeanPostProcessor {
    private final MyBatisConfig myBatisConfig;
    private final ConfigurableBeanFactory configurableBeanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        SqlSessionFactory sqlSessionFactory;
        SqlSessionTemplate sqlSessionTemplate;
        MapperScannerConfigurer mapperScannerConfigurer;

        for (MyBatisItem item : myBatisConfig.getMybatisItemList()) {
            if (!configurableBeanFactory.containsBean(item.getSqlSessionTemplateName())) {
                sqlSessionFactory = this.getSqlSessionFactory(item);

                if (sqlSessionFactory != null) {
                    sqlSessionTemplate = new SqlSessionTemplate(sqlSessionFactory);
                    mapperScannerConfigurer = new MapperScannerConfigurer();

                    mapperScannerConfigurer.setBasePackage(item.getMapperPackage());
                    mapperScannerConfigurer.setSqlSessionTemplateBeanName(item.getSqlSessionTemplateName());

                    configurableBeanFactory.registerSingleton(item.getSqlSessionTemplateName() + "Mapper", mapperScannerConfigurer);
                    configurableBeanFactory.registerSingleton(item.getSqlSessionTemplateName(), sqlSessionTemplate);

                    log.debug("# REGISTER {}", item.getSqlSessionTemplateName());
                }
            }
        }

        return bean;
    }

    /**
     * {@link MyBatisItem}에 있는 정보를 이용하여 {@link SqlSessionFactory}를 생성한다.
     *
     * @param item {@link MyBatisItem}
     * @return {@link SqlSessionFactory}
     */
    private SqlSessionFactory getSqlSessionFactory(MyBatisItem item) {
        SqlSessionFactory sqlSessionFactory = null;

        try {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
            HikariDataSource dataSource = configurableBeanFactory.getBean(item.getDatasourceName(), HikariDataSource.class);

            // DataSource 등록
            sqlSessionFactoryBean.setDataSource(dataSource);

            // 설정파일 등록
            sqlSessionFactoryBean.setConfigLocation(new ClassPathResource(item.getConfigLocation()));

            Resource[] mapperResources = null;
            Resource[] tempMapperResources;

            // 쿼리 XML 파일 등록
            for (String mapperLocation : item.getMapperLocations()) {
                tempMapperResources = new PathMatchingResourcePatternResolver().getResources(mapperLocation);

                if (mapperResources == null) {
                    mapperResources = tempMapperResources;
                } else {
                    ArrayUtils.addAll(mapperResources, tempMapperResources);
                }
            }

            sqlSessionFactoryBean.setMapperLocations(mapperResources);

            // Type Aliases 등록
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < item.getTypeAliasesPackages().size(); i++) {
                sb.append(item.getTypeAliasesPackages().get(i));

                if (i != item.getTypeAliasesPackages().size() - 1) {
                    sb.append(",");
                }
            }

            sqlSessionFactoryBean.setTypeAliasesPackage(sb.toString());

            sqlSessionFactory = sqlSessionFactoryBean.getObject();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return sqlSessionFactory;
    }
}
