package com.project.cmn.http.configuration;

import com.project.cmn.http.accesslog.AccessLog;
import com.project.cmn.http.accesslog.AccessLogAspect;
import com.project.cmn.http.accesslog.AccessLogConfig;
import com.project.cmn.http.accesslog.AccessLogInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 웹 프로젝트 용 설정들
 */
@Slf4j
@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final AccessLogConfig accessLogConfig;

    /**
     * {@link AccessLog} 생성
     *
     * @return {@link AccessLog}
     */
    @Bean
    @ConditionalOnProperty(prefix = "project.access.log", value = "enabled", havingValue = "true")
    public AccessLog accessLog() {
        log.debug("# Create AccessLog");
        return new AccessLog();
    }

    /**
     * Access Log를 위한 AOP 생성
     *
     * @return {@link AccessLogAspect}
     */
    @Bean
    @ConditionalOnProperty(prefix = "project.access.log", value = "enabled", havingValue = "true")
    public AccessLogAspect accessLogAspect() {
        log.debug("# Create AccessLogAspect");
        return new AccessLogAspect();
    }

    /**
     * Interceptor 설정
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        if (accessLogConfig.isEnabled()) {
            registry.addInterceptor(new AccessLogInterceptor(accessLog(), accessLogConfig)).addPathPatterns(accessLogConfig.getExcludePathPatterns());
        }
    }
}
