export default {
    email: [
        v => !!v || '必填',
        v => regexp.email.test(v) || '格式不正确'
    ],
    password: [
        v => !!v || '必填',
        v => regexp.password.test(v) || '格式不正确'
    ],
    nickname: [
        v => !!v || '必填',
        v => regexp.nickname.test(v) || '格式不正确'
    ],
    code: [
        v => !!v || '必填',
        v => regexp.code.test(v) || '格式不正确'
    ]
}
export const regexp = {
    email: /^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,3})+$/,
    password: /^(?![0-9]+$)(?![a-zA-Z]+$)[0-9a-zA-Z]{8,16}$/,
    nickname: /^[\u4e00-\u9fa5a-zA-Z]{2,12}$/,
    code: /^[0-9]{6}$/
}
