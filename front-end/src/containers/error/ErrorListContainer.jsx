import React, { useState, useCallback, useEffect } from 'react';
import 'antd/dist/antd.css';
import { Link, withRouter } from 'react-router-dom';
import { Badge, Icon, Modal } from 'antd';
import axios from 'axios';
import ErrorList from 'components/error/ErrorList';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep';
import * as containerFunc from 'lib/function/container/containerFunc';
import _ from 'lodash';
import handelException from 'lib/function/request/handleException';

const ErrorListContainer = ({ history }) => {
  const userId = sessionStorage.getItem('id');

  //로딩 state
  const [loading, setLoading] = useState(false);

  //테이블 필터 state
  const [form, setForm] = useState({
    filteredInfo: null,
    sortedInfo: null,
  });

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
        onSelect: () => {
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

  /**
   * 에러 데이터 요청
   */
  const [errorData, setErrorData] = useState([]);
  const [listKey, setListKey] = useState([]);
  const [dataMenu, setDataMenu] = useState({
    serviceF: [],
    httpF: [],
    // errorCodeF: [],
    departmentF: [],
  });
  useEffect(() => {
    const tempData = [];
    const listKey = []; //모든 Row 선택시 활용
    const serviceF = [];
    const httpF = [];
    // const errorCodeF = [];
    const departmentF = [];
    const res = async () => {
      setLoading(true);
      try {
        const response = await axios.get(
          'http://15.165.25.145:9500/user/apierr',
        );
        //console.log('response data:', response.data.list);
        const list = response.data.list;
        for (let i = 0; i < list.length; i++) {
          tempData.push({
            key: list[i].api_err_no,
            service: list[i].service_name_kr,
            apiNo: list[i].api_no,
            categoryNo: list[i].api_category_no,
            serviceNo: list[i].service_no,
            serviceName: list[i].service_url,
            apiName: list[i].url,
            http: list[i].method,
            errorCode: list[i].api_err_code,
            errorMsg: list[i].api_err_msg,
            errorStatus: list[i].api_err_status,
            department: list[i].service_group_name,
            manager: list[i].employee_name,
            subManager: list[i].employee_sub_name,
            insertDate: list[i].insert_timestamp,
            updateDate: list[i].updated_timestamp,
            kibana: list[i].kibana_url,
            jira: list[i].jira_url,
            ref: list[i].ref,
            managerNo: list[i].employee_no,
            subManagerNo: list[i].employee_sub_no,
          });
          listKey.push(list[i].api_err_no);
          serviceF.push(list[i].service_name_kr); //필터메뉴
          httpF.push(list[i].method); //필터메뉴
          // errorCodeF.push(list[i].api_err_code); //필터메뉴
          departmentF.push(list[i].service_group_name); //필터메뉴
        }
        setErrorData(tempData);
        setListKey(listKey);
        setDataMenu({
          serviceF,
          httpF,
          // errorCodeF,
          departmentF,
        });
        setLoading(false);
      } catch (error) {
        handelException(error, history);
      }
    };
    res();
    return () => window.scrollTo(0, 0);
  }, [history]);

  /**
   * 데이터에 따른 필터메뉴 수정 자동화
   */
  const menuConfig = menuData => {
    return containerFunc.onMenuConfig(menuData, dataMenu);
  };

  /**
   * 컬럼 width 크기 조정
   */
  const columnSize = column => {
    if (column === 'errorStatus') return 100;
    else if (column === 'apiName') return 180;
    else if (column === 'http') return 110;
    // else if (column === 'service') return 150;
    // else if (column === 'serviceName') return 290;
    // else if (column === 'errorCode') return 110;
    // else if (column === 'errorMsg') return 250;
    // else if (column === 'department') return 130;
    // else if (column === 'manager') return 120;
    // else if (column === 'subManager') return 120;
    // else if (column === 'insertDate') return 160;
    // else if (column === 'updateDate') return 160;
    // else if (column === 'kibana' || column === 'jira' || column === 'ref')
    //   return 120;
  };
  /**
   * 컬럼 Fixed
   */
  const columnFixed = column => {
    if (column === 'errorStatus') return 'left';
    else if (column === 'apiName') return 'left';
    else if (column === 'http') return 'left';
  };

  /**
   * 컬럼 설정 함수
   */
  const getColumnConfig = columnsKey => {
    //console.log('columnsKey :', columnsKey);
    const tempColumns = [];
    for (let i = 0; i < columnsKey.length; i++) {
      tempColumns.push({
        key: columnsKey[i].dataIndex,
        title: (
          <span style={{ fontWeight: 'bold' }}>{columnsKey[i].title}</span>
        ),
        width: columnSize(columnsKey[i].dataIndex),
        fixed: columnFixed(columnsKey[i].dataIndex),
        ellipsis:
          columnsKey[i].dataIndex === 'kibana'
            ? true
            : columnsKey[i].dataIndex === 'jira'
            ? true
            : false,
        dataIndex: columnsKey[i].dataIndex,
        filters: menuConfig(columnsKey[i].dataIndex),
        filteredValue: filteredInfo[columnsKey[i].dataIndex] || null,
        onFilter: (value, record) => {
          return record[columnsKey[i].dataIndex]
            .toLowerCase()
            .includes(value.toLowerCase());
        },
        sorter: (a, b) =>
          a[columnsKey[i].dataIndex] < b[columnsKey[i].dataIndex]
            ? -1
            : a[columnsKey[i].dataIndex] > b[columnsKey[i].dataIndex],
        sortOrder:
          sortedInfo.columnKey === columnsKey[i].dataIndex && sortedInfo.order,
        align: 'center',
        render:
          columnsKey[i].dataIndex === 'apiName'
            ? (apiName, record) => {
                return (
                  <span>
                    <Link
                      to={`/api/detail/${record.apiNo}?service_no=${record.serviceNo}&api_category_no=${record.categoryNo}`}
                    >
                      {apiName}
                    </Link>
                  </span>
                );
              }
            : columnsKey[i].dataIndex === 'errorStatus'
            ? (errorStatus, { key }) => {
                const [status, text] =
                  errorStatus === 'T'
                    ? ['error', 'Error']
                    : ['success', 'Finished'];
                return (
                  <span>
                    <Link to={`/error/${key}`}>
                      <Badge
                        status={status}
                        text={text}
                        style={{ color: 'blue' }}
                      />
                    </Link>
                  </span>
                );
              }
            : columnsKey[i].dataIndex === 'http'
            ? http => (
                <span style={{ fontSize: '120%' }}>
                  {renderMethodBage(http.toString().toLowerCase())}
                </span>
              )
            : columnsKey[i].dataIndex === 'kibana'
            ? kibana => (
                <a href={kibana} target="_blank" rel="noopener noreferrer">
                  {kibana}
                </a>
              )
            : columnsKey[i].dataIndex === 'jira'
            ? jira => (
                <a href={jira} target="_blank" rel="noopener noreferrer">
                  {jira}
                </a>
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
    { title: '상태', dataIndex: 'errorStatus' },
    { title: 'API URL', dataIndex: 'apiName' },
    { title: '메소드', dataIndex: 'http' },
    { title: '서비스', dataIndex: 'service' },
    { title: 'Service URL', dataIndex: 'serviceName' },
    { title: '에러코드', dataIndex: 'errorCode' },
    { title: '에러내용', dataIndex: 'errorMsg' },
    { title: '담당부서', dataIndex: 'department' },
    { title: '담당자(정)', dataIndex: 'manager' },
    { title: '담당자(부)', dataIndex: 'subManager' },
    { title: '발생날짜', dataIndex: 'insertDate' },
    { title: '수정날짜', dataIndex: 'updateDate' },
    { title: 'KIBANA_URL', dataIndex: 'kibana' },
    { title: 'JIRA_URL', dataIndex: 'jira' },
    { title: '참고', dataIndex: 'ref' },
  ];
  const [columnsKey, setColumnsKey] = useState(columnsKeyOrigin);

  //컬럼
  const columns = getColumnConfig(columnsKey);

  /**
   * 선택 record 데이터 설정
   */
  const [selectDatum, setSelectDatum] = useState([]);
  const selectDatumFunc = useCallback(() => {
    const tempDatum = [];
    const userCol = columnsKey.map(item => item.dataIndex);
    const originCol = columnsKeyOrigin.map(item => item.dataIndex);
    const deleteKey = originCol.filter(item => !userCol.includes(item));

    for (let i = 0; i < selectedRowKeys.length; i++) {
      errorData.filter(item =>
        item.key === selectedRowKeys[i] ? tempDatum.push(item) : null,
      );
    }
    const copyDatum = _.cloneDeep(tempDatum);
    copyDatum.map(item => {
      deleteKey.map(element => delete item[element]);
      delete item.key;
      delete item.serviceNo;
      delete item.categoryNo;
    });
    setSelectDatum(copyDatum);
    return copyDatum;
  }, [columnsKey, columnsKeyOrigin, errorData, selectedRowKeys]);

  /**
   * 유저 설정 columns 가져오기
   * (api 수정필요)
   */
  useEffect(() => {
    const resColumns = async () => {
      try {
        const response = await axios.get(
          `http://15.165.25.145:9500/user/employee/${userId}`,
        );
        const { data } = response.data;
        if (data.columns_config !== null) {
          const { errorLog } = data.columns_config;
          if (errorLog !== undefined) setColumnsKey(errorLog);
        }
      } catch (error) {
        handelException(error, history);
      }
    };
    resColumns();
  }, [history, userId]);

  // 검색 및 날짜 필터 이벤트
  const onDateChange = date => {
    //console.log('필터 확인 :', date);
    const tempForm = containerFunc.onFilter(date, form);
    setForm(tempForm);
  };
  //console.log('form:::', form);

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

  /**
   * 필터 초기화 이벤트
   */
  const clearAll = () => {
    //console.log('clearAll!!!!!!!');
    setForm(containerFunc.filterClearAll);
  };

  /**
   * Columns DnD
   */
  const onDragEnd = useCallback(
    (fromIndex, toIndex) => {
      if (fromIndex === 0 || toIndex === 0) {
        return;
      }
      const tempKey = containerFunc.onTableDragEnd(
        fromIndex,
        toIndex,
        columnsKey,
      );

      setColumnsKey(tempKey);
    },
    [columnsKey],
  );

  /**
   * Columns show / hide 함수
   * 형식 - ['apiName','http'...]
   */
  const [scrollX, setScrollX] = useState(2240);
  const coulumnsDisplay = checkedValues => {
    const tempArray = containerFunc.onCoulumnsDisplay(
      checkedValues,
      columnsKeyOrigin,
    );
    setColumnsKey(tempArray);

    let width = 0;
    checkedValues.map(item => {
      //console.log('값 :', columnSize(item));
      width += columnSize(item);
    });
    setScrollX(width);
  };
  //console.log('scrollX :', scrollX);
  /**
   * 사용자 Columns 설정(추후 로그인 적용시 본인 employee no적용 )
   * /user/columnsconfig/${employeeNo}
   */
  const userConfig = async () => {
    const userColumns = {
      errorLog: columnsKey,
    };
    //console.log('userColumns  :', userColumns);
    try {
      await axios.patch(
        `http://15.165.25.145:9500/user/columnsconfig/${userId}`,
        userColumns,
      );
      Modal.success({
        content: '설정 완료되었습니다!',
      });
    } catch (error) {
      handelException(error, history);
      // Modal.error({
      //   title: '설정에 실패하였습니다!',
      //   content: '다시 시도해 주십시오..',
      // });
    }
  };

  return !loading ? (
    <ErrorList
      onDateChange={onDateChange}
      clearAll={clearAll}
      columnsKeyOrigin={columnsKeyOrigin} //Display 컬럼 고정(원본 컬럼 키)
      columnsKey={columnsKey} //sorter 메뉴
      columns={columns}
      data={errorData}
      handleChange={handleChange}
      rowSelection={rowSelection} //row 선택 함수
      selectedRowKeys={selectedRowKeys} //선택 row 키 배열 [key넘버...]
      selectDatumFunc={selectDatumFunc} //선택 row 데이터 생성 함수
      selectDatum={selectDatum} //선택 row 데이터 집합
      onDragEnd={onDragEnd} //컬럼 Drag 함수
      coulumnsDisplay={coulumnsDisplay} //컬럼 show/hide 함수
      userConfig={userConfig} //사용자 Columns 설정
      onSorter={onSorter} //정렬 함수
      scrollX={scrollX} //Scroll X크기
    />
  ) : (
    <span style={{ color: 'blue', fontSize: '100px' }}>
      <Icon type="loading" />
    </span>
  );
};

export default withRouter(ErrorListContainer);
