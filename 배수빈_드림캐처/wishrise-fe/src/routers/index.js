// router/index.js

import { createRouter, createWebHistory } from 'vue-router';
import MainHome from '../components/MainHome.vue';
import BoardList from '../components/board/BoardList.vue';
import CreateBucket from '../components/board/CreateBucket.vue';
import JoinMember from '../components/member/JoinMember.vue';
import ModifyMember from '../components/member/ModifyMember.vue';
import Login from '../components/member/Login.vue';
import  AuthSuccess from '../components/member/AuthSuccess.vue';


const routes = [
  {
    path: '/',
    name: 'MainHome',
    component: MainHome
  },
  {
    path: '/BoardList',
    name: 'BoardList',
    component: BoardList
  },
  {
    path: '/CreateBucket',
    name: 'CreateBucket',
    component: CreateBucket
  },
  {
    path: '/JoinMember',
    name: 'JoinMember',
    component: JoinMember
  },
  {
    path: '/ModifyMember',
    name: 'ModifyMember',
    component: ModifyMember
  },
  {
    path: '/Login',
    name: 'Login',
    component: Login
  },
  {
    path: '/auth/success',
    name: 'AuthSuccess',
    component: AuthSuccess
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes
});

export default router;
