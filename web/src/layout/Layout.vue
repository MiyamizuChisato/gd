<template>
    <div class='layout'>
        <header :class="appStore.showHeader?'top-0':'top--80px'">
            <the-header />
        </header>
        <main>
            <router-view />
        </main>
    </div>
    <footer>
        <the-footer />
    </footer>
</template>
<script lang='ts' setup>
import TheHeader from './TheHeader.vue'
import TheFooter from './TheFooter.vue'
import { useAppStore } from '../store/useAppStore'
import { onMounted } from 'vue'

let scrollT = 0
onMounted(() => {
    window.addEventListener('scroll', () => {
        if (appStore.pauseHeader) {
            appStore.pauseHeader = false
        } else {
            let st = document.documentElement.scrollTop || document.body.scrollTop || window.pageYOffset
            appStore.showHeader = st < scrollT
            scrollT = st
        }
    })
})
const appStore = useAppStore()
</script>
<style>
header {
    z-index: 2;
    position: sticky;
    transition: top 360ms linear;
    backdrop-filter: blur(20px);
    background-color: var(--c-header);
    box-shadow: 0 20px 40px -10px rgba(0, 0, 0, 0.05);
}

.layout {
    min-height: 100vh;
    min-width: 100vw;
    display: flex;
    flex-direction: column;
    background-attachment: fixed;
    background-size: cover;
    background-position: center;
    background-repeat: no-repeat;
}

footer {
}
</style>
