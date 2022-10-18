import { defineStore } from 'pinia'

export const useAppStore = defineStore('app', {
    state: () => {
        return {
            darkMode: localStorage.getItem('theme') === 'dark',
            loading: false,
            showHeader: true,
            pauseHeader: false
        }
    },
    getters: {},
    actions: {
        colorModeToggle() {
            document.documentElement.classList.toggle('dark')
            this.darkMode = !this.darkMode
            if (this.darkMode) {
                localStorage.setItem('theme', 'dark')
            } else {
                localStorage.setItem('theme', 'light')
            }
        }
    }
})
