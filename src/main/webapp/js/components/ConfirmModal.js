/**
 * ConfirmModal Web Component
 *
 * Usage:
 *   <confirm-modal id="my-modal"></confirm-modal>
 *
 * Methods:
 *   open({ title, message, confirmLabel, onConfirm })
 *   close()
 *
 * Events:
 *   'confirm'  – dispatched when the confirm button is clicked
 *   'cancel'   – dispatched when cancelled / closed
 */
class ConfirmModal extends HTMLElement {
    constructor() {
        super();
        this._onConfirm = null;
    }

    connectedCallback() {
        this.innerHTML = `
            <div class="confirm-modal-backdrop hidden fixed inset-0 z-50 flex items-center justify-center bg-black/40 backdrop-blur-sm">
                <div class="bg-white dark:bg-surface-dark w-full max-w-sm mx-4 rounded-2xl shadow-2xl ring-1 ring-black/5">
                    <div class="px-6 py-6 flex flex-col items-center text-center">
                        <div class="w-12 h-12 rounded-full bg-red-50 dark:bg-red-900/30 flex items-center justify-center mb-4">
                            <span class="material-symbols-outlined text-red-500 text-2xl">delete</span>
                        </div>
                        <h3 class="confirm-modal-title text-lg font-semibold text-slate-900 dark:text-white mb-1">Confirm Delete</h3>
                        <p class="confirm-modal-message text-sm text-slate-500 dark:text-slate-400"></p>
                    </div>
                    <div class="flex items-center justify-end gap-3 px-6 py-4 border-t border-gray-100 dark:border-slate-700">
                        <button class="confirm-modal-cancel px-4 py-2 rounded-lg text-sm font-medium text-slate-600 dark:text-slate-300 bg-gray-100 dark:bg-slate-700 hover:bg-gray-200 dark:hover:bg-slate-600 transition-colors">
                            Cancel
                        </button>
                        <button class="confirm-modal-confirm px-5 py-2 rounded-lg text-sm font-medium text-white bg-red-500 hover:bg-red-600 transition-colors flex items-center gap-2">
                            <span class="confirm-modal-confirm-label">Delete</span>
                            <span class="confirm-modal-spinner hidden material-symbols-outlined text-base animate-spin-slow">progress_activity</span>
                        </button>
                    </div>
                </div>
            </div>
        `;

        this._backdrop      = this.querySelector('.confirm-modal-backdrop');
        this._titleEl       = this.querySelector('.confirm-modal-title');
        this._messageEl     = this.querySelector('.confirm-modal-message');
        this._cancelBtn     = this.querySelector('.confirm-modal-cancel');
        this._confirmBtn    = this.querySelector('.confirm-modal-confirm');
        this._confirmLabel  = this.querySelector('.confirm-modal-confirm-label');
        this._spinner       = this.querySelector('.confirm-modal-spinner');

        this._cancelBtn.addEventListener('click', () => this.close());
        this._confirmBtn.addEventListener('click', () => this._handleConfirm());
        this._backdrop.addEventListener('click', (e) => {
            if (e.target === this._backdrop) this.close();
        });
    }

    /**
     * @param {object} options
     * @param {string}   options.title         - Modal heading
     * @param {string}   options.message        - Body message (HTML allowed)
     * @param {string}   [options.confirmLabel] - Label for confirm button (default: 'Delete')
     * @param {function} [options.onConfirm]    - Async callback; receives setLoading(bool)
     */
    open({ title = 'Are you sure?', message = '', confirmLabel = 'Delete', onConfirm = null } = {}) {
        this._titleEl.textContent      = title;
        this._messageEl.innerHTML      = message;
        this._confirmLabel.textContent = confirmLabel;
        this._onConfirm                = onConfirm;
        this._setLoading(false);
        this._backdrop.classList.remove('hidden');
    }

    close() {
        this._backdrop.classList.add('hidden');
        this._onConfirm = null;
        this.dispatchEvent(new CustomEvent('cancel', { bubbles: true }));
    }

    _setLoading(loading) {
        this._confirmBtn.disabled = loading;
        this._spinner.classList.toggle('hidden', !loading);
    }

    async _handleConfirm() {
        this.dispatchEvent(new CustomEvent('confirm', { bubbles: true }));
        if (typeof this._onConfirm === 'function') {
            this._setLoading(true);
            try {
                await this._onConfirm();
            } finally {
                this._setLoading(false);
                this.close();
            }
        }
    }
}

customElements.define('confirm-modal', ConfirmModal);

