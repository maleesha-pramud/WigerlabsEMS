/**
 * ToastNotification Web Component
 *
 * Usage:
 *   <toast-notification id="toast"></toast-notification>
 *
 * Methods:
 *   show(message, type, duration)
 *   hide()
 *
 * type: 'success' | 'error' | 'info'   (default: 'success')
 * duration: ms before auto-hide        (default: 3000 — pass 0 to disable)
 */
class ToastNotification extends HTMLElement {
    constructor() {
        super();
        this._timer = null;
    }

    connectedCallback() {
        this.innerHTML = `
            <div class="toast-wrapper pointer-events-none fixed top-6 right-6 z-[60]
                        flex items-center gap-3 px-4 py-3 rounded-xl shadow-lg
                        text-sm font-medium text-white
                        opacity-0 translate-x-4
                        transition-all duration-300">
                <span class="toast-icon material-symbols-outlined text-base"></span>
                <span class="toast-message"></span>
                <button class="toast-close pointer-events-auto ml-2 opacity-70 hover:opacity-100 transition-opacity">
                    <span class="material-symbols-outlined text-base">close</span>
                </button>
            </div>
        `;

        this._wrapper  = this.querySelector('.toast-wrapper');
        this._iconEl   = this.querySelector('.toast-icon');
        this._msgEl    = this.querySelector('.toast-message');
        this._closeBtn = this.querySelector('.toast-close');

        this._closeBtn.addEventListener('click', () => this.hide());
    }

    show(message, type = 'success', duration = 3000) {
        clearTimeout(this._timer);

        const colors = { success: 'bg-emerald-500', error: 'bg-red-500', info: 'bg-blue-500' };
        const icons  = { success: 'check_circle',   error: 'error',      info: 'info'        };

        this._wrapper.classList.remove('bg-emerald-500', 'bg-red-500', 'bg-blue-500');
        this._wrapper.classList.add(colors[type] ?? colors.success);

        this._iconEl.textContent = icons[type] ?? icons.success;
        this._msgEl.textContent  = message;

        // Animate in
        this._wrapper.classList.remove('opacity-0', 'translate-x-4', 'pointer-events-none');
        this._wrapper.classList.add('opacity-100', 'translate-x-0', 'pointer-events-auto');

        if (duration > 0) {
            this._timer = setTimeout(() => this.hide(), duration);
        }
    }

    hide() {
        clearTimeout(this._timer);
        this._wrapper.classList.remove('opacity-100', 'translate-x-0', 'pointer-events-auto');
        this._wrapper.classList.add('opacity-0', 'translate-x-4', 'pointer-events-none');
    }
}

customElements.define('toast-notification', ToastNotification);

