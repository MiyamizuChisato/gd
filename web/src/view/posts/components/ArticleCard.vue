<template>
    <div class='article-card' @click='to' border='rounded-2'
         w='screen md:800px' bg='container-light dark:container-dark' v-ripple>
        <v-responsive :aspect-ratio='2'>
            <div overflow='hidden' border='md:rounded-l-2' position='relative' h='full' display='flex'
                 flex='col md:row'>
                <v-img :aspect-ratio='1' class='card-image' border='!md:rounded-l-2 !lt-md:rounded-2'
                       :src='"https://picsum.photos/800/800?random/"+src+")"' cover>
                    <template #placeholder>
                        <v-row class='fill-height ma-0' align='center' justify='center'>
                            <i text='3rem primary-light dark:primary-dark' i-line-md-loading-twotone-loop />
                        </v-row>
                    </template>
                    <div border='rounded-2' class='image-mask' h='full'></div>
                </v-img>
                <div class='card-content' border='md:rounded-r-2' bg='md:container-light md:dark:container-dark'
                     w='lt-md:full' flex='1' bottom='0' position='lt-md:absolute relative'>
                    <div p='md:8 4' h='full'>
                        <h1 class='reveal' text='strong-light dark:strong-dark lt-md:strong-dark 1.25rem' p='0' m='0'>
                            原神怎么你了？请停止这场互联网闹剧！
                        </h1>
                        <p class='summary reveal' text='normal-light dark:normal-dark' m='y-6' display='lt-md:none'>
                            你说得对，但是原神是米哈游株式会社自主研发的开放式3D动作冒险游戏。玩家将扮演原神主角，去探寻一个个烧鸡的故事。你说得对，但是原神是米哈游株式会社自主研发的开放式3D动作冒险游戏。玩家将扮演原神主角，去探寻一个个烧鸡的故事。你说得对，但是原神是米哈游株式会社自主研发的开放式3D动作冒险游戏。玩家将扮演原神主角，去探寻一个个烧鸡的故事。
                        </p>
                        <div position='md:absolute' bottom='md:10' class='reveal'
                             text='secondary-light dark:secondary-dark lt-md:secondary-dark 0.875rem'
                             display='flex' justify='between' items='center'>
                            <div>1 小时前</div>
                            <div display='flex' items='center'>
                                <div m='x-2' items='center' display='flex'>
                                    <i m='x-1' text='danger-light dark:danger-dark' i-mdi-fire />
                                    <span m='x-1'>42</span>
                                </div>
                                <div m='x-2' items='center' display='flex'>
                                    <i m='x-1' text='success-light dark:success-dark' i-mdi-chat-processing-outline />
                                    <span m='x-1'>42</span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </v-responsive>
    </div>
</template>

<script lang='ts' setup>

import { useRouter } from 'vue-router'

const router = useRouter()
const props = defineProps({
    src: Number
})
const to = () => {
    router.push('/post/' + props.src)
}
</script>

<style scoped>
.article-card {
    box-shadow: 0 20px 40px -15px rgba(0, 0, 0, 0.05);
}

.card-image {
    transition: transform 240ms ease-in-out;
}

.card-content {
    left: 0;
    transition: left 240ms ease-in-out;
}


@media screen and (min-width: 768px) {
    .summary {
        overflow: hidden;
        text-overflow: ellipsis;
        display: -webkit-box;
        -webkit-line-clamp: 6;
        -webkit-box-orient: vertical;
    }

    .article-card:hover .card-image {
        transform: scale(1.05);
    }

    .article-card:hover .card-content {
        left: -12px;
    }
}

@media screen and (max-width: 767px) {

    .image-mask {
        background: linear-gradient(180deg, transparent, #111111ee);
    }
}
</style>
