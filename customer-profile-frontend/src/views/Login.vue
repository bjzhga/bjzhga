<template>
  <div class="login-container">
    <a-card title="客户画像系统登录" :style="{ width: '350px' }">
      <a-form :model="formState" @finish="handleLogin">
        <a-form-item
            name="username"
            :rules="[{ required: true, message: '请输入用户名!' }]"
        >
          <a-input v-model:value="formState.username" placeholder="用户名 (admin)">
            <template #prefix><UserOutlined /></template>
          </a-input>
        </a-form-item>
        <a-form-item
            name="password"
            :rules="[{ required: true, message: '请输入密码!' }]"
        >
          <a-input-password v-model:value="formState.password" placeholder="密码 (admin123)">
            <template #prefix><LockOutlined /></template>
          </a-input-password>
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" block :loading="loading">
            登 录
          </a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { UserOutlined, LockOutlined } from '@ant-design/icons-vue';
import { login } from '../api';

const router = useRouter();
const formState = reactive({
  username: 'admin',
  password: 'admin123',
});
const loading = ref(false);

const handleLogin = async () => {
  loading.value = true;
  try {
    const response = await login(formState);
    // 登录成功，将用户信息存入sessionStorage
    sessionStorage.setItem('user', JSON.stringify(response.data));
    message.success('登录成功!');
    router.push('/'); // 跳转到首页
  } catch (error) {
    message.error('用户名或密码错误！');
    console.error(error);
  } finally {
    loading.value = false;
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
</style>