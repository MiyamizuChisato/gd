<template>
    <div v-if='!appStore.loading' id='posts' w='lt-md:95% md:768px lg:992px' m='x-auto'>
        <v-row m='!x-auto !y-5' justify='center'>
            <v-col display='flex' justify='center' cols='12' sm='6' lg='4' v-for='i in page.records'>
                <post-card :job='i' />
            </v-col>
        </v-row>
        <v-row justify='center'>
            <div text='center normal-light dark:normal-dark' bg='container-light dark:container-dark'
                 border='rounded-md'>
                <v-pagination
                    v-model='page.current'
                    :total-visible='3'
                    :length='page.pages'></v-pagination>
            </div>
        </v-row>
    </div>
</template>
<script lang='ts' setup>
import { ref, watch } from 'vue'
import { useAppStore } from '../../store/useAppStore'
import onLoadFinished from '../../hooks/onLoadFinished'
import PostCard from '../../components/PostCard.vue'
import { getPage } from '../../api/jobApi'

const appStore = useAppStore()
const page = ref({
    current: 1,
    pages: 999
})
onLoadFinished(async () => {
    await fetchPage()
})
const fetchPage = async () => {
    const { data, code } = await getPage(page.value.current)
    console.log(data)
    if (code > 0) {
        page.value = data
    }
}
watch(() => page.value.current, async () => {
    await fetchPage()
    window.scrollTo({
        behavior: 'smooth',
        top: 0
    })
})
</script>

<style scoped>

</style>
