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
 * 用户信息的导出（excel）
 * @returns {*}
 */
export function userListExcel() {
    return request({
        url: 'admin/userListExcel',
        method: 'get',
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

//登录日志
export function Loginlog(data) {
    return request({
        url: 'admin/commonUserLog',
        method: 'post',
        data
    })
}
