/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/4 15:53
 * @FileName: admin
 * @Description:
 */
import request from "@/utils/request";

/**
 * 系统监控信息
 * @returns {*}
 */
export function systemInfo() {
    return request({
        url: 'common/systemInfo',
        method: 'get'
    })
}


/**
 * 条件分页检索事项
 * @returns {*}
 */
export function matterSearch(data) {
    return request({
        url: 'matters/matterSearch',
        method: 'post',
        data
    })
}

/**
 * 新增事项
 * @returns {*}
 */
export function addMatter(data) {
    return request({
        url: 'matters/matter',
        method: 'post',
        data
    })
}

/**
 * 修改事项
 * @returns {*}
 */
export function updateMatter(data) {
    return request({
        url: 'matters/matter',
        method: 'put',
        data
    })
}

/**
 * 删除事项
 * @returns {*}
 */
export function deleteMatter(matterId) {
    return request({
        url: `matters/matter/${matterId}`,
        method: 'delete'
    })
}

/**
 * 修改事项状态
 * @returns {*}
 */
export function changeMatterStatus() {
    return request({
        url: 'matters/matterStatus',
        method: 'put'
    })
}

/**
 * 获取首页的总数
 * @returns {*}
 */
export function getTotalMessage() {
    return request({
        url: 'admin/total',
        method: 'get'
    })
}


/**
 * 获取管理员信息
 * @returns {*}
 */
export function adminInfo() {
    return request({
        url: 'admin/adminInfo',
        method: 'get'
    })
}


/**
 * 获取管理员信息
 * @returns {*}
 */
export function updateAdminInfo(data) {
    return request({
        url: 'admin/adminInfo',
        method: 'put',
        data
    })
}

/**
 * 获取管理员信息
 * @returns {*}
 */
export function updateAdminPwd(data) {
    return request({
        url: 'admin/adminPwd',
        method: 'put',
        data
    })
}

/**
 * 管理员登录
 * @param params
 * @returns {*}
 */
export function adminLogin(params) {
    return request({
        url: 'admin/login',
        method: 'post',
        data: params
    })
}


/**
 * 多条件查询用户列表
 * @param params
 * @returns {*}
 */
export function userSearch(params) {
    return request({
        url: 'admin/userSearch',
        method: 'post',
        data: params
    })
}


/**
 * 多条件查询管理员列表
 * @param params
 * @returns {*}
 */
export function adminSearch(params) {
    return request({
        url: 'admin/adminSearch',
        method: 'post',
        data: params
    })
}


/**
 * 修改管理员信息 log ok
 * @param data
 * @returns {*}
 */
export function updateAdmin(data) {
    return request({
        url: 'admin/admin',
        method: 'put',
        data
    })
}

/**
 * 管理员信息的导出（excel） log ok
 * @returns {*}
 */
export function adminListExcel(data) {
    return request({
        url: 'admin/adminListExcel',
        method: 'post',
        data,
        'responseType': 'blob'
    })
}

/**
 * 删除管理员 log ok
 * @param params
 * @returns {*}
 */
export function deleteAdmin(params) {
    return request({
        url: `admin/admin/${params}`,
        method: 'delete',
    })
}

/**
 * 重置管理员密码 log ok
 * @returns {*}
 */
export function resetAdminPwd(data) {
    return request({
        url: 'admin/adminPassword',
        method: 'put',
        data
    })
}

/**
 * 修改管理员描述 log ok
 * @param data
 * @returns {*}
 */
export function updateAdminRemark(data) {
    return request({
        url: 'admin/adminRemark',
        method: 'put',
        data
    })
}

/**
 * 锁定和解锁管理员 log ok
 * @param data
 * @returns {*}
 */
export function lockOrUnLockAdmin(data) {
    return request({
        url: 'admin/lockOrUnLockAdmin',
        method: 'put',
        data
    })
}


/**
 * 添加管理员 log ok
 * @param data
 * @returns {*}
 */
export function addAdmin(data) {
    return request({
        url: 'admin/commonAdmin',
        method: 'post',
        data
    })
}


/**
 * 用户信息的导出（excel） log ok
 * @returns {*}
 */
export function userListExcel(data) {
    return request({
        url: 'admin/userListExcel',
        method: 'post',
        data,
        'responseType': 'blob'
    })
}


/**
 * 管理员重置密码 log ok
 * @returns {*}
 */
export function resetPwd(data) {
    return request({
        url: 'admin/password',
        method: 'put',
        data
    })
}

/**
 * 修改用户信息 log ok
 * @param data
 * @returns {*}
 */
export function updateUser(data) {
    return request({
        url: 'admin/user',
        method: 'put',
        data
    })
}

/**
 * 管理员修改描述 log ok
 * @param data
 * @returns {*}
 */
export function updateRemark(data) {
    return request({
        url: 'admin/remark',
        method: 'put',
        data
    })
}

/**
 * 锁定和解锁用户 log ok
 * @param data
 * @returns {*}
 */
export function lockOrUnLockUser(data) {
    return request({
        url: 'admin/lockOrUnLockUser',
        method: 'put',
        data
    })
}

/**
 * 管理员删除用户 log ok
 * @param params
 * @returns {*}
 */
export function deleteUser(params) {
    return request({
        url: `admin/user/${params}`,
        method: 'delete',
    })
}

/**
 * 登录日志查询
 * @param data
 * @returns {*}
 */
export function commonUserLog(data) {
    return request({
        url: 'admin/commonUserLog',
        method: 'post',
        data
    })
}

/**
 * 登录日志导出 log ok
 * @param data
 * @returns {*}
 */
export function logExcelImport(data) {
    return request({
        url: 'admin/logExcelImport',
        method: 'post',
        data,
        'responseType': 'blob'
    })
}


/**
 * 励志语的查询
 * @param data
 * @returns {*}
 */
export function emotionWordsSearch(data) {
    return request({
        url: 'emotionWords/emotionWordsSearch',
        method: 'post',
        data
    })
}

/**
 * 励志语的一条录入 log ok
 * @param data
 * @returns {*}
 */
export function insertWord(data) {
    return request({
        url: 'emotionWords/emotionWords',
        method: 'post',
        data
    })
}

/**
 * 批量删除
 * @param data log ok
 * @returns {*}
 */
export function deleteWords(data) {
    return request({
        url: 'emotionWords/emotionWords',
        method: 'delete',
        data
    })
}


/**
 * 更新一条励志语 log ok
 * @param data
 * @returns {*}
 */
export function updateWord(data) {
    return request({
        url: 'emotionWords/emotionWords',
        method: 'put',
        data
    })
}


/**
 * 分页条件搜索单词模块
 * @param data
 * @returns {*}
 */
export function moduleSearch(data) {
    return request({
        url: 'word/wordModuleSearch',
        method: 'post',
        data
    })
}


/**
 * 更新一个单词模块 log ok
 * @param data
 * @returns {*}
 */
export function updateModule(data) {
    return request({
        url: 'word/wordModule',
        method: 'put',
        data
    })
}

/**
 * 删除一个单词模块 log ok
 * @param params
 * @returns {*}
 */
export function deleteModule(params) {
    return request({
        url: `word/wordModule/${params}`,
        method: 'delete',
    })
}


/**
 * 锁定/解锁一个单词模块 log ok
 * @param data
 * @returns {*}
 */
export function lockOrUnLockModule(data) {
    return request({
        url: 'word/lockOrUnLockModule',
        method: 'put',
        data
    })
}


/**
 * 上传单模块 log ok
 * @returns {*}
 * @param data
 */
export function uploadModule(data) {
    return request({
        url: 'word/wordModule',
        method: 'post',
        data,
    })
}


/**
 * 更换词源 log ok
 * @returns {*}
 * @param data
 */
export function changeEsWordModule(data) {
    return request({
        url: 'word/changeEsWordModule',
        method: 'post',
        data,
    })
}


/**
 * 更换图片
 * @returns {*}
 * @param data
 */
export function changePicture(data) {
    return request({
        url: 'word/uploadImage',
        method: 'post',
        data,
    })
}


/**
 * 文章分页搜索
 * @returns {*}
 * @param data
 */
export function articleSearch(data) {
    return request({
        url: 'article/articleSearch',
        method: 'post',
        data,
    })
}


/**
 * 修改文章 log ok
 * @returns {*}
 * @param data
 */
export function updateArticle(data) {
    return request({
        url: 'article/article',
        method: 'put',
        data,
    })
}

/**
 * 删除文章
 * @returns {*} log ok
 * @param params
 */
export function deleteArticle(params) {
    return request({
        url: `article/article/${params}`,
        method: 'delete',
    })
}


/**
 * 获取文章
 * @returns {*}
 * @param params
 */
export function getArticleById(params) {
    return request({
        url: `article/article/${params}`,
        method: 'get',
    })
}

/**
 * 发布文章
 * @returns {*} log ok
 * @param data
 */
export function insertArticle(data) {
    return request({
        url: 'article/article',
        method: 'post',
        data,
    })
}


/**
 * 分页条件查询操作日志
 * @returns {*}
 * @param data
 */
export function actionLogSearch(data) {
    return request({
        url: 'admin/actionLogSearch',
        method: 'post',
        data,
    })
}

/**
 * 操作日志导出 log ok
 * @param data
 * @returns {*}
 */
export function actionLogExcelImport(data) {
    return request({
        url: 'admin/actionLogExcelImport',
        method: 'post',
        data,
        'responseType': 'blob'
    })
}
