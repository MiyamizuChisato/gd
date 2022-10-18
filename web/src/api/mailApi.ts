const BASE_PATH = '/mail'
import request from '../utils/request'

export const verifyEmail = (email: string) => {
    return request({
        url: `${BASE_PATH}/verify/${email}`,
        method: 'GET'
    })
}
