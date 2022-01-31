import React, { useState } from 'react';
import { Modal } from 'antd';
import LoginForm from 'components/auth/LoginForm';
import FindPwdForm from 'components/auth/FindPwdForm';
import axios from 'axios';
import { withRouter, Redirect } from 'react-router-dom';
import { authException } from 'lib/function/request/handleException';

const LoginContainer = ({ history }) => {
  //console.log('인코딩:', new Buffer('sdfssd').toString('base64'));
  const [status, setStatus] = useState(true);
  const [loginFail, setLoginFail] = useState(false);
  const [findFail, setFindFail] = useState(false);
  const [transparency, setTransparency] = useState({ opacity: 1 });

  const changeStatus = () => {
    setStatus(!status);
    setFindFail(false);
    setLoginFail(false);
  };
  const { pathname } =
    history.location.state !== undefined
      ? history.location.state.from
      : { pathname: '/' };
  //로그인 한 경우 원래 페이지로 리다이렉트
  const user = sessionStorage.getItem('user') !== null;
  if (user) {
    return <Redirect to={pathname} />;
  }
  /**
   * 로그인 버튼 이벤트
   */
  const onLogin = async (e, form) => {
    e.preventDefault();
    setTransparency(() => ({ opacity: 0.5 }));
    //console.log('form ::', form);
    const request = {
      ...form,
      id: form.id,
    };
    //console.log('ID 확인:', request.id);
    try {
      const response = await axios.post(
        `http://15.165.25.145:9500/auth/signin`,
        request,
      );
      //console.log('re:', response.data.data);
      const {
        token,
        name,
        getid,
        certification,
        employee_no,
        group_name,
      } = response.data.data;
      sessionStorage.setItem('user', token);
      sessionStorage.setItem('id', getid);
      sessionStorage.setItem('name', name);
      sessionStorage.setItem('num', employee_no);
      sessionStorage.setItem('department', group_name);
      axios.defaults.headers.common['X-AUTH-TOKEN'] = token;
      //첫 로그인 확인
      if (certification === '0') {
        history.push('/firstLogin');
      } else if (certification === '1') {
        history.push(pathname);
      }
    } catch (error) {
      setTransparency({ opacity: 1 });
      const { status } = error.response;
      //console.log('error:', error.response);
      //console.log('error status:', status);
      authException(error) ? setLoginFail(true) : setLoginFail(false);

    } /* finally {
      setTransparency({ opacity: 1 });
    } */
  };

  /**
   * 비밀번호 찾기 <다음> 버튼 이벤트
   */

  const onFindPwd = async (e, { userName, id, email }) => {
    e.preventDefault();
    // message.loading({ content: '잠시만 기다려 주십시오...' });
    setTransparency({ opacity: 0.5 });
    const requeset = {
      name: userName,
      id: id,
      mail: email,
    };
    try {
      await axios.post(`http://15.165.25.145:9500/auth/email`, requeset);
      Modal.success({
        title: '이메일로 인증용 링크가 발송되었습니다.',
        content: '링크를 통해 비밀번호를 변경하십시오.',
      });
      changeStatus();
    } catch (error) {
      setTransparency({ opacity: 1 });
      const { status } = error.response;
      //console.log('errorStatus:', status);
      authException(error) ? setFindFail(true) : setFindFail(false);
      // if (status === 400 || status === 500) setFindFail(true);
      // else handelException(error, history);
    } finally {
      setTransparency({ opacity: 1 });
    }
  };

  return (
    <>
      {status ? (
        <LoginForm
          changeStatus={changeStatus}
          onLogin={onLogin}
          loginFail={loginFail}
          transparency={transparency}
        />
      ) : (
        <FindPwdForm
          changeStatus={changeStatus}
          onFindPwd={onFindPwd}
          findFail={findFail}
          transparency={transparency}
        />
      )}
    </>
  );
};

export default withRouter(LoginContainer);
