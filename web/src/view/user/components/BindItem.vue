<template>
    <v-list-item-title display='flex' items='center' p='!x-6 !y-2'>
        <div display='flex' items='center'>
            <i m='t-1 r-4' :class='icon' />
            <span text='normal-light dark:normal-dark'>{{ label }}</span>
        </div>
        <template v-if='userStore.currentUser.uid === userStore.user.uid'>
            <div flex='1' display='flex' justify='end' v-if='edit'>
                <v-btn color='transparent' v-if='loading' :loading='true' flat>

                </v-btn>
                <label v-else>
                    <div v-ripple bg='primary-light dark:primary-dark' p='x-2' border='rounded-sm'>
                        <input @change='fileHandler' type='file' accept='application/pdf' ref='avatarRef' hidden>
                        <span text='light-200 0.8rem'>上传</span>
                    </div>
                </label>
            </div>
            <div v-else>
                <a :href='url' v-if='bind'
                   class='text-decoration-none text-primary-light dark:text-primary-dark'>我的简历</a>
                <span v-else text='strong-light dark:strong-dark'>未上传</span>
            </div>
        </template>
        <div v-else>
            非本人不予查看
        </div>
    </v-list-item-title>
</template>

<script lang='ts' setup>
import { computed, ref } from 'vue'
import { ElNotification } from 'element-plus'
import { updateUserResume } from '../../../api/userApi'
import { useUserStore } from '../../../store/useUserStore'

const userStore = useUserStore()
const props = defineProps({
    label: String,
    icon: String,
    edit: {
        default: false
    },
    url: String
})
const bind = computed(() => {
    return !(null === props.url)
})
const loading = ref(false)
const fileHandler = async (e: any) => {
    const file = e.target.files[0]
    if (file === undefined) {
        return
    }
    if (file.type !== 'application/pdf') {
        ElNotification.error({
            position: 'bottom-right',
            title: '错误的文件',
            message: '目前仅支持PDF格式的简历文件'
        })
        return
    }
    const fd = new FormData()
    fd.append('resume', file)
    loading.value = true
    const { code, data } = await updateUserResume(fd)
    if (code > 0) {
        ElNotification.success({
            position: 'bottom-right',
            title: '上传完成',
            message: '文件已经更新成功'
        })
        userStore.user.resume = data
        loading.value = false
        location.reload()
    }
}
</script>

<style scoped>

</style>
