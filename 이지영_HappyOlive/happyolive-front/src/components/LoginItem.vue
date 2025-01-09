<script setup lang="ts">
import { useCounterStore } from '@/stores/counter'
import { ref } from 'vue'
import axios from 'axios'
import router from '@/router'
import { storeToRefs } from 'pinia'

const store = useCounterStore()
const connectData = ref('')
const accessToken = localStorage.getItem('accessToken')
const refreshToken = localStorage.getItem('refreshToken')

const snsLogin = (type: string): void => {
  //   store.setCallbackUrl(window.location.href)
  localStorage.setItem('callBackUrl', window.location.href + '')
  window.location.href = `${store.API_URL}/oauth2/authorization/google`
}

const print = () => {
  store.setCallbackUrl(window.location.href)
  console.log(store.CALL_BACK_URL)
}

const loginTest = function () {
  // const atk = `Bearer ${accessToken}`;
  axios({
    method: 'get',
    url: `${store.API_URL}/oauth2/authorization/google`,
  })
    .then((res) => {
      connectData.value = res.data
      console.log(connectData.value)
    })
    .catch((err) => console.log(err))
}
</script>

<template>
  <div class="login-form">
    <h2>Happyolive Login</h2>
    <div>
      <button @click="snsLogin('google')">
        <h5>Google</h5>
      </button>
    </div>
    <div>
      <button @click="loginTest()">
        <h5>loginTest</h5>
      </button>
    </div>
  </div>
</template>

<style scoped></style>
