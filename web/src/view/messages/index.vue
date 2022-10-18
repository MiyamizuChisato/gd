<template>
    <div v-if='list' id='messages'>
        <div v-if='list.length>0' border='rounded-md' m='y-5 x-auto' p='5' w='md:720px 90%'
             bg='container-light dark:container-dark'>
            <template v-for='i in list'>
                <div @click='to("/message/"+i.user.uid)' v-ripple display='flex' justify='between' items='center'
                     p='y-2 x-4'
                     border='!b-1 light-900 dark:dark-50'
                     class='message-item'>
                    <div display='flex' items='center'>
                        <img w='48px' h='48px' border='rounded-full'
                             :src='i.user.avatar'
                             alt=''>
                        <span text='normal-light dark:normal-dark' m='x-5'>{{ i.user.nickname }}</span>
                    </div>
                    <div v-if='i.session.unread > 0' select='none' display='flex' justify='center' items='center'
                         bg='danger-light dark:danger-dark'
                         w='24px' h='24px' p='1' border='rounded-full' text='light-200'>
                    <span>
                        {{ i.session.unread }}
                    </span>
                    </div>
                </div>
            </template>
        </div>
        <div display='flex' justify='center' items='center' m='y-6' v-else>
            <el-col :sm='12' :lg='6'>
                <el-result icon='info'>
                    <template #extra>
                        <div text='6 strong-light dark:strong-dark'>
                            你还没有发起过聊天
                        </div>
                    </template>
                </el-result>
            </el-col>
        </div>
    </div>
    <div display='flex' justify='center' items='center' m='y-6' v-else>
        <el-col :sm='12' :lg='6'>
            <el-result icon='error'>
                <template #extra>
                    <div text='6 strong-light dark:strong-dark'>
                        你还没有登录，请先登录
                    </div>
                </template>
            </el-result>
        </el-col>
    </div>
</template>
<script lang='ts' setup>
import onLoadFinished from '../../hooks/onLoadFinished'
import { ref } from 'vue'
import { getReceivers } from '../../api/userApi'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/useUserStore'
import { ElCol, ElResult } from 'element-plus'

const router = useRouter()
const list = ref(null)
const userStore = useUserStore()
onLoadFinished(async () => {
    if (!userStore.token) {
        return
    }
    const { data, code } = await getReceivers()
    if (code > 0) {
        list.value = data
    }
})
const to = (path: string) => {
    router.push(path)
}
</script>
<style scoped>
.message-item:last-child {
    border: transparent;
}
</style>
