# project-common
Springboot 프로젝트 생성 시 참조할 공통 모듈

## DataSource 설정에 대해
1. 연결해야 할 DataSource가 1개인 경우에는 Spring Boot의 application.yml 설정만으로도 충분하다.
2. 연결해야 할 DataSource가 2개 이상인 경우에는 별도의 DataSource 연결과 Transaction 설정이 필요하다.
3. 연결해야 할 DataSource가 2개 이상이고 같은 Transaction에 묶여야 한다면 XADataSource를 이용하고, spring-boot-starter-jta-atomikos를 포함하면
자동으로 JtaTransactionManager를 설정해 준다. - Atomikos AutoConfiguration
4. SpringBoot 3.x(SpringFramework 6.x) 부터 Transaction 패키지가 javax에서 jakarta로 변경되었고 Atomikos는 아직 변경 전이다.
따라서 Atomikos를 사용하려면 SpringBoot 2.x(SpringFramework 5.x)를 사용해야 한다.
