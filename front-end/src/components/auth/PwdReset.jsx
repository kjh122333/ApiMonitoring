import React, { useState } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/auth/login.css';
import queryString from 'query-string';
import { withRouter } from 'react-router-dom';
import { Form, Icon, Input, Button, Alert, Modal } from 'antd';
import axios from 'axios';
import handelException from 'lib/function/request/handleException';

/**
 * Validation
 */

var regex = /^.*(?=^.{8,20}$)(?=.*\d)(?=.*[a-zA-Z])(?=.*[!@#$%^&+=]).*$/;
const validatePwd = value => {
  if (
    regex.test(value)
  ) {
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
const PwdReset = ({ location, history }) => {
  const { token, id } = queryString.parse(location.search);

  sessionStorage.setItem('user', token);
  sessionStorage.setItem('id', id);
  axios.defaults.headers.common['X-AUTH-TOKEN'] = token;
  //console.log('query.token:', token);
  //console.log('query.id:', id);
  const [pwdFail, setPwdFail] = useState(false);
  const [form, setForm] = useState({
    password: { value: '' },
    confirmPassword: { value: '' },
  });
  /**
   * input change
   */
  const onInputChange = e => {
    setForm({
      ...form,
      [e.target.name]: {
        ...validatePwd(e.target.value),
        value: e.target.value,
      },
    });
  };
  const { password, confirmPassword } = form;
  const onResetPwd = async e => {
    e.preventDefault();
    if (
      password.validateStatus === 'error' || //정규식 통과x
      confirmPassword.validateStatus === 'error'
    ) {
      //정규식 통과x
      return;
    }
    if (password.value !== confirmPassword.value) {
      setPwdFail(true);
      return;
    }
    //console.log('password.value :', password.value);
    try {
      await axios.patch(
        `http://15.165.25.145:9500/user/employee/email/${sessionStorage.getItem(
          'id',
        )}`,
        //객체에서 스트링으로 전송데이터 변경(1204)
        { password: password.value },
      );
      Modal.success({
        content: '비밀번호 수정이 완료 되었습니다. 다시 로그인해 주십시오.',
      });
      sessionStorage.clear();
      history.push('/login');
    } catch (error) {
      handelException(error,history)  
      // Modal.error({
      //   content: '비밀번호 수정을 실패하였습니다. 다시 시도해 주십시오.',
      // });
    }
  };
  return (
    <div className="centerOuter">
      <div className="centerInner">
        <Form onSubmit={onResetPwd}>
          <Form.Item>
            <Icon style={{ fontSize: '30px', color: '#1890FF' }} type="user" />
            <h4>비밀번호 초기화</h4>
          </Form.Item>

          <Form.Item
            validateStatus={password.validateStatus}
            help={password.errorMsg}
            hasFeedback
            required
          >
            <Input.Password
              prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
              type="password"
              placeholder="Password"
              name="password"
              value={password.value}
              onChange={onInputChange}
              required
            />
          </Form.Item>
          <Form.Item
            validateStatus={confirmPassword.validateStatus}
            help={confirmPassword.errorMsg}
            hasFeedback
            required
          >
            <Input.Password
              prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
              type="password"
              placeholder="Confirm Password"
              name="confirmPassword"
              value={confirmPassword.value}
              onChange={onInputChange}
              required
            />
          </Form.Item>
          <Form.Item>
            {pwdFail && (
              <Alert
                message={
                  <span>
                    패스워드가 정확하지 않습니다. <br />
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
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
            >
              확인
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default withRouter(PwdReset);
