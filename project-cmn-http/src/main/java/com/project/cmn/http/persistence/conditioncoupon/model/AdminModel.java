package com.project.cmn.http.persistence.conditioncoupon.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * 관리자
 *
 * @TableName tb_admin
 */
@Getter
@Setter
public class AdminModel {
    /**
     * 관리자NUM
     */
    private String adminNum;

    /**
     * 관리자아이디
     */
    private String adminId;

    /**
     * 비밀번호
     */
    private String passwd;

    /**
     * 관리자이름
     */
    private String adminNm;

    /**
     * 권한ID
     */
    private String roleId;

    /**
     * 사원번호
     */
    private String empNo;

    /**
     * 이메일
     */
    private String email;

    /**
     * 전화번호
     */
    private String tel;

    /**
     * 휴대폰번호
     */
    private String hpNum;

    /**
     * 최종로그인일시
     */
    private LocalDateTime lastLoginDt;

    /**
     * 비밀번호초기화여부
     */
    private String pwdInitYn;

    /**
     * 비밀번호 수정일시
     */
    private LocalDateTime pwdUpdDt;

    /**
     * 로그인실패수
     */
    private Integer loginFailCnt;

    /**
     * 로그인실패일시
     */
    private LocalDateTime loginFailDt;

    /**
     * 유효여부
     */
    private String validYn;

    /**
     * 등록일시
     */
    private LocalDateTime regDt;

    /**
     * 등록자
     */
    private String regId;

    /**
     * 수정일시
     */
    private LocalDateTime updDt;

    /**
     * 수정자
     */
    private String updId;
}