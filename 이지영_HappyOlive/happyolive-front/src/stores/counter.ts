import { ref, computed } from 'vue'
import { defineStore } from 'pinia'
import axios from 'axios'

// export const useCounterStore = defineStore('counter', () => {
//   const API_URL = import.meta.env.VITE_API_URL as string
//   const CALL_BACK_URL = ref('/')

//   return { API_URL, CALL_BACK_URL }
// })
export const useCounterStore = defineStore('counter', {
  state: () => ({
    API_URL: import.meta.env.VITE_API_URL as string,
    CALL_BACK_URL: '/' as string,
  }),
  actions: {
    setCallbackUrl(url: string): void {
      this.CALL_BACK_URL = url
    },
  },
})
