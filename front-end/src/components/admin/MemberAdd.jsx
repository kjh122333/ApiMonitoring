import React, { useState, useEffect } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/member/tableRow.css';
import {
  Table,
  Button,
  Popconfirm,
  Row,
  Col,
  Icon,
  Upload,
  message,
  Modal,
  Tooltip,
  Select,
} from 'antd';
import axios from 'axios';
import { ExcelRenderer } from 'react-excel-renderer';
import { EditableFormRow, EditableCell } from './editable';
import handelException from 'lib/function/request/handleException';
import { withRouter } from 'react-router-dom';
import ReactExport from 'react-export-excel';

const ExcelFile = ReactExport.ExcelFile;
const ExcelSheet = ReactExport.ExcelFile.ExcelSheet;
const ExcelColumn = ReactExport.ExcelFile.ExcelColumn;

const { Option } = Select;
const regId = /^[a-zA-Z][a-zA-Z0-9_.-]{3,13}[a-zA-Z0-9]$/; //아이디
const regKor = /^[가-힣]+$/; // 한글
const regEmail = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i; //이메일 형식
const regPhone = /^\d{3}-\d{3,4}-\d{4}$/; //연락처

const helpId = (
  <span>
    다음과 같은 아이디는 회원가입 및 사용이 불가합니다.
    <br />
    <br />
    1. 첫 문자가 숫자로 시작되는 아이디 <br />
    2. 한글 아이디 <br />
    3. 하이픈(-), 언더바(_), 점(.)을 제외한 특수문자가 포함되어 있는 아이디{' '}
    <br />
  </span>
);
const helpName = '한글만 가능';
const helpEmail = 'ex) gildong@gmail.com';
const helpPhone = 'ex) 010-6666-4236';

/**
 * 회원가입 샘플 양식 데이터
 */
const sampleData = [
  {
    name: '홍길동',
    id: 'sample_id',
    phone: '010-7777-7777',
    email: 'hong@gmail.com',
    department: 'DBP',
  },
  {
    name: '구길동',
    id: 'sample_id2',
    phone: '010-1234-5678',
    email: 'gu@naver.com',
    department: 'ERP',
  },
];
const MemberAdd = ({ modalVisible, modalCancel, history }) => {
  const [state, setState] = useState({
    cols: [],
    rows: [],
  });
  /**
   * 담당부서 데이터
   */
  const [department, setDepartment] = useState([]);
  useEffect(() => {
    const resColumns = async () => {
      try {
        const response = await axios.get(
          `http://15.165.25.145:9500/user/groupname`,
        );
        const { list } = response.data;
        //console.log('데이터:', list);
        setDepartment(list);
      } catch (error) {
        handelException(error, history);
      }
    };
    resColumns();
  }, [history]);

  const optionDepartment = () =>
    department.map((item, key) => (
      <Option key={key} value={item.group_name}>
        {item.group_name}
      </Option>
    ));
  const onChangeDepart = (key, value) => {
    setState({
      ...state,
      rows: state.rows.map(item => {
        if (item.key === key) {
          return { ...item, group_no: value };
        }
        return item;
      }),
    });
    setExistId({
      ...existId,
      check: true,
    });
  };

  const columnsOrigin = [
    {
      title: (
        <span style={{ fontWeight: 'bold' }}>
          <span style={{ color: 'red' }}>*&nbsp;</span>아이디 &nbsp;
          <Tooltip placement="topRight" title={helpId}>
            <Icon type="question-circle" />
          </Tooltip>
        </span>
      ),
      dataIndex: 'id',
      editable: true,
    },
    {
      title: (
        <span style={{ fontWeight: 'bold' }}>
          <span style={{ color: 'red' }}>*&nbsp;</span>이름&nbsp;
          <Tooltip placement="topRight" title={helpName}>
            <Icon type="question-circle" />
          </Tooltip>
        </span>
      ),
      dataIndex: 'name',
      editable: true,
    },
    {
      title: (
        <span style={{ fontWeight: 'bold' }}>
          <span style={{ color: 'red' }}>*&nbsp;</span>연락처&nbsp;
          <Tooltip placement="topRight" title={helpPhone}>
            <Icon type="question-circle" />
          </Tooltip>
        </span>
      ),
      dataIndex: 'employee_contact',
      editable: true,
    },
    {
      title: (
        <span style={{ fontWeight: 'bold' }}>
          <span style={{ color: 'red' }}>*&nbsp;</span>이메일&nbsp;
          <Tooltip placement="topRight" title={helpEmail}>
            <Icon type="question-circle" />
          </Tooltip>
        </span>
      ),
      dataIndex: 'mail',
      editable: true,
    },
    {
      title: <span style={{ fontWeight: 'bold' }}>부서</span>,
      dataIndex: 'group_no',
      render: (text, record) => {
        // return <span>{text}</span>;
        //console.log('record :::', record);
        return (
          <Select
            showSearch
            style={{ width: 150 }}
            value={text}
            placeholder="Department"
            optionFilterProp="children"
            onChange={value => onChangeDepart(record.key, value)}
            filterOption={(input, option) =>
              option.props.children
                .toLowerCase()
                .indexOf(input.toLowerCase()) >= 0
            }
          >
            {optionDepartment()}
          </Select>
        );
      },
      // editable: true,
    },
    {
      title: 'Action',
      dataIndex: 'action',
      render: (text, record) => {
        //console.log('record:', record);
        return (
          <Popconfirm
            title="삭제하시겠습니까?"
            onConfirm={() => handleDelete(record.key)}
          >
            <Icon
              type="delete"
              theme="filled"
              style={{ color: 'red', fontSize: '20px' }}
            />
          </Popconfirm>
        );
      },
    },
  ];
  /**
   *  Row 편집시 저장
   */
  const handleSave = row => {
    const newData = [...state.rows];
    const index = newData.findIndex(item => row.key === item.key);
    const item = newData[index];
    newData.splice(index, 1, {
      ...item,
      ...row,
    });
    setState({ ...state, rows: newData });
    setExistId({
      ...existId,
      check: true,
    });
  };

  /**
   * 업로드 전 검사
   */
  const fileHandler = fileList => {
    //console.log('fileList', fileList);
    let fileObj = fileList;
    if (!fileObj) {
      message.error('업로드 할 파일이 없습니다!');
      return false;
    }
    //console.log('fileObj.type:', fileObj.type);
    if (
      !(
        fileObj.type ===
        'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'
      )
    ) {
      message.error(
        <span style={{ color: 'red' }}>.xlsx 확장자만 업로드 가능합니다!</span>,
      );

      return false;
    }
    //just pass the fileObj as parameter
    ExcelRenderer(fileObj, (err, resp) => {
      if (err) {
        //console.log(err);
      } else {
        const key = resp.rows.slice(0, 1)[0];
        //console.log('key : ', resp.rows);
        const idKey = key.findIndex(item => item === '아이디' || item === 'id');
        const nameKey = key.findIndex(
          item => item === '이름' || item === 'name',
        );
        const employeeContactKey = key.findIndex(
          item => item === '연락처' || item === 'employee_contact',
        );
        const mailKey = key.findIndex(
          item => item === '이메일' || item === 'mail',
        );
        const groupNoKey = key.findIndex(
          item => item === '부서' || item === 'group_no',
        );
        if (idKey < 0 || nameKey < 0 || employeeContactKey < 0 || mailKey < 0) {
          //에러 메세지 띄우기
          message.error(
            <span style={{ color: 'red' }}>
              필수 항목이 없습니다! <br /> (*아이디, *이름, *연락처, *이메일)
            </span>,
          );
          return false;
        }
        const newRows = [];
        resp.rows.slice(1).map((row, index) => {
          if (row && row !== 'undefined') {
            newRows.push({
              key: index,
              id: row[idKey], //컬럼 순서임
              name: row[nameKey],
              employee_contact: row[employeeContactKey],
              mail: row[mailKey],
              group_no: groupNoKey < 0 ? 0 : row[groupNoKey],
              color: 'red',
            });
          }
        });

        if (newRows.length === 0) {
          message.error('No data found in file!');
          return false;
        } else {
          setState({
            ...state,
            cols: resp.cols,
            rows: newRows,
            errorMessage: null,
          });
        }
      }
    });
    return false;
  };

  /**
   * 데이터 서버로 전송
   */
  const handleSubmit = () => {
    const request = state.rows.map(item => ({
      ...item,
      password: item.id + 'q1!',
      group_no: changeDepartment(item.group_no),
    }));
    //console.log('전송 request :', request);
    Modal.confirm({
      title: '회원등록을 하시겠습니까?',
      async onOk() {
        //submit to API
        try {
          await axios.post(
            'http://15.165.25.145:9500/admin/signup/import',
            request,
          );
          Modal.success({
            title: '회원등록을 완료했습니다.',
          });
          setState({ ...state, rows: [] });
        } catch (error) {
          handelException(error, history);
          // Modal.error({
          //   title: '데이터 전송에 실패했습니다.',
          // });
        }
      },
    });
  };

  /**
   * 부서 이름 -> 숫자 변경
   */
  const changeDepartment = group => {
    let groupNumber = '';
    department.map(item => {
      if (item.group_name === group) {
        groupNumber = item.group_no;
      }
    });
    //console.log('그룹 넘버 :', groupNumber);
    return groupNumber;
  };
  // const changeDepartment = department => {
  //   if (department === 'ERP') return 1;
  //   else if (department === 'UI/UX') return 2;
  //   else if (department === 'DBP') return 3;
  //   else if (department === 'Wehago') return 4;
  //   else if (department === 'Dev/Ops') return 5;
  //   else return 0;
  // };

  /**
   * 행 추가 및 삭제
   */
  const handleDelete = key => {
    const rows = [...state.rows];
    setState({ ...state, rows: rows.filter(item => item.key !== key) });
  };
  const handleAdd = () => {
    const { /* count, */ rows } = state;
    //console.log('row length:', rows.length);
    const newData = {
      key: rows.length,
      id: 'ID',
      name: 'name',
      employee_contact: '010-1234-5678',
      mail: 'email@bit.com',
      group_no: 'DBP',
    };
    setState({
      ...state,
      rows: [newData, ...rows],
    });
    setExistId({
      ...existId,
      check: true,
    });
  };

  const components = {
    body: {
      row: EditableFormRow,
      cell: EditableCell,
    },
  };
  const columns = columnsOrigin.map(col => {
    if (!col.editable) {
      return col;
    }
    return {
      ...col,
      onCell: record => ({
        record,
        editable: col.editable,
        dataIndex: col.dataIndex,
        title: col.title,
        handleSave: handleSave,
      }),
    };
  });
  /**
   * Import 회원데이터 정규식 검사
   */
  const [regular, setRegular] = useState({
    list: [],
    check: false,
  });
  const regularChk = () => {
    const temp = [];
    let column = '';
    state.rows.map(item => {
      if (!regId.test(item.id)) {
        temp.push(item.id);
        column = 'id';
      } else if (!regKor.test(item.name)) {
        temp.push(item.id);
        column = 'name';
      } else if (!regPhone.test(item.employee_contact)) {
        temp.push(item.id);
        column = 'employee_contact';
      } else if (!regEmail.test(item.mail)) {
        temp.push(item.id);
        column = 'mail';
      }
    });
    setRegular({
      list: temp.length > 0 ? temp : [],
      check: temp.length > 0 ? true : false,
    });
    if (column === 'id') {
      Modal.error({
        title: helpId,
      });
    } else if (column === 'name') {
      Modal.error({
        title: <span>이름은 한글만 가능합니다.</span>,
      });
    } else if (column === 'employee_contact') {
      Modal.error({
        title: (
          <span>
            연락처 양식을 맞춰주십시오.
            <br /> ex) 010-1111-2222
          </span>
        ),
      });
    } else if (column === 'mail') {
      Modal.error({
        title: (
          <span>
            이메일 양식을 맞춰주십시오.
            <br /> ex) hajh@naver.com
          </span>
        ),
      });
    }
    return temp.length > 0 ? true : false;
  };

  /**
   * 중복 아이디 확인(import한 회원내에서)
   */
  const [overlap, setOverlap] = useState({
    list: [],
    check: false,
  });
  const overlapChk = () => {
    const temp = [];
    for (let i = 0; i < state.rows.length - 1; i++) {
      for (let j = i + 1; j < state.rows.length; j++) {
        if (state.rows[i].id === state.rows[j].id) {
          temp.push(state.rows[i].id);
        }
      }
    }
    const overlapList = temp.filter((item, idx, array) => {
      return array.indexOf(item) === idx;
    });
    setOverlap({
      list: overlapList.length > 0 ? overlapList : [],
      check: overlapList.length > 0 ? true : false,
    });
    if (overlapList.length > 0) {
      Modal.error({
        title: '중복 아이디를 확인해 주십시오.',
      });
    }
    return temp.length > 0 ? true : false;
  };

  /**
   * 데이터 검사 버튼 이벤트
   */
  const [existId, setExistId] = useState({
    list: [],
    check: true,
  });
  const existChk = async () => {
    if (overlapChk()) {
      return false;
    }
    if (regularChk()) {
      return false;
    }
    const request = state.rows.map(item => item.id);
    try {
      const response = await axios.post(
        `http://15.165.25.145:9500/admin/employee/listcheck`,
        request,
      );
      const { length } = response.data.list;
      //console.log('중복 아이디길이:', length);
      setExistId({
        list: length > 0 ? response.data.list : [],
        check: length > 0 ? true : false,
      });
      length > 0
        ? Modal.error({
            title: '이미 사용중인 ID가 있습니다.',
          })
        : Modal.success({
            title: '사용 가능한 ID입니다. 회원 등록을 눌러주십시오.',
          });
    } catch (error) {
      handelException(error, history);
      // Modal.error({
      //   title: 'ID중복확인 요청 실패.',
      // });
    }
  };

  /**
   * 중복,정규식 통과안된 Row 강조
   */
  const idCheck = record => {
    let chk = false;
    if (overlap.check) {
      overlap.list.some(item => {
        if (item === record.id) {
          chk = true;
          return true;
        }
      });
    } else if (regular.check) {
      regular.list.some(item => {
        if (item === record.id) {
          chk = true;
          return true;
        }
      });
    } else if (existId.check) {
      existId.list.some(item => {
        if (item === record.id) {
          chk = true;
          return true;
        }
      });
    }
    return chk;
  };
  return (
    <div>
      <Modal
        title="회원 등록"
        visible={modalVisible}
        onCancel={modalCancel}
        width={800}
        footer={[
          <Button key="back" onClick={modalCancel}>
            취소
          </Button>,
        ]}
      >
        <Row gutter={16} justify="space-between">
          <Col
            span={8}
            style={{
              display: 'flex',
              justifyContent: 'space-between',
              alignItems: 'center',
              marginBottom: '5%',
            }}
          >
            <div style={{ display: 'flex', alignItems: 'center' }}>
              <ExcelFile
                filename="회원가입"
                element={<Button type="link">회원가입 샘플양식.xlsx</Button>}
              >
                <ExcelSheet data={sampleData} name="SingUp">
                  <ExcelColumn label="이름" value="name" />
                  <ExcelColumn label="아이디" value="id" />
                  <ExcelColumn label="연락처" value="phone" />
                  <ExcelColumn label="이메일" value="email" />
                  <ExcelColumn label="부서" value="department" />
                </ExcelSheet>
              </ExcelFile>
            </div>
          </Col>
          <Col span={8} />
          <Col
            span={8}
            align="right"
            style={{ display: 'flex', justifyContent: 'space-between' }}
          >
            <>
              <Button
                onClick={handleAdd}
                size="large"
                type="info"
                style={{ marginBottom: 16 }}
              >
                <Icon type="plus" />
                회원 추가
              </Button>{' '}
            </>
            {state.rows.length > 0 && (
              <>
                {existId.check ? (
                  <Button
                    onClick={existChk}
                    size="large"
                    type="primary"
                    style={{ marginBottom: 16, marginLeft: 10 }}
                  >
                    데이터 검사
                  </Button>
                ) : (
                  <Button
                    onClick={handleSubmit}
                    size="large"
                    type="primary"
                    style={{ marginBottom: 16, marginLeft: 10 }}
                  >
                    회원 등록
                  </Button>
                )}
              </>
            )}
          </Col>
        </Row>
        <div>
          <Upload
            name="file"
            beforeUpload={fileHandler}
            onRemove={() => setState({ ...state, rows: [] })}
            multiple={false}
            showUploadList={false}
          >
            <Button>
              <Icon type="upload" /> 엑셀파일 업로드
            </Button>
          </Upload>
        </div>
        <div style={{ marginTop: 20 }}>
          <span style={{ color: 'red' }}>(*) - 필수 항목</span>

          <Table
            size="small"
            components={components}
            // rowClassName={() => 'editable-row'}
            dataSource={state.rows}
            columns={columns}
            rowClassName={record => (idCheck(record) ? 'rowSelect' : '')}
          />
        </div>
      </Modal>
    </div>
  );
};

export default withRouter(MemberAdd);
