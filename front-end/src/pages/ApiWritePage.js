import React from 'react';
import 'lib/css/api/table-button.css';
import ApiWriteContainer from 'containers/api/ApiWriteContainer'

const ApiWritePage = () => {
  window.scrollTo(0,0);
  return (
    <div>      
      <div className="table-operations">
        <p>API 등록</p>
      </div>      
      <hr />
     <ApiWriteContainer/>
    </div>
  );
};

export default ApiWritePage;
