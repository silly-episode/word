<!--
 * @Author: dengyinzhe
 * @Date: 2023/4/8 15:10
 * @FileName: AdminInfo
 * @LastEditors: 2023/4/8 15:10
 * @LastEditTime: 2023/4/8 15:10
 * @Description:
-->
<template>
  <el-dialog
      :close-on-click-modal="false"
      :lock-scroll="true"
      :title=title
      :visible="visible"
      style="margin: 0 0 0 12%"
      width="70%"
      @close="closed"
      @open="init "
  >
    <div style=" margin:0 auto">
      <el-form
          ref="formRef"
          :model="adminMessage"
          label-width="120px"
          style="margin: 0 7% 0 0"
      >
        <el-row v-if="flag" :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="ID" prop="adminId">
              <el-input v-model.trim="adminMessage.adminId" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="添加时间" prop="addCreateTime">
              <el-input v-model.trim="adminMessage.addCreateTime" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="flag" :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="状态" prop="userStatus">
              <el-input v-model.number="adminMessage.userStatus" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="解锁/锁定时间" prop="lockTime">
              <el-input v-model.trim="adminMessage.lockTime" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="账号" prop="account">
              <el-input v-model.trim="adminMessage.account" :disabled="superFlag"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="持有人" prop="keepName">
              <el-input v-model.trim="adminMessage.keepName" :disabled="superFlag"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="手机号" prop="tel">
              <el-input v-model.trim="adminMessage.tel" :disabled="superFlag"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="角色" prop="role">
              <el-select
                  v-model="adminMessage.role"
                  :disabled="superFlag"
                  style="width: 100%">
                <el-option
                    v-for="item in roleList"
                    :key="item.value"
                    :label="item.label"
                    :value="item.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row>
          <el-form-item label="描述" prop="remark">
            <el-input v-model.trim="adminMessage.remark" :disabled="superFlag" :rows="4" maxlength="500" show-word-limit
                      type="textarea"></el-input>
          </el-form-item>
        </el-row>

        <el-form-item class="flex_center_center">
          <el-button :disabled="superFlag" type="danger" @click="onSubmit" v-text="buttonContent"></el-button>
          <el-button type="info" @click="closed">关闭</el-button>
        </el-form-item>
      </el-form>
    </div>

  </el-dialog>
</template>


<script>
import {addAdmin, updateAdmin} from "@/api/admin.js"

export default {
  name: 'AdminInfo',
  data() {
    return {
      buttonContent: "",
      title: "",
      flag: false,
      superFlag: false,
      visible: false,
      adminMessage: {
        account: "",
        tel: "",
        remark: "",
        role: "",
        keepName: "",
      },
      roleList: [
        // 角色，普通管理员 1 .。。。
        {label: '普通管理员', value: '1'}
      ],
    }
  },
  methods: {
    //提交用户信息
    onSubmit() {
      if (this.flag) {
        console.log(1)
        updateAdmin(this.adminMessage)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              this.$message.error(err.msg)
            });
      } else {
        console.log(2)
        addAdmin(this.adminMessage)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              debugger
              console.log("error")
              this.$message.error(err.msg)
            });
      }

    },

    init() {
      this.$nextTick(() => {
      })
    },

    showEditDialog(data) {
      if (data === "admin") {
        this.title = "添加管理员"
        this.buttonContent = "提交"
        this.adminMessage.role = this.roleList[0].value
        this.flag = false;
      } else {
        this.flag = true;
        this.title = data.keepName + "的详细信息"
        this.buttonContent = "保存"
        this.adminMessage = JSON.parse(JSON.stringify(data));
        if ("超级管理员" === this.adminMessage.role) {
          this.superFlag = true
        } else {
          this.roleList.forEach((item, index) => {
            if (item.label === this.adminMessage.role) {
              this.adminMessage.role = item.value;
            }
          })
        }
      }


      this.visible = true
    },

    // 对话框的关闭
    closed() {
      this.adminMessage = {};
      this.superFlag = false;
      this.$emit('searchAgain')
      this.visible = false
    },
  }
}
</script>

<style>
</style>

