import axios from 'axios';

// 创建一个axios实例，并配置基础URL
const apiClient = axios.create({
    baseURL: 'http://localhost:8088/api', // 后端API的基础路径
    headers: {
        'Content-Type': 'application/json',
    }
});

// 为请求添加凭证，这对于需要session的Spring Security是必须的
apiClient.defaults.withCredentials = true;

// 登录请求
export const login = (credentials: any) => {
    return apiClient.post('/users/login', credentials);
};

// 获取所有用户
export const fetchUsers = () => {
    return apiClient.get('/users');
};

// 添加用户
export const addUser = (user: any) => {
    return apiClient.post('/users', user);
};

// 删除用户
export const deleteUser = (id: number) => {
    return apiClient.delete(`/users/${id}`);
};

// 获取所有客户
export const fetchCustomers = () => {
    return apiClient.get('/customers');
};

// 获取指定客户的完整画像
export const fetchCustomerProfile = (id: number) => {
    return apiClient.get(`/customers/${id}/profile`);
};
