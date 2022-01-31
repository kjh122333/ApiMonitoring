import React from 'react';
import 'lib/css/api/table-button.css';
import ApiConfigContainer from 'containers/admin/ApiConfigContainer';

const ApiConfigPage = () => {
    window.scrollTo(0,0);

    return (
        <div>
            <div className="table-operations">
                <p>API 관리</p>
            </div>      
            <hr />
            <ApiConfigContainer/>
        </div>
    );
};

export default ApiConfigPage;


