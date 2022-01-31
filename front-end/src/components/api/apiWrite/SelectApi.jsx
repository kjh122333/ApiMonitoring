import React,{useCallback, useEffect, useState} from 'react';
import { Card, Row, Col, Empty,Input } from 'antd';
import { renderMethodBage } from './WriteStep';
import {cardBody} from 'lib/css/api/cardStyle.js';
import SearchEmployee from './SearchEmployee';
import {formDataNameSet as nameSet} from 'containers/api/ApiWriteContainer';
const InputGroup = Input.Group;


const SelectApi = ({ renderApiList, formData, handleSetFormData, renderEmployee,visible, toggleModal }) => {
  // //console.log('셀렉트메이피아이');
  


  useEffect(()=>{
    //console.log('마운트');
    
    handleSetFormData({target:{name:nameSet.selectedApiList, value : undefined}});
    handleSetFormData({target:{name:nameSet.employee, value : undefined}});
    handleSetFormData({target:{name:nameSet.employeeSub, value : undefined}});
    // setCards([]);
  },[handleSetFormData]);

    const makeCard = useCallback(api=>{
      const {path, method, data} = api;
      const methodType = renderMethodBage(method);
      const title = (
          <span>{methodType} {path}</span>
      )
      return(
        <Card
          key={`[${method}]${path}`}
          title={title}
          bordered={true}
          style={{border:'1px dashed #6C757D'}}
          headStyle={{padding:'0'}}
          bodyStyle={{maxHeight:'150px', padding:'15px 10px'}}
            >
              <Row gutter={[16, 16]}>
                <Col span={24}>
                {data.description? data.description : data.summary}
                </Col>
              </Row>   
        </Card>
    )},[]);

    const renderCardList = useCallback(()=>{
      const selectedApiList = formData[nameSet.selectedApiList];
      // //console.log(selectedApiList);
      
      const length = selectedApiList.length;
      const rowCount = length%2 === 0 ? length/2 : Math.floor(length/2)+1;
      // //console.log('rowCount',rowCount);
      const rows=[];
      for (let i = 0; i < rowCount; i++) {
        // //console.log('i*2',selectedApiList[i*2]);
        // //console.log('i*2+1',selectedApiList[i*2+1]);
        
        rows.push(
          <Row key={'row'+i} gutter={[16, 16]} type="flex" justify="center" align="top" style={i===0&&{paddingTop:'10px'}}>
              <Col key={'left'+i} span={12}>{makeCard(selectedApiList[i*2])}</Col>
            {selectedApiList[i*2+1]!==undefined &&
              <Col key={'right'+i} span={12}>{makeCard(selectedApiList[i*2+1])}</Col>
            }
            </Row>
          )
      }

      return rows.map(row=>row)
    },[formData, makeCard])
    



  return (
    <>
    <Row gutter={[16, 8]}>
      <Col span={8}>
        {/* <Affix offsetTop={top}> */}
          <Card 
            title={'Api 목록'} 
            bordered={false}
            headStyle={{background:'#FAFAFA', padding:'0'}}            
            bodyStyle={{...cardBody, background:'#FAFAFA', padding:'10px 0', borderTop:'1px solid'}}
            >
            {renderApiList(true, true)}
          </Card>
        {/* </Affix> */}
      </Col>
      <Col span={16}>
        <Card
          title={'선택된 Api 정보'}
          bordered={false}
          headStyle={{background:'#FAFAFA', padding:'0'}}  
          bodyStyle={{...cardBody, background:'#FAFAFA', padding:'10px 0', borderTop:'1px solid'}}
          >
            {
                formData[nameSet.selectedApiList]!==undefined&& formData[nameSet.selectedApiList].length>0 &&
                <InputGroup compact={true} style={{borderBottom:'1px solid', paddingBottom:'18px'}}>
                  {renderEmployee()}
                </InputGroup>
        
            }
            {
                formData[nameSet.selectedApiList]!==undefined  
                      ? renderCardList()
                    : <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
            }
          
        </Card>
      </Col>
    </Row>

    {visible.display && <SearchEmployee visible={visible} formData={formData} toggleModal={toggleModal} handleSetFormData={handleSetFormData}/>}
    </>
  );
};

export default SelectApi;
