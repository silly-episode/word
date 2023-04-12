<!--
 * @Author: dengyinzhe
 * @Date: 2023/4/8 15:10
 * @FileName: ArticleInfo
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
          :model="articleMessage"
          label-width="120px"
          style="margin: 0 7% 0 0"
      >
        <el-row v-if="flag" :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="ID" prop="articleId">
              <el-input v-model.trim="articleMessage.articleId" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="添加时间" prop="articleCreateTime">
              <el-input v-model.trim="articleMessage.articleCreateTime" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="flag" :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="学习人数" prop="articleStudyNumber">
              <el-input v-model.number="articleMessage.articleStudyNumber" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="字数" prop="wordNumber">
              <el-input v-model.trim="articleMessage.wordNumber" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="标题" prop="articleTitle">
              <el-input v-model.trim="articleMessage.articleTitle" :disabled="superFlag"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="作者" prop="articleAuthor">
              <el-input v-model.trim="articleMessage.articleAuthor" :disabled="superFlag"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-form-item label="内容" prop="content">
            <el-input v-model="articleMessage.content" :disabled="superFlag" :rows="13" maxlength="5000" show-word-limit
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
import {insertArticle, updateArticle} from "@/api/admin.js"

export default {
  name: 'ArticleInfo',
  data() {
    return {
      buttonContent: "",
      title: "",
      flag: false,
      superFlag: false,
      visible: false,
      articleMessage: {
        articleTitle: "",
        tel: "",
        remark: "",
        role: "",
        articleAuthor: "",
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
        updateArticle(this.articleMessage)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              this.$message.error(err.msg)
            });
      } else {
        console.log(2)
        insertArticle(this.articleMessage)
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
      if (data === "article") {
        this.title = "添加文章"
        this.buttonContent = "提交"
        this.articleMessage.role = this.roleList[0].value
        this.flag = false;
      } else {
        this.flag = true;
        this.title = data.articleTitle + "的详细信息"
        this.buttonContent = "保存"
        this.articleMessage = JSON.parse(JSON.stringify(data));
      }

      this.visible = true
    },

    // 对话框的关闭
    closed() {
      this.articleMessage = {};
      this.superFlag = false;
      this.$emit('searchAgain')
      this.visible = false
    },
  }
}
</script>

<style>
</style>

