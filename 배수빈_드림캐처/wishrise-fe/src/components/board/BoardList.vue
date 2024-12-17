<template>
    <div>
      <section>
        <div id="board_box">
          <h3>버킷리스트 둘러보기</h3>
          <p>다른사람들은 어떤 버킷리스트를 가지고 있나 둘러보아요:)</p>
          <div id="board_list">
            <div v-for="item in paginatedList" :key="item.num" class="card">
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
      </section>
    </div>
  </template>
  
  <script>  
  export default {
    name: 'BoardList',
    data() {
      return {
        list: [],
        currentPage: 1,
        scale: 6,
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
      //   axios.get('/api/board-list?kind=want-do')
      //     .then(response => {
      //       this.list = response.data.map(item => {
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
  
  #board_box {
    font-family: 'Dolbomche_R';
  }
  
  .card {
    width: 400px;
    display: inline-table;
    margin: 30px;
    padding: 10px;
    font-family: 'Dolbomche_R';
    background: lavenderblush;
  }
  </style>
  