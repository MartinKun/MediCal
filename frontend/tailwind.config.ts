import type { Config } from "tailwindcss";

const config: Config = {
  content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
    extend: {
      colors: {
        background: "var(--background)",
        foreground: "var(--foreground)",
      },
    },
    keyframes: {
      fadeIn: {
        "0%": {
          opacity: "0",
        },
        "100%": {
          opacity: "1",
        },
      },
      scaleUp: {
        "0%": {
          transform: "scale(0.95)",
        },
        "100%": {
          transform: "scale(1)",
        },
      },
      spin: {
        "0%": {
          transform: "rotate(0deg)",
        },
        "100%": {
          transform: "rotate(360deg)",
        },
      },
      slideInBottom: {
        "0%": {
          transform: "translateY(1000px);",
          opacity: "0;"
        },
        "100%": {
          transform: "translateY(0);",
          opacity: "1;"
        }
      },
    },
    animation: {
      fadeIn: "fadeIn 0.5s ease-out both",
      scaleUp: "scaleUp 0.5s ease-out both",
      spin: "spin 1s linear infinite",
      slideInBottom: "slideInBottom 0.5s cubic-bezier(0.250, 0.460, 0.450, 0.940) both;",
    },
  },
  plugins: [],
};
export default config;
