import { ElNotification } from 'element-plus'
import axios, { AxiosRequestConfig } from 'axios'

export interface MyResponseType<T = any> {
    code: number;
    message: string;
    data: T;
}

const http = axios.create({
    baseURL: 'http://localhost:8080/api',
    timeout: 100000
})

http.interceptors.request.use(config => {
    const token = localStorage.getItem('token')
    if (token !== null && config !== null) {
        // @ts-ignore
        config.headers.Authorization = token
    }
    return config
}, error => {
    return Promise.reject(error)
})

http.interceptors.response.use((res) => {
    return Promise.resolve(res)
}, error => {
    return Promise.reject(error)
})
const request = async <T = any>(config: AxiosRequestConfig): Promise<MyResponseType<T>> => {
    const { data } = await http.request<MyResponseType<T>>(config)
    if (data.code < 0) {
        ElNotification.error({
            position: 'bottom-right',
            title: '错误',
            message: data.message
        })
    }
    return data
}
export default request
