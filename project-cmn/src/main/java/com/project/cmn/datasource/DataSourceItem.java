package com.project.cmn.datasource;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * 하나의 DataSource 설정
 */
@Getter
@Setter
@ToString
public class DataSourceItem {
    /**
     * DataSource 이름. 필수
     */
    private String datasourceName;

    /**
     * JDBC 드라이버 클래스명. 필수
     */
    private String driverClassName;

    /**
     * JDBC URL. 필수
     */
    private String jdbcUrl;

    /**
     * 사용자명. 필수
     */
    private String username;

    /**
     * 비밀번호. 필수
     */
    private String password;

    /**
     * 연결 테스트 쿼리. 드라이버가 JDBC4를 지원하면 설정하지 않음
     */
    private String connectionTestQuery;

    /**
     * client가 pool로부터 connection을 얻기위해 기다리는 시간
     * 초단위. 기본 30초
     */
    private int connectionTimeout;

    /**
     * connection에 pool에서 idle 상태로 존재하는 시간
     * 초단위. 기본 10분. 최소 10초
     */
    private int idleTimeout;

    /**
     * 데이터베이스나 네트워크 인프라에 의해 타임아웃 상태가 되는 것을 방지하기 위해 설정
     * maxLifetime 값보다는 작아야 함.
     * 초단위. 기본 0(사용안함). 최소 30초
     */
    private int keepaliveTime;

    /**
     * 커넥션의 최대 유지 시간. 이 시간이 지난 커넥션 중에서 사용중인 커넥션은 종료된 이후에 풀에서 제거한다.
     * 초단위. 기본 30분. 최소 30초
     */
    private int maxLifetime;

    /**
     * pool에서 유지하려고 하는 idle connection의 최소 수
     * 기본은 maximumPoolSize에 설정값과 동일
     */
    private int minimumIdle;

    /**
     * pool에서 관리하는 connection의 최대 수(idle connection + in-use connection)
     * 기본 10개
     */
    private int maximumPoolSize;

    /**
     * 해당 시간이상 동안 connection을 pool에 반납하지 않는다면 connection 누수로 판단하고 로그를 출력함
     * 초단위. 기본 0(검출하지 않음). 최소 2초
     */
    private int leakDetectionThreshold;
}
