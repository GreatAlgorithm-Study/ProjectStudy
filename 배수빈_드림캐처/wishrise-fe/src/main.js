import { createApp } from 'vue'


import App from './App.vue'
import router from './routers';
import vuetify from './plugins/vuetify';
import axiosPlugin from './plugins/axios';

createApp(App)
    .use(vuetify)
    .use(router)
    .use(axiosPlugin)
    .mount('#app')

