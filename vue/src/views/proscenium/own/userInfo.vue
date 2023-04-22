<template>
  <el-form
    class="font_18"
    ref="form"
    :model="userInfo"
    :rules="rules"
    label-width="80px"
  >
    <el-form-item
      v-for="item in cols"
      :key="item.props"
      :label="item.lable"
      :prop="item.props"
    >
      <el-input v-model="userInfo[item.props]" maxlength="30" />
    </el-form-item>

    <el-form-item label="签名" prop="signature">
      <el-input
        type="textarea"
        :rows="5"
        placeholder="请输入签名"
        v-model="userInfo.signature"
      >
      </el-input>
    </el-form-item>
    <el-form-item class="flex_center_center">
      <el-button type="primary" size="mini" @click="submit">保存</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import { editUser } from '@/api/user.js'
export default {
  props: {
    user: {
      type: Object
    }
  },
  data() {
    return {
      userInfo: JSON.parse(JSON.stringify(this.user)),
      cols: [
        {
          lable: '昵称',
          props: 'nickName',
        },
        {
          lable: 'QQ',
          props: 'qq',
        },
        {
          lable: '微信号',
          props: 'wechat',
        },
        {
          lable: '邮箱',
          props: 'email',
          icon: 'tickets'
        }],
      // 表单校验
      rules: {
        nickName: [
          { required: true, message: "用户昵称不能为空", trigger: "blur" }
        ],
        email: [
          { required: true, message: "邮箱地址不能为空", trigger: "blur" },
          {
            type: "email",
            message: "请输入正确的邮箱地址",
            trigger: ["blur", "change"]
          }
        ],

      }
    };
  },
  methods: {
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          editUser(this.userInfo).then(res => {
            //  console.log('res', res)
            if (res.code == 200) {
              window.sessionStorage.setItem('userInfo', JSON.stringify(this.userInfo))
              this.$emit('refresh')
              this.$message.success("修改成功")
            }

          });
        }
      });
    },
  }
};
</script>
