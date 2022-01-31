import React,{useState,useCallback, useEffect,useMemo} from 'react';
import WriteStep from 'components/api/apiWrite/WriteStep';
import WriteType from 'components/api/apiWrite/WriteType';
import WriteManual from 'components/api/apiWrite/WriteManual';
import axios from 'axios';
import {withRouter} from 'react-router-dom';
import {Icon, List,  Row, Input,Col,Modal } from 'antd';
import handleException from 'lib/function/request/handleException.js';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep.jsx';
const InputGroup = Input.Group;
const { Search } = Input;
const { confirm } = Modal;


const radioValues = [
    {key:0, name:"Service", krName:"서비스", type:"import"},
    {key:1, name:"apiCategory",krName:"Api 카테고리", type:"import"},
    {key:2, name:"api", krName:"Api", type:"import"},
    {key:3, name:"manual", krName:"직접입력", type:"write"}
];



//name set을 통해 formData state의 속성명을 관리.
export const formDataNameSet ={
    serviceCategoryNo : 'serviceCategoryNo', //서비스 카테고리 번호(최상위 카테고리) 
    serviceCategoryName : 'serviceCategoryName', //서비스 카테고리 이름(최상위 카테고리) 
    serviceNo : 'serviceNo', //서비스 번호
    serviceName : 'serviceName',//서비스 이름
    apiCategoryNo : 'apiCategoryNo', //api 카테고리 번호
    apiCategoryName : 'apiCategoryName', //선택된 api 카테고리,
    apiListInCategory : 'apiListInCategory', //선택된 api 카테고리에 포함된 api 목록
    selectedApiList : 'selectedApiList', //사용자가 선택한 api 목록
    swaggerUrl : 'swaggerUrl', //swagger url 
    swaggerFile : 'swaggerFile', //swagger file data 
    swaggerData : 'swaggerData', //swagger json data
    swaggerCheck: 'swaggerCheck', //유효한 swagger 확인.
    importType : 'importType', //등록 타입 'url' or 'file'
    fileName : 'fileName',
    groupNo : 'groupNo', // 담당 부서 번호
    groupName : 'groupName', // 담당 부서
    employee : 'employee', // 담당자(정)
    employeeSub : 'employeeSub', // 담당자(부)

    apiUrl : 'apiUrl', //api 주소 - 직접입력 폼
    apiMethod : 'apiMethod', //api 메서드 - 직접입력 폼
    apiDescription : 'apiDescription', //api 설명 - 직접입력 폼
}

const ApiWriteContainer = ({history}) => {
    
    const [selected, setSelected] = useState(radioValues[0]); //선택된 라디오버튼 값
    const [formData, setFormData] = useState({
      [formDataNameSet.importType] : 'url',
    }); //폼 데이터
    const [resData, setResData] = useState({}); //checkSwagger() 을 통해 swagger url이 확인 될 경우 response data를 담음
    const [checkLoading , setCheckLoading] = useState(false);
    const [saveLoading, setSaveLoading] = useState(false);
    const [overLapLoading, setOverLapLoading] = useState(false);
    const [loading, setLoading] = useState(false); //로딩 상태
    const [serviceCategoryList, setServiceCategoryList] = useState([]); //서비스 카테고리 이름 목록
    const [serviceList, setServiceList] = useState([]); //서비스 이름 목록
    const [apiCategoryList, setApiCategoryList] = useState([]); //api 카테고리 이름 목록
    const [groupNameList, setGroupNameList] = useState([]); //그룹 이름 목록
    
    //console.log('컨테이너리렌더링 폼데이터:',formData);
    useEffect(()=>{
        const getServiceCateList = async () =>{     
            try{
              setLoading(true);

              const resServiceCategorys = await axios.get('http://15.165.25.145:9500/user/category/f');
              //console.log('[컨]서비스카테고리네임리스트받음',resServiceCategorys);
              setServiceCategoryList(resServiceCategorys.data.success ? resServiceCategorys.data.list : []);
              
              const resServices = await axios.get('http://15.165.25.145:9500/user/service/f');
              //console.log('[컨]서비스네임리스트받음',resServices);
              setServiceList(resServices.data.success ? resServices.data.list : []);
              
              const resApiCategorys = await axios.get('http://15.165.25.145:9500/user/apicategory/f');
              //console.log('[컨]APi카테고리네임리스트받음',resApiCategorys);
              setApiCategoryList(resApiCategorys.data.success ? resApiCategorys.data.list : []);
              
              const resGroupNames = await axios.get('http://15.165.25.145:9500/user/groupname');
              //console.log('[컨]그룹네임리스트받음',resGroupNames);
              setGroupNameList(resGroupNames.data.success ? resGroupNames.data.list : []);
              setLoading(false);
            }catch(error){
             handleException(error,history)
            }
           }
           getServiceCateList();
           
    },[history])
   
    //라디오 버튼 onChange       
    const changeSelected = useCallback((e)=>{ 
        setSelected(e.target.value);
    },[]);

    //응답 데이터 초기화
    const initalizeResData = useCallback(()=>{
        setResData({})
    },[]);

    //폼데이터 초기화
    const initalizeFormData = useCallback(()=>{
        setFormData({
          [formDataNameSet.importType] : 'url',
        });
    },[])

    //폼데이터 키 삭제
    const deleteFormDataKey = useCallback(key=>{
      if(formData.hasOwnProperty(key)){
        setFormData(prevForm=>{
          delete prevForm[key];
          return prevForm;
        })
      }
    })
    
    //input 변경 이벤트 공통 호출
    const handleSetFormData = useCallback((e)=>{     
        const {name, value} = e.target;
        setFormData(prevFormData=>({...prevFormData, [name] : value}))
    },[]);
 
    //form 데이터 빈 값(?) 검사
    const isUndefined = useCallback((requiredNames)=>{
        const undefinedFields = requiredNames.filter((name=>{
            const value =  typeof formData[name] === 'string' ? formData[name].trim() : formData[name];
            return value===undefined || value===""? true : false
        }))
        //console.log('언디파인 필드 목록',undefinedFields);
        return undefinedFields.length>0 ? true : false;
    },[formData]);

    const nullToObject = useCallback(swaggerData=>{
      for (const key in swaggerData) {
        if (swaggerData.hasOwnProperty(key)) {
          if(swaggerData[key]===null){
            swaggerData[key]={}
          }
        }
      }
    },[])

    //스와거 url, file 체크
    const checkSwagger = useCallback(async ()=>{
        setCheckLoading(true);
        try{
            //console.log('url체크 서브밋 데이터',formData);
            const res = await axios.post('http://15.165.25.145:9500/swagger-api-docs-json/check',formData);
            //console.log('ressssssss',res);
            if(res.data.swaggerCheck && res.data.sameCheck){ 
              const {swaggerData} = res.data;
              nullToObject(swaggerData)
                setResData(swaggerData);
                return true
            }else if(!res.data.swaggerCheck){ //스와거 url이 아닐때
              
              Modal.error({
                title: '올바른 데이터가 아닙니다.',
                content: '등록한 URL 또는 파일을 확인하세요.'
              });
                setResData({});
                return false;
            }else if(!res.data.sameCheck){ //서비스 카테고리, 담당부서 불일치

              Modal.error({
                title: '서비스 카테고리 혹은 담당부서가 일치하지 않습니다.',
                content: `해당 서비스는 ${res.data.properServiceCategoryName} 카테고리의 ${res.data.properGroupName} 부서로 등록해주십시오.`
              });
                setResData({});
                return false;
            };
          }catch(error){
            handleException(error,history)
            return false;
          }finally{
            setCheckLoading(false);
          }
    },[formData, history, nullToObject]);

      //서비스 url 등록 단계 유효성 검사(step0)
  const validServiceUrlStep = useCallback(async ()=>{
    //console.log('서비스 url 등록 단계 유효성 검사');
    const requireds = [
      formDataNameSet.serviceCategoryNo,
      formDataNameSet.groupName,
      formData[formDataNameSet.importType]==='url' ? formDataNameSet.swaggerUrl : formDataNameSet.swaggerFile,
    ]
    if (
      !isUndefined(requireds)
    ) {
      return (await checkSwagger())
        ? true
        : false;
    } else {
      Modal.error({
        title: '모든 항목을 입력해주십시오.',
      });
    }
  },[checkSwagger, formData, isUndefined]);

  //Api 카테고리 선택 단계 유효성 검사(step1)
  const validApiCategoryStep = useCallback(()=>{
    //console.log('Api 카테고리 선택 단계 유효성 검사');
    const requireds = [
      formDataNameSet.serviceCategoryNo,
      formDataNameSet.groupName,
      // formDataNameSet.swaggerUrl,
      formData[formDataNameSet.importType]==='url' ? formDataNameSet.swaggerUrl : formDataNameSet.swaggerFile,
      formDataNameSet.apiCategoryName,
      formDataNameSet.apiListInCategory,
    ];
    if(isUndefined(requireds)){
      Modal.error({
        title: '모든 항목을 입력해주십시오.',
      });
      return false;
    }else{
      return true;
    }
  },[formData, isUndefined]);

  //Api 선택 단계 유효성 검사(step2)
  const validApiStep = useCallback(()=>{
    //console.log('Api 선택 단계 유효성 검사');
    const requireds = [
      formDataNameSet.selectedApiList,
      formDataNameSet.employee,
      formDataNameSet.employeeSub,
    ];
    if(validApiCategoryStep() && !isUndefined(requireds)){
      return true;
    }else{
      Modal.error({
        title: '모든 항목을 입력해주십시오.',
      });
      return false;
    }
  },[isUndefined, validApiCategoryStep]);


  const showConfirm=useCallback(resData=>{
    //console.log('쇼컨펌펌컨펌컨',resData);
    //배열 3개 안넘오는거 때문에 디폴트 걸어놓음 191230-11:13
    const {isExpectedCreate=[], isExpectedUpdate=[], isExpectedDelete=[]} = resData;
    
    confirm({
      title: '아래의 작업이 진행됩니다.',
      width: 1100,
      content: (
        <Row gutter={[8,0]}>
          <br/>
            <Col span={8}>
              <p style={{ fontSize:'110%',fontWeight: 'bold', margin:0, textAlign:'center'}}>추가되는 데이터 {isExpectedCreate.length}개</p>
              <List
                size="small"
                style={{minHeight:'550px'}}
                bordered
                dataSource={isExpectedCreate}
                renderItem={createApi => (
                  <List.Item>
                    {renderMethodBage(createApi.method)}{' '}
                    <span style={{ fontWeight: 'bold' }}>
                      {createApi.api_url}
                    </span>
                  </List.Item>
                )}
                pagination={{ pageSize: 10,position:'top', simple:true  }}
              />
            </Col>

            <Col span={8}>
              <p style={{ fontSize:'110%',fontWeight: 'bold', margin:0, textAlign:'center' }}>최신화되는 데이터 {isExpectedUpdate.length}개</p>
               <List
                size="small"
                style={{minHeight:'550px'}}
                bordered
                dataSource={isExpectedUpdate}
                renderItem={updateApi => (
                  <List.Item>
                    {renderMethodBage(updateApi.method)}{' '}
                    <span style={{ fontWeight: 'bold' }}>
                      {updateApi.api_url}
                    </span>
                  </List.Item>
                )}
                pagination={{ pageSize: 10,position:'top', simple:true  }}
              />
            </Col>

            <Col span={8}>
              <p style={{ fontSize:'110%',fontWeight: 'bold', margin:0, textAlign:'center' }}>삭제되는 데이터 {isExpectedDelete.length}개</p>
               <List
                size="small"
                style={{minHeight:'550px'}}
                bordered
                dataSource={isExpectedDelete}
                renderItem={deleteApi => (
                  <List.Item>
                    {renderMethodBage(deleteApi.method)}{' '}
                    <span style={{ fontWeight: 'bold' }}>
                      {deleteApi.api_url}
                    </span>
                  </List.Item>
                )}
                pagination={{ pageSize: 10,position:'top', simple:true  }}
              />
            </Col>
        </Row>
      ),
      async onOk() {
        const saveRequest = async () => {
          const domain = 'http://15.165.25.145:9500';
          const saveUrl = domain + '/swagger-api-docs-json/save';
          try {
            setSaveLoading(true);
            //console.log('저장저장 폼 데이터 :::', formData);

            const res = await axios.post(saveUrl, formData);
            //console.log('resssss', res);

            if (res.data.saveSuccess) {
              
              Modal.success({
                title: 'API등록을 완료했습니다.',
              });
              history.push('/apiList');
              window.scrollTo(0, 0);
            } else {
              Modal.error({
                title: 'API등록을 실패했습니다.',
              });
            }
          } catch (error) {
            handleException(error, history);
          } finally {
            setSaveLoading(false);
          }
        };
        saveRequest();
      },
      onCancel() {
        //console.log('Cancel');
      },
    });
  },[formData, history])

    // 서비스, api카테고리, api 등록 요청
    const handleSubmit= useCallback(()=>{
      //  const domain =
      //    'http://15.165.25.145:9500';
      //  const overLapUrl =
      //    domain +
      //    '/swagger-api-docs-json/overlap';

       /* api 중복 검사 요청*/
       const overLapCheck = async () => {
         try {
           setOverLapLoading(true);
           //console.log('중복중복 폼 데이터 :::', formData);

           const res = await axios.post(
            'http://15.165.25.145:9500/swagger-api-docs-json/overlap',
             formData,
           );
           //console.log('중복중복resssss', res);

           showConfirm(res.data);
         } catch (error) {
           handleException(error, history);
         } finally {
           setOverLapLoading(false);
         }
       };

       overLapCheck();
     },[formData, history, showConfirm])

    const handleManualSubmit = useCallback(postData=>{
        const url = 'http://15.165.25.145:9500/user/api'

        const request = async () => {
          //console.log('서브밋 데이터', postData);
          try{
            setSaveLoading(true);
            const res = await axios.post(url, postData);
            //console.log(res);
            if(res.data.success){

            Modal.success({
              title: 'API등록을 완료했습니다.',
            });
            history.push('/')
            window.scrollTo(0, 0);
            }else{
              Modal.error({
                title: 'API등록을 실패했습니다.',
                content: res.data.message
              });

            }
          }catch(error){
           handleException(error,history)
          }finally{
            setSaveLoading(false)
          }
        }

        request();
    },[history])


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


    const renderEmployee = useCallback(()=>{
      const employeeName = formData[formDataNameSet.employee] !== undefined ? formData[formDataNameSet.employee].name : '';
      const employeeGroup = employeeName !== '' ? formData[formDataNameSet.employee].group_name:'';
      const employeeSubName = formData[formDataNameSet.employeeSub] !== undefined ? formData[formDataNameSet.employeeSub].name : '';
      const employeeSubGroup = employeeSubName !== '' ? formData[formDataNameSet.employeeSub].group_name:'';

    
      return(
        <Row gutter={[16, 16]}>
          <Col span={12} style={{paddingBottom:'0'}}>
          <Search placeholder="담당자(정)" value={employeeName&&`[${employeeGroup}] ${employeeName}`} readOnly onClick={()=>{toggleModal()}} onSearch={()=>{toggleModal()}} enterButton required />
            </Col>
          <Col span={12} style={{paddingBottom:'0'}}>
          <Search placeholder="담당자(부)" value={employeeSubName&&`[${employeeSubGroup}] ${employeeSubName}`} readOnly onClick={()=>{toggleModal(true)}} onSearch={()=>{toggleModal(true)}} enterButton required/>
            </Col>
        </Row>
      )
    },[formData, toggleModal]);

    return !loading ? (
        <>
            <WriteType selected={selected} changeSelected={changeSelected} radioValues={radioValues} />
                {selected.key !== 3 
                    ? <WriteStep 
                        selected={selected} 
                        handleSubmit={handleSubmit} 
                        formData={formData}
                        handleSetFormData={handleSetFormData}
                        initalizeFormData={initalizeFormData}
                        resData={resData}
                        checkLoading={checkLoading}
                        saveLoading={saveLoading}
                        initalizeResData={initalizeResData}
                        deleteFormDataKey={deleteFormDataKey}
                        serviceCategoryList={serviceCategoryList}
                        groupNameList={groupNameList}
                        validServiceUrlStep={validServiceUrlStep}
                        validApiCategoryStep={validApiCategoryStep}
                        validApiStep={validApiStep}
                        renderEmployee={renderEmployee}
                        visible={visible}
                        toggleModal={toggleModal}
                        overLapLoading={overLapLoading}
                        showConfirm={showConfirm}
                    />
                    :   <WriteManual
                            serviceCategoryList={serviceCategoryList}
                            serviceList={serviceList}
                            apiCategoryList={apiCategoryList}
                            formData={formData}
                            handleSetFormData={handleSetFormData}
                            history={history}
                            renderEmployee={renderEmployee}
                            visible={visible}
                            toggleModal={toggleModal}
                            handleManualSubmit={handleManualSubmit}
                            initalizeFormData={initalizeFormData}
                            saveLoading={saveLoading}
                            isUndefined={isUndefined}
                        />
                }
        </>        
    ) : <span style={{ color: 'blue', fontSize: '100px' }}>
            <Icon type="loading" />
        </span>;
};

export default withRouter(ApiWriteContainer);