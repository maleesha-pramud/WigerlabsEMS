// Server Configuration
export const SERVER_PORT = 8080;
export const CONTEXT_PATH = '/wigerlabs_ems';
export const BASE_URL = `http://localhost:${SERVER_PORT}${CONTEXT_PATH}`;
export const API_BASE_URL = `${BASE_URL}/api`;

// API Endpoints
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
    SEARCH_ADMIN: (q) => `/admin/search?q=${encodeURIComponent(q)}`,
    GET_ADMINS_BY_STATUS: (statusId) => `/admin/users/status/${statusId}`,

    // Manager
    ADD_MANAGER: '/manager',
    GET_ALL_MANAGERS: '/manager',
    GET_MANAGER_BY_ID: (id) => `/manager/${id}`,
    UPDATE_MANAGER: '/manager',
    DELETE_MANAGER: (id) => `/manager/${id}`,
    SEARCH_MANAGER: (q) => `/manager/search?q=${encodeURIComponent(q)}`,
    GET_MANAGERS_BY_STATUS: (statusId) => `/manager/users/status/${statusId}`,

    // Employee
    ADD_EMPLOYEE: '/employee',
    GET_ALL_EMPLOYEES: '/employee',
    GET_EMPLOYEE_BY_ID: (id) => `/employee/${id}`,
    UPDATE_EMPLOYEE: '/employee',
    DELETE_EMPLOYEE: (id) => `/employee/${id}`,
    SEARCH_EMPLOYEE: (q) => `/employee/search?q=${encodeURIComponent(q)}`,
    GET_EMPLOYEES_BY_STATUS: (statusId) => `/employee/users/status/${statusId}`,

    // Department
    ADD_DEPARTMENT: '/department',
    GET_ALL_DEPARTMENTS: '/department',
    GET_DEPARTMENT_BY_ID: (id) => `/department/${id}`,
    UPDATE_DEPARTMENT: '/department',
    DELETE_DEPARTMENT: (id) => `/department/${id}`,
    SEARCH_DEPARTMENT: (q) => `/department/search?q=${encodeURIComponent(q)}`,

    // Position
    ADD_POSITION: '/position',
    GET_ALL_POSITIONS: '/position',
    GET_POSITION_BY_ID: (id) => `/position/${id}`,
    UPDATE_POSITION: '/position',
    DELETE_POSITION: (id) => `/position/${id}`,
    SEARCH_POSITION: (q) => `/position/search?q=${encodeURIComponent(q)}`,

    // Status Change
    CHANGE_EMPLOYEE_STATUS: (id) => `/employee/${id}/status`,
    CHANGE_ADMIN_STATUS: (id) => `/admin/${id}/status`,
    CHANGE_MANAGER_STATUS: (id) => `/manager/${id}/status`,
};
