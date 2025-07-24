<template>
  <div class="dashboard-container">
    <!-- 操作区域 -->
    <div class="actions-bar">
      <el-button type="primary" :icon="Plus" @click="handleOpenDialog()">新增客户</el-button>
    </div>

    <!-- 客户数据表格 -->
    <el-table :data="customerList" v-loading="loading" style="width: 100%">
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="name" label="姓名" width="180" />
      <el-table-column prop="gender" label="性别" width="100" />
      <el-table-column prop="phone" label="电话" width="180" />
      <el-table-column prop="email" label="邮箱" />
      <el-table-column label="操作" width="200" fixed="right">
        <template #default="scope">
          <el-button size="small" :icon="Edit" @click="handleOpenDialog(scope.row)">编辑</el-button>
          <el-button size="small" type="danger" :icon="Delete" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <!-- 新增/编辑客户弹窗 -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="resetForm">
      <el-form :model="customerForm" label-width="80px">
        <el-form-item label="姓名">
          <el-input v-model="customerForm.name" placeholder="请输入客户姓名" />
        </el-form-item>
        <el-form-item label="性别">
          <el-input v-model="customerForm.gender" placeholder="请输入性别" />
        </el-form-item>
        <el-form-item label="电话">
          <el-input v-model="customerForm.phone" placeholder="请输入电话号码" />
        </el-form-item>
        <el-form-item label="邮箱">
          <el-input v-model="customerForm.email" placeholder="请输入电子邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="handleSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage, ElMessageBox } from 'element-plus';
import { Plus, Edit, Delete } from '@element-plus/icons-vue';
// 【关键修复】使用命名空间导入，将所有 API 函数打包成一个 api 对象
import * as api from '../api/index';

// --- 响应式状态定义 ---
const loading = ref(true); // 表格加载状态
const customerList = ref([]); // 客户列表数据
const dialogVisible = ref(false); // 控制弹窗显示
const dialogTitle = ref(''); // 弹窗标题
const customerForm = reactive({ // 客户表单数据
  id: null,
  name: '',
  gender: '',
  phone: '',
  email: ''
});

// --- API 调用与逻辑处理 ---

// 1. 获取客户列表
const getCustomerList = async () => {
  loading.value = true;
  try {
    const response = await api.fetchCustomers();
    customerList.value = response.data.data; // 请根据你后端返回的实际数据结构调整
  } catch (error) {
    console.error("获取客户列表失败:", error);
    ElMessage.error("获取客户列表失败！");
  } finally {
    loading.value = false;
  }
};

// 2. 打开新增/编辑弹窗
const handleOpenDialog = (customer?: any) => {
  if (customer) {
    // 编辑模式
    dialogTitle.value = '编辑客户';
    // 将行数据填充到表单中
    Object.assign(customerForm, customer);
  } else {
    // 新增模式
    dialogTitle.value = '新增客户';
    // 重置表单（在关闭时也会重置，但这里更保险）
    resetForm();
  }
  dialogVisible.value = true;
};

// 3. 提交表单（新增或更新）
const handleSubmit = async () => {
  try {
    if (customerForm.id) {
      // ID 存在，执行更新操作
      await api.updateCustomer(customerForm.id, customerForm); // **注意：此函数需在 api/index.ts 中添加**
      ElMessage.success('客户信息更新成功！');
    } else {
      // ID 不存在，执行新增操作
      await api.addCustomer(customerForm); // **注意：此函数需在 api/index.ts 中添加**
      ElMessage.success('新客户添加成功！');
    }
    dialogVisible.value = false; // 关闭弹窗
    await getCustomerList(); // 重新加载列表数据
  } catch (error) {
    console.error("操作失败:", error);
    ElMessage.error("操作失败，请重试！");
  }
};

// 4. 删除客户
const handleDelete = (id: number) => {
  ElMessageBox.confirm('确定要删除这位客户吗？此操作无法撤销。', '警告', {
    confirmButtonText: '确定删除',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    // 用户点击了确认
    try {
      await api.deleteCustomer(id); // **注意：此函数需在 api/index.ts 中添加**
      ElMessage.success('删除成功！');
      await getCustomerList(); // 重新加载列表数据
    } catch (error) {
      console.error("删除失败:", error);
      ElMessage.error('删除失败，请重试！');
    }
  }).catch(() => {
    // 用户点击了取消
    ElMessage.info('已取消删除');
  });
};

// 5. 重置表单
const resetForm = () => {
  customerForm.id = null;
  customerForm.name = '';
  customerForm.gender = '';
  customerForm.phone = '';
  customerForm.email = '';
};

// --- 生命周期钩子 ---
// 组件挂载后，立即获取客户列表
onMounted(() => {
  getCustomerList();
});
</script>

<style scoped>
.dashboard-container {
  padding: 20px;
}

.actions-bar {
  margin-bottom: 20px;
}
</style>