package com.project.cmn.http.accesslog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.MDC;
import org.springframework.http.MediaType;
import org.springframework.util.StopWatch;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import com.project.cmn.http.WebCmnConstants;
import com.project.cmn.util.HostInfoUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


/**
 * {@link HttpServletRequest}와 {@link HttpServletResponse}를 분석하여 접근로그에 대한 정보를 {@link AccessLogDto}에 담는다.
 */
@Slf4j
public class AccessLog {
	private AccessLogConfig accessLogConfig;

	private static ThreadLocalAccessLog threadLocalAccessLog = new ThreadLocalAccessLog();

	private static class ThreadLocalAccessLog extends ThreadLocal<AccessLogDto> {
		@Override
		public AccessLogDto initialValue() {
			return new AccessLogDto();
		}

		public AccessLogDto getDto() {
			return super.get();
		}

		@Override
		public void remove() {
			super.remove();
		}
	}

	/**
	 * ThreadLocal에서 {@link AccessLogVo}을 가져온다.
	 *
	 * @return {@link AccessLogVo}
	 */
	public static AccessLogDto getAccessLogDto() {
		return threadLocalAccessLog.getDto();
	}

	/**
	 * 최초 진입 시, {@link AccessLogDto}를 생성하고 시작시간을 저장한다.
	 *
	 * @param request {@link HttpServletRequest}
	 */
	public void start(HttpServletRequest request, AccessLogConfig accessLogConfig) {
		this.accessLogConfig = accessLogConfig;

		AccessLogDto accessLogDto = threadLocalAccessLog.initialValue();

		accessLogDto.setLogKey(MDC.get(WebCmnConstants.LOG_KEY));
		accessLogDto.setStartTime(System.currentTimeMillis());
		accessLogDto.setStopWatch(new StopWatch(request.getRequestURI()));

		threadLocalAccessLog.set(accessLogDto);
	}

	/**
	 * 종료 시 소요시간을 계산하고 {@link AccessLogDto}를 제거한다.
	 *
	 * @return {@link AccessLogVo}
	 */
	public AccessLogDto end() {
		AccessLogDto accessLogDto = threadLocalAccessLog.getDto();

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
		String headerName = null;

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

		AccessLogDto accessLogDto = threadLocalAccessLog.getDto();

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

		AccessLogDto accessLogDto = threadLocalAccessLog.getDto();

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

		AccessLogDto accessLogDto = threadLocalAccessLog.getDto();

		if (request instanceof ContentCachingRequestWrapper) {
			ContentCachingRequestWrapper wrapper = (ContentCachingRequestWrapper) request;
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

		threadLocalAccessLog.getDto().setResponseHeader(responseHeaders);
	}

	/**
	 * Response Body 정보를 {@link AccessLogDto}에 담는다.
	 *
	 * @param response {@link HttpServletResponse}
	 * @throws IOException
	 */
	public void setResponseBody(HttpServletResponse response) throws IOException {
		AccessLogDto accessLogDto = threadLocalAccessLog.getDto();

		byte[] content = null;

		if (response instanceof ContentCachingResponseWrapper) {
			ContentCachingResponseWrapper wrapper = (ContentCachingResponseWrapper) response;

			accessLogDto.setHttpStatus(wrapper.getStatus());

			content = wrapper.getContentAsByteArray();

			if (content != null && content.length > 0) {
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
	 * @param toFind 찾을 Http Header Name
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
