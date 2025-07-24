<template>
  <div class="login-container">
    <el-card class="login-card">
      <template #header>
        <div class="card-header">
          <span>客户画像系统登录</span>
        </div>
      </template>
      <el-form :model="loginForm" label-width="80px">
        <el-form-item label="用户名" prop="username">
          <el-input v-model="loginForm.username" placeholder="请输入用户名" :prefix-icon="User" clearable></el-input>
        </el-form-item>
        <el-form-item label="密码" prop="password">
          <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password @keyup.enter="handleLogin"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleLogin" class="login-button">登 录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup lang="ts">
import { reactive } from 'vue';
import { useRouter } from 'vue-router';
// 从 element-plus 中导入 ElMessage 提示组件和图标
import { ElMessage } from 'element-plus';
import { User, Lock } from '@element-plus/icons-vue';
// 【关键修复】使用命名空间导入，将 api/index.ts 中所有导出的函数打包成一个 api 对象
import * as api from '../api/index';

// 创建路由实例，用于登录成功后跳转页面
const router = useRouter();

// 创建响应式的登录表单数据
const loginForm = reactive({
  username: 'admin',
  password: '123'
});

// 处理登录逻辑的函数
const handleLogin = async () => {
  // 1. 前端基础校验
  if (!loginForm.username || !loginForm.password) {
    ElMessage.warning('请输入用户名和密码');
    return;
  }

  // 2. 调用后端 API
  try {
    // 【关键修复】使用 api.login() 调用登录接口
    const response = await api.login(loginForm);

    // 3. 根据后端返回结果处理
    // 假设后端成功返回的数据结构是 { code: 200, message: '...', data: ... }
    if (response.data && response.data.code === 200) {
      ElMessage.success('登录成功！');
      // 登录成功，跳转到主面板页面
      await router.push('/dashboard');
    } else {
      // 业务失败，例如用户名或密码错误
      ElMessage.error(response.data.message || '用户名或密码错误');
    }
  } catch (error) {
    // 请求异常，例如网络错误或后端服务未启动
    console.error('登录请求失败:', error);
    ElMessage.error('登录请求失败，请检查网络或联系管理员');
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f0f2f5;
}

.login-card {
  width: 400px;
}

.card-header {
  text-align: center;
  font-size: 20px;
  font-weight: bold;
}

.login-button {
  width: 100%;
}
</style>