import React,{useState, useMemo, useCallback} from 'react';
import {Modal, Button, Input,Form,Select} from 'antd';

const {Option} = Select;


//todo api 카테고리 추가 작업중

const AddModal = ({visible, closeModal, handleOk, workKey, reqSave,renderConfigContainer,initControllerFormData,listDatas}) => {

    //초기 폼 데이터 (workKey === 0 : 서비스 카테고리, 1 : 서비스, 2 : api 카테고리)
    const initalFormData = useMemo(()=>{
      if(workKey===0) return { 
        newName : '', // 추가 할 S.카테고리 이름
      }
      else if(workKey===1) return {
        serviceCategoryNo : '', 
        newName : '', // 추가 할 서비스 이름
        newServiceUrl : '', 
        newServiceCode : '',
        groupNo : '',
      }
      else if(workKey===2) return { 
        serviceCategoryNo : '',
        serviceNo : '',
        newName : '', // 추가 할 A.카테고리 이름
      }
      else return {}
    },[workKey])

    const [formData, setFormData] = useState(initalFormData);
    const [loading, setLoading] = useState(false);
    
    //console.log('formData',formData);
    
    //모달 title 결정
    const title = useMemo(()=>{
      if(workKey===0) return '서비스 카테고리 등록'
      else if(workKey===1) return '서비스 등록'
      else if(workKey===2) return 'API 카테고리 등록'
      else  return ''
    },[workKey]);
    
    
    const isEmpty = useCallback((str='')=> str.toString().trim() === '' ? true : false,[])
    
    const validateStatus = useMemo(()=>{
      if(workKey===0){
        return {
          newName : isEmpty(formData.newName)
          ? { status : 'error', message : '이름을 입력해주세요.'}
          : { status : '', message : ''}
        }
      }else if(workKey===1){
        return {
          serviceCategoryNo : isEmpty(formData.serviceCategoryNo)
            ? { status : 'error', message : '카테고리를 선택해 주세요.'} 
            : { status : '', message : ''},
          newName : isEmpty(formData.newName)
            ? { status : 'error', message : '이름을 입력해주세요.'} 
            : { status : '', message : ''},
          newServiceUrl : isEmpty(formData.newServiceUrl)
            ? { status : 'error', message : 'URL을 입력해주세요.'} 
            : { status : '', message : ''},           
          newServiceCode : isEmpty(formData.newServiceCode)
            ? { status : 'error', message : '서비스 코드 입력해주세요.'} 
            : formData.newServiceCode.substring(0,1)!=='/'
              ? { status : 'error', message : '서비스 코드는 \'/\'로 시작해야 합니다.'}
              : { status : '', message : ''},
           groupNo : isEmpty(formData.groupNo)
           ? { status : 'error', message : '담당 부서를 선택해주세요.'} 
           : { status : '', message : ''},
          }
        }else if(workKey===2){
          return {
            serviceCategoryNo : isEmpty(formData.serviceCategoryNo)
            ? { status : 'error', message : '카테고리를 선택해 주세요.'} 
            : { status : '', message : ''},
            serviceNo : isEmpty(formData.serviceNo)
            ? { status : 'error', message : '서비스를 선택해 주세요.'} 
            : { status : '', message : ''},
            newName : isEmpty(formData.newName)
              ? { status : 'error', message : '이름을 입력해주세요.'} 
              : { status : '', message : ''},
            }
        }else{
          return {}
        }
      },[formData, isEmpty, workKey]);
      
      
    const handleInputChange = useCallback(({target})=>{
      setFormData(prevForm=>({...prevForm, [target.name] : target.value}))
    },[]);

    const handleSelectChange = useCallback((name,value)=>{
      setFormData(prevForm=>({...prevForm, [name] : value}))
    },[]);

    const handleOnSubmit = useCallback(()=>{
      let postData = {}
      if(workKey===0){
          postData = {
              "category_name_kr" : formData.newName,
          }
      }else if(workKey===1){
          postData = {
            "service_category_no": formData.serviceCategoryNo,
            "service_name_kr": formData.newName, 
            "service_url": formData.newServiceUrl,
            "service_code":formData.newServiceCode,
            "group_no": formData.groupNo, 
          }
      }else if(workKey===2){
          postData={
            "service_no": formData.serviceNo,
            "api_category_name_kr": formData.newName, 
          }
      }

      setLoading(true);
      reqSave(postData).then(result=>{
        if(result){
          setFormData(initalFormData)
          closeModal();
          initControllerFormData();
          renderConfigContainer();
        }
      });
      setLoading(false);
      
    },[closeModal, formData, initControllerFormData, initalFormData, renderConfigContainer, reqSave, workKey])
    
    return (
      <Modal
        visible={visible}
        title={title}
        onCancel={closeModal}
        footer={[
          <Button key="back" onClick={closeModal}>
            취소{' '}
          </Button>,
          <Button
            key="submit"
            type="primary"
            loading={loading}
            onClick={handleOnSubmit}
            disabled={
              Object.keys(validateStatus).filter(
                key => validateStatus[key].status === 'error',
              ).length > 0
                ? true
                : false
            }
          >
            추가
          </Button>,
        ]}
      >
        <Form
          labelAlign="right"
          onSubmit={e => {
            e.preventDefault();
            handleOnSubmit();
          }}
        >
          {workKey > 0 && (
            <Form.Item
              label="서비스 카테고리"
              style={{ display: 'flex' }}
              required
              hasFeedback
              validateStatus={validateStatus.serviceCategoryNo.status}
              help={validateStatus.serviceCategoryNo.message}
            >
              <Select
                style={{ minWidth: '120px' }}
                onChange={value => {
                  handleSelectChange('serviceCategoryNo', value);
                  workKey===2 && handleSelectChange('serviceNo','')
                }}
              >
                {listDatas.serviceCategory
                  .filter(category => category.is_deleted === 'F')
                  .map(category => (
                    <Option
                      key={category.service_category_no}
                      value={category.service_category_no}
                    >
                      {category.category_name_kr}
                    </Option>
                  ))}
              </Select>
            </Form.Item>
          )}

          {workKey === 2 && (
            <Form.Item
              label="서비스"
              style={{ display: 'flex' }}
              required
              hasFeedback
              validateStatus={validateStatus.serviceNo.status}
              help={validateStatus.serviceNo.message}
            >
              <Select
                style={{ minWidth: '120px' }}
                value={formData.serviceNo}
                onChange={value => {
                  handleSelectChange('serviceNo', value);
                }}
              >
                {listDatas.service
                  .filter(service => service.is_deleted === 'F')
                  .filter(service=>service.service_category_no === formData.serviceCategoryNo)
                  .map(service => (
                    <Option
                      key={service.service_no}
                      value={service.service_no}
                    >
                      {service.service_name_kr}
                    </Option>
                  ))}
              </Select>
            </Form.Item>
          )}

          <Form.Item
            label="이름"
            style={{ display: 'flex' }}
            required
            hasFeedback
            validateStatus={validateStatus.newName.status}
            help={validateStatus.newName.message}
          >
            <Input
              style={{ minWidth: '250px' }}
              value={formData.newName}
              name="newName"
              onChange={handleInputChange}
            />
          </Form.Item>

          {workKey === 1 && (
            <>
              <Form.Item
                label="서비스 URL"
                style={{ display: 'flex' }}
                required
                hasFeedback
                validateStatus={validateStatus.newServiceUrl.status}
                help={validateStatus.newServiceUrl.message}
              >
                <Input
                  style={{ minWidth: '250px' }}
                  value={formData.newServiceUrl}
                  name="newServiceUrl"
                  onChange={handleInputChange}
                />
              </Form.Item>

              <Form.Item
                label="서비스 코드"
                style={{ display: 'flex' }}
                required
                hasFeedback
                validateStatus={validateStatus.newServiceCode.status}
                help={validateStatus.newServiceCode.message}
              >
                <Input
                  style={{ minWidth: '250px' }}
                  value={formData.newServiceCode}
                  name="newServiceCode"
                  onChange={handleInputChange}
                />
              </Form.Item>

              <Form.Item
                label="담당 부서"
                style={{ display: 'flex' }}
                required
                hasFeedback
                validateStatus={validateStatus.groupNo.status}
                help={validateStatus.groupNo.message}
              >
                <Select
                  style={{ minWidth: '120px' }}
                  onChange={value => {
                    handleSelectChange('groupNo', value);
                  }}
                >
                  {listDatas.groupNames.map(group => (
                    <Option key={group.group_no} value={group.group_no}>
                      {group.group_name}
                    </Option>
                  ))}
                </Select>
              </Form.Item>
            </>
          )}
        </Form>
      </Modal>
    );
};

export default AddModal;