import React,{ useMemo, useCallback,useState} from 'react';
import { Radio,Icon } from 'antd';
import { Badge } from 'reactstrap';



const WriteType = ({ selected, changeSelected,radioValues }) => {
  const setDefaultValues = useMemo(()=>(
    radioValues.find((radio=>selected.key===radio.key))
  ),[radioValues, selected.key]);

  // const [visibleGuide, setVisibleGuide] = useState(false);
    // //console.log(visibleGuide);
    
  const openGuidePage = useCallback(()=>{
    // setVisibleGuide(true);
    let url='/notFound'
    if(selected.key===0) url='/guide?selectedKey=0'
    else if(selected.key===1) url='/guide?selectedKey=1'
    else if(selected.key===2) url='/guide?selectedKey=2'
    else if(selected.key===3) url='/guide?selectedKey=3'
    
    if(selected.key!==3) window.open(url,'_blank','height=600,width=620');
    
  },[selected.key]);

   
  return (
    <div style={{ textAlign: 'left' }}>
      <Badge
        color="secondary"
        style={{ fontSize: '100%', width: 'auto', marginRight: '5px' }}
      >
        <span style={{ marginRight: '10px' }}>등록 단위</span>
        <Radio.Group          
          defaultValue={setDefaultValues}
          buttonStyle="solid"
          onChange={changeSelected}
        >
          <Radio.Button value={radioValues[0]}>{radioValues[0].krName}</Radio.Button>
          <Radio.Button value={radioValues[1]}>{radioValues[1].krName}</Radio.Button>
          <Radio.Button value={radioValues[2]}>{radioValues[2].krName}</Radio.Button>
          <Radio.Button value={radioValues[3]}>{radioValues[3].krName}</Radio.Button>
        </Radio.Group>
      </Badge>
      {selected.key < 3 && (
        <Icon
          onClick={openGuidePage}
          type="question-circle"
          style={{ color: '#1890FF', fontSize:'30px' }}
        />
      )
      }
      {/* {visibleGuide && guidePage} */}
    </div>
  );
};

export default WriteType;
