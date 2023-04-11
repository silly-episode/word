import request from '../utils/request'

export function getWordByUserId(params) {
    return request({
        url: `/word/word/${params.userId}`,
        method: 'get'
    })
}

export function getWordByNum(params) {
    return request({
        url: `/word/word/${params.moduleId}/${params.num}`,
        method: 'get'
    })
}

export function getWordByBookId(params) {
    return request({
        url: `/word/word/bookId/${params.bookId}`,
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
