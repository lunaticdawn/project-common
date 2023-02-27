package com.project.cmn.mybatis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * 하나의 MyBatis 설정
 */
@Getter
@Setter
@ToString
public class MyBatisItem {
    /**
     * 설정 사용 여부
     */
    private boolean enabled;

    /**
     * 사용할 DataSource 명
     */
    private String datasourceName;

    /**
     * SqlSessionTemplate 명
     */
    private String sqlSessionTemplateName;

    /**
     * MyBatis 설정 파일 위치
     */
    private String configLocation;

    /**
     * Mapper 클래스의 패키지 경로
     */
    private String mapperPackage;

    /**
     * 쿼리 XML 파일의 위치들
     */
    private List<String> mapperLocations;

    /**
     * ParameterType, ResultType 으로 사용할 클래스들이 있는 패키지들
     */
    private List<String> typeAliasesPackages;
}
