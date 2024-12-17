<template>
  <div>
    <div class="todayList">
      <h3>이루어진 버킷리스트</h3>
      <p>다른사람들이 이루어낸 버킷리스트입니다:)</p>
    </div>

    <div id="board_list">
      <div v-for="(item, index) in paginatedList" :key="index" class="card">
        <p>{{ item.id }}님의 버킷리스트</p>
        <img class="card-img-top" :src="`../data/${item.file_image}`" alt="Card image cap">
        <div class="card-body">
          <h3 class="card-title">{{ item.goal }}</h3>
          <p class="card-text">{{ item.how }}</p>
        </div>
        <div class="card-footer">
          <small class="text-muted">등록일: {{ item.regist_day }}</small><br>
          <small class="text-muted">예정일: {{ item.target_day }}</small>
        </div>
      </div>
    </div>

    <ul id="page_num">
      <li v-if="currentPage > 1">
        <button @click="currentPage--">◀ 이전</button>
      </li>
      <li v-for="page in totalPages" :key="page">
        <button @click="currentPage = page" :class="{ active: currentPage === page }">{{ page }}</button>
      </li>
      <li v-if="currentPage < totalPages">
        <button @click="currentPage++">다음 ▶</button>
      </li>
    </ul>

    <div v-if="isUserLoggedIn">
      <button @click="navigateToAddBucket" class="btn btn-warning btn-sm">버킷리스트 작성하러가기</button>
    </div>
    <div v-else>
      <button @click="alertLogin" class="btn btn-warning btn-sm">버킷리스트 작성하러가기</button>
    </div>
  </div>
</template>

<script>
export default {
  name: 'MainHome',
  data() {
    return {
      list: [],
      currentPage: 1,
      scale: 10,
      isUserLoggedIn: false, // 로그인 상태를 확인하는 로직에 따라 설정합니다
    };
  },
  computed: {
    totalPages() {
      return Math.ceil(this.list.length / this.scale);
    },
    paginatedList() {
      const start = (this.currentPage - 1) * this.scale;
      const end = start + this.scale;
      return this.list.slice(start, end);
    },
  },
  methods: {
    // fetchData() {
    //   // 실제 데이터 가져오는 로직을 여기에 추가합니다. 예: axios나 fetch API를 사용
    //   fetch('/api/board-list?kind=want-do')
    //     .then(response => response.json())
    //     .then(data => {
    //       this.list = data.map(item => {
    //         return {
    //           ...item,
    //           file_image: item.file_name ? item.file_copied : ' ',
    //         };
    //       });
    //     });
    // },
    navigateToAddBucket() {
      this.$router.push('/add_bucket');
    },
    alertLogin() {
      alert('로그인 후 이용해 주세요!');
    },
  },
  // created() {
  //   this.fetchData();
  // },
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