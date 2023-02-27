package com.project.cmn.http.accesslog;

import com.project.cmn.http.WebCmnConstants;
import com.project.cmn.util.HostInfoUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;


/**
 * {@link HttpServletRequest}와 {@link HttpServletResponse}를 분석하여 접근로그에 대한 정보를 {@link AccessLogDto}에 담는다.
 */
@Slf4j
@RequiredArgsConstructor
public class AccessLog {
    private final AccessLogConfig accessLogConfig;

    private static final ThreadLocalAccessLog threadLocalAccessLog = new ThreadLocalAccessLog();

    private static class ThreadLocalAccessLog extends ThreadLocal<AccessLogDto> {
        @Override
        public AccessLogDto initialValue() {
            return new AccessLogDto();
        }
    }

    /**
     * ThreadLocal에서 {@link AccessLogDto}을 가져온다.
     *
     * @return {@link AccessLogDto}
     */
    public static AccessLogDto getAccessLogDto() {
        return threadLocalAccessLog.get();
    }

    /**
     * 최초 진입 시, {@link AccessLogDto}를 생성하고 시작시간을 저장한다.
     *
     * @param request {@link HttpServletRequest}
     */
    public void start(HttpServletRequest request) {
        String logKey = "LK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + RandomStringUtils.randomAlphanumeric(5);

        MDC.put(WebCmnConstants.LOG_KEY, logKey);

        AccessLogDto accessLogDto = threadLocalAccessLog.initialValue();

        accessLogDto.setLogKey(MDC.get(WebCmnConstants.LOG_KEY));
        accessLogDto.setStartTime(System.currentTimeMillis());
        accessLogDto.setStopWatch(new StopWatch(request.getRequestURI()));

        threadLocalAccessLog.set(accessLogDto);
    }

    /**
     * 종료 시 소요시간을 계산하고 {@link AccessLogDto}를 제거한다.
     *
     * @return {@link AccessLogDto}
     */
    public AccessLogDto end() {
        AccessLogDto accessLogDto = threadLocalAccessLog.get();

        accessLogDto.setDurationTime(System.currentTimeMillis() - accessLogDto.getStartTime());

        threadLocalAccessLog.remove();

        return accessLogDto;
    }

    /**
     * Request Header 정보를 {@link AccessLogDto}에 담는다.
     *
     * @param request {@link HttpServletRequest}
     */
    public void setRequestHeader(HttpServletRequest request) {
        Map<String, String> requestHeaders = new HashMap<>();
        Enumeration<String> headerNames = request.getHeaderNames();
        String headerName;

        while (headerNames.hasMoreElements()) {
            headerName = headerNames.nextElement();

            if (accessLogConfig.getRequestHeaderNames() != null && !accessLogConfig.getRequestHeaderNames().isEmpty()) {
                if (contains(accessLogConfig.getRequestHeaderNames(), headerName)) {
                    requestHeaders.put(headerName, request.getHeader(headerName));
                }
            } else {
                requestHeaders.put(headerName, request.getHeader(headerName));
            }
        }

        AccessLogDto accessLogDto = threadLocalAccessLog.get();

        accessLogDto.setRequestHeader(requestHeaders);
        accessLogDto.setRequestMethod(request.getMethod());
        accessLogDto.setClientAddr(request.getRemoteAddr());

        if (StringUtils.isNotBlank(HostInfoUtils.getInstance().getHostAddr())) {
            accessLogDto.setHostAddr(HostInfoUtils.getInstance().getHostAddr());
        } else {
            accessLogDto.setHostAddr(HostInfoUtils.getInstance().getHostName());
        }

        String uri = request.getRequestURI();
        String queryString = request.getQueryString();

        if (StringUtils.isNotBlank(queryString)) {
            uri = String.format("%s?%s", uri, queryString);
        }

        accessLogDto.setRequestUri(uri);
    }

    /**
     * Request Parameter 정보를 {@link AccessLogDto}에 담는다.
     *
     * @param request {@link HttpServletRequest}
     */
    public void setRequestParam(HttpServletRequest request) {
        if (StringUtils.indexOfIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE) >= 0) {
            return;
        }

        AccessLogDto accessLogDto = threadLocalAccessLog.get();

        if (request.getParameterMap() != null && !request.getParameterMap().isEmpty()) {
            String requestParam = request.getParameterMap().entrySet().stream().map(entry -> entry.getKey() + "=" + String.join(",", entry.getValue())).collect(Collectors.joining("&"));

            accessLogDto.setRequestParam(requestParam);
        }
    }

    /**
     * Json 형식의 Request Body 정보를 {@link AccessLogDto}에 담는다.
     *
     * @param request {@link HttpServletRequest}
     */
    public void setRequestBody(HttpServletRequest request) {
        if (StringUtils.indexOfIgnoreCase(request.getContentType(), MediaType.APPLICATION_JSON_VALUE) < 0) {
            return;
        }

        AccessLogDto accessLogDto = threadLocalAccessLog.get();

        if (request instanceof ContentCachingRequestWrapper wrapper) {
            byte[] buff = wrapper.getContentAsByteArray();

            if (buff.length > 0) {
                int length = buff.length;

                if (accessLogConfig.getRequestBodyLength() > 0) {
                    length = Math.min(length, accessLogConfig.getRequestBodyLength());
                }

                String payload = null;

                try {
                    payload = new String(buff, 0, length, wrapper.getCharacterEncoding());
                } catch (UnsupportedEncodingException e) {
                    log.error(e.getLocalizedMessage(), e);
                }

                accessLogDto.setRequestPayload(payload);
            }
        }
    }

    /**
     * Response Header 정보를 {@link AccessLogDto}에 담는다.
     *
     * @param response {@link HttpServletResponse}
     */
    public void setResponseHeader(HttpServletResponse response) {
        Map<String, String> responseHeaders = new HashMap<>();
        Collection<String> headerNames = response.getHeaderNames();

        for (String headerName : headerNames) {
            if (accessLogConfig.getResponseHeaderNames() != null && !accessLogConfig.getResponseHeaderNames().isEmpty()) {
                if (contains(accessLogConfig.getResponseHeaderNames(), headerName)) {
                    responseHeaders.put(headerName, response.getHeader(headerName));
                }
            } else {
                responseHeaders.put(headerName, response.getHeader(headerName));
            }
        }

        threadLocalAccessLog.get().setResponseHeader(responseHeaders);
    }

    /**
     * Response Body 정보를 {@link AccessLogDto}에 담는다.
     *
     * @param response {@link HttpServletResponse}
     * @throws UnsupportedEncodingException {@link UnsupportedEncodingException}. {@link String} 객체 생성 시, Character Encoding을 지정하는 경우 발생
     */
    public void setResponseBody(HttpServletResponse response) throws UnsupportedEncodingException {
        AccessLogDto accessLogDto = threadLocalAccessLog.get();

        byte[] content;

        if (response instanceof ContentCachingResponseWrapper wrapper) {
            accessLogDto.setHttpStatus(wrapper.getStatus());

            content = wrapper.getContentAsByteArray();

            if (content.length > 0) {
                int length = content.length;

                if (accessLogConfig.getResponseBodyLength() > 0) {
                    length = Math.min(length, accessLogConfig.getResponseBodyLength());
                }

                String responsePayload = new String(content, 0, length, wrapper.getCharacterEncoding());

                responsePayload = StringUtils.trim(responsePayload);

                if (StringUtils.startsWith(responsePayload, "<")) {
                    accessLogDto.setResponsePayload("[HTML]");
                } else {
                    accessLogDto.setResponsePayload(responsePayload);
                }
            }
        }
    }

    /**
     * 저장할 Http Header Name인지 판단한다. 대소문자를 구별하지 않는다.
     *
     * @param headerNames 저장할 Http Header Name 배열
     * @param toFind      찾을 Http Header Name
     * @return true - 저장, false - 저장하지 않음
     */
    private boolean contains(List<String> headerNames, String toFind) {
        boolean isContain = false;

        if (headerNames == null || headerNames.isEmpty()) {
            isContain = true;
        } else {
            for (String str : headerNames) {
                if (StringUtils.equalsIgnoreCase(str, toFind)) {
                    isContain = true;

                    break;
                }
            }
        }

        return isContain;
    }
}
