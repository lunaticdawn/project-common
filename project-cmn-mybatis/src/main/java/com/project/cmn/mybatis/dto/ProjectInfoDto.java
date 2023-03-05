package com.project.cmn.mybatis.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProjectInfoDto {
    /**
     * 프로젝트의 전체 경로
     */
    private String projectPath;

    /**
     * 기본 패키지
     */
    private String basePackage;

    /**
     * 테이블 스키마
     */
    private String tableSchema;

    /**
     * 테이블 이름
     */
    private String tableName;

    /**
     * 공백으로 변경할 테이블 접두어. ex) TB_
     */
    private String tablePrefixReplaceByBlank;

    /**
     * DTO가 생성될 기본 패키지 이후의 패키지
     */
    private String dtoPackage;

    /**
     * DTO의 접미어 ex) Dto
     */
    private String dtoPostfix;
}
