package com.project.cmn.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * DataSource 설정들
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "project.datasources")
public class DataSourcesConfig {
    /**
     * project.datasources 설정 사용 여부
     */
    private boolean enabled;

    /**
     * DataSource 설정들
     */
    private List<DataSourceItem> datasourceItemList;
}
