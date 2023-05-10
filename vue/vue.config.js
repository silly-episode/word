const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
    transpileDependencies: true,
    // 关闭代码检查
    lintOnSave: false,
    //解决跨域问题
    devServer: {
        //配置前端的host和端口
        host: 'localhost',
        port: 8081,
        //自动打开浏览器
        open: true,
        // 代理配置表，在这里可以配置特定的请求代理到对应的API接口
        proxy: {

            // 匹配所有以 '/admin/api'开头的请求路径
            '/admin/api': {
                // 代理目标的基础路径
                target: 'http://localhost:9400',
                // target: 'http://ekmjfu.natappfree.cc',
                // 允许跨域
                changeOrigin: true,
                // 重写路径: 去掉路径中开头的'/api'
                pathRewrite: {
                    '^/admin/api': ''
                }
            },


            // 匹配所有以 '/api'开头的请求路径
            '/api': {
                // 代理目标的基础路径
                target: 'http://localhost:9400',
                // target: 'http://ekmjfu.natappfree.cc',
                // 允许跨域
                changeOrigin: true,
                // 重写路径: 去掉路径中开头的'/api'
                pathRewrite: {
                    '^/api': ''
                }
            }
        }
    }

});
