// ─── Server Configuration ────────────────────────────────────────────────────
export const SERVER_PORT = 8080;
export const CONTEXT_PATH = '/wigerlabs_ems';
export const BASE_URL = `http://localhost:${SERVER_PORT}${CONTEXT_PATH}`;
export const API_BASE_URL = `${BASE_URL}/api`;

// ─── API Endpoints ────────────────────────────────────────────────────────────
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

