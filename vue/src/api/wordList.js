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





