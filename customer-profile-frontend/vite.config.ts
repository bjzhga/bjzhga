import { defineConfig } from 'vite'
// 正确的引入方式
import vue from '@vitejs/plugin-vue'

export default defineConfig({
  plugins: [vue()],
  esbuild: {
    target: 'es2015'
  }
})