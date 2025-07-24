import axios from 'axios';
import type { AxiosInstance } from 'axios';

// 创建一个axios实例，并进行基础配置
const apiClient: AxiosInstance = axios.create({
    baseURL: 'http://localhost:8088/api', // 后端API的基础路径，请确保端口和路径正确
    timeout: 10000, // 请求超时时间
    headers: {
        'Content-Type': 'application/json',
    }
});

// 为请求添加凭证（Cookie），这对于需要 session 的 Spring Security 是必须的
apiClient.defaults.withCredentials = true;

/*
 * 注意：这里我们没有设置请求拦截器来添加 token，
 * 因为基于 Session 的认证，浏览器会自动处理 Cookie 的发送。
 * 如果未来改为 JWT Token 认证，则需要在这里添加请求拦截器。
 */


// --- 用户认证相关 API ---

/**
 * 登录请求
 * @param credentials - 包含 username 和 password 的对象
 */
export const login = (credentials: any) => {
    return apiClient.post('/users/login', credentials);
};


// --- 用户管理相关 API ---

/**
 * 获取所有用户列表
 */
export const fetchUsers = () => {
    return apiClient.get('/users');
};

/**
 * 添加新用户
 * @param user - 用户信息对象
 */
export const addUser = (user: any) => {
    return apiClient.post('/users', user);
};

/**
 * 更新指定ID的用户信息
 * @param id - 用户ID
 * @param user - 新的用户信息对象
 */
export const updateUser = (id: number, user: any) => {
    return apiClient.put(`/users/${id}`, user);
};

/**
 * 删除指定ID的用户
 * @param id - 用户ID
 */
export const deleteUser = (id: number) => {
    return apiClient.delete(`/users/${id}`);
};


// --- 客户管理相关 API ---

/**
 * 获取所有客户列表
 */
export const fetchCustomers = () => {
    return apiClient.get('/customers');
};

/**
 * 获取指定客户的完整画像
 * @param id - 客户ID
 */
export const fetchCustomerProfile = (id: number) => {
    return apiClient.get(`/customers/${id}/profile`);
};

/**
 * 添加新客户
 * @param customer - 客户信息对象
 */
export const addCustomer = (customer: any) => {
    return apiClient.post('/customers', customer);
};

/**
 * 更新指定ID的客户信息
 * @param id - 客户ID
 * @param customer - 新的客户信息对象
 */
export const updateCustomer = (id: number, customer: any) => {
    return apiClient.put(`/customers/${id}`, customer);
};

/**
 * 删除指定ID的客户
 * @param id - 客户ID
 */
export const deleteCustomer = (id: number) => {
    return apiClient.delete(`/customers/${id}`);
};