package com.project.cmn.http.configuration;

import com.project.cmn.datasource.DataSourcesConfig;
import com.project.cmn.datasource.RegistryDataSources;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
@Configuration
public class DataSourceConfiguration {
    /**
     * project.datasources에 있는 설정을 바탕으로 DataSource를 등록한다.
     *
     * @param env {@link Environment}
     * @return {@link RegistryDataSources}
     */
    @Bean
    @ConditionalOnProperty(prefix = "project.datasources", value = "enabled", havingValue = "true")
    public RegistryDataSources createDataSources(Environment env) {
        DataSourcesConfig dataSourcesConfig = DataSourcesConfig.init(env);

        return new RegistryDataSources(dataSourcesConfig);
    }
}
