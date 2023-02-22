package com.project.cmn.http.configuration;

import com.project.cmn.datasource.CreateDataSources;
import com.project.cmn.datasource.DataSourcesConfig;
import com.project.cmn.http.accesslog.AccessLog;
import com.project.cmn.http.accesslog.AccessLogAspect;
import com.project.cmn.http.accesslog.AccessLogConfig;
import com.project.cmn.http.accesslog.AccessLogInterceptor;
import com.project.cmn.mybatis.CreateMyBatis;
import com.project.cmn.mybatis.MyBatisConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
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
    private final ConfigurableBeanFactory configurableBeanFactory;
    private final DataSourcesConfig dataSourcesConfig;
    private final MyBatisConfig myBatisConfig;

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
     * @return
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

    /**
     * Datasource를 동적으로 생성
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "project.datasources", value = "enabled", havingValue = "true")
    public CreateDataSources createDataSources() {
        log.debug("# Create CreateDataSources");
        return new CreateDataSources(dataSourcesConfig, configurableBeanFactory);
    }

    /**
     * MyBatis 설정을 동적으로 생성
     *
     * @return
     */
    @Bean
    @ConditionalOnProperty(prefix = "project.mybatis", value = "enabled", havingValue = "true")
    public CreateMyBatis createMyBatis() {
        log.debug("# Create CreateMyBatis");
        return new CreateMyBatis(myBatisConfig, configurableBeanFactory);
    }
}
