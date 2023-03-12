package com.project.cmn.http.accesslog;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * 접근로그 관련 설정
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "project.access.log")
public class AccessLogConfig {
    /**
     * {@link Environment}에서 project.access.log 설정을 가져와 {@link AccessLogConfig}로 변환한다.
     *
     * @param environment {@link Environment}
     * @return {@link AccessLogConfig}
     */
    public static AccessLogConfig init(Environment environment) {
        return Binder.get(environment).bindOrCreate("project.access.log", AccessLogConfig.class);
    }

    /**
     * Access Log 사용 여부
     */
    private boolean enabled;

    /**
     * Request Header의 로깅 여부. default: false
     */
    private boolean requestHeader;

    /**
     * Request Parameter의 로깅 여부. default: false
     */
    private boolean requestParam;

    /**
     * Request Body의 로깅 여부. default: false
     */
    private boolean requestBody;

    /**
     * Response Header의 로깅 여부. default: false
     */
    private boolean responseHeader;

    /**
     * Response Body의 로깅 여부. default: false
     */
    private boolean responseBody;

    /**
     * 로깅할 Request Body의 길이. default: 0 - 전체를 로깅
     */
    private int requestBodyLength;

    /**
     * 로깅할 Response Body의 길이. default: 0 - 전체를 로깅
     */
    private int responseBodyLength;

    /**
     * 로깅할 Request Header들. default: 전체 로깅
     */
    private List<String> requestHeaderNames;

    /**
     * 로깅할 Response Header들. default: 전체 로깅
     */
    private List<String> responseHeaderNames;

    /**
     * 로깅하지 않을 Path Pattern 들
     */
    private List<String> excludePathPatterns;
}
