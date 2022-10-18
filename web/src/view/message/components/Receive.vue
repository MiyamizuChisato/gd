<template>
    <div display='flex' items='center' justify='start' m='y-2 md:r-48px r-32px'>
        <img w='md:48px 32px' h='md:48px 32px' border='rounded-full'
             :src='avatar'
             alt=''>
        <div h='full' p='x-2 y-3' m='x-4' border='rounded-md' bg='screen-light dark:screen-dark'>
            <span text='normal-light dark:normal-dark'>{{ content }}</span>
        </div>
    </div>
</template>

<script lang='ts' setup>
defineProps({
    content: String,
    avatar: String
})
</script>

<style scoped>

</style>
