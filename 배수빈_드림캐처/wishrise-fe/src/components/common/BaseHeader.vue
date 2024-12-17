<template>
  <div>
    <div align="right" class="logn">  
      <template v-if="!userId">
        <router-link to="/Login">로그인</router-link>
        <router-link to="/JoinMember">회원가입</router-link>
      </template>
      <template v-else>
        <a>{{ logged }}</a>
        <a href="logout.php">로그아웃</a>
        <router-link to="/ModifyMember">정보 수정</router-link>
      </template>
     
    </div>

    <div class="text-center">
      <img src="../../assets/CatLogo.jpg" class="logo" alt="Logo">
    </div>
    <div>
      <h1>BucketList</h1>
    </div>
    <nav class="gnb-menu">
      <router-link to="/">홈🏠</router-link>
      <router-link to="/BoardList">버킷리스트 둘러보기👀</router-link>
      <router-link to="/CreateBucket">버킷리스트 추가하기🛒</router-link>
      <router-link to="/MyBucket">나의 버킷리스트📃</router-link>
    </nav>
  </div>
</template>

<script>
export default {
  name: 'BaseHeader',
  data() {
    return {
      userId: '',
      userName: '',
      userLevel: '',
      userPoint: '',
    };
  },
  computed: {
    logged() {
      return `${this.userName} (${this.userId})님 [Level:${this.userLevel}, Point:${this.userPoint}]`;
    },
  },
  methods: {
    fetchSessionData() {
      axios.get('/api/session') // 적절한 API 엔드포인트로 변경하세요
        .then(response => {
          const data = response.data;
          this.userId = data.userid || '';
          this.userName = data.username || '';
          this.userLevel = data.userlevel || '';
          this.userPoint = data.userpoint || '';
        })
        .catch(error => {
          console.error('세션 데이터를 가져오는 중 오류 발생:', error);
        });
    },
  },
  mounted() {
    this.fetchSessionData();
  },
};
</script>

<style scoped>
@font-face {
  font-family: 'Dolbomche_R';
  src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2104@1.0/Dolbomche_R.woff') format('woff');
  font-weight: normal;
  font-style: normal;
}

.logo {
  height: 130px;
}

.gnb-menu {
  display: flex;
  justify-content: space-around;
  align-items: center;
  height: 7vh;
  border: solid lightgrey;
  border-radius: 30px;
  margin: 30px 0px 50px;
}

.gnb-menu a {
  font-size: 24px;
  color: black;
  text-decoration: none;
  position: relative;
  padding: 6px 12px;
  font-family: 'Dolbomche_R';
}

.gnb-menu a::after {
  content: "";
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  width: 0;
  height: 4px;
  background: #FFD54F;
  transition: all .5s ease-out;
}

.gnb-menu a:hover::after {
  width: 100%;
}

.logn a {
  font-size: 17px;
  color: black;
  text-align: right;
  padding: 6px 12px;
}

h1 {
  text-align: center;
  font-family: 'Dolbomche_R';
}

.text-center img {
  display: block;
  margin: 0 auto;
}
</style>
