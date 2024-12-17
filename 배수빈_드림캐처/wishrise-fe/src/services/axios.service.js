// service class를 통해 axios 접근

import axios from 'axios';

class AxiosService {
    static Instance = axios.create({
        timeout: 10000,
        withCredentials: true,  // 쿠키와 자격 증명 함께 전송
    });
}

export default AxiosService;