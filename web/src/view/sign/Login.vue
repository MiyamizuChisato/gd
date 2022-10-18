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
                    <h1 font='600' text='2rem strong-light dark:strong-dark'>欢迎回来</h1>
                    <v-btn bg='!danger-light !dark:danger-dark' @click='loginToggleHandler' flat>
                        <span text='light-200'>注册</span>
                    </v-btn>
                </div>
                <h2 font='500' text='1.5rem secondary-light dark:secondary-dark'>请输入以下信息登陆</h2>
            </div>
        </v-card-subtitle>
        <v-card-text>
            <div w='sm:428px' m='b-20'>
                <v-form ref='formRef' v-model='valid' lazy-validation text='normal-light dark:normal-dark'>
                    <v-row p='y-4'>
                        <v-text-field :rules='rules.email' v-model='user.email' label='邮箱'
                                      placeholder='请输入邮箱' required />
                    </v-row>
                    <v-row p='y-4'>
                        <v-text-field :rules='rules.password' v-model='user.password' label='密码'
                                      type='password' placeholder='请输入密码' required />
                    </v-row>
                    <v-row p='y-4'>
                        <v-btn @click='loginHandler' size='large' rounded w='!full'
                               bg='!primary-light dark:primary-dark' flat>
                            <div display='flex' items='center'>
                                <span text='light-200' m='x-2'>登 录</span>
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
import { reactive, ref } from 'vue'
import rules from '../../utils/rules'
import { login } from '../../api/userApi'
import { ElNotification } from 'element-plus'
import { useUserStore } from '../../store/useUserStore'

const userStore = useUserStore()
const valid = ref(false)
const formRef = ref()
const user = reactive({
    email: '',
    password: ''
})
const emits = defineEmits(['dialogToggle', 'loginToggle'])
const loginHandler = async () => {
    formRef.value.validate()
    if (valid.value) {
        const { data, code } = await login(user)
        if (code > 0) {
            emits('dialogToggle')
            ElNotification.success({
                position: 'bottom-right',
                title: '登陆成功',
                message: '欢迎回来'
            })
            userStore.saveToken(data)
        }
    }
}
const dialogToggleHandler = () => {
    emits('dialogToggle')
}
const loginToggleHandler = () => {
    emits('loginToggle')
}

</script>
