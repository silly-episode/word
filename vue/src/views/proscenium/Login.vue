<template>
  <div v-if="visible" class="z-index po">
    <div class="shade" @click="closed"></div>
    <div :class="`dowebok${signUp ? ' right-panel-active' : ''}`">
      <div class="form-container sign-up-container">
        <div
          class="flex_column_center_center text_center padding_0_50 hei_per100"
        >
          <h1 class="margin_b_15 font_30">注册</h1>
          <span class="margin_b_10">请输入您的信息</span>
          <el-form
            ref="registerForm"
            :model="registerForm"
            :rules="rules"
            class="login-form"
          >
            <el-form-item prop="account">
              <el-input
                v-model="registerForm.account"
                placeholder="请输入账号"
                prefix-icon="el-icon-s-custom"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="nickName">
              <el-input
                v-model="registerForm.nickName"
                placeholder="请输入用户名"
                prefix-icon="el-icon-collection-tag"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="registerForm.password"
                type="password"
                placeholder="请输入密码"
                prefix-icon="el-icon-lock"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="pwd">
              <el-input
                v-model="pwd"
                type="password"
                placeholder="请再次确认密码"
                prefix-icon="el-icon-lock"
              >
              </el-input>
            </el-form-item>
            <div @click="register" class="btn margin_t_10">注册</div>
          </el-form>
        </div>
      </div>

      <div class="form-container sign-in-container">
        <div
          class="flex_column_center_center text_center padding_0_50 hei_per100"
        >
          <h1 class="margin_b_15 font_30">登录</h1>
          <span class="margin_b_20"
            >或使用{{ telLogin ? "账号密码登录" : "手机号验证" }}</span
          >
          <el-form ref="loginForm" :model="loginForm" class="login-form">
            <el-form-item prop="loginAccount">
              <el-input
                v-if="telLogin"
                v-model="loginForm.loginAccount"
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
              <el-input
                v-else
                v-model="loginForm.loginAccount"
                placeholder="请输入账号"
                prefix-icon="el-icon-user"
              >
              </el-input>
            </el-form-item>
            <el-form-item prop="loginPassword">
              <el-input
                v-model="loginForm.loginPassword"
                type="password"
                :placeholder="`请输入${telLogin ? '验证码' : '密码'}`"
                prefix-icon="el-icon-lock"
              >
              </el-input>
            </el-form-item>
            <div class="margin_t_20 flex_around_center">
              <div class="btn" @click="login">登录</div>
              <div class="btn" @click="changeLogin">
                <i :class="`el-icon-${telLogin ? 'key' : 'phone'}`"></i>
              </div>
            </div>
          </el-form>
        </div>
      </div>

      <div class="overlay-container">
        <div class="overlay">
          <div class="overlay-panel overlay-left">
            <h1 class="margin_b_15 font_30">已有帐号？</h1>
            <p>请使用您的帐号进行登录</p>
            <button class="ghost" @click.stop="signUp = !signUp">登录</button>
          </div>
          <div class="overlay-panel overlay-right">
            <h1 class="margin_b_15 font_30">没有帐号？</h1>
            <p>立即注册加入我们，和我们一起开始旅程吧</p>
            <button class="ghost" @click.stop="signUp = !signUp">注册</button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {login, register, sms} from '@/api/user'

export default {
  name: 'Login',
  data() {
    var validatePass = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入密码'));
      } else {
        if (this.registerForm.password !== '') {
          this.$refs.registerForm.validateField('pwd');
        }
        callback();
      }
    };
    var validatePass2 = (rule, value, callback) => {
      if (this.pwd === '') {
        callback(new Error('请再次输入密码'));
      } else if (this.pwd !== this.registerForm.password) {
        console.log(this.pwd)
        console.log(this.registerForm.password)
        callback(new Error('两次输入密码不一致!'));
      } else {
        callback();
      }
    };
    return {
      telLogin: false,
      codeDisabled: true,
      codeText: '发送',
      signUp: false,
      visible: false,
      loginForm: {
        loginAccount: "",
        loginPassword: "",
        type: ''
      },
      registerForm: {
        nickName: "",
        account: "",
        password: ""
      },
      pwd: '',
      rules: {
        account: [{required: true, message: '请输入账号', trigger: 'blur'},
          {min: 6, max: 15, message: '长度在 6 到 15 个字符', trigger: 'blur'}],
        nickName: [{required: true, message: '请输入昵称', trigger: 'blur'},
          {min: 1, max: 10, message: '长度在 1 到 10 个字符', trigger: 'blur'}],
        password: [
          {validator: validatePass, trigger: 'blur'}
        ],
        pwd: [
          {validator: validatePass2, trigger: 'blur'}
        ]
      }
    }
  },

  created() {
    /*清除和设置相关信息*/
    window.sessionStorage.removeItem("adminToken")
    /*当前为普通用户使用*/
    window.sessionStorage.setItem("userKind", "commonUser");
  },

  methods: {
    showLogin() {
      this.visible = true
    },
    // 手机号正则
    checkTel() {
      console.log(this.loginForm.loginAccount)
      const TEL_REGEXP = /^1([38][0-9]|4[579]|5[0-3,5-9]|6[6]|7[0135678]|9[89])\d{8}$/;
      let telFlag = TEL_REGEXP.test(this.loginForm.loginAccount)
      if (telFlag) this.codeDisabled = false
      else this.codeDisabled = true
    },
    sendCode() {
      sms(this.loginForm.loginAccount)
        .then((res) => {
          console.log(res)
          if (res.code === 200) {
            this.$notify.success({
              title: '成功',
              message: "已发送验证码，请查收",
              offset: 60
            });
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
    // 切换登录模式
    changeLogin() {
      this.telLogin = !this.telLogin
      this.empty()
    },

    login() {
      const {loginForm} = this
      if (!loginForm.loginAccount) {
        this.$notify.warning({
          title: '警告',
          message: `请输入${this.telLogin ? '手机号码' : '账号'}`,
          offset: 60
        });
      } else if (!loginForm.loginPassword) {
        this.$notify.warning({
          title: '警告',
          message: `请输入${this.telLogin ? '验证码' : '密码'}`,
          offset: 60
        });
      } else {
        if (this.telLogin) loginForm.type = 'sms'
        else loginForm.type = 'pwd'

        login(this.loginForm)
            .then((res) => {
              if (res.code === 200) {
                // console.log(res)
                // window.sessionStorage.removeItem("adminToken")
                // /*当前为普通用户使用*/
                // window.sessionStorage.setItem("userKind", "commonUser");
                window.sessionStorage.setItem("token", res.data.token);
                window.sessionStorage.setItem('userInfo', JSON.stringify(res.data.userInfo))
                // this.$emit('beLogin')
                // debugger
                // console.log('path', this.$route.path)
                if (this.$route.path == '/myword') this.$bus.$emit('myword')
                if (this.$route.path == '/own') this.$bus.$emit('own')
                if (this.$route.path == '/module') this.$bus.$emit('module')
                this.$bus.$emit('beLogin')
                this.closed()
              }
          })
          .catch((err) => {
            console.log('err', err)
          })
      }

    },

    // 注册
    register() {
      this.$refs.registerForm.validate((valid) => {
        if (valid) {
          console.log(this.registerForm)
          register(this.registerForm)
            .then((res) => {
              // console.log(res)
              window.sessionStorage.setItem("token", res.data);
              this.$notify.success({
                title: '成功',
                message: "注册成功！",
                offset: 60
              });
              this.closed()
              // alert('submit!');
            })

        } else return false;
      });
    },

    closed() {
      this.visible = false
      this.empty()
    },
    empty() {
      this.loginForm = {
        loginAccount: "",
        loginPassword: "",
        type: ''
      }
      this.registerForm = {
        nickName: "",
        account: "",
        password: ""
      }
    }
  }
}
</script>

<style scoped>
.po {
  position: absolute;
  top: 100px;
  left: 320px;
}
.form-container >>> .el-input__inner {
  background: #eee;
  border: none;
  /* padding: 12px 15px;
  margin: 8px 0; */
  height: 43px;
  border-radius: 0px;
  width: 100%;
  outline: none;
}

.form-container >>> .el-form {
  width: 300px;
}

.fixIcon {
  position: absolute;
  top: 93px;
  right: 268px;
  font-size: 22px;
}

.dowebok {
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 14px 28px rgba(0, 0, 0, 0.25), 0 10px 10px rgba(0, 0, 0, 0.22);
  position: relative;
  overflow: hidden;
  width: 768px;
  max-width: 100%;
  min-height: 480px;
}

.social-container {
  margin: 20px 0;
}

.social-container a {
  border: 1px solid #ddd;
  border-radius: 50%;
  display: inline-flex;
  justify-content: center;
  align-items: center;
  margin: 0 5px;
  height: 40px;
  width: 40px;
}

.social-container a:hover {
  background-color: #eee;
}

.input {
  background: #eee;
  border: none;
  padding: 12px 15px;
  margin: 8px 0;
  width: 100%;
  outline: none;
}

button {
  border-radius: 35px;
  border: 1px solid lightseagreen;
  background: #83af9b;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  padding: 10px 30px;
  letter-spacing: 5px;
  text-transform: uppercase;
  transition: transform 80ms ease-in;
  cursor: pointer;
}
.btn {
  border-radius: 35px;
  border: 1px solid lightseagreen;
  background: #83af9b;
  color: #fff;
  font-size: 16px;
  font-weight: bold;
  padding: 10px 30px;
  letter-spacing: 5px;
  text-transform: uppercase;
  transition: transform 80ms ease-in;
  cursor: pointer;
}

button:active {
  transform: scale(0.95);
}

button:focus {
  outline: none;
}

button.ghost {
  background: transparent;
  border-color: #fff;
}

.form-container {
  position: absolute;
  top: 0;
  height: 100%;
  transition: all 0.6s ease-in-out;
}

.sign-in-container {
  left: 0;
  width: 50%;
  z-index: 2;
}

.sign-up-container {
  left: 0;
  width: 50%;
  z-index: 1;
  opacity: 0;
}

.overlay-container {
  position: absolute;
  top: 0;
  left: 50%;
  width: 50%;
  height: 100%;
  overflow: hidden;
  transition: transform 0.6s ease-in-out;
  z-index: 100;
}

.overlay {
  background: #83af9b;
  background: linear-gradient(to right, #83af9b, #83af9b) no-repeat 0 0 / cover;
  color: #fff;
  position: relative;
  left: -100%;
  height: 100%;
  width: 200%;
  transform: translateY(0);
  transition: transform 0.6s ease-in-out;
}

.overlay-panel {
  position: absolute;
  top: 0;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 0 40px;
  height: 100%;
  width: 40%;
  text-align: center;
  transform: translateY(0);
  transition: transform 0.6s ease-in-out;
}

.overlay-right {
  right: 0;
  transform: translateY(0);
}

.overlay-left {
  transform: translateY(-20%);
}

.dowebok.right-panel-active .sign-in-container {
  transform: translateY(100%);
}

.dowebok.right-panel-active .overlay-container {
  transform: translateX(-100%);
}

.dowebok.right-panel-active .sign-up-container {
  transform: translateX(100%);
  opacity: 1;
  z-index: 5;
}

/* Move overlay back to right */

.dowebok.right-panel-active .overlay {
  transform: translateX(50%);
}

/* Bring back the text to center */

.dowebok.right-panel-active .overlay-left {
  transform: translateY(0);
}

/* Same effect for right */

.dowebok.right-panel-active .overlay-right {
  transform: translateY(20%);
}

p {
  font-size: 14px;
  line-height: 20px;
  letter-spacing: 0.5px;
  margin: 20px 0 30px;
}
</style>
