<template>
    <div id='index'>
        <div display='flex' text='5' items='center' w='90% md:600px' m='y-8' p='x-4 y-2'
             ring='1.5 primary-light dark:primary-dark' border='rounded-md '>
            <input v-model='keyword' flex='1' placeholder='输入关键字检索' text='normal-light dark:normal-dark'
                   class='search-input'
                   type='text'
                   border='none' outline='none'>
            <v-btn @click='go' size='small' color='transparent' icon flat>
                <i text='6 primary-light dark:primary-dark' cursor='pointer' i-ri-search-2-line />
            </v-btn>
        </div>
        <div text='8 normal-light dark:normal-dark'>立即搜索理想的工作</div>
        <div m='t-20' w='lt-md:95% md:768px lg:992px'>
            <h1 display='flex' text='6 strong-light dark:strong-dark'>
                <div class='h-label' m='x-3'></div>
                <span>热门推荐</span>
            </h1>
            <v-row m='!x-auto !y-5' justify='center'>
                <v-col display='flex' justify='center' cols='12' sm='6' lg='4' v-for='i in index'>
                    <post-card :job='i' />
                </v-col>
            </v-row>
        </div>
    </div>
</template>

<script setup lang='ts'>
import onLoadFinished from '../../hooks/onLoadFinished'
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElNotification } from 'element-plus'
import PostCard from '../../components/PostCard.vue'
import { getIndex } from '../../api/jobApi'

const keyword = ref('')
const router = useRouter()
const go = () => {
    if (keyword.value === '') {
        ElNotification.warning({
            position: 'bottom-right',
            title: '搜索',
            message: '请输入关键字后搜索'
        })
        return
    }
    router.push('/search/' + keyword.value)
}
const index = ref(null)
onLoadFinished(async () => {
    const { code, data } = await getIndex()
    if (code > 0) {
        index.value = data
    }
})
</script>

<style scoped>
#index {
    top: 156px;
    left: 0;
    display: flex;
    position: relative;
    flex-direction: column;
    align-items: center;
    justify-content: center;
}

.search-input {
    width: 90%;
}

.h-label {
    border-left: 4px solid var(--c-primary);
    border-radius: 12px;
}
</style>
