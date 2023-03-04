package com.project.cmn.http.accesslog;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Slf4j
@RequiredArgsConstructor
@Configuration
@ConditionalOnBean(name = "accessLog")
public class RegistryAccessLogInterceptor implements WebMvcConfigurer {
    private final AccessLog accessLog;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.debug("# addInterceptors");
        registry.addInterceptor(new AccessLogInterceptor(accessLog)).addPathPatterns(accessLog.getAccessLogConfig().getExcludePathPatterns());
    }
}
