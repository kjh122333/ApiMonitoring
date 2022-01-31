import React, { useCallback } from 'react';
import { Input, Form, Select, Icon,Modal, Radio,Button} from 'antd';
import { Badge } from 'reactstrap';
// import axios from 'axios';
import {withRouter} from 'react-router-dom';
import {formDataNameSet as nameSet} from 'containers/api/ApiWriteContainer';
import ReactFileReader from 'react-file-reader';

const { Option } = Select;

const InputUrl = ({ formData, handleSetFormData, resData,history,serviceCategoryList,
  groupNameList,deleteFormDataKey }) => {


  const info= useCallback(()=>{
    Modal.info({
      title: <p>swagger url 입력 예</p>,
      content: (
        <div>
          <p>● http://localhost:8080/v1/swagger.json</p>
          <p>● http://localhost:8080/v2/api-docs</p>
          <p>● http://localhost:8080/service/v2/api-docs</p>
        </div>
      ),
      onOk() {},
    });
  },[]);

  const fileHandler = useCallback(e=>{
    let file = e[0];
    //console.log('파일 머임',file);
    if(file === undefined){
      return
    }
    if(file.type!=="application/json" && file.type!=="text/plain"){
     
      Modal.error({
        content: '.json 파일이나 .txt 파일만 등록 가능 합니다.',
      });
      return
    }             
    let fileReader = new FileReader();
    fileReader.onload = () => {
      // //console.log(fileReader.result);
      let fileValue;
      try {
        fileValue = JSON.parse(fileReader.result);
        handleSetFormData({target:{name:nameSet.swaggerFile, value : fileValue}});
        handleSetFormData({target:{name:nameSet.fileName, value : file.name}});
      } catch (error) {
        Modal.error({
          content: 'JSON 형식이 올바르지 않습니다.',
        })
      }

    };
    fileReader.readAsText(file);
    // //console.log(e);
    
  },[handleSetFormData])

  return (
    <>
      <Form.Item style={{ marginTop: '10px' }}>
        <Radio.Group
          onChange={e => {
            const {value} = e.target;
            //console.log('폼',value);
            handleSetFormData({
              target: { name: nameSet.importType, value: value },
            });
            if(value==='file'){
              deleteFormDataKey(nameSet.swaggerUrl)
            }else{
              deleteFormDataKey(nameSet.swaggerFile)
              deleteFormDataKey(nameSet.fileName)
              
            }
          }}
          value={formData[nameSet.importType]}
        >
          <Radio value="url">URL 등록</Radio>
          <Radio value="file">JSON 파일 등록</Radio>
        </Radio.Group>
      </Form.Item>

      <Form.Item style={{ marginTop: '10px' }}>
        <Badge
          color="secondary"
          style={{ fontSize: '100%', width: '150px', marginRight: '10px' }}
        >
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
        <Badge
          color="secondary"
          style={{ fontSize: '100%', width: '150px', marginRight: '10px' }}
        >
          <span>담당부서</span>
          <Select
            value={formData[nameSet.groupName]}
            style={{ display: 'block' }}
            onChange={(value, option) => {
              handleSetFormData({ target: { name: nameSet.groupNo, value } });
              handleSetFormData({
                target: {
                  name: nameSet.groupName,
                  value: option.props.children,
                },
              });
            }}
          >
            {groupNameList.map(group => (
              <Option key={group.group_no} value={group.group_no}>
                {group.group_name}
              </Option>
            ))}
          </Select>
        </Badge>
        {formData[nameSet.importType] === 'url' ? (
          <Badge color="secondary" style={{ fontSize: '100%', width: '50%' }}>
            <span>Swagger 주소 입력</span>
            <Input
              style={{ display: 'block' }}
              name={nameSet.swaggerUrl}
              value={formData[nameSet.swaggerUrl]}
              required
              placeholder="swagger ui 의 default 입력"
              suffix={
                <Icon
                  onClick={info}
                  type="question-circle"
                  style={{ color: '#1890FF' }}
                />
              }
              onChange={handleSetFormData}
              onKeyPress={e => {
                if (e.key === 'Enter') e.preventDefault();
              }}
            />
          </Badge>
        ) : (
          <Badge
            color="secondary"
            style={{ fontSize: '100%', width:'50%',display: 'inline-block' }}
          >
            <span>파일 명</span>
            <div style={{ display: 'block'}}>
              <ReactFileReader
                handleFiles={fileHandler}
                fileTypes={['.txt', '.json']}
              >
                <>
                  <Input
                    value={formData[nameSet.fileName]}
                    readOnly
                    required
                    onKeyPress={e => {
                      if (e.key === 'Enter') e.preventDefault();
                    }}
                  />
                  <Button type="primary">파일 선택</Button>
                </>
              </ReactFileReader>
            </div>
          </Badge>
        )}
      </Form.Item>
    </>
  );
};

export default React.memo(withRouter(InputUrl));
