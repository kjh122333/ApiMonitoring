import React from 'react';
import ErrorDetail from 'components/error/ErrorDetail';
const ErrorPage = () => {
  window.scrollTo(0,0);
  return (
    <div>
      <div className="table-operations">
        <p>에러 상세화면</p>
      </div>
      <hr />
      <ErrorDetail />
    </div>
  );
};

export default ErrorPage;
