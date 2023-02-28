package com.project.cmn.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.lang.NonNull;

@Slf4j
@RequiredArgsConstructor
public class RegistryDataSource implements BeanDefinitionRegistryPostProcessor {
    private final DataSourceConfig dataSourceConfig;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        this.registerDataSources(registry);
        this.registerTransaction(registry);
    }

    @Override
    public void postProcessBeanFactory(@NonNull ConfigurableListableBeanFactory beanFactory) throws BeansException {
        log.debug("# postProcessBeanFactory");
    }

    /**
     * DataSource Bean에 대한 정의를 등록한다.
     *
     * @param registry {@link BeanDefinitionRegistry}
     */
    private void registerDataSources(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition beanDefinition;
        HikariDataSource hikariDataSource = null;

        for (DataSourceItem item : dataSourceConfig.getItemList()) {
            if (!item.isEnabled()) {
                continue;
            }

            try {
                hikariDataSource = new HikariDataSource(item.getHikariConfig());
            } catch (Exception e) {
                log.error(e.getMessage(), e);

                // 메인 DB 연결 실패면 Exception을 전파시킴
                if (item.isPrimary()) {
                    throw e;
                }
            }

            if (hikariDataSource != null) {
                if (item.isLazyConnection()) {
                    beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(LazyConnectionDataSourceProxy.class)
                            .addConstructorArgValue(hikariDataSource)
                            .setPrimary(item.isPrimary())
                            .getBeanDefinition();
                } else {
                    beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(HikariDataSource.class)
                            .addConstructorArgValue(hikariDataSource)
                            .setPrimary(item.isPrimary())
                            .getBeanDefinition();
                }

                log.debug("# DataSource Register {}", item.getDatasourceName());

                registry.registerBeanDefinition(item.getDatasourceName(), beanDefinition);
            }
        }
    }

    /**
     * Transaction Bean에 대한 정의를 등록한다.
     *
     * @param registry {@link BeanDefinitionRegistry}
     */
    private void registerTransaction(BeanDefinitionRegistry registry) {
        AbstractBeanDefinition beanDefinition;

        for (DataSourceItem item : dataSourceConfig.getItemList()) {
            if (!item.isEnabled() || StringUtils.isBlank(item.getTransactionName())) {
                continue;
            }

            beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(DataSourceTransactionManager.class)
                    .addConstructorArgReference(item.getDatasourceName())
                    .getBeanDefinition();

            log.debug("# Transaction Register {}", item.getTransactionName());

            registry.registerBeanDefinition(item.getTransactionName(), beanDefinition);
        }
    }
}
