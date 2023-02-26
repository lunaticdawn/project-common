package com.project.cmn.mybatis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.bind.Binder;
import org.springframework.core.env.Environment;

import java.util.List;

/**
 * MyBatis 설정들
 */
@Getter
@Setter
@ToString
public class MyBatisConfig {
    public static MyBatisConfig init(Environment env) {
        return Binder.get(env).bind("project.mybatis", MyBatisConfig.class).get();
    }

    /**
     * project.mybatis 설정 사용 여부
     */
    private boolean enabled;

    /**
     * MyBatis 설정들
     */
    private List<MyBatisItem> mybatisItemList;
}
