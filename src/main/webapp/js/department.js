/**
 * department.js
 * Handles all Department CRUD operations for department.html.
 * Depends on:  api.js, ConfirmModal.js, ToastNotification.js
 */
import { get, post, put, del, API_ENDPOINTS } from './api.js';

// Wait for web components to be defined and upgraded before running
await Promise.all([
    customElements.whenDefined('toast-notification'),
    customElements.whenDefined('confirm-modal'),
]);

// ── DOM refs ──────────────────────────────────────────────────────────────────
const tbody        = document.getElementById('department-tbody');
const btnOpenAdd   = document.getElementById('btn-open-add');
const toast        = document.getElementById('toast');
const confirmModal = document.getElementById('confirm-modal');

// Add/Edit modal
const deptModal        = document.getElementById('dept-modal');
const modalTitle       = document.getElementById('modal-title');
const inputName        = document.getElementById('input-dept-name');
const inputError       = document.getElementById('input-error');
const btnCloseModal    = document.getElementById('btn-close-modal');
const btnCancelModal   = document.getElementById('btn-cancel-modal');
const btnSubmitModal   = document.getElementById('btn-submit-modal');
const btnSubmitLabel   = document.getElementById('btn-submit-label');
const btnSubmitSpinner = document.getElementById('btn-submit-spinner');

// State
let editingId = null;   // null = add mode, number = edit mode

// Helpers
function escapeHtml(str) {
    return String(str)
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

// Add/Edit Modal
function openAddModal() {
    editingId                  = null;
    modalTitle.textContent     = 'Add Department';
    btnSubmitLabel.textContent = 'Add';
    inputName.value            = '';
    hideInputError();
    deptModal.classList.remove('hidden');
    inputName.focus();
}

function openEditModal(id, name) {
    editingId                  = id;
    modalTitle.textContent     = 'Edit Department';
    btnSubmitLabel.textContent = 'Save Changes';
    inputName.value            = name;
    hideInputError();
    deptModal.classList.remove('hidden');
    inputName.focus();
}

function closeModal() {
    deptModal.classList.add('hidden');
}

function showInputError(msg) {
    inputError.textContent = msg;
    inputError.classList.remove('hidden');
    inputName.classList.add('ring-2', 'ring-red-400');
}

function hideInputError() {
    inputError.classList.add('hidden');
    inputName.classList.remove('ring-2', 'ring-red-400');
}

function setSubmitLoading(loading) {
    btnSubmitModal.disabled = loading;
    btnSubmitSpinner.classList.toggle('hidden', !loading);
}

// Render
function renderLoadingRow() {
    tbody.innerHTML = `
        <tr>
            <td colspan="3" class="px-6 py-10 text-center">
                <div class="flex items-center justify-center gap-2 text-sm text-slate-400 dark:text-slate-500">
                    <span class="material-symbols-outlined animate-spin-slow">progress_activity</span>
                    Loading departments...
                </div>
            </td>
        </tr>`;
}

function renderRows(departments) {
    if (!departments || departments.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="3" class="px-6 py-10 text-center text-sm text-slate-400 dark:text-slate-500">
                    No departments found. Click <strong>Add Department</strong> to create one.
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = departments.map((dept, index) => `
        <tr class="hover:bg-gray-50 dark:hover:bg-slate-800/50 transition-colors">
            <td class="px-6 py-4 whitespace-nowrap text-xs text-slate-400 dark:text-slate-500">${index + 1}</td>
            <td class="px-6 py-4 whitespace-nowrap font-semibold text-slate-800 dark:text-white">${escapeHtml(dept.name)}</td>
            <td class="px-6 py-4 text-right">
                <div class="inline-flex items-center space-x-1">
                    <button class="btn-edit p-1.5 rounded-lg text-slate-400 hover:text-primary hover:bg-gray-100 dark:hover:bg-slate-700 transition-colors"
                            data-id="${dept.id}" data-name="${escapeHtml(dept.name)}" title="Edit">
                        <span class="material-symbols-outlined text-lg pointer-events-none">edit</span>
                    </button>
                    <button class="btn-delete p-1.5 rounded-lg text-slate-400 hover:text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
                            data-id="${dept.id}" data-name="${escapeHtml(dept.name)}" title="Delete">
                        <span class="material-symbols-outlined text-lg pointer-events-none">delete</span>
                    </button>
                </div>
            </td>
        </tr>
    `).join('');
}

// API
async function loadDepartments() {
    renderLoadingRow();
    const res = await get(API_ENDPOINTS.GET_ALL_DEPARTMENTS);
    if (res.success && res.data.status) {
        renderRows(res.data.data);
    } else {
        tbody.innerHTML = `
            <tr>
                <td colspan="3" class="px-6 py-10 text-center text-sm text-red-500">
                    ${res.data?.message || res.error || 'Failed to load departments.'}
                </td>
            </tr>`;
        toast.show(res.data?.message || res.error || 'Failed to load departments.', 'error');
    }
}

async function handleSubmit() {
    const name = inputName.value.trim();
    if (!name) { showInputError('Department name is required.'); return; }
    hideInputError();
    setSubmitLoading(true);

    const res = editingId === null
        ? await post(API_ENDPOINTS.ADD_DEPARTMENT, { name })
        : await put(API_ENDPOINTS.UPDATE_DEPARTMENT, { id: editingId, name });

    setSubmitLoading(false);

    if (res.success && res.data.status) {
        toast.show(res.data.message, 'success');
        closeModal();
        loadDepartments();
    } else {
        showInputError(res.data?.message || res.error || 'Something went wrong.');
    }
}

// ── Event listeners ───────────────────────────────────────────────────────────
btnOpenAdd.addEventListener('click', openAddModal);
btnCloseModal.addEventListener('click', closeModal);
btnCancelModal.addEventListener('click', closeModal);
btnSubmitModal.addEventListener('click', handleSubmit);

// Close modal on backdrop click
deptModal.addEventListener('click', (e) => { if (e.target === deptModal) closeModal(); });

// Submit on Enter key
inputName.addEventListener('keydown', (e) => { if (e.key === 'Enter') handleSubmit(); });

// Edit / Delete via table row delegation
tbody.addEventListener('click', (e) => {
    const editBtn   = e.target.closest('.btn-edit');
    const deleteBtn = e.target.closest('.btn-delete');

    if (editBtn) {
        openEditModal(parseInt(editBtn.dataset.id), editBtn.dataset.name);
    }

    if (deleteBtn) {
        const { id, name } = deleteBtn.dataset;
        confirmModal.open({
            title:        'Delete Department',
            message:      `Are you sure you want to delete <strong>${escapeHtml(name)}</strong>? This action cannot be undone.`,
            confirmLabel: 'Delete',
            onConfirm:    async () => {
                const res = await del(API_ENDPOINTS.DELETE_DEPARTMENT(parseInt(id)));
                if (res.success && res.data.status) {
                    toast.show(res.data.message, 'success');
                    loadDepartments();
                } else {
                    toast.show(res.data?.message || res.error || 'Failed to delete department.', 'error');
                }
            },
        });
    }
});

// init
loadDepartments();

