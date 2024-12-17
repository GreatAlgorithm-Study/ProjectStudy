<template>
  <div>
    <h1>로그인 성공 중...</h1>
  </div>
</template>

<script>
export default {
  name: 'AuthSuccess',
  mounted() {
    // 로그인 성공 후 처리
    this.fetchLoginResponse();
  },
  methods: {
    async fetchLoginResponse() {
      try {
        // 1. 백엔드에서 응답을 받아오기 (쿠키 포함)
        const response = await axios.get('http://localhost:8081/auth/success', {
          withCredentials: true, // 쿠키 포함
        });

        // 2. 응답에서 JSON 데이터 추출
        const { accessToken, refreshToken } = response.data;

        // 3. 액세스 토큰과 리프레시 토큰 저장 (예: LocalStorage)
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);

        // 4. 메인 페이지로 리다이렉트
        this.$router.push({ name: 'MainHome' });
      } catch (error) {
        console.error('로그인 응답 처리 오류:', error);
        this.$router.push({ name: 'Login' });
      }
    },
  }
};
</script>

<style scoped>
h1 {
  color: #333;
}
</style>
