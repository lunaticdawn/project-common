package com.project.cmn.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.lang.NonNull;

@Slf4j
@RequiredArgsConstructor
public class RegistryDataSources implements BeanDefinitionRegistryPostProcessor {
    private final DataSourcesConfig dataSourcesConfig;

    @Override
    public void postProcessBeanDefinitionRegistry(@NonNull BeanDefinitionRegistry registry) throws BeansException {
        this.registerDataSources(registry);
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

        for (DataSourceItem item : dataSourcesConfig.getDatasourceItemList()) {
            if (!item.isEnabled()) {
                continue;
            }

            if (item.isLazyConnection()) {
                beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(LazyConnectionDataSourceProxy.class)
                        .addConstructorArgValue(new HikariDataSource(item.getHikariConfig()))
                        .getBeanDefinition();
            } else {
                beanDefinition = BeanDefinitionBuilder.genericBeanDefinition(HikariDataSource.class)
                        .addConstructorArgValue(new HikariDataSource(item.getHikariConfig()))
                        .getBeanDefinition();
            }

            log.debug("# DataSource Register {}", item.getDatasourceName());

            registry.registerBeanDefinition(item.getDatasourceName(), beanDefinition);
        }
    }
}
