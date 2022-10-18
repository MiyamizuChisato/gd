<template>
    <div id='post' v-if='show'>
        <div class='content' border='rounded-md' overflow='hidden' w='95% md:720px'
             m='x-auto y-5'>
            <div class='article-content' p='x-8 y-6' bg='container-light dark:container-dark'>
                <div m='b-16' bg='container-light dark:container-dark'>
                    <h1 text='2rem strong-light dark:strong-dark' m='b-10'>
                        {{ model.job.title }}
                    </h1>
                    <div display='flex' justify='between' items='center' text='normal-light dark:normal-dark'>
                        <div display='flex' items='center'>
                            <div display='flex' items='center' border='rounded-full' overflow='hidden'>
                                <img w='56px' h='56px' :src='model.user.avatar' />
                            </div>
                            <div m='x-4'>
                                <div style='line-height: 24px' h='24px'>{{ model.user.nickname }}</div>
                                <div style='line-height: 24px' text='0.6rem' h='24px'>{{ model.job.createTime }}</div>
                            </div>
                        </div>
                        <div text='end'>
                            <div text='strong-light dark:strong-dark'>{{ model.job.company }}</div>
                            <div text='danger-light dark:danger-dark'>{{ model.job.wage }} ￥</div>
                            <div display='flex' justify='end' items='center'>
                                <div i-mdi-fire p='x-4'></div>
                                <div>{{ model.job.hot }}</div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class='markdown-viewer'>
                    <md-editor text='normal-light dark:normal-dark'
                               bg='!container-light !dark:container-dark'
                               :previewOnly='true' v-model='model.job.content' />
                </div>
            </div>
        </div>
        <div class='side'>
            <div m='y-2'>
                <v-btn bg='!light-700 !dark:dark' @click='sendResumeHandler' flat icon>
                    <i text='1.5rem strong-light dark:strong-dark' i-ri-send-plane-2-fill />
                </v-btn>
            </div>
            <div m='y-2'>
                <v-btn @click='toMessage' bg='!light-700 !dark:dark ' flat icon>
                    <i text='1.5rem strong-light dark:strong-dark' i-mdi-chat-processing />
                </v-btn>
            </div>
            <div m='y-2'>
                <v-btn @click='router.back' bg='!light-700 !dark:dark ' flat icon>
                    <i text='1.5rem strong-light dark:strong-dark' i-ri-arrow-go-back-line />
                </v-btn>
            </div>
        </div>
    </div>
</template>


<script lang='ts' setup>
import MdEditor from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { onBeforeUnmount, reactive, ref } from 'vue'
import onLoadFinished from '../../hooks/onLoadFinished'
import { useRoute, useRouter } from 'vue-router'
import { getById } from '../../api/jobApi'
import { useUserStore } from '../../store/useUserStore'
import { sendResume } from '../../api/scheduleApi'
import { ElNotification } from 'element-plus'
import { validateSession } from '../../api/sessionApi'

const toMessage = async () => {
    const sender = userStore.user.uid
    const receiver = model.user.uid
    if (sender !== receiver) {
        await validateSession(sender, receiver)
        await router.push('/message/' + receiver)
    } else {
        ElNotification.warning({
            position: 'bottom-right',
            title: '注意',
            message: '请不要尝试和自己对话'
        })
    }
}
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const model = reactive({
    job: {
        jid: '',
        content: '',
        title: '',
        createTime: '',
        hot: '',
        company: '',
        wage: ''
    },
    user: {
        uid: '',
        nickname: '',
        avatar: ''
    }
})
const show = ref(false)
onLoadFinished(async () => {
    const { data, code } = await getById(route.params.id)
    if (code > 0) {
        model.job = data.job
        model.user = data.user
    } else {
        await router.push('/error')
    }
    show.value = true
})
const sendResumeHandler = async () => {
    if (userStore.user.uid === '' || userStore.user.uid === null) {
        ElNotification.warning({
            position: 'bottom-right',
            title: '请登录',
            message: '请登陆之后在进行该操作'
        })
        return
    } else if (userStore.user.uid === model.user.uid) {
        ElNotification.warning({
            position: 'bottom-right',
            title: '不能投递',
            message: '请勿投递简历给自己'
        })
        return
    }
    const schedule = {
        jid: model.job.jid,
        aid: model.user.uid,
        bid: userStore.user.uid,
        progress: 1,
        avatar: model.user.avatar,
        title: model.job.title,
        company: model.job.company,
        wage: model.job.wage
    }
    const { code } = await sendResume(schedule)
    if (code > 0) {
        ElNotification.success({
            position: 'bottom-right',
            title: '投递成功',
            message: '近期请注意绑定的邮箱，我们将会以邮件的形式推送结果'
        })
    }
}
</script>

<style>
#post .content {
    box-shadow: 0 20px 40px -15px rgba(0, 0, 0, 0.05);
}

#post blockquote {
    background-color: var(--c-bg);
    color: var(--c-secondary);
}

#md-editor-v3-preview h1, h2, h3, h4, h5, h6 {
    color: var(--c-strong);
}

.side {
    position: fixed;
    bottom: 10vh;
    right: 12vw;
}
</style>
