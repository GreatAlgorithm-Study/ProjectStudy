<script setup lang="ts">
import { useCounterStore } from '@/stores/counter'
import { jwtDecode } from 'jwt-decode'

// JWT 페이로드 구조를 정의하는 인터페이스
interface JwtPayload {
  sub: string
  nickname: string
  email: string
  exp: number
  renewalTime: number
}

const store = useCounterStore()
const accessToken = localStorage.getItem('accessToken')
let userinfo: JwtPayload | null = null // 기본값을 null로 설정
if (accessToken != null) {
  userinfo = jwtDecode<JwtPayload>(accessToken)
}
</script>

<template>
  <div>LOGIN success</div>
  <div>nickname : {{ userinfo.nickname }}</div>
  <div>email : {{ userinfo.email }}</div>
</template>

<style scoped></style>
