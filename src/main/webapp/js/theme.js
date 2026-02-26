/**
 * Theme Toggle Functionality
 * Handles dark mode / light mode switching and persistence
 */

// Initialize theme based on local storage or system preference
function initializeTheme() {
    if (localStorage.theme === 'dark' || (!('theme' in localStorage) && window.matchMedia('(prefers-color-scheme: dark)').matches)) {
        document.documentElement.classList.add('dark');
    } else {
        document.documentElement.classList.remove('dark');
    }
}

// Toggle theme when button is clicked
function setupThemeToggle() {
    const themeToggleBtn = document.getElementById('theme-toggle');

    if (themeToggleBtn) {
        themeToggleBtn.addEventListener('click', () => {
            if (document.documentElement.classList.contains('dark')) {
                document.documentElement.classList.remove('dark');
                localStorage.theme = 'light';
            } else {
                document.documentElement.classList.add('dark');
                localStorage.theme = 'dark';
            }
        });
    }
}

// Initialize on DOM content loaded
document.addEventListener('DOMContentLoaded', () => {
    initializeTheme();
    setupThemeToggle();
});

// Also run immediately for faster initial load
initializeTheme();

