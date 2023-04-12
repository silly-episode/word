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
      @open="init"
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

        <el-row v-if="flag" :gutter="50">
          <el-col :span="12">
            <el-form-item aria-rowindex="50px" label="状态" prop="wordModuleStatus">
              <el-input v-model.trim="statusList[moduleMessage.wordModuleStatus]" :disabled="true"></el-input>
            </el-form-item>
          </el-col>

          <el-col :span="12">
            <el-form-item label="锁定/解锁时间" prop="lockTime">
              <el-input v-model.trim="moduleMessage.lockTime" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>


        <el-row v-if="flag" :gutter="50">
          <el-col :span="12">
            <el-form-item label="单词数量" prop="wordCount">
              <el-input v-model.trim="moduleMessage.wordCount" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="学习人数" prop="studyNumber">
              <el-input v-model.trim="moduleMessage.studyNumber" :disabled="true"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="50">
          <el-col :span="12">
            <el-form-item label="上级模块" prop="superiorModule">
              <el-select
                  v-model="moduleMessage.superiorModule"
                  style="width: 100%">
                <el-option
                    v-for="status in superiorModuleList"
                    :key="status.key"
                    :label="status.label"
                    :value="status.key">
                </el-option>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="模块名称" prop="moduleName">
              <el-input v-model.trim="moduleMessage.moduleName"></el-input>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row>
          <el-form-item label="说明" prop="remark">
            <el-input v-model.trim="moduleMessage.remark" :rows="4" maxlength="100" show-word-limit
                      type="textarea"></el-input>
          </el-form-item>
        </el-row>


        <el-row v-if="!flag" :gutter="50" class="flex_center_center">
          <el-col :span="12">
            <el-form-item label="上传文件" prop="remark">
              <el-tooltip
                  :enterable="false"
                  :file-list="fileList"
                  :limit="2"
                  content="只能上传一份Json词源文件和一份Jpg单词模块封面图片"
                  effect="dark"
                  placement="top">
                <!--                    -->
                <!--                    :headers="headers"-->
                <el-upload
                    ref="upload"
                    :auto-upload="false"
                    :data="this.moduleMessage"
                    :disabled="this.importLoading||this.twoFlag"
                    :file-list="fileList"
                    :on-change="fileChange"
                    :on-error="importError"
                    :on-success="importSuccess"
                    :show-file-list="true"
                    accept=".json,.jpg"
                    action="api/word/wordModule"
                    drag>
                  <i class="el-icon-upload"></i>
                  <div class="el-upload__text" v-html="flagText"> 将文件拖到此处，或<em>点击上传</em></div>
                </el-upload>
              </el-tooltip>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="操作按钮" prop="remark">
              <el-button :disabled="this.importLoading" type="danger" @click="onSubmit"
                         v-text="buttonContent"></el-button>
              <el-button :disabled="this.importLoading" type="warning" @click="clearFileList">清空文件列表</el-button>
              <el-button :disabled="this.importLoading" type="info" @click="closed">关闭</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item v-if="flag" class="flex_center_center">
          <el-button type="danger" @click="onSubmit" v-text="buttonContent"></el-button>
          <el-button type="info" @click="closed">关闭</el-button>
        </el-form-item>
      </el-form>
    </div>

  </el-dialog>
</template>


<script>
import {updateWord} from "@/api/admin.js"
import dayjs from "dayjs";

export default {
  name: 'WordInfo',
  data() {
    return {
      importLoading: false,
      headers: {
        Authorization: "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzU5MTk2MzcsImlhdCI6MTY3NTgzMzIzNywiYWNjb3VudCI6Ijg5NzkxNzcyOCJ9.kjGMhBFkBFxf_D6G-srMkPogilpL91vE_EEE3n71ozA"
      },
      fileList: [],
      buttonContent: "",
      title: "",
      flag: false,
      visible: false,
      moduleMessage: {
        remark: "",
        moduleName: "",
        superiorModule: "",
        esIndex: "",
      },
      statusList: ['正常', '锁定'],
      superiorModuleList: [
        {label: '出国必用', key: "0"},
        {label: '大学词汇', key: "1"},
        {label: '高中词汇', key: "2"},
        {label: '初中词汇', key: "3"},
        {label: '小学词汇', key: "4"},
      ],
      flagText: "将文件拖到此处，或<em>点击上传",
      jsonFlag: false,
      jpgFlag: false,
      twoFlag: false,
      fileCount: 0
    }
  },
  methods: {

    //文件改变时调用
    fileChange(file, fileList) {
      this.fileCount = fileList.length;
      if (!this.importLoading) {
        this.twoFlag = false;
        this.jsonFlag = false;
        this.jpgFlag = false;
        this.twoFlag = false
        if (file.size === 0) {
          fileList.pop()
          this.twoFlag = false
          this.$message.warning("请勿上传空文件");
        }
        if (fileList.length === 2) {
          this.twoFlag = true
          fileList.forEach((item, index) => {
            let suffix = item.name.substring(item.name.lastIndexOf(".") + 1);
            if (suffix === "jpg") {
              this.jpgFlag = true;
            }
            if (suffix === "json") {
              this.jsonFlag = true;
            }
          });
          if (!(this.jsonFlag && this.jpgFlag)) {
            fileList.pop()
            this.twoFlag = false
            this.$message.warning("请按照要求上传");
          }
        }
      }
    },

    //清空文件列表
    clearFileList() {
      this.twoFlag = false
      this.$refs.upload.clearFiles();
    },


    importSuccess(response) {
      if (response.code === 201) {
        console.log("12423423")
        this.importLoading = false;
        this.flagText = "<em>导入成功</em>"
        this.$message.success("导入成功");
        this.closed();
      }
    },


    importError(err) {
      this.importLoading = false;
      this.flagText = "<em>导入失败</em>"
      this.$message.error(err.msg);
    },

    //提交用户信息
    onSubmit() {
      if (this.flag) {
        updateWord(this.moduleMessage)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              this.$message.error(err.msg)
            });
      } else {
        if (this.fileCount !== 2) {
          this.$message.warning("请按要求上传文件")
          return;
        }
        this.moduleMessage.moduleImagePath = "word_module_image" + dayjs().valueOf().toString() + ".jpg"
        this.importLoading = true;
        this.flagText = "<em>上传解析中,请稍等。。。</em>"
        this.$refs.upload.submit();
      }

    },

    init() {
      this.$nextTick(() => {
      })
    },

    showEditDialog(data) {
      if (data === "module") {
        this.title = "录入单词模块"
        this.buttonContent = "导入词源"
        this.moduleMessage.superiorModule = this.superiorModuleList[0].key
        this.flag = false;
      } else {
        this.flag = true;
        this.title = data.moduleName + "的详细信息"
        this.buttonContent = "保存"
        this.moduleMessage = JSON.parse(JSON.stringify(data));
      }
      this.visible = true
    },

    // 对话框的关闭
    closed() {
      if (!this.importLoading) {
        this.flagText = "将文件拖到此处，或<em>点击上传"
        this.fileCount = 0
        this.moduleMessage = {};
        this.fileList = []
        this.jsonFlag = false;
        this.jpgFlag = false
        this.twoFlag = false
      }
      this.$emit('searchAgain')
      this.visible = false
    },
  }
}
</script>

<style>
</style>

