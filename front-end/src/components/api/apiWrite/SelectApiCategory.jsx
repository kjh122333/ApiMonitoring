import React, { useState, useCallback, useMemo, useEffect } from 'react';
import { Radio,  Form, Row, Card, Col, Empty  } from 'antd';
import {cardBody} from 'lib/css/api/cardStyle.js';
import {formDataNameSet as nameSet} from 'containers/api/ApiWriteContainer';
const radioStyle = {
  display: 'block',
  height: '30px',
  lineHeight: '30px',
};

const SelectApiCategory = ({formData, handleSetFormData, resData, renderApiList}) => {
  const [selectedRadio, setSelectedRadio] = useState();
  const [apiInCategory, setApiInCategory] = useState([]);

  useEffect(()=>{
      //apiCategory를 선택하고 이전 스탭으로 돌아갔다가 다시 api 카테고리 스탭으로 왔을 경우. 
      //formData에서 이에 선택해 놓은 apiCategory를 지움.
      handleSetFormData({target : { name : nameSet.apiCategoryName, value : undefined}})
      handleSetFormData({target : { name : nameSet.apiListInCategory, value : undefined}})
  },[handleSetFormData]);

       const calcApiList = useCallback((categoryName)=>{
        const childApis = []; // 선택된 api 카테고리에 해당하는 api들 저장.
         
        if(categoryName!==undefined){
          const paths = Object.keys(resData.paths);
          paths.forEach(path=>{
            let childApi={};
            const methods = resData.paths[path]; //ex) {get:{}, post:{}, delete{} ...}
            const methodList = Object.keys(methods); //ex) [ get, post, delete, ...]
            
            methodList.forEach(method =>{ //ex) methods[method]={ summary:~ , description:~, response:~, tags:~ ...}
              if(methods[method].tags.includes(categoryName)){
                childApi = {
                  path, //ex) path : /user/login
                  'method' : { //ex) method : { get:{}, post: {} ... }
                    ...childApi.method,
                    [method] : methods[method]
                  }
                }
              }
            })
            if(Object.keys(childApi).length!==0) childApis.push(childApi);
          })
          setApiInCategory(childApis);
        }
          // //console.log('childApis',childApis);
          return childApis;
      },[resData.paths]);

  const changeRadio = useCallback((e) => {
    setSelectedRadio(e.target.value);
    handleSetFormData({target : {name : nameSet.apiCategoryName, value : e.target.value}});
    const apiList = calcApiList(e.target.value);
    handleSetFormData({target : {name : nameSet.apiListInCategory, value : apiList}})
  }, [calcApiList, handleSetFormData]);

  const renderRadio = useMemo(()=>{
    const resDataTagsSort = Object.keys(resData.tags).length>0 ? [...resData.tags] : []; //apiCategoryName 들 복사 후 정렬(원본데이터 수정x)
    resDataTagsSort.sort((obj1, obj2)=>(obj1.name < obj2.name ? -1 : obj1.name > obj2.name ? 1 : 0))
        
    return (
      resDataTagsSort.map(apiCategory =>(
      <Radio key={apiCategory.name} style={radioStyle} value={apiCategory.name}>
        <span>
          <span style={{fontWeight:"bold", fontSize:"15px", }}>{apiCategory.name}</span>
          &nbsp;({apiCategory.description})
        </span>
      </Radio>
    ))
  )},[resData]);

  return (
    <Form.Item style={{ background: '#FAFAFA', padding: '0 30px 0 30px' }}>
      <Row gutter={[16, 8]}> 
        <Col span={16}>
      {/* <Affix offsetTop={top}>     */}
          <Card 
            title="Api 카테고리 선택" 
            bordered={true} 
            // bodyStyle={{...cardBody, maxHeight:'400px',overflowY:'auto'}}
            bodyStyle={cardBody}
            >
            <Radio.Group
              onChange={changeRadio}
              value={selectedRadio}
              style={{textAlign : 'left'}}
            >
              {resData && resData.tags.length > 0 && renderRadio}
            </Radio.Group>
          </Card>
        {/* </Affix> */}
        </Col>
        <Col span={8}>
          <Card 
            title={selectedRadio ? `${selectedRadio}의 Api 목록`:"카테고리를 선택하세요"}
            // bodyStyle={cardBody}
            bodyStyle={cardBody}
            bordered={true}>
            {selectedRadio && apiInCategory.length>0 ? renderApiList() : <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />}
          </Card>
        </Col>
      </Row>
    </Form.Item>
  );
};

export default SelectApiCategory;