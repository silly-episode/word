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

export function exam(data) {
    return request({
        url: 'examResult/questionBank',
        method: 'post',
        data
    })
}

export function examResult(data) {
    return request({
        url: 'examResult/examResult',
        method: 'post',
        data
    })
}

export function planScore(planId) {
    return request({
        url: `word/dayWord/${planId}`,
        method: 'put'
    })
}

// export function sayHello(data) {
//     return request({
//         url: '/hello/name',
//         method: 'post',
//         data: data
//     })
// }

export function collectWord(data) {
    return request({
        url: '/bookOfWords/word',
        method: 'post',
        data: data
    })
}
