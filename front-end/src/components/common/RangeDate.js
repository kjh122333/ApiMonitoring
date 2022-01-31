import React, { useState } from 'react';
import 'antd/dist/antd.css';
import 'lib/css/admin/search.css';
import 'lib/css/api/page.css';
import {
  Form,
  DatePicker,
  Button,
  Input,
  Select,
  Icon,
  Row,
  Col,
  Tooltip,
  Switch,
} from 'antd';
import moment from 'moment';
import { useEffect } from 'react';

const { RangePicker } = DatePicker;
const { Option, OptGroup } = Select;

const RangeDate = ({
  onDateChange,
  clearAll,
  onSorter,
  displayMenu,
  columnsKey,
  data,
  radio,
}) => {
  const employeeName = sessionStorage.getItem('name');
  //담당 API 보기
  const [switchMain, setSwitchMain] = useState(false);
  const [switchSub, setSwitchSub] = useState(false);

  //날짜 state저장
  const [date, setDate] = useState({
    startDate: '',
    finishDate: '',
  });

  const onChange = data => {
    setDate({
      ...date,
      startDate: data[0],
      finishDate: data[1],
    });
  };

  //Today 이벤트 핸들러
  const onClickToday = () => {
    setDate({
      ...date,

      startDate: moment(),
      finishDate: moment(),
    });
  };
  //기간 단위(7일,1개월,3개월 등) 이벤트 핸들러
  const onClickDate = (previous, unit) => {
    setDate({
      ...date,
      startDate: moment().subtract(previous, unit),
      finishDate: moment(),
    });
  };

  /**
   * Select 이벤트 핸들러
   */
  const onSelectChange = value => {
    onSorter(JSON.parse(value));
  };

  /**
   * Input 이벤트 핸들러
   */
  const onInputChange = e => {
    setDate({
      ...date,
      [e.target.name]: e.target.value,
    });
  };
  useEffect(() => {
    if (radio !== undefined) {
      setSwitchMain(false);
      setSwitchSub(false);
    }
    //console.log('radio :', radio);
  }, [radio]);

  /**
   * 담당 API만 보기 스위치 이벤트
   */
  const onChangeMain = checked => {
    setSwitchMain(!switchMain);
    //console.log('사람이름:',employeeName)
    setDate(date => ({
      ...date,
      manager: checked ? employeeName : '',
    }));
    onDateChange({
      ...date,
      manager: checked ? employeeName : '',
    });
  };
  const onChangeSub = checked => {
    setSwitchSub(!switchSub);
    setDate(date => ({
      ...date,
      subManager: checked ? employeeName : '',
    }));

    onDateChange({
      ...date,
      subManager: checked ? employeeName : '',
    });
  };

  /**
   * submit 프로젝트에 맞게 수정 필요
   */

  const handleSubmit = e => {
    e.preventDefault();
    onDateChange(date);
  };

  /**
   * Reset 버튼
   */
  const reset = () => {
    setDate({
      startDate: '',
      finishDate: '',
    });
    setSwitchMain(false);
    setSwitchSub(false);

    clearAll();
  };

  /**
   * OptGroup 동적 생성
   */
  const optCreate = columnKey => {
    // //console.log('columnsKey5555', columnsKey);
    return columnKey.map((item, index) => {
      return (
        <OptGroup label={item.title} key={index}>
          <Option
            value={JSON.stringify({
              order: 'ascend',
              columnKey: item.dataIndex,
            })}
          >
            {item.title} 오름차순
          </Option>
          <Option
            value={JSON.stringify({
              order: 'descend',
              columnKey: item.dataIndex,
            })}
          >
            {item.title} 내림차순
          </Option>
        </OptGroup>
      );
    });
  };
  const row = [
    { label: '서비스', name: 'service', placeholder: 'Service' },
    { label: 'Service URL', name: 'serviceName', placeholder: 'Service URL' },
    { label: 'API URL', name: 'apiName', placeholder: 'API URL' },
    { label: '메소드', name: 'http', placeholder: 'Http Method' },
    { label: '담당부서', name: 'department', placeholder: 'Group' },
    { label: '담당자(정)', name: 'manager', placeholder: 'manager' },
    { label: '담당자(부)', name: 'subManager', placeholder: 'subManager' },
    { label: '담당 API보기', name: 'management', placeholder: 'management' },
  ];
  const createItem = formItem => {
    return formItem.map((item, index) => {
      return (
        <Col key={index} span={6}>
          <Form.Item style={{ marginBottom: '0px' }} label={item.label}>
            {item.name !== 'management' ? (
              <Input
                name={item.name}
                value={date[item.name]}
                placeholder={item.placeholder}
                onChange={onInputChange}
              />
            ) : (
              <>
                <Switch
                  checked={switchMain}
                  onChange={onChangeMain}
                  // checkedChildren="정"
                  unCheckedChildren="정"
                  style={{ marginRight: '10px' }}
                />
                <Switch
                  checked={switchSub}
                  onChange={onChangeSub}
                  // checkedChildren="부"
                  unCheckedChildren="부"
                />
              </>
            )}
          </Form.Item>
        </Col>
      );
    });
  };

  return (
    <div>
      <Form className="ant-advanced-search-form" onSubmit={handleSubmit}>
        <Row>
          <Col span={12}>
            <Form.Item style={{ marginBottom: '0px' }} label={<span>{radio==='apiList' ? '등록기간 검색' :  '발생 기간 검색'}</span>}>
              <RangePicker
                value={
                  date.startDate && date.finishDate
                    ? [date.startDate, date.finishDate]
                    : []
                }
                onChange={onChange}
              />
              <br />
              <Button onClick={onClickToday}>오늘</Button>
              <Button onClick={() => onClickDate(7, 'd')}>일주일</Button>
              <Button onClick={() => onClickDate(1, 'M')}>1개월</Button>
              <Button onClick={() => onClickDate(3, 'M')}>3개월</Button>
              <Button onClick={() => onClickDate(6, 'M')}>6개월</Button>
              <Button onClick={() => onClickDate(1, 'Y')}>12개월</Button>
            </Form.Item>
          </Col>
          <Col span={9}></Col>
          <Col span={3}>
            <Form.Item style={{ marginBottom: '0px' }}>
              <Tooltip placement="bottom" title={displayMenu}>
                <Button type="primary">
                  <Icon type="setting" />
                  컬럼 설정
                  <Icon type="down" />
                </Button>
              </Tooltip>
            </Form.Item>
          </Col>
        </Row>
        <Row gutter={16}>{createItem(row.slice(0, 4))}</Row>
        <Row gutter={16}>{createItem(row.slice(4))}</Row>
        <Row gutter={16}>
          <Col span={6}>
            <Form.Item style={{ marginBottom: '0px' }}>
              <Button onClick={reset}>
                <Icon type="reload" style={{ fontSize: '20px' }} /> 초기화
              </Button>
              <Button type="primary" htmlType="submit">
                <Icon type="search" style={{ fontSize: '20px' }} /> 검색
              </Button>
            </Form.Item>
          </Col>
          <Col span={14}></Col>
          <Col span={4}>
            <Form.Item style={{ marginBottom: '0px' }}>
              <Select
                defaultValue="테이블 정렬 선택"
                style={{ width: '100%' }}
                onChange={onSelectChange}
              >
                {optCreate(columnsKey)}
              </Select>
            </Form.Item>
          </Col>
        </Row>
      </Form>
    </div>
  );
};

export default RangeDate;
