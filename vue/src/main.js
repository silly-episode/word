import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import axios from 'axios';
import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';
import * as echarts from 'echarts';

import dayjs from "dayjs"

// 导入字体图标样式
import "../src/assets/iconfont/iconfont.css"
// 导入全局样式表
import './assets/css/global.css'

//使用echarts
Vue.prototype.$echarts = echarts
//使用el-ui
Vue.use(ElementUI);
//全局挂载axios
Vue.prototype.$axios = axios
Vue.prototype.$dayjs = dayjs;


//关闭提示
Vue.config.productionTip = false

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
