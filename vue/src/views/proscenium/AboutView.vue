<!--
 * @Author: dengyinzhe
 * @Date: 2023/1/30 12:37
 * @FileName: AboutView
 * @LastEditors: 2023/1/30 12:37
 * @LastEditTime: 2023/1/30 12:37
 * @Description:
-->
<template>
  <div>
    <div>
      <el-upload
          class="upload-demo"
          ref="upload"
          action="api/upload"
          :on-preview="handlePreview"
          :limit="2"
          :on-exceed="handleExceed"
          :on-remove="handleRemove"
          :file-list="fileList"
          :auto-upload="false">
        <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
        <el-button style="margin-left: 10px;" size="small" type="success" @click="submitUpload">上传到服务器</el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png/json文件</div>
      </el-upload>
    </div>



    <el-form ref="form" :model="form" label-width="80px">
      <el-form-item label="account:">
        <el-input v-model="form.loginAccount"></el-input>
      </el-form-item>
      <el-form-item label="password:">
        <el-input v-model="form.loginPassword"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button @click="login">登录</el-button>
      </el-form-item>
    </el-form>

  </div>
</template>

<script>
import {importWord} from "@/api/word"
import {login} from "@/api/user"
export default {
  name: "AboutView",
  data() {
    return {
      form: {
        loginAccount: "",
        loginPassword: "",
      },
      fileList: [{name: 'food.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}, {name: 'food2.jpeg', url: 'https://fuss10.elemecdn.com/3/63/4e7f3a15429bfda99bce42a18cdd1jpeg.jpeg?imageMogr2/thumbnail/360x360/format/webp/quality/100'}]
    };
  },
  methods: {

    login() {
      // let params =JSON.stringify(this.form)
      // alert(params)
      login(this.form)
      .then((res)=>{
        console.log(res)
      })
      .catch((err)=>{
        console.log(err)
      })
    },

    submitUpload() {
      this.$refs.upload.submit();
    },

    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 2 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
  }
}
</script>

<style scoped>

</style>
