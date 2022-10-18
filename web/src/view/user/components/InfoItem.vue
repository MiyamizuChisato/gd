<template>
    <v-list-item-action p='!x-6 !y-2' display='flex' justify='between'>
        <div display='flex' items='center' m='r-4'>
            <i text='1.25rem red' m='t-1 r-4' :class='icon' />
            <p text='normal-light dark:normal-dark'>{{ label }}</p>
        </div>
        <v-form ref='formRef' v-model='valid' flex='1' m='lt-sm:t-1' display='flex' justify='end' v-if='editing'
                text='normal-light dark:normal-dark'>
            <v-text-field :rules='rules' v-model='text' :type='password?"password":""'>
                <template #append-inner>
                    <div p='l-2' display='flex' flex='col' justify='around'>
                        <v-btn @click='cancelModifyText' color='transparent' width='24px' height='24px' icon flat>
                            <i text='red' i-mdi-close-circle />
                        </v-btn>
                        <v-btn @click='saveModifyText' color='transparent' width='24px' height='24px' icon flat>
                            <i text='blue' i-mdi-content-save />
                        </v-btn>
                    </div>
                </template>
            </v-text-field>
        </v-form>
        <div flex='1' display='flex' justify='end' items='center' v-else>
            <div class='break-all' text='normal-light dark:normal-dark'>
                <p>{{ showText }}</p>
            </div>
            <div v-if='modify'>
                <v-btn :disabled='userStore.editing' @click='textModifyHandler' v-if='edit' m='x-1'
                       bg='!transparent'
                       size='mini' flat icon>
                    <i m='1' :text='editClass' i-ri-edit-2-fill />
                </v-btn>
            </div>
        </div>
    </v-list-item-action>
</template>

<script lang='ts' setup>
import { computed, ref, watch } from 'vue'
import { useUserStore } from '../../../store/useUserStore'

const userStore = useUserStore()
const props = defineProps({
    label: String,
    icon: String,
    text: String,
    index: {
        required: true
    },
    modify: {
        default: true
    },
    edit: {
        default: false
    },
    editing: {
        default: false
    },
    password: {
        default: false
    },
    rules: Object
})
const textModifyHandler = () => {
    userStore.editing = true
    editing.value = true
}
const cancelModifyText = () => {
    editing.value = false
    userStore.editing = false
    text.value = props.text
}
const saveModifyText = () => {
    formRef.value.validate()
    if (valid.value) {
        editing.value = false
        userStore.editing = false
        //@ts-ignore
        userStore.currentUser[props.index] = text.value
        userStore.updateUser()
        if (props.password) {
            text.value = props.text
        }
    }
}
const showText = computed(() => {
    if (props.password) {
        return '*********'
    } else if (props.text === '') {
        return '无描述'
    } else {
        return props.text
    }
})
const text = ref(props.text)
const editing = ref(false)
const formRef = ref()
const valid = ref(false)
const editClass = computed(() => {
    if (userStore.editing) {
        return 'strong-light dark:strong-dark'
    }
    return 'primary-light dark:primary-dark'
})
watch(() => props.edit, (value) => {
    if (!value) {
        editing.value = false
        userStore.editing = false
    }
})
</script>

<style scoped>
.v-btn--disabled.v-btn--variant-elevated {
    color: transparent;
}
</style>
