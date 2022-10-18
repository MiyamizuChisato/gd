const BASE_PATH = '/message'
import request from '../utils/request'

export const getUnreadMessage = (sender: string | string[]) => {
    return request({
        url: `${BASE_PATH}/get/unread/sender/${sender}`,
        method: 'GET'
    })
}
