<script lang="ts">
import { defineComponent, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { useCounterStore } from '@/stores/counter'

export default defineComponent({
  name: 'LoginSuccess',
  setup() {
    const route = useRoute()
    const store = useCounterStore()

    onMounted(() => {
      // Ensure the tokens are present in the route params
      const accessToken = route.query.atk as string
      const refreshToken = route.query.rtk as string

    //   console.log(accessToken)
    //   console.log(refreshToken)

      const url = localStorage.getItem('callBackUrl') as string
      localStorage.removeItem('callBackUrl') // 로컬 스토리지에 저장된 callBackUrl 삭제
      console.log(url)

      if (accessToken && refreshToken) {
        // Store the tokens in localStorage
        localStorage.setItem('accessToken', accessToken)
        localStorage.setItem('refreshToken', refreshToken)
      }

      // Redirect to the root path
      window.location.href = url
    })

    return {}
  },
})
</script>

<style scoped>
/* Your styles here */
</style>
