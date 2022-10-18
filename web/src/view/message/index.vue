<template>
    <div id='message' w='90% md:720px' bg='container-light dark:container-dark' m='x-auto y-5'
         border='rounded-md'
         p='x-4 y-2'>
        <div display='flex' justify='center' items='center' p='x-2' m='y-5' position='relative'>
            <div position='absolute' left='0'>
                <v-btn @click='back' color='transparent' size='small' icon flat>
                    <i text='6 normal-light dark:normal-dark' i-mdi-arrow-left-bold-circle />
                </v-btn>
            </div>
            <div text='5 normal-light dark:normal-dark'>
                与{{ user.nickname }}的聊天
            </div>
        </div>
        <div ref='chatBox' h='400px' overflow='auto' p='r-5'>
            <template v-for='i in chats'>
                <send :content='i.content' v-if='i.sender===userStore.user.uid' />
                <receive :content='i.content' :avatar='user.avatar' v-else />
            </template>
        </div>
        <v-divider m='y-5' bg='secondary-light dark:secondary-dark' />
        <div display='flex'>
            <v-textarea v-model='content' rows='3' text='!strong-light !dark:strong-dark' />
            <v-btn @click='sendHandler' bg='!primary-light dark:primary-dark' m='x-2' flat>
                <span text='light-200'>发送</span>
            </v-btn>
        </div>
    </div>
</template>
<script lang='ts' setup>
import { useUserStore } from '../../store/useUserStore'

type chat = {
    sender: string,
    receiver: string | string [],
    content: string
}
import onLoadFinished from '../../hooks/onLoadFinished'
import Send from './components/Send.vue'
import Receive from './components/Receive.vue'
import { nextTick, onBeforeUnmount, onMounted, onUpdated, ref, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import { watchUser } from '../../api/userApi'
import { getUnreadMessage } from '../../api/messageApi'

const chatBox = ref<HTMLElement>()
const chats = ref<any>(null)
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const content = ref('')
const user = ref({
    avatar: '',
    nickname: ''
})
const back = () => {
    router.back()
}
//@ts-ignore
chats.value = JSON.parse(localStorage.getItem('chat:' + userStore.user.uid + '-' + route.params.id)) || []
onLoadFinished(async () => {
    const { data, code } = await watchUser(route.params.id)
    if (code > 0) {
        user.value = data
    }
})
let ws: WebSocket
onMounted(() => {
    ws = new WebSocket('ws://localhost:8080/chat/sender/' + userStore.user.uid + '/receiver/' + route.params.id)
    ws.onopen = async () => {
        const { code, data } = await getUnreadMessage(route.params.id)
        if (code > 0 && data.length > 0) {
            chats.value.push(...data)
            localStorage.setItem('chat:' + userStore.user.uid + '-' + route.params.id, JSON.stringify(chats.value))
        }
    }
    ws.onmessage = (event) => {
        const message = JSON.parse(event.data)
        chats.value.push(message)
        localStorage.setItem('chat:' + userStore.user.uid + '-' + route.params.id, JSON.stringify(chats.value))
    }
    chatBox.value!.scrollTo({
        top: chatBox.value!.scrollHeight
    })
})
onBeforeUnmount(() => {
    ws.close()
    auto()
})
const auto = watch(() => chats.value, async () => {
    await nextTick()
    chatBox.value!.scrollTo({
        top: chatBox.value!.scrollHeight,
        behavior: 'smooth'
    })
}, { deep: true })
const sendHandler = () => {
    if (content.value === '') {
        ElNotification.warning({
            position: 'bottom-right',
            title: '不可发送',
            message: '请勿发送空消息'
        })
        return
    }
    const json: chat = {
        sender: userStore.user.uid,
        receiver: route.params.id,
        content: content.value
    }
    content.value = ''
    ws.send(JSON.stringify(json))
}
</script>
<style scoped>
#message {
}
</style>
