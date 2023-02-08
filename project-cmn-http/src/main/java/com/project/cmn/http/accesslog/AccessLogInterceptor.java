package com.project.cmn.http.accesslog;


import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.Nullable;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.project.cmn.http.util.JsonUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * {@link HttpServletRequest}와 {@link HttpServletResponse}를 분석하여 접근로그에 대한 정보를 {@link AccessLogDto}에 담고, 로깅한다.
 */
@Slf4j
@RequiredArgsConstructor
public class AccessLogInterceptor implements HandlerInterceptor {
	private final AccessLog accessLog;
	private final AccessLogConfig accessLogConfig;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		accessLog.start(request, accessLogConfig);
		accessLog.setRequestHeader(request);
		accessLog.setRequestParam(request);

		this.getRequestInfoLog(AccessLog.getAccessLogDto());

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable Exception ex) throws Exception {
		accessLog.setRequestBody(request);
		accessLog.setResponseHeader(response);
		accessLog.setResponseBody(response);

		AccessLogDto accessLogDto = accessLog.end();

		this.getRequestBodyLogStr(accessLogDto);
		this.getResponseLogStr(accessLogDto);

		StopWatch stopWatch = accessLogDto.getStopWatch();

		if (stopWatch != null) {
			if (stopWatch.isRunning()) {
				stopWatch.stop();
			}

			if (log.isDebugEnabled()) {
				log.debug("\n{}", stopWatch.prettyPrint());
			}
		}
	}

	private void getRequestInfoLog(AccessLogDto accessLogDto) {
		StringBuilder buff = new StringBuilder();

		buff.append("\n");
		buff.append("-------------------- REQUEST INFO START --------------------");
		buff.append("\n");
		buff.append(String.format("-- URL: %s %s", accessLogDto.getRequestMethod(), accessLogDto.getRequestUri()));
		buff.append("\n");

		if (accessLogConfig.isRequestHeader()) {
			buff.append(String.format("-- HEADER: %s", accessLogDto.getRequestHeader()));
			buff.append("\n");
		}

		if (accessLogConfig.isRequestParam()) {
			buff.append(String.format("-- PARAM: %s", accessLogDto.getRequestParam()));
			buff.append("\n");
		}

		buff.append("-------------------- REQUEST INFO END   --------------------");

		log.info(buff.toString());
	}

	private void getRequestBodyLogStr(AccessLogDto accessLogDto) {
		if (accessLogConfig.isRequestBody()) {
			StringBuilder buff = new StringBuilder();

			buff.append("\n");
			buff.append("-------------------- REQUEST BODY START --------------------");
			buff.append("\n");

			if (StringUtils.isNotEmpty(accessLogDto.getRequestPayload())) {
				buff.append(accessLogDto.getRequestPayload());
			}

			buff.append("\n");
			buff.append("-------------------- REQUEST BODY END   --------------------");

			log.info(buff.toString());
		}
	}

	private void getResponseLogStr(AccessLogDto accessLogDto) {
		if (accessLogConfig.isResponseHeader() || accessLogConfig.isResponseBody()) {
			StringBuilder buff = new StringBuilder();

			buff.append("\n");
			buff.append("-------------------- RESPONSE START --------------------");
			buff.append("\n");

			if (accessLogConfig.isResponseHeader()) {
				buff.append(String.format("-- HEADER: %s", accessLogDto.getResponseHeader()));
				buff.append("\n");
			}

			if (accessLogConfig.isResponseBody()) {
				try {
					if (StringUtils.isNotBlank(accessLogDto.getResponsePayload())) {
						buff.append("-- BODY:\n");

						if (StringUtils.startsWith(accessLogDto.getResponsePayload(), "{") && StringUtils.endsWith(accessLogDto.getResponsePayload(), "}")) {
							buff.append(JsonUtils.getJsonPretty(accessLogDto.getResponsePayload()));
						} else {
							buff.append(accessLogDto.getResponsePayload());
						}
					} else {
						buff.append("-- BODY: null");
					}
				} catch (Exception e) {
					log.warn(e.getLocalizedMessage());
				}
			}

			buff.append("\n");
			buff.append("-------------------- RESPONSE END   --------------------");

			log.info(buff.toString());
		}
	}
}
