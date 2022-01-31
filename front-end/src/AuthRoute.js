import React, { useState, useEffect } from 'react';
import { Route, Redirect, withRouter } from 'react-router-dom';
import axios from 'axios';
import queryString from 'query-string';
import handelException from 'lib/function/request/handleException';

const AuthRoute = ({ component, path, history, ...props }) => {
  const { token } = queryString.parse(props.location.search);
  //console.log('props:', props);
  //console.log('session:', sessionStorage.getItem('id'));
  useEffect(() => {
    const userChk = async () => {
      const request = { id: sessionStorage.getItem('id') };
      try {
        const response = await axios.post(
          'http://15.165.25.145:9500/user/check',
          request,
        );
        //console.log('권한체크:', response.data.data);
        // //console.log('메시지:', response);
        setAuthority(response.data.data);
      } catch (error) {
        //console.log('error:', error);
        handelException(error, history);
      }
    };
    if (sessionStorage.getItem('id') !== null) userChk();
  }, [history]);
  const [authority, setAuthority] = useState(''); //권한(user,admin)
  const user = sessionStorage.getItem('user') !== null; //로그인 여부

  if (path === '/pwdreset') {
    return token !== undefined ? (
      <Route {...props} component={component} path={path} />
    ) : (
      <Redirect to="/" />
    );
  }
  if (authority !== '[ROLE_ADMIN]' && path === '/memberConfig') {
    return <Redirect to="/" />;
  }
  return (
    <div>
      {user ? (
        <Route {...props} component={component} path={path} />
      ) : (
        <Redirect
          to={{ pathname: '/login', state: { from: props.location } }}
        />
      )}
    </div>
  );
};

export default withRouter(AuthRoute);
