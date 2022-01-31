import React, { useState, useEffect, useCallback } from 'react';
import { Layout, Icon, BackTop } from 'antd';
import { withRouter } from 'react-router-dom';
import TemplateSidebar from './TemplateSidebar';

const { Content, Footer } = Layout;

const TemplateLayout = ({ children, history }) => {
  // const userId = sessionStorage.getItem('id');
  // const user = sessionStorage.getItem('user') !== null;
  // //console.log('TemplateLayout :', TemplateLayout);
  const [state, setState] = useState({
    collapsed: false,
    selectedKey: ['1'],
  });

  const pathname = history.location.pathname.split('/')[1];
  const pathname2 = history.location.pathname.split('/')[2];
  //console.log('패쓰네임:', pathname);
  const onCollapse = collapsed => {
    // //console.log(collapsed);
    setState({ ...state, collapsed });
  };
  const onSelectedKey = useCallback(selectedKey => {
    setState(state => ({ ...state, selectedKey: [selectedKey] }));
  }, []);
  useEffect(() => {
    if (pathname === 'apiList') onSelectedKey('1');
    else if (pathname === 'apiWrite') onSelectedKey('2');
    else if (pathname === 'errList') onSelectedKey('3');
    else if (pathname === 'delayList') onSelectedKey('4');
    else if (pathname === 'generateErr') onSelectedKey('5');
    else if (pathname === 'apiConfig') onSelectedKey('7');
  }, [onSelectedKey, pathname]);

  if (
    pathname === 'login' ||
    pathname === 'pwdreset' ||
    pathname === 'firstLogin' ||
    pathname === 'guide'
  )
    return <div>{children}</div>;
  else if (
    pathname === '' ||
    pathname === 'apiList' ||
    pathname === 'apiWrite' ||
    pathname === 'errList' ||
    pathname === 'delayList' ||
    (pathname === 'api' && pathname2 === 'detail') ||
    pathname === 'error' ||
    pathname === 'delay' ||
    pathname === 'memberConfig' ||
    pathname === 'apiConfig' ||
    pathname === 'profile' ||
    (pathname === 'user' && pathname2 === 'info') ||
    pathname === 'generateErr'
  )
    return (
      <Layout style={{ minHeight: '100vh' }}>
        <TemplateSidebar
          state={state}
          onCollapse={onCollapse}
          onSelectedKey={onSelectedKey}
        />
        <Layout>
          <Content style={{ margin: '20px 20px 20px 20px' }}>
            <div style={{ padding: 24, background: '#fff', minHeight: 360 }}>
              <BackTop visibilityHeight={300} />
              {children}
            </div>
          </Content>
          <Footer style={{ textAlign: 'center' }}>
            API Monitoring <br />
            ©2019 Created by DNA Team <Icon type="team" />
          </Footer>
        </Layout>
      </Layout>
    );
  else return <div>{children}</div>;
};

export default withRouter(TemplateLayout);
