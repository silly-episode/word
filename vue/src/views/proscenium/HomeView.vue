<template>
  <div>
    <el-header class="bg_f4 flex_between_center font_30" style="height: 70px">
      <img src="@/assets/wordLog.png" alt="" class="wid_300 margin_l_10" />
      <el-menu
        background-color="#f4f4f4"
        text-color="#000"
        active-text-color="#818cf8"
        :router="true"
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
      >
        <el-menu-item
          :index="'/' + item.path"
          v-for="item in menulist"
          :key="item.path"
          @click="saveNavState('/' + item.path)"
        >
          <template slot="title">
            <span>{{ item.authName }}</span>
          </template>
        </el-menu-item>
      </el-menu>

      <div class="margin_r_20 flex_center">
        <span @click="reciteWord" class="margin_r_20 font_16 pointer"
          >开始背单词</span
        >
        <el-dropdown trigger="click" @command="handleJump">
          <span class="el-dropdown-link">
            <el-avatar
              :size="60"
              src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
            >
            </el-avatar>
          </span>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item command="own"> 个人中心 </el-dropdown-item>
            <el-dropdown-item command="login">{{
              isLogin ? "退出登录" : "登录"
            }}</el-dropdown-item>
            <el-dropdown-item command="assist"> 用户辅助 </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
      </div>
    </el-header>
    <el-main>
      <router-view></router-view>
    </el-main>
    <Login ref="login"></Login>
  </div>
</template>

<script>
import Login from './Login.vue'
export default {
  name: "HomeView",
  components: { Login },
  data() {
    return {
      isLogin: false,
      userInfo: {},
      activeIndex: '/wordlist',
      menulist: [{
        path: 'wordlist',
        authName: '单词列表'
      }, {
        path: 'myword',
        authName: '我的单词'
      }, {
        path: 'articlelist',
        authName: '文章练习'
      }, {
        path: 'translate',
        authName: '翻译工具'
      }, {
        path: 'keyboard',
        authName: '键盘练习'
      }]
    }
  },
  methods: {
    handleJump(name) {
      if (name == 'login') {
        if (this.isLogin) {
          this.$confirm('确认退出登录吗?', {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }).then(() => {
            window.sessionStorage.clear();
            window.location.reload()
          }).catch(() => {
            console.log('取消操作');
          });
          this.isLogin = false
        } else this.$refs.login.showLogin()
      }
      else this.$router.push({ name })

    },

    saveNavState(activePath) {
      // console.log(activePath)
      window.sessionStorage.setItem("activePath", activePath);
      this.activePath = activePath;
    },

    reciteWord() {
      if (this.isLogin) this.$router.push({
        name: 'word',
        params: { userId: this.userInfo.userId }
      })
      else this.$refs.login.showLogin()
    },

    beLogin() {
      this.isLogin = true
    }
  },
  created() {
    const token = window.sessionStorage.getItem("token");
    this.isLogin = token ? true : false
    this.userInfo = JSON.parse(window.sessionStorage.getItem('userInfo'))
    // console.log('userInfo',this.userInfo)
    console.log('this.isLogin', this.isLogin)
  },
  mounted() {
    this.$bus.$on('beLogin', this.beLogin)
  },
  beforeDestroy() {
    this.$bus.$off('beLogin')
  },
};
</script>

<style>
.el-menu-item:hover {
  outline: 0 !important;
  color: #818cf8 !important;
  background: transparent !important;
}
.el-menu:hover {
  outline: 0 !important;
  color: #818cf8 !important;
  background: transparent !important;
}
.el-submenu:hover {
  background: transparent !important;
}
</style>
