import React,{useState, useEffect, useCallback} from 'react';
import handleException from 'lib/function/request/handleException.js';
import {Link} from 'react-router-dom';
import {List,Icon} from 'antd';
import axios from 'axios';
// import InfiniteScroll from "react-infinite-scroller";


const NotiList = ({history, popOverVisible, closePopOverVisible,onVisibleChange,notiCntToZero}) => {
    const [notiList, setNotiList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [reRender, setReRender] = useState({});
    const [currentPage, setCurrentPage] = useState(1);
    // const [listState, setListState] = useState({
    //     data:[],
    //     loading : false,
    //     hasMore : false,
    // })
  
    useEffect(()=>{

        const reqNotiList = async ()=>{
            try{
              setLoading(true);
              const employeeNum = sessionStorage.getItem('num');
              const res = await axios.get(`http://15.165.25.145:9500/user/notification/list/${employeeNum}`);
              const {list} = res.data
              //console.log('알림 목록',list);
              
              setNotiList(list)
            //   if(list.length < 5){
            //       setListState({
            //           data : list,
            //           loading:false,
            //           hasMore:false,
            //       })
            //   }else{
            //     setListState({
            //         data : list.slice(0,5),
            //         loading:false,
            //         hasMore:true,
            //     })
            //   }
              setLoading(false);
              
            }catch(error){
              handleException(error,history);
            }
          }
          //console.log('마운트됨');
          notiCntToZero();
          popOverVisible && reqNotiList();
          
          
    },[history, notiCntToZero, popOverVisible, reRender]);

    /**
     * 알림 메세지 생성(에러/지연/담당자 배정).
     */
    const renderMessage = useCallback(notiItem=>{
        let message = '';
        if(notiItem.err_no!==0) message = '에러가 발생 했습니다.'
        else if(notiItem.delay_no!==0) message = '지연이 발생 했습니다.'
        else message = '담당 API가 추가되었습니다.'
        
        return message;
        
    },[]);
    /**
     * 해당 알림 링크 지정.
     */
    const renderLink = useCallback(notiItem=>{
        const {err_no, delay_no, api_no, service_no, api_category_no} = notiItem;
        let link='';
        if(err_no!==0) link = `/error/${err_no}`
        else if(delay_no!==0) link = `/delay/${delay_no}`
        else link = `/api/detail/${api_no}?service_no=${service_no}&api_category_no=${api_category_no}`
        return link;
    },[])

    /**
     * 알림 1개 읽음 처리.
     */
    const readNotification = useCallback(async (notifyNo)=>{
        //console.log(notifyNo);
        try{
            await axios.get(`http://15.165.25.145:9500/user/notification/${notifyNo}`);
            setReRender({});
        }catch(error){
            handleException(error,history);
        }
    },[history]);

    /**
     * 알림 1개 삭제 처리.
     */
    const removeNotification = useCallback(async (notifyNo)=>{
        //console.log(notifyNo);
        try{
            await axios.delete(`http://15.165.25.145:9500/user/notification/${notifyNo}`);
            setReRender({});
        }catch(error){
            handleException(error,history);
        }
    },[history]);
         
    return !loading ? (
        <List        
        size="small"
        bordered
        dataSource={notiList}
        renderItem={item => (
            <List.Item style={{background:item.is_read==='F' && '#E6F7FF'}} >
                <div style={{width:'100%'}}>
                    <Link to={renderLink(item)} onClick={()=>{readNotification(item.notify_no); closePopOverVisible();}}>{renderMessage(item)} <br/>{item.service_url+item.api_url}</Link><br/>
                    <span style={{color:'gray'}}>{item.insert_timestamp}</span>
                    <span style={{float:'right'}}>
                        {item.is_read==='F' && (
                            <Icon type="check-circle" onClick={e=>{e.stopPropagation(); readNotification(item.notify_no)}} style={{marginRight:'3px'}}/>
                        )}
                        <Icon type="close-circle" onClick={e=>{e.stopPropagation(); removeNotification(item.notify_no)}}/>
                    </span>
                </div>
            </List.Item>
        )}
        pagination={{ 
            pageSize: 5,
            position:'top', 
            simple:true, 
            current : currentPage,            
            onChange:current=>{
                setCurrentPage(current)
            },
            style:{textAlign:'left'}
         }}
        />
    ):(
    <span style={{ color: 'blue', fontSize: '100px',display:'grid' }}>
        <Icon type="loading"/>
    </span>
    );
};

export default NotiList;











/*
<div style={{overflowY:'scroll', maxHeight:'300px'}}>
<InfiniteScroll
initialLoad={false}
pageStart={0}
loadMore={handleInfiniteOnLoad}
hasMore={!listState.loading && listState.hasMore}
useWindow={false}
>
<List
size="small"
bordered
dataSource={listState.data}
renderItem={item => (
    <List.Item style={{background:item.is_read==='F' && '#E6F7FF'}} >
        <div style={{width:'100%'}}>
            <Link to={renderLink(item)} onClick={()=>{readNotification(item.notify_no); closePopOverVisible();}}>{renderMessage(item)} <br/>{item.service_url+item.api_url}</Link><br/>
            <span style={{color:'gray'}}>{item.insert_timestamp}</span>
            <span style={{float:'right'}}>
                {item.is_read==='F' && (
                    <Icon type="check-circle" onClick={e=>{e.stopPropagation(); readNotification(item.notify_no)}} style={{marginRight:'3px'}}/>
                )}
                <Icon type="close-circle" onClick={e=>{e.stopPropagation(); removeNotification(item.notify_no)}}/>
            </span>
        </div>
    </List.Item>
)}
>
  {listState.loading && listState.hasMore && (
      <div style={{ position: 'absolute',
        bottom: '40px',
        width: '100%',
        textAlign: 'center',}}>
        <Spin />
      </div>
  )}
</List>
</InfiniteScroll>
</div>
*/