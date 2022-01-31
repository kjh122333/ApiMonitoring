import React, {  useEffect, useState, useCallback } from 'react';
import UserInfo from 'components/user/UserInfo';
import handelException from 'lib/function/request/handleException';
import { withRouter } from 'react-router-dom';
import queryString from 'query-string';
import axios from 'axios';
const UserInfoContainer = ({ history }) => {
  const userId = sessionStorage.getItem('id');
  const employeeNum = sessionStorage.getItem('num');
  const [reRender, setReRender] = useState({});
  const [selectMenu, setSelectMenu] = useState('recordTotal');
  /**
   * 로그인 유저 에러,지연 기록관련
   */
  const [recordCount, setRecordCount] = useState({});
  useEffect(() => {
    const recortCnt = async () => {
      try {
        const response = await axios.get(
          `http://15.165.25.145:9500/user/employee/infocount/${userId}`,
        );

        //console.log('response카운트 :::', response.data);
        const { Count, MainCount, SubCount,NotiCount } = response.data.data;
        setRecordCount({
          count: Count,
          mainCount: MainCount,
          subCount: SubCount,
          alarmCount: NotiCount
        });
      } catch (error) {
        //console.log('에러 카운트:',error.response)
        handelException(error, history);
      }
    };
    recortCnt();
  }, [history, userId,reRender]);

  /**
   * 기록, 미처리 건 데이터 요청
   */
  const [recordData, setRecordData] = useState({});

  useEffect(() => {
    const userRecord = async () => {
      try {
        const response = await axios.get(
          `http://15.165.25.145:9500/user/employee/info/${userId}`,
        );
        const responseAlarm = await axios.get(
          `http://15.165.25.145:9500/user/notification/list/${employeeNum}`,
        );
        //console.log('알림 리스트', responseAlarm.data.list);
        //console.log('response리스트 :::', response.data);
        const { AllList, MainList, SubList } = response.data.data;
        const alarm = responseAlarm.data.list;
        setRecordData({
          recordTotal: AllList,
          recordMain: MainList,
          recordSub: SubList,
          recordAlarm: alarm,
        });
        const { mode } = queryString.parse(history.location.search);
        //console.log('유저정보 페이지 path:', mode);
        if (mode === 'alarm' || selectMenu === 'recordAlarm') {
          setSelectMenu('recordAlarm');
          setData(alarm);
        } else if (mode === 'total' || selectMenu === 'recordTotal') {
          setSelectMenu('recordTotal');
          setData(AllList);
        }else if (mode === 'main' || selectMenu === 'recordMain') {
          setSelectMenu('recordMain');
          setData(MainList);
        }else if (mode === 'sub' || selectMenu === 'recordSub') {
          setSelectMenu('recordSub');
          setData(SubList);
        }
      } catch (error) {
        //console.log('에러 :',error)
        //console.log('에러 :',error.response)
        handelException(error, history);
      }
    };
    userRecord();
  }, [employeeNum, history, userId, reRender, selectMenu]);

  /**
   * [기록, 담당 , 부담당 Value 선택]
   */
  const { recordTotal, recordMain, recordSub, recordAlarm } = recordData;
  const [data, setData] = useState(recordTotal);

  const onDataChange = useCallback(
    target => {
      //console.log('value 값 :', target);
      if (target === 'recordTotal') {
        setData(recordTotal);
        setSelectMenu('recordTotal');
      } else if (target === 'recordMain') {
        setData(recordMain);
        setSelectMenu('recordMain');
      } else if (target === 'recordSub') {
        setData(recordSub);
        setSelectMenu('recordSub');
      } else if (target === 'recordAlarm') {
        setData(recordAlarm);
        setSelectMenu('recordAlarm');
      } else return;
    },
    [recordAlarm, recordMain, recordSub, recordTotal],
  );
  return (
    <UserInfo
      recordCount={recordCount}
      onDataChange={onDataChange}
      data={data}
      selectMenu={selectMenu}
      setReRender={setReRender}
    />
  );
};

export default withRouter(UserInfoContainer);
