import { nextTick, onBeforeMount } from 'vue'
import { useAppStore } from '../store/useAppStore'

export const sleep = (delay: number) => {
    return new Promise(resolve => setTimeout(resolve, delay))
}
export default function onLoadFinished(fn: Function, autoClose = true) {
    const appStore = useAppStore()
    onBeforeMount(async () => {
        if (autoClose) {
            try {
                await fn()
                await nextTick()
            } finally {
                await sleep(0)
                appStore.$patch({
                    loading: false
                })
            }
        } else {
            fn()
        }
    })
}
