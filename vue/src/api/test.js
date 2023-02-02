import request from '../utils/request'

export function hello() {
    return request({
        url: '/hello',
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
