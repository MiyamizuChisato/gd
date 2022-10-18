<template>
    <div v-if='!appStore.loading' id='user'>
        <div m='x-auto y-5' w='95% md:720px'>
            <div class='user-card' p='y-8' border='rounded-md'
                 bg='container-light dark:container-dark' display='flex' flex='col'
                 items='center'>
                <div m='b-4'>
                    <user-avatar :edit='edit' />
                </div>
                <div v-if='userStore.isCurrent'>
                    <v-btn @click='edit = false' v-if='edit' bg='!danger-light !dark:danger-dark' size='small'
                           flat>
                        <span text='light-200'>退出编辑</span>
                    </v-btn>
                    <v-btn @click='edit = true' v-else bg='!primary-light !dark:primary-dark' size='small'
                           flat>
                        <span text='light-200'>编辑资料</span>
                    </v-btn>
                </div>
            </div>
            <div class='user-card' border='rounded-md' overflow='!hidden'
                 bg='container-light dark:container-dark' m='t-6 b-3'>
                <v-tabs v-model='tab' slider-color='error' fixed-tabs>
                    <v-tab value='info'>
                        <div display='flex' items='center'>
                            <i display='block' text='1.5rem danger-light dark:danger-dark'
                               i-mdi-text-box-search />
                            <span p='x-2' text='normal-light dark:normal-dark'>资料</span>
                        </div>
                    </v-tab>
                    <v-tab value='send'>
                        <div display='flex' items='center'>
                            <i display='block' text='1.5rem success-light dark:success-dark'
                               i-mdi-send />
                            <span p='x-2' text='normal-light dark:normal-dark'>投送</span>
                        </div>
                    </v-tab>
                    <v-tab value='publish'>
                        <div display='flex' items='center'>
                            <i display='block' text='1.5rem primary-light dark:primary-dark'
                               i-mdi-publish />
                            <span p='x-2' text='normal-light dark:normal-dark'>发布</span>
                        </div>
                    </v-tab>
                </v-tabs>
            </div>
            <div class='user-card' border='rounded-md' overflow='!hidden'>
                <v-window v-model='tab'>
                    <v-window-item value='info'>
                        <info-window :edit='edit' />
                    </v-window-item>
                    <v-window-item value='send'>
                        <send-window />
                    </v-window-item>
                    <v-window-item value='publish'>
                        <publish-window :uid='uid' />
                    </v-window-item>
                </v-window>
            </div>
        </div>
    </div>
</template>

<script lang='ts' setup>
import { ref } from 'vue'
import { useAppStore } from '../../store/useAppStore'
import onLoadFinished from '../../hooks/onLoadFinished'
import UserAvatar from './components/UserAvatar.vue'
import InfoWindow from './components/InfoWindow.vue'
import SendWindow from './components/SendWindow.vue'
import PublishWindow from './components/PublishWindow.vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../../store/useUserStore'
import { getUser, watchUser } from '../../api/userApi'

const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const appStore = useAppStore()

const tab = ref<string>()
const edit = ref(false)
const uid = ref(route.params.id)
const fetchUser = async () => {
    if (uid.value === undefined) {
        return
    }
    let result
    if (String(userStore.user.uid) === uid.value) {
        result = await getUser()
    } else {
        result = await watchUser(uid.value)
    }
    if (result.code > 0) {
        userStore.currentUser = result.data
    }
}
onLoadFinished(async () => {
    await fetchUser()
})
</script>
<style scoped>
#user .user-card {
    box-shadow: 0 20px 40px -15px rgba(0, 0, 0, 0.05);
}
</style>
