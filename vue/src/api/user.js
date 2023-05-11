import request from '../utils/request'


export function examResult() {
    return request({
        url: `examResult/examResult`,
        method: 'get'
    })
}

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

export function logOff() {
    return request({
        url: `/user/user`,
        method: 'delete',
    })
}

export function editUser(data) {
    return request({
        url: `/user/user`,
        method: 'put',
        data
    })
}

export function smsLogined(phone) {
    return request({
        url: `/user/smsLogined/${phone}`,
        method: 'get'
    })
}

export function editPwd(data) {
    return request({
        url: '/user/password',
        method: 'put',
        data
    })
}

export function editTel(data) {
    return request({
        url: `/user/tel`,
        method: 'post',
        data
    })
}


export function resetPwd(data) {
    return request({
        url: '/user/resetPassword',
        method: 'put',
        data
    })
}
