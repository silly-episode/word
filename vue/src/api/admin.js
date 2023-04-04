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
