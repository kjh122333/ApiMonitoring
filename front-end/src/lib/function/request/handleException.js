import { Modal, Empty } from 'antd';
import React from 'react';
const handelException = (error, history) => {
  if (error.response) {
    const { status } = error.response;
    const { code } = error.response.data;
    let message = '';
    let move = false;

    if (status === 401) {
      message = '세션이 만료되었습니다.';
      move = true;
      sessionStorage.clear();
    } else if (status === 400) {
      message = '잘못된 데이터 요청입니다.';
      move = true;
    } else if (status === 403) {
      message = '접근 권한이 없습니다.';
      move = true;
    } else if (status === 405) {
      message = '접근 권한이 없습니다.';
      move = true;
      // sessionStorage.clear();
    } else if (status === 500) {
      message = '서버 에러 발생 - ' + error.response.data.message;
      move = false;
    } else {
      message = error.response.data.message;
      move = false;
    }

    if (move) {
      if (status === 401) {
        Modal.error({
          title: message,
          onOk() {
            history.push('/login');
          },
        });
        return;
      } else if (status === 400) {
        if (code === 916 || code === 915) {
          Modal.error({
            title: '존재하지 않는 데이터 입니다.',
            onOk() {
              history.goBack();
            },
          });
          return;
        }
        Modal.error({
          title: message,
          onOk() {
            history.push('/');
          },
        });
        return;
      } else {
        history.goBack();
        return;
      }
    }
    Modal.error({
      title: message,
    });
  } else {
    Modal.error({
      title: '요청을 실패했습니다.',
      content: '잠시 후 다시 시도해 주십시오.',
    });
  }
};
export default handelException;

export const authException = error => {
  if (error.response) {
    const { status, data } = error.response;

    if (status === 400) {
      return true;
    } else if (status === 500) {
      if (data.code === -1001) return true;
      Modal.error({
        title: '서버에 오류가 발생했습니다. 잠시 후 다시 시도해 주십시오.',
      });
      return false;
    } else {
      return false;
    }
  }
};

// 익셉션 나누기.

// 로그인 관련
// api 등록,수정, 삭제 관련
// 관리자 api config 등록, 수정, 삭제 관련 (위에꺼랑 합칠 수 있으면 합치는 쪽으로)
//
