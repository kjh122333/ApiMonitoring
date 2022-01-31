# JSON PARSING

## __SETTING__

- from : swagger api docs(.json)
- to : `maria db`(RDB)
- use : `Jackson`

## __Json's shapes__

> Focus Key& Value

 Key | Type | Value
:-----|:-----|:-----
 `schema` | object | `$ref`
 `item` | object | `$ref`
 `$ref` | key | `#/definitions/...`

### _Root Object : { ..., Paths : { /user/servicegroupname{ }, ... } }_

```json
   "/user/servicegroupname": {
            "get": {
                "tags": [
                    "2. Service"
                ],
                "summary": "Service GroupName List",
                "description": "서비스 GroupName 리스트",
                "operationId": "serviceGroupNameListUsingGET",
                "consumes": [
                    "application/json"
                ],
                "produces": [
                    "*/*"
                ],
                "parameters": [
                    {
                        "name": "X-AUTH-TOKEN",
                        "in": "header",
                        "description": "로그인 성공 후 access_token",
                        "required": true,
                        "type": "string"
                    }
                ],
                "responses": {
                    "200": {
                        "description": "OK",
                        "schema": {
                            "$ref": "#/definitions/_List«Map«string,object»»"
                        }
                    }
                }
            }
        }
```

### _Root Object : { ..., definitions : { _List«Map«string,object»» : { }, ... } }_

```json
"_List«Map«string,object»»": {
            "type": "object",
            "properties": {
                "code": {
                    "type": "integer",
                    "format": "int32",
                    "description": "응답 코드번호 : > 0 정상, < 0 비정상"
                },
                "list": {
                    "type": "array",
                    "items": {
                        "$ref": "#/definitions/Map«string,object»"
                    }
                },
                "message": {
                    "type": "string",
                    "description": "응답 메시지"
                },
                "success": {
                    "type": "boolean",
                    "example": false,
                    "description": "응답 성공여부 : TRUE/FALSE"
                }
            }
        }
```

### _Root Object : { ..., definitions : { Map«string,object» : { }, ... } }_

```json
 "Map«string,object»": {
            "type": "object",
            "additionalProperties": {
                "type": "object"
            }
        }
```

### Goal

```json
"/user/servicegroupname": {
    "get": {
        "tags": [
            "2. Service"
        ],
        "summary": "Service GroupName List",
        "description": "서비스 GroupName 리스트",
        "operationId": "serviceGroupNameListUsingGET",
        "consumes": [
            "application/json"
        ],
        "produces": [
            "*/*"
        ],
        "parameters": [
            {
                "name": "X-AUTH-TOKEN",
                "in": "header",
                "description": "로그인 성공 후 access_token",
                "required": true,
                "type": "string"
            }
        ],
        "responses": {
            "200": {
                "description": "OK",
                "schema": {
                    "_List«Map«string,object»»": {
                        "type": "object",
                        "properties": {
                            "code": {
                                "type": "integer",
                                "format": "int32",
                                "description": "응답 코드번호 : > 0 정상, < 0 비정상"
                            },
                            "list": {
                                "type": "array",
                                "items": {
                                    "Map«string,object»": {
                                        "type": "object",
                                        "additionalProperties": {
                                            "type": "object"
                                        }
                                    }
                                }
                            },
                            "message": {
                                "type": "string",
                                "description": "응답 메시지"
                            },
                            "success": {
                                "type": "boolean",
                                "example": false,
                                "description": "응답 성공여부 : TRUE/FALSE"
                            }
                        }
                    }
                }
            }
        }
    }
}
```
