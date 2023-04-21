import request from '../utils/request'

export function sms(phone) {
    return request({
        url: `/user/sms/${phone}`,
        method: 'get'
    })
}

export function register(data) {
    return request({
        url: `/user/user`,
        method: 'post',
        data
    })
}

export function login(data) {
    return request({
        url: '/user/login',
        method: 'post',
        data
    })
}


export function editUser(data) {
    return request({
        url: `/user/user`,
        method: 'put',
        data
    })
}


