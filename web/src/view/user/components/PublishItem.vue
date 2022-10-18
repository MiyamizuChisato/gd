<template>
    <v-list-item-action @click='to("/post/"+props.job.jid)' v-ripple p='!x-6 !y-4' display='flex' justify='between'>
        <div display='flex' items='center'>
            <div>
                <p text='strong-light dark:strong-dark'>{{ job.title }}</p>
                <p text='normal-light dark:normal-dark'>{{ job.company }}</p>
                <p text='danger-light dark:danger-dark'>{{ job.wage }} ￥</p>
            </div>
        </div>
        <div text='end'>
            <div m='t-1' v-if='userStore.user.uid === props.job.uid'>
                <v-btn m='!l-2' @click.stop='to("/progress/"+props.job.jid)'
                       bg='!primary-light !dark:primary-dark'
                       size='small' flat>
                    <span text='light-200'>管理</span>
                </v-btn>
                <v-btn @click.stop='to("/publish/update/"+props.job.jid)' m='!l-2'
                       bg='!success-light !dark:success-dark' size='small' flat>
                    <span text='light-200'>修改</span>
                </v-btn>
                <v-btn @click.stop='dialog = true' m='!l-2' bg='!danger-light !dark:danger-dark'
                       size='small'
                       flat>
                    <span text='light-200'>删除</span>
                </v-btn>
            </div>
            <div text='secondary-light dark:secondary-dark' v-else>{{ props.job.createTime.substring(0, 10) }}</div>
        </div>
        <v-dialog v-model='dialog' persistent>
            <div bg='container-light dark:container-dark' p='4' border='rounded-md'>
                <div text='1.75rem strong-light dark:strong-dark'>
                    确认删除
                    <span text='danger-light dark:danger-dark'>{{ job.title }}</span>
                    吗？
                </div>
                <div display='flex' justify='end' m='2'>
                    <v-btn m='l-10' bg='!danger-light !dark:danger-dark' @click='deleteHandler(job.jid)'><span
                        text='light-200'>确认</span>
                    </v-btn>
                    <v-btn m='l-10' bg='!primary-light !dark:primary-dark' @click='dialog = false'><span
                        text='light-200'>取消</span></v-btn>
                </div>
            </div>
        </v-dialog>
    </v-list-item-action>
</template>

<script lang='ts' setup>
import { useRouter } from 'vue-router'
import { deleteById } from '../../../api/jobApi'
import { ElNotification } from 'element-plus'
import { ref } from 'vue'
import { useUserStore } from '../../../store/useUserStore'

const userStore = useUserStore()
const dialog = ref(false)
const emits = defineEmits(['deleted'])
const props = defineProps({
    job: Object
})
const router = useRouter()
const to = (path: string) => {
    router.push(path)
}
const deleteHandler = async (id: string) => {
    const { code } = await deleteById(id)
    if (code > 0) {
        ElNotification.success({
            position: 'bottom-right',
            title: '删除成功'
        })
        emits('deleted')
    }
    dialog.value = false
}
</script>

<style scoped>
.v-btn--disabled.v-btn--variant-elevated {
    color: transparent;
}
</style>
