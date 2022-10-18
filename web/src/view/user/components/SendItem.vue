<template>
    <v-list-item-action @click='to("/post/"+schedule.jid)' v-ripple p='!x-6 !y-4' display='flex' justify='between'>
        <div display='flex' items='center'>
            <div w='64px'>
                <img w='64px' h='64px' border='rounded-full' :src='schedule.user.avatar'>
            </div>
            <div m='x-4'>
                <p text='strong-light dark:strong-dark'>{{ schedule.job.title }}</p>
                <p text='normal-light dark:normal-dark'>{{ schedule.job.company }}</p>
                <p text='danger-light dark:danger-dark'>{{ schedule.job.wage }} ￥</p>
            </div>
        </div>
        <div text='end'>
            <p text='secondary-light dark:secondary-dark'>{{ schedule.createTime.substring(0, 10) }}</p>
            <div>
                <v-btn color='transparent' width='24px' height='24px' size='mini' icon flat>
                    <v-tooltip activator='parent' location='start'>
                        <span v-if='schedule.progress === 2'>通过简历</span>
                        <span v-if='schedule.progress === 3'>不太合适</span>
                        <span v-if='schedule.progress === 1'>等待结果</span>
                    </v-tooltip>
                    <i v-if='schedule.progress === 2' text='1.25rem success-light dark:success-light'
                       i-ep-circle-check-filled />
                    <i v-if='schedule.progress === 3' text='1.25rem danger-light dark:danger-light'
                       i-ep-circle-close-filled />
                    <i v-if='schedule.progress === 1' text='1.25rem primary-light dark:primary-light'
                       i-ep-info-filled />
                </v-btn>
            </div>
            <div>
                <v-btn @click.stop='to("/message/"+schedule.aid)' color='transparent' width='24px' height='24px' size='mini'
                       icon flat>
                    <i text='warning-light dark:warning-light ' i-mdi-message-reply-text />
                </v-btn>
            </div>
        </div>
    </v-list-item-action>
</template>

<script lang='ts' setup>
import { useRouter } from 'vue-router'

defineProps({
    schedule: Object
})
const router = useRouter()
const to = (path: string) => {
    router.push(path)
}
</script>

<style scoped>
.v-btn--disabled.v-btn--variant-elevated {
    color: transparent;
}
</style>
