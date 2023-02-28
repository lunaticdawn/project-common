package com.project.cmn.http.persistence.model.lmmdev11;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

/**
 * 괸리자
 *
 * @TableName DL_ADMIN
 */
@Getter
@Setter
@ToString
public class Lmmdev11AdminModel {
    /**
     * 관리자NO
     */
    private Long ADMIN_NO;

    /**
     * 지점ID
     */
    private Long BRANCH_ID;

    /**
     * 관리자ID
     */
    private String ADMIN_ID;

    /**
     * 비밀번호
     */
    private String PASSWD;

    /**
     * 이름
     */
    private String ADMIN_NM;

    /**
     * 휴대전화번호
     */
    private String HP_NUM;

    /**
     * 관리자역할ID
     */
    private String ADMIN_ROLE_ID;

    /**
     * 최종로그인일시
     */
    private LocalDateTime LAST_LOGIN_DT;

    /**
     * 비밀번호초기화여부
     */
    private String PWD_INIT_YN;

    /**
     * 비밀번호수정일시
     */
    private LocalDateTime PWD_MOD_DT;

    /**
     * 로그인실패수
     */
    private Integer LOGIN_FAIL_CNT;

    /**
     * 로그인실패일시
     */
    private LocalDateTime LOGIN_FAIL_DT;

    /**
     * 유효여부
     */
    private String IS_VALID;

    /**
     * 등록일시
     */
    private LocalDateTime REG_DT;

    /**
     * 등록자
     */
    private String REG_ID;

    /**
     * 수정일시
     */
    private LocalDateTime MOD_DT;

    /**
     * 수정자
     */
    private String MOD_ID;
}