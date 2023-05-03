import request from '../utils/request'

export function newWordModule() {
    return request({
        url: '/word/newWordModule',
        method: 'get'
    })
}

export function wordModuleBySuperior(index) {
    return request({
        url: `/word/wordModuleBySuperior/${index}`,
        method: 'get'
    })
}

export function hotWordModule() {
    return request({
        url: '/word/hotWordModule',
        method: 'get'
    })
}

export function wordModuleById(moduleId) {
    return request({
        url: `/word/wordModuleById/${moduleId}`,
        method: 'get'
    })
}

export function addPlan(data) {
    return request({
        url: 'plan/plan',
        method: 'post',
        data
    })
}

export function allBook() {
    return request({
        url: '/wordBooks/allBook',
        method: 'get'
    })
}

export function addBook(data) {
    return request({
        url: '/wordBooks/book/',
        method: 'post',
        data
    })
}

export function allPlan() {
    return request({
        url: '/plan/plan',
        method: 'get'
    })
}


export function deletePlan(planId) {
    return request({
        url: `/plan/plan/${planId}`,
        method: 'delete'
    })
}

export function setMain(data) {
    return request({
        url: `/plan/mainPlan/${data.old}/${data.new}`,
        method: 'put'
    })
}

export function hotIntegration(data) {
    return request({
        url: 'user/hotIntegration',
        method: 'post',
        data
    })
}


export function swearSearch(data) {
    return request({
        url: 'user/swearSearch',
        method: 'post',
        data
    })
}


export function swear() {
    return request({
        url: 'user/swear',
        method: 'post',
    })
}

export function bookInfo(data) {
    return request({
        url: 'bookOfWords/wordSearch',
        method: 'post',
        data
    })
}

export function deleteWord(wordId) {
    return request({
        url: `bookOfWords/word/${wordId}`,
        method: 'delete',
    })
}

export function deleteBook(bookId) {
    return request({
        url: `wordBooks/book/${bookId}`,
        method: 'delete',
    })
}

export function editBook(data) {
    return request({
        url: `wordBooks/book`,
        method: 'put',
        data
    })
}

export function updateBookOfWord(data) {
    return request({
        url: `bookOfWords/word`,
        method: 'put',
        data
    })
}


/**
 * 单词本单词导出
 * @returns {*}
 */
export function wordListPdf(bookId) {
    return request({
        url: `bookOfWords/book/${bookId}`,
        method: 'get',
        'responseType': 'blob'
    })
}







