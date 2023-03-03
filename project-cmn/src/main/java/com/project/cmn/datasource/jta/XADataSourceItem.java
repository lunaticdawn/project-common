package com.project.cmn.datasource.jta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

/**
 * {@link javax.sql.XADataSource} 생성에 필요한 설정
 * #project.xa-datasource.item-list
 */
@Getter
@Setter
@ToString
public class XADataSourceItem {
    /**
     * 데이터소스 사용여부
     * #project.xa-datasource.item-list.enabled
     */
    private boolean enabled;

    /**
     * 리소스 이름
     * #project.xa-datasource.item-list.unique-resource-name
     */
    private String uniqueResourceName;

    /**
     * DBMS별 XADataSource 클래스 명
     * #project.xa-datasource.item-list.xa-data-source-class-name
     */
    private String xaDataSourceClassName;

    /**
     * DB의 URL
     * #project.xa-datasource.item-list.url
     */
    private String url;

    /**
     * DB 사용자 이름
     * #project.xa-datasource.item-list.user
     */
    private String user;

    /**
     * DB 비밀번호
     * #project.xa-datasource.item-list.password
     */
    private String password;

    /**
     * Pool 사이즈
     * #project.xa-datasource.item-list.pool-size
     */
    private int poolSize;

    /**
     * project.xa-datasource.item-list 의 설정 정보를 바탕으로 {@link Properties}를 생성한 후 반환한다.
     *
     * @return 설정 정보를 바탕으로 생성한 {@link Properties}
     */
    public Properties getProperties() {
        Properties properties = new Properties();

        // Oracle 의 XADataSource 의 경우, url 에 대한 속성이 URL 로 대문자임
        if (StringUtils.containsIgnoreCase(xaDataSourceClassName, "oracle")) {
            properties.put("URL", url);
        } else {
            properties.put("url", url);
        }

        properties.put("user", user);
        properties.put("password", password);

        return properties;
    }
}
