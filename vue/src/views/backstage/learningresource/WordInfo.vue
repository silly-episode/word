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
          :model="moduleMessage"
          label-width="120px"
          style="margin: 0 7% 0 0"
      >
        <el-row v-if="flag" :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="ID" prop="moduleId">
              <el-input v-model.trim="moduleMessage.moduleId" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="添加时间" prop="wordModuleCreateTime">
              <el-input v-model.trim="moduleMessage.wordModuleCreateTime" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="状态" prop="wordModuleStatus">
              <el-input v-model.trim="moduleMessage.wordModuleStatus"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="锁定/解锁时间" prop="lockTime">
              <el-input v-model.trim="moduleMessage.lockTime"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="上级模块" prop="superiorModule">
              <el-input v-model.trim="moduleMessage.superiorModule"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模块名称" prop="moduleName">
              <el-input v-model.trim="moduleMessage.moduleName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-col :span="12">
            <el-form-item label="单词数量" prop="wordCount">
              <el-input v-model.trim="moduleMessage.wordCount"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学习人数" prop="studyNumber">
              <el-input v-model.trim="moduleMessage.studyNumber"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-form-item label="说明" prop="remark">
            <el-input v-model.trim="moduleMessage.remark" :rows="4" maxlength="100" show-word-limit
                      type="textarea"></el-input>
          </el-form-item>
        </el-row>

        <el-form-item class="flex_center_center">
          <el-button type="danger" @click="onSubmit" v-text="buttonContent"></el-button>
          <el-button type="info" @click="closed">关闭</el-button>
        </el-form-item>
      </el-form>
    </div>

  </el-dialog>
</template>


<script>
import {insertWord, updateWord} from "@/api/admin.js"

export default {
  name: 'WordInfo',
  data() {
    return {
      buttonContent: "",
      title: "",
      flag: false,
      visible: false,
      moduleMessage: {
        emoAuthor: "",
        frequency: "",
        cnContent: "",
        engContent: "",
      },
      frequencyList: [
        // 角色，普通管理员 1 .。。。
        {value: '高', label: '高', key: 1},
        {value: '中', label: '中', key: 2},
        {value: '低', label: '低', key: 3}
      ],
    }
  },
  methods: {
    //提交用户信息
    onSubmit() {
      if (this.flag) {
        console.log(1)
        updateWord(this.moduleMessage)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              this.$message.error(err.msg)
            });
      } else {
        console.log(2)
        insertWord(this.moduleMessage)
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
      if (data === "emo") {
        this.title = "添加励语"
        this.buttonContent = "提交"
        this.moduleMessage.frequency = this.frequencyList[1].value
        this.flag = false;
      } else {
        this.flag = true;
        this.title = "励志语的详细信息"
        this.buttonContent = "保存"
        this.moduleMessage = JSON.parse(JSON.stringify(data));
      }


      this.visible = true
    },

    // 对话框的关闭
    closed() {
      this.moduleMessage = {};
      this.$emit('searchAgain')
      this.visible = false
    },
  }
}
</script>

<style>
</style>

