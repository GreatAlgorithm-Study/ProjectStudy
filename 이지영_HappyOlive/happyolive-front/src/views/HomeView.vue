<script setup lang="ts">
import LoginItem from '@/components/LoginItem.vue'
import LogoutHomeItem from '@/components/LogoutHomeItem.vue'
import LoginHomeItem from '@/components/LoginHomeItem.vue'
import { useCounterStore } from '@/stores/counter'
import { jwtDecode } from 'jwt-decode'
import { ref } from 'vue'
import axios from 'axios'

const store = useCounterStore()
const accessToken = localStorage.getItem('accessToken')
const refreshToken = localStorage.getItem('refreshToken')

const jwtTest = function () {
  console.log('>> 보낼 ATK: ' + accessToken)
  axios({
    method: 'get',
    url: `${store.API_URL}/api/auth/jwtTest`,
    headers: {
      Authorization: `Bearer ${accessToken}`,
      'Content-Type': 'application/json',
    },
    withCredentials: true,
  })
    .then((res) => {
      console.log('success send to atk')
    })
    .catch((err) => {
      console.log('fail send to atk', err)
    })
}
</script>

<template>
  <main>
    <RouterLink to="/login">SIGN IN</RouterLink>
    <div>
      <button @click="jwtTest()">
        <h5>jwtTest</h5>
      </button>
    </div>
    <LogoutHomeItem v-if="!accessToken"> </LogoutHomeItem>
    <LoginHomeItem v-if="accessToken"> </LoginHomeItem>
  </main>
</template>
