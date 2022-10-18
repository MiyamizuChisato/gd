import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Unocss from 'unocss/vite'
import { presetUno, presetAttributify, presetIcons } from 'unocss'

export default defineConfig({
    plugins: [vue(),
        Unocss({
            presets: [
                presetUno(),
                presetAttributify(),
                presetIcons()],
            theme: {
                colors: {
                    primary: {
                        light: '#1a73e8ff',
                        dark: '#1a73e8aa'
                    },
                    success: {
                        light: '#81c995ff',
                        dark: '#81c995aa'
                    },
                    warning: {
                        light: '#fdd663ff',
                        dark: '#fdd663aa'
                    },
                    danger: {
                        light: '#ee675cff',
                        dark: '#ee675caa'
                    },
                    strong: {
                        light: 'rgba(0,0,0,0.87)',
                        dark: '#ffffffff'
                    },
                    normal: {
                        light: '#4c4e4dff',
                        dark: '#ddddddff'
                    },
                    secondary: {
                        light: 'rgba(0,0,0,0.6)',
                        dark: 'rgba(255,255,255,0.7)'
                    },
                    screen: {
                        light: '#f4f4f4ff',
                        dark: '#171d20ff'
                    },
                    container: {
                        light: '#ffffffff',
                        dark: '#2f3133ff'
                    }
                }
            }
        })]
})
