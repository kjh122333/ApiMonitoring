import React, { useState, useEffect, useRef } from 'react';
import {
  Layout,
  Menu,
  Icon,
  Affix,
  Avatar,
  Dropdown,
  Row,
  Col,
  Badge,
  notification,
  Popover,
  Modal
} from 'antd';
import { Link } from 'react-router-dom';
import axios from 'axios';
import handleException from 'lib/function/request/handleException.js';
import { withRouter } from 'react-router-dom';
import Websocket from 'react-websocket';
import { useCallback } from 'react';
import NotiList from 'components/notification/NotiList';

const { Sider } = Layout;
const { SubMenu } = Menu;

const TemplateSidebar = ({ state, onCollapse, onSelectedKey, history }) => {
  const userId = sessionStorage.getItem('id');
  const department = sessionStorage.getItem('department');
  const user = sessionStorage.getItem('user') !== null;
  const [authority, setAuthority] = useState(false); //권한(user,admin)
  const [notiCnt, setNotiCnt] = useState(0);
  const [popOverVisible, setPopOverVisible] = useState(false);

  const closePopOverVisible = useCallback(() => {
    setPopOverVisible(false);
  }, []);

  const handleVisibleChange = visible => {
    //console.log('vi :',visible)
    setPopOverVisible(visible);
  };

  const openNotificationWithIcon = (socketMessage) => {
    const {type,url,employee_name,employee_sub_name} = socketMessage;
    const notiType = (socketMessage.type==='err' || socketMessage.type==='delay') ? 'error' : 'info';
    let message = '';

    if(type==='err') message = '에러가 발생 했습니다.'
    else if(type==='delay') message = '지연이 발생 했습니다.'
    else message = '담당 API가 추가되었습니다.'

    notification[notiType]({
      message: message,
      description: (employee_name!==undefined && employee_sub_name!==undefined) ? url+'\n'+`담당자(정, 부) : ${employee_name}, ${employee_sub_name}` : url
    });
  };

  //console.log('111111111111',user,userId);

  const notiCntToZero = useCallback(()=>{
    setNotiCnt(0);
  },[])

  const refWebSocket = useRef();

  const handleData = useCallback((message)=> {
    const data = JSON.parse(message);
    //console.log('소켓 데이터 옴', data);
    
    setNotiCnt(data.message.count);
    if(data.message.hasOwnProperty('url')){ //url 속성은 에러/지연 발생때만 있는 키. 소켓 처음 연결할 때는 없음.
      openNotificationWithIcon(data.message);
      //배정일땐 인포로
    }
  }, []);

  const sendMessage = useCallback(message => {
    //console.log(refWebSocket);
    refWebSocket.current.sendMessage(message);
  }, []);

  const handleOpen = useCallback(() => {
    //console.log('소켓 connected:)');
    try {
      const join = {
        "type":"JOIN",
        "roomId":userId
      }
      const resWS = sendMessage(JSON.stringify(join));
      //console.log('조인조인조인조인', resWS);
    } catch (error) {
      handleException(error, history);
    }
  }, [history, sendMessage, userId]);

  const handleClose = useCallback(() => {
    //소켓 연결 확인용
    // Modal.error({
    //   title: '알림 서버 연결 끊김',
    // });
    //console.log('소켓 disconnected:(');
  }, []);

  //console.log('사이더바 렌더링:', state);

  useEffect(() => {
    const userChk = async () => {
      const request = { id: sessionStorage.getItem('id') };
      try {
        const response = await axios.post(
          'http://15.165.25.145:9500/user/check',
          request,
        );
        //console.log('권한:', response.data.data);
        const adminChk = response.data.data === '[ROLE_ADMIN]';
        setAuthority(adminChk);
      } catch (error) {
        handleException(error, history);
      }
    };
    if (sessionStorage.getItem('id') !== null) userChk();
  }, [history]);
  /**
   * 로그아웃 버튼 이벤트
   */
  const logout = () => {
    sessionStorage.clear();
    history.push('/login');
  };

  /**
   * 사용자 아이콘 클릭 시 메뉴
   */
  const menu = (
    <Menu>
      <Menu.Item
        onClick={() => {
          history.push('/profile');
        }}
      >
        <Icon type="user" />
        &nbsp;&nbsp;정보수정
      </Menu.Item>

      <Menu.Divider />
      <Menu.Item onClick={logout}>
        <Icon type="logout" />
        <span style={{ fontWeight: 'bold' }}>&nbsp;&nbsp;로그아웃</span>
      </Menu.Item>
    </Menu>
  );

  return (
    <Affix offsetTop={10} style={{ backgroundColor: '#001529' }}>
      <Sider
        collapsible
        collapsed={state.collapsed}
        onCollapse={onCollapse}
        theme="dark"
      >
        <div
          style={{
            fontSize: '60px',
            color: '#1890FF',
            // borderBottom: '0.5px solid #585858',
          }}
        >
          <Link to="/">
            <Icon type="laptop" />
          </Link>
          {/* <br /> */}
          <p style={{ fontSize: '15px', fontWeight: 'bold' }}>API Monitoring</p>
        </div>
        {user && (
        <div
          style={{ fontSize: '30px', marginBottom: '100px', textAlign: 'left' }}
        >
          <Row>
            <Col
              span={!state.collapsed ? 5 : 12}
              style={{
                marginTop: '8px',
                marginLeft: '10px',
                cursor: 'pointer',
              }}
            >
              <Avatar
                size="middle"
                style={{ color: 'white', backgroundColor: '#848484' }}
                onClick={() => {
                  history.push('/user/info');
                }}
              >
                {user && userId.toUpperCase().substring(0, 1)}
              </Avatar>
            </Col>
            {!state.collapsed && (
              <Col span={14} style={{ display: 'grid', marginTop: '12px' }}>
                <>
                  <span
                    style={{
                      fontSize: '15px',
                      fontWeight: 'bold',
                      color: 'white',
                      cursor: 'pointer',
                    }}
                    onClick={() => {
                      history.push('/user/info');
                    }}
                  >
                    {userId.length > 10 ? userId.substring(0, 10) + '...' : userId}
                  </span>
                  <span
                    style={{
                      fontSize: '14px',
                      color: '#BDBDBD',
                    }}
                  >
                    {department}
                  </span>
                </>
              </Col>
            )}
            <Col
              span={!state.collapsed ? 3 : 3}
              style={{ display: 'grid', marginTop: '14px' }}
            >
              <Dropdown overlay={menu} trigger={['click']}>
                <Badge count="0">
                  <Icon
                    type="setting"
                    theme="filled"
                    style={{
                      fontSize: '15px',
                      color: 'white',
                      marginBottom: '10px',
                      cursor: 'pointer',
                    }}
                  />
                </Badge>
              </Dropdown>
              
              {user && (
                <Websocket
                url="ws://15.165.25.145:9500/ws/chat"
                onMessage={handleData}
                onOpen={handleOpen}
                onClose={handleClose}
                reconnect={true}
                debug={true}
                ref={refWebSocket}
                />
              )}
              <Popover
                placement="rightTop"
                title={
                  <>
                    <span style={{ fontSize: '15px', fontWeight: 'bold' }}>
                      알림 목록
                    </span>
                    <Link to="/user/info?mode=alarm" style={{ float: 'right' }}>
                      더보기
                    </Link>
                  </>
                }
                onVisibleChange={handleVisibleChange}
                content={
                  <NotiList
                    history={history}
                    popOverVisible={popOverVisible}
                    closePopOverVisible={closePopOverVisible}
                    notiCntToZero={notiCntToZero}
                  />
                }
                trigger="click"
                visible={popOverVisible}
              >
                <Badge count={notiCnt}>
                  <Icon
                    type="bell"
                    theme="filled"
                    style={{
                      fontSize: '15px',
                      color: 'white',
                      cursor: 'pointer',
                    }}
                  />
                </Badge>
              </Popover>
            </Col>
          </Row>
        </div>
        )}
        <Menu
          // defaultSelectedKeys={state.selectedKey}
          selectedKeys={state.selectedKey}
          // defaultOpenKeys={['sub1']}
          mode="inline"
          theme="dark"
          style={{
            textAlign: 'left',
          }}
        >
          <Menu.Item key="1">
            <Link to="/apiList" onClick={() => onSelectedKey('1')}>
              <Icon type="desktop" />
              <span>API 실시간 현황</span>
            </Link>
          </Menu.Item>

          <Menu.Item key="3">
            <Link to="/errList" onClick={() => onSelectedKey('3')}>
              <Icon type="unordered-list" />
              <span>에러 기록</span>
            </Link>
          </Menu.Item>
          <Menu.Item key="4">
            <Link to="/delayList" onClick={() => onSelectedKey('4')}>
              <Icon type="unordered-list" />
              <span>응답지연 기록</span>
            </Link>
          </Menu.Item>

          <Menu.Item key="2">
            <Link to="/apiWrite" onClick={() => onSelectedKey('2')}>
              <Icon type="plus-circle" />
              <span>API 추가</span>
            </Link>
          </Menu.Item>
          <Menu.Item key="5">
            <Link to="/generateErr" onClick={() => onSelectedKey('5')}>
              <Icon type="plus-circle" />
              <span>에러/지연 추가</span>
            </Link>
          </Menu.Item>
          <Menu.Item key="7">
            <Link to="/apiConfig" onClick={() => onSelectedKey('7')}>
              <Icon type="plus-circle" />
              <span>API 관리</span>
            </Link>
          </Menu.Item>
          {authority && (
            <SubMenu
              key="sub1"
              title={
                <span>
                  <Icon type="setting" theme="filled" />
                  <span>관리자 메뉴</span>
                </span>
              }
            >
              <Menu.Item key="6">
                <Link to="/memberConfig" onClick={() => onSelectedKey('6')}>
                  <Icon type="usergroup-add" />
                  <span>회원관리</span>
                </Link>
              </Menu.Item>
            </SubMenu>
          )}
        </Menu>
      </Sider>
    </Affix>
    // </div>
  );
};

export default withRouter(TemplateSidebar);
