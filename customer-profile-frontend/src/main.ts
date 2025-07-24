import { createApp } from 'vue'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';
import './style.css'
import App from './App.vue'
import router from './router'; // 引入路由

const app = createApp(App);

app.use(Antd);
app.use(router); // 使用路由
app.mount('#app');
