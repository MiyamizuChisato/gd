<template>
    <div id='progress' w='md:720px 95%' p='y-5' border='rounded-md' m='y-5 x-auto'
         bg='container-light dark:container-dark'>
        <div v-if='meta' text='center 1.25rem strong-light dark:strong-dark'>
            {{ meta.job.title }}-{{ meta.job.company }}
        </div>
        <div m='y-5' text='center strong-light dark:strong-dark' v-if='list.length === 0'>
            暂无投递的简历
        </div>
        <v-list bg='container-light dark:container-dark'>
            <v-list-item v-ripple m='x-2' v-for='i in list'>
                <div display='flex' justify='between' items='center'>
                    <div display='flex' justify='center' items='center'>
                        <img w='64px' h='64px' border='rounded-full' :src='i.user.avatar'>
                        <div m='x-5' text='secondary-light dark:secondary-dark'>{{ i.user.nickname }}</div>
                    </div>
                    <div text='right'>
                        <v-btn @click='toMessage(i.user.uid)' bg='!primary-light dark:primary-dark' m='l-2 y-1'
                               flat>
                            <span text='light-200'>与他聊天</span>
                        </v-btn>
                        <a v-if='i.user.resume' :href='i.user.resume' class='text-light-200'>
                            <v-btn bg='!warning-light dark:warning-dark' m='l-2 y-1' flat>
                                <span text='light-200'>下载简历</span>
                            </v-btn>
                        </a>
                        <v-btn @click='msg' v-else bg='!warning-light dark:warning-dark' m='l-2 y-1' flat>
                            <span text='light-200'>暂无简历</span>
                        </v-btn>
                        <v-btn @click.stop='progressHandler(i.user.uid,"2")' bg='!success-light dark:success-dark'
                               m='l-2 y-1' flat>
                            <span text='light-200'>简历合格</span>
                        </v-btn>
                        <v-btn @click.stop='progressHandler(i.user.uid,"3")' bg='!danger-light dark:danger-dark'
                               m='l-2 y-1'
                               flat><span
                            text='light-200'>简历淘汰</span>
                        </v-btn>
                    </div>
                </div>
            </v-list-item>
        </v-list>
    </div>
</template>
<script lang='ts' setup>
import onLoadFinished from '../../hooks/onLoadFinished'
import { listSendByJid, progress } from '../../api/scheduleApi'
import { useRouter, useRoute } from 'vue-router'
import { ref } from 'vue'
import { ElNotification } from 'element-plus'
import { getById } from '../../api/jobApi'
import { validateSession } from '../../api/sessionApi'
import { useUserStore } from '../../store/useUserStore'

const router = useRouter()
const to = (path: string) => {
    router.push(path)
}
const msg = () => {
    ElNotification.info({
        position: 'bottom-right',
        title: '消息',
        message: '去通知他上传简历吧'
    })
}
const userStore = useUserStore()
const toMessage = async (uid: number) => {
    const sender = userStore.user.uid
    if (sender !== uid) {
        await validateSession(sender, uid + '')
        await router.push('/message/' + uid)
    } else {
        ElNotification.warning({
            position: 'bottom-right',
            title: '注意',
            message: '请不要尝试和自己对话'
        })
    }
}
const route = useRoute()
const list = ref([])
const fetchList = async () => {
    const { code, data } = await listSendByJid(route.params.id)
    if (code > 0) {
        list.value = data
    }
}
const meta = ref(null)
onLoadFinished(async () => {
    await fetchList()
    const { data } = await getById(route.params.id)
    meta.value = data
})
const progressHandler = async (bid: string, step: '2' | '3') => {
    const { code } = await progress(route.params.id, bid, step)
    if (code > 0) {
        ElNotification.success({
            position: 'bottom-right',
            title: '操作成功'
        })
        await fetchList()
    }
}
</script>
