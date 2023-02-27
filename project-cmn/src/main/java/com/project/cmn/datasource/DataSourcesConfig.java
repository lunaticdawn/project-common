package com.project.cmn.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * DataSource 설정들
 */
@Getter
@Setter
@ToString
public class DataSourcesConfig {
    public static DataSourcesConfig init(Environment env) {
        return Binder.get(env).bind("project.datasources", DataSourcesConfig.class).get();
    }

    /**
     * project.datasources 설정 사용여부
     */
    private boolean enabled;

    /**
     * project.datasources.datasource-item-list 설정들
     */
    private List<DataSourceItem> datasourceItemList;
}
