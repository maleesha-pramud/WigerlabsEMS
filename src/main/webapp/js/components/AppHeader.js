/**
 * AppHeader Web Component
 *
 * Usage:
 *   <app-header placeholder="Search employees..."></app-header>
 *
 * Attributes:
 *   placeholder  – search input placeholder text (default: "Search…")
 *
 * Reads user details (name, role, department, position) from UserStore (localStorage).
 */
import UserStore from '../utils/user_store.js';

class AppHeader extends HTMLElement {
    connectedCallback() {
        this._render();
    }

    static get observedAttributes() { return ['placeholder']; }
    attributeChangedCallback() { this._render(); }

    _render() {
        const user        = UserStore.get();
        const placeholder = this.getAttribute('placeholder') || 'Search…';

        // Build avatar: initials fallback
        const initials = user?.userName
            ? user.userName.split(' ').map(w => w[0]).join('').toUpperCase().slice(0, 2)
            : '?';

        const userName       = this._escape(user?.userName       ?? '—');
        const userRole       = this._escape(user?.userRole       ?? '');
        const departmentName = this._escape(user?.departmentName ?? '');
        const positionName   = this._escape(user?.positionName   ?? '');

        this.innerHTML = `
            <header class="h-20 px-8 flex items-center justify-between border-b border-gray-100 dark:border-slate-800 bg-background-light dark:bg-background-dark">
                <!-- Search -->
                <div class="w-96">
                    <div class="relative">
                        <span class="absolute inset-y-0 left-0 flex items-center pl-3 text-slate-400 pointer-events-none">
                            <span class="material-symbols-outlined text-[20px]">search</span>
                        </span>
                        <input id="header-search-input" class="w-full py-2 pl-10 pr-4 bg-transparent border-none text-sm text-slate-600 dark:text-slate-300 focus:ring-0 placeholder-slate-400"
                               placeholder="${placeholder}" type="text"/>
                    </div>
                </div>

                <!-- Right side -->
                <div class="flex items-center space-x-5">
                    <!-- Chat -->
                    <button class="text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 transition-colors">
                        <span class="material-symbols-outlined">chat_bubble_outline</span>
                    </button>

                    <!-- Notifications -->
                    <button class="text-slate-500 hover:text-slate-700 dark:text-slate-400 dark:hover:text-slate-200 transition-colors relative">
                        <span class="material-symbols-outlined">notifications_none</span>
                        <span class="absolute top-0 right-0 w-2 h-2 bg-red-500 rounded-full"></span>
                    </button>

                    <!-- User info -->
                    <div class="flex items-center space-x-3 pl-4 border-l border-slate-200 dark:border-slate-700">
                        <!-- Avatar -->
                        <div class="w-10 h-10 rounded-full bg-sidebar flex items-center justify-center flex-shrink-0
                                    text-sm font-bold text-white ring-2 ring-white dark:ring-slate-700 select-none">
                            ${initials}
                        </div>
                        <!-- Name + role -->
                        <div>
                            <p class="text-sm font-semibold text-slate-800 dark:text-white leading-tight">${userName}</p>
                            <p class="text-xs text-slate-500 dark:text-slate-400 capitalize leading-tight"
                               title="${departmentName ? departmentName + (positionName ? ' · ' + positionName : '') : userRole}">
                                ${departmentName
                                    ? `${departmentName}${positionName ? ' · ' + positionName : ''}`
                                    : userRole}
                            </p>
                        </div>
                    </div>
                </div>
            </header>`;

        // Add search event listener
        const input = this.querySelector('#header-search-input');
        if (input) {
            input.addEventListener('input', (e) => {
                this.dispatchEvent(new CustomEvent('search', {
                    detail: { query: e.target.value },
                    bubbles: true,
                }));
            });
            input.addEventListener('keydown', (e) => {
                if (e.key === 'Enter') {
                    this.dispatchEvent(new CustomEvent('search', {
                        detail: { query: e.target.value },
                        bubbles: true,
                    }));
                }
            });
        }
    }

    _escape(str) {
        return String(str)
            .replace(/&/g, '&amp;').replace(/</g, '&lt;')
            .replace(/>/g, '&gt;').replace(/"/g, '&quot;');
    }
}

customElements.define('app-header', AppHeader);

