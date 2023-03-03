package com.project.cmn.http.accesslog;


import com.project.cmn.http.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.util.StopWatch;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * {@link HttpServletRequest}와 {@link HttpServletResponse}를 분석하여 접근로그에 대한 정보를 {@link AccessLogDto}에 담고, 로깅한다.
 */
@Slf4j
@RequiredArgsConstructor
public class AccessLogInterceptor implements HandlerInterceptor {
    private final AccessLogConfig accessLogConfig;
    private final AccessLog accessLog;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) {
        accessLog.start(request);
        accessLog.setRequestHeader(request);
        accessLog.setRequestParam(request);

        this.logRequestHeader(AccessLog.getAccessLogDto());

        return true;
    }

    @Override
    public void afterCompletion(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler, @Nullable Exception ex) throws Exception {
        accessLog.setRequestBody(request);
        accessLog.setResponseHeader(response);
        accessLog.setResponseBody(response);

        AccessLogDto accessLogDto = accessLog.end();

        this.logRequestBody(accessLogDto);
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

        MDC.clear();
    }

    /**
     * {@link HttpServletRequest}의 Header 정보를 바탕으로 한 요청 로그를 출력한다.
     *
     * @param accessLogDto {@link AccessLogDto}
     */
    private void logRequestHeader(AccessLogDto accessLogDto) {
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

    /**
     * {@link HttpServletRequest}의 Body 정보를 출력한다.
     * Request의 Content-Type이 application/json인 경우에만 Body를 저장
     *
     * @param accessLogDto {@link AccessLogDto}
     */
    private void logRequestBody(AccessLogDto accessLogDto) {
        if (accessLogConfig.isRequestBody()
                && StringUtils.startsWith(accessLogDto.getRequestHeader().get("content-type"), MediaType.APPLICATION_JSON_VALUE)) {
            StringBuilder buff = new StringBuilder();

            buff.append("\n");
            buff.append("-------------------- REQUEST BODY START --------------------");
            buff.append("\n");

            if (StringUtils.isNotBlank(accessLogDto.getRequestPayload())) {
                try {
                    buff.append(JsonUtils.getJsonPretty(accessLogDto.getRequestPayload()));
                } catch (IOException e) {
                    buff.append(accessLogDto.getRequestPayload());
                }
            } else {
                buff.append("Request content is null or missing.");
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
                if (StringUtils.isNotBlank(accessLogDto.getResponsePayload())) {
                    try {
                        buff.append(JsonUtils.getJsonPretty(accessLogDto.getResponsePayload()));
                    } catch (IOException e) {
                        buff.append(accessLogDto.getResponsePayload());
                    }
                } else {
                    buff.append("-- BODY: Unhandled response.");
                }
            }

            buff.append("\n");
            buff.append("-------------------- RESPONSE END   --------------------");

            log.info(buff.toString());
        }
    }
}
