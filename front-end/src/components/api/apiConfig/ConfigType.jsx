import React,{ useMemo} from 'react';
import { Radio } from 'antd';
import { Badge } from 'reactstrap';


const ConfigType = ({ selected, changeSelected,radioValues, isAdmin }) => {
  const setDefaultValues = useMemo(()=>(
    radioValues.find((radio=>selected.key===radio.key))
  ),[radioValues, selected.key])
  
  return (
    <div style={{ textAlign: 'left' }}>
      <Badge
        color="secondary"
        style={{ fontSize: '100%', width: 'auto', marginRight: '5px' }}
      >
        <span style={{ marginRight: '10px' }}>작업 단위</span>
        <Radio.Group          
          defaultValue={setDefaultValues}
          buttonStyle="solid"
          onChange={changeSelected}
        >
          {isAdmin && <Radio.Button value={radioValues[0]}>{radioValues[0].krName}</Radio.Button> }
          <Radio.Button value={radioValues[1]}>{radioValues[1].krName}</Radio.Button>
          <Radio.Button value={radioValues[2]}>{radioValues[2].krName}</Radio.Button>
        </Radio.Group>
      </Badge>
    </div>
  );
};

export default ConfigType;
