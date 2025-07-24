<template>
  <div>
    <h1>用户管理</h1>
    <a-button type="primary" @click="showAddModal" style="margin-bottom: 16px">添加用户</a-button>
    <a-table :columns="columns" :data-source="users" row-key="id" :loading="loading">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a-popconfirm
              title="确定要删除这个用户吗?"
              ok-text="是的"
              cancel-text="不了"
              @confirm="handleDelete(record.id)"
          >
            <a>删除</a>
          </a-popconfirm>
        </template>
      </template>
    </a-table>

    <a-modal v-model:open="isModalVisible" title="添加新用户" @ok="handleAddUser">
      <a-form :model="newUser">
        <a-form-item label="用户名">
          <a-input v-model:value="newUser.username" />
        </a-form-item>
        <a-form-item label="密码">
          <a-input-password v-model:value="newUser.password" />
        </a-form-item>
        <a-form-item label="角色">
          <a-input v-model:value="newUser.role" placeholder="USER 或 ADMIN"/>
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue';
import { message } from 'ant-design-vue';
import { fetchUsers, addUser, deleteUser } from '../api';

const users = ref([]);
const loading = ref(true);
const isModalVisible = ref(false);
const newUser = reactive({ username: '', password: '', role: 'USER' });

const columns = [
  { title: 'ID', dataIndex: 'id', key: 'id' },
  { title: '用户名', dataIndex: 'username', key: 'username' },
  { title: '角色', dataIndex: 'role', key: 'role' },
  { title: '创建时间', dataIndex: 'createdAt', key: 'createdAt' },
  { title: '操作', key: 'action' },
];

const loadUsers = async () => {
  loading.value = true;
  try {
    const response = await fetchUsers();
    users.value = response.data;
  } catch (error) {
    message.error('获取用户列表失败');
  } finally {
    loading.value = false;
  }
};

onMounted(loadUsers);

const showAddModal = () => {
  isModalVisible.value = true;
};

const handleAddUser = async () => {
  try {
    await addUser(newUser);
    message.success('用户添加成功');
    isModalVisible.value = false;
    // 重置表单
    Object.assign(newUser, { username: '', password: '', role: 'USER' });
    loadUsers(); // 刷新列表
  } catch (error) {
    message.error('添加用户失败');
  }
};

const handleDelete = async (id: number) => {
  try {
    await deleteUser(id);
    message.success('用户删除成功');
    loadUsers(); // 刷新列表
  } catch (error) {
    message.error('删除用户失败');
  }
};
</script>