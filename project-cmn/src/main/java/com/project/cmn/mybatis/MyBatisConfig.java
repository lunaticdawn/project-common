package com.project.cmn.mybatis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * MyBatis 설정들
 * #project.mybatis
 */
@Getter
@Setter
@ToString
public class MyBatisConfig {
    /**
     * {@link Environment}에서 project.mybatis 설정을 가져와 {@link MyBatisConfig}로 변환한다.
     *
     * @param environment {@link Environment}
     * @return {@link MyBatisConfig}
     */
    public static MyBatisConfig init(Environment environment) {
        return Binder.get(environment).bindOrCreate("project.mybatis", MyBatisConfig.class);
    }

    /**
     * project.mybatis 설정 사용여부
     * #project.mybatis.enable
     */
    private boolean enable;

    /**
     * {@link org.mybatis.spring.SqlSessionTemplate}과 {@link org.mybatis.spring.mapper.MapperScannerConfigurer}를 위한 설정들
     * #project.mybatis.item-list
     */
    private List<MyBatisItem> itemList;
}
