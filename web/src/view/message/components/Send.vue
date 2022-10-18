<template>
    <div display='flex' items='center' justify='end' m='y-2 md:l-48px l-32px'>
        <div h='full' p='x-2 y-3' m='x-4' border='rounded-md' bg='screen-light dark:screen-dark'>
            <span text='normal-light dark:normal-dark'>{{ content }}</span>
        </div>
        <img w='md:48px 32px' h='md:48px 32px' border='rounded-full' :src='userStore.user.avatar' alt=''>
    </div>
</template>

<script lang='ts' setup>
import { useUserStore } from '../../../store/useUserStore'

const userStore = useUserStore()
defineProps({
    content: String
})
</script>

<style scoped>

</style>
