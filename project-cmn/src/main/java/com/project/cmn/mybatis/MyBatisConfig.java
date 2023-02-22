package com.project.cmn.mybatis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * MyBatis 설정들
 */
@Getter
@Setter
@ToString
@Component
@ConfigurationProperties(prefix = "project.mybatis")
public class MyBatisConfig {
    /**
     * project.mybatis 설정 사용 여부
     */
    private boolean enabled;

    /**
     * MyBatis 설정들
     */
    private List<MyBatisItem> mybatisItemList;
}
