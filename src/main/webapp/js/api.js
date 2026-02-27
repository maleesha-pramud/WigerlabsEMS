import axios from 'axios';


const BASE_URL = 'http://localhost:8080/wigerlabs_ems/api';

// Create axios instance
const api = axios.create({
    baseURL: BASE_URL,
    timeout: 10000, // 10 seconds
    headers: {
        'Content-Type': 'application/json',
    },
    withCredentials: true, // Enable cookies and session
});

// GET request
export const get = async (url, params) => {
    try {
        const response = await api.get(url, { params });
        return { success: true, data: response.data };
    } catch (error) {
        console.error('GET request error:', error);
        return {
            success: false,
            error: error.response?.data?.message || error.message
        };
    }
};

// POST request
export const post = async (url, data) => {
    try {
        const response = await api.post(url, data);
        return { success: true, data: response.data };
    } catch (error) {
        console.error('POST request error:', error);
        return {
            success: false,
            error: error.response?.data?.message || error.message
        };
    }
};

// PUT request
export const put = async (url, data) => {
    try {
        const response = await api.put(url, data);
        return { success: true, data: response.data };
    } catch (error) {
        console.error('PUT request error:', error);
        return {
            success: false,
            error: error.response?.data?.message || error.message
        };
    }
};

// DELETE request
export const del = async (url) => {
    try {
        const response = await api.delete(url);
        return { success: true, data: response.data };
    } catch (error) {
        console.error('DELETE request error:', error);
        return {
            success: false,
            error: error.response?.data?.message || error.message
        };
    }
};

// API endpoints
export const API_ENDPOINTS = {
    // Auth
    LOGIN: '/auth/login',
    LOGOUT: '/auth/logout',
    CHECK_SESSION: '/auth/check',

    // Admin
    ADD_ADMIN: '/admin',
    GET_ALL_ADMINS: '/admin',
    GET_ADMIN_BY_ID: (id) => `/admin/${id}`,
    UPDATE_ADMIN: '/admin',
    DELETE_ADMIN: (id) => `/admin/${id}`,

    // Manager
    ADD_MANAGER: '/manager',
    GET_ALL_MANAGERS: '/manager',
    GET_MANAGER_BY_ID: (id) => `/manager/${id}`,
    UPDATE_MANAGER: '/manager',
    DELETE_MANAGER: (id) => `/manager/${id}`,

    // Employee
    ADD_EMPLOYEE: '/employee',
    GET_ALL_EMPLOYEES: '/employee',
    GET_EMPLOYEE_BY_ID: (id) => `/employee/${id}`,
    UPDATE_EMPLOYEE: '/employee',
    DELETE_EMPLOYEE: (id) => `/employee/${id}`,

    // Department
    ADD_DEPARTMENT: '/department',
    GET_ALL_DEPARTMENTS: '/department',
    GET_DEPARTMENT_BY_ID: (id) => `/department/${id}`,
    UPDATE_DEPARTMENT: '/department',
    DELETE_DEPARTMENT: (id) => `/department/${id}`,

    // Position
    ADD_POSITION: '/position',
    GET_ALL_POSITIONS: '/position',
    GET_POSITION_BY_ID: (id) => `/position/${id}`,
    UPDATE_POSITION: '/position',
    DELETE_POSITION: (id) => `/position/${id}`,
};

export default api;