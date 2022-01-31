import axios from 'axios';
import handleException from 'lib/function/request/handleException.js';

//(담당자(정), 담당자(부), 로그인한 사용자 number, history)
export const authChk = async (managerNo, subManagerNo, checkNo, history) => {
  const request = { id: sessionStorage.getItem('id') };
  let adminChk = '';
  try {
    const response = await axios.post(
      'http://15.165.25.145:9500/user/check',
      request,
    );
    adminChk = response.data.data === '[ROLE_ADMIN]';
} catch (error) {
    handleException(error, history);
}
//console.log('dataChk:',typeof managerNo,typeof subManagerNo,typeof parseInt(checkNo));
//console.log('adminChk:', adminChk);
//console.log('chk;:',(adminChk || checkNo === managerNo || checkNo === subManagerNo))
  if (adminChk || parseInt(checkNo) === managerNo || parseInt(checkNo) === subManagerNo) {
    return true;
  }
  return false;
};
