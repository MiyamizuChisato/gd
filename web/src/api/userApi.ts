const BASE_PATH = '/user'
import request from '../utils/request'

export const login = (user: Object) => {
    return request({
        url: `${BASE_PATH}/login`,
        method: 'POST',
        data: user
    })
}
export const register = (user: Object) => {
    return request({
        url: `${BASE_PATH}/register`,
        method: 'POST',
        data: user
    })
}
export const getUser = () => {
    return request({
        url: `${BASE_PATH}/get/user`,
        method: 'GET'
    })
}
export const watchUser = (uid: string | string[]) => {
    return request({
        url: `${BASE_PATH}/watch/uid/${uid}`,
        method: 'GET'
    })
}
export const getReceivers = () => {
    return request({
        url: `${BASE_PATH}/get/receivers`,
        method: 'GET'
    })
}
export const updateUser = (user: Object) => {
    return request({
        url: `${BASE_PATH}/put/user`,
        data: user,
        method: 'PUT'
    })
}
export const updateUserAvatar = (avatar: FormData) => {
    return request({
        url: `${BASE_PATH}/put/avatar`,
        data: avatar,
        method: 'PUT'
    })
}
export const updateUserResume = (resume: FormData) => {
    return request({
        url: `${BASE_PATH}/put/resume`,
        data: resume,
        method: 'PUT'
    })
}
