import React,{useMemo,useCallback} from 'react';
import { Form, Descriptions } from 'antd';
import 'lib/css/api/discription.css';
import {formDataNameSet as nameSet} from 'containers/api/ApiWriteContainer';
// const itemStyle = ".ant-descriptions-bordered .ant-descriptions-item-label"
const itemStyle = {maxHeight:'500px',overflowY:'auto', padding :'16px 0px' };

const Check = ({ formData, selected, resData,renderMethodBage }) => {
  // //console.log('selected', selected);
  const swaggerInfo = useMemo(()=>({
    host : resData.host,
    title : resData.info.title,
    basePath : resData.basePath 
  }),[resData])

  const renderContent = useCallback((key)=>{
    return (
        <span className="descriptions-content">
            {formData[key]}
        </span>
    )    
  },[formData]);

  const renderApiList = useCallback(()=>{
    
    if(selected.key === 2){
      const renderedPath = []; //이미 렌더링이 결정된 path 들
      const renderList = formData[nameSet.selectedApiList].map((api,index) => {
        if(renderedPath.find(path=>path===api.path)===undefined){
          const item = <p key={api.path+index} className="descriptions-content" style={{textAlign:'left', paddingLeft:'15%'}}>
                            ● http://{swaggerInfo.host}{swaggerInfo.basePath!=='/'?swaggerInfo.basePath:''}{api.path}
                            &nbsp;{formData[nameSet.selectedApiList].filter(api2=>api2.path===api.path).map(api2=>renderMethodBage(api2.method))}<br/>
                        </p>
          renderedPath.push(api.path);
          return item;
        }
      })
      
      return(
        <Descriptions.Item  label="Api 목록" span={2}>
          <div style={itemStyle}>
            {renderList.map(item=>item)}
          </div>
        </Descriptions.Item>
      )
    }
    
    return(
    <Descriptions.Item  label="Api 목록" span={2}>
      <div style={itemStyle}>
        {selected.key===1 && formData[nameSet.apiListInCategory].map(api=>(
            <p key={api.path} className="descriptions-content" style={{textAlign:'left', paddingLeft:'15%'}}>
                ● http://{swaggerInfo.host}{swaggerInfo.basePath!=='/'?swaggerInfo.basePath:''}{api.path}
                &nbsp;{Object.keys(api.method).map(method=>renderMethodBage(method))}<br/>
            </p>
        ))}
        </div>
    </Descriptions.Item>
  )},[formData, renderMethodBage, selected.key, swaggerInfo]);

  const renderApiCategoryList = useCallback(()=>(
    <Descriptions.Item  label={<><span>포함된 Api 카테고리</span><br/><span>목록</span></>} span={2}>
      <div style={itemStyle}>
        {resData.tags.map(category=>(
            <p key={category.name} className="descriptions-content" style={{textAlign:'left', paddingLeft:'15%'}}>
                ● {category.name} ({category.description})
            </p>
        ))}
        </div>
    </Descriptions.Item>
  ),[resData]);

  const renderEmployees = useCallback(()=>(
    
    [
      <Descriptions.Item key="employee" label="담당자(정)" >
          {`[${formData[nameSet.employee].group_name}] ${formData[nameSet.employee].name}`}
      </Descriptions.Item> ,
      <Descriptions.Item key="employeeSub"  label="담당자(부)" >
          {`[${formData[nameSet.employeeSub].group_name}] ${formData[nameSet.employeeSub].name}`}
      </Descriptions.Item>
    ]
  ),[formData])

  return (
    <Form.Item style={{ padding: '0 10% 0 10%' }}>
      <Descriptions
        title={`${selected.krName} 등록 정보`}
        bordered
        column={2}
        colon={false}
      >
        <Descriptions.Item label="서비스 카테고리">        
          {renderContent(nameSet.serviceCategoryName)}
        </Descriptions.Item>
        <Descriptions.Item label="담당 부서">        
          {renderContent(nameSet.groupName)}
        </Descriptions.Item>
        <Descriptions.Item span={2} label="Swagger URL">        
          {renderContent(nameSet.swaggerUrl)}
        </Descriptions.Item>
        <Descriptions.Item label="Swagger Info" span={2}>
          {/* <p className="descriptions-content" style={{textAlign:'left', paddingLeft:'15%'}}> */}
          <div className="descriptions-content">
            <p style={{marginTop:'10px',marginBottom:'5px'}}>Host : {swaggerInfo.host}</p>
            <p style={{marginBottom:'5px'}}>Title : {swaggerInfo.title} </p>
            <p style={{marginBottom:'10px'}}>BasePath : {swaggerInfo.basePath}</p>
          </div>
          {/* </p> */}
        </Descriptions.Item>

        {selected.key===0 && renderApiCategoryList()}

        {selected.key === 2 && renderEmployees().map(item=>item)}

        {selected.key > 0 && formData[nameSet.apiCategoryName] !== undefined && (
            <Descriptions.Item  label="Api 카테고리" span={2}>
                {renderContent(nameSet.apiCategoryName)}
            </Descriptions.Item>
        )}

        {selected.key >= 1 && formData[nameSet.apiListInCategory] !== undefined && renderApiList()}
        
    
      </Descriptions>
      
    </Form.Item>
  );
};

export default Check;
