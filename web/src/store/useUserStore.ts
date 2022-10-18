import { defineStore } from 'pinia'
import jwtDecode from 'jwt-decode'
import { updateUser } from '../api/userApi'
import { ElNotification } from 'element-plus'

export const useUserStore = defineStore('user', {
    state: () => {
        const token = localStorage.getItem('token')
        let currentUser: any = {
            uid: '0'
        }
        let user: any = {
            uid: '',
            avatar: '',
            nickname: ''
        }
        if (token !== null && token !== '') {
            user = jwtDecode(token)
        }
        return { token, user, editing: false, currentUser }
    },
    getters: {
        isLogin(state) {
            return state.token !== null && state.token !== ''
        },
        isCurrent(state) {
            if (state.currentUser.uid !== undefined) {
                return state.user.uid === state.currentUser.uid
            }
            return false
        }
    },
    actions: {
        saveToken(token: string) {
            this.token = token
            this.$patch({
                user: {
                    ...jwtDecode(token)
                }
            })
            localStorage.setItem('token', token)
        },
        removeToken() {
            localStorage.removeItem('token')
            window.location.href = '/'
        },
        async updateUser() {
            const { code, data } = await updateUser(this.currentUser)
            if (code > 0) {
                ElNotification.success({
                    position: 'bottom-right',
                    title: '更新成功',
                    message: '你的资料已经被更新'
                })
                this.$patch({
                    currentUser: {
                        ...jwtDecode(data)
                    }
                })
                this.saveToken(data)
                this.user.password = null
            }
        }
    }
})
