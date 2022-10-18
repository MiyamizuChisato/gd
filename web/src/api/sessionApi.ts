const BASE_PATH = '/message'
import request from '../utils/request'

export const validateSession = (sender: string, receiver: string) => {
    return request({
        url: `${BASE_PATH}/validate/sender/${sender}/receiver/${receiver}`,
        method: 'POST'
    })
}
