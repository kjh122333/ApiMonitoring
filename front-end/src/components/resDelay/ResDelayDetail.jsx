import React, { useEffect, useState } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/api/timeLine.css';
import 'lib/css/api/discription.css';
import {
  Descriptions,
  Icon,
  Empty,
  Button,
  PageHeader,
  Tag,
  Timeline,
} from 'antd';
import { withRouter } from 'react-router-dom';
import axios from 'axios';
import ResDelayWrite from './ResDelayWrite';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep';
import handelException from 'lib/function/request/handleException';
import { authChk } from 'lib/function/authChk';

const ResDelayDetail = ({ history, match }) => {
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
  const [resDelayData, setResDelayData] = useState({});
  useEffect(() => {
    const resDelayDetail = async () => {
      setLoading(true);
      try {
        const response = await axios.get(
          `http://15.165.25.145:9500/user/apidelay/${id}`,
        );
        //console.log('response Detail : ', response.data.data);
        const { data } = response.data;
        setResDelayData({
          service: data.service_name_kr,
          apiName: data.api_url,
          delayStatus: data.api_delay_status,
          http: data.method,
          delayCode: data.api_delay_code,
          department: data.service_group_name,
          mDepartment: data.employee_group_name,
          smDepartment: data.employee_sub_group_name,
          delayTime: data.api_delay_time,
          insertDate: data.insert_timestamp,
          updateDate: data.updated_timestamp,
          manager: data.employee_name,
          managerNo: data.employee_no,
          subManager: data.employee_sub_name,
          subManagerNo: data.employee_sub_no,
          delayMsg: data.api_delay_msg,
          delayComment: data.api_delay_comment,
          delayCount: data.api_delay_count,
          delayAvg: data.api_delay_avg_time,
          delayMin: data.api_delay_min_time,
          delayMax: data.api_delay_max_time,
          apiNo: data.api_no,
          kibana: data.kibana_url,
          jira: data.jira_url,
          ref: data.ref,
        });
        setLoading(false);
      } catch (error) {
        //console.log('error:', error.response);
        handelException(error, history);
      }
    };
    resDelayDetail();
  }, [history, id, editMode]);
  //console.log('데이터 :', resDelayData.service === undefined);
  const { managerNo, subManagerNo } = resDelayData;
  const [auth, setAuth] = useState(false);
  useEffect(() => {
    const modifyAuth = async () => {
      setAuth(await authChk(managerNo, subManagerNo, userNum, history));
    };
    modifyAuth();
  }, [history, managerNo, subManagerNo, userNum]);

  if (editMode) {
    return <ResDelayWrite resDelayData={resDelayData} onEdit={onEdit} />;
  }
  return !loading ? (
    <div style={{ textAlign: 'left' }}>
      <PageHeader
        style={{
          border: '1px solid rgb(235, 237, 240)',
        }}
        onBack={() => history.goBack()}
        title="Delay Info"
        tags={[
          <Tag
            key="1"
            color={resDelayData.delayStatus === 'T' ? 'red' : 'green'}
          >
            {resDelayData.delayStatus === 'T' ? 'Error' : 'Finished'}
          </Tag>,
          <span key="2" style={{ fontSize: '120%' }}>
            {renderMethodBage(resDelayData.http)}
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
        <Descriptions style={{ fontWeight: 'bold' }}>
          <Descriptions.Item label={<span className="item-label">서비스</span>}>
            {resDelayData.service}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">API Name</span>}
          >
            {resDelayData.apiName}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">담당 부서</span>}
          >
            {resDelayData.department}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">KIBANA_URL</span>}
          >
            <a
              href={resDelayData.kibana}
              target="_blank"
              rel="noopener noreferrer"
            >
              {resDelayData.kibana && resDelayData.kibana.length > 40
                ? resDelayData.kibana.substring(0, 40) + '...'
                : resDelayData.kibana}
            </a>
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">JIRA_URL</span>}
          >
            <a
              href={resDelayData.jira}
              target="_blank"
              rel="noopener noreferrer"
            >
              {resDelayData.jira && resDelayData.jira.length > 40
                ? resDelayData.jira.substring(0, 40) + '...'
                : resDelayData.jira}
            </a>
          </Descriptions.Item>
          <Descriptions.Item />
          <Descriptions.Item
            label={<span className="item-label">응답시간</span>}
          >
            {resDelayData.delayTime} s
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">응답지연 발생시간</span>}
          >
            {resDelayData.insertDate}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">응답지연 해결시간</span>}
          >
            {resDelayData.updateDate}
          </Descriptions.Item>
        </Descriptions>
      </PageHeader>
      <PageHeader
        style={{
          border: '1px solid rgb(235, 237, 240)',
        }}
      >
        <Descriptions style={{ marginBottom: '50px', fontWeight: 'bold' }}>
          <Descriptions.Item
            span={3}
            label={<span className="item-label">응답지연 횟수</span>}
          >
            {resDelayData.delayCount} 회
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">평균 응답지연 시간</span>}
          >
            {resDelayData.delayAvg} s
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">최소 응답지연 시간</span>}
          >
            {resDelayData.delayMin} s
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">최대 응답지연 시간</span>}
          >
            {resDelayData.delayMax} s
          </Descriptions.Item>
        </Descriptions>

        <Timeline>
          <Timeline.Item className="line-item">
            <h5>응답코드</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            {resDelayData.delayCode}
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
            >{`[${resDelayData.mDepartment}] ${resDelayData.manager}`}</Tag>
            <Tag
              style={{ fontSize: '110%', marginRight: '0' }}
              color="#108ee9"
            >{`부`}</Tag>
            <Tag
              style={{ fontSize: '110%' }}
              color="geekblue"
            >{`[${resDelayData.smDepartment}] ${resDelayData.subManager}`}</Tag>
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>응답지연 원인</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            {resDelayData.delayMsg}
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>응답지연 해결 내용</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            {resDelayData.delayComment}
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>참고 사항</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            {resDelayData.ref}
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

export default withRouter(ResDelayDetail);
