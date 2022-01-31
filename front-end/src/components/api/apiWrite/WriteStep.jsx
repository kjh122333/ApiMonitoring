import React,{useState,useCallback,useRef, useMemo, useEffect} from 'react';
import { Steps, Button,  Form, Tree,Icon, Tooltip,Spin, Tag } from 'antd';
// import { Badge } from 'reactstrap';
import 'lib/css/api/steps.css';
import InputUrl from './InputUrl';
import SelectApiCategory from './SelectApiCategory';
import SelectApi from './SelectApi';
import Check from './Check';
import {formDataNameSet as nameSet} from 'containers/api/ApiWriteContainer'

const { Step } = Steps;
const { TreeNode } = Tree;

const stepStyle = {
  marginBottom: 15,
  boxShadow: '0px -1px 0 0 #e8e8e8 inset',
};

export const renderMethodBage = (method,toolTipText='')=>{
  let color = '';
  if(method === 'get') color = 'blue'
  else if(method === 'post') color = 'green'
  else if(method === 'delete') color = 'red'
  else if(method === 'put') color = 'gold'
  else if(method === 'patch') color = 'purple'
  else if(method === 'head') color = 'cyan'
  else if(method === 'options') color = 'cyan'
  
  return toolTipText === '' 
                        ? <Tag key={method} color={color} style={{fontWeight:'bold'}}>{method}</Tag>
                        : (
                          <Tooltip placement="right" title={<span style={{color:'white'}}>{toolTipText}</span>} style={{color:'black'}}>
                             <Tag key={method} color={color} style={{fontSize:'100%',fontWeight:'bold'}}>{method}</Tag>
                          </Tooltip>                
                        )
}


const WriteStep = ({selected, handleSubmit,resData,initalizeResData,checkLoading,
  saveLoading,serviceCategoryList, groupNameList,formData,handleSetFormData,initalizeFormData,
  validServiceUrlStep, validApiCategoryStep, validApiStep,renderEmployee,visible,toggleModal,overLapLoading,showConfirm,deleteFormDataKey}) => {

  /* Step의 데이터 */
  const stepsData = useRef([
    {key:0, title : '서비스 URL 등록', },
    {key:1, title : 'API 카테고리 선택',},
    {key:2, title : 'API 선택',},
    {key:3, title : '확인',},
  ]);

  //선택된 라디오 버튼에 따라 표시할 스탭 결정.
  const calcStep = useMemo(()=>{
    const data = stepsData.current;     
      return {
        steps : selected.type==='import' ? data.slice(0,selected.key+1).concat(data[data.length-1]) : [],
        lastStep : selected.key+1,
      }
  },[selected]);

  const [steps, setSteps] = useState([]);
  const [currentStep, setCurrentStep] = useState(0);
  

  //라디오 버튼 선택 될 때마다 현재 현재 스탭과 출력 해 줄 steps 초기화
  useEffect(()=>{
    setSteps(calcStep.steps);
    setCurrentStep(0);
    initalizeFormData();
    initalizeResData({});
  },[calcStep, handleSetFormData, initalizeFormData, initalizeResData]);


   //Next버튼 클릭 이벤트(현재 스탭 +1)
   const nextStep = useCallback(()=>{
    setCurrentStep(currentStep+1);    
    window.scrollTo(0, 0);
  },[currentStep]);

  //Previous버튼 클릭 이벤트(현재 스탭 -1)
  const prevStep = useCallback(()=>{
    setCurrentStep(currentStep-1); 
    window.scrollTo(0, 0);
  },[currentStep]);
  
  //Next버튼 클릭 이벤트
  const handleNext = useCallback(async () =>{
    if(currentStep===0){
      if(await validServiceUrlStep()){
        //console.log('카테고리 선택으로');
        nextStep();
      }
    } else if(currentStep===1){
      if(validApiCategoryStep()){
        //console.log('api 선택으로');
        nextStep();
      }
    } else if(currentStep===2){
      if(validApiStep()){
        //console.log('확인으로');
        nextStep();
      }
    } else {
      //있을 수 없음.
      //currentStep가 3 일때는 next 버튼 없음. done 버튼으로 이벤트.
    }
  },[currentStep,nextStep,validServiceUrlStep,validApiCategoryStep,validApiStep]);
 

  //현재 steps에 따라 steps의 status의 상태를 결정
  const getStepStauts = useCallback((thisStep)=>{
      if(thisStep<currentStep) return "finish"
      else if(thisStep===currentStep) return "process"
      else return "wait" 
  },[currentStep]);

  //Step 컴포넌트 렌더링. 선택된 라디오 버튼 값의 type이 'import'일 때만 호출.
  const renderSteps = useMemo(()=>(
    <Steps type="navigation" current={currentStep} onChange={(current)=>{setCurrentStep(current)}} style={stepStyle}>
        {steps.map((step,index)=>( 
            <Step key={step.key} status={getStepStauts(step.key)} title={step.title} disabled/>
        ))}
    </Steps>
    ),[steps,getStepStauts,currentStep]);
    
    //트리 체크된 키들 저장.
    const [treeCheckedKeys , setTreeCheckedKeys] = useState([]);

    //트리 onCheck
    const handleOnCheck = useCallback(checkedKeys=>{
      setTreeCheckedKeys(checkedKeys);
      //formData에는 root와 parent 트리 노드는 제외하고 leaf 노드인 api들만 넣음.
      const selectedApiKeys = checkedKeys.filter(key=>JSON.parse(key).node==='leaf')
                                          .map(key=>JSON.parse(key));
      const apiListInCategory = formData[nameSet.apiListInCategory].slice();

      ////////////////////////////////////////
      const selectedApiList = [];
      selectedApiKeys.forEach( key=>{
        const {path, method} = key;
        const api =  apiListInCategory.find( apiInCategory => apiInCategory.path === path );
        if(api!==undefined && api.method[method]!==undefined){
          const found = {
            path,
            method,
            data : api.method[method],
            employee : '',
            employeeSub:'',
          }
          selectedApiList.push(found)
        }
      })
      handleSetFormData({target:{name : nameSet.selectedApiList, value : selectedApiList}})
    },[formData, handleSetFormData]);

    //apiList 트리 렌더링
    const renderApiList = useCallback((checkable=false,defaultExpandAll=true)=>{
      const apiList = formData[nameSet.apiListInCategory];
      const categoryName = formData[nameSet.apiCategoryName];
      return apiList !== undefined && apiList.length>0 && (
        <Tree
        showIcon
        switcherIcon={<Icon type="down"/>}
        selectable={false}
        checkable={checkable}
        defaultExpandAll={defaultExpandAll}
        defaultExpandedKeys={[JSON.stringify({node : 'root'})]}
        onCheck={e=>handleOnCheck(e)}
        style={{textAlign : 'left'}}
        >
          <TreeNode title={<span style={{fontWeight:"bold", fontSize:"15px", }}>{categoryName}</span>} key={JSON.stringify({node : 'root'})} icon={<Icon type="cluster" style={{fontSize:'150%'}}/>} >
          {apiList.map(api=>(
            <TreeNode title={<span  style={{fontWeight:"bold", fontSize:"13px", }}>{api.path}</span>} key={JSON.stringify({node : 'parent', path:api.path})} icon={<Icon type="global"/>}>
              {Object.keys(api.method).map(method=>(
                <TreeNode title={renderMethodBage(method,api.method[method].description?api.method[method].description : api.method[method].summary)} key={JSON.stringify({node : 'leaf', path:api.path, method})} isLeaf={true} icon={<Icon type="api"/>}/>
                
                ))}              
            </TreeNode>
          ))}            
          </TreeNode>
        </Tree>
)
},[formData, handleOnCheck]);

//Content 렌더링.
const renderContent = useMemo(()=>{
  if(currentStep===calcStep.lastStep){
    return <Check key={3} formData={formData} selected={selected}  resData={resData} renderMethodBage={renderMethodBage} />
  }else{
    if(currentStep===0) return <InputUrl key={0} formData={formData}  handleSetFormData={handleSetFormData} deleteFormDataKey={deleteFormDataKey} serviceCategoryList={serviceCategoryList}
    groupNameList={groupNameList}/>;
    else if(currentStep===1) return <SelectApiCategory key={1} formData={formData}  handleSetFormData={handleSetFormData} resData={resData} renderApiList={renderApiList}/>;
    else if(currentStep===2) return <SelectApi key={2} renderApiList={renderApiList} resData={resData} formData={formData} handleSetFormData={handleSetFormData} renderEmployee={renderEmployee} visible={visible} toggleModal={toggleModal}/>;
    // else if(currentStep===3) return ;
    else return null;      
  }
},[currentStep, calcStep.lastStep, formData, selected, resData, handleSetFormData, deleteFormDataKey, serviceCategoryList, groupNameList, renderApiList, renderEmployee, visible, toggleModal]);

  //console.log('--------------------------');
  //console.log('rrrrrrr',formData);

  const renderForm = useCallback(()=>(
    <Form onSubmit={e=>{
      e.preventDefault();
      //console.log('서브밋됨');
      handleSubmit();
    }}>
      {selected.type==='import' && renderSteps }

    <div className="steps-content" style={{padding:'10px 5px', margin:'0'}}>
      {selected.type==='import'&& renderContent}
    </div>
      <div className="steps-action" style={{display:'inline-flex'}}>          
        {currentStep > 0 && selected.type === 'import' && (
          <Button style={{ marginLeft: 8 }} onClick={() => prevStep()}>
            이전
          </Button>
        )}

        {currentStep < steps.length - 1 && selected.type === 'import' && (
            <Button type="primary" onClick={() => handleNext()}>
              다음
            </Button>
        )}

        {currentStep === steps.length - 1 && selected.type === 'import' && (
            <Button htmlType="submit" type="primary" onClick={()=>{handleSetFormData({target:{name : 'updateEmployeeId', value : sessionStorage.getItem('id')}})}}>
                확인
            </Button>
        )}
      </div>
    </Form>   
  ),[currentStep, handleNext, handleSetFormData, handleSubmit, prevStep, renderContent, renderSteps, selected.type, steps.length])
  
  return (checkLoading || overLapLoading || saveLoading)?  (
    <Spin size="large" tip="Loading...">
      {renderForm()}
      </Spin>
  ) : renderForm();
  
};

export default WriteStep;