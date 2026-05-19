/**
 * Security Session 认证工具库
 * 基于Cookie/Session的认证方式，无需手动管理Token
 * 浏览器自动携带JSESSIONID Cookie
 */
const Auth = (() => {
    const USER_KEY = 'user_info';

    // ===== 用户信息存储（仅用于前端展示，实际认证依赖服务端Session） =====

    function saveUserInfo(userInfo) {
        sessionStorage.setItem(USER_KEY, JSON.stringify(userInfo));
    }

    function getUserInfo() {
        const data = sessionStorage.getItem(USER_KEY);
        return data ? JSON.parse(data) : null;
    }

    function clearUserInfo() {
        sessionStorage.removeItem(USER_KEY);
    }

    // ===== API 请求（浏览器自动携带Cookie，无需手动设置Authorization头） =====

    async function request(url, options = {}) {
        const headers = { 'Content-Type': 'application/json', ...options.headers };
        // Session模式：浏览器自动携带JSESSIONID Cookie，无需手动设置
        // credentials: 'same-origin' 确保同源请求携带Cookie
        const response = await fetch(url, {
            ...options,
            headers,
            credentials: 'same-origin'
        });

        // 401 表示Session过期
        if (response.status === 401) {
            clearUserInfo();
            window.location.href = '/login.html';
            return Promise.reject(new Error('登录已过期，请重新登录'));
        }

        return response;
    }

    // ===== 登录 =====

    async function login(username, password, rememberMe) {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            credentials: 'same-origin',
            body: JSON.stringify({ username, password, rememberMe })
        });

        if (response.ok) {
            const result = await response.json();
            const data = result.data || {};
            saveUserInfo({ username: data.username, roles: data.roles });
            return { success: true, data };
        } else {
            const error = await response.json().catch(() => ({ message: '登录失败' }));
            return { success: false, message: error.message || '登录失败' };
        }
    }

    // ===== 登出 =====

    async function logout() {
        try {
            await fetch('/api/auth/logout', {
                method: 'POST',
                credentials: 'same-origin'
            });
        } catch (e) {
            console.warn('登出请求失败:', e);
        }
        clearUserInfo();
        window.location.href = '/login.html';
    }

    // ===== 获取当前用户信息（从服务端获取，更准确） =====

    async function fetchCurrentUserInfo() {
        try {
            const response = await fetch('/api/auth/info', {
                credentials: 'same-origin'
            });
            if (response.ok) {
                const result = await response.json();
                const data = result.data || {};
                if (data.username) {
                    saveUserInfo({ username: data.username, roles: data.roles });
                    return data;
                }
            }
        } catch (e) {
            console.warn('获取用户信息失败:', e);
        }
        return null;
    }

    // ===== 页面访问控制 =====

    function requireAuth() {
        // 先检查本地缓存，再尝试从服务端验证
        const userInfo = getUserInfo();
        if (!userInfo) {
            // 本地无缓存，尝试从服务端获取
            fetchCurrentUserInfo().then(data => {
                if (!data || !data.username) {
                    window.location.href = '/login.html';
                }
            });
            // 暂时认为已认证，等fetchCurrentUserInfo完成后处理
            return true;
        }
        return true;
    }

    function redirectIfAuthenticated() {
        const userInfo = getUserInfo();
        if (userInfo) {
            window.location.href = '/index.html';
            return true;
        }
        return false;
    }

    return {
        saveUserInfo, getUserInfo, clearUserInfo,
        request, login, logout, fetchCurrentUserInfo,
        requireAuth, redirectIfAuthenticated
    };
})();
