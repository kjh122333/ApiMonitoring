import React, { useState, useRef } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/auth/login.css';
import { Form, Icon, Modal, Button, Alert, message } from 'antd';
import axios from 'axios';
import { withRouter } from 'react-router-dom';
const FirstLogin = ({ history }) => {
  const [transparency, setTransparency] = useState({ opacity: 1 });
  const onMailSubmit = async e => {
    e.preventDefault();
    setTransparency({ opacity: 0.5 });
    // message.loading({ content: '잠시만 기다려 주십시오...' });
    const requeset = {
      id: sessionStorage.getItem('id'),
    };
    try {
      await axios.post(
        `http://15.165.25.145:9500/user/firstlogin/email`,
        requeset,
      );

      Modal.success({
        title: '이메일로 인증링크를 발송하였습니다.',
      });
    } catch (error) {
      //console.log('error:', error);
      Modal.error({
        title: '이메일 발송을 실패했습니다.',
      });
    } finally {
      setTransparency({ opacity: 1 });
    }
  };
  return (
    <div className="centerOuter">
      <div className="centerInner" style={transparency}>
        <Form onSubmit={onMailSubmit}>
          <Form.Item>
            <Icon style={{ fontSize: '30px', color: '#1890FF' }} type="mail" />
            <h4>본인인증 안내</h4>
          </Form.Item>
          <Form.Item>
            <Alert
              message="본인인증 미확인"
              description="아래 본인 인증 버튼을 눌러 본인인증 완료 후, 다시 로그인 해 주십시오."
              type="info"
              showIcon
            />
          </Form.Item>

          <Form.Item>
            <Button
              type="primary"
              className="login-form-button"
              onClick={() => history.push('/')}
            >
              나중에 변경
            </Button>
            <Button
              type="primary"
              htmlType="submit"
              className="login-form-button"
              loading={transparency.opacity===0.5}
            >
              본인 인증 요청
            </Button>
          </Form.Item>
        </Form>
      </div>
    </div>
  );
};

export default withRouter(FirstLogin);
