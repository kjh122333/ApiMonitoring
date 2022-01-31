import React, { useState } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/auth/login.css';
import { Form, Icon, Input, Button, Alert } from 'antd';

const FindPwdForm = ({ changeStatus, onFindPwd, findFail, transparency }) => {
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
        <Form onSubmit={e => onFindPwd(e, form)}>
          <Form.Item>
            <Icon style={{ fontSize: '30px', color: '#1890FF' }} type="user" />
            <h4>비밀번호 찾기</h4>
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
            <Input
              prefix={
                <Icon type="idcard" style={{ color: 'rgba(0,0,0,.25)' }} />
              }
              placeholder="User name"
              name="userName"
              value={form.userName}
              onChange={onInputChange}
              required
            />
          </Form.Item>
          <Form.Item>
            <Input
              prefix={<Icon type="mail" style={{ color: 'rgba(0,0,0,.25)' }} />}
              placeholder="E-Mail"
              name="email"
              value={form.email}
              onChange={onInputChange}
              required
            />

            {findFail && (
              <Alert
                message={
                  <span>
                    입력하신 정보가 일치하지 않습니다. <br />
                    다시 시도해 주십시오.
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
          </Form.Item>
          <Form.Item>
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
              loading={transparency.opacity===0.5}
            >
              다음
            </Button>
            <Button className="login-form-back" onClick={changeStatus}>
              <Icon type="left-circle" />
              Back
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default FindPwdForm;
