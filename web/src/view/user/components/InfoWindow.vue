<template>
    <v-list p='!0' bg='!container-light !dark:container-dark'>
        <v-list-item p='!x-0'>
            <info-item class='info-item' border='b-1 light-900 dark:dark-50' label='邮箱' index='email'
                       :text='user.email' @asdasd='editingHandler' icon='i-mdi-email-outline text-red'
                       :modify='false' />
            <info-item class='info-item' border='b-1 light-900 dark:dark-50' label='昵称' index='nickname'
                       :text='user.nickname' :edit='edit' :rules='rules.nickname' :editing='editing'
                       icon='i-uil-letter-japanese-a text-indigo' />
            <info-item class='info-item' border='b-1 light-900 dark:dark-50' label='签名' index='sign'
                       :text='user.sign' :edit='edit' :editing='editing'
                       icon='i-mdi-fountain-pen-tip text-pink' />
            <info-item class='info-item' border='b-1 light-900 dark:dark-50' label='密码' index='password'
                       :text='user.password' :edit='edit' :rules='rules.password' :editing='editing' :password='true'
                       icon='i-mdi-lock-outline text-purple' />
            <bind-item label='简历' :url='user.resume' :edit='edit'
                       class='info-item' border='b-1 light-900 dark:dark-50'
                       icon='i-mdi-document text-1.25rem text-yellow' display='flex' justify='between' />
        </v-list-item>
    </v-list>
</template>

<script lang='ts' setup>
import InfoItem from './InfoItem.vue'
import BindItem from './BindItem.vue'
import { computed, onUnmounted, ref } from 'vue'
import { useUserStore } from '../../../store/useUserStore'
import rules from '../../../utils/rules'

defineProps({
    edit: {
        default: false
    }
})
const userStore = useUserStore()
const user = computed(() => userStore.currentUser)
const editing = ref(false)
const editingHandler = () => {
    editing.value = true
}
onUnmounted(() => {
    userStore.editing = false
})
</script>

<style scoped>
.info-item:last-child {
    border-bottom: none;
}
</style>
