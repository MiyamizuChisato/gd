import scrollReveal from 'scrollreveal'

export default defineNuxtPlugin((nuxtApp) => {
    return {
        provide: {
            scrollReveal: scrollReveal({
                origin: 'bottom',
                distance: '80px',
                duration: 600,
                opacity: 0.2,
            })
        }
    }
})
