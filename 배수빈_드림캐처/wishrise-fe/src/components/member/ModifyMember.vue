<template>
    <v-container class="fill-height">
      <v-row align="center" justify="center">
        <v-col cols="12" sm="8" md="6" lg="4">
          <v-card>
            <v-card-title class="text-center">
              <span class="headline">회원 정보수정</span>
            </v-card-title>
            <v-card-text>
              <v-form @submit.prevent="checkInput">
                <!-- 아이디 표시 -->
                <v-row class="mb-4">
                  <v-col cols="4">
                    <v-text-field
                      v-model="userId"
                      label="아이디"
                      readonly
                      outlined
                      dense
                    ></v-text-field>
                  </v-col>
                </v-row>
  
                <!-- 비밀번호 입력 -->
                <v-row class="mb-4">
                  <v-col cols="12">
                    <v-text-field
                      v-model="password"
                      label="비밀번호"
                      type="password"
                      outlined
                      dense
                    ></v-text-field>
                  </v-col>
                </v-row>
  
                <!-- 비밀번호 확인 입력 -->
                <v-row class="mb-4">
                  <v-col cols="12">
                    <v-text-field
                      v-model="passwordConfirm"
                      label="비밀번호 확인"
                      type="password"
                      outlined
                      dense
                    ></v-text-field>
                  </v-col>
                </v-row>
  
                <!-- 이름 입력 -->
                <v-row class="mb-4">
                  <v-col cols="12">
                    <v-text-field
                      v-model="name"
                      label="이름"
                      outlined
                      dense
                    ></v-text-field>
                  </v-col>
                </v-row>
  
                <!-- 이메일 입력 -->
                <v-row class="mb-4">
                  <v-col cols="5">
                    <v-text-field
                      v-model="email1"
                      label="이메일"
                      outlined
                      dense
                    ></v-text-field>
                  </v-col>
                  <v-col cols="7">
                    <v-text-field
                      v-model="email2"
                      label="도메인"
                      outlined
                      dense
                    ></v-text-field>
                  </v-col>
                </v-row>
  
                <!-- 버튼들 -->
                <v-row>
                  <v-col cols="12" class="text-right">
                    <v-btn color="#FFD54F" type="submit" class="mr-2">
                      확인
                    </v-btn>
                    <v-btn color="info" @click="resetForm">
                      취소
                    </v-btn>
                  </v-col>
                </v-row>
              </v-form>
            </v-card-text>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script>
  export default {
    name: 'ModifyMember',
    data() {
      return {
        userId: '', // 서버에서 받아온 사용자 ID
        password: '',
        passwordConfirm: '',
        name: '',
        email1: '',
        email2: '',
      };
    },
    methods: {
      fetchData() {
        // 사용자 정보를 서버에서 받아오는 메서드
        axios.get('/api/member', { params: { id: this.userId } }) // 이 API 엔드포인트는 실제 서버에 맞게 수정
          .then(response => {
            const data = response.data;
            this.userId = data.id;
            this.password = data.pass;
            this.passwordConfirm = data.pass;
            this.name = data.name;
            const email = data.email.split('@');
            this.email1 = email[0];
            this.email2 = email[1];
          })
          .catch(error => {
            console.error('There was an error fetching the member data!', error);
          });
      },
      checkInput() {
        if (!this.password || !this.passwordConfirm || !this.name || !this.email1 || !this.email2) {
          alert('모든 필드를 입력하세요!');
          return;
        }
        if (this.password !== this.passwordConfirm) {
          alert('비밀번호가 일치하지 않습니다!');
          return;
        }
        this.submitForm();
      },
      resetForm() {
        this.fetchData(); // 폼을 초기 데이터로 리셋
      },
      submitForm() {
        const data = {
          id: this.userId,
          pass: this.password,
          name: this.name,
          email: `${this.email1}@${this.email2}`,
        };
        axios.post(`/api/member_modify`, data) // 이 API 엔드포인트는 실제 서버에 맞게 수정
          .then(response => {
            console.log('회원 정보가 수정되었습니다!', response.data);
          })
          .catch(error => {
            console.error('There was an error modifying the member data!', error);
          });
      },
    },
    created() {
      this.fetchData();
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
  
  .fill-height {
    height: 100vh;
  }
  
  .v-card {
    padding: 20px;
  }
  
  .v-card-title {
    padding-bottom: 0;
  }
  
  .v-text-field {
    margin-bottom: 20px;
  }
  
  .v-btn {
    background-color: #FFD54F !important;
    color: #000 !important; /* 버튼 텍스트 색상 조정 */
  }
  
  .v-btn:hover {
    background-color: #ffca28 !important; /* 버튼 호버 색상 조정 */
  }
  </style>
  