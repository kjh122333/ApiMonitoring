import React, { useState, useEffect, useCallback } from 'react';
import { Badge, Icon, Modal, Button } from 'antd';
import ApiList from 'components/api/ApiList';
import axios from 'axios';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep';
import * as containerFunc from 'lib/function/container/containerFunc';
import handelException from 'lib/function/request/handleException';
import { withRouter, Link } from 'react-router-dom';
const ApiListContainer = ({ history }) => {
  const userId = sessionStorage.getItem('id');

  //로딩 state
  const [loading, setLoading] = useState(false);
  /**
   * Table 관련 함수
   */
  const [form, setForm] = useState({
    filteredInfo: null,
    sortedInfo: null,
  });
  let { sortedInfo, filteredInfo } = form;
  sortedInfo = sortedInfo || {};
  filteredInfo = filteredInfo || {};

  /**
   * Radio버튼 관련
   */
  const [radio, setRadio] = useState('apiList');
  const onRadioChange = e => {
    // ////console.log('radio checked', e.target.value);
    setRowSelect({
      // selectCheck: !selectCheck,
      selectedRowKeys: [],
    });
    clearAll(); //테이블 이동시 필터 초기화
    setRadio(e.target.value);
  };

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
    // ////console.log('selectedRowKeys changed: ', selectedRowKeys);
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
              selectedRowKeys:
                radio === 'apiList'
                  ? [...listKey]
                  : radio === 'errorList'
                  ? [...eListKey]
                  : [...dListKey],
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
  //////console.log('selectedRowKeys:', selectedRowKeys);

  /**
   * 선택 record 데이터 설정
   */
  const [selectDatum, setSelectDatum] = useState([]);
  const selectDatumFunc = () => {
    const tempDatum = containerFunc.onSelectDatumFunc(
      radio,
      selectedRowKeys,
      apiAllData,
      errorData,
      delayData,
      columnsKey,
      columnsKeyErr,
      columnsKeyDelay,
      columnsKeyOrigin,
      columnsKeyOriginErr,
      columnsKeyOriginDelay,
    );
    setSelectDatum(tempDatum);
    return tempDatum;
  };

  /**
   * api 전체 현황 데이터 요청
   */
  const [apiAllData, setApiAllData] = useState([]);
  const [listKey, setListKey] = useState([]);
  const [dataMenu, setDataMenu] = useState({
    serviceF: [],
    // errorStatusF: [],
    // dealyStatusF: [],
    httpF: [],
    departmentF: [],
  });
  useEffect(() => {
    const tempData = [];
    const listKey = []; //모든 Row 선택시 활용
    const serviceF = [];
    // const errorStatusF = [];
    // const dealyStatusF = [];
    const httpF = [];
    const departmentF = [];
    const res = async () => {
      setLoading(true);

      try {
        const response = await axios.get(
          'http://15.165.25.145:9500/user/api/isdeletef',
        );
        //console.log('response data API:', response.data.list);
        const list = response.data.list;
        for (let i = 0; i < list.length; i++) {
          tempData.push({
            key: list[i].api_no,
            service: list[i].service_name_kr,
            categoryNo: list[i].api_category_no,
            serviceNo: list[i].service_no,
            serviceName: list[i].service_url,
            apiName: list[i].url,
            function: list[i].description,
            http: list[i].method,
            errorStatus: list[i].err_status,
            delayStatus: list[i].delay_status,
            department: list[i].service_group_name,
            manager: list[i].employee_name,
            subManager: list[i].employee_sub_name,
            insertDate: list[i].insert_timestamp,
            updateDate: list[i].updated_timestamp,
            managerNo: list[i].employee_no,
            subManagerNo: list[i].employee_sub_no,
          });
          listKey.push(list[i].api_no);
          serviceF.push(list[i].service_name_kr); //필터메뉴
          // errorStatusF.push(list[i].err_status); //필터메뉴
          // dealyStatusF.push(list[i].delay_status); //필터메뉴
          httpF.push(list[i].method); //필터메뉴
          departmentF.push(list[i].service_group_name); //필터메뉴
        }
        setApiAllData(tempData);
        setListKey(listKey);
        setDataMenu({
          serviceF,
          // errorStatusF,
          // dealyStatusF,
          httpF,
          departmentF,
        });
        setLoading(false);
      } catch (error) {
        handelException(error, history);
      }
    };
    res();
  }, [history]);

  /**
   * 에러 데이터 요청
   */
  const [errorData, setErrorData] = useState([]);
  const [eListKey, setEListKey] = useState([]);
  const [errorDataMenu, setErrorDataMenu] = useState({
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
    const departmentF = [];
    const res = async () => {
      try {
        const response = await axios.get(
          'http://15.165.25.145:9500/user/apierr/status',
        );
        //console.log('response data ERROR:', response.data.list);
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
          departmentF.push(list[i].service_group_name); //필터메뉴
        }
        setErrorData(tempData);
        setEListKey(listKey);
        setErrorDataMenu({
          serviceF,
          httpF,
          departmentF,
        });
      } catch (error) {
        handelException(error, history);
      }
    };
    res();
  }, [history]);

  /**
   * 응답지연 데이터 요청
   */
  const [delayData, setDelayData] = useState([]);
  const [dListKey, setDListKey] = useState([]);
  const [delayDataMenu, setDelayDataMenu] = useState({
    serviceF: [],
    httpF: [],
    // responseCodeF: [],
    departmentF: [],
  });
  useEffect(() => {
    const tempData = [];
    const dListKey = []; //모든 Row 선택시 활용
    const serviceF = [];
    const httpF = [];
    // const responseCodeF = [];
    // const delayStatusF = [];
    const departmentF = [];
    const res = async () => {
      try {
        const response = await axios.get(
          'http://15.165.25.145:9500/user/apidelay/status',
        );
        //console.log('response data Delay:', response.data.list);
        const list = response.data.list;
        for (let i = 0; i < list.length; i++) {
          tempData.push({
            key: list[i].api_delay_no,
            apiNo: list[i].api_no,
            service: list[i].service_name_kr,
            categoryNo: list[i].api_category_no,
            serviceNo: list[i].service_no,
            serviceName: list[i].service_url,
            apiName: list[i].url,
            http: list[i].method,
            errorCode: list[i].api_delay_code,
            errorMsg: list[i].api_delay_msg,
            delayTime: list[i].api_delay_time,
            delayStatus: list[i].api_delay_status,
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
          dListKey.push(list[i].api_delay_no);
          serviceF.push(list[i].service_name_kr); //필터메뉴
          httpF.push(list[i].method); //필터메뉴
          // responseCodeF.push(list[i].api_delay_code); //필터메뉴
          departmentF.push(list[i].service_group_name); //필터메뉴
        }
        setDelayData(tempData);
        setDListKey(dListKey);
        setDelayDataMenu({
          serviceF,
          httpF,
          // responseCodeF,
          departmentF,
        });
      } catch (error) {
        handelException(error, history);
      }
    };
    res();
  }, [history]);

  /** API 전체
   * 데이터에 따른 필터메뉴 수정 자동화
   *  (리팩토링 필요)
   */
  const menuConfig = menuData => {
    const menu =
      radio === 'apiList'
        ? dataMenu
        : radio === 'errorList'
        ? errorDataMenu
        : delayDataMenu;
    return containerFunc.onMenuConfig(menuData, menu);
  };
  /**
   * 컬럼 width 크기 조정
   */
  const columnSize = column => {
    if (
      // (radio === 'apiList' || radio === 'errorList') &&
      column === 'errorStatus'
    )
      return '100px';
    else if (
      // (radio === 'apiList' || radio === 'resDelayList') &&
      column === 'delayStatus'
    )
      return '100px';
    else if (column === 'apiName') return '180px';
    else if (column === 'http') return '110px';
    // else if (
    //   // (radio === 'errorList' || radio === 'resDelayList') &&
    //   column === 'errorMsg'
    // )
    //   return '250px';
    // else if (
    //   // (radio === 'errorList' || radio === 'resDelayList') &&
    //   column === 'errorCode'
    // )
    //   return '110px';
    // else if (
    //   // (radio === 'errorList' || radio === 'resDelayList') &&
    //   column === 'kibana' ||
    //   column === 'jira' ||
    //   column === 'ref'
    // )
    //   return '120px';
    // else if (/* radio === 'resDelayList' && */ column === 'delayTime')
    //   return '100px';
    // else if (column === 'service') return '150px';
    // else if (column === 'serviceName') return '290px';
    // else if (column === 'function') return '330px';
    // else if (column === 'department') return '130px';
    // else if (column === 'manager') return '120px';
    // else if (column === 'subManager') return '120px';
    // else if (column === 'insertDate') return '160px';
    // else if (column === 'updateDate') return '160px';
  };
  /**
   * 컬럼 Fixed
   */
  const columnFixed = column => {
    if (
      (radio === 'apiList' || radio === 'errorList') &&
      column === 'errorStatus'
    )
      return 'left';
    else if (
      (radio === 'apiList' || radio === 'resDelayList') &&
      column === 'delayStatus'
    )
      return 'left';
    else if (column === 'apiName') return 'left';
    else if (column === 'http') return 'left';
  };
  /**
   * 컬럼 설정 함수집합
   */
  const getColumnConfig = columnsKey => {
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
          radio === 'errorList' || radio === 'resDelayList'
            ? columnsKey[i].dataIndex === 'kibana'
              ? true
              : columnsKey[i].dataIndex === 'jira'
              ? true
              : false
            : false,
        dataIndex: columnsKey[i].dataIndex,
        filters: menuConfig(columnsKey[i].dataIndex),
        filteredValue: filteredInfo[columnsKey[i].dataIndex] || null,
        onFilter: (value, record) => {
          ////console.log('작동확인!!!!');
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
                ////console.log('api_category_no:', record.api_category_no);
                const apiNo = radio === 'apiList' ? record.key : record.apiNo;
                return (
                  <span>
                    <Link
                      to={`/api/detail/${apiNo}?service_no=${record.serviceNo}&api_category_no=${record.categoryNo}`}
                    >
                      {apiName}
                    </Link>
                  </span>
                );
              }
            : columnsKey[i].dataIndex === 'errorStatus'
            ? (errorStatus, record) => {
                // //console.log('에러스테이터스 :', record.apiName);
                const [status, text] =
                  errorStatus === 'T'
                    ? ['error', 'Error']
                    : ['success', 'Finished'];
                return radio === 'apiList' ? (
                  <span>
                    <Button
                      style={{ border: 'none', background: 'none' }}
                      onClick={() => apiToError(record.apiName, record.http)}
                    >
                      <Badge
                        status={status}
                        text={text}
                        style={{ color: 'blue' }}
                      />
                    </Button>
                  </span>
                ) : (
                  <span>
                    <Link to={`/error/${record.key}`}>
                      <Badge
                        status={status}
                        text={text}
                        style={{ color: 'blue' }}
                      />
                    </Link>
                  </span>
                );
              }
            : columnsKey[i].dataIndex === 'delayStatus'
            ? (errorStatus, record) => {
                const [status, text] =
                  errorStatus === 'T'
                    ? ['error', 'Error']
                    : ['success', 'Finished'];
                return radio === 'apiList' ? (
                  <span style={{ color: 'blue' }}>
                    <Button
                      style={{ border: 'none', background: 'none' }}
                      onClick={() => apiToDelay(record.apiName, record.http)}
                    >
                      <Badge
                        status={status}
                        text={text}
                        style={{ color: 'blue' }}
                      />
                    </Button>
                  </span>
                ) : (
                  <span>
                    <Link to={`/delay/${record.key}`}>
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
            : columnsKey[i].dataIndex === 'delayTime'
            ? delayTime => <span>{delayTime} s</span>
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
   * 컬럼 원본 키(API 전체)
   */
  const columnsKeyOrigin = [
    { title: '에러상태', dataIndex: 'errorStatus' }, //api, error
    { title: '지연상태', dataIndex: 'delayStatus' }, //api, delay
    { title: 'API URL', dataIndex: 'apiName' }, //api, error, delay
    { title: '메소드', dataIndex: 'http' }, //api, error, delay
    { title: '서비스', dataIndex: 'service' }, //api, error, delay
    { title: 'Service URL', dataIndex: 'serviceName' }, //api, error, delay
    { title: '기능설명', dataIndex: 'function' }, //api
    { title: '담당부서', dataIndex: 'department' }, //api, error, delay
    { title: '담당자(정)', dataIndex: 'manager' }, //api, error, delay
    { title: '담당자(부)', dataIndex: 'subManager' }, //api, error, delay
    { title: '등록날짜', dataIndex: 'insertDate' }, //api, error, delay
    { title: '수정날짜', dataIndex: 'updateDate' }, //api, error, delay
  ];
  /**
   * 컬럼 원본 키(에러)
   */
  const columnsKeyOriginErr = [
    { title: '상태', dataIndex: 'errorStatus' }, //api, error
    { title: 'API URL', dataIndex: 'apiName' }, //api, error, delay
    { title: '메소드', dataIndex: 'http' }, //api, error, delay
    { title: '서비스', dataIndex: 'service' }, //api, error, delay
    { title: 'Service URL', dataIndex: 'serviceName' }, //api, error, delay
    { title: '에러코드', dataIndex: 'errorCode' }, //error, delay
    { title: '에러내용', dataIndex: 'errorMsg' }, //error, delay
    { title: '담당부서', dataIndex: 'department' }, //api, error, delay
    { title: '담당자(정)', dataIndex: 'manager' }, //api, error, delay
    { title: '담당자(부)', dataIndex: 'subManager' }, //api, error, delay
    { title: '발생날짜', dataIndex: 'insertDate' }, //api, error, delay
    { title: '수정날짜', dataIndex: 'updateDate' }, //api, error, delay
    { title: 'KIBANA_URL', dataIndex: 'kibana' },
    { title: 'JIRA_URL', dataIndex: 'jira' },
    { title: '참고', dataIndex: 'ref' },
  ];
  /**
   * 컬럼 원본 키(응답지연)
   */
  const columnsKeyOriginDelay = [
    { title: '상태', dataIndex: 'delayStatus' }, //api, delay
    { title: 'API URL', dataIndex: 'apiName' }, //api, error, delay
    { title: '메소드', dataIndex: 'http' }, //api, error, delay
    { title: '서비스', dataIndex: 'service' }, //api, error, delay
    { title: 'Service URL', dataIndex: 'serviceName' }, //api, error, delay
    { title: '에러코드', dataIndex: 'errorCode' }, //error, delay
    { title: '에러내용', dataIndex: 'errorMsg' }, //error, delay
    { title: '응답시간', dataIndex: 'delayTime' }, //delay
    { title: '담당부서', dataIndex: 'department' }, //api, error, delay
    { title: '담당자(정)', dataIndex: 'manager' }, //api, error, delay
    { title: '담당자(부)', dataIndex: 'subManager' }, //api, error, delay
    { title: '발생날짜', dataIndex: 'insertDate' }, //api, error, delay
    { title: '수정날짜', dataIndex: 'updateDate' }, //api, error, delay
    { title: 'KIBANA_URL', dataIndex: 'kibana' },
    { title: 'JIRA_URL', dataIndex: 'jira' },
    { title: '참고', dataIndex: 'ref' },
  ];
  const [columnsKey, setColumnsKey] = useState(columnsKeyOrigin);
  const [columnsKeyErr, setColumnsKeyErr] = useState(columnsKeyOriginErr);
  const [columnsKeyDelay, setColumnsKeyDelay] = useState(columnsKeyOriginDelay);
  //컬럼
  const columns = getColumnConfig(
    radio === 'apiList'
      ? columnsKey
      : radio === 'errorList'
      ? columnsKeyErr
      : columnsKeyDelay,
  );
  // ////console.log('컬럼 키값 (state):', columnsKey);

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
        // //console.log('columnsConfig:', data.columns_config);
        if (data.columns_config !== null) {
          const { api, error, delay } = data.columns_config;
          if (api !== undefined) {
            setColumnsKey(api);
          }
          if (error !== undefined) {
            setColumnsKeyErr(error);
          }
          if (delay !== undefined) {
            setColumnsKeyDelay(delay);
          }
        }
      } catch (error) {
        // //console.log('유저 컬럼 가져오기', error);
        handelException(error, history);
      }
    };
    resColumns();
  }, [history, userId]);

  // 검색 및 날짜 필터 이벤트
  const onDateChange = date => {
    const tempForm = containerFunc.onFilter(date, form);
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
   * Table 변경시 발생 함수
   */
  const handleChange = (pagination, filters, sorter) => {
    // ////console.log('Various parameters', pagination, filters, sorter);
    const tempForm = containerFunc.onTableChange(pagination, filters, sorter);
    setForm(tempForm);
  };
  /**
   * 필터 초기화 이벤트
   */
  const clearAll = () => {
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
      const keyCopy =
        radio === 'apiList'
          ? containerFunc.onTableDragEnd(fromIndex, toIndex, columnsKey)
          : radio === 'errorList'
          ? containerFunc.onTableDragEnd(fromIndex, toIndex, columnsKeyErr)
          : containerFunc.onTableDragEnd(fromIndex, toIndex, columnsKeyDelay);

      radio === 'apiList'
        ? setColumnsKey(keyCopy)
        : radio === 'errorList'
        ? setColumnsKeyErr(keyCopy)
        : setColumnsKeyDelay(keyCopy);
    },
    [columnsKey, columnsKeyDelay, columnsKeyErr, radio],
  );

  /**
   * Columns show / hide 함수
   * 형식 - ['apiName','http'...]
   */
  const coulumnsDisplay = checkedValues => {
    // ////console.log('확인:', checkedValues);
    const tempArray =
      radio === 'apiList'
        ? containerFunc.onCoulumnsDisplay(checkedValues, columnsKeyOrigin)
        : radio === 'errorList'
        ? containerFunc.onCoulumnsDisplay(checkedValues, columnsKeyOriginErr)
        : containerFunc.onCoulumnsDisplay(checkedValues, columnsKeyOriginDelay);

    radio === 'apiList'
      ? setColumnsKey(tempArray)
      : radio === 'errorList'
      ? setColumnsKeyErr(tempArray)
      : setColumnsKeyDelay(tempArray);

    // ////console.log('tempArray: ', tempArray);
  };
  /**
   * 사용자 Columns 설정(추후 로그인 적용시 본인 employee no적용 )
   * /user/columnsconfig/${employeeNo}
   */
  const userConfig = async () => {
    let userColumns = {};
    if (radio === 'apiList') {
      userColumns = {
        api: columnsKey,
      };
    } else if (radio === 'errorList') {
      userColumns = {
        error: columnsKeyErr,
      };
    } else if (radio === 'resDelayList') {
      userColumns = {
        delay: columnsKeyDelay,
      };
    }
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

  /**
   * API에서 에러상태 눌렀을 경우 해당API 에러
   */
  const apiToError = (apiName, http) => {
    setRadio('errorList');
    setForm({
      ...form,
      filteredInfo: {
        ...form.filteredInfo,
        apiName: [apiName],
        http: [http],
      },
    });
    window.scrollTo(0, 0);
  };
  /**
   * API에서 지연상태 눌렀을 경우 해당API 응답지연
   */
  const apiToDelay = (apiName, http) => {
    setRadio('resDelayList');
    setForm({
      ...form,
      filteredInfo: {
        ...form.filteredInfo,
        apiName: [apiName],
        http: [http],
      },
    });
    window.scrollTo(0, 0);
  };

  return !loading ? (
    <ApiList
      onDateChange={onDateChange}
      clearAll={clearAll}
      columns={columns}
      //sorter 메뉴
      columnsKey={
        radio === 'apiList'
          ? columnsKey
          : radio === 'errorList'
          ? columnsKeyErr
          : columnsKeyDelay
      }
      columnsKeyOrigin={
        radio === 'apiList'
          ? columnsKeyOrigin
          : radio === 'errorList'
          ? columnsKeyOriginErr
          : columnsKeyOriginDelay
      }
      data={
        radio === 'apiList'
          ? apiAllData
          : radio === 'errorList'
          ? errorData
          : delayData
      }
      onRadioChange={onRadioChange}
      handleChange={handleChange}
      rowSelection={rowSelection} //row 선택 함수
      selectedRowKeys={selectedRowKeys} //선택 row 키 배열 [key넘버...]
      selectDatumFunc={selectDatumFunc} //선택 row 데이터 생성 함수
      selectDatum={selectDatum} //선택 row 데이터 집합
      onDragEnd={onDragEnd} //컬럼 Drag 함수
      coulumnsDisplay={coulumnsDisplay} //컬럼 show/hide 함수
      userConfig={userConfig} //사용자 Columns 설정
      onSorter={onSorter} //정렬 함수
      radio={radio}
    />
  ) : (
    <span style={{ color: 'blue', fontSize: '100px' }}>
      <Icon type="loading" />
    </span>
  );
};

export default withRouter(ApiListContainer);
