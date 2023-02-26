package com.project.cmn.http.configuration;

import com.project.cmn.datasource.DataSourcesConfig;
import com.project.cmn.mybatis.MyBatisConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Slf4j
@Configuration
public class MapperConfig {
    @Bean
    @ConditionalOnProperty(prefix = "project.datasources", value = "enabled", havingValue = "true")
    public MapperBeanPostProcessor mapperBeanPostProcessor(Environment env) {
        log.debug("# Create MapperBeanPostProcessor");
        try {
            DataSourcesConfig dataSourcesConfig = DataSourcesConfig.init(env);
            MyBatisConfig myBatisConfig = MyBatisConfig.init(env);

            return new MapperBeanPostProcessor(dataSourcesConfig, myBatisConfig);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }

        return null;
    }
}
