<template>
    <div class='markdown-editor markdown-viewer' overflow='hidden'>
        <md-editor bg='container-light dark:container-dark' v-model='text' :toolbars='toolbars' :footers='footers'
                   placeholder='# 请在这里编辑详情内容，支持markdown语法' :historyLength='0'>
            <template #defToolbars>
                <normal-toolbar :title='previewDisplay.iconTitle' @on-click='previewToggle'>
                    <template #trigger>
                        <i text='1.25rem' display='flex' h='full' justify='center' items='center'
                           :class='previewDisplay.icon' />
                    </template>
                </normal-toolbar>
            </template>
            <template #defFooters>
                <span m='x-3' display='flex' h='full' justify='center' items='center'>
                    {{ previewDisplay.mode }}模式
                </span>
            </template>
        </md-editor>
    </div>
</template>

<script lang='ts' setup>
import MdEditor from 'md-editor-v3'
import 'md-editor-v3/lib/style.css'
import { computed, ref, watch } from 'vue'

const props = defineProps({
    content: String
})
const emits = defineEmits(['change'])
const NormalToolbar = MdEditor.NormalToolbar
const preview = ref(false)
const previewToggle = () => {
    preview.value = !preview.value
}
const text = ref(props.content)
const toolbars = ['sub', 'sup', 'quote', 'unorderedList', 'orderedList', '-', 'title', 'bold', 'underline', 'italic', 'strikeThrough', '=', 0]
const footers = ['markdownTotal', '=', 0]
const previewDisplay = computed(() => {
    if (preview.value) {
        return {
            input: 'none',
            preview: 'block',
            icon: 'i-mdi-file-document-edit',
            mode: '预览',
            iconTitle: '编辑'
        }
    }
    return {
        input: 'block',
        preview: 'none',
        icon: 'i-mdi-eye',
        mode: '编辑',
        iconTitle: '预览'
    }
})
watch(() => text.value, (value) => {
    emits('change', value)
})
</script>

<style>
.markdown-editor .md-toolbar {
    min-width: 0 !important;
}

.markdown-editor .md-preview-wrapper {
    display: v-bind(previewDisplay.preview) !important;
}

.markdown-editor .md-input-wrapper {
    display: v-bind(previewDisplay.input) !important;
}

.markdown-editor #md-editor-v3-textarea {
    overflow-y: auto;
}

.markdown-editor .md {
    color: var(--c-normal);
    border-radius: 6px;
}

.markdown-editor .md-footer {
    color: var(--c-secondary);
}
</style>
