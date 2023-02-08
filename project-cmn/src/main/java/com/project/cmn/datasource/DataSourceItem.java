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
	 * DataSource 이름
	 */
	private String datasourceName;

	/**
	 * JDBC 드라이버 클래스명
	 */
	private String driverClassName;

	/**
	 * JDBC URL
	 */
	private String jdbcUrl;

	/**
	 * 사용자명
	 */
	private String username;

	/**
	 * 비밀번호
	 */
	private String password;

	/**
	 * 연결 테스트 쿼리
	 */
	private String connectionTestQuery;
}
