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
                <v-btn @click='updateHandler' type='button' bg='!primary-light dark:primary-dark' flat>
                    <span text='light-200'>更新</span>
                </v-btn>
            </div>
        </div>
    </div>
</template>


<script lang='ts' setup>
import MarkdownEditor from '../../components/MarkdownEditor.vue'
import { useAppStore } from '../../store/useAppStore'
import onLoadFinished from '../../hooks/onLoadFinished'
import { ref } from 'vue'
import { check, publish } from '../../api/jobApi'
import { ElNotification } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'

const appStore = useAppStore()
const router = useRouter()
const route = useRoute()

onLoadFinished(async () => {
    const { data, code } = await check(route.params.id)
    if (code > 0) {
        job.value = data
    } else {
        await router.push('/error')
    }
})
const job = ref({
    title: '',
    company: '',
    wage: '',
    content: ''
})
const contentEdit = (value: string) => {
    job.value.content = value
}
const updateHandler = async () => {
    const { code, data } = await publish(job.value)
    if (code > 0) {
        ElNotification.success({
            position: 'bottom-right',
            title: '更新成功',
            message: '招聘信息修改成功，将为你自动跳转至预览页'
        })
        await router.push('/post/' + data)
    }
}
</script>

<style scoped>

</style>
