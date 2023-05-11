<template>
  <div class="flex_center_center">
    <div class="wid_1200">
      <el-row :gutter="20">
        <el-col :span="8" :xs="24">
          <el-card class="box-card" shadow="always">
            <div slot="header" class="clearfix">
              <span class="font_22">个人信息</span>
            </div>
            <div>
              <div class="text_center margin_b_10">
                <el-upload
                    class="upload-demo"
                    ref="upload"
                    action="api/user/userImage"
                    :data="UserOfId"
                    :headers="headers"
                    :file-list="fileList"
                >
                  <el-image
                    class="radius_per50"
                    :src="avatarSrc"
                    style="width: 120px; height: 120px"
                    :fit="fit"
                  ></el-image>
                </el-upload>
              </div>
              <ul class="list-group list-group-striped">
                <li
                    v-for="item in cols"
                    :key="item.label"
                    class="list-group-item font_30"
                >
                  <i :class="`el-icon-${item.icon}`"></i>{{ item.label }}
                  <div class="pull-right">
                    {{ user[item.props] || "暂未填写" }}
                  </div>
                </li>
              </ul>
            </div>
          </el-card>
        </el-col>
        <el-col :span="16" :xs="24">
          <el-card>
            <div slot="header" class="clearfix flex_between_center">
              <span class="font_22">基本资料</span>
              <el-button
                :disabled="!isLogin"
                size="medium"
                @click="logOff"
                type="danger"
                >注销账号</el-button
              >
            </div>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="成绩分析" name="analysis">
                <analysis />
              </el-tab-pane>
              <el-tab-pane label="修改资料" name="userinfo">
                <userInfo :user="user" @refresh="getUser" />
              </el-tab-pane>
              <el-tab-pane label="修改密码" name="editPwd">
                <editPwd />
              </el-tab-pane>
              <el-tab-pane label="修改手机号" name="resetTel">
                <resetTel @refresh="getUser" />
              </el-tab-pane>
              <el-tab-pane label="重置密码" name="resetPwd">
                <resetPwd @refresh="getUser" />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
    <Login ref="login"></Login>
  </div>
</template>

<script>
import analysis from "./analysis";
import userInfo from "./userInfo";
import editPwd from "./editPwd";
import resetPwd from "./resetPwd";
import resetTel from "./resetTel";
import {logOff} from '@/api/user.js'
import {avatarUrl} from '@/utils/img.js'
import Login from '../Login.vue'

export default {
  name: "own",
  components: {analysis, userInfo, editPwd, resetPwd, resetTel, Login},
  data() {
    return {
      headers: {
        Authorization: window.sessionStorage.getItem('token')
      },
      isLogin: false,
      fileList: [],
      cols: [{
        label: '账号',
        props: 'account',
        icon: 'user-solid'
      },
        {
          label: '昵称',
          props: 'nickName',
          icon: 'user'
        },
        {
          label: '积分',
          props: 'integration',
          icon: 'trophy'
        },
        {
          label: '电话',
          props: 'tel',
          icon: 'phone-outline'
        },
        {
          label: 'QQ',
          props: 'qq',
          icon: 'chat-round'
        },

        {
          label: '微信号',
          props: 'wechat',
          icon: 'chat-dot-round'
        },
        {
          label: '邮箱',
          props: 'email',
          icon: 'message'
        },
        {
          label: '注册时间',
          props: 'registerTime',
          icon: 'date'
        },
        {
          label: '签名',
          props: 'signature',
          icon: 'position'
        },

      ],
      user: {},
      avatarSrc: '',
      UserOfId: { userId: '' },
      fit: 'cover',
      roleGroup: {},
      postGroup: {},
      activeTab: "analysis"
    };
  },
  created() {
    this.$bus.$off('own').$on('own', () => {
      this.getUser()
      console.log('调用个人中心')
    })
    const token = window.sessionStorage.getItem('token')
    this.token = token
    if (token) this.getUser();
  },
  mounted() {
    if (!this.token) this.$refs.login.showLogin()
  },
  methods: {
    getUser() {
      const userInfo = JSON.parse(window.sessionStorage.getItem('userInfo'))
      if (userInfo) {
        this.avatarSrc = avatarUrl(userInfo.userId)
        this.user = userInfo
        this.UserOfId.userId = userInfo.userId
        this.isLogin = true
      } else this.isLogin = false
    },
    logOff() {
      this.$confirm('下个月初将彻底删除账户，重新登录将撤销注销。', '危险行为！', {
        cancelButtonText: "取消",
        confirmButtonText: "确认",
        showClose: true,
        center: true,
        type: "warning"
      })
        .then(() => {
          console.log('确认')
          logOff()
            .then((res) => {
              console.log('res', res)
            })
        })
    }
  },
};
</script>

<style scoped>
.list-group-striped > .list-group-item {
  border-left: 0;
  border-right: 0;
  border-radius: 0;
  padding-left: 0;
  padding-right: 0;
}

.list-group {
  padding-left: 0px;
  list-style: none;
}

.list-group-item {
  border-bottom: 1px solid #e7eaec;
  border-top: 1px solid #e7eaec;
  margin-bottom: -1px;
  padding: 11px 0px;
  font-size: 16px;
}
</style>
