package com.project.cmn.datasource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;

import com.zaxxer.hikari.HikariDataSource;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class DataSources implements BeanPostProcessor {

	private final ConfigurableBeanFactory configurableBeanFactory;

	@Override
	public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
		if (!(bean instanceof DataSourcesConfig)) {
			return bean;
		}

		DataSourcesConfig dataSourcesConfig = (DataSourcesConfig) bean;

		for (DataSourceItem item : dataSourcesConfig.getDatasourceItemList()) {
			HikariDataSource dataSource = new HikariDataSource();

			dataSource.setDriverClassName(item.getDriverClassName());
			dataSource.setJdbcUrl(item.getJdbcUrl());
			dataSource.setUsername(item.getUsername());
			dataSource.setPassword(item.getPassword());
			dataSource.setConnectionTestQuery(item.getConnectionTestQuery());

			configurableBeanFactory.registerSingleton(item.getDatasourceName(), dataSource);

			log.debug("# Create Datasource: {}", item.getDatasourceName());
		}

		return bean;
	}
}
