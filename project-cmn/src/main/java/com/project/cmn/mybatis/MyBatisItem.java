package com.project.cmn.mybatis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * {@link org.mybatis.spring.SqlSessionTemplate}과 {@link org.mybatis.spring.mapper.MapperScannerConfigurer}를 위한 설정들
 * #project.mybatis.item-list
 */
@Getter
@Setter
@ToString
@ConfigurationProperties(prefix = "project.mybatis.item-list")
public class MyBatisItem {
    /**
     * 설정 사용 여부
     * #project.mybatis.item-list.enabled
     */
    private boolean enabled;

    /**
     * &#064;Primary  선언 여부
     * #project.mybatis.item-list.primary
     */
    private boolean primary;

    /**
     * 사용할 DataSource 명. {@link org.mybatis.spring.SqlSessionFactoryBean}의 dataSource
     * #project.mybatis.item-list.datasource-name
     */
    private String datasourceName;

    /**
     * {@link org.mybatis.spring.SqlSessionTemplate}의 이름
     * #project.mybatis.item-list.sql-session-template-name
     */
    private String sqlSessionTemplateName;

    /**
     * MyBatis 설정 파일 위치. {@link org.mybatis.spring.SqlSessionFactoryBean}의 configLocation
     * #project.mybatis.item-list.config-location
     */
    private String configLocation;

    /**
     * Mapper 클래스의 패키지 경로. {@link org.mybatis.spring.mapper.MapperScannerConfigurer}의 basePackage
     * #project.mybatis.item-list.mapper-base-package
     */
    private String mapperBasePackage;

    /**
     * Mapper 로 등록할 Annotation Class 이름. {@link org.mybatis.spring.mapper.MapperScannerConfigurer}의 annotationClass
     * #project.mybatis.item-list.annotation-class-name
     */
    private String annotationClassName = "org.apache.ibatis.annotations.Mapper";

    /**
     * 쿼리 XML 파일의 위치들. {@link org.mybatis.spring.SqlSessionFactoryBean}의 mapperLocations
     * #project.mybatis.item-list.mapper-locations
     */
    private List<String> mapperLocations;

    /**
     * ParameterType, ResultType 으로 사용할 클래스들이 있는 패키지들. {@link org.mybatis.spring.SqlSessionFactoryBean}의 typeAliasesPackage
     * #project.mybatis.item-list.typeAliasesPackages
     */
    private List<String> typeAliasesPackages;
}
