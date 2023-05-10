<!--
 * @Author: dengyinzhe
 * @Date: 2023/4/21 18:20
 * @FileName: AdminLogin
 * @LastEditors: 2023/4/21 18:20
 * @LastEditTime: 2023/4/21 18:20
 * @Description:
-->
<template>
  <div class="container" onclick="onclick">
    <div class="top"></div>
    <div class="bottom"></div>
    <div class="center">
      <h2 style="margin-bottom: 30px">Word Management</h2>
      <input v-model.trim="loginMessage.loginAccount" placeholder="Account" type="text"/>
      <input v-model.trim="loginMessage.loginPassword" placeholder="Password" type="password"/>
      <el-button style="margin-Top: 15px" type="warning" @click="adminLogin">LOGIN</el-button>
      <h2>&nbsp;</h2>
    </div>
  </div>
</template>

<script>
import {adminLogin} from "@/api/admin.js"

export default {
  name: "AdminLogin",
  data() {
    return {
      loginMessage: {
        loginAccount: "",
        loginPassword: "",
      }
    }
  },
  methods: {
    adminLogin() {
      /*清除和设置相关信息*/
      window.sessionStorage.clear();
      window.sessionStorage.setItem("userKind", "admin");
      adminLogin(this.loginMessage)
          .then((res) => {
            // window.sessionStorage.clear();
            // window.sessionStorage.setItem("userKind", "admin");

            window.sessionStorage.setItem("adminToken", res.data);
            this.$router.push('/admin/welcome')
          })
          .catch((err) => {
            console.log(err)
          })
    },
  },


};
</script>

<style scoped>

*, *:before, *:after {
  box-sizing: border-box;
}

body {
  min-height: 100vh;
  font-family: "Raleway", sans-serif;
}

.container {
  position: absolute;
  width: 100%;
  height: 100%;
  overflow: hidden;
}

.container:hover .top:before, .container:hover .top:after, .container:hover .bottom:before, .container:hover .bottom:after, .container:active .top:before, .container:active .top:after, .container:active .bottom:before, .container:active .bottom:after {
  margin-left: 200px;
  transform-origin: -200px 50%;
  transition-delay: 0s;
}

.container:hover .center, .container:active .center {
  opacity: 1;
  transition-delay: 0.2s;
}

.top:before, .top:after, .bottom:before, .bottom:after {
  content: "";
  display: block;
  position: absolute;
  width: 200vmax;
  height: 200vmax;
  top: 50%;
  left: 50%;
  margin-top: -100vmax;
  transform-origin: 0 50%;
  transition: all 0.5s cubic-bezier(0.445, 0.05, 0, 1);
  z-index: 10;
  opacity: 0.65;
  transition-delay: 0.2s;
}

.top:before {
  transform: rotate(45deg);
  background: #e46569;
}

.top:after {
  transform: rotate(135deg);
  background: #ecaf81;
}

.bottom:before {
  transform: rotate(-45deg);
  background: #60b8d4;
}

.bottom:after {
  transform: rotate(-135deg);
  background: #3745b5;
}

.center {
  position: absolute;
  width: 400px;
  height: 400px;
  top: 50%;
  left: 50%;
  margin-left: -200px;
  margin-top: -200px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 30px;
  opacity: 0;
  transition: all 0.5s cubic-bezier(0.445, 0.05, 0, 1);
  transition-delay: 0s;
  color: #333;
}

.center input {
  width: 100%;
  padding: 15px;
  margin: 5px;
  border-radius: 1px;
  border: 1px solid #ccc;
  font-family: inherit;
}

</style>
