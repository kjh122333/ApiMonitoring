import React, { useState, useEffect } from 'react';
import axios from 'axios';
import 'antd/dist/antd.css';
import Profile from 'components/user/Profile';
import { Button, Form, Input, Icon, Row, Col, Alert } from 'antd';
import { authException } from 'lib/function/request/handleException';
import { withRouter } from 'react-router-dom';
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
var regex = /^.*(?=^.{8,20}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;
const validatePwd = value => {
  if (regex.test(value)) {
    return {
      validateStatus: 'success',
      errorMsg: null,
    };
  }

  return {
    validateStatus: 'error',
    errorMsg: '영문+특수문자+숫자 조합(8~20자릿수)!',
  };
};
const ProfileContainer = ({ history }) => {
  const [userCheck, setUserCheck] = useState({
    password: {},
  });
  const [confirm, setConfirm] = useState(false);
  const [pwdFail, setPwdFail] = useState(false);

  /**
   * input change
   */
  const onInputChange = e => {
    setUserCheck({
      password: {
        ...validatePwd(e.target.value),
        value: e.target.value,
      },
    });
  };
  const { password } = userCheck;
  //console.log('userCheck', userCheck);
  /**
   * 유저 본인 확인 요청
   */
  const onFormSubmit = async e => {
    e.preventDefault();
    if (confirm) return;

    const request = {
      id: sessionStorage.getItem('id'),
      password: password.value,
    };

    try {
      const response = await axios.post(
        `http://15.165.25.145:9500/user/self/certification`,
        request,
      );
      //console.log('response :', response.data);
      setConfirm(true);
    } catch (error) {
      //console.log(error);
      if (authException(error)) {
        setConfirm(false);
        setPwdFail(true);
      }
      // if (status === 400) {
      //   setConfirm(false);
      //   setPwdFail(true);
      // } else handelException(error,history);
    }
  };
  const updateSuccess = () => {
    setConfirm(false);
    setUserCheck({ password: { value: '' } });
    window.scrollTo(0, 0);
  };

  return confirm === true ? (
    <Profile passwordOrigin={password.value} updateSuccess={updateSuccess} />
  ) : (
    <div>
      <Form
        {...formItemLayout}
        className="ant-advanced-search-form"
        onSubmit={onFormSubmit}
      >
        <Row>
          <Col span={8} />
          <Col span={8}>
            <Form.Item
              label="비밀번호 확인"
              required
              validateStatus={password.validateStatus}
              help={password.errorMsg}
              hasFeedback
              style={{ marginBottom: '0px' }}
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
                required
                // style={{width:'200px'}}
              />
            </Form.Item>
          </Col>
          <Col span={8} />
        </Row>
        <Row>
          <Col span={8} />
          <Col span={8}>
            <Form.Item>
              {pwdFail && (
                <Alert
                  message={
                    <span>
                      패스워드가 일치하지 않습니다. <br />
                      다시 확인해 주십시오.
                    </span>
                  }
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
              <Button type="primary" htmlType="submit">
                확인
              </Button>
            </Form.Item>
          </Col>
          <Col span={8} />
        </Row>
      </Form>
    </div>
  );
};

export default withRouter(ProfileContainer);
