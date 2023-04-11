/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/4 15:53
 * @FileName: admin
 * @Description:
 */
import request from "@/utils/request";


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
 * 修改用户信息
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
 * 管理员信息的导出（excel）
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
 * 管理员删除用户
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
 * 重置管理员密码
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
 * 修改管理员描述
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
 * 锁定和解锁管理员
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
 * 添加管理员
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
 * 用户信息的导出（excel）
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
 * 管理员重置密码
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
 * 修改用户信息
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
 * 管理员修改描述
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
 * 锁定和解锁用户
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
 * 管理员删除用户
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
 * 登录日志
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
 * 登录日志导出
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
 * 励志语的一条录入
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
 * @param data
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
 * 更新一条励志语
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
 * 更新一个单词模块
 * @param data
 * @returns {*}
 */
export function updateModule(data) {
    return request({
        url: 'word/wordModule',
        method: 'post',
        data
    })
}

/**
 * 删除一个单词模块
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
 * 锁定/解锁一个单词模块
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
 * 上传单模块
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
 * 上传单模块
 * @returns {*}
 * @param data
 */
export function getArticle(data) {
    return request({
        url: `article/article/${data}`,
        method: 'get',
    })
}
