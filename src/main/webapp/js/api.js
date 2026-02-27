import UserStore from './utils/user_store.js';
import {redirectTOLogin} from "./utils/common.js";
import { API_BASE_URL } from './utils/constants.js';


/**
 * Helper to handle fetch requests with a timeout and consistent error handling
 */
const request = async (url, options = {}) => {
    const { timeout = 10000, skipRedirectOn401 = false, ...customOptions } = options;

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
        const response = await fetch(`${API_BASE_URL}${url}`, config);
        clearTimeout(id);

        const data = await response.json().catch(() => ({})); // Handle empty responses

        if (!response.ok) {
            if (response.status === 401 && !skipRedirectOn401) {
                UserStore.clear();
                setTimeout(() => { redirectTOLogin(); }, 1000);
            }
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
export const post = (url, data, options = {}) => {
    return request(url, {
        method: 'POST',
        body: JSON.stringify(data),
        ...options,
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
