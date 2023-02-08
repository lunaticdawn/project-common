package com.project.cmn.http.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.project.cmn.datasource.DataSources;
import com.project.cmn.datasource.DataSourcesConfig;
import com.project.cmn.http.accesslog.AccessLog;
import com.project.cmn.http.accesslog.AccessLogAspect;
import com.project.cmn.http.accesslog.AccessLogConfig;
import com.project.cmn.http.accesslog.AccessLogInterceptor;

import lombok.extern.slf4j.Slf4j;

/**
 * 웹 프로젝트 용 설정들
 *
 */
@Slf4j
@Configuration
@EnableWebMvc
@EnableConfigurationProperties
public class WebMvcConfiguration implements WebMvcConfigurer {
	@Autowired
	private ConfigurableBeanFactory configurableBeanFactory;

	@Value("${project.access.log.enabled}")
	private boolean accessLogEnabled;

	/**
	 * Access Log 설정을 가져옴
	 *
	 * @return {@link AccessLogConfig}
	 */
	@Bean
	@ConditionalOnProperty(prefix = "project.access.log", value = "enabled", havingValue = "true")
	public AccessLogConfig getAccessLogConfig() {
		log.debug("# Create AccessLogConfig");
		return new AccessLogConfig();
	}

	/**
	 * {@link AccessLog} 생성
	 *
	 * @return {@link AccessLog}
	 */
	@Bean
	@ConditionalOnProperty(prefix = "project.access.log", value = "enabled", havingValue = "true")
	public AccessLog getAccessLog() {
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
	public AccessLogAspect getAccessLogAspect() {
		log.debug("# Create AccessLogAspect");
		return new AccessLogAspect();
	}

	/**
	 * Interceptor 설정
	 */
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		if (accessLogEnabled) {
			registry.addInterceptor(new AccessLogInterceptor(getAccessLog(), getAccessLogConfig())).addPathPatterns(getAccessLogConfig().getExcludePathPatterns());
		}
	}

	/**
	 * Datasource에 대한 설정을 가져옴
	 *
	 * @return {@link DataSourcesConfig}
	 */
	@Bean
	@ConditionalOnProperty(prefix = "project.datasources", value = "enabled", havingValue = "true")
	public DataSourcesConfig getDataSourcesConfig() {
		log.debug("# Create DataSourcesConfig");
		return new DataSourcesConfig();
	}

	/**
	 * Datasource를 동적으로 생성
	 *
	 * @return
	 */
	@Bean
	@ConditionalOnProperty(prefix = "project.datasources", value = "enabled", havingValue = "true")
	public DataSources getDataSources() {
		log.debug("# Create DataSources");
		return new DataSources(configurableBeanFactory);
	}
}
