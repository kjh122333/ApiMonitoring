import React, { useState, useEffect, useCallback } from 'react';
import { Modal, Table, Icon, Input, Button, Row } from 'antd';
import { formDataNameSet as nameSet } from 'containers/api/ApiWriteContainer';
import axios from 'axios';

const SearchEmployee = ({
  toggleModal,
  visible,
  handleSetFormData,
  formData,
  update,
}) => {
  /**
   * 테이블 필터 state
   */
  const [form, setForm] = useState({
    filteredInfo: null,
  });

  let { filteredInfo } = form;
  filteredInfo = filteredInfo || {};
  const handleChange = (pagination, filters, sorter) => {
    //console.log('Various parameters', pagination, filters, sorter);
    setForm({
      filteredInfo: filters,
    });
  };

  /**
   * 컬럼
   */
  const columns = [
    {
      title: '선택',
      dataIndex: 'select',
      key: 'select',
    },
    {
      title: '직원번호',
      dataIndex: 'employee_no',
      key: 'employeeNum',
    },
    {
      title: '이름',
      dataIndex: 'name',
      key: 'employeeName',
      filteredValue: filteredInfo.employeeName || null,
      onFilter: (value, record) => record.name.includes(value),
    },
    {
      title: '부서명',
      dataIndex: 'group_name',
      key: 'employeeGroupNmae',
    },
  ];

  /**
   * 담당자 검색 Input onChange
   */
  const [employee, setEmployee] = useState('');
  const handleInput = e => {
    setEmployee(e.target.value);
  };

  /**
   * 담당자 검색 Form Submit
   */
  const filterName = () => {
    setForm({
      filteredInfo: {
        employeeName: [employee],
      },
    });
  };
  const keyPressed = event => {
    if (event.key === 'Enter') {
      filterName();
    }
  };

  /**
   * 담당자 데이터
   */
  const [dataSource, setDataSource] = useState([]);
  //console.log('dataSource', dataSource);

  const clickIcon = useCallback(
    (employee, sub = false) => {
      //console.log(employee);
      if (update) {
        visible.isSub
          ? handleSetFormData(prevForm => ({
              ...prevForm,
              employee_sub_no: employee.employee_no,
              employee_sub_name: employee.name,
              employee_sub_group_name: employee.group_name,
            }))
          : handleSetFormData(prevForm => ({
              ...prevForm,
              employee_no: employee.employee_no,
              employee_name: employee.name,
              employee_group_name: employee.group_name,
            }));
      } else {
        visible.isSub
          ? handleSetFormData({
              target: { name: nameSet.employeeSub, value: employee },
            })
          : handleSetFormData({
              target: { name: nameSet.employee, value: employee },
            });
      }

      toggleModal();
    },
    [handleSetFormData, toggleModal, update, visible.isSub],
  );

  useEffect(() => {
    const getEmployeeList = async () => {
      const res = await axios.get(
        'http://15.165.25.145:9500/user/employee/data',
      );
      const { list } = res.data;
      //console.log(list);
      const employeeList = list.map((employee, index) => ({
        ...employee,
        key: index,
        select: (
          <Icon
            type="check-circle"
            theme="filled"
            onClick={() => {
              clickIcon(employee);
            }}
          />
        ),
      }));
      setDataSource(employeeList);
    };
    getEmployeeList();
  }, [clickIcon, toggleModal]);

  return (
    <Modal
      title="직원 검색"
      visible={visible.display}
      onOk={() => {}}
      onCancel={toggleModal}
    >
      <Row style={{ display: 'flex' }}>
        <label>
          담당자:&nbsp;&nbsp;
          <Input
            placeholder="이름"
            name="employeeName"
            value={employee}
            onChange={handleInput}
            onKeyPress={keyPressed}
            required
            style={{ width: '200px' }}
          />
        </label>
        <Button type="primary" onClick={filterName}>
          검색
        </Button>
      </Row>
      <Row>
        <Table
          dataSource={dataSource}
          columns={columns}
          size="small"
          onChange={handleChange}
        />
      </Row>
    </Modal>
  );
};

export default SearchEmployee;
