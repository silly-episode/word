import request from '../utils/request'


export function login(params) {
    return request({
        url: '/user/loginPassword',
        method: 'post',
        data: params
    })
}
