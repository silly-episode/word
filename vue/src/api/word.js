import request from '../utils/request'

export function getWord(params) {
    return request({
        url: `/word/word/${params.userId}`,
        method: 'get'
    })
}

// export function sayHello(data) {
//     return request({
//         url: '/hello/name',
//         method: 'post',
//         data: data
//     })
// }
