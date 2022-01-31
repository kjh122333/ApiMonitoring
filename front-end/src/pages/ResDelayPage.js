import React from 'react';
import ResDelayDetail from 'components/resDelay/ResDelayDetail';

const ResponsePage = () => {
  window.scrollTo(0,0);
  return (
    <div>
      <div className="table-operations">
        <p>응답지연 상세화면</p>
      </div>
      <hr />
      <ResDelayDetail />
    </div>
  );
};

export default ResponsePage;
