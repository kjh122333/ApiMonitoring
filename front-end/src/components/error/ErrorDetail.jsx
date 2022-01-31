import React, { useEffect, useState } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/api/timeLine.css';
import 'lib/css/api/discription.css';
import {
  Descriptions,
  Button,
  Empty,
  Icon,
  PageHeader,
  Tag,
  Timeline,
} from 'antd';
import { withRouter } from 'react-router-dom';
import axios from 'axios';
import ErrorWrite from './ErrorWrite';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep';
import handelException from 'lib/function/request/handleException';
import { authChk } from 'lib/function/authChk';
const ErrorDetail = ({ history, match }) => {
  window.scrollTo(0, 0);
  const { id } = match.params;
  const userNum = sessionStorage.getItem('num');
  const [loading, setLoading] = useState(false);
  //console.log('id :', id);

  /**
   * 수정
   */
  const [editMode, setEditMode] = useState(false);
  const onEdit = () => {
    setEditMode(!editMode);
  };

  /**
   * 상세 정보 요청
   */
  const [errdata, setErrData] = useState({});
  useEffect(() => {
    const errorDetail = async () => {
      setLoading(true);
      try {
        const response = await axios.get(
          `http://15.165.25.145:9500/user/apierr/${id}`,
        );
        //console.log('response Detail : ', response.data.data);
        const { data } = response.data;
        setErrData({
          service: data.service_name_kr,
          apiName: data.api_url,
          errorStatus: data.api_err_status,
          http: data.method,
          errorCode: data.api_err_code,
          department: data.service_group_name,
          mDepartment: data.employee_group_name,
          smDepartment: data.employee_sub_group_name,
          insertDate: data.insert_timestamp,
          updateDate: data.updated_timestamp,
          manager: data.employee_name,
          managerNo: data.employee_no,
          subManager: data.employee_sub_name,
          subManagerNo: data.employee_sub_no,
          errorMsg: data.api_err_msg,
          errorComment: data.api_err_comment,
          apiNo: data.api_no,
          kibana: data.kibana_url,
          jira: data.jira_url,
          ref: data.ref,
        });
        setLoading(false);
      } catch (error) {
        //console.log('erroraaaaaaa:');
        //console.log('error:', error.response);
        handelException(error, history);
      }
    };
    errorDetail();
  }, [history, id, editMode]);
  //console.log('데이터 :', Object.keys(errdata).length);
  const { managerNo, subManagerNo } = errdata;
  const [auth, setAuth] = useState(false);
  useEffect(() => {
    const modifyAuth = async () => {
      setAuth(await authChk(managerNo, subManagerNo, userNum, history));
    };
    modifyAuth();
  }, [history, managerNo, subManagerNo, userNum]);
  if (editMode) {
    return <ErrorWrite errdata={errdata} onEdit={onEdit} />;
  }
  //console.log('다시 접속');
  return !loading ? (
    <div style={{ textAlign: 'left' }}>
      <PageHeader
        style={{
          border: '1px solid rgb(235, 237, 240)',
        }}
        onBack={() => history.goBack()}
        title="Error Info"
        tags={[
          <Tag key="1" color={errdata.errorStatus === 'T' ? 'red' : 'green'}>
            {errdata.errorStatus === 'T' ? 'Error' : 'Finished'}
          </Tag>,
          <span key="2" style={{ fontSize: '120%' }}>
            {renderMethodBage(errdata.http)}
          </span>,
        ]}
        extra={[
          auth && (
            <Button key="1" type="primary" onClick={onEdit}>
              수정
            </Button>
          ),
        ]}
      >
        <Descriptions style={{ marginBottom: '50px', fontWeight: 'bold' }}>
          <Descriptions.Item label={<span className="item-label">서비스</span>}>
            {errdata.service}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">API Name</span>}
          >
            {errdata.apiName}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">담당 부서</span>}
          >
            {errdata.department}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">KIBANA_URL</span>}
          >
            <a href={errdata.kibana} target="_blank" rel="noopener noreferrer">
              {errdata.kibana && errdata.kibana.length > 40
                ? errdata.kibana.substring(0, 40) + '...'
                : errdata.kibana}
            </a>
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">JIRA_URL</span>}
          >
            <a href={errdata.jira} target="_blank" rel="noopener noreferrer">
              {errdata.jira && errdata.jira.length > 40
                ? errdata.jira.substring(0, 40) + '...'
                : errdata.jira}
            </a>
          </Descriptions.Item>
          <Descriptions.Item />
          <Descriptions.Item
            label={<span className="item-label">에러 발생시간</span>}
          >
            {errdata.insertDate}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">에러 해결시간</span>}
          >
            {errdata.updateDate}
          </Descriptions.Item>
        </Descriptions>

        <Timeline>
          <Timeline.Item className="line-item">
            <h5>에러코드</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            {errdata.errorCode}
          </Timeline.Item>
        </Timeline>
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
            >{`[${errdata.mDepartment}] ${errdata.manager}`}</Tag>
            <Tag
              style={{ fontSize: '110%', marginRight: '0' }}
              color="#108ee9"
            >{`부`}</Tag>
            <Tag
              style={{ fontSize: '110%' }}
              color="geekblue"
            >{`[${errdata.smDepartment}] ${errdata.subManager}`}</Tag>
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>에러 원인</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            {errdata.errorMsg}
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>에러 해결 내용</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            {errdata.errorComment}
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>참고 사항</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            {errdata.ref}
          </Timeline.Item>
        </Timeline>
      </PageHeader>
    </div>
  ) : (
    <span style={{ color: 'blue', fontSize: '100px' }}>
      <Icon type="loading" />
    </span>
  );
};

export default withRouter(ErrorDetail);
