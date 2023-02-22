package com.project.cmn.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import static java.util.concurrent.TimeUnit.SECONDS;

@Slf4j
@RequiredArgsConstructor
public class CreateDataSources implements BeanPostProcessor {
    private final DataSourcesConfig dataSourcesConfig;
    private final ConfigurableBeanFactory configurableBeanFactory;

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        for (DataSourceItem item : dataSourcesConfig.getDatasourceItemList()) {
            if (!configurableBeanFactory.containsBean(item.getDatasourceName())) {
                HikariDataSource dataSource = new HikariDataSource();

                // 필수 설정
                dataSource.setPoolName(item.getDatasourceName());
                dataSource.setDriverClassName(item.getDriverClassName());
                dataSource.setJdbcUrl(item.getJdbcUrl());
                dataSource.setUsername(item.getUsername());
                dataSource.setPassword(item.getPassword());

                // 옵션 설정
                if (StringUtils.isNotBlank(item.getConnectionTestQuery())) {
                    dataSource.setConnectionTestQuery(item.getConnectionTestQuery());
                }

                if (item.getConnectionTimeout() != 0) {
                    dataSource.setConnectionTimeout(SECONDS.toMillis(item.getConnectionTimeout()));
                }

                if (item.getIdleTimeout() != 0) {
                    dataSource.setIdleTimeout(SECONDS.toMillis(item.getIdleTimeout()));
                }

                if (item.getKeepaliveTime() != 0) {
                    dataSource.setKeepaliveTime(SECONDS.toMillis(item.getKeepaliveTime()));
                }

                if (item.getMaxLifetime() != 0) {
                    dataSource.setMaxLifetime(SECONDS.toMillis(item.getMaxLifetime()));
                }

                if (item.getMinimumIdle() != 0) {
                    dataSource.setMinimumIdle(item.getMinimumIdle());
                }

                if (item.getMaximumPoolSize() != 0) {
                    dataSource.setMaximumPoolSize(item.getMaximumPoolSize());
                }

                if (item.getLeakDetectionThreshold() != 0) {
                    dataSource.setLeakDetectionThreshold(SECONDS.toMillis(item.getLeakDetectionThreshold()));
                }

                configurableBeanFactory.registerSingleton(item.getDatasourceName(), dataSource);

                log.debug("# REGISTER {}", item.getDatasourceName());
            }
        }

        return bean;
    }
}
