# JSON CUTTER

## 계획

> Swagger로 작성된 API Document를 Json 형태로 받아와서 </br> 각각의 구역으로 Object화 시켜서 Database에 저장시켜야됨.

- Json은 Url로 받아야됨
- 받은 Json의 key, value는 우리가 필요한 데이터가 있음
- 기본 형태들은 비슷하겠지만 내부적으로 다른 Json들을 받아와서 오류없이 다 객체화 시킬수 있어야됨
- Database에 저장하는 것이 일단 최종 목표

---

## Swagger-v2-api-docs 분석

### Json Files

- 프로젝트 백엔드 스웨거 : [http://15.165.25.145:9500/v2/api-docs](http://15.165.25.145:9500/v2/api-docs)
- json 폴더 : PM님 주신 회사 서비스에대한 스웨거들(__양오짐__)

### 도움 사이트

- JSON 구조화 사이트 :[https://jsoneditoronline.org](https://jsoneditoronline.org)
- JSON to Java POJO : [http://www.jsonschema2pojo.org/](http://www.jsonschema2pojo.org/)

### Swagger의 `v2/api-docs`

> 프로젝트 기준으로 Swagger에서 지원해주는 `v2/api-docs`에 대한것을 </br> 파싱하는 것이므로 이것을 기준으로 분석할 것임.

{ } 하위 Key | Value | Type
:------------|:----------|:---------
 swagger    | 값       | `String`
 info       | {하위갯수} | `Object`
 host       | 값       | `String`
 basePath   | 값       | `String`
 tags       | [하위갯수] | `Array`
 paths      | {하위갯수} | `Object`
 definitions| {하위갯수} | `Object`

- 가지고 있는 api-docs는 모두 위와 같이 하위 1단계는 7개의 key 형태임(data는 다름)
- swagger, info, host, basePath를 하나로 묶는 Entity
- tags( 이게아마 API 카테고리) Entity
- paths( 이게 API ) Entity
- definitions : 이거는 DTO인데 음 파라미터? 로 생각하면 될듯

---
