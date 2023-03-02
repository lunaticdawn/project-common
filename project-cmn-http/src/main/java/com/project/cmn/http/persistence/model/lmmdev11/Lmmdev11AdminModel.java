package com.project.cmn.http.persistence.model.lmmdev11;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * DL_ADMIN 괸리자
 */
@Getter
@Setter
@ToString
public class Lmmdev11AdminModel {
    /**
     * 관리자NO
     */
    private Long adminNo;

    /**
     * 지점ID
     */
    private Long branchId;

    /**
     * 관리자ID
     */
    private String adminId;

    /**
     * 비밀번호
     */
    private String passwd;

    /**
     * 이름
     */
    private String adminNm;

    /**
     * 휴대전화번호
     */
    private String hpNum;

    /**
     * 관리자역할ID
     */
    private String adminRoleId;

    /**
     * 최종로그인일시
     */
    private LocalDateTime lastLoginDt;

    /**
     * 비밀번호초기화여부
     */
    private String pwdInitYn;

    /**
     * 비밀번호수정일시
     */
    private LocalDateTime pwdModDt;

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
    private String isValid;

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
    private LocalDateTime modDt;

    /**
     * 수정자
     */
    private String modId;
}