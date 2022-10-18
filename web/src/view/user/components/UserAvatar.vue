<template>
    <div position='relative'>
        <v-avatar size='88'>
            <img class='rounded-full' :src='userStore.currentUser.avatar' w='88px' h='88px' alt=''>
        </v-avatar>
        <label>
            <div v-if='edit' class='avatar-mask' v-ripple>
                <i display='block' i-mdi-camera-image text='white 1.75rem' />
                <input @change='fileHandler' type='file' accept='image/png' ref='avatarRef' hidden>
            </div>
        </label>
    </div>
    <v-dialog v-model='show' persistent>
        <div w='420px' border='rounded-md' h='420px' display='flex' flex='col' justify='center'
             bg='container-light dark:container-dark'>
            <div w='300px' h='300px' m='x-auto'>
                <vue-cropper ref='cropperRef'
                             :img='avatar'
                             :output-size='cropperOptions.outputSize'
                             :output-type='cropperOptions.outputType'
                             :can-scale='cropperOptions.canScale'
                             :can-move='cropperOptions.canMove'
                             :auto-crop='cropperOptions.autoCrop'
                             :auto-crop-width='cropperOptions.autoCropWidth'
                             :auto-crop-height='cropperOptions.autoCropHeight'
                             :fixed-box='cropperOptions.fixedBox'
                             :fixed='cropperOptions.fixed'
                             :fixed-number='cropperOptions.fixedNumber'
                             :full='cropperOptions.full'
                             :can-move-box='cropperOptions.canMoveBox'
                             :original='cropperOptions.original'
                             :center-box='cropperOptions.centerBox' />
            </div>
            <div text='center'>
                <v-btn :loading='loading' @click='uploadHandler' m='!y-2 !x-4'
                       bg='!primary-light !dark:primary-dark' flat>
                    <span text='light-200'>上传</span>
                </v-btn>
                <v-btn :disabled='loading' @click='cancelHandler' m='!y-2 !x-4' bg='!danger-light !dark:danger-dark'
                       flat>
                    <span text='light-200'>取消</span>
                </v-btn>
            </div>
        </div>
    </v-dialog>
</template>

<script lang='ts' setup>
import { reactive, ref } from 'vue'
import 'vue-cropper/dist/index.css'
import { VueCropper } from 'vue-cropper'
import { ElNotification } from 'element-plus'
import { updateUserAvatar } from '../../../api/userApi'
import { useUserStore } from '../../../store/useUserStore'
import { dataURLtoFile } from '../../../utils/image'

defineProps({
    edit: Boolean
})
const loading = ref(false)
const userStore = useUserStore()
const show = ref(false)
const cropperOptions = reactive({
    outputSize: 1,
    outputType: 'png',
    canScale: true,
    canMove: true,
    autoCrop: true,
    autoCropWidth: 108,
    autoCropHeight: 108,
    fixedBox: true,
    fixed: true,
    fixedNumber: [1, 1],
    full: true,
    canMoveBox: true,
    original: false,
    centerBox: true
})
const avatarRef = ref<any>()
const avatar = ref<any>()
const cropperRef = ref()
const uploadHandler = async () => {
    loading.value = true
    let file
    cropperRef.value.getCropData(async (value: any) => {
        file = dataURLtoFile(value, 'avatar.png')
        const fd = new FormData()
        fd.append('avatar', file)
        const { code, data } = await updateUserAvatar(fd)
        if (code > 0) {
            ElNotification.success({
                position: 'bottom-right',
                title: '上传完成',
                message: '头像已经更新成功'
            })
            userStore.saveToken(data)
            userStore.currentUser.avatar = userStore.user.avatar
            loading.value = false
            cancelHandler()
        }
    })

}
const fileHandler = (e: any) => {
    const file = e.target.files[0]
    if (file === undefined) {
        return
    }
    if (file.type !== 'image/png') {
        ElNotification.error({
            position: 'bottom-right',
            title: '错误的图片',
            message: '目前仅支持PNG格式的图片文件'
        })
        return
    }
    const reader = new FileReader()
    reader.readAsDataURL(file)
    reader.onload = () => {
        avatar.value = reader.result
    }
    show.value = true
}
const cancelHandler = () => {
    show.value = false
    avatar.value = null
    avatarRef.value.value = null
}
</script>

<style scoped>
.avatar-mask {
    border-radius: 50%;
    position: absolute;
    top: 50%;
    left: 50%;
    width: 108px;
    height: 108px;
    transform: translateX(-50%) translateY(-50%);
    background-color: rgba(0, 0, 0, 0.4);
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
