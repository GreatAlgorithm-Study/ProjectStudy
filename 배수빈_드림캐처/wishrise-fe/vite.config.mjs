import { defineConfig } from 'vite';
import vue from '@vitejs/plugin-vue';
import vuetify from 'vite-plugin-vuetify';
import { resolve } from 'path';

export default defineConfig({
  root: resolve(__dirname, '.'),  // 프로젝트 루트 디렉토리 설정
  plugins: [
    vue(),
    vuetify({
      autoImport: true,
    }),
  ],
  build: {
    outDir: '../wishrise/src/main/resources/static', // Build Directory
    emptyOutDir: true,  // 빌드 디렉토리를 비움
    rollupOptions: {
      external: ['@mdi/font'], // @mdi/font 패키지를 외부로 설정
    },
  },
  server: {
    // port:8080,
    proxy: {
      '/api': {
        target: 'http://localhost:8081',
        changeOrigin: true,
        withCredentials: true,  // 쿠키와 자격 증명을 함께 전송
      },
    },
  },
  publicDir: 'public',  // public 폴더 지정
});
