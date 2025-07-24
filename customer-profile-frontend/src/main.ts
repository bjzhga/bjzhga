// src/main.ts

import { createApp } from 'vue'

// 1. 引入 Element Plus 组件库
import ElementPlus from 'element-plus'
// 2. 引入 Element Plus 的样式文件 (这很可能是你缺失的一行！)
import 'element-plus/dist/index.css'

import App from './App.vue'
import router from './router'

const app = createApp(App)

// 3. 在应用中使用 Element Plus 和路由
app.use(router)
app.use(ElementPlus)

app.mount('#app')