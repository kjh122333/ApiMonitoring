import React, { useState, useCallback, useEffect, useRef } from 'react';
// import { Link } from 'react-router-dom';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep.jsx';
import handleException from 'lib/function/request/handleException.js';
import {
  Collapse,
  Table,
  Button,
  Icon,
  PageHeader,
  Descriptions,
  Empty,
  Tabs,
  Tag,
  Tooltip,
  Row,
  Col,
  Badge,
  Timeline,
  Modal,
  message,
  Dropdown,
  Menu
} from 'antd';
import { withRouter } from 'react-router-dom';
import axios from 'axios';
import 'lib/css/api/discription.css';
import 'lib/css/api/timeLine.css';
import ApiDetailUpdate from './ApiDetailUpdate';
import scrollToComponent from 'react-scroll-to-component';


const { Panel } = Collapse;
const { TabPane } = Tabs;
const { confirm } = Modal;

const columns = [
  {
    title: 'Name',
    dataIndex: 'name',
    key: 'name',
  },
  {
    title: 'Description',
    dataIndex: 'description',
    key: 'description',
  },
  {
    title: 'Type',
    dataIndex: 'in',
    key: 'in',
  },
  {
    title: 'Data Type',
    dataIndex: 'type',
    key: 'type',
  },
  {
    title: 'Required',
    dataIndex: 'required',
    key: 'required',
  },
];

const isUndefinedOrNull = value=>{
  return value===undefined || value === null ? true : false
}


const ApiDetail = ({ history }) => {
  const [loading, setLoading] = useState(true); //api 목록 요청 로딩 플래그
  const [apiDatas, setApiDatas] = useState({}); // 서비스 정보, api카테고리 정보, api 리스트
  const [paramDatas, setParamDatas] = useState([]);
  const [pathApiNo, setPathApiNo] = useState(parseInt(history.location.pathname.split('/').pop()));
  const [visible, setVisible] = useState(false);
  const [apiData, setApiData] = useState({}); //수정 페이지로 넘길 API 데이터
  const [openPanels, setOpenPanels] = useState([]);
  const [firstRender, setFirstRender] = useState(true);
  const [reRender, setRender] = useState({});
  const userNo = parseInt(sessionStorage.getItem('num'));

  const focus = useRef(null);

  //console.log('유저넘버너버',userNo);
  // //console.log('히히히히ㅣㅎ',history);
  
  const forceRender = useCallback(()=>{
    setRender({});
  },[]);

  useEffect(() => {
    //console.log('열린 이팩 1');
    
    const reqApiInfo = async () => {
      setLoading(true);
      const { pathname, search } = history.location;
      const apiKey = parseInt(pathname.split('/').pop());
      setPathApiNo(apiKey);
      
      // setOpenPanels(prev=>prev.length===0?[apiKey.toString()] : prev);
      
      try {
        const res = await axios.get(`http://15.165.25.145:9500/user${pathname}${search}`);
        if (res.status === 200) {
          //console.log('상세페이지reseeess',res.data);
          const { data } = res.data;
          
          setApiDatas(data);
          setParamDatas(() =>
            data.apiList.map(api => ({
              apiNo: api.api_no,
              params: api.param,
            })),
          );

          firstRender && setOpenPanels(pathname.split('/').pop());
          setLoading(false);
          setFirstRender(false);

        }
      } catch (error) {
        handleException(error,history)
      } 
    };

    
    reqApiInfo(); //모달 닫혀있을 때만 데이터 요청 
    
  }, [history, firstRender,reRender]);
  
  //console.log('열린 처음 렌더', firstRender);
  
  useEffect(()=>{
    //console.log('열린 이팩 2');

    firstRender && scrollToComponent(focus.current,{align : 'top'})
  });

  //console.log('apiDatas', apiDatas);
  // //console.log('paramDatas', paramDatas);

  const showModal = useCallback(() => {
    setVisible(true);
  }, []);

  const closeModal = useCallback(() => {
    setVisible(false);
  }, []);

  const handleCancel = useCallback(() => {
    setVisible(false);
  }, []);

  const showConfirm = useCallback(
    apiNo => {
      confirm({
        title: '해당 API를 삭제하시겠습니까?',
        content: '삭제된 API는 복구할 수 없습니다.(재등록은 가능)',
        okText: 'Yes',
        okType: 'danger',
        cancelText: 'No',
        async onOk() {
          const url = `http://15.165.25.145:9500/user/api/t/${apiNo}`;
          try {
            const res = await axios.patch(url);
            //console.log(res);
            if (res.data.success) {
              Modal.success({
                title: '삭제가 완료되었습니다.',
              });
              forceRender();
              window.scrollTo(0,0);
            } else {
              Modal.error({
                title: '삭제에 실패하였습니다.',
              });
            }
          } catch (error) {
            handleException(error,history)
          }
        },
        onCancel() {},
      });
    },
    [forceRender, history],
  );

  const callback = useCallback(keys => {
    //console.log('열린 패널 키',keys);
    setOpenPanels(keys);
  }, []);

  const renderPageHeader = useCallback(() => (
      <PageHeader
        style={{
          border: '1px solid rgb(235, 237, 240)',
        }}
        title="서비스 정보"
        onBack={() => history.goBack()}
        extra={
          apiDatas.service.service_state ? (
            <Tag className="item-label" color="green">
              running
            </Tag>
          ) : (
            <Tag className="item-label" color="red">
              stoped
            </Tag>
          )
        }
      >
        <Descriptions
          size="small"
          column={2}
          style={{ fontWeight: 'bold', textAlign: 'left' }}
        >
          <Descriptions.Item label={<span className="item-label">이름</span>}>
            {apiDatas.service.service_name_kr}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">담당 부서</span>}
          >
            {apiDatas.service.group_name}
          </Descriptions.Item>
          <Descriptions.Item label={<span className="item-label">URL</span>}>
            {apiDatas.service.service_url}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">Api 카테고리</span>}
          >
            {apiDatas.apicategory.api_category_name_kr}
          </Descriptions.Item>
        </Descriptions>
      </PageHeader>
    ),[apiDatas, history]);


  const renderResponseClass = useCallback(resCodeObj=>(
      <div style={{ background: '#f0f2f5' }}>
        {resCodeObj.description && (
          <span style={{ display: 'block', marginBottom: '10px' }}>
            message : {resCodeObj.description}
          </span>
        )}

        {resCodeObj.schema && (
          <pre>{JSON.stringify(resCodeObj.schema, null, 2)}</pre>
        )}
      </div>
    ),[]);

  //렌더링
  return !loading ? (
    apiDatas.hasOwnProperty('apiList') && apiDatas.apiList.length ? (
      <>
        {renderPageHeader()}
        <Collapse
          onChange={callback}
          style={{ textAlign: 'left' }}
          // defaultActiveKey={pathApiNo}
          activeKey={openPanels}
        >
          {apiDatas.apiList.map(api => (
            <Panel
              header={
                <>
                  <span
                    ref={api.api_no === pathApiNo ? focus : null}
                    style={{ fontSize: '120%' }}
                  >
                    {renderMethodBage(api.method.toLowerCase())}
                  </span>
                  <span style={{ fontSize: '110%' }}>
                    {' '}
                    {api.api_url}
                  </span>
                </>
              }
              key={api.api_no}
              extra={
                <span>
                  <Badge color={api.err_status === 'T' ? 'red' : 'green'} />
                  에러{' '}
                  <Badge color={api.delay_status === 'T' ? 'red' : 'green'} />
                  지연{' '}
                </span>
              }
            >
              <Row>
                <Col span={23}>
                  <Timeline>
                    <Timeline.Item className="line-item">
                      <h5>설명</h5>
                    </Timeline.Item>
                    <Timeline.Item className="line-item" color="gray">
                      {api.description}
                    </Timeline.Item>
                  </Timeline>
                </Col>
                {(userNo === 1 ||
                  (api.employee_no === 0 && api.employee_sub_no === 0) ||
                  userNo === api.employee_no ||
                  userNo === api.employee_sub_no) && (
                  <Col span={1}>
                    <Dropdown
                      placement="bottomCenter"
                      trigger={['click']}
                      overlay={
                        <Menu style={{ padding: '2px' }}>
                          <Menu.Item disabled={true} style={{ padding: '0' }}>
                            <Button
                              style={{ border: 'none' }}
                              onClick={() => {
                                setApiData(api);
                                showModal();
                              }}
                            >
                              수정
                            </Button>
                          </Menu.Item>
                          <Menu.Divider />
                          <Menu.Item disabled={true} style={{ padding: '0' }}>
                            <Button
                              style={{ border: 'none' }}
                              onClick={() => {
                                showConfirm(api.api_no);
                              }}
                            >
                              삭제
                            </Button>
                          </Menu.Item>
                        </Menu>
                      }
                    >
                      <Icon
                        style={{ width: '20px', height: '20px' }}
                        type="setting"
                        theme="filled"
                      />
                    </Dropdown>
                  </Col>
                )}
              </Row>

              <Row>
                <Col span={23}>
                  <Timeline>
                    <Timeline.Item className="line-item">
                      <h5>담당자</h5>
                    </Timeline.Item>
                    <Timeline.Item className="line-item" color="gray">
                      <Tag
                        style={{ fontSize: '110%', marginRight: '0' }}
                        color="#108ee9"
                      >{`정`}</Tag>
                      <Tag
                        style={{ fontSize: '110%' }}
                        color="geekblue"
                      >{`[${api.employee_group_name}] ${api.employee_name}`}</Tag>
                      <Tag
                        style={{ fontSize: '110%', marginRight: '0' }}
                        color="#108ee9"
                      >{`부`}</Tag>
                      <Tag
                        style={{ fontSize: '110%' }}
                        color="geekblue"
                      >{`[${api.employee_sub_group_name}] ${api.employee_sub_name}`}</Tag>
                    </Timeline.Item>
                  </Timeline>
                </Col>
              </Row>

              <br />

              <Timeline>
                <Timeline.Item className="line-item">
                  <h5>Response</h5>
                </Timeline.Item>
                {api.response_list !== null ? (
                  <>
                    <Timeline.Item className="line-item" color="gray">
                      <span style={{}}>
                        {' '}
                        Content-type{' '}
                        <Tag color="geekblue">{api.response_type}</Tag>
                      </span>
                    </Timeline.Item>
                    <Timeline.Item className="line-item" color="gray">
                      <Tabs defaultActiveKey="200">
                        {Object.keys(api.response_list).map(responseCode => (
                          <TabPane tab={responseCode} key={responseCode}>
                            {renderResponseClass(
                              api.response_list[responseCode],
                            )}
                          </TabPane>
                        ))}
                      </Tabs>
                    </Timeline.Item>
                  </>
                ) : (
                  <Timeline.Item className="line-item" color="gray">
                    <Empty
                      image={Empty.PRESENTED_IMAGE_SIMPLE}
                      style={{ margin: '0' }}
                    />
                  </Timeline.Item>
                )}
                <Timeline.Item className="line-item" color="gray">
                  &nbsp;
                </Timeline.Item>
              </Timeline>

              <br />
              <Timeline>
                <Timeline.Item className="line-item">
                  <h5>Parameters</h5>
                </Timeline.Item>
                {api.param !== null && api.param.length > 0 ? (
                  <>
                    <Timeline.Item className="line-item" color="gray">
                      <span>
                        {' '}
                        Content-type{' '}
                        <Tag color="geekblue">{api.parameter_type}</Tag>
                      </span>
                    </Timeline.Item>
                    <Timeline.Item className="line-item" color="gray">
                      <Table
                        columns={columns}
                        dataSource={paramDatas
                          .find(item => item.apiNo === api.api_no)
                          .params.map((param, index) => ({
                            ...param,
                            key: index,
                            required:
                              param.required.toString() === 'true'
                                ? 'required'
                                : '',
                          }))}
                        pagination={false}
                        size="small"
                        style={{ overflowX: 'auto' }}
                      />
                    </Timeline.Item>
                  </>
                ) : (
                  <Timeline.Item className="line-item" color="gray">
                    <Empty
                      image={Empty.PRESENTED_IMAGE_SIMPLE}
                      style={{ margin: '0' }}
                    />
                  </Timeline.Item>
                )}
                <Timeline.Item className="line-item" color="gray">
                  &nbsp;
                </Timeline.Item>
              </Timeline>

              <br />
              {api.updated_timestamp !== null && (
                <Timeline>
                  <Timeline.Item className="line-item">
                    <h5>마지막 업데이트</h5>
                  </Timeline.Item>
                  <Timeline.Item className="line-item" color="gray">
                    <span>
                      <Tag style={{ fontSize: '110%' }} color="blue">
                        {api.updated_timestamp}
                      </Tag>
                      <Tag
                        style={{ fontSize: '110%' }}
                        color="geekblue"
                      >{`[${api.update_employee_group_name}] ${api.update_employee_name}`}</Tag>
                    </span>
                  </Timeline.Item>
                </Timeline>
              )}
            </Panel>
          ))}
        </Collapse>
        <Modal
          title="API 수정"
          visible={visible}
          footer={null}
          onCancel={handleCancel}
          width="80%"
        >
          <ApiDetailUpdate
            service={apiDatas.service}
            apiCategory={apiDatas.apicategory}
            api={apiData}
            closeModal={closeModal}
            history={history}
            forceRender={forceRender}

          />
        </Modal>
      </>
    ) : (
      <Empty image={Empty.PRESENTED_IMAGE_SIMPLE} />
    )
  ) : (
    <span style={{ color: 'blue', fontSize: '100px' }}>
      <Icon type="loading" />
    </span>
  );
};

export default withRouter(ApiDetail);
