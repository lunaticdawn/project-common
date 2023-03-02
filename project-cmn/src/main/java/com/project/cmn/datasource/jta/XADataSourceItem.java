package com.project.cmn.datasource.jta;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;

@Getter
@Setter
@ToString
public class XADataSourceItem {
    /**
     * 데이터소스 사용여부
     */
    private boolean enabled;

    /**
     * 리소스 이름
     */
    private String uniqueResourceName;

    /**
     * XA 데이터소스의 클래스 이름
     */
    private String xaDataSourceClassName;

    /**
     * DB의 URL
     */
    private String url;

    /**
     * DB 사용자 이름
     */
    private String user;

    /**
     * DB 비밀번호
     */
    private String password;

    /**
     * Pool 사이즈
     */
    private int poolSize;

    /**
     * 설정 정보를 바탕으로 {@link Properties}를 생성한 후 반환한다.
     *
     * @return 설정 정보를 바탕으로 생성한 {@link Properties}
     */
    public Properties getProperties() {
        Properties properties = new Properties();

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
