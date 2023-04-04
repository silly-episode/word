import request from '../utils/request'

export function hello() {
    return request({
        url: '/hello',
        method: 'get'
    })
}

export function translate(data) {
    return request({
        url: '/common/translate',
        method: 'post',
        data
    })
}
