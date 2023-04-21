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
                  :file-list="fileList"
                  :on-change="handlePreview"
                  :on-success="handleSuccess"
                >
                  <el-avatar :src="avatarSrc" :size="120" :fit="fit">
                  </el-avatar>
                </el-upload>
              </div>
              <ul class="list-group list-group-striped">
                <li
                  v-for="item in cols"
                  :key="item.lable"
                  class="list-group-item font_30"
                >
                  <i :class="`el-icon-${item.icon}`"></i>{{ item.lable }}
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
            <div slot="header" class="clearfix">
              <span class="font_22">基本资料</span>
            </div>
            <el-tabs v-model="activeTab">
              <el-tab-pane label="基本资料" name="userinfo">
                <userInfo :user="user" />
              </el-tab-pane>
              <el-tab-pane label="修改密码" name="resetPwd">
                <resetPwd />
              </el-tab-pane>
            </el-tabs>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import userInfo from "./userInfo";
import resetPwd from "./resetPwd";
import { avatarUrl } from '@/utils/img.js'
export default {
  name: "own",
  components: { userInfo, resetPwd },
  data() {
    return {
      fileList: [],
      cols: [{
        lable: '账号',
        props: 'account',
        icon: 'user-solid'
      },
      {
        lable: '昵称',
        props: 'nickName',
        icon: 'user'
      },
      {
        lable: '电话',
        props: 'tel',
        icon: 'phone-outline'
      },
      {
        lable: 'QQ',
        props: 'qq',
        icon: 'user-magic-stick'
      },

      {
        lable: '微信号',
        props: 'wechat',
        icon: 'mobile'
      },
      {
        lable: '邮箱',
        props: 'email',
        icon: 'tickets'
      },
      {
        lable: '积分',
        props: 'integration',
        icon: 's-goods'
      },
      {
        lable: '注册时间',
        props: 'registerTime',
        icon: 'date'
      },
      {
        lable: '签名',
        props: 'signature',
        icon: 'user-paperclip'
      },

      ],
      user: {},
      avatarSrc: '',
      UserOfId: { userId: '' },
      fit: 'contain',
      roleGroup: {},
      postGroup: {},
      activeTab: "userinfo"
    };
  },
  created() {
    this.getUser();
  },
  methods: {
    getUser() {
      const userInfo = JSON.parse(window.sessionStorage.getItem('userInfo'))
      if (userInfo) {
        this.avatarSrc = avatarUrl(userInfo.userId)
        this.user = userInfo
        this.UserOfId.userId = userInfo.userId
      }
    },

    handlePreview(file) { console.log('file', file.url) },
    handleSuccess(res, file, fileList) {
      console.log('res', res)
      console.log('file', file.url)
      console.log('fileList', fileList)
    }
  }
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
