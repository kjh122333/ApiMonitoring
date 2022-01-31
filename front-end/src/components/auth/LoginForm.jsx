import React, { useState, useRef } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/auth/login.css';
import { Form, Icon, Input, Button, Alert } from 'antd';

const LoginForm = ({ changeStatus, onLogin, loginFail,transparency }) => {
  const [form, setForm] = useState({});

  const onInputChange = e => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <div className="centerOuter">
      <div className="centerInner" style={transparency}>
        <Form onSubmit={e => onLogin(e, form)}>
          <Form.Item>
            <Icon style={{ fontSize: '30px', color: '#1890FF' }} type="user" />
            <h4>로그인</h4>
          </Form.Item>
          <Form.Item>
            <Input
              prefix={<Icon type="user" style={{ color: 'rgba(0,0,0,.25)' }} />}
              placeholder="User Id"
              name="id"
              value={form.id}
              onChange={onInputChange}
              required
            />
          </Form.Item>
          <Form.Item>
            <Input.Password
              prefix={<Icon type="lock" style={{ color: 'rgba(0,0,0,.25)' }} />}
              type="password"
              placeholder="Password"
              name="password"
              value={form.password}
              onChange={onInputChange}
              required
            />
            {loginFail && (
              <Alert
                message={<span>아이디 패스워드가 일치하지 않습니다. <br/>다시 시도해 주십시오.</span>}
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
          <Form.Item>
            <Button className="login-form-forgot" onClick={changeStatus}>
              Forgot password
            </Button>
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
              loading={transparency.opacity===0.5}
            >
              Log in
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default LoginForm;
