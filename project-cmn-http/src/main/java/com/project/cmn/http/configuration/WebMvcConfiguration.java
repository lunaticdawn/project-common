package com.project.cmn.http.configuration;

import com.project.cmn.http.accesslog.AccessLog;
import com.project.cmn.http.accesslog.AccessLogConfig;
import com.project.cmn.http.accesslog.AccessLogInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 웹 프로젝트 용 설정들
 */
@Slf4j
@RequiredArgsConstructor
@EnableWebMvc
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final AccessLogConfig accessLogConfig;

    /**
     * Interceptor 설정
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        if (accessLogConfig.isEnabled()) {
            AccessLog accessLog = new AccessLog(accessLogConfig);

            registry.addInterceptor(new AccessLogInterceptor(accessLogConfig, accessLog)).addPathPatterns(accessLogConfig.getExcludePathPatterns());
        }
    }
}
