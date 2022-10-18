<template>
    <v-list p='!0' bg='!container-light !dark:container-dark'>
        <v-list-item p='!x-0'>
            <template v-if='list'>
                <publish-item @deleted='deletedHandler' v-for='i in list' :job='i' border='b-1 light-900 dark:dark-50'
                              :key='i.jid' class='publish-item' />
            </template>
            <template v-else>
                <div text='center danger-light dark:danger-dark'>这位用户还没有发布过招聘</div>
            </template>
        </v-list-item>
    </v-list>
</template>

<script lang='ts' setup>
import PublishItem from './PublishItem.vue'
import { onMounted, ref } from 'vue'
import { getListByUid } from '../../../api/jobApi'

const props = defineProps({
    uid: String
})
const list = ref(null)
const fetchList = async () => {
    const { code, data } = await getListByUid(props.uid)
    if (code > 0 && data.length > 0) {
        list.value = data
    } else {
        list.value = null
    }
}
onMounted(async () => {
    await fetchList()
})
const deletedHandler = async () => {
    console.log(1)
    await fetchList()
}
</script>

<style scoped>
.publish-item:last-child {
    border-bottom: none;
}
</style>
