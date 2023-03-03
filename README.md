# project-common
Springboot 프로젝트 생성 시 참조할 공통 모듈

## DataSource 설정에 대해
1. 연결해야 할 DataSource가 1개인 경우에는 Spring Boot의 application.yml 설정을 통한 AutoConfiguration으로 충분
2. 연결해야 할 DataSource가 2개 이상인 경우에는 별도의 DataSource 연결과 Transaction 설정이 필요
    - DataSource, SqlSessionFactory, SqlSessionTemplate, DataSourceTransactionManager를 각각의 DataSource 별로 등록하는 방법
        => 일반적인 방법. @Configuration과 @Bean을 이용하여 DataSource, SqlSessionFactory, SqlSessionTemplate, DataSourceTransactionManager를 직접 생성
    - BeanDefinitionRegistryPostProcessor를 이용하여 Bean으로 등록하는 방법
        => commit된 소스가 사용하는 방법. application.yml에 DataSourceAutoConfiguration, DataSourceTransactionManagerAutoConfiguration,
        XADataSourceAutoConfiguration를 제외
3. 연결해야 할 DataSource가 2개 이상이고 같은 Transaction에 묶여야 한다면 XADataSource를 이용하고, 라이브러리에 spring-boot-starter-jta-atomikos를 포함하면
자동으로 JtaTransactionManager를 설정해 준다. - Atomikos AutoConfiguration
4. SpringBoot 3.x(SpringFramework 6.x) 부터 Transaction 패키지가 javax에서 jakarta로 변경되었고 Atomikos는 아직 변경 전이다.
따라서 Atomikos를 사용하려면 SpringBoot 2.x(SpringFramework 5.x)를 사용해야 한다.

### DataSource 설정용 properties
<pre>
<code>
{
  "groups": [
    {
      "name": "project.datasource",
      "type": "com.project.cmn.datasource.DataSourceConfig",
      "sourceType": "com.project.cmn.datasource.DataSourceConfig"
    }
  ],
  "properties": [
    {
      "name": "project.datasource.enabled",
      "type": "java.lang.Boolean",
      "description": "project.datasource 설정 사용여부 #project.datasource.enabled",
      "sourceType": "com.project.cmn.datasource.DataSourceConfig",
      "defaultValue": false
    },
    {
      "name": "project.datasource.item-list",
      "type": "java.util.List<com.project.cmn.datasource.DataSourceItem>",
      "description": "여러 개의 {@link com.zaxxer.hikari.HikariDataSource} 생성에 필요한 설정들 #project.datasource.item-list",
      "sourceType": "com.project.cmn.datasource.DataSourceConfig"
    },
    {
      "name": "project.datasource.item-list.connection-test-query",
      "type": "java.lang.String",
      "description": "연결 테스트 쿼리. 드라이버가 JDBC4를 지원하면 설정하지 않음 #project.datasource.item-list.connection-test-query",
      "sourceType": "com.project.cmn.datasource.DataSourceItem"
    },
    {
      "name": "project.datasource.item-list.connection-timeout",
      "type": "java.lang.Integer",
      "description": "client가 pool로부터 connection을 얻기위해 기다리는 시간 초단위. 기본 30초 #project.datasource.item-list.connection-timeout",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": 0
    },
    {
      "name": "project.datasource.item-list.datasource-name",
      "type": "java.lang.String",
      "description": "DataSource 이름. 필수 해당 이름을 pool name 으로 사용 #project.datasource.item-list.datasource-name",
      "sourceType": "com.project.cmn.datasource.DataSourceItem"
    },
    {
      "name": "project.datasource.item-list.driver-class-name",
      "type": "java.lang.String",
      "description": "JDBC 드라이버 클래스명. 필수 #project.datasource.item-list.driver-class-name",
      "sourceType": "com.project.cmn.datasource.DataSourceItem"
    },
    {
      "name": "project.datasource.item-list.enabled",
      "type": "java.lang.Boolean",
      "description": "DataSource 사용여부 #project.datasource.item-list.enabled",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": false
    },
    {
      "name": "project.datasource.item-list.idle-timeout",
      "type": "java.lang.Integer",
      "description": "connection에 pool에서 idle 상태로 존재하는 시간 초단위. 기본 10분. 최소 10초 #project.datasource.item-list.idle-timeout",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": 0
    },
    {
      "name": "project.datasource.item-list.keepalive-time",
      "type": "java.lang.Integer",
      "description": "데이터베이스나 네트워크 인프라에 의해 타임아웃 상태가 되는 것을 방지하기 위해 설정 maxLifetime 값보다는 작아야 함. 초단위. 기본 0(사용안함). 최소 30초 #project.datasource.item-list.keepalive-time",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": 0
    },
    {
      "name": "project.datasource.item-list.lazy-connection",
      "type": "java.lang.Boolean",
      "description": "{@link org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy} 사용여부 #project.datasource.item-list.lazy-connection",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": false
    },
    {
      "name": "project.datasource.item-list.leak-detection-threshold",
      "type": "java.lang.Integer",
      "description": "해당 시간이상 동안 connection을 pool에 반납하지 않는다면 connection 누수로 판단하고 로그를 출력함 초단위. 기본 0(검출하지 않음). 최소 2초 #project.datasource.item-list.leak-detection-threshold",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": 0
    },
    {
      "name": "project.datasource.item-list.max-lifetime",
      "type": "java.lang.Integer",
      "description": "커넥션의 최대 유지 시간. 이 시간이 지난 커넥션 중에서 사용중인 커넥션은 종료된 이후에 풀에서 제거한다. 초단위. 기본 30분. 최소 30초 #project.datasource.item-list.max-lifetime",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": 0
    },
    {
      "name": "project.datasource.item-list.maximum-pool-size",
      "type": "java.lang.Integer",
      "description": "pool에서 관리하는 connection의 최대 수(idle connection + in-use connection) 기본 10개 #project.datasource.item-list.maximum-pool-size",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": 0
    },
    {
      "name": "project.datasource.item-list.minimum-idle",
      "type": "java.lang.Integer",
      "description": "pool에서 유지하려고 하는 idle connection의 최소 수 기본은 maximumPoolSize에 설정값과 동일 #project.datasource.item-list.minimum-idle",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": 0
    },
    {
      "name": "project.datasource.item-list.password",
      "type": "java.lang.String",
      "description": "Datbase 의 비밀번호. 필수 #project.datasource.item-list.password",
      "sourceType": "com.project.cmn.datasource.DataSourceItem"
    },
    {
      "name": "project.datasource.item-list.primary",
      "type": "java.lang.Boolean",
      "description": "&#064;Primary 선언 여부 #project.datasource.item-list.primary",
      "sourceType": "com.project.cmn.datasource.DataSourceItem",
      "defaultValue": false
    },
    {
      "name": "project.datasource.item-list.transaction-name",
      "type": "java.lang.String",
      "description": "Transaction 이름. 옵션. 없으면 생성안함 #project.datasource.item-list.transaction-name",
      "sourceType": "com.project.cmn.datasource.DataSourceItem"
    },
    {
      "name": "project.datasource.item-list.url",
      "type": "java.lang.String",
      "description": "Database 의 JDBC URL. 필수 #project.datasource.item-list.url",
      "sourceType": "com.project.cmn.datasource.DataSourceItem"
    },
    {
      "name": "project.datasource.item-list.user",
      "type": "java.lang.String",
      "description": "Database 의 사용자명. 필수 #project.datasource.item-list.user",
      "sourceType": "com.project.cmn.datasource.DataSourceItem"
    }
  ],
  "hints": []
}
</code>
</pre>
