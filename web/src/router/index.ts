import { createRouter, createWebHistory } from 'vue-router'
import Layout from '../layout/Layout.vue'
import Index from '../view/index/index.vue'
import { useAppStore } from '../store/useAppStore'

const routes = [
    {
        name: 'layout',
        path: '/',
        component: Layout,
        children: [
            {
                name: 'index',
                path: '/',
                component: Index,
                meta: {
                    menu: true,
                    title: '主页'
                }
            },
            {
                name: 'posts',
                path: '/posts',
                component: () => import('../view/posts/index.vue'),
                meta: {
                    menu: true,
                    title: '职位'
                }
            }, {
                name: 'search',
                path: '/search/:keyword',
                component: () => import('../view/search/index.vue'),
                meta: {
                    menu: false,
                }
            },
            {
                name: 'messages',
                path: '/messages',
                component: () => import('../view/messages/index.vue'),
                meta: {
                    menu: true,
                    title: '消息'
                }
            },
            {
                name: 'post',
                path: '/post/:id',
                component: () => import('../view/post/index.vue'),
                meta: {
                    menu: false
                }
            },
            {
                name: 'user',
                path: '/user/:id',
                component: () => import('../view/user/index.vue'),
                meta: {
                    menu: false
                }
            },
            {
                name: 'job-publish',
                path: '/publish',
                component: () => import('../view/publish/index.vue'),
                meta: {
                    menu: false
                }
            },
            {
                name: 'job-update',
                path: '/publish/update/:id',
                component: () => import('../view/publish/update.vue'),
                meta: {
                    menu: false
                }
            },
            {
                name: 'progress',
                path: '/progress/:id',
                component: () => import('../view/progress/index.vue'),
                meta: {
                    menu: false
                }
            },
            {
                name: 'message',
                path: '/message/:id',
                component: () => import('../view/message/index.vue'),
                meta: {
                    menu: false
                }
            }
        ]
    }
]
const router = createRouter({
    routes,
    history: createWebHistory()
})
router.beforeEach((to, from, next) => {
    if (to.name !== from.name) {
        const appStore = useAppStore()
        appStore.loading = true
    }
    next()
})
export default router
