<template>
  <el-container style="height: 100%">
    <el-header>
      <div style="display: flex; align-items: center">
        <!-- <img
          class="headerImg"
          mode="aspectFill"
          src="@/assets/学习.jpg"
          alt=""
        /> -->
        <span style="margin-left: 15px">基于Web的单词记忆系统</span>
      </div>
      <el-button type="info" @click="logout">退出</el-button>
    </el-header>

    <el-container>
      <el-aside :width="isCollapse ? '64px' : '200px'">
        <!-- 控制侧边栏折叠的按钮 -->
        <div class="toggle-button" @click="toggleCollapse">|||</div>
        <!-- 侧边栏菜单区 -->
        <el-menu
            :collapse="isCollapse"
            :default-active="activePath"
            active-text-color="#409EFF"
            background-color="#2e645e"
            text-color="#fff"
            router
        >
          <el-menu-item
              index="/admin/welcome"
              @click="saveNavState('/' + subItem.path)"
          >
            <template slot="title">
              <i :class="`iconfont icon-shouye font_24`"></i>
              <span>首页</span>
            </template>
          </el-menu-item>
          <!-- 一级菜单 -->
          <el-submenu
              v-for="item in menulist"
              :key="item.id"
              :index="item.id + ''"
          >
            <template slot="title">
              <i :class="`iconfont font_24 ${item.icon}`"></i>
              <span>{{ item.authName }}</span>
            </template>
            <!-- 二级菜单 -->
            <el-menu-item
                v-for="subItem in item.children"
                :key="subItem.id"
                :index="'/' + subItem.path"
                @click="saveNavState('/' + subItem.path)"
            >
              <template slot="title">
                <i class="el-icon-menu"></i>
                <span>{{ subItem.authName }}</span>
              </template>
            </el-menu-item>
          </el-submenu>
        </el-menu>
      </el-aside>
      <el-main :style="`height:calc(10/9.0*${radio}*100vh - 62px)`">
        <router-view></router-view>
      </el-main>
      <!--      /*:height="`calc(10/9.0*${radio}*100vh - 301px)`"*/-->
    </el-container>
  </el-container>
</template>

<script>
export default {
  data() {
    return {
      tableHeight: 0,
      radio: window.sessionStorage.getItem("ratio"),
      clientWidth: document.body.clientWidth, // 文档宽度
      menulist: [
        {
          id: '2',
          authName: '用户',
          icon: 'icon-yonghu',
          children: [{
            id: '21',
            authName: '用户管理',
            path: 'admin/usermanage'
          }, {
            id: '22',
            authName: '管理员管理',
            path: 'admin/adminmanage'
          }]
        }, {
          id: 3,
          authName: '学习资源',
          icon: 'icon-ziyuan',
          children: [{
            id: '31',
            authName: '单词管理',
            path: 'admin/wordmanage'
          },
            {
              id: '32',
              authName: '文章管理',
              path: 'admin/articlemanage'
            },
            {
              id: '33',
              authName: '励志语管理',
              path: 'admin/sentencemanage'
            }]
        }, {
          id: 4,
          authName: '系统监控',
          icon: 'icon-xitong',
          children: [{
            id: '41',
            authName: '管理员操作日志',
            path: 'admin/operationlog'
          }, {
            id: '42',
            authName: '用户登录日志',
            path: 'admin/loginlog'
          }, {
            id: '43',
            authName: '服务信息',
            path: 'admin/serviceinformation'
          },


            //   {
            //   id: '44',
            //   authName: '缓存列表',
            //   path: 'admin/cachelist'
            // }, {
            //   id: '45',
            //   authName: '数据库表信息',
            //   path: 'admin/dbinformation'
            // }

          ]
        }],
      //   侧边栏是否折叠
      isCollapse: false,
      //   记录处于激活状态的链接地址
      activePath: "",

      height: "500px"
    };
  },


  // computed:{
  //   mainHeight(){
  //     return {
  //       "--height":this.height
  //     }
  //   }
  //
  // },

  created() {
    // 将记录下来的激活状态的链接赋予menu
    this.activePath = window.sessionStorage.getItem("activePath");
  },
  methods: {
    logout() {
      window.sessionStorage.clear();
      this.$router.push("/admin/adminLogin");//   重定向到登录页
    },

    toggleCollapse() { // 点击折叠、展开侧边栏
      this.isCollapse = !this.isCollapse;
    },

    saveNavState(activePath) { // 保存处于激活状态的index，便于持续的高亮显示
      window.sessionStorage.setItem("activePath", activePath);
      this.activePath = activePath;
    },
  },


};
</script>

<style scoped>


.el-main {
  background-color: #eaedf1;
}

.font_24 {
  font-size: 24px;
}

.headerImg {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

.el-header {
  background-color: #2e645e;
  display: flex;
  justify-content: space-between;
  padding-left: 10px;
  align-items: center;
  color: #fff;
  font-size: 20px;
}

.el-aside {
  background-color: #2e645e;
  /*   使侧边栏丝滑折叠 */
  transition: width 0.2s;
}
.el-menu {
  border-right: none;
}


.iconfont {
  /* 让侧边菜单栏的图标和文字有间距 */
  margin-right: 10px;
}

.toggle-button {
  background-color: #2e645e;
  font-size: 10px;
  line-height: 24px;
  color: #fff;
  text-align: center;
  letter-spacing: 0.2em;
  cursor: pointer;
}
</style>
