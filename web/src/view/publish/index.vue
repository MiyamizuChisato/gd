<template>
    <div v-if='!appStore.loading' id='publish'>
        <div bg='container-light dark:container-dark' border='rounded-md' w='95% md:720px' m='x-auto y-5' p='5'>
            <p text='2rem strong-light dark:strong-dark' font='700'>发布招聘</p>
            <v-form text='2rem normal-light dark:normal-dark'>
                <div m='y-4'>
                    <v-text-field v-model='job.title' label='名称'></v-text-field>
                </div>
                <div m='y-4'>
                    <v-text-field v-model='job.company' label='公司'></v-text-field>
                </div>
                <div m='y-4'>
                    <v-text-field v-model='job.wage' label='工资/月' type='number'></v-text-field>
                </div>
            </v-form>
            <markdown-editor @change='contentEdit' :content='job.content' />
            <div m='y-6' text='end'>
                <v-btn @click='publishHandler' type='button' bg='!primary-light dark:primary-dark' flat>
                    <span text='light-200'>发布</span>
                </v-btn>
            </div>
        </div>
    </div>
</template>


<script lang='ts' setup>
import MarkdownEditor from '../../components/MarkdownEditor.vue'
import { useAppStore } from '../../store/useAppStore'
import onLoadFinished from '../../hooks/onLoadFinished'
import { reactive } from 'vue'
import { publish } from '../../api/jobApi'
import { ElNotification } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '../../store/useUserStore'

const appStore = useAppStore()
const router = useRouter()
const userStore = useUserStore()
onLoadFinished(() => {
})
const job = reactive({
    title: '',
    company: '',
    wage: '',
    content: '',
    avatar: userStore.user.avatar,
    nickname: userStore.user.nickname
})
const contentEdit = (value: string) => {
    job.content = value
}
const publishHandler = async () => {
    const { code, data } = await publish(job)
    if (code > 0) {
        ElNotification.success({
            position: 'bottom-right',
            title: '发布成功',
            message: '新的招聘信息已经发布成功，将为你自动跳转至预览页'
        })
        await router.push('/post/' + data)
    }
}
</script>

<style scoped>

</style>
