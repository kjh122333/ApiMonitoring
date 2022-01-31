import React from 'react';
import ApiDetail from 'components/api/ApiDetail';

const ApiPage = () => {
  // window.scrollTo(0,0);

  return (
    <div>
      <div className="table-operations">
        <p>API 상세보기</p>
      </div>
      <hr />
      <ApiDetail />
    </div>
  );
};

export default ApiPage;
