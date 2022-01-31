import React, { useState } from 'react';
import 'antd/dist/antd.css';
import {
  Select,
  Button,
  PageHeader,
  Input,
  Modal,
  Timeline,
  InputNumber,
} from 'antd';
import { withRouter } from 'react-router-dom';
const { Option } = Select;
const { TextArea } = Input;


const GenerateError = ({ history, data, onSubmit }) => {
  const [form, setForm] = useState({
    url: '',
    method: '',
    api_err_code: '',
    api_err_msg: '',
  });

  const onChangeData = e => {
    setForm({
      ...form,
      [e.target.name]: e.target.value,
    });
  };
  const onChangeCode = value => {
    setForm({
      ...form,
      api_err_code: value,
    });
  };

  const urlOption = () => {
    if (data !== undefined) {
      return data.map((item, k) => (
        <Option key={k} value={item.url}>
          {item.url}
        </Option>
      ));
    }
  };

  const methodOption = () => {
    if (data !== undefined) {
      return data.map((item, k) => {
        if (form.url === item.url) {
          const tempMethod = item.method.split(',');
          //console.log('메소드:', item.method.split(','));
          return tempMethod.map((element, key) => (
            <Option key={key} value={element}>
              {element.toUpperCase()}
            </Option>
          ));
        }
      });
    }
  };
  const onChangeUrl = value => {
    //console.log('onChangeURL');
    setForm({
      ...form,
      url: value,
      method: '',
    });
  };
  const onChangeMethod = value => {
    //console.log('onChangeMethod');
    setForm({
      ...form,
      method: value,
    });
  };
  //console.log('form:', form);
  const submitBefore = () => {
    const { url, method, api_err_code, api_err_msg } = form;

    if (
      url === '' ||
      method === '' ||
      api_err_code === '' ||
      api_err_code === null ||
      api_err_msg === ''
    ) {
      Modal.warning({
        title: '모든 항목을 입력해 주십시오.',
      });
      return false;
    }

    onSubmit(form);
    setForm({
      url: '',
      method: '',
      api_err_code: '',
      api_err_msg: '',
    });
  };
  return (
    <div style={{ textAlign: 'left' }}>
      <PageHeader
        style={{
          border: '1px solid rgb(235, 237, 240)',
        }}
        // onBack={() => history.goBack()}
        // title={
        //   <Badge
        //     status="error"
        //     text={<span style={{ fontSize: '20px', color: 'red' }}>에러</span>}
        //   />
        // }
        // tags={[
        //   <Tag key="1" color={errdata.errorStatus === 'T' ? 'red' : 'green'}>
        //     {errdata.errorStatus === 'T' ? 'Error' : 'Finished'}
        //   </Tag>,
        //   <span key="2" style={{ fontSize: '120%' }}>
        //     {renderMethodBage(errdata.http)}
        //   </span>,
        // ]}
        extra={[
          <Button key="1" type="primary" onClick={submitBefore}>
            추가
          </Button>,
        ]}
      >
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>URL 명</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            <Select
              showSearch
              style={{ width: 550 }}
              value={form.url}
              placeholder="url"
              optionFilterProp="children"
              onChange={onChangeUrl}
              filterOption={(input, option) =>
                option.props.children
                  .toLowerCase()
                  .indexOf(input.toLowerCase()) >= 0
              }
            >
              {urlOption()}
            </Select>
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>Http Method</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            <Select
              showSearch
              style={{ width: 150 }}
              value={form.method}
              placeholder="method"
              optionFilterProp="children"
              onChange={onChangeMethod}
              filterOption={(input, option) =>
                option.props.children
                  .toLowerCase()
                  .indexOf(input.toLowerCase()) >= 0
              }
            >
              {methodOption()}
            </Select>
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>에러코드</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            <InputNumber
              style={{ width: '20%' }}
              min={100}
              max={599}
              onChange={onChangeCode}
              value={form.api_err_code}
            />
          </Timeline.Item>
        </Timeline>
        <Timeline>
          <Timeline.Item className="line-item">
            <h5>에러 원인</h5>
          </Timeline.Item>
          <Timeline.Item className="line-item" color="gray">
            <TextArea
              rows={5}
              name="api_err_msg"
              onChange={onChangeData}
              value={form.api_err_msg}
            />
          </Timeline.Item>
        </Timeline>
      </PageHeader>
    </div>
  );
};

export default withRouter(GenerateError);
