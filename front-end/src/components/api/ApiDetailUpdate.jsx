import React,{useState, useEffect, useCallback, useMemo} from 'react';
import {Form, Modal,Icon, Select,Input, Row, Col,Button,Checkbox,message,Spin,Tooltip} from 'antd';
import axios from 'axios'
import { Badge } from 'reactstrap';
import {inputHeader, isJsonString} from '../api/apiWrite/WriteManual';
import SearchEmployee from '../api/apiWrite/SearchEmployee';
import 'lib/css/api/api-write.css'
import _ from 'lodash';
import ResponseListTabs from './apiWrite/ResponseListTabs';
import handleException from 'lib/function/request/handleException.js';



const { Option } = Select;
const InputGroup = Input.Group;
const {TextArea, Search} = Input;


const ApiDetailUpdate = ({service, apiCategory, api,closeModal, history, forceRender}) => {
    const [loading, setLoading] = useState(false); //로딩 상태
    const [serviceCategoryList, setServiceCategoryList] = useState([]); //서비스 카테고리 이름 목록
    const [serviceList, setServiceList] = useState([]); //서비스 이름 목록
    const [apiCategoryList, setApiCategoryList] = useState([]); //api 카테고리 이름 목록
    const [formData, setFormData] = useState(()=>{
      //console.log('여기여기여기',api);
      let response_list={};
      if(api.response_list!==null){
      const response_list_copy = _.cloneDeep(api.response_list);
      // response_list_copy
      
      Object.keys(response_list_copy).forEach(key=>{
        response_list = {
          ...response_list,
          [key] : {
            isJson:true,
            value : response_list_copy[key]
          }
        }
      })}
      
      return {
        ...service,
        ...apiCategory,
        ...api,
        param : api.param === null ? [] : api.param,
        response_list,
      }
    });

    //console.log('리스폰스리스트',formData.response_list);
    //console.log('리스폰스 폼데이터',formData);

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
    
    const [httpCode, setHttpCode]= useState('');
    const [tabs, setTabs] = useState(
      Object.keys(formData.response_list).map(key => ({
        title: key.toString(),
        key: key.toString(),
        content: (value, key) => (
          <>
            <div>
              <span>● 메세지</span>
              <Input
                value={value.description}
                onChange={({ target }) => {
                  //console.log('수정한 리스폰스 디스크립션', target.value);

                  setFormData(prevForm => ({
                    ...prevForm,
                    response_list: {
                      ...prevForm.response_list,
                      [key]: {
                        isJson: prevForm.response_list[key].isJson,
                        value: {
                          ...prevForm.response_list[key].value,
                          description: target.value,
                        },
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
                // defaultValue={JSON.stringify(formData.response_list[key], null, 2)}
                defaultValue={JSON.stringify(value.schema, null, 2)}
                onChange={({ target }) => {
                  //console.log('수정한 리스폰스 스키마', target.value);

                  setFormData(prevForm => ({
                    ...prevForm,
                    response_list: {
                      ...prevForm.response_list,
                      [key]: {
                        isJson: isJsonString(target.value),
                        value: {
                          ...prevForm.response_list[key].value,
                          schema: isJsonString(target.value)
                            ? JSON.parse(target.value)
                            : target.value,
                        },
                      },
                    },
                  }));
                }}
                
              />
            </div>
          </>
        ),
        closable: key.toString() === '200' ? false : true,
      })),
    );
     const [saveLoading, setSaveLoading] = useState(false);

    
    useEffect(()=>{
        const getServiceCateList = async () =>{     
            try{
              setLoading(true);

              const resServiceCategorys = await axios.get('http://15.165.25.145:9500/user/category/f');
              setServiceCategoryList(resServiceCategorys.data.success ? resServiceCategorys.data.list : []);
              
              const resServices = await axios.get('http://15.165.25.145:9500/user/service/f');
              setServiceList(resServices.data.success ? resServices.data.list : []);
              
              const resApiCategorys = await axios.get('http://15.165.25.145:9500/user/apicategory/f');
             setApiCategoryList(resApiCategorys.data.success ? resApiCategorys.data.list : []);
              // setLoading(false);
            }catch(error){
             handleException(error,history)
            }finally{
              setLoading(false);
            }
           }
           getServiceCateList();
    },[history]);

    const [visible, setVisible ] = useState({
      display : false,
      isSub : false
    });

    const toggleModal = useCallback((isSub=false)=>{
      setVisible((prevState=>({
        display : !prevState.display,
        isSub,
      })));
    },[]);

    // //console.log(formData.param);
    //console.log(formData)
    

    /* 파라미터 추가 버튼 클릭 이벤트 */
    const addParamRow = useCallback(() => {
      const newParam = {
        name:'',
        in:'body',
        description:'',
        type:'string',
        required:false
      }
      setFormData(prevState=>({...prevState, param : prevState.param.concat(newParam)}));;
    },[]);

    /* 파라미터 제거 버튼 클릭 이벤트 */
    const removeParamRow = useCallback(key=>{ //인덱스를 키로 씀
      //console.log(key);
      setFormData(prevState=>({...prevState, param :prevState.param.filter((param,index)=>index!==key)}));
    },[]);

    /* 응답 리스트의 탭 추가 */
    const handleAddTabs = useCallback(()=>{
      if(httpCode===''){
        message.error('Http Code를 입력하세요.');
        return;
      }    
      if(!(parseInt(httpCode)>=100 && parseInt(httpCode)<600)){
        message.error('존재하지 않는 Http Code 입니다.');
        return;
      }
      if(tabs.find(tab=>(tab.key===httpCode))){
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
                    //console.log('수정한 리스폰스 디스크립션', target.value);

                    setFormData(prevForm => ({
                      ...prevForm,
                      response_list: {
                        ...prevForm.response_list,
                        [httpCode]: {
                          isJson: prevForm.response_list[httpCode].isJson,
                          value: {
                            ...prevForm.response_list[httpCode].value,
                            description: target.value,
                          },
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
                  onChange={({ target }) => {
                    //console.log('수정한 리스폰스 스키마', target.value);

                    setFormData(prevForm => ({
                      ...prevForm,
                      response_list: {
                        ...prevForm.response_list,
                        [httpCode]: {
                          isJson: isJsonString(target.value),
                          value: {
                            ...prevForm.response_list[httpCode].value,
                            schema: isJsonString(target.value)
                              ? JSON.parse(target.value)
                              : target.value,
                          },
                        },
                      },
                    }));
                  }}
                  
                />
              </div>
            </>
          ),
        })
      );
      setFormData(prevForm=>({
        ...prevForm,
        response_list : {
          ...prevForm.response_list,
          [httpCode] : {
            isJson: true,
            value: {
              description: 'message',
              schema: {},
            },
          },
      }}));
      setHttpCode('');
    },[example, httpCode, tabs]);
  
  /* 응답 리스트의 선택된 탭 제거 */
    const handleRemoveTabs = useCallback((tabKey, action)=>{
      if(action==='remove'){
        setTabs(prevTabs=>prevTabs.filter(tab=>tab.key!==tabKey))
        setFormData(prevForm=>{
          const response_list_copy = _.cloneDeep(prevForm.response_list)
          delete response_list_copy[tabKey] //탭 제거시 state에서 해당 값 제거
          return ({
            ...prevForm,
            response_list : response_list_copy
          })
        })
      }
    },[]);



  /* 응답 리스트의 항목들이 json 형식에 맞는지 검사. */
  const validResponseValues = useCallback(()=>{
    const notJsonList = Object.keys(formData.response_list).filter(key=>formData.response_list[key].isJson===false);
    return ({
      isJson : notJsonList.length ? false : true,
      notJsonList, //[] or [...] 
    })
  },[formData.response_list]);

      /* 전송 전 폼 데이터의 값들이 존재하는지 검사. */
      const validFormData = useCallback(()=>{
        const requiredKeys = [
          "api_category_no",
          "api_no",
          "api_url",
          "description",
          "employee_no",
          "employee_sub_no",
          "method",
          "param",
          "parameter_type",
          "response_list",
          "response_type",
        ]
  
        const undefinedList = requiredKeys.filter(key=>{
          const checkValue = typeof formData[key] === 'string' ? formData[key].trim() : formData[key];
          return checkValue===undefined || checkValue==='' ? true : false;
        })
  
        //console.log('언디파인 목록',undefinedList);
  
        if(undefinedList.length>0){

          Modal.error({
            title: '필수 항목을 입력해주십시오.',
          });
          return false;
        }
  
        if(formData.api_url.charAt(0)!=='/'){

          Modal.error({
            title: '입력 형식 불일치',
            content: 'API URL은 \'/\' 로 시작해야 합니다.'
          });
          return false;
        }
    
        const {isJson, notJsonList} = validResponseValues();
        if(isJson===false){
          
          Modal.error({
            title: '입력 형식 불일치',
            content: `code ${notJsonList}의 응답 내용을 JSON 형식에 맞게 작성해주세요.`
          });
          return false;
        }
    
          return true;
      },[formData, validResponseValues]);

    const handleOnSubmit = useCallback(()=>{
      const updateReq = async () =>{
        try {
          setSaveLoading(true);

          if(validFormData()===false){
            setSaveLoading(false);
            return;
          }

          const response_list_copy = _.cloneDeep(formData.response_list);
          let response_list = {};
          Object.keys(response_list_copy).forEach(key=>{
            response_list = {
              ...response_list,
              [key] : response_list_copy[key].value
            }
          })

          const postData = {
            "api_category_no": formData.api_category_no,
            "api_no": formData.api_no,
            "api_url": formData.api_url,
            "description": formData.description,
            "employee_no": formData.employee_no,
            "employee_sub_no": formData.employee_sub_no,
            "method": formData.method,
            "param": formData.param,
            "parameter_type": formData.parameter_type.toString(),
            "response_list": response_list,
            "response_type": formData.response_type.toString(),
            "update_employee_id" : sessionStorage.getItem('id').toString()
          }
          
          //console.log('리스폰스 날린데ㅣ터',postData);
          const res = await axios.patch(`http://15.165.25.145:9500/user/api/${api.api_no}`,postData)
          //console.log('바든 데이터 ' ,res);
          if(res.data.success){

            Modal.success({
              title: '수정이 완료되었습니다.',
            });
            setSaveLoading(false);
            closeModal();
            forceRender();
            const {pathname, search} = history.location
            history.replace(pathname+search)
          }else{

            Modal.error({
              title: '수정을 실패하였습니다',
              content: `${res.data.message}`,
            });
          }
        } catch (error) {
          setSaveLoading(false);
          //console.log(error);
          
         handleException(error,history);
        }       
      }
      updateReq();
    },[api.api_no, closeModal, forceRender, formData.api_category_no, formData.api_no, formData.api_url, formData.description, formData.employee_no, formData.employee_sub_no, formData.method, formData.param, formData.parameter_type, formData.response_list, formData.response_type, history, validFormData])

    const renderForm = useCallback(()=>(
      <Form 
          layout="horizontal" 
          onSubmit={e=>{
            e.preventDefault();
            handleOnSubmit()
          }}            
        >
            <Form.Item label="카테고리" required style={{marginBottom:'5px'}}>
            <Badge color="secondary" style={{ ...inputHeader, minWidth:'150px'}}>
        <span>서비스 카테고리</span>
          <Select
            value={formData.service_category_no}
            style={{ display: 'block' }}
            disabled
            >
            {serviceCategoryList.map(category=>
              <Option key={category.service_category_no} value={category.service_category_no}>{category.category_name_kr}</Option>
            )}
          </Select>
        </Badge>

        <Badge color="secondary" style={{ ...inputHeader, minWidth:'150px'}}>
          <span>서비스</span>
          <Select
            value={formData.service_no}
            style={{ display: 'block' }}
            disabled
            >
            {serviceList.filter(service=>service.service_category_no===formData.service_category_no).map(service=>
              <Option key={service.service_no} value={service.service_no}>{service.service_name_kr}</Option>
            )}
          </Select>
        </Badge>

        <Badge color="secondary" style={{ ...inputHeader,minWidth:'150px'}}>
          <span>API 카테고리</span>
          <Select
            value={formData.api_category_no}
            style={{ display: 'block' }}
            onChange={(value,option) =>{
              setFormData(prevForm=>({
                ...prevForm,
                api_category_no : value,
              }))
            }}
            >
            {apiCategoryList.filter(category=>category.service_no===formData.service_no).map(category=>
              <Option key={category.api_category_no} value={category.api_category_no}>{category.api_category_name_kr}</Option>
            )}
          </Select>
        </Badge>
            </Form.Item>

            <Form.Item label="URL 입력" required style={{marginBottom:'5px'}}>
      <Badge color="secondary" style={{ ...inputHeader, minWidth:'300px'}}>
          <span>서비스 URL</span>
          <Input
            value={formData.service_url.charAt(formData.service_url.length-1)==='/' ? formData.service_url.slice(0,-1) : formData.service_url} 
            style={{ display: 'block' }}
            name="serviceUrl"
            readOnly
          />
        </Badge>

        <Badge color="secondary" style={{ ...inputHeader, minWidth:'250px'}}>
          <span>API URL</span>
          <Input
            style={{ display: 'block' }}
            placeholder="서비스 URL을 제외한 API URL만 입력하세요"
            autoComplete="off"
            value={formData.api_url}
            onChange={({target})=>{
              setFormData(prevForm=>({
                ...prevForm,
                api_url : target.value,
              }))
            }}
            // onBlur={handleSetFormData}
            required
          />
        </Badge>

        <Badge color="secondary" style={{ ...inputHeader, minWidth:'100px'}}>
          <span>Method</span>
          <Select
            defaultValue={formData.method}
            onChange={value => {
              setFormData(prevForm=>({
                ...prevForm,
                method : value,
              }))
              // handleSetFormData({target:{name:nameSet.apiMethod, value,}})
              // handleDropBoxChange('method', value);
            }}
            style={{ display:'block'}}
          >
            <Option value="get">GET</Option>
            <Option value="post">POST</Option>
            <Option value="put">PUT</Option>
            <Option value="patch">PATCH</Option>
            <Option value="delete">DELETE</Option>
          </Select>
        </Badge>

      </Form.Item>
        

      <Form.Item label="url 확인" style={{marginBottom:'5px'}}>
        <InputGroup compact style={{marginBottom:'25px'}}>
          <Badge color="secondary" style={{ ...inputHeader, minWidth:'400px'}}>
          <span>전체 URL</span>
            <Input
              style={{ display: 'block' }}
              placeholder="(자동완성)"
              name="totalUrl"
              value={(formData.service_url.charAt(formData.service_url.length-1)==='/' ? formData.service_url.slice(0,-1) : formData.service_url)+formData.api_url}
              readOnly
            />
          </Badge>
        </InputGroup>
      </Form.Item>

      <Form.Item label="Api 설명" required style={{marginBottom:'5px'}}>
        <InputGroup compact>
        <Badge color="secondary" style={{ ...inputHeader, minWidth:'400px'}}>
          <span style={{display:'block'}}>설명</span>
          <TextArea
            rows={2}
            style={{textAlign:'left'}}
            value={formData.description}
            required
            // name={nameSet.apiDescription}
            onChange={({target})=>{
              setFormData(prevForm=>({
                ...prevForm,
                description : target.value,
              }))
            }}            
          />
          </Badge>
        </InputGroup>
      </Form.Item>


      <Form.Item label="담당자 수정" required style={{marginBottom:'5px'}}>
        <Badge color="secondary" style={{ ...inputHeader, minWidth:'400px'}}>
          <span style={{marginBottom:'1px',display:'block'}}>담당자 선택</span>          
          <Row gutter={[16, 16]}>
          <Col span={12} style={{paddingBottom:'0'}}>
          <Search placeholder="담당자(정)" value={`[${formData.employee_group_name}] ${formData.employee_name}`} readOnly onSearch={()=>{toggleModal()}} enterButton required />
            </Col>
          <Col span={12} style={{paddingBottom:'0'}}>
          <Search placeholder="담당자(부)" value={`[${formData.employee_sub_group_name}] ${formData.employee_sub_name}`} readOnly onSearch={()=>{toggleModal(true)}} enterButton required/>
            </Col>
        </Row>
          </Badge>
          {visible.display && <SearchEmployee 
                            visible={visible} 
                            formData={formData} 
                            toggleModal={toggleModal} 
                            handleSetFormData={setFormData}
                            update={true}
                          />}
      </Form.Item>      
     
      <Form.Item label="파라미터 수정" style={{marginBottom:'5px'}}>
            <Badge color="secondary" style={{...inputHeader, minWidth:'400px'}}>
            <span>Parameter Content-Type</span>
              <Input
                style={{ display: 'block' }}
                value={formData.parameter_type.length>0 &&formData.parameter_type[0]} //// 추후 수정 필요. 배열에 있는거 다 꺼내야함.
                onChange={({target})=>{setFormData(prevState=>({...prevState,parameter_type : [target.value]}))}} // 추후 수정 필요. 배열로 된거 해결해야함.
              />
            </Badge>
        <Button type="dashed" onClick={addParamRow} className="param-header" style={{height:'43px', verticalAlign:'text-bottom'}}>
          <Icon type="plus" style={{fontSize:'1.5rem'}}/>파라미터 추가
        </Button>
      </Form.Item>


  {formData.param !== null && formData.param.length>0 &&
      <Form.Item style={{ display: 'inline' }}>
      <div className="param-header-empty"/>
      {
        [
          {value : '파라미터 이름', width : '25%'},
          {value : '타입', width : '10%'},
          {value : '설명', width : '25%'},
          {value : '데이터 타입', width : '15%'},
          {value : '필수 여부', width : '98px'},
        ].map(data=><Input key={data.value} size='small' className='param-header' style={{width:data.width}} value={data.value} readOnly/>)
      }          
  </Form.Item>
}

{formData.param !== null && formData.param.map((param,index) => 
    <Form.Item key={index} style={{marginBottom:'3px'}}>
      <Icon
        className="dynamic-delete-button"
        type="minus-circle-o"
        onClick={(e) => {
          removeParamRow(index);
        }}
        style={{ color: 'red', marginRight: 8 }}
      />
      <Input
        placeholder="name"
        value={param.name}
        style={{ width: '25%', marginRight: 8 }}
        required
        onChange={({target}) => {
          const paramCopy = _.cloneDeep(param);
          paramCopy.name=target.value;
          setFormData(prevState=>({...prevState, param : prevState.param.map((item,index2)=>index2===index?paramCopy : item)})) //{...param, name: target.value}
        }}
      />
      <Select
        defaultValue="body"
        style={{ width: '10%', marginRight: 8 }}
        value={param.in}
        onChange={value => {
          const paramCopy = _.cloneDeep(param);
          paramCopy.in=value;
          setFormData(prevState=>({...prevState, param : prevState.param.map((item,index2)=>index2===index?paramCopy : item)})) //{...param, name: target.value}
        }}
      >
        <Option value="body">body</Option>
        <Option value="path">path</Option>
        <Option value="query">query</Option>
        <Option value="header">header</Option>
      </Select>
      <Input
        placeholder="description"
        value={param.description}
        style={{ width: '25%', marginRight: 8 }}
        onChange={({target}) => {
          const paramCopy = _.cloneDeep(param);
          paramCopy.description=target.value;
          setFormData(prevState=>({...prevState, param : prevState.param.map((item,index2)=>index2===index?paramCopy : item)})) //{...param, name: target.value}
        }}
      />

      <Select
        defaultValue="string"
        value={param.type===undefined && param.schema !== undefined ? 'object' : param.type}
        style={{ width: '15%', marginRight: 8 }}
        onChange={value => {
          const paramCopy = _.cloneDeep(param);
          if(paramCopy.schema!==undefined) delete paramCopy.schema
          paramCopy.type=value;
          setFormData(prevState=>({...prevState, param : prevState.param.map((item,index2)=>index2===index?paramCopy : item)}))
        }}
      >
        <Option value="string">string</Option>
        <Option value="int">int</Option>
        <Option value="float">float</Option>
        <Option value="double">double</Option>
        <Option value="number">number</Option>
        <Option value="json">json</Option>
        <Option value="binary">binary</Option>
        <Option value="object">object</Option>
      </Select>
      <Checkbox
        checked={param.required}
        onChange={({target}) => {
          const paramCopy = _.cloneDeep(param);
          paramCopy.required=target.checked;
          setFormData(prevState=>({...prevState, param : prevState.param.map((item,index2)=>index2===index?paramCopy : item)})) //{...param, name: target.value}
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
)}


<Form.Item label="응답 입력" required>      
      <Badge color="secondary" style={{...inputHeader, minWidth:'400px'}}>
            <span>Response Content-Type</span>
              <Input
                style={{ display: 'block' }}
                defaultValue="application/json;charset=utf-8"
                value={formData.response_type}
                name="responseType"
                onChange={({target})=>{setFormData(prevState=>({...prevState,response_type : [target.value]}))}} // 추후 수정 필요. 배열로 된거 해결해야함.
              />
            </Badge> 
            <Badge color="secondary" style={{...inputHeader, width:'100px'}}>
            <span>Http Code</span>
              <Input
                style={{ display: 'block' }}
                placeholder="ex)400"
                name="httpCode"
                value={httpCode}
                onChange={
                  e => {
                    const { value } = e.target;
                    const reg = /^-?(0|[1-9][0-9]*)(\.[0-9]*)?$/;
                    if ((!isNaN(value) && reg.test(value)) || value === '' || value === '-') {
                      setHttpCode(value);
                    }
                }
              }
              />
            </Badge> 
            <Button type="dashed" onClick={handleAddTabs} className="param-header" style={{height:'43px', verticalAlign:'text-bottom'}}>
              <Icon type="plus" style={{fontSize:'1.5rem'}}/>응답 클래스 추가
            </Button>
        <div style={{...inputHeader, minWidth:'400px'}}>
        <ResponseListTabs tabs={tabs} handleRemoveTabs={handleRemoveTabs} responseValues={formData.response_list} validResponseValues={validResponseValues}/>
        </div>
      </Form.Item>

      <Form.Item style={{textAlign:'center'}}>
      
              <Button type="primary" htmlType="submit">수정</Button>
   
         
      </Form.Item>

        </Form>
    ),[addParamRow, apiCategoryList, formData, handleAddTabs, handleOnSubmit, handleRemoveTabs, httpCode, removeParamRow, serviceCategoryList, serviceList, tabs, toggleModal, validResponseValues, visible])

    return !loading ? 
              saveLoading ? (
                <Spin tip="Loading..." size="large">
                  {renderForm()}
                </Spin> 
              ) : renderForm()
     :<span style={{ color: 'blue', fontSize: '100px',display:'grid' }}>
        <Icon type="loading" />
    </span>;
};

export default ApiDetailUpdate;