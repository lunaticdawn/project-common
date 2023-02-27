package com.project.cmn.http.configuration;

import com.project.cmn.mybatis.MyBatisConfig;
import com.project.cmn.mybatis.RegistryMyBatisMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@RequiredArgsConstructor
@Configuration
public class MyBatisConfiguration {
    /**
     * project.mybatis에 있는 설정을 바탕으로 {@link org.mybatis.spring.SqlSessionTemplate}과 {@link org.mybatis.spring.mapper.MapperScannerConfigurer}를 등록한다.
     *
     * @param env {@link Environment}
     * @return {@link RegistryMyBatisMapper}
     */
    @Bean
    @ConditionalOnProperty(prefix = "project.mybatis", value = "enabled", havingValue = "true")
    public RegistryMyBatisMapper createMyBatis(Environment env) {
        MyBatisConfig myBatisConfig = MyBatisConfig.init(env);

        return new RegistryMyBatisMapper(myBatisConfig);
    }
}
