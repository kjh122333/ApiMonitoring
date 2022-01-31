import React, { useState, useCallback, useMemo } from 'react';
import {
  Form,
  Input,
  Button,
  Modal,
  Spin,
  Tooltip,
  Switch,
  Row,
  Col,
} from 'antd';
import AddModal from './AddModal';
import 'lib/css/admin/apiConfig.css';
import { initalFilterValue } from 'containers/admin/ApiConfigContainer';

const { confirm } = Modal;

/**
 * initalFilterValue = {
 *  serviceCategoryName
 *  serviceName
 *  apiCategoryName
 *  groupNames
 * }
 */
// const initalFormData = {
//     serviceCategoryName : '',
//     serviceName : '',
//     apiCategoryName : '',
//     groupName : '',
// }

const ListController = ({
  handleFilterNames,
  workKey,
  reqSave,
  reqDeleteCheck,
  reqDelete,
  renderConfigContainer,
  showDeleted,
  handleShowDeleted,
  selectedRowKeys,
  listDatas,
}) => {
  //console.log('값:', initalFilterValue);
  // const [serviceCategoryName, setServiceCategoryName] = useState(''); //필터 input
  // const [serviceName, setServiceName] = useState(''); //필터 input
  // const [apiCategoryName, setApiCategoryName] = useState(''); //필터 input

  const [formData, setFormData] = useState(initalFilterValue);
  const [visible, setVisible] = useState(false); //모달 visible
  const [loading, setLoading] = useState(false);

  const addButtonName = useMemo(() => {
    if (workKey === 0) return '서비스 카테고리 등록';
    else if (workKey === 1) return '서비스 등록';
    else if (workKey === 2) return 'API 카테고리 등록';
  }, [workKey]);

  const initControllerFormData = useCallback(() => {
    setFormData(initalFilterValue);
  }, []);

  const handleFormData = useCallback(({ target }) => {
    setFormData(prevData => ({ ...prevData, [target.name]: target.value }));
  }, []);

  const resetOnClick = useCallback(() => {
    initControllerFormData();
    handleFilterNames(initalFilterValue);
  }, [handleFilterNames, initControllerFormData]);

  const showModal = useCallback(() => {
    setVisible(true);
  }, []);

  const closeModal = useCallback(() => {
    setVisible(false);
  }, []);

  const renderDeleteListTable = useCallback(
    deleteList => {
      let th = [];
      let tbody;
      if (workKey === 0) {
        th = ['서비스 카테고리', '서비스', 'API 카테고리', 'API'];
      } else if (workKey === 1) {
        th = ['서비스', '서비스 URL', 'API 카테고리', 'API'];
      } else if (workKey === 2) {
        th = ['API 카테고리', 'API'];
      }

      //console.log('삭제될 테이블 : ', tbody);

      return (
        <table className="confirm-table">
          <thead>
            <tr>
              {th.map(title => (
                <th key={title}>{title}</th>
              ))}
            </tr>
          </thead>
          <tbody>
            {deleteList.map((item, index) => {
              if (workKey === 0) {
                return item.serviceList.length > 0 ? (
                  item.serviceList.map(service => {
                    //현재 row에 해당하는 서비스에 포함되어 있는 api 카테고리 리스트
                    const aCategoryInService = item.apicategoryList.filter(
                      aCategory => aCategory.service_no === service.service_no,
                    );
                    //현재 row에 해당하는 서비스에 포함되어 있는 api_category_no 리스트
                    const aCategoryNoInService = aCategoryInService.map(
                      aCategory => aCategory.api_category_no,
                    );
                    //현재 row에 해당하는 서비스에 포함되어 있는 api 리스트
                    const apiInService = item.apiList.filter(api =>
                      aCategoryNoInService.includes(api.api_category_no),
                    );
                    return (
                      <tr
                        key={service.service_no}
                        style={{ background: index % 2 !== 0 && '#dddddd' }}
                      >
                        <td>{item.servicecategoryname}</td>
                        <td>{service.service_name_kr}</td>
                        <td>
                          {aCategoryInService.length > 0 ? (
                            <Tooltip
                              placement="right"
                              title={aCategoryInService.map(
                                (aCategory, index) => (
                                  <span
                                    key={index}
                                    className="tooltip-span"
                                  >{`${index + 1})  ${
                                    aCategory.api_category_name_kr
                                  }`}</span>
                                ),
                              )}
                            >
                              <span className="open-tooltip">목록보기</span>
                            </Tooltip>
                          ) : (
                            '-'
                          )}
                        </td>
                        <td>
                          {apiInService.length > 0 ? (
                            <Tooltip
                              placement="right"
                              title={apiInService.map((api, index) => (
                                <span key={index} className="tooltip-span">
                                  {' '}
                                  {`${index + 1}) (${api.method})  ${
                                    api.api_url
                                  }`}
                                </span>
                              ))}
                            >
                              <span className="open-tooltip">목록보기</span>
                            </Tooltip>
                          ) : (
                            '-'
                          )}
                        </td>
                      </tr>
                    );
                  })
                ) : (
                  <tr
                    key={index}
                    style={{ background: index % 2 !== 0 && '#dddddd' }}
                  >
                    <td>{item.servicecategoryname}</td>
                    <td>-</td>
                    <td>-</td>
                    <td>-</td>
                  </tr>
                );
              } else if (workKey === 1) {
                return (
                  <tr
                    key={item.serviceList.service_no}
                    style={{ background: index % 2 !== 0 && '#dddddd' }}
                  >
                    <td>{item.serviceList.service_name_kr}</td>
                    <td>{item.serviceList.service_url}</td>
                    <td>
                      {item.apicategoryList.length > 0 ? (
                        <Tooltip
                          placement="right"
                          title={item.apicategoryList.map(
                            (aCategory, index) => (
                              <span
                                key={index}
                                className="tooltip-span"
                              >{`${index + 1})  ${
                                aCategory.api_category_name_kr
                              }`}</span>
                            ),
                          )}
                        >
                          <span className="open-tooltip">목록보기</span>
                        </Tooltip>
                      ) : (
                        '-'
                      )}
                    </td>
                    <td>
                      {item.apiList.length > 0 ? (
                        <Tooltip
                          placement="right"
                          title={item.apiList.map((api, index) => (
                            <span key={index} className="tooltip-span">
                              {' '}
                              {`${index + 1}) (${api.method})  ${api.api_url}`}
                            </span>
                          ))}
                        >
                          <span className="open-tooltip">목록보기</span>
                        </Tooltip>
                      ) : (
                        '-'
                      )}
                    </td>
                  </tr>
                );
              } else if (workKey === 2) {
                return (
                  <tr
                    key={index}
                    style={{ background: index % 2 !== 0 && '#dddddd' }}
                  >
                    <td>{item.apicategory}</td>
                    <td>
                      {item.apiList.length > 0 ? (
                        <Tooltip
                          placement="right"
                          title={item.apiList.map((api, index) => (
                            <span key={index} className="tooltip-span">
                              {' '}
                              {`${index + 1}) (${api.method})  ${api.api_url}`}
                            </span>
                          ))}
                        >
                          <span className="open-tooltip">목록보기</span>
                        </Tooltip>
                      ) : (
                        '-'
                      )}
                    </td>
                  </tr>
                );
              } else return '';
            })}
          </tbody>
        </table>
      );
    },
    [workKey],
  );

  const showConfirm = useCallback(
    deleteList => {
      confirm({
        width: '620px',
        title: '정말 삭제 하시겠습니까?',
        content: (
          <div>
            {deleteList.length > 0 && (
              <>
                <p>아래의 데이터가 모두 삭제 됩니다.</p>
                {renderDeleteListTable(deleteList)}
              </>
            )}
          </div>
        ),
        async onOk() {
          //선택된 서비스 카테고리와 아래 모든 데이터 삭제
          await reqDelete();
          setLoading(false);
        },
        onCancel() {
          setLoading(false);
        },
      });
    },
    [renderDeleteListTable, reqDelete],
  );

  const handleDelete = useCallback(async () => {
    if (selectedRowKeys.length === 0) {
      Modal.error({
        title: '삭제 할 항목을 선택해주십시오.',
      });
      return;
    }
    setLoading(true);
    const deleteList = await reqDeleteCheck();
    //console.log('삭제될 리스트 ', deleteList);
    deleteList && showConfirm(deleteList);
    // setLoading(false);
  }, [reqDeleteCheck, selectedRowKeys.length, showConfirm]);

  return (
    <>
      <Form
        className="ant-advanced-search-form"
        style={{ textAlign: 'left', margin: '20px 0 ' }}
        onSubmit={e => {
          e.preventDefault();
          handleFilterNames(formData);
        }}
      >
        <Row gutter={[16, 0]}>
          <Col span={8}>
            <Form.Item label="서비스 카테고리명" style={{ marginBottom: '0' }}>
              <Input
                value={formData.serviceCategoryName}
                name="serviceCategoryName"
                onChange={handleFormData}
              />
            </Form.Item>
          </Col>

          {workKey > 0 && (
            <Col span={8}>
              <Form.Item label="서비스명" style={{ marginBottom: '0' }}>
                <Input
                  value={formData.serviceName}
                  name="serviceName"
                  onChange={handleFormData}
                />
              </Form.Item>
            </Col>
          )}

          {workKey > 1 && (
            <Col span={8}>
              <Form.Item label="Api 카테고리명" style={{ marginBottom: '0' }}>
                <Input
                  value={formData.apiCategoryName}
                  name="apiCategoryName"
                  onChange={handleFormData}
                />
              </Form.Item>
            </Col>
          )}
        </Row>

        <Row gutter={[16, 0]}>
          {workKey > 0 && (
            <Col span={8}>
              <Form.Item label="담당 부서" style={{ marginBottom: '0' }}>
                <Input
                  value={formData.groupName}
                  name="groupName"
                  onChange={handleFormData}
                />
              </Form.Item>
            </Col>
          )}

          <Col span={8}>
            <Form.Item label="삭제된 항목 보기" style={{ marginBottom: '0' }}>
              <Switch checked={showDeleted} onChange={handleShowDeleted} />
            </Form.Item>
          </Col>
        </Row>

        <Row gutter={[16, 0]}>
          <Col span={8} style={{ display: 'flex' }}>
            <Form.Item style={{ marginRight: '10px', marginBottom: '0' }}>
              <Button type="primary" icon="search" htmlType="submit">
                검색
              </Button>
            </Form.Item>

            <Form.Item style={{ marginBottom: '0' }}>
              <Button htmlType="submit" onClick={resetOnClick}>
                필터 초기화
              </Button>
            </Form.Item>
          </Col>

          <Col span={8} offset={8} style={{ display: 'flex', justifyContent:'flex-end' }}>
            <Form.Item style={{ marginRight: '10px' }}>
              <Button type="primary" onClick={showModal}>
                {addButtonName}
              </Button>
            </Form.Item>

            <Form.Item>
              {loading ? (
                <>
                  <Spin tip="loading...">
                    <Button>삭제</Button>
                  </Spin>
                </>
              ) : (
                <Button onClick={handleDelete}>삭제</Button>
              )}
            </Form.Item>
          </Col>
        </Row>
      </Form>

      <AddModal
        visible={visible}
        // loading={loading}
        closeModal={closeModal}
        reqSave={reqSave}
        workKey={workKey}
        renderConfigContainer={renderConfigContainer}
        initControllerFormData={initControllerFormData}
        listDatas={listDatas}
      />
    </>
  );
};

export default ListController;

// ant table 사용 소스 (서비스 카테고리만 되고 소스가 엉망임. map-map-pop)
// const renderDeleteListTable=useCallback(deleteList=>{

//     let columns = [];
//     let dataSource=[];
//     if(workKey===0){
//         columns = [
//             {title: '서비스 카테고리', dataIndex: 'serviceCategory', key: 'serviceCategory'},
//             {title: '서비스', dataIndex: 'service', key: 'service'},
//             {title: 'API 카테고리', dataIndex: 'apiCategory', key: 'apiCategory'},
//             {title: 'API', dataIndex: 'api', key: 'api'},
//         ]
//         dataSource = deleteList.map((item,index)=>
//             item.serviceList.length > 0
//                 ? item.serviceList.map(service=>{
//                   //현재 row에 해당하는 서비스에 포함되어 있는 api 카테고리 리스트
//                   const aCategoryInService = item.apicategoryList.filter(aCategory=>aCategory.service_no===service.service_no);
//                   //현재 row에 해당하는 서비스에 포함되어 있는 api_category_no 리스트
//                   const aCategoryNoInService = aCategoryInService.map(aCategory=>aCategory.api_category_no)
//                   //현재 row에 해당하는 서비스에 포함되어 있는 api 리스트
//                   const apiInService = item.apiList.filter(api=>aCategoryNoInService.includes(api.api_category_no));
//                   return ({
//                     "key" : service.service_no,
//                     "serviceCategory":item.servicecategoryname,
//                     "service":service.service_name_kr,
//                     "apiCategory":aCategoryInService.length>0
//                         ? <Tooltip
//                             placement="right"
//                             title={aCategoryInService.map((aCategory,index)=><span key={index} className='tooltip-span'>{`${index+1})  ${aCategory.api_category_name_kr}`}</span>)}
//                         >
//                             <span className='open-tooltip'>목록보기</span>
//                         </Tooltip>
//                         : '-' ,
//                     "api": apiInService.length>0
//                     ? <Tooltip
//                         placement="right"
//                         title={apiInService.map((api,index)=><span key={index} className='tooltip-span'> {`${index+1}) (${api.method})  ${api.api_url}`}</span>)}
//                     >
//                         <span className='open-tooltip'>목록보기</span>
//                     </Tooltip>
//                     : '-'
//                   })
//                 }).pop()
//                 : {
//                     "key" : index,
//                     "serviceCategory":item.servicecategoryname,
//                     "service":'-',
//                     "apiCategory":'-',
//                     "api": '-'
//                 }
//         )
//     }else if(workKey===1){

//     }else if(workKey===2){

//     }

//     //console.log('삭제될 테이블', dataSource);

//     return <Table dataSource={dataSource} columns={columns} size="small" bordered/>
// },[workKey]);
