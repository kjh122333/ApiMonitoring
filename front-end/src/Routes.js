import React from 'react';
import { Route, Switch } from 'react-router-dom';
import AuthRoute from 'AuthRoute';

import LoginPage from 'pages/LoginPage';
import ApiWritePage from 'pages/ApiWritePage';
import ApiListPage from 'pages/ApiListPage';
import ErrListPage from 'pages/ErrListPage';
import ResponseListPage from 'pages/ResDelayListPage';
import ApiPage from 'pages/ApiPage';
import ErrorPage from 'pages/ErrorPage';
import ResDelayPage from 'pages/ResDelayPage';
import NotFound from 'pages/NotFound';
import MemberConfigPage from 'pages/MemberConfigPage';
import ProfilePage from 'pages/ProfilePage';
import PwdResetPage from 'pages/PwdResetPage';
import FirstLoginPage from 'pages/FirstLoginPage';
import ApiConfigPage from 'pages/ApiConfigPage';
import UserInfoPage from './pages/UserInfoPage';
import GenerateErrPage from './pages/GenerateErrPage';
import ServiceGuide from './components/api/apiWrite/guide/ServiceGuide';

const Routes = () => {
  //console.log('Routes');
  return (
    <Switch>
      <AuthRoute
        path={['/', '/apiList']}
        component={ApiListPage}
        exact
      />
      <AuthRoute path="/apiWrite" component={ApiWritePage} />
      <AuthRoute path="/errList" component={ErrListPage} />
      <AuthRoute path="/delayList" component={ResponseListPage} />
      <AuthRoute path="/api/detail/:id" component={ApiPage} />
      <AuthRoute path="/error/:id" component={ErrorPage} />
      <AuthRoute path="/delay/:id" component={ResDelayPage} />
      <AuthRoute path="/memberConfig" component={MemberConfigPage} />
      <AuthRoute path="/apiConfig" component={ApiConfigPage} />
      <AuthRoute path="/profile" component={ProfilePage} />
      <AuthRoute path="/pwdreset" component={PwdResetPage} />
      <AuthRoute path="/firstLogin" component={FirstLoginPage} />
      <AuthRoute path="/user/info" component={UserInfoPage} />
      <AuthRoute path="/generateErr" component={GenerateErrPage} />

      
      <Route path="/login" component={LoginPage} />
      <Route path="/guide" component={ServiceGuide} />

      <Route component={NotFound} />
    </Switch>
  );
};

export default Routes;
