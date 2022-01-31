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
  Menu,
  Dropdown,
  Select,
} from 'antd';
import { withRouter } from 'react-router-dom';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep';
import handelException from 'lib/function/request/handleException';

const { confirm } = Modal;
const { TextArea } = Input;
const { Option } = Select;
const ErrorWrite = ({ errdata, match, history, onEdit }) => {
  //console.log('match222', match);
  const { id } = match.params;

  //console.log('errData:', errdata);
  /**
   * 에러상태 Select 함수
   */
  const onChangeStatus = key => {
    setModifyData({
      ...modifyData,
      errorStatus: key === '1' ? 'T' : 'F',
    });
  };
  const [modifyData, setModifyData] = useState({
    ...errdata,
    updateDate: moment().format('YYYY-MM-DD HH:mm:ss'),
  });
  /**
   * Input Onchange 함수
   */
  const onChangeInput = e => {
    setModifyData({
      ...modifyData,
      [e.target.name]: e.target.value,
    });
  };

  /**
   * 날짜 Select 함수
   */
  const onTimeChange = (value, dateString) => {
    setModifyData({
      ...modifyData,
      // updateDate: value.format('YYYY-MM-DD HH:mm:ss'),
      updateDate: dateString,
    });
  };

  /**
   * 달력 날짜 선택
   */
  const onOk = value => {
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
  //console.log('날짜확인:', modifyData.insertDate);
  //console.log('날짜확인 원래:', moment(modifyData.insertDate));
  /**
   * 수정 완료 버튼
   */
  const update = () => {
    confirm({
      title: '수정 하시겠습니까?',
      async onOk() {
        const request = {
          api_err_code: modifyData.errorCode,
          api_err_comment: modifyData.errorComment,
          api_err_msg: modifyData.errorMsg,
          api_err_no: parseInt(id, 10),
          api_err_status: modifyData.errorStatus,
          api_no: modifyData.apiNo,
          insert_timestamp: modifyData.insertDate,
          updated_timestamp: modifyData.updateDate,
          jira_url: modifyData.jira,
          kibana_url: modifyData.kibana,
          ref: modifyData.ref,
        };
        //console.log('전송 데이터 :', request);

        try {
          await axios.patch(
            `http://15.165.25.145:9500/user/apierr/${id}`,
            request,
          );
          // history.goBack();
          onEdit();
        } catch (err) {
          handelException(err, history);
          //console.log(err.response);
          // error({
          //   title: '수정에 실패하였습니다.',
          // });
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
        title="Error Info"
        tags={[
          <Tag key="1" color={modifyData.errorStatus === 'T' ? 'red' : 'green'}>
            {modifyData.errorStatus === 'T' ? 'Error' : 'Finished'}
          </Tag>,
          <span key="2" style={{ fontSize: '120%' }}>
            {renderMethodBage(errdata.http)}
          </span>,
        ]}
        extra={[
          <Button key="1" type="primary" onClick={update}>
            확인
          </Button>,
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
            <Input
              defaultValue={errdata.kibana}
              value={modifyData.kibana}
              onChange={onChangeInput}
              name="kibana"
            />
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">JIRA_URL</span>}
          >
            <Input
              defaultValue={errdata.jira}
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
                modifyData.errorStatus === 'T' ? 'Error' : 'Finished'
              }
              style={{ width: 120 }}
              onChange={onChangeStatus}
            >
              <Option value="1">Error</Option>
              <Option value="0">Finished</Option>
            </Select>
          </Descriptions.Item>
          <Descriptions.Item
            label={<span className="item-label">에러 발생시간</span>}
          >
            {errdata.insertDate}
          </Descriptions.Item>
          <Descriptions.Item
            label={
              <span className="item-label">
                <span style={{ color: 'red' }}>* </span>에러 해결시간
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
            <h5>
              <span style={{ color: 'red' }}>* </span>에러 해결 내용
            </h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            <TextArea
              rows={5}
              defaultValue={errdata.errorComment}
              name="errorComment"
              onChange={onChangeInput}
            />
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>참고 사항</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            <TextArea
              rows={3}
              defaultValue={errdata.ref}
              name="ref"
              onChange={onChangeInput}
            />
          </Timeline.Item>
        </Timeline>
      </PageHeader>
    </div>
  );
};

export default withRouter(ErrorWrite);
