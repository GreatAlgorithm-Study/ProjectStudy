// Vue 플러그인을 통해 AxiosService 전역 설정
// 모든 컴포넌트에서 this.$axios로 접근 가능

import AxiosService from '../services/axios.service';

const axiosPlugin = {
  install(app) {
      app.config.globalProperties.$axios = AxiosService.Instance; // Axios 인스턴스를 전역 속성으로 추가
  }
};

export default axiosPlugin;