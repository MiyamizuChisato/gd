<template>
    <div @click='to("/post/"+job.jid)' v-ripple cursor='pointer' w='100%' bg='container-light dark:container-dark'
         border='rounded-md'
         p='x-6 y-4'>
        <div display='flex' justify='between' items='center'>
            <h1 class='post-card-title' text='strong-light dark:strong-dark 1.5rem' m='0'>{{ job.title }}</h1>
            <span flex='1' text='danger-light dark:danger-dark right'>{{ job.wage }}￥</span>
        </div>
        <p text='normal-light dark:normal-dark' m='y-2'>{{ job.company }}</p>
        <div display='flex' justify='between' items='center'>
            <div display='flex' items='center'>
                <img w='42px' h='42px' border='rounded-full'
                     :src='job.user.avatar'>
                <span m='x-4' text='secondary-light dark:secondary-dark'>{{ job.user.nickname }}</span>
            </div>
            <div>
                <v-btn @click.stop='toMessage' color='transparent' size='small' flat icon>
                    <i text='1.25rem primary-light dark:primary-dark' i-mdi-chat-processing></i>
                </v-btn>
            </div>
        </div>
    </div>
</template>

<script lang='ts' setup>
import { useUserStore } from '../store/useUserStore'

const props = defineProps({
    job: Object
})
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import { validateSession } from '../api/sessionApi'

const userStore = useUserStore()
const router = useRouter()
const to = (path: string) => {
    router.push(path)
}
const toMessage = async () => {
    const sender = userStore.user.uid
    const receiver = props.job!.uid
    if (sender !== receiver) {
        await validateSession(sender, receiver)
        to('/message/' + props.job!.uid)
    } else {
        ElNotification.warning({
            position: 'bottom-right',
            title: '注意',
            message: '请不要尝试和自己对话'
        })
    }
}
</script>

<style scoped>
.post-card-title {
    text-overflow: ellipsis;
    overflow: hidden;
    white-space: nowrap;
}
</style>
