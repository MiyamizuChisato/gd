const BASE_PATH = '/job'
import request from '../utils/request'

export const publish = (job: Object) => {
    return request({
        url: `${BASE_PATH}/publish`,
        method: 'POST',
        data: job
    })
}
export const getById = (id: string | string []) => {
    return request({
        url: `${BASE_PATH}/get/id/${id}`,
        method: 'GET'
    })
}
export const getListByUid = (uid: string | string [] | undefined) => {
    if (uid === undefined) {
        return { data: null, code: -1 }
    }
    return request({
        url: `${BASE_PATH}/get/list/uid/${uid}`,
        method: 'GET'
    })
}
export const check = (id: string | string[]) => {
    return request({
        url: `${BASE_PATH}/check/id/${id}`,
        method: 'GET'
    })
}
export const getPage = (current: number | string[]) => {
    return request({
        url: `${BASE_PATH}/get/page/${current}`,
        method: 'GET'
    })
}
export const getIndex = () => {
    return request({
        url: `${BASE_PATH}/get/index`,
        method: 'GET'
    })
}
export const deleteById = (id: string | string[]) => {
    return request({
        url: `${BASE_PATH}/delete/id/${id}`,
        method: 'DELETE'
    })
}
export const search = (keyword: any, current: number) => {
    return request({
        url: `${BASE_PATH}/search/${keyword}/${current}`,
        method: 'GET'
    })
}
