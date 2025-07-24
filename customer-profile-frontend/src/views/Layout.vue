<template>
  <el-container class="layout-container">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="main-aside">
      <div class="logo-container">
        <!-- 这里使用了我们安装的图标 -->
        <i-ep-data-analysis class="logo-icon" />
        <span class="logo-text">客户画像系统</span>
      </div>
      <el-menu
          :default-active="$route.path"
          class="main-menu"
          router
      >
        <el-menu-item index="/main/dashboard">
          <i-ep-house class="menu-icon"/>
          <span>客户画像总览</span>
        </el-menu-item>
        <el-menu-item index="/main/tags">
          <i-ep-price-tag class="menu-icon"/>
          <span>标签管理</span>
        </el-menu-item>
        <el-menu-item index="/main/users">
          <i-ep-user class="menu-icon"/>
          <span>用户管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <el-container>
      <!-- 顶栏 -->
      <el-header class="main-header">
        <div class="header-left">
          <span class="header-title">{{ $route.meta.title }}</span>
        </div>
        <div class="header-right">
          <el-dropdown>
            <span class="user-info">
              <el-avatar size="small" :icon="UserFilled" style="margin-right: 8px;" />
              欢迎您, {{ username }}
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <!-- 内容区域 -->
      <el-main class="main-content">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { ElMessageBox } from 'element-plus';
import { ArrowDown, UserFilled, SwitchButton } from '@element-plus/icons-vue'

const router = useRouter();
// 从 localStorage 获取用户名，如果取不到则显示 '访客'
const username = ref(localStorage.getItem('username') || '访客');

const logout = () => {
  ElMessageBox.confirm('您确定要退出登录吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(() => {
    localStorage.removeItem('token');
    localStorage.removeItem('username'); // 同时移除用户名
    router.push('/login');
  }).catch(() => {
    // 用户点击取消，不做任何事
  });
};
</script>

<style scoped>
.layout-container {
  height: 100vh;
}

/* 侧边栏样式 */
.main-aside {
  background-color: #001529; /* 深蓝色背景 */
  color: #fff;
  transition: width 0.28s;
}

.logo-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 60px;
  background-color: #002140;
}

.logo-icon {
  font-size: 28px;
  color: #409EFF;
  margin-right: 10px;
}

.logo-text {
  font-size: 20px;
  font-weight: 600;
  color: #fff;
}

.main-menu {
  border-right: none;
  background-color: #001529;
}

.el-menu-item {
  color: #a6adb4;
  font-size: 14px;
}

.menu-icon {
  margin-right: 10px;
  font-size: 18px;
}

.el-menu-item:hover {
  background-color: #000c17;
  color: #fff;
}

.el-menu-item.is-active {
  color: #fff !important;
  background-color: #409EFF !important;
}

/* 顶栏样式 */
.main-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  background-color: #fff;
  box-shadow: 0 1px 4px rgba(0, 21, 41, 0.08);
  padding: 0 20px;
}

.header-title {
  font-size: 18px;
  color: #333;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  cursor: pointer;
  display: flex;
  align-items: center;
}

/* 内容区域样式 */
.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>