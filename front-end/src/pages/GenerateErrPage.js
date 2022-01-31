import React from 'react';
import GenerateErrContainer from 'containers/generateError/GenerateErrContainer';

const GenerateErrPage = () => {
  window.scrollTo(0,0);
    return (
        <div>
          <div className="table-operations">
            <p>에러/지연 추가</p>
          </div>
          <hr />
          <GenerateErrContainer />
        </div>
      );
    };

export default GenerateErrPage;