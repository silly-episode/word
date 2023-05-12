import Vue from "vue";
import VueRouter from "vue-router";
import {monitorZoom} from "@/utils/monitorZoom.js";
// 前台
import HomeView from "@/views/proscenium/HomeView.vue";
import WordListView from "@/views/proscenium/WordListView.vue";
import MyWordView from "@/views/proscenium/MyWordView.vue";
import ArticleList from "@/views/proscenium/ArticleList.vue";
import TranslateView from "@/views/proscenium/TranslateView.vue";
import KeyboardView from "@/views/proscenium/KeyboardView.vue";
import WordModule from "@/views/proscenium/WordModule.vue";

import Assist from "@/views/proscenium/Assist.vue";
import Own from "@/views/proscenium/own/index.vue";

import WordView from "@/views/proscenium/WordView.vue";
import ExamView from "@/views/proscenium/ExamView.vue";
import ArticleView from "@/views/proscenium/ArticleView.vue";
import BookInfo from "@/views/proscenium/BookInfo.vue";

// 后台
import Admin from "@/views/backstage/Admin.vue";
import Welcome from '@/views/backstage/Welcome.vue'
import UserManage from '@/views/backstage/user/UserManage.vue'
import AdminManage from '@/views/backstage/user/AdminManage.vue'
import LoginLog from '@/views/backstage/system/LoginLog.vue'

import WordManage from '@/views/backstage/learningresource/WordManage.vue'
import ArticleManage from '@/views/backstage/learningresource/ArticleManage.vue'
import SentenceManage from '@/views/backstage/learningresource/SentenceManage.vue'

import OperationLog from '@/views/backstage/system/OperationLog.vue'
import ServiceInformation from '@/views/backstage/system/ServiceInformation.vue'

import AdminLogin from "@/views/backstage/AdminLogin";

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
    redirect: '/wordlist',
    children: [{
      path: "/wordlist",
      component: WordListView,
    }, {
      path: "/myword",
      component: MyWordView,
    }, {
      path: "/articlelist",
      component: ArticleList,
    }, {
      path: "/translate",
      component: TranslateView,
    }, {
      path: "/keyboard",
      component: KeyboardView,
    }, {
      path: "/own",
      name: 'own',
      component: Own,
    }, {
      path: "/assist",
      name: 'assist',
      component: Assist,
    }, {
      path: "/module",
      name: 'module',
      component: WordModule,
    }, {
      path: "/bookInfo",
      name: 'bookInfo',
      component: BookInfo,
    },]
  },
  {
    path: "/admin",
    name: "admin",
    component: Admin,
    // redirect: '/adminLogin',
    children: [{
      path: '/admin/welcome',
      component: Welcome
    }, {
      path: '/admin/usermanage',
      component: UserManage
    }, {
      path: '/admin/adminmanage',
      component: AdminManage
    }, {
      path: '/admin/loginlog',
      component: LoginLog
    }, {
      path: '/admin/wordmanage',
      component: WordManage
    }, {
      path: '/admin/articlemanage',
      component: ArticleManage
    }, {
      path: '/admin/sentencemanage',
      component: SentenceManage
    }, {
      path: '/admin/operationlog',
      component: OperationLog
    }, {
      path: '/admin/serviceinformation',
      component: ServiceInformation
    },

      //   {
      //   path: '/admin/cachelist',
      //   component: CacheList
      // }, {
      //   path: '/admin/dbinformation',
      //   component: DbInformation
      // }

    ]
  },
  {
    path: "/word",
    name: 'word',
    component: WordView,
  },
  {
    path: "/exam",
    name: 'exam',
    component: ExamView,
  },
  {
    path: "/article/:articleId",
    name: 'article',
    component: ArticleView,
  }, {
    path: "/adminLogin",
    component: AdminLogin,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

const m = monitorZoom();
// 挂载路由导航守卫，控制页面访问权限
router.beforeEach((to, from, next) => {
  window.sessionStorage.setItem("ratio", (m / 100.0).toString());
  if (to.path.substring(0, 6) === "/admin") {
    document.body.style.zoom = 90 / Number(m);
    if (to.path === '/adminLogin') {
      return next()
    }
    // 访问其他页面则要检查是否有token
    const tokenStr = window.sessionStorage.getItem('adminToken')
    if (!tokenStr) {
      return next('/adminLogin')
    }
    next()
  } else {
    document.body.style.zoom = 100 / m;
    /*放行*/
    next()
  }

})

export default router;
