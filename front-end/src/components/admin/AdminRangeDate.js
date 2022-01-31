import React, { useState } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/admin/search.css';
import {
  Modal,
  Form,
  DatePicker,
  Button,
  Input,
  Select,
  Icon,
  Row,
  Col,
} from 'antd';
import moment from 'moment';
import MemberAdd from './MemberAdd';
import axios from 'axios';
import handelException from 'lib/function/request/handleException';
import { withRouter } from "react-router-dom";
const { RangePicker } = DatePicker;
const { Option, OptGroup } = Select;

const AdminRangeDate = ({
  onDateChange,
  clearAll,
  onSorter,
  columnsKey,
  modalVisible,
  setModalVisible,
  selectedRowKeys,
  setUserDelete,
  setRowSelect,
  history
}) => {
  //날짜 state저장
  const [date, setDate] = useState({
    startDate: '',
    finishDate: '',
  });
  /**
   * Modal 관련 함수
   */
  const showModal = () => {
    setModalVisible(true);
  };

  const modalOk = e => {
    //console.log(e);
    setModalVisible(false);
  };

  const modalCancel = e => {
    //console.log(e);
    setModalVisible(false);
  };

  //Range Picker onChange
  const onChange = data => {
    //console.log('data ', data);
    setDate({
      ...date,
      startDate: data[0],
      finishDate: data[1],
    });
  };

  //Today 이벤트 핸들러
  const onClickToday = () => {
    setDate({
      ...date,

      startDate: moment(),
      finishDate: moment(),
    });
  };
  //기간 단위(7일,1개월,3개월 등) 이벤트 핸들러
  const onClickDate = (previous, unit) => {
    setDate({
      ...date,
      startDate: moment().subtract(previous, unit),
      finishDate: moment(),
    });
  };
  /**
   * Select 이벤트 핸들러
   */
  const onSelectChange = value => {
    onSorter(JSON.parse(value));
  };

  /**
   * Input 이벤트 핸들러
   */
  const onInputChange = e => {
    setDate({
      ...date,
      [e.target.name]: e.target.value,
    });
  };
  //console.log('date :', date);
  /**
   * submit 프로젝트에 맞게 수정 필요
   */

  const handleSubmit = e => {
    e.preventDefault();

    onDateChange(date);
  };

  /**
   * Reset 버튼
   */
  const reset = () => {
    setDate({
      startDate: '',
      finishDate: '',
    });
    clearAll();
  };

  /**
   * OptGroup 동적 생성
   */
  const optCreate = columnKey => {
    //console.log('columnsKey5555', columnsKey);
    return columnKey.map((item, index) => {
      return item.title === '설정' ? null : (
        <OptGroup label={item.title} key={index}>
          <Option
            value={JSON.stringify({
              order: 'ascend',
              columnKey: item.dataIndex,
            })}
          >
            {item.title} 오름차순
          </Option>
          <Option
            value={JSON.stringify({
              order: 'descend',
              columnKey: item.dataIndex,
            })}
          >
            {item.title} 내림차순
          </Option>
        </OptGroup>
      );
    });
  };
  //console.log("selectedRowKeys:",selectedRowKeys)
  /**
   * 회원 삭제
   */
  const userDelete = async () => {
    //row 선택 없을 경우 경고창
    if (!selectedRowKeys.length) {
      Modal.error({
        title: '삭제할 회원정보가 없습니다.',
        content: '회원을 1명 이상 선택해주십시오.',
      });
      return;
    }
    
    //삭제할 회원 번호 전송
    Modal.confirm({
      title: '선택한 회원을 삭제 하시겠습니까?',
      async onOk() {
        try {
          await axios.patch(
            `http://15.165.25.145:9500/admin/employee`,
            selectedRowKeys,
          );
          Modal.success({
            title: '선택한 회원을 삭제 하였습니다.',
          });
          setRowSelect({
            selectCheck: false,
            selectedRowKeys: [],
          });
          setUserDelete(true);
        } catch (error) {
          handelException(error,history)  
          // Modal.error({
          //   title: '회원 삭제에 실패했습니다!',
          // });
        }
      },
    });
  };
  return (
    <div>
      <Form className="ant-advanced-search-form" onSubmit={handleSubmit}>
        <Row>
          <Col span={12}>
            <Form.Item
              style={{ marginBottom: '0px' }}
              label="회원가입 기간 검색"
            >
              <RangePicker
                value={
                  date.startDate && date.finishDate
                    ? [date.startDate, date.finishDate]
                    : []
                }
                onChange={onChange}
              />
              <br />
              <Button onClick={onClickToday}>오늘</Button>
              <Button onClick={() => onClickDate(7, 'd')}>일주일</Button>
              <Button onClick={() => onClickDate(1, 'M')}>1개월</Button>
              <Button onClick={() => onClickDate(3, 'M')}>3개월</Button>
              <Button onClick={() => onClickDate(6, 'M')}>6개월</Button>
              <Button onClick={() => onClickDate(1, 'Y')}>12개월</Button>
            </Form.Item>
          </Col>
          <Col span={9}></Col>
          <Col span={3}>
            <Form.Item style={{ marginBottom: '0px' }}>
              <Button type="primary" onClick={showModal}>
                <Icon type="user-add" />
                회원추가
              </Button>
              <Button type="danger" onClick={userDelete}>
                <Icon type="user-delete" /> 회원삭제
              </Button>
            </Form.Item>
          </Col>
        </Row>

        <Row gutter={16}>
          <Col span={6}>
            <Form.Item label="아이디" style={{ marginBottom: '0px' }}>
              <Input
                name="id"
                value={date.id}
                placeholder="id"
                onChange={onInputChange}
              />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item label="이름" style={{ marginBottom: '0px' }}>
              <Input
                name="userName"
                value={date.userName}
                placeholder="Name"
                onChange={onInputChange}
              />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item label="연락처" style={{ marginBottom: '0px' }}>
              <Input
                name="userPhone"
                value={date.userPhone}
                placeholder="Phone"
                onChange={onInputChange}
              />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={6}>
            <Form.Item label="이메일" style={{ marginBottom: '0px' }}>
              <Input
                name="userEmail"
                value={date.userEmail}
                placeholder="E-mail"
                onChange={onInputChange}
              />
            </Form.Item>
          </Col>
          <Col span={6}>
            <Form.Item label="부서" style={{ marginBottom: '0px' }}>
              <Input
                name="department"
                value={date.department}
                placeholder="department"
                onChange={onInputChange}
              />
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>
          <Col span={6}>
            <Form.Item style={{ marginBottom: '0px' }}>
              <Button onClick={reset}>
                <Icon type="reload" style={{ fontSize: '20px' }} /> 초기화
              </Button>
              <Button type="primary" htmlType="submit">
                <Icon type="search" style={{ fontSize: '20px' }} /> 검색
              </Button>
            </Form.Item>
          </Col>

          <Col span={14}></Col>
          <Col span={4}>
            <Form.Item style={{ marginBottom: '0px' }}>
              <Select
                defaultValue="테이블 정렬 선택"
                style={{ width: '100%' }}
                onChange={onSelectChange}
              >
                {optCreate(columnsKey)}
              </Select>
            </Form.Item>
          </Col>
        </Row>
      </Form>
      {modalVisible && <MemberAdd
        modalVisible={modalVisible}
        modalOk={modalOk}
        modalCancel={modalCancel}
      />}
    </div>
  );
};
export default withRouter(AdminRangeDate);
