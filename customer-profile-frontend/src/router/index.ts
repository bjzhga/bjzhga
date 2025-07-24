import { createRouter, createWebHistory } from 'vue-router';
import Login from '../views/Login.vue';
import MainLayout from '../views/MainLayout.vue';
import UserManagement from '../views/UserManagement.vue';
import CustomerList from '../views/CustomerList.vue';
import CustomerDetail from '../views/CustomerDetail.vue';

const routes = [
    {
        path: '/login',
        name: 'Login',
        component: Login,
    },
    {
        path: '/',
        component: MainLayout,
        redirect: '/customers', // 默认重定向到客户列表
        children: [
            {
                path: 'users',
                name: 'UserManagement',
                component: UserManagement,
            },
            {
                path: 'customers',
                name: 'CustomerList',
                component: CustomerList,
            },
            {
                path: 'customers/:id', // 动态路由，:id 是客户ID
                name: 'CustomerDetail',
                component: CustomerDetail,
                props: true, // 将路由参数作为props传递给组件
            }
        ],
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 路由守卫：检查是否登录
router.beforeEach((to, _, next) => {
    const loggedIn = sessionStorage.getItem('user');
    if (to.name !== 'Login' && !loggedIn) {
        next({ name: 'Login' });
    } else {
        next();
    }
});


export default router;