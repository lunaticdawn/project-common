package com.project.cmn.datasource;

import com.project.cmn.datasource.jta.XADataSourceConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
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
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.lang.NonNull;

@Slf4j
@AutoConfiguration
@ConditionalOnProperty(prefix = "project.datasource", value = "enabled", havingValue = "true")
public class RegistryDataSource implements BeanDefinitionRegistryPostProcessor, EnvironmentAware {
    private DataSourceConfig dataSourceConfig;
    private XADataSourceConfig xaDataSourceConfig;

    @Override
    public void setEnvironment(@NonNull Environment environment) {
        this.dataSourceConfig = DataSourceConfig.init(environment);
        this.xaDataSourceConfig = XADataSourceConfig.init(environment);
    }

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        if (!xaDataSourceConfig.isEnabled()) {
            log.info("# RegistryDataSource");

            this.registerDataSource(registry);
            this.registerTransactionManager(registry);
        }
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("# postProcessBeanFactory");
    }

    /**
     * {@link javax.sql.DataSource}에 대한 정의를 등록한다.
     *
     * @param registry {@link BeanDefinitionRegistry}
     */
    private void registerDataSource(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition beanDefinition;

        for (DataSourceItem item : dataSourceConfig.getItemList()) {
            if (!item.isEnabled()) {
                continue;
            }

            if (item.isLazyConnection()) {
                beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(LazyConnectionDataSourceProxy.class)
                        .addConstructorArgValue(new HikariDataSource(item.getHikariConfig()))
                        .setPrimary(item.isPrimary())
                        .getBeanDefinition();
            } else {
                beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(HikariDataSource.class)
                        .addPropertyValue("dataSource", new HikariDataSource(item.getHikariConfig()))
                        .setPrimary(item.isPrimary())
                        .getBeanDefinition();
            }

            log.info("# DataSource({}) Register.", item.getDatasourceName());
            registry.registerBeanDefinition(item.getDatasourceName(), beanDefinition);
        }
    }

    /**
     * {@link DataSourceTransactionManager}에 대한 정의를 등록한다.
     *
     * @param registry {@link BeanDefinitionRegistry}
     */
    private void registerTransactionManager(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition beanDefinition;

        for (DataSourceItem item : dataSourceConfig.getItemList()) {
            if (!item.isEnabled()) {
                continue;
            }

            beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class)
                    .addConstructorArgReference(item.getDatasourceName())
                    .setPrimary(item.isPrimary())
                    .getBeanDefinition();

            log.info("# Transaction({}) Register.", item.getTransactionName());
            registry.registerBeanDefinition(item.getTransactionName(), beanDefinition);
        }
    }
}
