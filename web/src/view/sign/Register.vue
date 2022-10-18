<template>
    <v-card p='!2' class='!sm:rounded-md' bg='container-light dark:container-dark'>
        <v-card-title>
            <div m='y-1'>
                <v-btn @click='dialogToggleHandler' m='x--4' bg='!warning-light ' text='2rem'
                       i-mdi-arrow-left-drop-circle />
            </div>
        </v-card-title>
        <v-card-subtitle>
            <div m='b-8'>
                <div display='flex' justify='between' items='center'>
                    <h1 font='600' text='2rem strong-light dark:strong-dark'>欢迎加入</h1>
                    <v-btn bg='!success-light !dark:success-dark' @click='loginToggleHandler' flat>
                        <span text='light-200'>登陆</span>
                    </v-btn>
                </div>
                <h2 font='500' text='1.5rem secondary-light dark:secondary-dark'>请输入以下信息注册</h2>
            </div>
        </v-card-subtitle>
        <v-card-text>
            <div w='sm:428px' m='b-20'>
                <v-form ref='formRef' v-model='valid' text='normal-light dark:normal-dark'>
                    <v-row p='y-4'>
                        <v-text-field v-model='user.email' :rules='rules.email' label='邮箱' placeholder='请输入邮箱'>
                            <template #append-inner>
                                <div position='relative' bottom='1.5'>
                                    <v-btn @click='codeSendHandler' variant='plain' cursor='pointer' rounded icon>
                                        <v-tooltip activator='parent' location='bottom'>
                                            发送验证码
                                        </v-tooltip>
                                        <i text='2rem primary-light dark:primary-dark' i-mdi-send-circle />
                                    </v-btn>
                                </div>
                            </template>
                        </v-text-field>
                    </v-row>
                    <v-row p='y-4'>
                        <v-text-field v-model='user.code' :rules='rules.code' label='验证码'
                                      placeholder='请输入邮箱验证码'>
                        </v-text-field>
                    </v-row>
                    <v-row p='y-4'>
                        <v-text-field v-model='user.password' :rules='rules.password' label='密码' type='password'
                                      placeholder='请输入密码'></v-text-field>
                    </v-row>
                    <v-row p='y-4'>
                        <v-text-field v-model='user.nickname' :rules='rules.nickname' label='昵称'
                                      placeholder='请输入昵称'></v-text-field>
                    </v-row>
                    <v-row p='y-4'>
                        <v-btn @click='registerHandler' size='large' rounded w='!full'
                               bg='!primary-light dark:primary-dark' flat>
                            <div display='flex' items='center'>
                                <span text='light-200' m='x-2'>注 册</span>
                                <i text='light-200 1.5rem' display='block' i-mdi-arrow-right-drop-circle />
                            </div>
                        </v-btn>
                    </v-row>
                </v-form>
            </div>
        </v-card-text>
    </v-card>
</template>
<script lang='ts' setup>
import rules, { regexp } from '../../utils/rules'
import { reactive, ref } from 'vue'
import { verifyEmail } from '../../api/mailApi'
import { register } from '../../api/userApi'
import { ElNotification } from 'element-plus'
import { useUserStore } from '../../store/useUserStore'

const userStore = useUserStore()
const emits = defineEmits(['dialogToggle', 'loginToggle'])
const dialogToggleHandler = () => {
    emits('dialogToggle')
}
const loginToggleHandler = () => {
    emits('loginToggle')
}
const codeSendHandler = async () => {
    if (regexp.email.test(user.email)) {
        const { code } = await verifyEmail(user.email)
        if (code > 0) {
            ElNotification.success({
                position: 'bottom-right',
                title: '操作成功',
                message: '验证码发送成功'
            })
        }
    } else {
        ElNotification.warning({
            position: 'bottom-right',
            title: '注意',
            message: '请正确输入邮箱地址后重试'
        })
    }
}
const registerHandler = async () => {
    formRef.value.validate()
    if (valid.value) {
        const { data, code } = await register(user)
        if (code > 0) {
            emits('dialogToggle')
            ElNotification.success({
                position: 'bottom-right',
                title: '注册成功',
                message: '已经为你自动登陆'
            })
            userStore.saveToken(data)
        }
    }
}
const valid = ref(false)
const formRef = ref()
const user = reactive({
    email: '',
    password: '',
    code: '',
    nickname: ''
})

</script>

<style scoped>

</style>
