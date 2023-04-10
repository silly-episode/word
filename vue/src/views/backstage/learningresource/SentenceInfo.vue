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
          :model="emoMessage"
          label-width="120px"
          style="margin: 0 7% 0 0"
      >
        <el-row v-if="flag" :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="ID" prop="emoId">
              <el-input v-model.trim="emoMessage.emoId" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="添加时间" prop="emoCreateTime">
              <el-input v-model.trim="emoMessage.emoCreateTime" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="作者" prop="emoAuthor">
              <el-input v-model.trim="emoMessage.emoAuthor"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="出现概率" prop="frequency">
              <el-select
                  v-model="emoMessage.frequency"
                  style="width: 100%">
                <el-option
                    v-for="status in frequencyList"
                    :key="status.key"
                    :label="status.label"
                    :value="status.value">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-form-item label="中文" prop="cnContent">
            <el-input v-model.trim="emoMessage.cnContent" :rows="4" maxlength="500" show-word-limit
                      type="textarea"></el-input>
          </el-form-item>
        </el-row>

        <el-row>
          <el-form-item label="英文" prop="engContent">
            <el-input v-model.trim="emoMessage.engContent" :rows="4" maxlength="500" show-word-limit
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
  name: 'SentenceInfo',
  data() {
    return {
      buttonContent: "",
      title: "",
      flag: false,
      visible: false,
      emoMessage: {
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
        updateWord(this.emoMessage)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              this.$message.error(err.msg)
            });
      } else {
        console.log(2)
        insertWord(this.emoMessage)
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
        this.emoMessage.frequency = this.frequencyList[1].value
        this.flag = false;
      } else {
        this.flag = true;
        this.title = "励志语的详细信息"
        this.buttonContent = "保存"
        this.emoMessage = JSON.parse(JSON.stringify(data));
      }


      this.visible = true
    },

    // 对话框的关闭
    closed() {
      this.emoMessage = {};
      this.$emit('searchAgain')
      this.visible = false
    },
  }
}
</script>

<style>
</style>

