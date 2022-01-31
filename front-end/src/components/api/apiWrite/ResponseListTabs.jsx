import React,{useState, useCallback, useRef} from 'react';
import { Tabs,Input ,Badge  } from 'antd';

const { TabPane } = Tabs;
const { TextArea } = Input;


const ResponseListTabs =({tabs, handleRemoveTabs,responseValues,validResponseValues}) => {
  //console.log('탭',tabs);
  

    const [activeKey, setActiveKey] = useState(()=>tabs.length>0 ? tabs[0].key : "");
    
    const onChange = useCallback(activeKey => {
      setActiveKey(activeKey);
      },[]);
    
    return (
      <Tabs
        onChange={onChange}
        activeKey={activeKey}
        type="editable-card"
        onEdit={(key, action) => {
          handleRemoveTabs(key, action);
        }}
        hideAdd={true}
        tabBarStyle={{marginBottom:'0', borderBottom:'none'}}
      >
        {tabs.map(pane => (
          <TabPane
          style={{border:'1px solid #dddddd',borderRadius:'4px'}}
            tab={
              <span>
                <Badge
                  status={responseValues[pane.key].isJson ? 'success' : 'error'}
                />
                {pane.title}
              </span>
            }
            key={pane.key}
            closable={pane.closable}
          >
            {pane.content(responseValues[pane.key].value, pane.key)}
            
          </TabPane>
        ))}
        {validResponseValues().isJson === false && (
              <span style={{ color: 'red' }}>
                json 타입이 일치하지 않습니다.
              </span>
            )}
      </Tabs>
    );
};

export default ResponseListTabs;