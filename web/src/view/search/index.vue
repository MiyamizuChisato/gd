<template>
    <div v-if='!appStore.loading' id='posts' w='lt-md:95% md:768px lg:992px' m='x-auto'>
        <div v-if='page.records.length>0'>
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
        <div display='flex' justify='center' items='center' m='y-6' v-else>
            <el-col :sm='12' :lg='6'>
                <el-result icon='info'>
                    <template #extra>
                        <div text='6 strong-light dark:strong-dark'>
                            暂时还没有此类型的工作
                        </div>
                    </template>
                </el-result>
            </el-col>
        </div>
    </div>
</template>
<script lang='ts' setup>
import { ref, watch } from 'vue'
import { ElCol, ElResult } from 'element-plus'
import { useAppStore } from '../../store/useAppStore'
import onLoadFinished from '../../hooks/onLoadFinished'
import PostCard from '../../components/PostCard.vue'
import { search } from '../../api/jobApi'
import { useRoute } from 'vue-router'

const appStore = useAppStore()
const route = useRoute()
const page = ref({
    current: 1,
    pages: 999
})
onLoadFinished(async () => {
    await fetchPage()
})
const fetchPage = async () => {
    const { data, code } = await search(route.params.keyword, page.value.current)
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
