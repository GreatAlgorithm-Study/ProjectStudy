<template>
  <v-app>
    <BaseHeader />
    <v-main>
      <router-view />
    </v-main>
    <BaseFooter />
  </v-app>
</template>

<script>
import BaseFooter from './components/common/BaseFooter.vue'
import BaseHeader from './components/common/BaseHeader.vue'
import { defineComponent } from 'vue';


export default defineComponent({
  name: 'App',
  components: {
    BaseHeader,
    BaseFooter,
  },
});
</script>
