import React, { useCallback } from 'react';
import 'antd/dist/antd.css';
import { PageHeader, Icon, List, Row, Col, Statistic, Tag, Badge } from 'antd';
import { Link, withRouter } from 'react-router-dom';
import { renderMethodBage } from '../api/apiWrite/WriteStep';
import handleException from 'lib/function/request/handleException.js';
import axios from 'axios';
const UserInfo = ({
  history,
  recordCount,
  onDataChange,
  data,
  selectMenu,
  setReRender,
}) => {
  const userId = sessionStorage.getItem('id');
  const department = sessionStorage.getItem('department');
  const { count, mainCount, subCount, alarmCount } = recordCount;
  /**
   * 알림 메세지 생성(에러/지연/담당자 배정).
   */
  const renderMessage = useCallback(notiItem => {
    let message = '';
    if (notiItem.err_no !== 0) message = '에러가 발생 했습니다.';
    else if (notiItem.delay_no !== 0) message = '지연이 발생 했습니다.';
    else message = '담당 API가 추가되었습니다.';
    return message;
  }, []);

  /**
   * 알림 1개 읽음 처리.
   */
  const readNotification = useCallback(
    async notifyNo => {
      //console.log(notifyNo);
      try {
        await axios.get(
          `http://15.165.25.145:9500/user/notification/${notifyNo}`,
        );
        setReRender({});
      } catch (error) {
        handleException(error, history);
      }
    },
    [history, setReRender],
  );
  /**
   * 알림 1개 삭제 처리.
   */
  const removeNotification = useCallback(
    async notifyNo => {
      //console.log(notifyNo);
      try {
        await axios.delete(
          `http://15.165.25.145:9500/user/notification/${notifyNo}`,
        );
        setReRender({});
      } catch (error) {
        handleException(error, history);
      }
    },
    [history, setReRender],
  );
  return (
    <div>
      <div>
        <PageHeader
          style={{
            border: '1px solid rgb(235, 237, 240)',
            textAlign: 'left',
          }}
        >
          <Row style={{ marginBottom: '30px', textAlign: 'center' }}>
            <Col span={5}>
              <span
                onClick={() => {
                  setReRender({});
                }}
                style={{
                  cursor: 'pointer',
                  fontSize: '250%',
                  fontWeight: 'bold',
                  color: 'black',
                }}
              >
                <Icon type="user" style={{ fontSize: '300%' }} />
                <br />
                {userId}<br/>[{department}]
              </span>
            </Col>

            <Col
              span={14}
              style={{ display: 'flex', marginTop: '5%', marginLeft: '5%' }}
            >
              <Statistic
                title={
                  <span style={{ fontSize: '120%' }}>
                    <Icon type="solution" />
                    기록
                  </span>
                }
                formatter={() => (
                  <Link
                    style={{
                      // cursor: 'pointer',
                      color: '#1890FF',
                      fontSize: '150%',
                      textDecoration:
                        selectMenu === 'recordTotal' ? 'underline' : null,
                    }}
                    onClick={() => {
                      onDataChange('recordTotal');
                    }}
                    to="/user/info/?mode=total"
                  >
                    {count}
                  </Link>
                )}
                style={{
                  margin: '0 32px',
                }}
              />
              <Statistic
                title={
                  <span style={{ fontSize: '120%' }}>
                    <Icon type="alert" />
                    담당 미처리
                  </span>
                }
                formatter={() => (
                  <Link
                    style={{
                      // cursor: 'pointer',
                      color: '#1890FF',
                      fontSize: '150%',
                      textDecoration:
                        selectMenu === 'recordMain' ? 'underline' : null,
                    }}
                    onClick={() => {
                      onDataChange('recordMain');
                    }}
                    to="/user/info/?mode=main"
                  >
                    {mainCount}
                  </Link>
                )}
                style={{
                  margin: '0 32px',
                }}
              />
              <Statistic
                title={
                  <span style={{ fontSize: '120%' }}>
                    <Icon type="alert" />
                    부담당 미처리
                  </span>
                }
                formatter={() => (
                  <Link
                    style={{
                      // cursor: 'pointer',
                      color: '#1890FF',
                      fontSize: '150%',
                      textDecoration:
                        selectMenu === 'recordSub' ? 'underline' : null,
                    }}
                    onClick={() => {
                      onDataChange('recordSub');
                    }}
                    to="/user/info/?mode=sub"
                  >
                    {subCount}
                  </Link>
                )}
                style={{
                  margin: '0 32px',
                }}
              />
              <Statistic
                title={
                  <span style={{ fontSize: '120%' }}>
                    <Icon type="bell" />
                    알림
                  </span>
                }
                formatter={() => (
                  <Link
                    style={{
                      // cursor: 'pointer',
                      color: '#1890FF',
                      fontSize: '150%',
                      textDecoration:
                        selectMenu === 'recordAlarm' ? 'underline' : null,
                    }}
                    onClick={() => {
                      onDataChange('recordAlarm');
                    }}
                    to="/user/info/?mode=alarm"
                  >
                    {alarmCount}
                  </Link>
                )}
                style={{
                  margin: '0 32px',
                }}
              />
            </Col>
            <Col span={5} />
          </Row>
        </PageHeader>
      </div>
      <div style={{ textAlign: 'left' }}>
        {selectMenu !== 'recordAlarm' ? (
          <List
            itemLayout="horizontal"
            pagination={{
              onChange: page => {
                //console.log(page);
              },
              pageSize: 10,
            }}
            dataSource={data}
            renderItem={item => (
              <List.Item>
                {item.api_err_no !== undefined ? (
                  <>
                    <List.Item.Meta
                      avatar={
                        <Tag color="red" style={{ fontWeight: 'bold' }}>
                          에러
                        </Tag>
                      }
                      title={
                        <Link to={`/error/${item.api_err_no}`}>
                          {item.service_name_kr} {item.api_category_name_kr}{' '}
                          {item.service_url}
                          {item.api_url} {renderMethodBage(item.method)}
                        </Link>
                      }
                      description={
                        <span>
                          {item.api_err_msg}&nbsp;&nbsp;&nbsp;
                          {item.insert_timestamp}&nbsp;(Code:{' '}
                          {item.api_err_code})
                        </span>
                      }
                    />
                    <div>
                      {item.api_err_status === 'T' ? (
                        <Badge status="error" text="Error" />
                      ) : (
                        <Badge status="success" text="Finished" />
                      )}
                    </div>
                  </>
                ) : (
                  <>
                    <List.Item.Meta
                      avatar={
                        <Tag color="orange" style={{ fontWeight: 'bold' }}>
                          지연
                        </Tag>
                      }
                      title={
                        <Link to={`/delay/${item.api_delay_no}`}>
                          {item.service_name_kr} {item.api_category_name_kr}{' '}
                          {item.service_url}
                          {item.api_url} {renderMethodBage(item.method)}
                        </Link>
                      }
                      description={
                        <span>
                          {item.api_delay_msg}&nbsp;&nbsp;&nbsp;
                          {item.insert_timestamp}&nbsp;(Delay:{' '}
                          {item.api_delay_time}초 Code: {item.api_delay_code})
                        </span>
                      }
                    />
                    <div>
                      {item.api_delay_status === 'T' ? (
                        <Badge status="error" text="Error" />
                      ) : (
                        <Badge status="success" text="Finished" />
                      )}
                    </div>
                  </>
                )}
              </List.Item>
            )}
          />
        ) : (
          <div style={{ textAlign: 'left' }}>
            <List
              itemLayout="horizontal"
              pagination={{
                onChange: page => {
                  //console.log(page);
                },
                pageSize: 10,
              }}
              dataSource={data}
              renderItem={item => (
                <List.Item
                  style={{ background: item.is_read === 'F' && '#E6F7FF' }}
                >
                  <>
                    {item.err_no !== 0 ? (
                      <List.Item.Meta
                        avatar={
                          <Tag color="red" style={{ fontWeight: 'bold' }}>
                            에러
                          </Tag>
                        }
                        title={
                          <Link
                            to={`/error/${item.err_no}`}
                            onClick={() => {
                              readNotification(item.notify_no);
                            }}
                          >
                            {renderMessage(item)}{' '}
                            {item.service_url + item.api_url}
                          </Link>
                        }
                        description={item.insert_timestamp}
                      />
                    ) : item.delay_no !== 0 ? (
                      <List.Item.Meta
                        avatar={
                          <Tag color="orange" style={{ fontWeight: 'bold' }}>
                            지연
                          </Tag>
                        }
                        title={
                          <Link
                            to={`/delay/${item.delay_no}`}
                            onClick={() => {
                              readNotification(item.notify_no);
                            }}
                          >
                            {renderMessage(item)}{' '}
                            {item.service_url + item.api_url}
                          </Link>
                        }
                        description={item.insert_timestamp}
                      />
                    ) : (
                      <List.Item.Meta
                        avatar={
                          <Tag color="geekblue" style={{ fontWeight: 'bold' }}>
                            배정
                          </Tag>
                        }
                        title={
                          <Link
                            to={`/api/detail/${item.api_no}?service_no=${item.service_no}&api_category_no=${item.api_category_no}`}
                            onClick={() => {
                              readNotification(item.notify_no);
                            }}
                          >
                            {renderMessage(item)}{' '}
                            {item.service_url + item.api_url}
                          </Link>
                        }
                        description={item.insert_timestamp}
                      />
                    )}
                    <div>
                      <Icon
                        type="check-circle"
                        onClick={e => {
                          e.stopPropagation();
                          readNotification(item.notify_no);
                        }}
                        style={{ marginRight: '3px' }}
                      />
                                              
                      <Icon
                        type="close-circle"
                        onClick={e => {
                          //console.log('test');
                          e.stopPropagation();
                          removeNotification(item.notify_no);
                        }}
                      />
                    </div>
                                    
                  </>
                </List.Item>
              )}
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default withRouter(UserInfo);
