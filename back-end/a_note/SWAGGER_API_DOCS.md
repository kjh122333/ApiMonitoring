# Table <---> Json

## __`t_service_category`__

- Privileges : select,insert,update,references, 보기에서 지움
- Comment : nodata, 보기에서 지움
- service_category_no 일괄입력
- service_category_no : auto_increment
- xxx.V = xxx의 키
- xxx.K = xxx의 값

## __`t_service`__

- Privileges : select,insert,update,references
- Comment : nodata

## __`t_api_category`__

- Privileges : select,insert,update,references
- Comment : nodata

## __`t_api`__

- Privileges : select,insert,update,references, 보기에서 지움
- Comment : nodata, 보기에서 지움
- employee_no 일괄입력
- api_no : auto_increment
- xxx.V = xxx의 키
- xxx.K = xxx의 값

---
```SQL
+--------+-------------+------------------+--------------+-------------+------------+---------------------+------------+--------+
| api_no | api_url     | content_type     | delay_status | employee_no | err_status | insert_timestamp    | is_deleted | method |
+--------+-------------+------------------+--------------+-------------+------------+---------------------+------------+--------+
|     86 | /qwera      | APPLICATION_JSON | F            |           3 | F          | 2019-11-18 13:16:47 | F          | GET    |
+--------+-------------+------------------+--------------+-------------+------------+---------------------+------------+--------+
+--------------------------------+
| param                          |
+--------------------------------+
| [                              |
|  {                             |
|    "name": "id",               |
|    "type": "query",            |
|    "description": "id test",   |
|    "required": "true",         |
|    "datatype": "String"        |
|  },                            |
|  {                             |
|    "name": "name",             |
|    "type": "query",            |
|    "description": "name test", |
|    "required": "true",         |
|    "datatype": "String"        |
|  }                             |
|]                               |
+--------------------------------+
+---------------------------------+
| response_list                   |
+---------------------------------+
|[                                |
|  {                              |
|    "resultName": "response200", |
|    "resultMsg": "good",         |
|    "resultCode": "200",         |
|    "resultData": "200 code"     |
|  },                             |
|  {                              |
|    "resultName": "response500", |
|    "resultMsg": "server err",   |
|    "resultCode": "500",         |
|    "resultData": "500 code"     |
|  }                              |
|]                                |
+---------------------------------+
+----------------+-----------------+---------------------+-----------------+
| description    | api_category_no | updated_timestamp   | employee_sub_no |
+----------------+-----------------+---------------------+-----------------+
| /docs/db/test1 |               5 | 2019-11-20 12:57:13 |               4 |
+----------------+-----------------+---------------------+-----------------+
```
