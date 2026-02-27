import { BASE_URL } from './constants.js';

export function redirectTOLogin() {
    window.location.href = `${BASE_URL}/login.html`;
}
