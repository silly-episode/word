import request from '../utils/request'

export function getArticle(articleId) {
    return request({
        url: `article/article/${articleId}`,
        method: 'get'
    })
}

export function articleSearch(data) {
    return request({
        url: 'article/articleSearch',
        method: 'post',
        data,
    })
}
