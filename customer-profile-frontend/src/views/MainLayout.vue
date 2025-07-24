<template>
  <a-layout style="min-height: 100vh">
    <a-layout-sider v-model:collapsed="collapsed" collapsible>
      <div class="logo">画像系统</div>
      <a-menu v-model:selectedKeys="selectedKeys" theme="dark" mode="inline" @click="handleMenuClick">
        <a-menu-item key="customers">
          <PieChartOutlined />
          <span>客户列表</span>
        </a-menu-item>
        <a-menu-item key="users">
          <TeamOutlined />
          <span>用户管理</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header style="background: #fff; padding: 0 16px; display: flex; justify-content: space-between; align-items: center;">
        <span style="font-weight: bold; font-size: 16px;">客户全景洞察平台</span>
        <div>
          <span>欢迎, {{ username }}</span>
          <a-button type="link" @click="handleLogout">退出登录</a-button>
        </div>
      </a-layout-header>
      <a-layout-content style="margin: 16px">
        <div :style="{ padding: '24px', background: '#fff', minHeight: '360px' }">
          <router-view />
        </div>
      </a-layout-content>
      <a-layout-footer style="text-align: center">
        Customer Profile System ©2024 Created by You
      </a-layout-footer>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue';
import { useRouter, useRoute } from 'vue-router';
import { PieChartOutlined, TeamOutlined } from '@ant-design/icons-vue';

const router = useRouter();
const route = useRoute();

const collapsed = ref<boolean>(false);
const selectedKeys = computed(() => [route.name]);

const user = JSON.parse(sessionStorage.getItem('user') || '{}');
const username = ref(user.username || 'Guest');


const handleMenuClick = ({ key }: { key: string }) => {
  if (key === 'customers') {
    router.push('/customers');
  } else if (key === 'users') {
    router.push('/users');
  }
}

const handleLogout = () => {
  sessionStorage.removeItem('user');
  router.push('/login');
}
</script>

<style scoped>
.logo {
  height: 32px;
  margin: 16px;
  background: rgba(255, 255, 255, 0.3);
  color: white;
  text-align: center;
  line-height: 32px;
  font-weight: bold;
}
</style>