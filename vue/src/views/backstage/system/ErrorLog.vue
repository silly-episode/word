<!--
 * @Author: dengyinzhe
 * @Date: 2023/2/8 10:31
 * @FileName: wordModelUpload
 * @LastEditors: 2023/2/8 10:31
 * @LastEditTime: 2023/2/8 10:31
 * @Description:
-->
<template>


  <div>
    <el-form ref="form" :model="wordModule" label-width="80px">
      <el-form-item label="上级模块名称:">
        <el-input v-model="wordModule.superiorModule"></el-input>
      </el-form-item>
      <el-form-item label="模块名称:">
        <el-input v-model="wordModule.moduleName"></el-input>
      </el-form-item>
      <el-form-item label="描述:">
        <el-input v-model="wordModule.remark"></el-input>
      </el-form-item>
    </el-form>
    <el-upload
        ref="upload"
        :auto-upload="false"
        :data="this.wordModule"
        :file-list="fileList"
        :headers="headers"
        :limit="2"
        :on-exceed="handleExceed"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        action="api/word/wordModule"
        class="wordModuleUpload">
      <el-button slot="trigger" size="small" type="primary">选取文件</el-button>
      <div slot="tip" class="el-upload__tip">只能上传jpg/png/json文件</div>
    </el-upload>
    <el-button type="success" @click="submitUpload">提交</el-button>
  </div>
</template>

<script>


export default {
  name: "wordModelUpload",
  data() {
    return {
      headers: {
        Authorization: "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzU5MTk2MzcsImlhdCI6MTY3NTgzMzIzNywiYWNjb3VudCI6Ijg5NzkxNzcyOCJ9.kjGMhBFkBFxf_D6G-srMkPogilpL91vE_EEE3n71ozA"
      },
      wordModule: {
        superiorModule: "",
        moduleName: "",
        remark: "",
      },
      fileList: [],
    };
  },
  methods: {


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
