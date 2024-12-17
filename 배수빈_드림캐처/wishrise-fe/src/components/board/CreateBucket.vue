<template>
    <div>
      <section>
        <h3>버킷리스트 작성하기</h3>
        <div class="add-form">
          <div class="card-header">
            <h4>BUCKET LIST</h4>
          </div>
  
          <form @submit.prevent="checkInput">
            <div class="goal">
              <label for="goal">{{ userId }}의 버킷리스트</label>
              <input id="goal" v-model="goal" type="text" class="form-control" placeholder="목표를 적어주세요">
            </div>
            <div class="kind">
              <input type="radio" name="kind" id="want-do" value="want-do" v-model="kind" checked>
              <label class="form-check-label" for="want-do">하고싶은 일</label>
              <input type="radio" name="kind" id="want-have" value="want-have" v-model="kind">
              <label class="form-check-label" for="want-have">갖고싶은 것</label>
            </div>
            <div class="goal-date">
              <label for="target_date">목표날짜</label>
              <input id="target_date" v-model="targetDate" type="text" class="form-control" placeholder="언제까지 달성할지 적어주세요">
            </div>
            <div class="how">
              <label for="how">방법</label>
              <input id="how" v-model="how" type="text" class="form-control" placeholder="어떻게 달성할지 알려주세요">
            </div>
            <div class="upload-img">
              <label for="upfile">목표사진 올리기</label>
              <input id="upfile" @change="onFileChange" type="file" class="form-control-file">
            </div>
            <button type="submit" class="btn btn-warning">완료</button>
          </form>
        </div>
      </section>
    </div>
  </template>
  
  <script>
  export default {
    name: 'CreateBucket',
    data() {
      return {
        userId: '', // 사용자 ID를 설정하는 로직을 추가해야 합니다
        goal: '',
        kind: 'want-do',
        targetDate: '',
        how: '',
        file: null,
      };
    },
    methods: {
      checkInput() {
        if (!this.goal) {
          alert('버킷리스트를 입력하세요!');
          this.$refs.goal.focus();
          return;
        }
        if (!this.targetDate) {
          alert('목표날짜를 입력하세요!');
          this.$refs.targetDate.focus();
          return;
        }
        if (!this.how) {
          alert('방법을 입력하세요!');
          this.$refs.how.focus();
          return;
        }
        this.submitForm();
        alert('버킷리스트를 등록하였습니다:)');
      },
      onFileChange(event) {
        this.file = event.target.files[0];
      },
      submitForm() {
        const formData = new FormData();
        formData.append('goal', this.goal);
        formData.append('kind', this.kind);
        formData.append('target_date', this.targetDate);
        formData.append('how', this.how);
        formData.append('upfile', this.file);
  
        axios.post('/api/board-insert', formData)
          .then(response => {
            console.log(response.data);
          })
          .catch(error => {
            console.error('There was an error!', error);
          });
      },
    },
  };
  </script>
  
  <style scoped>
  @font-face {
    font-family: 'Dolbomche_R';
    src: url('https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_2104@1.0/Dolbomche_R.woff') format('woff');
    font-weight: normal;
    font-style: normal;
    font-size: large;
}

div{
    font-family: 'Dolbomche_R';
    font-size: medium;
}

/* 버킷추가 */
.add-form {
    border: 1.5px solid #fed35a;
    border-radius: 20px;
    margin: 30px;
    padding: 30px 50px 50px;
    width: 600px;
    display: inline-block;
    background: #ffedb8;
}

.goal {
    margin: 5px;
    padding: 10px;
}

.kind {
    margin: 5px;
    padding: 10px;
}

.goal-date {
    margin: 5px;
    padding: 10px;
}

.how {
    margin: 5px;
    padding: 10px;
}

.upload-img {
    margin: 5px;
    padding: 10px;
}

.card-header {
    padding: .75rem 1.25rem;
    margin-bottom: 0;
    border-bottom: 1px solid #ffc107;
    background-color: cornsilk;
}

label{
    font-size: 20px;
}

h3{
    font-family: 'Dolbomche_R';
    font-size: 35px;
}
  </style>
  