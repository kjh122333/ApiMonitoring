import React, { useState, useCallback, useEffect } from 'react';
import 'antd/dist/antd.css';
import { Link } from 'react-router-dom';
import { Badge, Modal, Icon } from 'antd';
import ResDelayList from 'components/resDelay/ResDelayList';
import axios from 'axios';
import { renderMethodBage } from 'components/api/apiWrite/WriteStep';
import * as containerFunc from 'lib/function/container/containerFunc';
import _ from 'lodash';
import handelException from 'lib/function/request/handleException';
import { withRouter } from 'react-router-dom';
const ResDelayListContainer = ({ history }) => {
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
   * 응답지연 데이터 요청
   */
  const [delayData, setDelayData] = useState();
  const [listKey, setListKey] = useState([]);
  const [dataMenu, setDataMenu] = useState({
    serviceF: [],
    httpF: [],
    responseCodeF: [],
    departmentF: [],
  });
  useEffect(() => {
    const tempData = [];
    const listKey = []; //모든 Row 선택시 활용
    const serviceF = [];
    const httpF = [];
    // const responseCodeF = [];
    // const delayStatusF = [];
    const departmentF = [];
    const res = async () => {
      setLoading(true);
      try {
        const response = await axios.get(
          'http://15.165.25.145:9500/user/apidelay',
        );
        //console.log('response data:', response.data.list);
        const list = response.data.list;
        for (let i = 0; i < list.length; i++) {
          tempData.push({
            key: list[i].api_delay_no,
            service: list[i].service_name_kr,
            apiNo: list[i].api_no,
            categoryNo: list[i].api_category_no,
            serviceNo: list[i].service_no,
            serviceName: list[i].service_url,
            apiName: list[i].url,
            http: list[i].method,
            responseCode: list[i].api_delay_code,
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
          listKey.push(list[i].api_delay_no);
          serviceF.push(list[i].service_name_kr); //필터메뉴
          httpF.push(list[i].method); //필터메뉴
          // responseCodeF.push(list[i].api_delay_code); //필터메뉴
          departmentF.push(list[i].service_group_name); //필터메뉴
        }
        setDelayData(tempData);
        setListKey(listKey);
        setDataMenu({
          serviceF,
          httpF,
          // responseCodeF,
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
   *
   */
  const menuConfig = menuData => {
    return containerFunc.onMenuConfig(menuData, dataMenu);
  };
  /**
   * 컬럼 width 크기 조정
   */
  const columnSize = column => {
    if (column === 'delayStatus') return '100px';
    else if (column === 'apiName') return '180px';
    else if (column === 'http') return '110px';
    // if (column === 'errorMsg') return '250px';
    // if (column === 'responseCode') return '110px';
    // if (column === 'delayTime') return '100px';
    // else if (column === 'service') return '150px';
    // else if (column === 'serviceName') return '290px';
    // else if (column === 'department') return '130px';
    // else if (column === 'manager') return '120px';
    // else if (column === 'subManager') return '120px';
    // else if (column === 'insertDate') return '160px';
    // else if (column === 'updateDate') return '160px';
    // else if (column === 'kibana' || column === 'jira' || column === 'ref')
    //   return '120px';
  };
  /**
   * 컬럼 Fixed
   */
  const columnFixed = column => {
    if (column === 'delayStatus') return 'left';
    else if (column === 'apiName') return 'left';
    else if (column === 'http') return 'left';
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
          //console.log('작동확인!!!!');
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
            : columnsKey[i].dataIndex === 'delayStatus'
            ? (errorStatus, { key }) => {
                const [status, text] =
                  errorStatus === 'T'
                    ? ['error', 'Error']
                    : ['success', 'Finished'];
                return (
                  <span>
                    <Link to={`/delay/${key}`}>
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
   * 컬럼 원본 키
   */
  const columnsKeyOrigin = [
    { title: '상태', dataIndex: 'delayStatus' },
    { title: 'API URL', dataIndex: 'apiName' },
    { title: '메소드', dataIndex: 'http' },
    { title: '서비스', dataIndex: 'service' },
    { title: 'Service URL', dataIndex: 'serviceName' },
    { title: '에러코드', dataIndex: 'responseCode' },
    { title: '에러내용', dataIndex: 'errorMsg' },
    { title: '응답시간', dataIndex: 'delayTime' },
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
      delayData.filter(item =>
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
  }, [columnsKey, columnsKeyOrigin, delayData, selectedRowKeys]);

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
          const { delayLog } = data.columns_config;
          if (delayLog !== undefined) setColumnsKey(delayLog);
        }
      } catch (error) {
        handelException(error, history);
      }
    };
    resColumns();
  }, [history, userId]);

  // 검색 및 날짜 필터 이벤트
  const onDateChange = date => {
    //console.log('필터 확인 :',date)
    const tempForm = containerFunc.onFilter(date, form);
    setForm(tempForm);
  };
  //console.log('form :::', form);

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
  const coulumnsDisplay = checkedValues => {
    const tempArray = containerFunc.onCoulumnsDisplay(
      checkedValues,
      columnsKeyOrigin,
    );
    setColumnsKey(tempArray);
  };
  /**
   * 사용자 Columns 설정(추후 로그인 적용시 본인 employee no적용 )
   * /user/columnsconfig/${employeeNo}
   */
  const userConfig = async () => {
    const userColumns = {
      delayLog: columnsKey,
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
    <ResDelayList
      onDateChange={onDateChange}
      clearAll={clearAll}
      columnsKeyOrigin={columnsKeyOrigin} //Display 컬럼 고정(원본 컬럼 키)
      columnsKey={columnsKey} //sorter 메뉴
      columns={columns}
      data={delayData}
      handleChange={handleChange}
      rowSelection={rowSelection} //row 선택 함수
      selectedRowKeys={selectedRowKeys} //선택 row 키 배열 [key넘버...]
      selectDatumFunc={selectDatumFunc} //선택 row 데이터 생성 함수
      selectDatum={selectDatum} //선택 row 데이터 집합
      onDragEnd={onDragEnd} //컬럼 Drag 함수
      coulumnsDisplay={coulumnsDisplay} //컬럼 show/hide 함수
      userConfig={userConfig} //사용자 Columns 설정
      onSorter={onSorter} //정렬 함수
    />
  ) : (
    <span style={{ color: 'blue', fontSize: '100px' }}>
      <Icon type="loading" />
    </span>
  );
};

export default withRouter(ResDelayListContainer);
