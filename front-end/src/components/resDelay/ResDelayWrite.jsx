import React, { useState } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/api/timeLine.css';
import 'lib/css/api/discription.css';
import axios from 'axios';
import moment from 'moment';
import {
  Descriptions,
  Button,
  Input,
  DatePicker,
  Modal,
  PageHeader,
  Timeline,
  Tag,
  Select,
} from 'antd';
import { withRouter } from 'react-router-dom';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep';
import handelException from 'lib/function/request/handleException';

const { confirm } = Modal;
const { TextArea } = Input;
const { Option } = Select;
const ResDelayWrite = ({ resDelayData, match, history, onEdit }) => {
  const { id } = match.params;

  /**
   * Select 관련 함수
   */
  const onChangeStatus = key => {
    setModifyData({
      ...modifyData,
      delayStatus: key === '1' ? 'T' : 'F',
    });
  };

  /**
   * Input Onchange 함수
   */
  const [modifyData, setModifyData] = useState({
    ...resDelayData,
    updateDate: moment().format('YYYY-MM-DD HH:mm:ss'),
  });
  const onChangeInput = e => {
    setModifyData({
      ...modifyData,
      [e.target.name]: e.target.value,
    });
  };

  /**
   * Select Time 관련 함수
   */
  const onTimeChange = (value, dateString) => {
    //console.log('Selected Time: ', value);
    //console.log('Formatted Selected Time: ', dateString);
    setModifyData({
      ...modifyData,
      updateDate: dateString,
      // updateDate: value.format('YYYY-MM-DD HH:mm:ss'),
    });
  };

  const onOk = value => {
    //console.log('onOk: ', value);
    setModifyData({
      ...modifyData,
      updateDate: value.format('YYYY-MM-DD HH:mm:ss'),
    });
  };

  /**
   * 이전 버튼
   */
  const showConfirm = () => {
    confirm({
      title: '데이터가 저장되지 않습니다. 이전으로 돌아가시겠습니까?',
      onOk() {
        onEdit();
      },
      onCancel() {
        //console.log('Cancel');
      },
    });
  };
  //console.log('지연 업데이트 날짜확인', moment(modifyData.updateDate));

  /**
   * 수정 완료 버튼
   */
  const update = () => {
    confirm({
      title: '수정 하시겠습니까?',
      async onOk() {
        const request = {
          api_delay_code: modifyData.delayCode,
          api_delay_comment: modifyData.delayComment,
          api_delay_msg: modifyData.delayMsg,
          api_delay_no: parseInt(id, 10),
          api_delay_status: modifyData.delayStatus,
          api_delay_time: modifyData.delayTime,
          api_no: modifyData.apiNo,
          insert_timestamp: modifyData.insertDate,
          updated_timestamp: modifyData.updateDate,
          jira_url: modifyData.jira,
          kibana_url: modifyData.kibana,
          ref: modifyData.ref,
        };
        //console.log('최종데이터 :', request);
        try {
          await axios.patch(
            `http://15.165.25.145:9500/user/apidelay/${id}`,
            request,
          );

          onEdit();
        } catch (err) {
          handelException(err, history);
        }
      },
    });
  };

  return (
    <div style={{ textAlign: 'left' }}>
      <PageHeader
        style={{
          border: '1px solid rgb(235, 237, 240)',
        }}
        onBack={() => showConfirm()}
        title="Delay Info"
        tags={[
          <Tag
          key="1"
            color={modifyData.delayStatus === 'T' ? 'red' : 'green'}
          >
            {modifyData.delayStatus === 'T' ? 'Error' : 'Finished'}
          </Tag>,
          <span key="2" style={{ fontSize: '120%' }}>
            {renderMethodBage(resDelayData.http)}
          </span>,
        ]}
        extra={[
          <Button key="1" type="primary" onClick={update}>
            확인
          </Button>,
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
            label={
              <span className="item-label">
               KIBANA_URL
              </span>
            }
          >
            <Input
              defaultValue={resDelayData.kibana}
              value={modifyData.kibana}
              onChange={onChangeInput}
              name="kibana"
            />
          </Descriptions.Item>
          <Descriptions.Item
            label={
              <span className="item-label">
               JIRA_URL
              </span>
            }
          >
            <Input
              defaultValue={resDelayData.jira}
              value={modifyData.jira}
              onChange={onChangeInput}
              name="jira"
            />
          </Descriptions.Item>
          <Descriptions.Item
            label={
              <span className="item-label">
                <span style={{ color: 'red' }}>* </span>에러상태
              </span>
            }
          >
            <Select
              defaultValue={
                modifyData.delayStatus === 'T' ? 'Error' : 'Finished'
              }
              style={{ width: 120 }}
              onChange={onChangeStatus}
            >
              <Option /* key="1" */ value="1">Error</Option>
              <Option /* key="0" */ value="0">Finished</Option>
            </Select>
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">응답시간</span>}
          >
            {resDelayData.delayTime}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">응답지연 발생시간</span>}
          >
            {resDelayData.insertDate}
          </Descriptions.Item>
          <Descriptions.Item
            label={
              <span className="item-label">
                <span style={{ color: 'red' }}>* </span>응답지연 해결시간
              </span>
            }
          >
            <DatePicker
              showTime
              placeholder="Select Time"
              onChange={onTimeChange}
              onOk={onOk}
              defaultValue={moment()}
            />
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
            {resDelayData.delayCount}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">평균 응답지연 시간</span>}
          >
            {resDelayData.delayAvg}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">최소 응답지연 시간</span>}
          >
            {resDelayData.delayMin}
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">최대 응답지연 시간</span>}
          >
            {resDelayData.delayMax}
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
            <h5>
              {' '}
              <span style={{ color: 'red' }}>* </span>응답지연 해결 내용
            </h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            <TextArea
              rows={5}
              defaultValue={resDelayData.delayComment}
              name="delayComment"
              onChange={onChangeInput}
            />
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>
              참고 사항
            </h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            <TextArea
              rows={3}
              defaultValue={resDelayData.ref}
              name="ref"
              onChange={onChangeInput}
            />
          </Timeline.Item>
        </Timeline>
      </PageHeader>
    </div>
  );
};

export default withRouter(ResDelayWrite);
