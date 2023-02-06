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
          action="api/upload"
          :on-preview="handlePreview"
          :on-remove="handleRemove"
          :before-remove="beforeRemove"
          multiple="false"
          :limit="3"
          :on-exceed="handleExceed"
          :file-list="fileList">
        <el-button size="small" type="primary">点击上传</el-button>
        <div slot="tip" class="el-upload__tip">只能上传jpg/png文件，且不超过500kb</div>
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

    handleRemove(file, fileList) {
      console.log(file, fileList);
    },
    handlePreview(file) {
      console.log(file);
    },
    handleExceed(files, fileList) {
      this.$message.warning(`当前限制选择 3 个文件，本次选择了 ${files.length} 个文件，共选择了 ${files.length + fileList.length} 个文件`);
    },
    beforeRemove(file, fileList) {
      return this.$confirm(`确定移除 ${ file.name }？`);
    }
  }
}
</script>

<style scoped>

</style>
