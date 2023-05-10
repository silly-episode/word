import axios from 'axios'
import {Message, Notification} from 'element-ui'

// axios.defaults.headers['Content-Type'] = 'application/json;charset=utf-8'
// axios.defaults.headers.common['Authorization'] = getJwtAuthorization()


// 创建axios实例
const service = axios.create({
    // axios中请求配置有baseURL选项，表示请求URL公共部分
    baseURL: '/api',
    // 超时
    timeout: 10000
})
// request拦截器
service.interceptors.request.use(config => {
    const token = window.sessionStorage.getItem('token')
    const adminToken = window.sessionStorage.getItem('adminToken')
    const userKind = window.sessionStorage.getItem("userKind");
    if (userKind === "commonUser") {
        token ? config.headers.Authorization = token : null;
    } else if (userKind === "admin") {
        adminToken ? config.headers.Authorization = adminToken : null;
    }
    return config
}, error => {
    console.log(error)
    Promise.reject(error)
})

// 响应拦截器
service.interceptors.response.use(res => {
        console.log(res)
        // 未设置状态码则默认成功状态
        const code = res.data.code || 200;
        // 获取错误信息
        const message = res.data.msg
        if (code === 500) {

            Message({
                message: message,
                type: 'error'
            });
            return Promise.reject(new Error(message));
        } else if (code !== 200) {
            Notification.error({
                title: message
            });
            return Promise.reject('error');
        } else {
            return res.data;
        }
    },
    err => {
        console.log('err' + err)
        const code = err.response.data.code || 500;
        const message = err.response.data.msg
        if (code === 4010) {
            console.log("uncauth")
            // Notification.info({
            //     title: "请登录！",
            //     offset:60
            // });
        }

        return Promise.reject(err)
    }
)

export default service
