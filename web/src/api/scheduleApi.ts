const BASE_PATH = '/schedule'
import request from '../utils/request'

export const sendResume = (schedule: Object) => {
    return request({
        url: `${BASE_PATH}/send/resume`,
        method: 'POST',
        data: schedule
    })
}
export const listSendByUid = (uid: string | string[]) => {
    return request({
        url: `${BASE_PATH}/get/list/uid/${uid}`,
        method: 'GET'
    })
}
export const listSendByJid = (jid: string | string[]) => {
    return request({
        url: `${BASE_PATH}/get/list/jid/${jid}`,
        method: 'GET'
    })
}
export const progress = (jid: string | string[], bid: string, progress: '2' | '3') => {
    return request({
        url: `${BASE_PATH}/put/jid/${jid}/bid/${bid}/progress/${progress}`,
        method: 'PUT'
    })
}
