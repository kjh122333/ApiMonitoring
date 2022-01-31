import React,{useState, useCallback, useEffect,useMemo} from 'react';
import {Table, Input, InputNumber, Popconfirm, Form,Badge,Button } from 'antd';
import UpdateModal from './UpdateModal';

const serviceCategoryColumns = [
  {title: '서비스 카테고리명', dataIndex : 'serviceCategoryName'},
  {title: '등록일', dataIndex : 'insertTimestamp'},
  {title: '삭제여부', dataIndex : 'isDelete'},
  {title: '수정', dataIndex : 'update'},
]

const serviceColumns = [
  {title: '서비스 카테고리명', dataIndex : 'serviceCategoryName'},
  {title: '서비스명', dataIndex : 'serviceName'},
  {title: 'URL', dataIndex : 'serviceUrl'},
  {title:'서비스 코드', dataIndex:'serviceCode'},
  {title: '담당부서', dataIndex : 'groupName'},
  {title: '등록일', dataIndex : 'insertTimestamp'},
  {title: '상태', dataIndex : 'serviceState'},
  {title: '삭제여부', dataIndex : 'isDelete'},
  {title: '수정', dataIndex : 'update'},
]

const apiCategoryColumns = [
  {title: '서비스 카테고리명', dataIndex : 'serviceCategoryName'},
  {title: '서비스명', dataIndex : 'serviceName'},
  {title: 'API 카테고리명', dataIndex : 'apiCategoryName'},
  {title: '담당부서', dataIndex : 'groupName'},
  {title: '등록일', dataIndex : 'insertTimestamp'},
  {title: '삭제여부', dataIndex : 'isDelete'},
  {title: '수정', dataIndex : 'update'},
]


const ListPrinter = ({ workKey,filterValues, listDatas, selectedRowKeys,handleSelectedRowKeys,showDeleted,renderConfigContainer,reqUpdate }) => {

  const [visible, setVisible] = useState(false); //모달 visible
  const [updateRow, setUpdateRow] = useState({});

  const showModal = useCallback(() => {
    setVisible(true);
  },[]);

  const closeModal = useCallback(() => {
    setVisible(false);
  },[]);

  const handleOnClick = useCallback(rowInfo=>{
    setUpdateRow(rowInfo)
      showModal();
  },[showModal]);


  //테이블 컬럼
  const columns = useMemo(()=>{
    let columnFormat
    if(workKey===0) columnFormat = serviceCategoryColumns;
    else if(workKey===1) columnFormat = serviceColumns;
    else if(workKey===2) columnFormat = apiCategoryColumns;
    else columnFormat = [];

    return columnFormat.map(format=>
        ({
            title: format.title,
            dataIndex: format.dataIndex,
            align : "center",
            sortDirections: format.dataIndex!=='update' && ['descend','ascend'],   
            sorter: format.dataIndex!=='update' && (format.dataIndex==='isDelete'
                    ? (a, b) => a[format.dataIndex].props.text < b[format.dataIndex].props.text ? -1 : a[format.dataIndex].props.text > b[format.dataIndex].props.text ? 1 : 0
                    : (a, b) => a[format.dataIndex] < b[format.dataIndex] ? -1 : a[format.dataIndex] > b[format.dataIndex] ? 1 : 0),
            
        })
    )
  },[workKey])

  const dataSource = useMemo(()=>{
    let {serviceCategoryName , serviceName, apiCategoryName, groupName} = filterValues;
        serviceCategoryName=serviceCategoryName.trim().toLowerCase();
        serviceName=serviceName.trim().toLowerCase();
        apiCategoryName=apiCategoryName.trim().toLowerCase();
        groupName = groupName.trim().toLowerCase();
        //console.log('###필더들 로우 케이스',serviceCategoryName,serviceName,apiCategoryName,groupName);
        

    let dataSource={};
    
    if(workKey===0){
      dataSource=listDatas.serviceCategory.filter(serviceCategory=>
        showDeleted? true : serviceCategory.is_deleted==='F'
      )

     dataSource = serviceCategoryName===''
        ? dataSource
        :dataSource.filter(serviceCategory=> serviceCategory.category_name_kr.toLowerCase().includes(serviceCategoryName))
      
      return dataSource.map(serviceCategory=>({
          key: serviceCategory.service_category_no,
          serviceCategoryName : serviceCategory.category_name_kr,
          insertTimestamp : serviceCategory.insert_timestamp,
          isDelete : serviceCategory.is_deleted==='T'?<Badge color="red" text="Deleted"/> : <Badge color="green" text="Alive" />,
          updatedTimestamp : serviceCategory.updated_timestamp,
          update: serviceCategory.is_deleted==='F' && <Button onClick={()=>{handleOnClick(serviceCategory)}}>수정</Button>
      }))
    }
    else if(workKey===1){
      
      dataSource =  listDatas.service.filter(service=>
        showDeleted? true : service.is_deleted==='F'
        )   
        
        dataSource = serviceCategoryName!==''
        ? dataSource.filter(service=> service.category_name_kr.toLowerCase().includes(serviceCategoryName))
        : dataSource
        
        dataSource = serviceName!=='' 
        ? dataSource.filter(service=>service.service_name_kr.toLowerCase().includes(serviceName))
        : dataSource
        
        dataSource = groupName!=='' 
        ? dataSource.filter(service=>service.group_name.toLowerCase().includes(groupName))
        : dataSource
        
        //console.log('###필터 된 데이터 소스', dataSource);

        return dataSource.map(service=>({
          key: service.service_no,
          serviceCategoryName : service.category_name_kr,
          serviceName : service.service_name_kr,
          serviceUrl : service.service_url,
          serviceCode : service.service_code,
          groupName : service.group_name,
          insertTimestamp : service.insert_timestamp,
          serviceState : service.service_state?<Badge color="green" text="Running" /> : <Badge color="red" text="Stoped"/>,
          isDelete : service.is_deleted==='T'?<Badge color="red" text="Deleted"/> : <Badge color="green" text="Alive" />,
          update: service.is_deleted==='F' && <Button onClick={()=>{handleOnClick(service)}}>수정</Button>
        }))
    }
    else if(workKey===2){
     
      dataSource = listDatas.apiCategory.filter(aCategory=>
        showDeleted? true : aCategory.is_deleted==='F'
      );

      dataSource = serviceCategoryName!==''
        ? dataSource.filter(aCategory=> aCategory.category_name_kr.toLowerCase().includes(serviceCategoryName))
        : dataSource

      dataSource = serviceName!=='' 
        ? dataSource.filter(aCategory=>aCategory.service_name_kr.toLowerCase().includes(serviceName))
        : dataSource
      
      dataSource = groupName!=='' 
        ? dataSource.filter(aCategory=>aCategory.group_name.toLowerCase().includes(groupName))
        : dataSource

      dataSource = apiCategoryName!=='' 
        ? dataSource.filter(aCategory=>aCategory.api_category_name_kr.toLowerCase().includes(apiCategoryName))
        : dataSource

      return dataSource.map(aCategory=>({
        key: aCategory.api_category_no,
        serviceCategoryName : aCategory.category_name_kr, 
        serviceName : aCategory.service_name_kr, 
        apiCategoryName : aCategory.api_category_name_kr, 
        groupName : aCategory.group_name,
        insertTimestamp : aCategory.insert_timestamp, 
        isDelete : aCategory.is_deleted==='T'?<Badge color="red" text="Deleted"/> : <Badge color="green" text="Alive" />,
        update: aCategory.is_deleted==='F' && <Button onClick={()=>{handleOnClick(aCategory)}}>수정</Button>,
      }))
    }
    else return {};

  },[filterValues, handleOnClick, listDatas, showDeleted, workKey])

  const rowSelection = useMemo(()=>({
    selectedRowKeys,
    onChange: selectedRowKeys => {
      handleSelectedRowKeys(selectedRowKeys);
    },
    getCheckboxProps: record => ({
      disabled: record.isDelete.props.text === 'Deleted', 
      isDelete: record.isDelete,
    }),
  }),[handleSelectedRowKeys, selectedRowKeys]);

  return (
    <div id="api-config-table">
    <Table
      bordered
      scroll={{ x: 'auto' }}
      rowSelection={rowSelection}
      columns={columns}
      dataSource={dataSource}
    />
    <UpdateModal
      visible={visible}
      // loading={loading}
      closeModal={closeModal}
      workKey={workKey}
      renderConfigContainer={renderConfigContainer}
      reqUpdate={reqUpdate}
      updateRow={updateRow}
      listDatas={listDatas}
        />
    </div>
  );
};

export default ListPrinter;

