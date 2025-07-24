<template>
  <div>
    <h1>客户列表</h1>
    <a-table :columns="columns" :data-source="customers" row-key="id" :loading="loading">
      <template #bodyCell="{ column, record }">
        <template v-if="column.key === 'action'">
          <a @click="viewProfile(record.id)">查看画像</a>
        </template>
      </template>
    </a-table>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { message } from 'ant-design-vue';
import { fetchCustomers } from '../api';

const router = useRouter();
const customers = ref([]);
const loading = ref(true);

const columns = [
  { title: '客户编号', dataIndex: 'customerNo', key: 'customerNo' },
  { title: '姓名', dataIndex: 'name', key: 'name' },
  { title: '手机号', dataIndex: 'phone', key: 'phone' },
  { title: '职业', dataIndex: 'profession', key: 'profession' },
  { title: '总资产(万元)', dataIndex: 'assetScale', key: 'assetScale' },
  { title: '操作', key: 'action' },
];

onMounted(async () => {
  try {
    const response = await fetchCustomers();
    customers.value = response.data;
  } catch (error) {
    message.error('获取客户列表失败');
  } finally {
    loading.value = false;
  }
});

const viewProfile = (id: number) => {
  router.push(`/customers/${id}`);
};
</script>