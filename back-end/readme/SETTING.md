# 프로젝트 세팅

## 준비

1. Project
   1. `Spring Boot` - `Maven` : Back-end
   2. `React` : Front-end
2. `BitBucket` : 프로젝트 소스 관리
3. `Jenkins` : 빌드 자동화, [참고] : 도메인 설정 문제로 `ngrok` 사용
4. `Slack` : 버킷 푸시 및 젠킨스 빌드 알림
5. 배포용 서버 : `AWS - ec2`( CentOs7 ) 2개

---

## 1. Project

### `Spring Boot` - `Maven` : Back-end

### `React` : Front-end

---

## 2. `BitBucket` : 프로젝트 소스 관리

1. 회원가입
2. 로그인
3. 레포지토리 생성
4. __젠킨스에게 요청(응답)하기 위한 `WebHook` 생성__
   1. `title` : 식별하기위한 닉네임
   2. `URL` : 젠킨스주소/bitbucket-hook/ <br> [예제] `http://5fb2855b.ngrok.io/bitbucket-hook/`
   3. `Status` : Active(그대로)
   4. `SSL` / TLS : 그대로
   5. `Request history` : 그대로
   6. `Triggers` : Repository push

---

## 3. `Jenkins` : 빌드 자동화, [참고] : 도메인 설정 문제로 `ngrok` 사용

### Jenkins 실행

1. New Command Window
2. ```jenkins```
3. [http://lcoalhost:8080](http://lcoalhost:8080)

### ngrok을 이용한 젠킨스 외부 접속 혀용

1. New Command Window
2. ```ngrok http 8080```
3. 결과

    ```zsh
    Forwarding  http://5fb2855b.ngrok.io -> http://localhost:8080
    ```

4. [http://5fb2855b.ngrok.io](http://5fb2855b.ngrok.io)
5. `Jenkins` -> `새로운 item` -> `Enter an item name`(이름 입력) -> `freestyle project`(선택) -> `ok`
6. 프로젝트 선택 -> `구성`
   1. General -> 오래된 빌드 삭제
      1. Strategy -> 빌드 이력 유지 기간(일) : 5일
      2. Strategy -> 보관할 최대갯수 : 5개
   2. 소스 코드 관리 -> Git
      1. Repository URL : 버킷 레포지토리 주소
      2. Credentials -> Add -> Jenkins -> Jenkins Credentials Provider: Jenkins
         1. Username : 버킷 계정 메일
         2. Password : 버킷 계정 비번
         3. ID : 식별용 닉네임
      3. 빌드 유발 -> `Build when a change is pushed to BitBucket`
      4. 빌드 -> `Invoke top-level Maven target`(고급...도 누르고 설정해야됨)
         1. Maven Version : Maven 추가
         2. Goals : `clean package -P prod -Dmaven.test.skip=true`(빌드 명령어)
         3. POM	: `backend/pom.xml`(최상단 폴더 기준에서의 pom.xml위치)
         4. 나머지는 기본 세팅(빈칸으로)

---

## 4. `Slack` : 버킷 푸시 및 젠킨스 빌드 알림

1. slack -> Jenkins-Cl 앱 추가 -> 토큰, 알림 받을 채널, 팀 워크스페이스 3개 필요
2. 토큰, 알림 받을 채널, 팀 워크스페이스 추가해서 연결 성공

---

## 5. 배포용 서버 : `AWS - ec2`( CentOs7 ) 2개

- 정환

```zsh
sudo ssh -i "kjh.pem"  centos@15.165.25.145
```

- 정훈

```zsh
sudo ssh -i "test.pem" centos@52.78.155.118
```