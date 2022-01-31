import React, { useState, useEffect } from 'react';
import 'antd/dist/antd.css';
import {
  PageHeader,
  Button,
  Descriptions,
  Form,
  Input,
  Icon,
  Select,
  Modal,
  Row,
  Col,
  Alert,
} from 'antd';
import axios from 'axios';
import handelException from 'lib/function/request/handleException';
import { withRouter } from "react-router-dom";

const { Option } = Select;
const formItemLayout = {
  labelCol: {
    xs: { span: 24 },
    sm: { span: 8 },
  },
  wrapperCol: {
    xs: { span: 24 },
    sm: { span: 20 },
  },
};
/**
 * Validation
 */

const regex = /^.*(?=^.{8,20}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/; //패스워드
const regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i; //이메일 형식
const regPhone = /^\d{3}-\d{3,4}-\d{4}$/; //연락처
const regKor = /^[가-힣]+$/; // 한글
const validate = (name, value, setSubmitChk) => {
  //console.log('name:', name);
  if (name === 'name' && regKor.test(value)) {
    setSubmitChk(false);
    return {
      validateStatus: 'success',
      value: value,
    };
  } else if (
    (name === 'password' || name === 'confirmPassword') &&
    regex.test(value)
  ) {
    setSubmitChk(false);
    return {
      validateStatus: 'success',
      value: value,
    };
  } else if (name === 'mail' && regEmail.test(value)) {
    setSubmitChk(false);
    return {
      validateStatus: 'success',
      value: value,
    };
  } else if (name === 'phone' && regPhone.test(value)) {
    setSubmitChk(false);
    return {
      validateStatus: 'success',
      value: value,
    };
  } else if (
    (name === 'password' || name === 'confirmPassword') &&
    value.length === 0
  ) {
    setSubmitChk(false);
    return {
      value: value,
    };
  }
  setSubmitChk(true);
  return {
    validateStatus: 'error',
    value: value,
  };
};
const pwdTips = (
  <span>
    영문+특수문자+숫자 조합
    <br />
    (8~20자릿수)
  </span>
);
const mailTips = 'ex)abcd@gmail.com';
const phoneTips = 'ex) 010-1234-5678';

const Profile = ({ passwordOrigin, updateSuccess,history }) => {
  const [department, setDepartment] = useState([]);
  const [infoOrigin, setInfoOrigin] = useState({});
  const [userInfo, setUserInfo] = useState({
    name: { value: '' },
    password: { value: '' },
    confirmPassword: { value: '' },
    mail: { value: '' },
    phone: { value: '' },
    group_no: { value: 0 },
  });
  const [submitChk, setSubmitChk] = useState(false);
  /**
   * 유저 데이터 요청
   */
  useEffect(() => {
    const resColumns = async () => {
      try {
        const response = await axios.get(
          `http://15.165.25.145:9500/user/employee/${sessionStorage.getItem(
            'id',
          )}`,
        );
        const info = response.data.data;
        //console.log('데이터:', info);
        setUserInfo({
          password: { value: '' },
          name: { value: info.name },
          confirmPassword: { value: '' },
          phone: { value: info.employee_contact },
          mail: { value: info.mail },
          group_no: { value: info.group_no },
        });
        setInfoOrigin({
          id: info.id,
          name: info.name,
          phone: info.employee_contact,
          mail: info.mail,
          department: info.group_name,
        });
      } catch (error) {
        handelException(error,history)  
      }
    };
    resColumns();
  }, [history]);
  /**
   * 담당부서 데이터
   */
  useEffect(() => {
    const resColumns = async () => {
      try {
        const response = await axios.get(
          `http://15.165.25.145:9500/user/groupname`,
        );
        const { list } = response.data;
        //console.log('데이터:', list);
        setDepartment(list);
      } catch (error) {
        handelException(error,history)  
      }
    };
    resColumns();
  }, [history]);

  const onInputChange = e => {
    setUserInfo({
      ...userInfo,
      [e.target.name]: {
        ...validate(e.target.name, e.target.value, setSubmitChk),
      },
    });
  };
  const { name, password, confirmPassword, mail, phone, group_no } = userInfo;

  /**
   * Select 이벤트 핸들러
   */
  const onSelectChange = value => {
    //console.log('value :', value);
    setUserInfo({
      ...userInfo,
      group_no: {
        value: value,
      },
    });
  };

  /**
   * OptGroup 동적 생성
   */
  const optCreate = () => {
    // //console.log('columnsKey5555', columnsKey);
    return department.map((item, index) => {
      return (
        <Option key={index} value={item.group_no}>
          {item.group_name}
        </Option>
      );
    });
  };

  /**
   * 폼 데이터 제출
   */
  const [pwdFail, setPwdFail] = useState(false);
  const onSubmit = e => {
    e.preventDefault();
    if (submitChk) {
      //console.log('1번');
      return;
    }
    if (password.value !== confirmPassword.value) {
      //console.log('2번');
      setPwdFail(true);
      return;
    }
    const requeset = {
      name: name.value,
      password: password.value.length === 0 ? passwordOrigin : password.value,
      mail: mail.value,
      employee_contact: phone.value,
      group_no: group_no.value,
    };
    //console.log('requeset:', requeset);
    Modal.confirm({
      title: '회원정보를 수정 하시겠습니까?',
      async onOk() {
        try {
          await axios.patch(
            `http://15.165.25.145:9500/user/employee/${sessionStorage.getItem(
              'id',
            )}`,
            requeset,
          );
        } catch (error) {
          handelException(error,history)  
          // Modal.error({
          //   title: '회원정보 수정에 실패했습니다.',
          // });
        }
        Modal.success({
          title: '회원정보 수정이 완료되었습니다.',
        });
        updateSuccess();
      },
    });
  };
  const descriptions = () => {
    const label = [
      { name: '아이디', value: infoOrigin.id },
      { name: '이름', value: infoOrigin.name },
      { name: '연락처', value: infoOrigin.phone },
      { name: '이메일', value: infoOrigin.mail },
      { name: '담당부서', value: infoOrigin.department },
    ];
    return label.map((item, index) => {
      return (
        <Descriptions.Item label={<span style={{fontWeight:'bold',fontSize:'15px'}}>{item.name}</span>} key={index}>
          {item.value}
        </Descriptions.Item>
      );
    });
  };

  return (
    <div>
      <div>
        <PageHeader
          style={{
            border: '1px solid rgb(235, 237, 240)',
          }}
          title="회원정보 조회/수정"
        >
          <Descriptions size="small" column={3} style={{ textAlign: 'left' }}>
            {descriptions()}
          </Descriptions>
        </PageHeader>
      </div>
      <div>
        <Form
          {...formItemLayout}
          className="ant-advanced-search-form"
          onSubmit={onSubmit}
        >
          <Row>
            <Col span={8} />
            <Col span={8}>
              <Form.Item label="아이디">
                <Input
                  prefix={
                    <Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  placeholder="id"
                  name="id"
                  value={infoOrigin.id}
                  disabled
                />
              </Form.Item>
            </Col>
            <Col span={8} />
          </Row>
          <Row>
            <Col span={8} />
            <Col span={8}>
              <Form.Item
                label="이름"
                hasFeedback
                validateStatus={name.validateStatus}
              >
                <Input
                  prefix={
                    <Icon type="idcard" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  placeholder="User name"
                  name="name"
                  value={name.value}
                  onChange={onInputChange}
                />
              </Form.Item>
            </Col>
            <Col span={8} />
          </Row>
          <Row>
            <Col span={8} />
            <Col span={8}>
              <Form.Item
                label="비밀번호 수정"
                hasFeedback
                validateStatus={password.validateStatus}
                help={pwdTips}
              >
                <Input.Password
                  prefix={
                    <Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  type="password"
                  placeholder="Password"
                  name="password"
                  value={password.value}
                  onChange={onInputChange}
                  // required={pwdUpdate}
                />
              </Form.Item>
            </Col>
            <Col span={8} />
          </Row>
          <Row>
            <Col span={8} />
            <Col span={8}>
              <Form.Item
                label="비밀번호 확인"
                hasFeedback
                validateStatus={confirmPassword.validateStatus}
              >
                <Input.Password
                  prefix={
                    <Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  type="password"
                  placeholder="Confirm Password"
                  name="confirmPassword"
                  value={confirmPassword.value}
                  onChange={onInputChange}
                  // required={pwdUpdate}
                />
                {pwdFail && (
                  <Alert
                    message={<span>패스워드가 일치하지 않습니다.</span>}
                    type="error"
                    showIcon
                    icon={
                      <Icon
                        type="exclamation-circle"
                        theme="twoTone"
                        twoToneColor="red"
                      />
                    }
                  />
                )}
              </Form.Item>
            </Col>
            <Col span={8} />
          </Row>
          <Row>
            <Col span={8} />
            <Col span={8}>
              <Form.Item
                label="이메일"
                hasFeedback
                validateStatus={mail.validateStatus}
                help={mailTips}
              >
                <Input
                  prefix={
                    <Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  placeholder="E-Mail"
                  name="mail"
                  value={mail.value}
                  onChange={onInputChange}
                  // required
                />
              </Form.Item>
            </Col>
            <Col span={8} />
          </Row>

          <Row>
            <Col span={8} />
            <Col span={8}>
              <Form.Item
                label="연락처"
                hasFeedback
                validateStatus={phone.validateStatus}
                help={phoneTips}
              >
                <Input
                  prefix={
                    <Icon type="phone" style={{ color: 'rgba(0,0,0,.25)' }} />
                  }
                  placeholder="Phone Number"
                  name="phone"
                  value={phone.value}
                  onChange={onInputChange}
                  
                  // required
                />
              </Form.Item>
            </Col>
            <Col span={8} />
          </Row>
          <Row>
            <Col span={8} />
            <Col span={8}>
              <Form.Item label="담당부서">
                <Select
                  value={group_no.value}
                  style={{ width: '100%' }}
                  onChange={onSelectChange}
                >
                  {optCreate()}
                </Select>
              </Form.Item>
            </Col>
            <Col span={8} />
          </Row>
          <Row>
            <Col span={8} />
            <Col span={8}>
              <Form.Item>
                <Button
                  type="primary"
                  htmlType="submit"
                  className="login-form-button"
                >
                  회원정보 수정
                </Button>
              </Form.Item>
            </Col>
            <Col span={8} />
          </Row>
        </Form>
      </div>
    </div>
  );
};

export default withRouter(Profile);
