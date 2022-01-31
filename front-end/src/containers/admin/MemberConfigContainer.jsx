import React, { useState,  useEffect } from 'react';
import 'antd/dist/antd.css';
import {  Button, Icon } from 'antd';
import MemberConfig from 'components/admin/MemberConfig';
import axios from 'axios';
import * as containerFunc from 'lib/function/container/containerFunc';
import moment from 'moment';
import UserInfoUpdate from 'components/admin/UserInfoUpdate';
import handelException from 'lib/function/request/handleException';
import { withRouter } from "react-router-dom";
const MemberConfigContainer = ({history}) => {
  //로딩 state
  const [loading, setLoading] = useState(false);

  //테이블 필터 state
  const [form, setForm] = useState({
    filteredInfo: null,
    sortedInfo: null,
  });

  //회원정보 수정 state
  const [updateMode, setUpdateMode] = useState(false);

  //회원추가 Modal state
  const [modalVisible, setModalVisible] = useState(false);

  //회원삭제 state
  const [userDelete, setUserDelete] = useState(false);

  let { sortedInfo, filteredInfo } = form;
  sortedInfo = sortedInfo || {};
  filteredInfo = filteredInfo || {};

  //Row 선택 state
  const [rowSelect, setRowSelect] = useState({
    selectedRowKeys: [], // Check here to configure the default column
    selectCheck: false,
  });
  const { selectedRowKeys, selectCheck } = rowSelect;
  /**
   * Row 선택 함수
   */
  const onSelectChange = selectedRowKeys => {
    //console.log('selectedRowKeys changed: ', selectedRowKeys);
    setRowSelect({ ...rowSelect, selectedRowKeys });
  };
  const rowSelection = {
    selectedRowKeys,
    onChange: onSelectChange,
    hideDefaultSelections: true, //기본 선택옵션 숨김.
    selections: [
      {
        key: 'all-data',
        text: 'Select All Data',
        onSelect: (a, b, c, d) => {
          //console.log('rrrrrr:', a, b, c, d);
          if (!selectCheck) {
            setRowSelect({
              selectCheck: !selectCheck,
              selectedRowKeys: [...listKey], // api_delay_no
            });
          } else {
            setRowSelect({
              selectCheck: !selectCheck,
              selectedRowKeys: [],
            });
          }
        },
      },
    ],
  };
  //console.log('selectedRowKeys:', selectedRowKeys);

  /**
   * 유저 데이터 요청
   */
  const [userData, setUserData] = useState();
  const [listKey, setListKey] = useState([]);
  const [dataMenu, setDataMenu] = useState({
    idF: [],
    userNameF: [],
    userPhoneF: [],
    userEmailF: [],
    departmentF: [],
  });
  useEffect(() => {
    const tempData = [];
    const listKey = []; //모든 Row 선택시 활용
    const idF = [];
    const userNameF = [];
    const userPhoneF = [];
    const userEmailF = [];
    const departmentF = [];
    const res = async () => {
      setLoading(true);
      try {
        const response = await axios.get(
          'http://15.165.25.145:9500/user/employee',
        );
        //console.log('response data:', response.data.list);
        const list = response.data.list;
        for (let i = 0; i < list.length; i++) {
          tempData.push({
            key: list[i].employee_no,
            id: list[i].id,
            userName: list[i].name,
            userPhone: list[i].employee_contact,
            userEmail: list[i].mail,
            department: list[i].group_name,
            insertDate: moment(list[i].insert_timestamp).format('YYYY-MM-DD'),
            updateDate: moment(list[i].updated_timestamp).format('YYYY-MM-DD'),
          });
          listKey.push(list[i].employee_no);
          idF.push(list[i].id); //필터메뉴
          userNameF.push(list[i].name); //필터메뉴
          userPhoneF.push(list[i].employee_contact); //필터메뉴
          userEmailF.push(list[i].mail); //필터메뉴
          departmentF.push(list[i].group_name); //필터메뉴
        }
        setUserData(tempData);
        setListKey(listKey);
        setDataMenu({
          idF,
          userNameF,
          userPhoneF,
          userEmailF,
          departmentF,
        });
        setLoading(false);
      } catch (error) {
        handelException(error,history)  
      }
    };
    setUserDelete(false);

    if (!updateMode && modalVisible) return;
    if (!updateMode || !modalVisible || userDelete) res();
  }, [updateMode, modalVisible, userDelete, setUserDelete, history]);

  /**
   * 데이터에 따른 필터메뉴 수정 자동화
   */
  const menuConfig = menuData => {
    return containerFunc.onMenuConfigAdmin(menuData, dataMenu);
  };

  /**
   * 컬럼 설정 함수
   */
  const getColumnConfig = columnsKey => {
    const tempColumns = [];
    for (let i = 0; i < columnsKey.length; i++) {
      tempColumns.push({
        key: columnsKey[i].dataIndex,
        title: (
          <span style={{ fontWeight: 'bold' }}>{columnsKey[i].title}</span>
        ),
        dataIndex: columnsKey[i].dataIndex,
        filters: menuConfig(columnsKey[i].dataIndex),
        filteredValue: filteredInfo[columnsKey[i].dataIndex] || null,
        onFilter: (value, record) => {
          //console.log('작동확인!!!!');
          return record[columnsKey[i].dataIndex]
            .toLowerCase()
            .includes(value.toLowerCase());
        },
        sorter:
          columnsKey[i].title === '설정'
            ? null
            : (a, b) =>
                a[columnsKey[i].dataIndex] < b[columnsKey[i].dataIndex]
                  ? -1
                  : a[columnsKey[i].dataIndex] > b[columnsKey[i].dataIndex],
        sortOrder:
          sortedInfo.columnKey === columnsKey[i].dataIndex && sortedInfo.order,
        render:
          columnsKey[i].title === '설정'
            ? (text, record) => (
                <Button onClick={() => updatePage(record.id)}>수정</Button>
              )
            : null,
      });
    }
    return tempColumns;
  };

  /**
   * 컬럼 원본 키
   */
  const columnsKeyOrigin = [
    { title: '아이디', dataIndex: 'id' },
    { title: '이름', dataIndex: 'userName' },
    { title: '연락처', dataIndex: 'userPhone' },
    { title: '이메일', dataIndex: 'userEmail' },
    { title: '부서', dataIndex: 'department' },
    { title: '회원가입날짜', dataIndex: 'insertDate' },
    { title: '회원수정날짜', dataIndex: 'updateDate' },
    { title: '설정', dataIndex: 'config' },
  ];
  const [columnsKey, setColumnsKey] = useState(columnsKeyOrigin);

  //컬럼
  const columns = getColumnConfig(columnsKey);
  // //console.log('컬럼 키값 (state):', columnsKey);

  // 검색 및 날짜 필터 이벤트
  const onDateChange = date => {
    const tempForm = containerFunc.onFilterAdmin(date, form);
    setForm(tempForm);
  };
  //정렬 필터 이벤트
  const onSorter = sorterData => {
    setForm({
      ...form,
      sortedInfo: sorterData,
    });
  };

  /**
   * 테이블 변경시 실행
   */
  const handleChange = (pagination, filters, sorter) => {
    //console.log('Various parameters', pagination, filters, sorter);
    const tempForm = containerFunc.onTableChange(pagination, filters, sorter);
    setForm(tempForm);
  };
  //console.log('컬럼 필터 :', form);

  /**
   * 필터 초기화 이벤트
   */
  const clearAll = () => {
    setForm(containerFunc.filterClearAll);
  };

  /**
   * 관리자가 회원정보 수정
   */
  const [userId, setUserId] = useState('');
  const updatePage = userId => {
    //console.log('userId:', userId);
    setUpdateMode(true);
    setUserId(userId);
  };
  return updateMode ? (
    <UserInfoUpdate userId={userId} setUpdateMode={setUpdateMode} />
  ) : !loading ? (
    <MemberConfig
      onDateChange={onDateChange}
      onSorter={onSorter}
      clearAll={clearAll}
      columns={columns}
      columnsKey={columnsKey}
      data={userData}
      handleChange={handleChange}
      rowSelection={rowSelection} //row 선택 함수
      selectedRowKeys={selectedRowKeys} //선택 row 키 배열 [key넘버...]
      modalVisible={modalVisible}
      setModalVisible={setModalVisible}
      setUserDelete={setUserDelete}
      setRowSelect={setRowSelect}
    />
  ) : (
    <span style={{ color: 'blue', fontSize: '100px' }}>
      <Icon type="loading" />
    </span>
  );
};

export default withRouter(MemberConfigContainer);
