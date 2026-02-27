// Base URL for API
const BASE_URL = 'http://localhost:8080/wigerlabs_ems/api';

/**
 * Helper to handle fetch requests with a timeout and consistent error handling
 */
const request = async (url, options = {}) => {
    const { timeout = 10000, ...customOptions } = options;

    // Set up timeout controller
    const controller = new AbortController();
    const id = setTimeout(() => controller.abort(), timeout);

    const config = {
        ...customOptions,
        signal: controller.signal,
        headers: {
            'Content-Type': 'application/json',
            ...customOptions.headers,
        },
    };

    try {
        const response = await fetch(`${BASE_URL}${url}`, config);
        clearTimeout(id);

        const data = await response.json().catch(() => ({})); // Handle empty responses

        if (!response.ok) {
            return {
                success: false,
                error: data.message || `Error: ${response.status} ${response.statusText}`
            };
        }

        return { success: true, data };
    } catch (error) {
        clearTimeout(id);
        console.error(`${options.method || 'GET'} request error:`, error);

        let errorMessage = error.message;
        if (error.name === 'AbortError') errorMessage = 'Request timed out';

        return { success: false, error: errorMessage };
    }
};

// GET request
export const get = (url, params) => {
    const queryString = params ? '?' + new URLSearchParams(params).toString() : '';
    return request(`${url}${queryString}`, { method: 'GET' });
};

// POST request
export const post = (url, data) => {
    return request(url, {
        method: 'POST',
        body: JSON.stringify(data),
    });
};

// PUT request
export const put = (url, data) => {
    return request(url, {
        method: 'PUT',
        body: JSON.stringify(data),
    });
};

// DELETE request
export const del = (url) => {
    return request(url, { method: 'DELETE' });
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