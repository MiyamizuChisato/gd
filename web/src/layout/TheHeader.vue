<template>
    <div h='56px' display='flex' justify='center' items='center'>
        <div position='md:absolute' left='md:20' text='strong-light dark:strong-dark'>毕业设计</div>
        <div position='lt-md:absolute' left='1' display='md:none'>
            <v-btn @click='drawer = !drawer' color='transparent' size='small' icon flat>
                <i text='6 strong-light dark:strong-dark' i-material-symbols-menu-rounded />
            </v-btn>
        </div>
        <div display='lt-md:none flex'>
            <template v-for='r in routes'>
                <div m='x-2' v-if='r.meta.menu'>
                    <v-btn p='!x-8' rounded='0' color='transparent' height='56px'
                           @click='to(r.path)'
                           :class='r.name===route.name?"selected":""'
                           flat>
                    <span text='strong-light dark:strong-dark'>
                        {{ r.meta.title }}
                    </span>
                    </v-btn>
                </div>
            </template>
        </div>
        <div display='lt-md:none' position='absolute' right='20'>
            <v-btn m='l-8' color='transparent' @click='appStore.colorModeToggle' flat>
                <div text='1.5rem strong-light dark:strong-dark' i-line-md-light-dark-loop></div>
            </v-btn>
            <template v-if='isLogin'>
                <v-btn @click='to("/publish")' m='l-4' bg='!success-light !dark:success-dark' flat>
                    <span text='light-200'>招聘</span>
                </v-btn>
                <v-btn m='l-8' color='transparent' flat icon>
                    <v-avatar size='48px'>
                        <img class='rounded-full' :src='user.avatar' w='36px' h='36px' alt=''>
                    </v-avatar>
                    <v-menu position='!fixed' transition='slide-y-transition' activator='parent'>
                        <v-list shadow='!md' bg='!container-light !dark:container-dark'>
                            <v-list-item class='!rounded-xl'>
                                <v-list-item-title>
                                    <v-btn w='!full' @click='to(`/user/${user.uid}`)' rounded='lg' color='transparent'
                                           flat>
                                        <i text='1.25rem strong-light dark:strong-dark' m='r-3' display='block'
                                           i-ri-user-fill />
                                        <div text='normal-light dark:normal-dark'>我的账号</div>
                                    </v-btn>
                                </v-list-item-title>
                                <v-list-item-title m='t-1'>
                                    <v-btn @click='userStore.removeToken' rounded='lg' color='transparent' flat>
                                        <i text='1.25rem strong-light dark:strong-dark' m='r-3' display='block'
                                           i-ri-logout-circle-fill />
                                        <div text='normal-light dark:normal-dark'>退出</div>
                                    </v-btn>
                                </v-list-item-title>
                            </v-list-item>
                        </v-list>
                    </v-menu>
                </v-btn>
            </template>
            <v-btn m='l-8' bg='!primary-light !dark:primary-dark' @click='dialogToggle' v-else flat>
                <span text='light-200'>登陆</span>
            </v-btn>
        </div>
    </div>
    <v-dialog :fullscreen='mobile' v-model='dialog.display'
              transition='dialog-bottom-transition' persistent>
        <login @dialog-toggle='dialogToggle' @login-toggle='loginToggle' v-if='dialog.login' />
        <register @dialog-toggle='dialogToggle' @login-toggle='loginToggle' v-else />
    </v-dialog>
    <teleport to='body'>
        <el-drawer :with-header='false' v-model='drawer' size='50%'>
            <div>
                <div display='flex' justify='center' items='center'>
                    <v-btn color='transparent' @click='appStore.colorModeToggle' size='small' icon flat>
                        <div text='1.5rem strong-light dark:strong-dark' i-line-md-light-dark-loop></div>
                    </v-btn>
                </div>
                <div m='y-6' display='flex' justify='center' items='center' v-if='isLogin'>
                    <img class='rounded-full' :src='user.avatar' w='88px' h='88px' alt=''>
                </div>
                <div>
                    <template v-for='r in routes'>
                        <div display='flex' justify='center' m='y-2' v-if='r.meta.menu'>
                            <v-btn p='!x-8' rounded='0' color='transparent' height='56px'
                                   @click='to(r.path)'
                                   :class='r.name===route.name?"selected":""'
                                   flat>
                                <span text='strong-light dark:strong-dark'>
                                    {{ r.meta.title }}
                                </span>
                            </v-btn>
                        </div>
                    </template>
                </div>
                <div w='100%' position='absolute' bottom='2' left='0'>
                    <div display='flex' justify='center'>
                        <div w='full' p='x-2' display='flex' flex='col' justify='center' items='center' v-if='isLogin'>
                            <v-btn block m='y-2' @click='to("/publish")' bg='!success-light !dark:success-dark' flat>
                                <span text='light-200'>毕业设计</span>
                            </v-btn>
                            <v-btn @click='to(`/user/${user.uid}`)' block m='y-2'
                                   bg='!primary-light !dark:primary-dark'>
                                <span text='light-200'>我的信息</span>
                            </v-btn>
                            <v-btn @click='userStore.removeToken' block m='y-2' bg='!danger-light !dark:danger-dark'>
                                <span text='light-200'>退出登录</span>
                            </v-btn>
                        </div>

                        <v-btn v-else m='l-8' bg='!primary-light !dark:primary-dark' @click='dialogToggle' flat>
                            <span text='light-200'>登陆</span>
                        </v-btn>
                    </div>
                </div>
            </div>
        </el-drawer>
    </teleport>
</template>

<script lang='ts' setup>
import { useRoute, useRouter } from 'vue-router'
import { computed, reactive, ref } from 'vue'
import { useDisplay } from 'vuetify'
import { useAppStore } from '../store/useAppStore'
import { useUserStore } from '../store/useUserStore'
import Login from '../view/sign/Login.vue'
import Register from '../view/sign/Register.vue'
import { ElDrawer } from 'element-plus'

const { mobile } = useDisplay()
const appStore = useAppStore()
const userStore = useUserStore()
const route = useRoute()
const router = useRouter()
const routes = router.options.routes[0].children
const user = computed(() => userStore.user)
const isLogin = computed(() => userStore.isLogin)
const drawer = ref(false)
const to = (path: string) => {
    drawer.value = false
    router.push(path)
}
const dialog = reactive({
    display: false,
    login: true
})
const dialogToggle = () => {
    drawer.value = false
    dialog.display = !dialog.display
    appStore.pauseHeader = true
}
const loginToggle = () => {
    dialog.login = !dialog.login
}
</script>
<style>
button.selected {
    background: var(--c-bg-selected) !important;
}

button.selected span {
    font-weight: bolder;
}

.el-drawer {
    background-color: var(--c-container) !important;
}
</style>
