# Final Project

- Project Name : API Monitoring
- Project URL ; [http://15.165.25.145:9600/](http://15.165.25.145:9600/)
- Project Developers 
  - Front-end : 하정훈, 최영원
  - Back-end : 김홍일, 공정환

## Front-end

- Language : JavaScript, HTML, CSS
- Framework : React
- Tool : VS code

## Back-end

- Language : JAVA 8
- Framework : Spring boot
- Tool : VS code

### Focusing Libraries

- `Security & Json Web Token` : Client 보안 및 인증
  - `spring-boot-starter-security`
  - `jjwt`
- `Swagger` : api document 작성
  - `springfox-swagger2`
  - `springfox-swagger-ui`
- `Jackson` : Json 매핑, 포맷
  - `jackson-databind`
  - `jackson-core`
  - `jackson-annotations`
- `JPA` & `MyBatis` : 단순 CRUD는 JPA, dynamic query는 MyBatis
  - `spring-boot-starter-data-jpa`
  - `mybatis-spring-boot-starter`
- ...

---

## Servers

1. dev pc( Write )
2. Bitbucket ( Push )
3. Jenkins ( Build )
4. AWS EC2 - CentOS7 ( Deploy )

- DataBase : AWS RDS ( MariaDB )

## `BItbucket`, `Jenkins`, EC2, RDS

## Registering API with `Swagger 2.0`

> Swagger를 이용해서 도큐먼트를 작성한 서비스라면 해당 swagger api document URL 또는 file(.json, .txt)을 통해 등록 가능(OpenApi3.0은 따로 변환해서 등록 [https://www.apimatic.io/transformer/](https://www.apimatic.io/transformer/) )

---

- REST api : 웹상에 존재하는 모든 자원(이미지, 동영상, DB자원)에 고유한 URI를 부영해 활용하는 것뒈

1. api
2. api category
3. service
4. service


SELECT api_category_name_kr  FROM t_api_category WHERE api_category_name_kr = #{api_category_name_kr} AND is_deleted = 'F' AND service_no = (select service_no from t_service where is_deleted = 'F' AND service_url=#{service_url})