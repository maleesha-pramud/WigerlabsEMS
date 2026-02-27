
import {get, post, put, del} from './api.js';
import {API_ENDPOINTS} from './utils/constants.js';

await Promise.all([
    customElements.whenDefined('toast-notification'),
    customElements.whenDefined('confirm-modal'),
]);

const tbody = document.getElementById('admin-tbody');
const btnOpenAdd = document.getElementById('btn-open-add');
const toast = document.getElementById('toast');
const confirmModal = document.getElementById('confirm-modal');

const adminModal = document.getElementById('admin-modal');
const modalTitle = document.getElementById('modal-title');
const inputName = document.getElementById('input-admin-name');
const inputEmail = document.getElementById('input-admin-email');
const inputPassword = document.getElementById('input-admin-password');
const inputHireDate = document.getElementById('input-admin-hire-date');
const inputSalary = document.getElementById('input-admin-salary');
const emailField = document.getElementById('email-field');
const passwordField = document.getElementById('password-field');
const selectDept = document.getElementById('select-department');
const selectPos = document.getElementById('select-position');
const selectStatus = document.getElementById('select-status');
const formError = document.getElementById('form-error');
const btnCloseModal = document.getElementById('btn-close-modal');
const btnCancelModal = document.getElementById('btn-cancel-modal');
const btnSubmitModal = document.getElementById('btn-submit-modal');
const btnSubmitLabel = document.getElementById('btn-submit-label');
const btnSubmitSpinner = document.getElementById('btn-submit-spinner');

let editingId = null;

function escapeHtml(str) {
    return String(str ?? '')
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;');
}

function capitalize(str) {
    return str ? str.charAt(0).toUpperCase() + str.slice(1) : '';
}

function showError(msg) {
    formError.textContent = msg;
    formError.classList.remove('hidden');
}

function hideError() {
    formError.classList.add('hidden');
}

function setSubmitLoading(loading) {
    btnSubmitModal.disabled = loading;
    btnSubmitSpinner.classList.toggle('hidden', !loading);
}

async function populateDropdowns() {
    const [deptRes, posRes] = await Promise.all([
        get(API_ENDPOINTS.GET_ALL_DEPARTMENTS),
        get(API_ENDPOINTS.GET_ALL_POSITIONS),
    ]);
    if (deptRes.success && deptRes.data.status && deptRes.data.data?.length) {
        selectDept.innerHTML = deptRes.data.data.map(d => `<option value="${d.id}">${escapeHtml(d.name)}</option>`).join('');
    } else {
        selectDept.innerHTML = '<option value="">No departments found</option>';
    }
    if (posRes.success && posRes.data.status && posRes.data.data?.length) {
        selectPos.innerHTML = posRes.data.data.map(p => `<option value="${p.id}">${escapeHtml(p.name)}</option>`).join('');
    } else {
        selectPos.innerHTML = '<option value="">No positions found</option>';
    }
}

async function openAddModal() {
    editingId = null;
    modalTitle.textContent = 'Add Admin';
    btnSubmitLabel.textContent = 'Add';

    // Reset fields
    inputName.value = '';
    inputEmail.value = '';
    inputPassword.value = '';
    inputHireDate.value = '';
    inputSalary.value = '';
    selectStatus.value = '1';
    hideError();

    // Show email/password fields for add
    emailField.classList.remove('hidden');
    passwordField.classList.remove('hidden');

    adminModal.classList.remove('hidden');
    await populateDropdowns();
    inputName.focus();
}

async function openEditModal(admin) {
    editingId = admin.id;
    modalTitle.textContent = 'Edit Admin';
    btnSubmitLabel.textContent = 'Save Changes';
    hideError();

    adminModal.classList.remove('hidden');
    await populateDropdowns();

    // Fetch latest user data
    const res = await get(API_ENDPOINTS.GET_ADMIN_BY_ID(admin.id));
    let user = admin;
    if (res.success && res.data.status && res.data.data) {
        user = res.data.data;
    }

    inputName.value = user.name || '';
    inputEmail.value = user.email || '';
    inputPassword.value = '';
    inputHireDate.value = user.hireDate || '';
    inputSalary.value = user.salary || '';
    selectDept.value = String(user.departmentId);
    selectPos.value = String(user.positionId);
    selectStatus.value = String(user.statusId);

    emailField.classList.remove('hidden');
    passwordField.classList.remove('hidden');
    inputName.focus();
}

function closeModal() {
    adminModal.classList.add('hidden');
}

let viewModal = null;
let viewModalContent = null;

function createViewModal() {
    if (document.getElementById('view-modal')) return;
    const modal = document.createElement('div');
    modal.id = 'view-modal';
    modal.className = 'hidden fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm';
    modal.innerHTML = `
        <div class="bg-white dark:bg-surface-dark w-full max-w-md mx-4 rounded-2xl shadow-2xl ring-1 ring-black/5">
            <div class="flex items-center justify-between px-6 py-5 border-b border-gray-100 dark:border-slate-700">
                <h3 class="text-lg font-semibold text-slate-900 dark:text-white">Admin Details</h3>
                <button id="btn-close-view-modal" class="text-slate-400 hover:text-slate-600 dark:hover:text-slate-200">
                    <span class="material-symbols-outlined">close</span>
                </button>
            </div>
            <div id="view-modal-content" class="px-6 py-6 space-y-4"></div>
        </div>
    `;
    document.body.appendChild(modal);
    viewModal = modal;
    viewModalContent = modal.querySelector('#view-modal-content');
    modal.querySelector('#btn-close-view-modal').addEventListener('click', () => modal.classList.add('hidden'));
    modal.addEventListener('click', (e) => {
        if (e.target === modal) modal.classList.add('hidden');
    });
}

function openViewModal(admin) {
    createViewModal();
    viewModalContent.innerHTML = `
        <div class="flex items-center justify-center gap-2 text-sm text-slate-400 dark:text-slate-500 py-8">
            <span class="material-symbols-outlined animate-spin-slow">progress_activity</span>
            Loading admin details...
        </div>
    `;
    viewModal.classList.remove('hidden');
    fetchAndRenderAdmin(admin.id);
}

async function fetchAndRenderAdmin(adminId) {
    const res = await get(API_ENDPOINTS.GET_ADMIN_BY_ID(adminId));
    if (res.success && res.data.status && res.data.data) {
        const user = res.data.data;
        viewModalContent.innerHTML = `
            <div class="space-y-2">
                <div><strong>ID:</strong> ${escapeHtml(String(user.id))}</div>
                <div><strong>Name:</strong> ${escapeHtml(user.name)}</div>
                <div><strong>Email:</strong> ${escapeHtml(user.email || '—')}</div>
                <div><strong>Department:</strong> ${escapeHtml(user.departmentName)}</div>
                <div><strong>Position:</strong> ${escapeHtml(user.positionName)}</div>
                <div><strong>Status:</strong> ${statusBadge(user.statusValue)}</div>
                <div><strong>Hire Date:</strong> ${formatDate(user.hireDate)}</div>
                <div><strong>Salary:</strong> ${user.salary ? escapeHtml(user.salary) : '—'}</div>
            </div>
        `;
    } else {
        viewModalContent.innerHTML = `<div class="text-red-500 py-8">${res.data?.message || res.error || 'Failed to load admin details.'}</div>`;
    }
}

async function changeStatus(admin, newStatusId) {
    setSubmitLoading(true);
    const res = await put(API_ENDPOINTS.CHANGE_ADMIN_STATUS(admin.id), {statusId: newStatusId});
    setSubmitLoading(false);
    if (res.success && res.data.status) {
        toast.show('Status updated.', 'success');
        loadAdmins();
    } else {
        toast.show(res.data?.message || res.error || 'Failed to update status.', 'error');
    }
}

function renderLoadingRow() {
    tbody.innerHTML = `
        <tr>
            <td colspan="6" class="px-6 py-10 text-center">
                <div class="flex items-center justify-center gap-2 text-sm text-slate-400 dark:text-slate-500">
                    <span class="material-symbols-outlined animate-spin-slow">progress_activity</span>
                    Loading admins...
                </div>
            </td>
        </tr>`;
}

function statusBadge(statusValue) {
    const isActive = statusValue?.toLowerCase() === 'active';
    return isActive
        ? `<span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-emerald-50 dark:bg-emerald-900/30 text-emerald-600 dark:text-emerald-400 border border-emerald-100 dark:border-emerald-800">${capitalize(statusValue)}</span>`
        : `<span class="inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium bg-red-50 dark:bg-red-900/30 text-red-600 dark:text-red-400 border border-red-100 dark:border-red-800">${capitalize(statusValue)}</span>`;
}

function renderRows(admins) {
    if (!admins || admins.length === 0) {
        tbody.innerHTML = `
            <tr>
                <td colspan="8" class="px-6 py-10 text-center text-sm text-slate-400 dark:text-slate-500">
                    No admins found. Click <strong>Add Admin</strong> to create one.
                </td>
            </tr>`;
        return;
    }

    tbody.innerHTML = admins.map((admin, index) => `
        <tr class="hover:bg-gray-50 dark:hover:bg-slate-800/50 transition-colors">
            <td class="px-6 py-4 whitespace-nowrap text-xs text-slate-400 dark:text-slate-500">${index + 1}</td>
            <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center space-x-3">
                    <div class="w-9 h-9 rounded-full bg-sidebar/10 dark:bg-sidebar/30 flex items-center justify-center flex-shrink-0">
                        <span class="text-sm font-semibold text-sidebar">${escapeHtml(admin.name.charAt(0).toUpperCase())}</span>
                    </div>
                    <p class="font-semibold text-slate-800 dark:text-white">${escapeHtml(admin.name)}</p>
                </div>
            </td>
            <td class="px-6 py-4 whitespace-nowrap text-slate-700 dark:text-slate-300">${escapeHtml(admin.departmentName)}</td>
            <td class="px-6 py-4 whitespace-nowrap text-slate-700 dark:text-slate-300">${escapeHtml(admin.positionName)}</td>
            <td class="px-6 py-4 whitespace-nowrap">${statusBadge(admin.statusValue)}</td>
            <td class="px-6 py-4 whitespace-nowrap text-slate-700 dark:text-slate-300">${formatDate(admin.hireDate)}</td>
            <td class="px-6 py-4 whitespace-nowrap text-slate-700 dark:text-slate-300">${admin.salary ? escapeHtml(admin.salary) : '—'}</td>
            <td class="px-6 py-4 text-right">
                <div class="inline-flex items-center space-x-1">
                    <button class="btn-view p-1.5 rounded-lg text-slate-400 hover:text-blue-500 hover:bg-blue-50 dark:hover:bg-blue-900/20 transition-colors"
                            data-admin='${JSON.stringify(admin)}' title="View">
                        <span class="material-symbols-outlined text-lg pointer-events-none">visibility</span>
                    </button>
                    <button class="btn-edit p-1.5 rounded-lg text-slate-400 hover:text-primary hover:bg-gray-100 dark:hover:bg-slate-700 transition-colors"
                            data-admin='${JSON.stringify({
        id: admin.id,
        name: admin.name,
        departmentId: admin.departmentId,
        positionId: admin.positionId,
        statusId: admin.statusId,
        hireDate: admin.hireDate,
        salary: admin.salary
    })}'
                            title="Edit">
                        <span class="material-symbols-outlined text-lg pointer-events-none">edit</span>
                    </button>
                    <button class="btn-delete p-1.5 rounded-lg text-slate-400 hover:text-red-500 hover:bg-red-50 dark:hover:bg-red-900/20 transition-colors"
                            data-id="${admin.id}" data-name="${escapeHtml(admin.name)}" title="Delete">
                        <span class="material-symbols-outlined text-lg pointer-events-none">delete</span>
                    </button>
                    <select class="btn-status px-2 py-1 rounded-lg text-xs border border-gray-200 dark:border-slate-700 bg-white dark:bg-slate-800 text-slate-700 dark:text-slate-200" data-admin='${JSON.stringify({
        id: admin.id,
        name: admin.name,
        departmentId: admin.departmentId,
        positionId: admin.positionId,
        statusId: admin.statusId
    })}' title="Change Status">
                        <option value="1" ${admin.statusId === 1 ? 'selected' : ''}>Active</option>
                        <option value="2" ${admin.statusId === 2 ? 'selected' : ''}>Inactive</option>
                    </select>
                </div>
            </td>
        </tr>
    `).join('');
}

async function loadAdmins() {
    renderLoadingRow();
    const res = await get(API_ENDPOINTS.GET_ALL_ADMINS);
    if (res.success && res.data.status) {
        renderRows(res.data.data);
    } else {
        tbody.innerHTML = `
            <tr>
                <td colspan="6" class="px-6 py-10 text-center text-sm text-red-500">
                    ${res.data?.message || res.error || 'Failed to load admins.'}
                </td>
            </tr>`;
        toast.show(res.data?.message || res.error || 'Failed to load admins.', 'error');
    }
}

async function handleSubmit() {
    hideError();

    const name = inputName.value.trim();
    const email = inputEmail.value.trim();
    const password = inputPassword.value;
    const deptId = parseInt(selectDept.value);
    const posId = parseInt(selectPos.value);
    const statusId = parseInt(selectStatus.value);
    const hireDate = inputHireDate.value;
    const salary = inputSalary.value;

    // Validation
    if (!name) {
        showError('Full name is required.');
        return;
    }
    if (!email) {
        showError('Email is required.');
        return;
    }
    if (!deptId) {
        showError('Please select a department.');
        return;
    }
    if (!posId) {
        showError('Please select a position.');
        return;
    }
    if (!hireDate) {
        showError('Hire date is required.');
        return;
    }
    if (!salary || isNaN(Number(salary)) || Number(salary) < 0) {
        showError('Valid salary is required.');
        return;
    }

    let res;

    if (editingId === null) {
        // Add mode
        if (!password) {
            showError('Password is required.');
            return;
        }
        setSubmitLoading(true);
        res = await post(API_ENDPOINTS.ADD_ADMIN, {
            name, email, password,
            departmentId: deptId,
            positionId: posId,
            statusId,
            hireDate,
            salary,
        });
    } else {
        // Edit mode
        setSubmitLoading(true);
        res = await put(API_ENDPOINTS.UPDATE_ADMIN, {
            id: editingId,
            name,
            email,
            password: password || undefined,
            departmentId: deptId,
            positionId: posId,
            statusId,
            hireDate,
            salary,
        });
    }

    setSubmitLoading(false);

    if (res.success && res.data.status) {
        toast.show(res.data.message, 'success');
        closeModal();
        loadAdmins();
    } else {
        showError(res.data?.message || res.error || 'Something went wrong.');
    }
}

btnOpenAdd.addEventListener('click', openAddModal);
btnCloseModal.addEventListener('click', closeModal);
btnCancelModal.addEventListener('click', closeModal);
btnSubmitModal.addEventListener('click', handleSubmit);
adminModal.addEventListener('click', (e) => {
    if (e.target === adminModal) closeModal();
});
tbody.addEventListener('click', (e) => {
    const viewBtn = e.target.closest('.btn-view');
    const editBtn = e.target.closest('.btn-edit');
    const deleteBtn = e.target.closest('.btn-delete');
    if (viewBtn) {
        const admin = JSON.parse(viewBtn.dataset.admin);
        openViewModal(admin);
    }
    if (editBtn) {
        const admin = JSON.parse(editBtn.dataset.admin);
        openEditModal(admin);
    }
    if (deleteBtn) {
        const {id, name} = deleteBtn.dataset;
        confirmModal.open({
            title: 'Delete Admin',
            message: `Are you sure you want to delete <strong>${escapeHtml(name)}</strong>? This action cannot be undone.`,
            confirmLabel: 'Delete',
            onConfirm: async () => {
                const res = await del(API_ENDPOINTS.DELETE_ADMIN(parseInt(id)));
                if (res.success && res.data.status) {
                    toast.show(res.data.message, 'success');
                    loadAdmins();
                } else {
                    toast.show(res.data?.message || res.error || 'Failed to delete admin.', 'error');
                }
            },
        });
    }
});
tbody.addEventListener('change', (e) => {
    const statusSelect = e.target.closest('.btn-status');
    if (statusSelect) {
        const admin = JSON.parse(statusSelect.dataset.admin);
        const newStatusId = parseInt(statusSelect.value);
        if (admin.statusId !== newStatusId) {
            changeStatus(admin, newStatusId);
        }
    }
});
const headerSearch = document.querySelector('app-header');
if (headerSearch) {
    headerSearch.addEventListener('search', async (e) => {
        const q = e.detail?.query || '';
        if (!q.trim()) {
            loadAdmins();
            return;
        }
        renderLoadingRow();
        const res = await get(API_ENDPOINTS.SEARCH_ADMIN(q));
        if (res.success && res.data.status) {
            renderRows(res.data.data);
        } else {
            tbody.innerHTML = `<tr><td colspan="6" class="px-6 py-10 text-center text-sm text-red-500">${res.data?.message || res.error || 'Failed to search admins.'}</td></tr>`;
            toast.show(res.data?.message || res.error || 'Failed to search admins.', 'error');
        }
    });
}
const filterStatus = document.getElementById('filter-admin-status');

filterStatus.addEventListener('change', async () => {
    const statusId = filterStatus.value;
    if (statusId) {
        renderLoadingRow();
        const res = await get(API_ENDPOINTS.GET_ADMINS_BY_STATUS(statusId));
        if (res.success && res.data.status) {
            renderRows(res.data.data);
        } else {
            tbody.innerHTML = `<tr><td colspan="8" class="px-6 py-10 text-center text-sm text-red-500">${res.data?.message || res.error || 'Failed to filter admins.'}</td></tr>`;
            toast.show(res.data?.message || res.error || 'Failed to filter admins.', 'error');
        }
    } else {
        loadAdmins();
    }
});
loadAdmins();

function formatDate(dateStr) {
    if (!dateStr) return '—';
    return dateStr.split('T')[0];
}
