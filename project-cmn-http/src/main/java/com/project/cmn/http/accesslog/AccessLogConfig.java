package com.project.cmn.http.accesslog;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 접근로그 관련 설정
 *
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "project.access.log")
public class AccessLogConfig {

	/**
	 * Access Log 사용 여부
	 */
	private boolean enabled;

	/**
	 * Request Header의 로깅 여부. default: false
	 */
	private boolean isRequestHeader;

	/**
	 * Request Parameter의 로깅 여부. default: false
	 */
	private boolean isRequestParam;

	/**
	 * Request Body의 로깅 여부. default: false
	 */
	private boolean isRequestBody;

	/**
	 * Response Header의 로깅 여부. default: false
	 */
	private boolean isResponseHeader;

	/**
	 * Response Body의 로깅 여부. default: false
	 */
	private boolean isResponseBody;

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
