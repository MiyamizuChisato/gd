<template>
    <loading :show='appStore.loading' />
    <router-view />
</template>
<script lang='ts' setup>
import { useAppStore } from './store/useAppStore'
import Loading from './components/Loading.vue'

const appStore = useAppStore()
</script>
