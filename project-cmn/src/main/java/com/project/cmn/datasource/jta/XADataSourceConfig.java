package com.project.cmn.datasource.jta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * XADataSource 설정
 * #project.xa-datasource
 */
@Getter
@Setter
@ToString
public class XADataSourceConfig {
    /**
     * {@link Environment}에서 project.xa-datasource 설정을 가져와 {@link XADataSourceConfig}로 변환한다.
     *
     * @param environment {@link Environment}
     * @return {@link XADataSourceConfig}
     */
    public static XADataSourceConfig init(Environment environment) {
        return Binder.get(environment).bindOrCreate("project.xa-datasource", XADataSourceConfig.class);
    }

    /**
     * project.xa-datasource 설정 사용여부
     * #project.xa-datasource.enabled
     */
    private boolean enabled;

    /**
     * 여러 개의 {@link javax.sql.XADataSource} 생성에 필요한 설정들
     * #project.xa-datasource.item-list
     */
    private List<XADataSourceItem> itemList;
}
