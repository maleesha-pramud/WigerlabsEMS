/**
 * AppSidebar Web Component
 *
 * Usage:
 *   <app-sidebar></app-sidebar>
 *
 * Automatically highlights the active nav link based on the current page filename.
 * Dynamically loads the department list from the API.
 */
import {get, post} from '../api.js';
import {API_ENDPOINTS} from '../utils/constants.js';
import UserStore from '../utils/user_store.js';
import {redirectTOLogin} from "../utils/common.js";

const NAV_ITEMS = [
    {href: 'dashboard.html', icon: 'grid_view', label: 'Dashboard'},
    {href: 'employee.html', icon: 'group', label: 'Employee'},
    {href: 'admin.html', icon: 'verified_user', label: 'Admin'},
    {href: 'manager.html', icon: 'supervisor_account', label: 'Manager'},
    {href: 'position.html', icon: 'work', label: 'Position'},
    {href: 'department.html', icon: 'apartment', label: 'Department'},
];

const DEPT_COLORS = [
    'bg-orange-500', 'bg-indigo-500', 'bg-teal-500',
    'bg-blue-500', 'bg-purple-500', 'bg-pink-500',
    'bg-emerald-500', 'bg-yellow-500',
];

class AppSidebar extends HTMLElement {
    connectedCallback() {
        // Instant local auth guard — redirect to login if no user in store
        if (!UserStore.isLoggedIn()) {
            redirectTOLogin();
            return;
        }

        this._render([]);          // render immediately with empty dept list
        this._setActiveLink();
        this._loadDepartments();
        this._bindLogout();
    }

    // Render
    _render(departments) {
        const current = window.location.pathname.split('/').pop() || 'index.html';

        const navItems = NAV_ITEMS.map(({href, icon, label}) => {
            const isActive = href === current;
            const cls = isActive
                ? 'flex items-center space-x-3 px-2 py-2.5 rounded-lg bg-slate-700/50 text-white transition-colors'
                : 'flex items-center space-x-3 px-2 py-2.5 rounded-lg text-slate-300 hover:bg-slate-700/50 hover:text-white transition-colors';
            return `
                <li>
                    <a class="${cls}" href="${href}">
                        <span class="material-symbols-outlined text-[20px]">${icon}</span>
                        <span class="text-sm font-medium">${label}</span>
                    </a>
                </li>`;
        }).join('');

        const deptItems = departments.length > 0
            ? departments.map((dept, i) => {
                const color = DEPT_COLORS[i % DEPT_COLORS.length];
                return `
                    <li>
                        <a class="flex items-center space-x-3 px-2 py-2.5 rounded-lg text-slate-300 hover:bg-slate-700/50 hover:text-white transition-colors"
                           href="department.html">
                            <div class="w-2 h-2 rounded-full ${color} flex-shrink-0"></div>
                            <span class="text-sm font-medium truncate">${this._escape(dept.name)}</span>
                        </a>
                    </li>`;
            }).join('')
            : `<li class="px-2 py-1 text-xs text-slate-500">No departments yet</li>`;

        this.innerHTML = `
            <aside class="w-64 bg-sidebar text-white flex flex-col h-full flex-shrink-0 transition-colors duration-200">
                <div class="px-6 py-8 flex items-center space-x-3">
                    <div class="text-primary">
                        <span class="material-symbols-outlined text-3xl">emergency_home</span>
                    </div>
                    <h1 class="text-xl font-bold tracking-tight">Wigerlabs EMS</h1>
                </div>
                <nav class="flex-1 overflow-y-auto px-4 space-y-8 no-scrollbar">
                    <div>
                        <p class="px-2 text-xs font-medium text-slate-400 uppercase tracking-wider mb-2">Menu</p>
                        <ul class="space-y-1">
                            ${navItems}
                        </ul>
                    </div>
                    <div>
                        <div class="flex items-center justify-between px-2 mb-2">
                            <p class="text-xs font-medium text-slate-400 uppercase tracking-wider">Department</p>
                            <a href="department.html" class="text-slate-400 hover:text-white" title="Manage Departments">
                                <span class="material-symbols-outlined text-sm">add</span>
                            </a>
                        </div>
                        <ul class="space-y-1" id="sidebar-dept-list">
                            ${deptItems}
                        </ul>
                    </div>
                </nav>
                <div class="p-4 border-t border-slate-700">
                    <button id="sidebar-logout-btn"
                            class="w-full flex items-center space-x-3 px-2 py-2.5 rounded-lg text-slate-300 hover:bg-slate-700/50 hover:text-white transition-colors">
                        <span class="material-symbols-outlined text-[20px]">logout</span>
                        <span class="text-sm font-medium">Log out</span>
                    </button>
                </div>
            </aside>`;
    }

    // Active link
    _setActiveLink() {
        // Already handled in _render() via class logic, but re-run after innerHTML set
        const current = window.location.pathname.split('/').pop() || 'index.html';
        this.querySelectorAll('a[href]').forEach(a => {
            const href = a.getAttribute('href');
            if (href === current) {
                a.className = 'flex items-center space-x-3 px-2 py-2.5 rounded-lg bg-slate-700/50 text-white transition-colors';
            }
        });
    }

    // Load departments from API
    async _loadDepartments() {
        const res = await get(API_ENDPOINTS.GET_ALL_DEPARTMENTS);
        if (res.success && res.data.status && res.data.departments?.length) {
            const list = this.querySelector('#sidebar-dept-list');
            if (!list) return;
            list.innerHTML = res.data.departments.map((dept, i) => {
                const color = DEPT_COLORS[i % DEPT_COLORS.length];
                return `
                    <li>
                        <a class="flex items-center space-x-3 px-2 py-2.5 rounded-lg text-slate-300 hover:bg-slate-700/50 hover:text-white transition-colors"
                           href="department.html">
                            <div class="w-2 h-2 rounded-full ${color} flex-shrink-0"></div>
                            <span class="text-sm font-medium truncate">${this._escape(dept.name)}</span>
                        </a>
                    </li>`;
            }).join('');
        }
    }

    // Logout
    _bindLogout() {
        this.addEventListener('click', async (e) => {
            if (e.target.closest('#sidebar-logout-btn')) {
                // Clear local store immediately for instant UX
                UserStore.clear();
                // Fire server-side session invalidation (don't block on result)
                post(API_ENDPOINTS.LOGOUT, {}).catch(() => {
                });
                redirectTOLogin();
            }
        });
    }

    // Helpers
    _escape(str) {
        return String(str)
            .replace(/&/g, '&amp;').replace(/</g, '&lt;')
            .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }
}

customElements.define('app-sidebar', AppSidebar);

