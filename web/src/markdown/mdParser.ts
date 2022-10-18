import hljs from 'highlight.js'
import 'highlight.js/styles/tokyo-night-dark.css'
import MarkdownIt from 'markdown-it'

const ren = new MarkdownIt({
    highlight: function(str, lang) {
        if (lang && hljs.getLanguage(lang)) {
            try {
                return hljs.highlight(str, {
                    language: lang,
                    ignoreIllegals: true
                }).value
            } catch (__) {
            }
        }
        return ''
    }
})
export default (md: string): string => {
    return ren.render(md)
}
