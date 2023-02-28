package com.project.cmn.http.configuration;

import com.project.cmn.datasource.DataSourceConfig;
import com.project.cmn.datasource.RegistryDataSource;
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
     * @return {@link RegistryDataSource}
     */
    @Bean
    @ConditionalOnProperty(prefix = "project.datasource", value = "enabled", havingValue = "true")
    public RegistryDataSource createDataSources(Environment env) {
        DataSourceConfig dataSourceConfig = DataSourceConfig.init(env);

        return new RegistryDataSource(dataSourceConfig);
    }
}
