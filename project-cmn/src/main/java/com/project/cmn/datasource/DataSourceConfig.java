package com.project.cmn.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * DataSource 설정
 * #project.datasource
 */
@Getter
@Setter
@ToString
public class DataSourceConfig {
    /**
     * {@link Environment}에서 project.datasource 설정을 가져와 {@link DataSourceConfig}로 변환한다.
     *
     * @param environment {@link Environment}
     * @return {@link DataSourceConfig}
     */
    public static DataSourceConfig init(Environment environment) {
        return Binder.get(environment).bindOrCreate("project.datasource", DataSourceConfig.class);
    }

    /**
     * project.datasource 설정 사용여부
     * #project.datasource.enabled
     */
    private boolean enabled;

    /**
     * {@link com.zaxxer.hikari.HikariDataSource}를 위한 설정들
     * #project.datasource.item-list
     */
    private List<DataSourceItem> itemList;
}
