import React from 'react';
import 'antd/dist/antd.css';
import 'lib/css/api/table-button.css';
import { Table } from 'antd';
import AdminRangeDate from './AdminRangeDate';

const MemberConfig = ({
  onDateChange,
  onSorter,
  clearAll,
  columns,
  columnsKey,
  data,
  handleChange,
  rowSelection,
  selectedRowKeys,
  modalVisible,
  setModalVisible,
  setUserDelete,
  setRowSelect
}) => {
  return (
    <div className="table-operations">
      <p>회원 관리</p>

      <div style={{ textAlign: 'left', marginBottom: '10px' }}>
        <AdminRangeDate
          onDateChange={onDateChange}
          clearAll={clearAll}
          onSorter={onSorter}
          columnsKey={columnsKey}
          modalVisible={modalVisible}
          setModalVisible={setModalVisible}
          selectedRowKeys={selectedRowKeys}
          setUserDelete={setUserDelete}
          setRowSelect={setRowSelect}
        />
      </div>

      <div>
        <Table
          rowSelection={rowSelection}
          columns={columns}
          dataSource={data}
          onChange={handleChange}
          scroll={{ x: 600, y: 500 }}
        />
      </div>
    </div>
  );
};

export default MemberConfig;
