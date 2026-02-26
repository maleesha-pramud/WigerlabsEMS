/**
 * Tailwind CSS Configuration
 * This configuration extends Tailwind's default theme with custom colors and settings
 */

tailwind.config = {
    darkMode: "class",
    theme: {
        extend: {
            colors: {
                primary: "#D4B157", // Gold color from logo/active states
                sidebar: "#2F3B43",
                "background-light": "#FDFBF7",
                "background-dark": "#121212",
                "surface-light": "#FFFFFF",
                "surface-dark": "#1E1E1E",
                "card-green-light": "#E0F2E9",
                "card-green-dark": "#163326",
                "card-pink-light": "#FCE4E4",
                "card-pink-dark": "#3D1A1A",
            },
            fontFamily: {
                display: ['Plus Jakarta Sans', 'sans-serif'],
                body: ['Plus Jakarta Sans', 'sans-serif'],
            },
            borderRadius: {
                DEFAULT: "0.5rem",
                'xl': '1rem',
                '2xl': '1.5rem',
                '3xl': '2rem',
            },
        },
    },
};

