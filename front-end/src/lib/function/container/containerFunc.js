import moment from 'moment';
import _ from 'lodash';

/**
 * 선택 record 데이터 설정
 */
export const onSelectDatumFunc = (
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
) => {
  const tempDatum = [];
  const userCol = () => {
    return radio === 'apiList'
      ? columnsKey.map(item => item.dataIndex)
      : radio === 'errorList'
      ? columnsKeyErr.map(item => item.dataIndex)
      : radio === 'resDelayList'
      ? columnsKeyDelay.map(item => item.dataIndex)
      : null;
  };
  const originCol = () => {
    return radio === 'apiList'
      ? columnsKeyOrigin.map(item => item.dataIndex)
      : radio === 'errorList'
      ? columnsKeyOriginErr.map(item => item.dataIndex)
      : radio === 'resDelayList'
      ? columnsKeyOriginDelay.map(item => item.dataIndex)
      : null;
  };
  const deleteKey = originCol().filter(item => !userCol().includes(item));

  for (let i = 0; i < selectedRowKeys.length; i++) {
    if (radio === 'apiList') {
      apiAllData.filter(item =>
        item.key === selectedRowKeys[i] ? tempDatum.push(item) : null,
      );
    } else if (radio === 'errorList') {
      errorData.filter(item =>
        item.key === selectedRowKeys[i] ? tempDatum.push(item) : null,
      );
    } else if (radio === 'resDelayList') {
      delayData.filter(item =>
        item.key === selectedRowKeys[i] ? tempDatum.push(item) : null,
      );
    }
  }
  const copyDatum = _.cloneDeep(tempDatum);
  copyDatum.map(item => {
    deleteKey.map(element => delete item[element]);
    delete item.key;
    delete item.serviceNo;
    delete item.categoryNo;
  });
  return copyDatum;
};

/**
 * 데이터에 따른 필터메뉴 수정 자동화
 *
 */
export const onMenuConfig = (menuData, dataMenu) => {
  if (dataMenu.serviceF.length > 0 && menuData === 'service') {
    const menu = dataMenu.serviceF.filter(
      (item, index) => dataMenu.serviceF.indexOf(item) === index,
    );
    return menu.map(item => ({ text: item, value: item }));
  } else if (dataMenu.httpF.length > 0 && menuData === 'http') {
    const menu = dataMenu.httpF.filter(
      (item, index) => dataMenu.httpF.indexOf(item) === index,
    );
    return menu.map(item => ({ text: item, value: item }));
  } else if (menuData === 'errorStatus' || menuData === 'delayStatus') {
    const menu = [
      { text: 'Error', value: 'T' },
      { text: 'Finished', value: 'F' },
    ];
    return menu;
  } else if (dataMenu.departmentF.length > 0 && menuData === 'department') {
    const menu = dataMenu.departmentF.filter(
      (item, index) => dataMenu.departmentF.indexOf(item) === index,
    );
    return menu.map(item => ({ text: item, value: item }));
  }
};

/**
 * 데이터에 따른 필터메뉴 수정 자동화
 * Admin - 회원관리
 */
export const onMenuConfigAdmin = (menuData, dataMenu) => {
  if (dataMenu.idF.length > 0 && menuData === 'id') {
    const menu = dataMenu.idF.filter(
      (item, index) => dataMenu.idF.indexOf(item) === index,
    );
    return menu.map(item => ({ text: item, value: item }));
  } else if (dataMenu.userNameF.length > 0 && menuData === 'userName') {
    const menu = dataMenu.userNameF.filter(
      (item, index) => dataMenu.userNameF.indexOf(item) === index,
    );
    return menu.map(item => ({ text: item, value: item }));
  } else if (dataMenu.userPhoneF.length > 0 && menuData === 'userPhone') {
    const menu = dataMenu.userPhoneF.filter(
      (item, index) => dataMenu.userPhoneF.indexOf(item) === index,
    );
    return menu.map(item => ({ text: item, value: item }));
  } else if (dataMenu.userEmailF.length > 0 && menuData === 'userEmail') {
    const menu = dataMenu.userEmailF.filter(
      (item, index) => dataMenu.userEmailF.indexOf(item) === index,
    );
    return menu.map(item => ({ text: item, value: item }));
  }
  else if (dataMenu.departmentF.length > 0 && menuData === 'department') {
    const menu = dataMenu.departmentF.filter(
      (item, index) => dataMenu.departmentF.indexOf(item) === index,
    );
    return menu.map(item => ({ text: item, value: item }));
  }
};

// 검색 및 날짜 필터 이벤트
export const onFilter = (date, form) => {
  //find key
  const columnKey = Object.keys(date).filter(item =>
    item === 'startDate' ? false : item === 'finishDate' ? false : true,
  );
  const keyData = columnKey.map(item => ({ [item]: [date[item]] }));
  let collect = {};
  for (let i = 0; i < keyData.length; i++) {
    Object.assign(collect, keyData[i]);
  }
  const { startDate, finishDate } = date;
  const dateArr = [];
  if (startDate !== '' && finishDate !== '') {
    const tempDate = finishDate.clone();
    while (
      !moment(startDate)
        .subtract(1, 'd')
        .isSame(tempDate, 'day')
    ) {
      dateArr.push(tempDate.format('YYYY-MM-DD'));
      tempDate.subtract(1, 'd');
    }
  }
  return {
    ...form,
    filteredInfo: {
      ...form.filteredInfo,
      insertDate: dateArr === [] ? null : dateArr,
      ...collect,
    },
  };
};
// 검색 및 날짜 필터 이벤트(Admin - 회원관리)
export const onFilterAdmin = (date, form) => {
  //find key
  const columnKey = Object.keys(date).filter(item =>
    item === 'startDate' ? false : item === 'finishDate' ? false : true,
  );
  const keyData = columnKey.map(item => ({ [item]: [date[item]] }));
  let collect = {};
  for (let i = 0; i < keyData.length; i++) {
    Object.assign(collect, keyData[i]);
  }
  //기간 검색
  const { startDate, finishDate } = date;
  const dateArr = [];
  if (startDate !== '' && finishDate !== '') {
    const tempDate = finishDate.clone();
    while (
      !moment(startDate)
        .subtract(1, 'd')
        .isSame(tempDate, 'day')
    ) {
      dateArr.push(tempDate.format('YYYY-MM-DD'));
      tempDate.subtract(1, 'd');
    }
  }

  return {
    ...form,
    filteredInfo: {
      ...form.filteredInfo,
      insertDate: dateArr === [] ? null : dateArr,
      ...collect,
    },
  };
};

/**
 * 테이블 변경시 실행
 */
export const onTableChange = (pagination, filters, sorter) => {
  //console.log('Various parameters', pagination, filters, sorter);
  return {
    filteredInfo: filters,
    sortedInfo: sorter,
  };
};

/**
 * 필터 초기화 이벤트
 */
export const filterClearAll = () => {
  return {
    filteredInfo: null,
    sortedInfo: null,
  };
};

/**
 * Columns DnD
 */
export const onTableDragEnd = (fromIndex, toIndex, columnsKey) => {
  //console.log('fromIndex:', fromIndex);
  //console.log('toIndex:', toIndex);
  fromIndex--;
  toIndex--;
  const keyCopy = columnsKey.slice();
  const item = keyCopy.splice(fromIndex, 1)[0];
  keyCopy.splice(toIndex, 0, item);
  return keyCopy;
};

/**
 * Columns show / hide 함수
 * 형식 - ['apiName','http'...]
 */
export const onCoulumnsDisplay = (checkedValues, columnsKeyOrigin) => {
  //console.log('확인:', checkedValues);
  const tempArray = [];
  columnsKeyOrigin.forEach(element => {
    checkedValues.forEach(item => {
      if (element.dataIndex === item) tempArray.push(element);
    });
  });
  return tempArray;
};
