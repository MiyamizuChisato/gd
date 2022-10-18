<template>
    <div class='markdown-viewer' v-html='html'></div>
</template>

<script lang='ts' setup>
import mdParser from '../markdown/mdParser'

const props = defineProps({
    text: {
        default: ''
    }
})
const html = mdParser(props.text)
</script>
