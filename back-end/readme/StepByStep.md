# Project(SpringBoot, ReactJs) + BitBucket + Jenkins + Slack

```c
sudo ssh -i "aws-centos7.pem"  centos@15.164.249.29
```

```c
sudo ssh -i "test.pem" centos@52.78.155.118
```

---

## Goal

1. `VScode` will _clone_ , _push_ to `Bitbucket`
2. `Bitbucket` _notify_ `Jenkins`( using __Webhook__)
3. `Jenkins` _build_ project
4. `Jenkins` _depoly_ from `aws-ec2 server`
5. _finish_

---

## Project(SpringBoot)

### __Spring Boot Rest Security Api__

- Java : __`1.8`__
- `Maven`
- `Spring-boot`( maybe using `Inner Tomcat`)
- Database : `PostgreSQL`(`RDS`)
- postgresql port : `5432`
- postgresql id/password : `postgres` / `root1234`

### 1. Change DB(local → RDS)

- Add maven dependency

```xml
<dependency>
<groupId>org.postgresql</groupId>
<artifactId>postgresql</artifactId>
<scope>runtime</scope>
</dependency>
```

- springrestapi/src/main/resources/`application.properties`

```yml
# DB(RDS) : Postgre
spring.datasource.url=jdbc:postgresql://AWS-RDS-ADDRESS:5432/postgres
spring.datasource.username=********
spring.datasource.password=********

# JPA
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.properties.hibernate.hbm2ddl.auto=update
spring.jpa.show-sql=true
```

- output

```java
2019-11-02 12:32:57.029  INFO 62549 --- [           main] org.hibernate.dialect.Dialect            : HHH000400: Using dialect: org.hibernate.dialect.PostgreSQLDialect
Hibernate: create table "user_roles" ("user_msrl" int8 not null, roles varchar(255))
Hibernate: create table "user" (msrl  bigserial not null, name varchar(100) not null, password varchar(100) not null, uid varchar(50) not null, primary key (msrl))
Hibernate: alter table "user" drop constraint UK_a7hlm8sj8kmijx6ucp7wfyt31
Hibernate: alter table "user" add constraint UK_a7hlm8sj8kmijx6ucp7wfyt31 unique (uid)
Hibernate: alter table "user_roles" add constraint FKgprr2sw8eifkx9w3wm20hl28f foreign key ("user_msrl") references "user"
```

```SQL
postgres=> \dt
           List of relations
 Schema |    Name    | Type  |  Owner
--------+------------+-------+----------
 public | user       | table | postgres
 public | user_roles | table | postgres
(2 rows)
```

### 2. Commit& Push to Bitbucket

---

## Project(ReactJs)

> This part maybe skip or another markdown page next time...

---

## BitBucket

[https://bitbucket.org](https://bitbucket.org)

1. signup
2. signin
3. create repository(choose public or private)
4. git clone( check url `clone url`)
5. commit, push your project

## Jenkins

> My `jenkins` is in `AWS-ec2 CentOS7(free tier)`

### 1. Service Start Jenkins

```zsh
$ sudo service jenkins start
Starting jenkins (via systemctl):                          [  OK  ]
```

```zsh
$ netstat -lnput | grep 8080
(No info could be read for "-p": geteuid()=1000 but you should be root.)
tcp6       0      0 :::8080                 :::*                    LISTEN      -
```

### 2. Login Jenkins

- Jenkins URL : [http://15.164.249.29:8080/](http://15.164.249.29:8080/)
- Jenkins account : api / api

clean package -P prod -Dmaven.test.skip=true
