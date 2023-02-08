package com.project.cmn.datasource;

import java.util.List;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * DataSource 설정들
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "project.datasources")
public class DataSourcesConfig implements InitializingBean {
	/**
	 * DataSource 사용 여부
	 */
	private boolean enabled;

	/**
	 * DataSource 설정들
	 */
	private List<DataSourceItem> datasourceItemList;

	@Override
	public void afterPropertiesSet() throws Exception {
	}
}
