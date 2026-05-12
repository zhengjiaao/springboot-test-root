/**
 * JWT 认证工具库
 * 封装Token存储、API请求、自动刷新、登出等公共逻辑
 */
const Auth = (() => {
    const TOKEN_KEY = 'access_token';
    const REFRESH_KEY = 'refresh_token';
    const USER_KEY = 'user_info';

    // ===== Token 存储 =====

    function saveTokens(accessToken, refreshToken) {
        localStorage.setItem(TOKEN_KEY, accessToken);
        localStorage.setItem(REFRESH_KEY, refreshToken);
    }

    function getAccessToken() {
        return localStorage.getItem(TOKEN_KEY);
    }

    function getRefreshToken() {
        return localStorage.getItem(REFRESH_KEY);
    }

    function saveUserInfo(userInfo) {
        localStorage.setItem(USER_KEY, JSON.stringify(userInfo));
    }

    function getUserInfo() {
        const data = localStorage.getItem(USER_KEY);
        return data ? JSON.parse(data) : null;
    }

    function clearAll() {
        localStorage.removeItem(TOKEN_KEY);
        localStorage.removeItem(REFRESH_KEY);
        localStorage.removeItem(USER_KEY);
    }

    function isAuthenticated() {
        return !!getAccessToken();
    }

    // ===== API 请求（自动携带Token、401自动刷新） =====

    async function request(url, options = {}) {
        const token = getAccessToken();
        const headers = { 'Content-Type': 'application/json', ...options.headers };

        if (token) {
            headers['Authorization'] = 'Bearer ' + token;
        }

        const response = await fetch(url, { ...options, headers });

        // Token过期，尝试刷新
        if (response.status === 401 && token) {
            const refreshed = await tryRefresh();
            if (refreshed) {
                headers['Authorization'] = 'Bearer ' + getAccessToken();
                return fetch(url, { ...options, headers });
            } else {
                handleExpired();
                return Promise.reject(new Error('登录已过期，请重新登录'));
            }
        }

        return response;
    }

    async function tryRefresh() {
        const refreshToken = getRefreshToken();
        if (!refreshToken) return false;

        try {
            const response = await fetch('/api/auth/refresh', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + refreshToken
                }
            });

            if (response.ok) {
                const data = await response.json();
                saveTokens(data.accessToken, data.refreshToken);
                saveUserInfo({ username: data.username, roles: data.roles });
                return true;
            }
        } catch (e) {
            console.error('Token刷新失败:', e);
        }
        return false;
    }

    function handleExpired() {
        clearAll();
        window.location.href = '/login.html';
    }

    // ===== 登录 =====

    async function login(username, password) {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ username, password })
        });

        if (response.ok) {
            const data = await response.json();
            saveTokens(data.accessToken, data.refreshToken);
            saveUserInfo({ username: data.username, roles: data.roles });
            return { success: true, data };
        } else {
            const error = await response.json().catch(() => ({ message: '登录失败' }));
            return { success: false, message: error.message || '登录失败' };
        }
    }

    // ===== 登出 =====

    async function logout() {
        const token = getAccessToken();
        if (token) {
            try {
                await fetch('/api/auth/logout', {
                    method: 'POST',
                    headers: { 'Authorization': 'Bearer ' + token }
                });
            } catch (e) {
                console.warn('登出请求失败:', e);
            }
        }
        clearAll();
        window.location.href = '/login.html';
    }

    // ===== 页面访问控制 =====

    function requireAuth() {
        if (!isAuthenticated()) {
            window.location.href = '/login.html';
            return false;
        }
        return true;
    }

    function redirectIfAuthenticated() {
        if (isAuthenticated()) {
            window.location.href = '/index.html';
            return true;
        }
        return false;
    }

    return {
        saveTokens, getAccessToken, getRefreshToken,
        saveUserInfo, getUserInfo, clearAll,
        isAuthenticated,
        request, login, logout,
        requireAuth, redirectIfAuthenticated
    };
})();
