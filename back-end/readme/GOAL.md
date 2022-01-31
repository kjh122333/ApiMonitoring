# ___Goal___

------

## __1. Setting `Maven` dependencies__

### __(1). [https://start.spring.io](https://start.spring.io)__

set | version | tip
----|---------|----
`maven` ||
`java` | 1.8 |
`spring boot` | 2.2.0 |
`spring-boot-starter-data-jpa` || jpa
`spring-boot-starter-freemarker` || view template engine
`spring-boot-starter-web` || web mvc
`spring-boot-starter-security` || security
`lombok` || entity, vo, dto helper library
`h2` || database
`mysql-connector-java` || database
`postgresql` || database

### __(2). [https://mvnrepository.com/](https://mvnrepository.com/)__

set | version | tip
----|---------|----
`jjwt` | 0.9.1 | Json Web Token
`springfox-swagger2` | 2.6.1 | automation rest api document
`springfox-swagger-ui` | 2.6.1 | automation rest api document
`yaml-resource-bundle` | 1.1 | Multilingual

---

### Connect Postgre

```command
brew update; brew upgrade; brew cleanup
```

```command
brew install postgresql
```

```command
    brew services start postgresql
```

```command
    pg_ctl -D /usr/local/var/postgres start
```
