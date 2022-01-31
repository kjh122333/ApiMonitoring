import React, { useState, useCallback, useRef, useEffect,useMemo } from 'react';
import {
  Form,
  Icon,
  Select,
  Input,
  Button,
  Checkbox,
  Alert,
  message,
  Spin,
  Modal,
  Tooltip
} from 'antd';
import { Badge } from 'reactstrap';
import ResponseListTabs from './ResponseListTabs';
import { formDataNameSet as nameSet } from 'containers/api/ApiWriteContainer';
import SearchEmployee from './SearchEmployee';
import 'lib/css/api/api-write.css';
import _ from 'lodash';

const InputGroup = Input.Group;
const { Option } = Select;
const { TextArea } = Input;

export const inputHeader = {
  padding: '5px 0px 0px 0px',
  fontSize: '100%',
  marginRight: '10px',
};

export const isJsonString = str => {
  try {
    const json = JSON.parse(str);
    return typeof json === 'object';
  } catch (e) {
    return false;
  }
};

const WriteManual = ({
  history,
  serviceCategoryList,
  formData,
  handleSetFormData,
  serviceList,
  apiCategoryList,
  renderEmployee,
  visible,
  toggleModal,
  handleManualSubmit,
  initalizeFormData,
  saveLoading,
  isUndefined,
}) => {
  const paramRow = useRef(0);
  const [paramValues, setParamValues] = useState([]);
  const [responseValues, setResponseValues] = useState({
    '200': {
      isJson: true,
      value: {
        description: 'OK',
        schema: { 
          'resultCode(integer,int32)': 200, 
          'resultMsg(string)': '응답 메세지',
          'resultData(object)':"응답 데이터", 
        }
      },
    },
  });
  const [serviceUrl, setServiceUrl] = useState('');
  const [apiUrl, setApiUrl] = useState('');
  const [httpCode, setHttpCode] = useState('');
  //console.log('리스폰스 밸류', responseValues);

  const example = useMemo(()=>{
    const schema = { '변수명(타입[,포맷])': '[설명]' }
    const exampleJson = { 
      'resultCode(integer,int32)': 200, 
      'resultMsg(string)': '응답 메세지',
      'resultData(object)':{
        'name(string)' : 'GilDong',
        'age(integer,int16)' : 27,
      }, 
    }
    
    return (
      <div>
        <span>[ 형식 ]</span>
        <pre style={{color:'white', marginBottom:'0'}}>{JSON.stringify(schema, null, 1)}</pre>
        <span style={{fontSize:'85%', color:'gray'}}>타입이 객체나 배열의 경우 [설명] 자리에 내부 요소 작성 가능</span><br/>
        <span>[ 예시 ].</span>
        <pre style={{color:'white', marginBottom:'0'}}>{JSON.stringify(exampleJson, null, 1)}</pre>
      </div>
    )
  },[])

  const [tabs, setTabs] = useState([
    {
      title: '200',
      key: '200',
      content: (value, key) => (
        <>
          <div>
            <span>● 메세지</span>
            <Input
              value={value.description}
              onChange={({ target }) => {
                //console.log('리스폰스 디스크립션', target.value);

                setResponseValues(prevValues => ({
                  ...prevValues,
                  [key]: {
                    isJson: prevValues[key].isJson,
                    value: {
                      ...prevValues[key].value,
                      description: target.value,
                    },
                  },
                }));
              }}
            />
            
            <span style={{ display: 'block', marginTop: '10px' }}>
              ● 스키마&nbsp;&nbsp;
              <Tooltip placement="right" title={example}>
                <Icon type="question-circle" style={{ color: '#1890FF', fontSize:'18px', display:'inline-flex' }}/>
              </Tooltip>
              </span>
            <TextArea
              rows={6}
              defaultValue={JSON.stringify(value.schema, null, 2)}
              onChange={e => {
                handleSetResValues(e.target.value, '200');
              }}
              style={{marginBottom:'0'}}
            />
          </div>
        </>
      ),
      closable: false,
    },
  ]);

  useEffect(() => {
    handleSetFormData({ target: { name: nameSet.apiMethod, value: 'get' } });
    handleSetFormData({
      target: { name: 'parameterType', value: 'application/json' },
    });
    handleSetFormData({
      target: { name: 'responseType', value: 'application/json;charset=utf-8' },
    });
  }, [handleSetFormData]);

  /* 파라미터의 value 변경 */
  const handleParamChange = useCallback((key, name, value) => {
    setParamValues(prevParamValues =>
      prevParamValues.map(param => {
        if (param.key === key) {
          param[name] = value;
        }
        return param;
      }),
    );
  }, []);

  /* 파라미터 제거 버튼 클릭 이벤트 */
  const removeParamRow = useCallback(key => {
    setParamValues(prevState => prevState.filter(param => param.key !== key));
  }, []);

  /* 파라미터 추가 버튼 클릭 이벤트 */
  const addParamRow = useCallback(() => {
    const newParam = {
      key: paramRow.current++,
      name: '',
      in: 'body',
      description: '',
      type: 'string',
      required: false,
    };
    setParamValues(prevState => prevState.concat(newParam));
  }, []);

  /* 응답 리스트의 값 갱신 */
  const handleSetResValues = useCallback(
    (contentValue, key, action = undefined) => {
      // remove action(onEdit event)이 발생하면 responseValues state에서 삭제된 탭의 키를 제거.
      if (contentValue === '' && action === 'remove') {
        setResponseValues(prevValues => {
          delete prevValues[key];
          return prevValues;
        });
        return;
      }

      //console.log(contentValue, contentValue);
      //console.log(key, key);

      //action 값이 없을 경우(onChange event), 값만 갱신
      setResponseValues(prevValues => ({
        ...prevValues,
        [key]: {
          isJson: isJsonString(contentValue),
          value: {
            ...prevValues[key].value,
            schema: isJsonString(contentValue)
              ? JSON.parse(contentValue)
              : contentValue,
          },
        },
      }));
    },
    [],
  );

  /* 응답 리스트의 탭 추가 */
  const handleAddTabs = useCallback(() => {
    if (httpCode === '') {
      message.error('Http Code를 입력하세요.');
      return;
    }
    if (!(parseInt(httpCode) >= 100 && parseInt(httpCode) < 600)) {
      message.error('존재하지 않는 Http Code 입니다.');
      return;
    }
    if (tabs.find(tab => tab.key === httpCode)) {
      message.error('이미 존재하는 Http Code 입니다.');
      return;
    }
    setTabs( 
      tabs.concat({
        title: httpCode,
        key: httpCode,
        content: (value, key) => (
          <>
            <div>
              <span>● 메세지</span>
              <Input
                value={value.description}
                onChange={({ target }) => {
                  //console.log('리스폰스 디스크립션', target.value);

                  setResponseValues(prevValues => ({
                    ...prevValues,
                    [key]: {
                      isJson: prevValues[key].isJson,
                      value: {
                        ...prevValues[key].value,
                        description: target.value,
                      },
                    },
                  }));
                }}
              />
              <span style={{ display: 'block', marginTop: '10px' }}>
              ● 스키마&nbsp;&nbsp;
              <Tooltip placement="right" title={example}>
                <Icon type="question-circle" style={{ color: '#1890FF', fontSize:'18px', display:'inline-flex' }}/>
              </Tooltip>
              </span>
              <TextArea
                rows={6}
                defaultValue={JSON.stringify(value.schema, null, 2)}
                onChange={e => {
                  handleSetResValues(e.target.value, httpCode);
                }}
              />
            </div>
          </>
        ),
      }),
    );
    setResponseValues(preValues => ({
      ...preValues,
      [httpCode]: {
        isJson: true,
        value: {
          description: 'message',
          schema: {},
        },
      },
    }));
    setHttpCode('');
  }, [example, handleSetResValues, httpCode, tabs]);

  /* 응답 리스트의 선택된 탭 제거 */
  const handleRemoveTabs = useCallback(
    (tabKey, action) => {
      if (action === 'remove') {
        setTabs(prevTabs => prevTabs.filter(tab => tab.key !== tabKey));
        handleSetResValues('', tabKey, action);
      }
    },
    [handleSetResValues],
  );

  /* 응답 리스트의 항목들이 json 형식에 맞는지 검사. */
  const validResponseValues = useCallback(() => {
    const notJsonList = Object.keys(responseValues).filter(
      key => responseValues[key].isJson === false,
    );
    return {
      isJson: notJsonList.length ? false : true,
      notJsonList, //[] or [...]
    };
  }, [responseValues]);

  /* 전송 전 폼 데이터의 값들이 존재하는지 검사. */
  const validFormData = useCallback(() => {
    const requiredKeys = [
      nameSet.apiCategoryNo,
      nameSet.apiUrl,
      nameSet.apiDescription,
      nameSet.employee,
      nameSet.employeeSub,
      nameSet.apiMethod,
      'parameterType',
      'responseType',
    ];
    if (isUndefined(requiredKeys)) {
      Modal.error({
        title: '필수 항목을 입력해주십시오.',
      });
      return false;
    }

    if (formData[nameSet.apiUrl].charAt(0) !== '/') {
      Modal.error({
        title: '입력 형식 불일치',
        content: "API URL은 '/' 로 시작해야 합니다.",
      });
      return false;
    }

    const { isJson, notJsonList } = validResponseValues();
    if (isJson === false) {
      Modal.error({
        title: '입력 형식 불일치',
        content: `code ${notJsonList}의 응답 내용을 JSON 형식에 맞게 작성해주세요.`,
      });
      return false;
    }

    return true;
  }, [formData, isUndefined, validResponseValues]);

  /* 파라미터 헤더 렌더링 */
  const renderParamHeader = useCallback(
    () => (
      <Form.Item style={{ display: 'inline' }}>
        <div className="param-header-empty" />
        {[
          { value: '파라미터 이름', width: '25%' },
          { value: '타입', width: '10%' },
          { value: '설명', width: '25%' },
          { value: '데이터 타입', width: '15%' },
          { value: '필수 여부', width: '98px' },
        ].map(data => (
          <Input
            key={data.value}
            size="small"
            className="param-header"
            style={{ width: data.width }}
            value={data.value}
            readOnly
          />
        ))}
      </Form.Item>
    ),
    [],
  );

  /* 파라미터 row 렌더링 */
  const renderParamRow = useCallback(
    () =>
      paramValues.map(param => (
        <Form.Item key={param.key} style={{ marginBottom: '8px' }}>
          <Icon
            className="dynamic-delete-button"
            type="minus-circle-o"
            onClick={e => {
              removeParamRow(param.key);
            }}
            style={{ color: 'red', marginRight: 8 }}
          />
          <Input
            placeholder="name"
            name="name"
            style={{ width: '25%', marginRight: 8 }}
            required
            onBlur={e => {
              handleParamChange(param.key, e.target.name, e.target.value);
            }}
          />
          <Select
            defaultValue="body"
            style={{ width: '10%', marginRight: 8 }}
            onBlur={value => {
              handleParamChange(param.key, 'in', value);
            }}
          >
            <Option value="body">body</Option>
            <Option value="path">path</Option>
            <Option value="query">query</Option>
            <Option value="header">header</Option>
          </Select>
          <Input
            placeholder="description"
            name="description"
            style={{ width: '25%', marginRight: 8 }}
            onBlur={e => {
              handleParamChange(param.key, e.target.name, e.target.value);
            }}
          />

          <Select
            defaultValue="string"
            style={{ width: '15%', marginRight: 8 }}
            onBlur={value => {
              handleParamChange(param.key, 'type', value);
            }}
          >
            <Option value="string">string</Option>
            <Option value="int">int</Option>
            <Option value="float">float</Option>
            <Option value="double">double</Option>
            <Option value="number">number</Option>
            <Option value="json">json</Option>
            <Option value="binary">binary</Option>
          </Select>
          <Checkbox
            name="required"
            onBlur={e => {
              handleParamChange(param.key, e.target.name, e.target.checked);
            }}
            style={{
              marginRight: 8,
              textAlign: 'center',
              display: 'inherit',
              padding: '5px 6px',
            }}
            className="ant-input"
          >
            required
          </Checkbox>
        </Form.Item>
      )),
    [handleParamChange, paramValues, removeParamRow],
  );

  return (
    <Form
      onSubmit={e => {
        e.preventDefault();

        if (validFormData() === false) return;

        const cloneResList = _.cloneDeep(responseValues);
        let postResList = {};
        //console.log('클론 리스트', cloneResList);

        const keys = Object.keys(cloneResList);
        keys.forEach(key => {
          postResList = {
            ...postResList,
            [key]: cloneResList[key].value,
          };
        });

        const cloneParam = _.cloneDeep(paramValues);
        let postParam = cloneParam.map(param => {
          delete param.key;
          return param;
        });

        const postData = {
          api_category_no: formData[nameSet.apiCategoryNo],
          api_url: formData[nameSet.apiUrl],
          description: formData[nameSet.apiDescription],
          employee_no: formData[nameSet.employee].employee_no,
          employee_sub_no: formData[nameSet.employeeSub].employee_no,
          method: formData[nameSet.apiMethod],
          param: postParam,
          parameter_type: formData['parameterType'],
          response_list: postResList,
          response_type: formData['responseType'],
          update_employee_id: sessionStorage.getItem('id'),
        };
        handleManualSubmit(postData);
        //console.log('직접입력 서브밋 함.', postData);
      }}
      className="steps-content"
      style={{ textAlign: 'left', padding: '30px 60px' }}
    >
      <Alert
        message="Swagger를 통해 생성된 서비스에 api를 직접 등록 할 경우 추후에 Swagger 재등록시 데이터가 지워질 수 있습니다."
        type="warning"
        showIcon
        style={{ marginBottom: '10px' }}
      />

      <Form.Item label="카테고리 선택" required>
        <Badge color="secondary" style={{ ...inputHeader, minWidth: '150px' }}>
          <span>서비스 카테고리</span>
          <Select
            value={formData[nameSet.serviceCategoryNo]}
            style={{ display: 'block' }}
            onChange={(value, option) => {
              handleSetFormData({
                target: { name: nameSet.serviceCategoryNo, value },
              });
              handleSetFormData({
                target: {
                  name: nameSet.serviceCategoryName,
                  value: option.props.children,
                },
              });

              handleSetFormData({
                target: { name: nameSet.serviceNo, value: '' },
              });
              handleSetFormData({
                target: { name: nameSet.serviceName, value: '' },
              });
              setServiceUrl('');

              handleSetFormData({
                target: { name: nameSet.apiCategoryNo, value: '' },
              });
              handleSetFormData({
                target: { name: nameSet.apiCategoryName, value: '' },
              });
            }}
          >
            {serviceCategoryList.map(category => (
              <Option
                key={category.service_category_no}
                value={category.service_category_no}
              >
                {category.category_name_kr}
              </Option>
            ))}
          </Select>
        </Badge>

        <Badge color="secondary" style={{ ...inputHeader, minWidth: '150px' }}>
          <span>서비스</span>
          <Select
            value={formData[nameSet.serviceNo]}
            style={{ display: 'block' }}
            onChange={(value, option) => {
              handleSetFormData({ target: { name: nameSet.serviceNo, value } });
              handleSetFormData({
                target: {
                  name: nameSet.serviceName,
                  value: option.props.children,
                },
              });
              const serviceUrl = serviceList.find(
                service => service.service_no === value,
              ).service_url;
              setServiceUrl(
                serviceUrl.charAt(serviceUrl.length - 1) === '/'
                  ? serviceUrl.slice(0, -1)
                  : serviceUrl,
              );

              handleSetFormData({
                target: { name: nameSet.apiCategoryNo, value: '' },
              });
              handleSetFormData({
                target: { name: nameSet.apiCategoryName, value: '' },
              });
            }}
          >
            {serviceList
              .filter(
                service =>
                  service.service_category_no ===
                  formData[nameSet.serviceCategoryNo],
              )
              .map(service => (
                <Option key={service.service_no} value={service.service_no}>
                  {service.service_name_kr}
                </Option>
              ))}
          </Select>
        </Badge>

        <Badge color="secondary" style={{ ...inputHeader, minWidth: '150px' }}>
          <span>API 카테고리</span>
          <Select
            value={formData[nameSet.apiCategoryNo]}
            style={{ display: 'block' }}
            onChange={(value, option) => {
              handleSetFormData({
                target: { name: nameSet.apiCategoryNo, value },
              });
              handleSetFormData({
                target: {
                  name: nameSet.apiCategoryName,
                  value: option.props.children,
                },
              });
            }}
          >
            {apiCategoryList
              .filter(
                category => category.service_no === formData[nameSet.serviceNo],
              )
              .map(category => (
                <Option
                  key={category.api_category_no}
                  value={category.api_category_no}
                >
                  {category.api_category_name_kr}
                </Option>
              ))}
          </Select>
        </Badge>
      </Form.Item>

      <Form.Item label="URL 입력" required>
        <Badge color="secondary" style={{ ...inputHeader, minWidth: '350px' }}>
          <span>서비스 URL</span>
          <Input
            value={serviceUrl}
            style={{ display: 'block' }}
            name="serviceUrl"
            readOnly
          />
        </Badge>

        <Badge color="secondary" style={{ ...inputHeader, minWidth: '350px' }}>
          <span>API URL</span>
          <Input
            style={{ display: 'block' }}
            placeholder="서비스 URL을 제외한 API URL만 입력하세요"
            autoComplete="off"
            name={nameSet.apiUrl}
            onChange={e => {
              setApiUrl(e.target.value);
            }}
            onBlur={handleSetFormData}
            required
          />
        </Badge>

        <Badge color="secondary" style={{ ...inputHeader, minWidth: '100px' }}>
          <span>Method</span>
          <Select
            defaultValue="get"
            onChange={value => {
              handleSetFormData({ target: { name: nameSet.apiMethod, value } });
              // handleDropBoxChange('method', value);
            }}
            style={{ display: 'block' }}
          >
            <Option value="get">GET</Option>
            <Option value="post">POST</Option>
            <Option value="put">PUT</Option>
            <Option value="patch">PATCH</Option>
            <Option value="delete">DELETE</Option>
          </Select>
        </Badge>
      </Form.Item>

      <Form.Item label="url 확인">
        <InputGroup compact style={{ marginBottom: '25px' }}>
          <Badge
            color="secondary"
            style={{ ...inputHeader, minWidth: '600px' }}
          >
            <span>전체 URL</span>
            <Input
              style={{ display: 'block' }}
              placeholder="(자동완성)"
              name="totalUrl"
              value={serviceUrl + apiUrl}
              // value={serviceUrl.slice(0,-1)+apiUrl}
              readOnly
            />
          </Badge>
        </InputGroup>
      </Form.Item>

      <Form.Item label="Api 설명" required>
        <InputGroup compact>
          <Badge
            color="secondary"
            style={{ ...inputHeader, minWidth: '400px' }}
          >
            <span style={{ display: 'block' }}>설명</span>
            <TextArea
              rows={5}
              style={{ textAlign: 'left' }}
              required
              name={nameSet.apiDescription}
              onBlur={handleSetFormData}
            />
          </Badge>
        </InputGroup>
      </Form.Item>

      <Form.Item label="담당자 지정" required>
        <Badge color="secondary" style={{ ...inputHeader, minWidth: '400px' }}>
          <span style={{ marginBottom: '1px', display: 'block' }}>
            담당자 선택
          </span>
          {renderEmployee()}
        </Badge>
      </Form.Item>

      <Form.Item label="파라미터 입력">
        <Badge color="secondary" style={{ ...inputHeader, minWidth: '400px' }}>
          <span>Parameter Content-Type</span>
          <Input
            style={{ display: 'block' }}
            defaultValue="application/json"
            name="parameterType"
            onBlur={handleSetFormData}
          />
        </Badge>
        <Button type="dashed" onClick={addParamRow} className="add-button">
          <Icon type="plus" style={{ fontSize: '1.5rem' }} />
          파라미터 추가
        </Button>
      </Form.Item>

      {paramValues.length > 0 && renderParamHeader()}
      {paramValues.length > 0 && renderParamRow()}

      <Form.Item label="응답 입력" required>
        <Badge color="secondary" style={{ ...inputHeader, minWidth: '400px' }}>
          <span>Response Content-Type</span>
          <Input
            style={{ display: 'block' }}
            defaultValue="application/json;charset=utf-8"
            name="responseType"
            onBlur={handleSetFormData}
          />
        </Badge>
        <Badge color="secondary" style={{ ...inputHeader, width: '100px' }}>
          <span>Http Code</span>
          <Input
            style={{ display: 'block' }}
            placeholder="ex)400"
            name="httpCode"
            value={httpCode}
            onChange={e => {
              const { value } = e.target;
              const reg = /^-?(0|[1-9][0-9]*)(\.[0-9]*)?$/;
              if (
                (!isNaN(value) && reg.test(value)) ||
                value === '' ||
                value === '-'
              ) {
                setHttpCode(value);
              }
            }}
          />
        </Badge>
        <Button type="dashed" onClick={handleAddTabs} className="add-button">
          <Icon type="plus" style={{ fontSize: '1.5rem' }} />
          응답 클래스 추가
        </Button>
        <div style={{ ...inputHeader, minWidth: '400px' }}>
          <ResponseListTabs
            tabs={tabs}
            handleRemoveTabs={handleRemoveTabs}
            responseValues={responseValues}
            validResponseValues={validResponseValues}
          />
        </div>
      </Form.Item>

      <Form.Item style={{ textAlign: 'center' }}>
        {saveLoading ? (
          <Spin tip="Loading...">
            <Button type="primary" htmlType="submit">
              등록
            </Button>
          </Spin>
        ) : (
          <Button type="primary" htmlType="submit">
            등록
          </Button>
        )}
      </Form.Item>

      {visible.display && (
        <SearchEmployee
          visible={visible}
          formData={formData}
          toggleModal={toggleModal}
          handleSetFormData={handleSetFormData}
        />
      )}
    </Form>
  );
};

export default React.memo(WriteManual);
