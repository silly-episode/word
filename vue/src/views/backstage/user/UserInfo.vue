<template>
  <el-dialog
      :visible="visible"
      style="margin: 0 0 0 12%"
      :close-on-click-modal="false"
      :lock-scroll="true"
      :title="`${this.userMessage.nickName}的详细信息`"
      width="70%"
      @close="closed"
      @open="init "
  >
    <div style=" margin:0 auto">
      <el-form
          ref="formRef"
          :model="userMessage"
          label-width="120px"
          style="margin: 0 7% 0 0"
      >

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="ID" prop="userId">
              <el-input v-model.trim="userMessage.userId" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="注册时间" prop="registerTime">
              <el-date-picker
                  v-model="userMessage.registerTime"
                  :picker-options="{
                 firstDayOfWeek: 1}"
                  placeholder="选择日期时间"
                  size="large"
                  style="width: 100%"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="昵称" prop="nickName">
              <el-input v-model.trim="userMessage.nickName"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="账号" prop="account">
              <el-input v-model.trim="userMessage.account"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="手机号" prop="tel">
              <el-input v-model.trim="userMessage.tel"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="邮箱" prop="email">
              <el-input v-model.trim="userMessage.email"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="QQ" prop="qq">
              <el-input v-model.number="userMessage.qq"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="微信" prop="wechat">
              <el-input v-model.trim="userMessage.wechat"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="状态" prop="userStatus">
              <el-input v-model.trim="userMessage.userStatus" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="锁定/解锁时间" prop="lockTime">
              <el-date-picker
                  v-model="userMessage.lockTime"
                  :picker-options="{
                 firstDayOfWeek: 1}"
                  placeholder="选择日期时间"
                  size="large"
                  style="width: 100%"
                  type="datetime"
                  value-format="yyyy-MM-dd HH:mm:ss"
              >
              </el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="积分" prop="integration">
              <el-input v-model.number="userMessage.integration"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="描述" prop="remark">
              <el-input v-model.trim="userMessage.remark"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-form-item label="个性签名" prop="signature">
            <el-input v-model.trim="userMessage.signature" :rows="4" maxlength="500" show-word-limit
                      type="textarea"></el-input>
          </el-form-item>
        </el-row>

        <el-form-item class="flex_center_center">
          <el-button type="danger" @click="onSubmit">提交</el-button>
          <el-button type="info" @click="closed">关闭</el-button>
        </el-form-item>
      </el-form>
    </div>

  </el-dialog>
</template>


<script>
import {updateUser} from "@/api/admin.js"

export default {
  name: 'UserInfo',
  data() {
    return {
      visible: false,
      userMessage: {},// 在修改信息时查询到的用户信息对象
    }
  },
  methods: {
    //提交用户信息
    onSubmit() {
      this.$prompt('防止误操作,请输入:“没事别瞎修改，避免用户困扰”', '确认框', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        center: true,
        inputPattern: /^没事别瞎修改，避免用户困扰$/,
        inputErrorMessage: '输入内容错误'
      }).then(({value}) => {
        updateUser(this.userMessage)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              this.$message.error(err.msg)
            })
      }).catch(() => {

      });
    },

    init() {
      this.$nextTick(() => {
      })
    },

    showEditDialog(data) {
      console.log('从表格中获取到的用户信息', data)
      this.userMessage = null
      this.visible = true
      if (data) this.userMessage = JSON.parse(JSON.stringify(data));
    },

    // 对话框的关闭
    closed() {
      this.$emit('searchAgain')
      this.visible = false
    },
  }
}
</script>

<style>
</style>
