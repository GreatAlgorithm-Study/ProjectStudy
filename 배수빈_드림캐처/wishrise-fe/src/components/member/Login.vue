<template>
    <v-container class="fill-height">
      <v-row align="center" justify="center">
        <v-col cols="12" sm="8" md="4">
          <v-card>
            <v-card-title class="text-center">
              <span class="headline">로그인</span>
            </v-card-title>
            <v-card-text>
              <v-form @submit.prevent="checkInput">
                <v-text-field
                  v-model="id"
                  label="아이디"
                  required
                ></v-text-field>
                <v-text-field
                  v-model="pass"
                  label="비밀번호"
                  type="password"
                  required
                ></v-text-field>
                <v-btn color="warning" type="submit" class="mt-4" block>로그인</v-btn>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  export default {
    name: 'Login',
    data() {
      return {
        id: '',
        pass: ''
      };
    },
    methods: {
      checkInput() {
        if (!this.id) {
          alert('아이디를 입력하세요!');
          return;
        }
        if (!this.pass) {
          alert('비밀번호를 입력하세요!');
          return;
        }
        this.submitForm();
      },
      submitForm() {
        axios.post('http://localhost:8000/api/login', { id: this.id, pass: this.pass })
          .then(response => {
            const data = response.data;
            if (data.success) {
              // 로그인 성공 처리
              sessionStorage.setItem('userid', data.userid);
              sessionStorage.setItem('username', data.username);
              sessionStorage.setItem('userlevel', data.userlevel);
              sessionStorage.setItem('userpoint', data.userpoint);
              this.$router.push('/');
            } else {
              alert(data.message);
            }
          })
          .catch(error => {
            console.error('There was an error!', error);
            alert('로그인 중 오류가 발생했습니다.');
          });
      }
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
  
  .fill-height {
    height: 100vh;
  }
  
  .v-card {
    padding: 20px;
  }
  
  .v-card-title {
    padding-bottom: 0;
  }
  
  .v-btn {
    margin-top: 20px;
    color: #FFD54F;
  }
  </style>
  