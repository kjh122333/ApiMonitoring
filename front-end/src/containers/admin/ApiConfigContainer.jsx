import React,{useState, useCallback, useMemo, useEffect} from 'react';
import ConfigType from 'components/api/apiConfig/ConfigType';
import ListController from 'components/api/apiConfig/ListController';
import axios from 'axios';
import {withRouter} from 'react-router-dom';
import handleException from 'lib/function/request/handleException.js';
import ListPrinter from 'components/api/apiConfig/ListPrinter';
import {Icon,Modal} from 'antd';

const radioValues = [
    {key:0, name:"ServiceCategory", krName:"서비스 카테고리"},
    {key:1, name:"Service", krName:"서비스"},
    {key:2, name:"apiCategory",krName:"Api 카테고리"},
];

export const initalFilterValue = {
    serviceCategoryName : '',
    serviceName : '',
    apiCategoryName : '',
    groupName : '',
}

const ApiConfigContainer = ({history}) => {
    const [selected, setSelected] = useState(radioValues[1]); //선택된 라디오버튼 값    
    const [loading, setLoading] = useState(false); //데이터 응답 로딩
    const [listDatas, setListDatas] = useState({
        serviceCategory : [],
        service : [],
        apiCategory: [],
        groupNames : []
    });
    const [filterValues, setFilterValues] = useState(initalFilterValue); //필터 값
    const [reload,setReload] = useState({}); //컨테이너 force render용
    const [selectedRowKeys, setSelectedRowKeys] = useState([]); //리스트에 선택된 키 값 배열
    const [showDeleted, setShowDeleted] = useState(false); //삭제된 항목 출력 여부
    const [isAdmin, setIsAdmin] = useState(false);

    useEffect(()=>{
        const reqUserCheck = async () =>{
            setLoading(true);
            try {
                const resUserCheck = await axios.post('http://15.165.25.145:9500/user/check',{id:sessionStorage.getItem('id')});
                //console.log('유저케케체크', resUserCheck.data);
                if(resUserCheck.data.data==='[ROLE_ADMIN]'){
                    setIsAdmin(true);
                    setSelected(radioValues[0])
                }
                setLoading(false);
            } catch (error) {
                handleException(error,history)
            }
        }
        reqUserCheck();
    },[history]);

    useEffect(()=>{
        const reqList = async () =>{
            try{
                setLoading(true);
                const resServiceCategory = await axios.get('http://15.165.25.145:9500/user/category');
                //console.log('서비스 카테고리 목록', resServiceCategory.data);
                
                const resService = await axios.get('http://15.165.25.145:9500/user/service');
                //console.log('서비스 목록', resService);
                const resApiCategory = await axios.get('http://15.165.25.145:9500/user/apicategory');
                //console.log('api 카테고리 목록', resApiCategory.data);
                const resGroupNames = await axios.get('http://15.165.25.145:9500/user/groupname');
                //console.log('그룹네임 목록', resGroupNames.data);
               
                setListDatas({
                    serviceCategory : resServiceCategory.data.list,
                    service : resService.data.list,
                    apiCategory : resApiCategory.data.list,
                    groupNames : resGroupNames.data.list,
                })
                setFilterValues(initalFilterValue); //필터 초기화
                setSelectedRowKeys([]); //테이블 선택 row 초기화
                setShowDeleted(false); //삭제 항목 출력 여부 초기화
                setLoading(false);
            }catch(error){
                handleException(error,history)
            }
        }
        reqList();
    },[history,selected.key,reload]);

    const renderConfigContainer = useCallback(()=>{
        setReload({})
    },[])

    const handleFilterNames = useCallback(names=>{
        setFilterValues(names)
    },[])
    
     //라디오 버튼 onChange       
     const changeSelected = useCallback((e)=>{ 
        setSelected(e.target.value);
    },[]);

    const handleSelectedRowKeys = useCallback(selectedKeys=>{
        setSelectedRowKeys(selectedKeys)
    },[]);

    //서비스 카테고리, 서비스, api 카테고리 추가 요청
    const reqSave = useCallback(async(postData)=>{
        //console.log('추가 요청 데이터',postData);

        let requestUrl;
        if(selected.key===0) requestUrl='http://15.165.25.145:9500/admin/category'
        else if(selected.key===1) requestUrl='http://15.165.25.145:9500/user/service'
        else if(selected.key===2) requestUrl='http://15.165.25.145:9500/user/apicategory'
        else requestUrl=''
        try{            
            const res = await axios.post(requestUrl,postData);
                if(res.data.success){
                    //console.log('추가 응답',res.data)
                    Modal.success({
                        title: '등록이 완료되었습니다',
                      });
                    return true;   
                }else{

                    Modal.error({
                        title: '등록이 실패하였습니다.',
                        content: res.data.message
                      });
                    return false;
                }
        }catch(error){
            handleException(error, history);
            return false;
        }
    },[history, selected.key]);    

    //삭제 전 삭제되는 하위 항목들 
    const reqDeleteCheck = useCallback(async()=>{
        let requestUrl;
        if(selected.key===0) requestUrl=`http://15.165.25.145:9500/admin/category/tcheck?idx=${selectedRowKeys}`
        else if(selected.key===1) requestUrl=`http://15.165.25.145:9500/user/service/tcheck?idx=${selectedRowKeys}`
        else if(selected.key===2) requestUrl=`http://15.165.25.145:9500/user/apicategory/tcheck?idx=${selectedRowKeys}`
        else requestUrl=''

        try{
            
            //console.log('삭제체크 요청 데이터',selectedRowKeys);
            const res = await axios.get(requestUrl);
                if(res.data.success){
                    //console.log('삭제체크 응답 ',res.data);
                    return res.data.list;
                }else{

                    Modal.error({
                        title: '데이터요청을 실패하였습니다',
                        content: res.data.message
                      });
                }
        }catch(error){
            handleException(error, history);
        }
    },[history, selected.key, selectedRowKeys]);    
    //선택된 테이블 row 삭제 요청
    const reqDelete = useCallback(async()=>{
        let requestUrl;
        if(selected.key===0) requestUrl='http://15.165.25.145:9500/admin/category/t'
        else if(selected.key===1) requestUrl='http://15.165.25.145:9500/user/service/t'
        else if(selected.key===2) requestUrl='http://15.165.25.145:9500/user/apicategory/t/'
        else requestUrl=''
        try{            
            const res = await axios.patch(requestUrl,selectedRowKeys);
            //console.log('삭제 요청 데이터',selectedRowKeys);
            //console.log('삭제 요청 응답',res);            
                if(res.data.success){
                    Modal.error({
                        title: '삭제 완료되었습니다.',
                      });
                    renderConfigContainer();
                    return true;
                }else{
                    Modal.error({
                        title: '삭제 실패하였습니다.',
                        content: res.data.message
                      });
              
                }
        }catch(error){
            handleException(error, history);
        }
    },[history, renderConfigContainer, selected.key, selectedRowKeys]);    

    const reqUpdate = useCallback(async(postData)=>{        
        //console.log('수정 서브밋 데이터' , postData);
        
        let requestUrl;
        if(selected.key===0) requestUrl=`http://15.165.25.145:9500/admin/category/${postData.service_category_no}`
        else if(selected.key===1) requestUrl=`http://15.165.25.145:9500/user/service/${postData.service_no}`
        else if(selected.key===2) requestUrl=`http://15.165.25.145:9500/user/apicategory/${postData.api_category_no}`
        else requestUrl=''
        try{            
            const res = await axios.put(requestUrl,postData);
                if(res.data.success){
                    //console.log('수정 응답',res.data)
                    Modal.success({
                        title: '수정 완료되었습니다.',
                      });
                    return true;
                }else{
                    Modal.error({
                        title: '수정 실패하였습니다.',
                        content: res.data.message
                      });

                    return false;
                }
        }catch(error){
            handleException(error, history);
            return false;
        }
    },[history, selected.key]);

    const handleShowDeleted = useCallback(checked=>{
        //console.log(checked);
        setShowDeleted(checked);
    },[])


    //console.log('필터네임',filterValues);
    //console.log('리스트데이터', listDatas);
    //console.log('셀렉트 로우', selectedRowKeys);
    

    return !loading ?(
        <>
            <ConfigType selected={selected} changeSelected={changeSelected} radioValues={radioValues} isAdmin={isAdmin} />
            <ListController 
                handleFilterNames={handleFilterNames} 
                workKey={selected.key} 
                reqSave={reqSave} 
                reqDeleteCheck={reqDeleteCheck}
                reqDelete={reqDelete} 
                renderConfigContainer={renderConfigContainer}
                showDeleted={showDeleted}
                handleShowDeleted={handleShowDeleted}
                selectedRowKeys={selectedRowKeys}
                listDatas={listDatas}
            />
           
            <ListPrinter 
                workKey={selected.key} 
                listDatas={listDatas}
                filterValues={filterValues}
                selectedRowKeys={selectedRowKeys}
                handleSelectedRowKeys={handleSelectedRowKeys}
                showDeleted={showDeleted}
                renderConfigContainer={renderConfigContainer}
                reqUpdate={reqUpdate}
            />
        </>
    ) : <span style={{ color: 'blue', fontSize: '100px' }}>
            <Icon type="loading" />
        </span>;
};

export default withRouter(ApiConfigContainer);
