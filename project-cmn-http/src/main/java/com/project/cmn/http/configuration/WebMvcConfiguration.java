package com.project.cmn.http.configuration;

import com.project.cmn.http.accesslog.AccessLog;
import com.project.cmn.http.accesslog.AccessLogAspect;
import com.project.cmn.http.accesslog.AccessLogConfig;
import com.project.cmn.http.accesslog.AccessLogInterceptor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
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
@Configuration
@EnableWebMvc
public class WebMvcConfiguration implements WebMvcConfigurer {
    private final AccessLogConfig accessLogConfig;

    /**
     * Access Log를 위한 AOP 생성
     *
     * @return {@link AccessLogAspect}
     */
    @Bean
    @ConditionalOnClass(AccessLog.class)
    public AccessLogAspect accessLogAspect() {
        log.debug("# Create AccessLogAspect");
        return new AccessLogAspect();
    }

    /**
     * Interceptor 설정
     */
    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        if (accessLogConfig.isEnabled()) {
            registry.addInterceptor(new AccessLogInterceptor(new AccessLog(accessLogConfig), accessLogConfig)).addPathPatterns(accessLogConfig.getExcludePathPatterns());
        }
    }
}
