import React from 'react';
import {withRouter} from 'react-router-dom';
import { Collapse, Icon } from 'antd';
import queryString from 'query-string';


import Step1Url from 'static/img/Step1_url.png'
import Step1File from 'static/img/Step1_file.png'
import Step1File2 from 'static/img/Step1_file2.png'
import Step1SwaggerUrl from 'static/img/Step1_swaggerUrl.png'
import Step1SwaggerData from 'static/img/Step1_swaggerData.png'
import Step1Convert from 'static/img/Step1_convert.png'
import Step4Check from 'static/img/Step4_check.png'
import Step5CUD from 'static/img/step5_create_update_delete.png'
import Step2ApiCategory from 'static/img/step2_apiCategory.png'
import Step4Check2 from 'static/img/Step4_check2.png'
import Step3Api from 'static/img/step3_api.png'


const { Panel } = Collapse;


const customPanelStyle = {
    background: '#f7f7f7',
    borderRadius: 4,
    marginBottom: 24,
    border: 0,
    overflow: 'hidden',
  };

const ServiceGuide = ({history}) => {
   
    const {selectedKey} = queryString.parse(history.location.search); //string

    const title = (()=>{
        if(selectedKey==='0') return '서비스 등록 가이드';
        else if(selectedKey==='1') return 'API 카테고리 등록 가이드';
        else if(selectedKey==='2') return 'API 등록 가이드';
        else if(selectedKey==='3') return '직접 입력 가이드';
        else return '가이드 페이지';
    })();

    const checkStepNumber = (()=>{
        if(selectedKey==='0') return '②';
        else if(selectedKey==='1') return '③';
        else if(selectedKey==='2') return '④';
        else return '';
    })();

    const checkCUD_StepNumber = (()=>{
        if(selectedKey==='0') return '③';
        else if(selectedKey==='1') return '④';
        else if(selectedKey==='2') return '⑤';
        else return '';
    })();

    return (
        <div>
            <br/>
            <h4>{title}</h4>
            <hr/>
            <div className="content">
                <div className="step">
                    <h4>①. </h4><h5>Swagger url 또는 json파일 등록.</h5><br/>
                    <red-bold> * 본 서비스는 Swagger 2.0 포맷으로 작성된 데이터만 등록 가능 합니다.</red-bold>
                    <img src={Step1Url} width="580px" alt="Step1_url" className="img" /><br/>
                    <red-bold>1. </red-bold><span>Swagger 데이터를 import 할 방법 선택.</span><br/>
                    <red-bold>2. </red-bold><span>(URL 등록)서비스 카테고리, 담당부서 선택 후 <blue-bold>Swagger 주소</blue-bold> 입력.</span><br/>
                    <red-bold>2-1. </red-bold><span>(파일 등록)서비스 카테고리, 담당부서 선택 후 <blue-bold>파일(.json, .txt)</blue-bold> 입력.</span><br/>
                    <img src={Step1File2} width="500px" alt="Step1_file2" className="img" style={{marginLeft:'15px'}} /><br/>
                    <red-bold>3. </red-bold><span>Swagger 데이터의 유효성 검사 후 다음 단계 진행.</span><br/><br/>
                </div>
                
                <Collapse
                    bordered={false}
                    // defaultActiveKey={['1']}
                    expandIcon={({ isActive }) => <Icon type="caret-right" rotate={isActive ? 90 : 0} />}
                >
                <Panel header={<blue-bold>※ Swagger 주소 및 파일 얻기<span style={{fontSize:'12px'}}>(click)</span></blue-bold>} key="1" style={customPanelStyle}>
                <blue-bold>1. </blue-bold><span>Swagger Ui에 접속하여 default 경로를 확인 후, 서비스 url + default 경로가 Swagger json 데이터 URL.</span><br/>
                    <img src={Step1SwaggerUrl} width="580px" alt="Step1_swaggerUrl" className="img" /><br/><br/>
                    <blue-bold>2. </blue-bold><span>위에서 얻은 URL을 통해 json 데이터 확인 가능. 해당 URL을 본 서비스에 등록하거나 데이터를 파일(.txt, .json)로 저장하여 등록.</span><br/>
                    <img src={Step1SwaggerData} width="580px" alt="Step1_swaggerData" className="img" /><br/>
                </Panel>
                <Panel header={<blue-bold>※ 다른 포맷의 데이터 Swagger 2.0 포맷으로 변환하기<span style={{fontSize:'12px'}}>(click)</span></blue-bold>} key="2" style={customPanelStyle}>
                <red-bold> * 경고! API 정보가 외부에 유출될 수 있습니다.</red-bold><br/>
                <span>Open Api Spec 3.0 등의 다른 포맷으로 작성된 데이터는 <a href="https://www.apimatic.io/" target="_blank" rel="noopener noreferrer">여기에서</a>(로그인 필요) <blue-bold>OpenAPI/Swagger v2.0(JSON)</blue-bold>으로 변환하여 등록 바랍니다.</span>
                    <img src={Step1Convert} width="580px" alt="Step1_swaggerData" className="img" /><br/>
                    <red-bold>1. </red-bold>로그인 후 Transform API 선택<br/>
                    <red-bold>2. </red-bold>파일 또는 url 등록<br/>
                    <red-bold>3. </red-bold><blue-bold>OpenAPI/Swagger v2.0(JSON)</blue-bold> 포맷 설정<br/>
                    <red-bold>4. </red-bold>Convert 진행(url로 데이터 변환이 실패할 경우 데이터를 파일로 저장하고 재시도)<br/>
                </Panel>    
            </Collapse>

                {selectedKey>'0' && (
                    <>
                    <br/>
                    <div className="step">
                        <h4>②. </h4><h5>API 카테고리 선택.</h5><br/>
                        <img src={Step2ApiCategory} width="580px" alt="step2_apiCategory" className="img" /><br/>
                        <red-bold>1. </red-bold><span>등록할 API 카테고리 선택.</span><br/>
                        <red-bold>2. </red-bold><span>선택한 API 카테고리에 포함된 API 정보(메소드에 마우스 올리면 API 정보 띄움).</span><br/>
                        <red-bold>3. </red-bold><span>다음 단계 진행.</span><br/>
                    </div>
                    </>
                )}

                {selectedKey==='2' && (
                    <>
                    <br/>
                    <div className="step">
                        <h4>③. </h4><h5>API 선택.</h5><br/>
                        <img src={Step3Api} width="580px" alt="step3_api" className="img" /><br/>
                        <red-bold>1. </red-bold><span>등록할 API 선택.</span><br/>
                        <red-bold>2. </red-bold><span>선택한 API의 정보.</span><br/>
                        <red-bold>3. </red-bold><span>담당자(정) 지정.</span><br/>
                        <red-bold>4. </red-bold><span>담당자(부) 지정.</span><br/>
                        <red-bold>5. </red-bold><span>다음 단계 진행.</span><br/>
                    </div>
                    </>
                )}
                <br/>
                <div className="step">
                    <h4>{checkStepNumber}. </h4><h5>입력 Swagger 데이터 확인.</h5><br/>
                    <img src={selectedKey === '0' ? Step4Check : Step4Check2} width="580px" alt="Step4_check" className="img" /><br/>
                    <red-bold>1. </red-bold><span>입력한 Swagger URL.</span><br/>
                    <red-bold>2. </red-bold><span>입력한 Swagger URL의 서비스 정보.</span><br/>
                    {selectedKey === '0' 
                        ? <><red-bold>3. </red-bold><span>서비스에 포함되어 있는 API카테고리 정보.</span><br/></>
                        : selectedKey === '1'
                            ? <><red-bold>3. </red-bold><span>선택한 API 카테고리에 포함되어 있는 API 정보.</span><br/></>
                            : <><red-bold>3. </red-bold><span>선택한 API 정보.</span><br/></>
                    }
                    
                    <red-bold>4. </red-bold><span>추가, 수정, 삭제되는 API들 확인.</span><br/><br/>
                </div>
                <br/>
                <div className="step">
                    <h4>{checkCUD_StepNumber}. </h4><h5>추가, 수정, 삭제되는 API 확인.</h5><br/>
                    <red-bold> * 직접입력을 제외한 모든 등록은 등록하는 Swagger의 데이터를 기준으로 최신화 됩니다.</red-bold>
                    <img src={Step5CUD} width="580px" alt="step5_create_update_delete" className="img" /><br/>
                    <red-bold>1. </red-bold><span>새롭게 추가되는 API 목록(이전에 등록한 데이터가 없다면 새롭게 추가)</span><br/>
                    <red-bold>2. </red-bold><span>최신화 되는 API 목록(Swagger 데이터 재등록 시 이전에 등록된 데이터 갱신)</span><br/>
                    <red-bold>3. </red-bold><span>삭제되는 API 목록(이전에 등록된 데이터 중 현재 Swagger 데이터에 존재하지 않는 데이터 삭제)</span><br/>
                    <red-bold>4. </red-bold><span>데이터 등록 요청</span><br/><br/>
                </div>







                


            <br/>
            


            {/* <div className="step">
                <h5> 1. Swagger url 또는 json파일 등록 - 파일 등록</h5>
                <img src={Step1File} width="580px" alt="Step1_file" className="img" /><br/>
            </div> */}
            </div>
        </div>
    );
};

export default withRouter(ServiceGuide);