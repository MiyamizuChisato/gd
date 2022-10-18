<template>
    <v-list p='!0' bg='!container-light !dark:container-dark'>
        <template v-if='auth'>
            <v-list-item p='!x-0'>
                <send-item :schedule='i' border='b-1 light-900 dark:dark-50' class='send-item' v-for='i in list' />
            </v-list-item>
        </template>
        <div v-else p='3' text='center danger-light dark:danger-dark'>只能查看自己的投递情况</div>
    </v-list>
</template>

<script lang='ts' setup>
import SendItem from './SendItem.vue'
import { useUserStore } from '../../../store/useUserStore'
import { computed, onMounted, ref } from 'vue'
import { listSendByUid } from '../../../api/scheduleApi'

const userStore = useUserStore()
const auth = computed(() => {
    return userStore.user.uid !== '' && (userStore.user.uid === userStore.currentUser.uid)
})
const list = ref(null)
onMounted(async () => {
    const { code, data } = await listSendByUid(userStore.user.uid)
    if (code > 0) {
        list.value = data
    }
})
</script>

<style scoped>
.send-item:last-child {
    border-bottom: none;
}
</style>
