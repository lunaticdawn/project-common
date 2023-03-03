# project-common
Springboot 프로젝트 생성 시 참조할 공통 모듈
    - Springboot + MyBatis

## DataSource 설정에 대해
1. 연결해야 할 DataSource가 1개인 경우에는 Spring Boot의 application.yml 설정을 통한 AutoConfiguration으로 충분
2. 연결해야 할 DataSource가 2개 이상인 경우에는 별도의 DataSource 연결과 Transaction 설정이 필요
    - DataSource, SqlSessionFactory, SqlSessionTemplate, DataSourceTransactionManager를 각각의 DataSource 별로 등록하는 방법
        - 일반적인 방법.
        - @Configuration과 @Bean을 이용하여 DataSource, SqlSessionFactory, SqlSessionTemplate, DataSourceTransactionManager를 직접 생성
    - BeanDefinitionRegistryPostProcessor를 이용하여 Bean으로 등록하는 방법
        - commit된 소스가 사용하는 방법.
        - application.yml에서 다음 AutoConfiguration은 제외
            - org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
            - org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration
            - org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration
3. 연결해야 할 DataSource가 2개 이상이고 같은 Transaction에 묶여야 한다면 XADataSource를 이용하고, 라이브러리에 spring-boot-starter-jta-atomikos를 포함하면
자동으로 JtaTransactionManager를 설정해 준다. - Atomikos AutoConfiguration
4. SpringBoot 3.x(SpringFramework 6.x) 부터 Transaction 패키지가 javax에서 jakarta로 변경되었고 Atomikos는 아직 변경 전이다.
따라서 Atomikos를 사용하려면 SpringBoot 2.x(SpringFramework 5.x)를 사용해야 한다.

### 추가한 Properties
    - https://github.com/lunaticdawn/project-common/blob/main/project-cmn/src/main/resources/META-INF/spring-configuration-metadata.json
