import 'uno.css'
import 'normalize.css'
import './assets/style/markdown-viewer.css'
import 'element-plus/dist/index.css'
import 'vuetify/styles'
import { createApp } from 'vue'
import App from './App.vue'
import { createPinia } from 'pinia'
import router from './router/index'
import { vuetify } from './plugins/vuetify'

const pinia = createPinia()
createApp(App).use(router).use(vuetify).use(pinia).mount('#app')
