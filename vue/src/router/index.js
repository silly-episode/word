import Vue from "vue";
import VueRouter from "vue-router";

// 前台
import HomeView from "@/views/proscenium/HomeView.vue";
import TranslateView from "@/views/proscenium/TranslateView.vue";
import WordView from "@/views/proscenium/WordView.vue";


// 后台
import Admin from "@/views/backstage/Admin.vue";
import Welcome from '@/views/backstage/Welcome.vue'
import UserManage from '@/views/backstage/user/UserManage.vue'
import AdminManage from '@/views/backstage/user/AdminManage.vue'
import LoginLog from '@/views/backstage/user/LoginLog.vue'

import WordManage from '@/views/backstage/learningresource/WordManage.vue'
import SentenceManage from '@/views/backstage/learningresource/SentenceManage.vue'

import OperationLog from '@/views/backstage/system/OperationLog.vue'
import ErrorLog from '@/views/backstage/system/ErrorLog.vue'
import ServiceInformation from '@/views/backstage/system/ServiceInformation.vue'
import CacheList from '@/views/backstage/system/CacheList.vue'
import DbInformation from '@/views/backstage/system/DbInformation.vue'

Vue.use(VueRouter);

const routes = [
  {
    // 重定向到登录页面
    path: '/',
    redirect: '/home'
  },
  {
    path: "/home",
    name: "home",
    component: HomeView,
    redirect: '/translate',
    children: [{
      path: "/translate",
      name: "translate",
      component: TranslateView,
    }]
  },
  {
    path: "/admin",
    name: "admin",
    component: Admin,
    redirect: '/welcome',
    children: [{
      path: '/welcome',
      component: Welcome
    }, {
      path: '/usermanage',
      component: UserManage
    }, {
      path: '/adminmanage',
      component: AdminManage
    }, {
      path: '/loginlog',
      component: LoginLog
    }, {
      path: '/wordmanage',
      component: WordManage
    }, {
      path: '/sentencemanage',
      component: SentenceManage
    }, {
      path: '/operationlog',
      component: OperationLog
    }, {
      path: '/errorlog',
      component: ErrorLog
    }, {
      path: '/serviceinformation',
      component: ServiceInformation
    }, {
      path: '/cachelist',
      component: CacheList
    }, {
      path: '/dbinformation',
      component: DbInformation
    }]
  },
  {
    path: "/word",
    name: "word",
    component: WordView,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

// 挂载路由导航守卫，控制页面访问权限
// router.beforeEach((to, from, next) => {
//   if (to.path === '/login') return next()
//   // 访问其他页面则要检查是否有token
//   const tokenStr = window.sessionStorage.getItem('token')
//   if (!tokenStr) return next('/login')
//   next()
// })

export default router;
