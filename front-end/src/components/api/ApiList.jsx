import React from 'react';
import 'antd/dist/antd.css';
import 'lib/css/api/table-button.css';
import 'lib/css/admin/apiConfig.css';
import { Table, Button, Radio, Modal, Icon, Checkbox } from 'antd';
import RangeDate from 'components/common/RangeDate';
import ReactDragListView from 'react-drag-listview';
import { CSVLink } from 'react-csv';

const { DragColumn } = ReactDragListView;
const { confirm } = Modal;

const ApiList = ({
  onDateChange,
  clearAll,
  columns,
  columnsKeyOrigin,
  columnsKey,
  data,
  handleChange,
  onRadioChange,
  rowSelection,
  selectedRowKeys,
  selectDatumFunc,
  selectDatum,
  onDragEnd,
  coulumnsDisplay,
  userConfig,
  onSorter,
  radio,
}) => {
  /**
   * Export to CSV Button onClick 이벤트
   */
  const csvButton = (e, fileType) => {
    //console.log('fileType: ', fileType);
    //row 선택 없을 경우 경고창
    if (!selectedRowKeys.length) {
      e.preventDefault();
      Modal.error({
        title: '선택한 데이터가 없습니다!',
        content: '데이터를 1개 이상 선택해주십시오.',
      });
      return;
    }
    selectDatumFunc();
  };

  /**
   * Display Menu에서 체크박스 onClick 이벤트
   */
  const onChange = checkedValues => {
    console.log('checked = ', checkedValues);
    coulumnsDisplay(checkedValues);
  };

  /**
   * 컬럼 리스트 출력
   */
  const options = () =>
    columnsKeyOrigin.map((item, index) => ({
      label: item.title,
      value: item.dataIndex,
      disabled: item.dataIndex === 'apiName' ? true : false,
    }));

  /**
   * 초기값 선택
   */
  const defaultCheck = () => columns.map((item, index) => item.key);

  /**
   * Reset 버튼 이벤트
   */
  const reset = () => {

        const columnsReset = columnsKeyOrigin.map(item => item.dataIndex);
        onChange(columnsReset);

  };

  /**
   * Save 버튼 이벤트
   */
  const showConfirm = () => {
    confirm({
      title: '설정한 컬럼을 저장하시겠습니까?',
      // content: 'Some descriptions',
      onOk() {
        userConfig();
      },
      onCancel() {
        // //console.log('Cancel');
      },
    });
  };

  /**
   * Tooltip title
   */
  const displayMenu = () => (
    <div>
      <div style={{ color: 'white', marginBottom: '8%' }}>
        <Checkbox.Group
          options={options()}
          defaultValue={defaultCheck()}
          value={defaultCheck()}
          onChange={onChange}
        />
      </div>
      <Button onClick={reset}>초기화</Button>
      <Button type="primary" onClick={showConfirm}>
        저장
      </Button>
    </div>
  );
  /**
   * CSV 헤더
   */
  const csvHeaders = columnsKey.map(item => item.dataIndex);
  return (
    <div className="table-operations">
      <p>API 실시간 현황</p>

      <div style={{ textAlign: 'left', marginBottom: '10px' }}>
        <RangeDate
          onDateChange={onDateChange}
          clearAll={clearAll}
          displayMenu={displayMenu}
          onSorter={onSorter}
          columnsKey={columnsKey}
          data={data}
          radio={radio}
        />
      </div>
      <div style={{ textAlign: 'left' }}>
        <Radio.Group
          onChange={onRadioChange}
          defaultValue="apiList"
          value={radio}
          buttonStyle="solid"
        >
          <Radio.Button value="apiList">전체현황</Radio.Button>
          <Radio.Button value="errorList">에러현황</Radio.Button>
          <Radio.Button value="resDelayList">지연현황</Radio.Button>
        </Radio.Group>
        {/* <div style={{ textAlign: 'left', float: 'right' }}> */}
        <Button
          style={{ backgroundColor: 'green', color: 'white', float: 'right' }}
          onClick={e => {
            csvButton(e);
          }}
        >
          <CSVLink data={selectDatum} headers={csvHeaders}>
            <Icon
              type="file-excel"
              theme="twoTone"
              twoToneColor="#5FB404"
              style={{ fontSize: '20px' }}
            />
            엑셀 내보내기
          </CSVLink>
        </Button>
      </div>
      <div id="api-list-table">
        <DragColumn onDragEnd={onDragEnd} nodeSelector="th">
          <Table
            rowSelection={rowSelection}
            columns={columns}
            dataSource={data}
            onChange={handleChange}
            bordered
            // size="middle"
            scroll={
              radio === 'apiList'
                ? { x: 2000 }
                : radio === 'errorList'
                ? { x: 2000 }
                : { x: 2100 }
            }
          />
        </DragColumn>
      </div>
    </div>
  );
};

export default ApiList;
