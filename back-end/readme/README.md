# step by step ( cntl c + v)

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


## `AWS(CentOS7)` 접속

```centos
➜  ~ /Users/kjh/Desktop/15.164.249.29

➜  sudo ssh -i "aws-centos7.pem"  centos@15.164.249.29
****
Password: 비밀번호

Last login: Fri Nov  1 05:19:27 2019 from 211.169.69.254

$
```
./var/lib/jenkins/workspace/test/
## AWS(CentOS7)에서 `RDS(PostgreSQL)` 접속

```centos
$ yum update

$ psql -V
-bash: psql: command not found

$ sudo su

# yum install postgresql

# psql -V
psql (PostgreSQL) 9.2.24

# psql --host=postgredb.c3ov9aiuaeum.ap-northeast-2.rds.amazonaws.com --port=5432 --username=postgres --password
Password for user postgres:

postgres=> \l
                                  List of databases
   Name    |  Owner   | Encoding |   Collate   |    Ctype    |   Access privileges
-----------+----------+----------+-------------+-------------+-----------------------
 postgres  | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 |
 rdsadmin  | rdsadmin | UTF8     | en_US.UTF-8 | en_US.UTF-8 | rdsadmin=CTc/rdsadmin
 template0 | rdsadmin | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/rdsadmin          +
           |          |          |             |             | rdsadmin=CTc/rdsadmin
 template1 | postgres | UTF8     | en_US.UTF-8 | en_US.UTF-8 | =c/postgres          +
           |          |          |             |             | postgres=CTc/postgres
(4 rows)

postgres-> \q

#
```

## Install `Jenkins`

```zsh
wget -O /etc/yum.repos.d/jenkins.repo http://pkg.jenkins-ci.org/redhat-stable/jenkins.repo

rpm --import https://jenkins-ci.org/redhat/jenkins-ci.org.key

yum install jenkins

vi /etc/sysconfig/jenkins
    JENKINS_PORT="10000"

firewall-cmd --list-ports

firewall-cmd --zone=public --add-port=10000/tcp --permanent

firewall-cmd --zone=public --add-service=http --permanent

firewall-cmd --reload

netstat -tnlp

netstat -lnput | grep 8080

sudo service jenkins start

cat  /var/lib/jenkins/secrets/initialAdminPassword ### 8846e7a31a4c4b21b2130604cccb7ef6

### Jenkins SITE
# 1. initialAdminPassword
# 2. [click] Install suggested plugins
# 3. Getting Started ( auto Installing... )
# 4. Create First Admin User

```

---

## 번외 ( 정훈이꺼 )

```zsh

/Users/kjh/Desktop/52.78.155.118

➜ sudo ssh -i "test.pem" centos@52.78.155.118
Password:
```

---

### 참조 사이트

- 젠킨스 : [https://cloudopsmonkey.wordpress.com/2017/04/06/installing-and-configuring-jenkins-on-centos-7/](https://cloudopsmonkey.wordpress.com/2017/04/06/installing-and-configuring-jenkins-on-centos-7/)





```

fuser -k 8081/tcp

netstat -tnlp
 
sudo firewall-cmd --add-port=9501/tcp --permanent
success
```