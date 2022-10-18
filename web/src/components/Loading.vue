<template>
    <transition>
        <div v-show='show' class='loading'>
            <div class='large'>
                <div class='medium'>
                    <div class='small'>
                        <div class='dot'>

                        </div>
                    </div>
                </div>
            </div>
        </div>
    </transition>
</template>
<script lang='ts' setup>
const props = defineProps({
    show: {
        required: true,
        type: Boolean,
        default: true
    }
})
</script>

<style scoped>
.loading {
    opacity: 1;
    position: fixed;
    z-index: 10;
}

.v-enter-active,
.v-leave-active {
    transition: opacity 0.5s ease;
}

.v-enter-from {
    opacity: 1;
}

.v-leave-to {
    opacity: 0;
}
</style>
