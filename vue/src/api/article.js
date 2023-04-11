import request from '../utils/request'

export function getArticle(articleId) {
    return request({
        url: `article/article/${articleId}`,
        method: 'get'
    })
}
