<template>
  <div>
    <section>
      <div id="main_content">
        <div id="login_box">
          <h2>소셜 로그인</h2>

          <!-- 구글 소셜 로그인 버튼 -->
          <button @click="loginWithGoogle" class="btn btn-danger">Google로 로그인</button>

          <div v-if="errorMessage" class="error-message">{{ errorMessage }}</div>
        </div> <!-- login_box -->
      </div> <!-- main_content -->
    </section>
  </div>
</template>

<script>
export default {
  name: 'JoinMember',
  data() {
    return {
      errorMessage: '', // 로그인 오류 메시지를 저장할 변수
    };
  },
  methods: {
    loginWithGoogle() {
      try {
        // 1. OAuth2 로그인 페이지로 리다이렉트
        const providerUrl = 'http://localhost:8081/oauth2/authorization/google';
        window.location.href = providerUrl; // 구글 로그인 인증 페이지로 이동
      } catch (error) {
        this.errorMessage = '로그인 중 오류가 발생했습니다.'; // 오류 메시지 처리
      }
    },

  //   async checkLoginStatus() {
  //     try {
  //       const response = await axios.get('/auth/success', { withCredentials: true }); // 백엔드에서 로그인 상태 확인
  //       if (response.data) {
  //       this.$router.push({ name: 'MainHome' });    // 홈 화면으로 리다이렉트
  //     }
  //     } catch (error) {
  //       console.error('로그인 상태 확인 오류:', error);
  //       this.errorMessage = '로그인 상태 확인 실패';
  //     }
  //   }
  // },

  async fetchLoginResponse() {
      try {
        // 2. 백엔드의 '/auth/success'에서 JSON 응답 받기
        const response = await axios.get('http://localhost:8081/auth/success', {
          withCredentials: true, // 쿠키 포함
        });

        // 3. JSON 응답에서 필요한 데이터 추출
        const { accessToken, refreshToken, username, userId } = response.data;

        // 4. 액세스 토큰과 리프레시 토큰 저장 (예: LocalStorage)
        localStorage.setItem('accessToken', accessToken);
        localStorage.setItem('refreshToken', refreshToken);

        // 5. 메인 페이지로 리다이렉트
        this.$router.push({ name: 'MainHome' });
      } catch (error) {
        console.error('로그인 응답 처리 오류:', error);
        this.errorMessage = '로그인 결과를 가져오는 중 오류가 발생했습니다.';
      }
    }
  },


  mounted() {
    // this.checkLoginStatus(); // 컴포넌트가 마운트될 때 로그인 상태 확인
    
    // 컴포넌트가 마운트될 때 로그인 상태를 확인하거나 응답을 처리
    this.fetchLoginResponse();
  }
};
</script>

<style scoped>
@font-face {
  font-family: 'Dolbomche_R';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2104@1.0/Dolbomche_R.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

div {
  font-family: 'Dolbomche_R';
}

/* 로그인 박스 스타일 */
#login_box {
  width: 800px;
  margin: 0 auto;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
}

#login_box h2 {
  margin: 10px 0 30px 0;
  font-size: 24px;
  color: #333;
}

.btn {
  padding: 10px 20px;
  font-size: 16px;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

.btn-danger {
  background-color: #db4437;
  color: white;
}

.error-message {
  color: red;
  margin-top: 10px;
}
</style>
