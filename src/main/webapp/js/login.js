/**
 * login.js
 * Handles login page logic: session check, form validation, API call, redirect.
 * Depends on: api.js, ToastNotification.js
 */
import { get, post, API_ENDPOINTS } from './api.js';

// Wait for the custom element to be defined and upgraded before running
await customElements.whenDefined('toast-notification');

// DOM refs
const form           = document.getElementById('login-form');
const inputEmail     = document.getElementById('input-email');
const inputPassword  = document.getElementById('input-password');
const emailError     = document.getElementById('email-error');
const passwordError  = document.getElementById('password-error');
const formError      = document.getElementById('form-error');
const formErrorMsg   = document.getElementById('form-error-msg');
const btnSubmit      = document.getElementById('btn-login');
const btnLabel       = document.getElementById('btn-login-label');
const btnSpinner     = document.getElementById('btn-login-spinner');
const togglePassword = document.getElementById('toggle-password');
const toggleIcon     = document.getElementById('toggle-password-icon');
const toast          = document.getElementById('toast');

// Session check on load
(async () => {
    const res = await get(API_ENDPOINTS.CHECK_SESSION);
    if (res.success && res.data.authenticated) {
        window.location.href = 'dashboard.html';
    }
})();

// Password visibility toggle
togglePassword.addEventListener('click', () => {
    const isText = inputPassword.type === 'text';
    inputPassword.type      = isText ? 'password' : 'text';
    toggleIcon.textContent  = isText ? 'visibility' : 'visibility_off';
});

// Validation helpers
function showFieldError(el, msg) {
    el.textContent = msg;
    el.classList.remove('hidden');
}

function clearFieldError(el) {
    el.textContent = '';
    el.classList.add('hidden');
}

function showFormError(msg) {
    formErrorMsg.textContent = msg;
    formError.classList.remove('hidden');
}

function clearFormError() {
    formErrorMsg.textContent = '';
    formError.classList.add('hidden');
}

function setLoading(loading) {
    btnSubmit.disabled      = loading;
    btnLabel.textContent    = loading ? 'Signing in…' : 'Sign In';
    btnSpinner.classList.toggle('hidden', !loading);
}

function validate() {
    let valid = true;
    clearFieldError(emailError);
    clearFieldError(passwordError);
    clearFormError();

    const email    = inputEmail.value.trim();
    const password = inputPassword.value;

    if (!email) {
        showFieldError(emailError, 'Email is required.');
        valid = false;
    } else if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
        showFieldError(emailError, 'Please enter a valid email address.');
        valid = false;
    }

    if (!password) {
        showFieldError(passwordError, 'Password is required.');
        valid = false;
    } else if (password.length < 4) {
        showFieldError(passwordError, 'Password must be at least 4 characters.');
        valid = false;
    }

    return valid;
}

// Clear errors on input
inputEmail.addEventListener('input', () => clearFieldError(emailError));
inputPassword.addEventListener('input', () => clearFieldError(passwordError));

// Form submit
form.addEventListener('submit', async (e) => {
    e.preventDefault();
    if (!validate()) return;

    setLoading(true);

    const res = await post(API_ENDPOINTS.LOGIN, {
        email:    inputEmail.value.trim(),
        password: inputPassword.value,
    }, { skipRedirectOn401: true });

    setLoading(false);

    // HTTP 200 with status: true  →  success
    if (res.success && res.data.status) {
        toast.show(`Welcome back, ${res.data.data.userName}!`, 'success', 1500);
        setTimeout(() => { window.location.href = 'dashboard.html'; }, 1500);
    } else {
        // HTTP 401 lands in res.success = false; message is in res.error
        // HTTP 200 with status: false lands in res.data.message
        const msg = res.data?.message || res.error || 'Invalid email or password.';
        showFormError(msg);
        toast.show(msg, 'error');
    }
});



