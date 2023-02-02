import request from '../utils/request'

export function importWord() {
    return request({
        url: '/importWord',
        method: 'get'
    })
}

export function sayHello(data) {
    return request({
        url: '/hello/name',
        method: 'post',
        data: data
    })
}
