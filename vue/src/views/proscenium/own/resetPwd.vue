<template>
  <el-form ref="form" :model="user" :rules="rules" label-width="80px">
    <el-form-item label="手机号" prop="tel">
      <el-input
        v-model="user.tel"
        placeholder="请输入电话号码"
        prefix-icon="el-icon-mobile"
        @blur="checkTel"
      >
        <template slot="append">
          <el-button
            @click.native.prevent="sendCode"
            class="wid_90"
            :disabled="codeDisabled"
            >{{ codeText }}</el-button
          >
        </template>
      </el-input>
    </el-form-item>
    <el-form-item label="验证码" prop="code">
      <el-input
        type="number"
        v-model="user.code"
        placeholder="请输入验证码"
        prefix-icon="el-icon-lock"
      >
      </el-input>
    </el-form-item>
    <el-form-item label="新密码" prop="password">
      <el-input
        v-model="user.password"
        placeholder="请输入新密码"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item label="确认密码" prop="confirmPassword">
      <el-input
        v-model="user.confirmPassword"
        placeholder="请确认新密码"
        type="password"
        show-password
      />
    </el-form-item>
    <el-form-item class="flex_center_center">
      <el-button size="medium" type="primary" @click="submit">保存</el-button>
    </el-form-item>
  </el-form>
</template>

<script>
import {resetPwd, smsLogined} from "@/api/user";

export default {
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.user.password !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
      codeDisabled: true,
      codeText: '发送',
      user: {
        tel: '',
        code: '',
        password: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        code: [
          { required: true, message: "验证码不能为空", trigger: "blur" },
        ],
        password: [
          { required: true, message: "新密码不能为空", trigger: "blur" },
        ],
        confirmPassword: [
          { required: true, message: "确认密码不能为空", trigger: "blur" },
          { required: true, validator: equalToPassword, trigger: "blur" }
        ]
      }
    };
  },
  methods: {
    // 手机号正则
    checkTel() {
      console.log(this.user.tel)
      const TEL_REGEXP = /^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
      let telFlag = TEL_REGEXP.test(this.user.tel)
      if (telFlag) this.codeDisabled = false
      else this.codeDisabled = true
    },
    sendCode() {
      smsLogined(this.user.tel)
        .then((res) => {
          console.log(res)
          if (res.code == 200) {
            this.$message("已发送验证码，请查收");
            this.codeDisabled = true;
            this.codeText = "请稍候...";
            setTimeout(() => {
              this.doLoop(60);
            }, 500);
          }
        })


    },
    // 手机验证码的倒计时
    doLoop(seconds) {
      seconds = seconds ? seconds : 60;
      this.codeText = seconds + "s";
      // this.code = code
      let countdown = setInterval(() => {
        if (seconds > 0) {
          this.codeText = seconds + "s";
          --seconds;
        } else {
          this.codeText = "发送";
          this.codeDisabled = false;
          clearInterval(countdown);
        }
      }, 1000);
    },
    submit() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const data = { tel: this.user.tel, code: this.user.code, password: this.user.password, }
          resetPwd(data).then(res => {
            console.log('res', res)
            if (res.code == 200) {
              this.user = {
                oldPassword: undefined,
                newPassword: undefined,
                confirmPassword: undefined
              },
                this.$message.success('修改成功！')
            }
          });
        }
      });
    },
  }
};
</script>
