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
 * 导出
 * @returns {*}
 */
export function leadingOut() {
    return request({
        url: 'admin/userListExcel',
        method: 'get'
    })
}

/**
 * 重置密码
 * @returns {*}
 */
export function resetPwd(data) {
    return request({
        url: 'admin/password',
        method: 'put',
        data
    })
}

//登录日志
export function Loginlog(data) {
    return request({
        url: 'admin/commonUserLog',
        method: 'post',
        data
    })
}
