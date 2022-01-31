import React, { useState, useEffect } from 'react';
import GenerateError from 'components/generateError/GenerateError';
import GenerateDelay from 'components/generateError/GenerateDelay';
import 'antd/dist/antd.css';
import { Radio, Modal } from 'antd';
import axios from 'axios';
const GenerateErrContainer = () => {
  const [data, setData] = useState();
  /**
   * Radio버튼 관련
   */
  const [radio, setRadio] = useState('error');
  useEffect(() => {
    const requestFunc = async () => {
      try {
        const response = await axios.get(
          'http://15.165.25.145:9500/user/api/fullurlmethod',
        );
        //console.log('response List:', response.data.list);
        const { list } = response.data;
        const tempList = [];
        for (let i = 0; i < list.length; i++) {
          const idx = tempList.findIndex(item => item.url === list[i].url);
          if (idx > -1) {
            tempList[idx].method += ',' + list[i].method;
          } else {
            tempList.push(list[i]);
          }
        }
        //console.log('result:', tempList);
        setData(tempList);
      } catch (error) {}
    };
    requestFunc();
  }, []);

  const onSubmitErr = async data => {
    //console.log('data', data);
    try {
      const response = await axios.post(
        'http://15.165.25.145:9500/user/apierr',
        data,
      );
      //console.log('response List:', response);
      Modal.success({
        title: '에러 등록을 완료했습니다.',
      });
    } catch (error) {
      //console.log(error.response);
    }
  };
  const onSubmitDelay = async data => {
    //console.log('data', data);
    try {
      const response = await axios.post(
        'http://15.165.25.145:9500/user/apidelay',
        data,
      );
      //console.log('response List:', response);
      Modal.success({
        title: '응답지연 등록을 완료했습니다.',
      });
    } catch (error) {
      //console.log(error.response);
    }
  };
  return (
    <div>
      <div style={{ textAlign: 'left' }}>
        <Radio.Group
          onChange={e => {
            setRadio(e.target.value);
          }}
          defaultValue="error"
          value={radio}
          buttonStyle="solid"
        >
          <Radio.Button value="error">에러</Radio.Button>
          <Radio.Button value="delay">지연</Radio.Button>
        </Radio.Group>
      </div>

      {radio === 'error' ? (
        <GenerateError data={data} onSubmit={onSubmitErr} />
      ) : (
        <GenerateDelay data={data} onSubmit={onSubmitDelay} />
      )}
    </div>
  );
};

export default GenerateErrContainer;
