import React,{useEffect,useState} from 'react';
import { Icon,Button } from 'antd';
import 'lib/css/notFound/notFound.css';
const NotFound = ({history}) => {
  // const [transparency, setTransparency] = useState({ opacity: 1 });
  const [boolean, setBoolean] = useState(false);
  const tick = () => {
    setBoolean((boolean)=> !boolean);
    // setTransparency(() => ({opacity : boolean ? 0.5 : 1}))
  }
  useEffect(()=>{
    setInterval(() => {
      tick();
    }, 1000);
  },[])
  return (
    <div className="centerOuterNot">
      
      <div className="centerInnerNot">
        <h1>
          4<span style={{opacity : boolean ? 0.7 : 1}}>0</span>4
        </h1>
        <h2>죄송합니다. 현재 찾을 수 없는 페이지를 요청 하셨습니다.</h2>
        <br />
        <h3>
          존재하지 않는 주소를 입력하셨거나,
          <br />
          요청하신 페이지의 주소가 변경, 삭제되어 찾을 수 없습니다.
          <br />
          <br />
          감사합니다.
        </h3><br/>
        <Button type="primary" style={{marginRight:'10px'}} onClick={()=>{history.push('/')}}>메인으로</Button>
        <Button onClick={()=>{history.goBack()}}>이전 페이지</Button>
      </div>
    </div>
  );
};

export default NotFound;
