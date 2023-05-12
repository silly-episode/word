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
      v-if="visible"
      :destroy-on-close="true"
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

        <el-row v-if="flag" :gutter="20" class="flex_center_center">
          <el-col :span="3">
            <el-form-item label="头像">
              <div class="demo-image__preview">
                <el-image
                    ref="oldPicture"
                    :preview-src-list="srcList"
                    :src="url"
                    style="width: 100px; height: 100px">
                </el-image>
              </div>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item>
              <el-upload
                  ref="changePicture"
                  :auto-upload="true"
                  :headers="headers"
                  :limit="1"
                  :on-success="importPictureSuccess"
                  accept=".jpg"
                  action="api/word/uploadImage"
                  list-type="picture-card">
                <i slot="default" class="el-icon-plus"></i>
                <div slot="file" slot-scope="{file}">
                  <img
                      :src="file.url"
                      alt="" class="el-upload-list__item-thumbnail">
                  <span class="el-upload-list__item-actions">
                    <span
                        class="el-upload-list__item-preview"
                        @click="handlePictureCardPreview(file)">
                      <i class="el-icon-zoom-in"></i>
                    </span>
                    <span
                        v-if="!disabled"
                        class="el-upload-list__item-delete"
                        @click="handleRemove(file)">
                      <i class="el-icon-delete"></i>
                    </span>
                  </span>
                </div>
              </el-upload>
              <el-dialog :append-to-body="true" :visible.sync="dialogVisible">
                <img :src="dialogImageUrl" alt="" width="100%">
              </el-dialog>
            </el-form-item>
          </el-col>
          <el-col :span="10">
            <el-form-item label="操作按钮">
              <el-button :disabled="this.importLoading" type="danger" @click="onSubmit"
                         v-text="buttonContent"></el-button>
              <el-button :disabled="this.importLoading" type="warning" @click="resetModuleMessage">复原</el-button>
              <el-button :disabled="this.importLoading" type="info" @click="closed">关闭</el-button>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row v-if="!flag" :gutter="50" class="flex_center_center">
          <el-col :span="12">
            <el-form-item label="上传文件">
              <el-tooltip
                  :enterable="false"
                  :file-list="fileList"
                  :limit="2"
                  content="只能上传一份Json词源文件和一份Jpg单词模块封面图片"
                  effect="dark"
                  placement="top">
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
                    :headers="headers"
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
            <el-form-item label="操作按钮">
              <el-button :disabled="this.importLoading" type="danger" @click="onSubmit"
                         v-text="buttonContent"></el-button>
              <el-button :disabled="this.importLoading" type="warning" @click="clearFileList">清空文件列表</el-button>
              <el-button :disabled="this.importLoading" type="info" @click="closed">关闭</el-button>
            </el-form-item>
          </el-col>
        </el-row>


      </el-form>
    </div>

  </el-dialog>
</template>


<script>
import {updateModule} from "@/api/admin.js"
import dayjs from "dayjs";

export default {
  name: 'WordInfo',
  data() {
    return {
      /*原始头像信息*/
      url: '',
      srcList: [],
      /*未知*/
      disabled: false,
      dialogImageUrl: '',
      dialogVisible: false,


      /*新头像名*/
      newPictureName: "",

      importLoading: false,
      headers: {
        Authorization: window.sessionStorage.getItem('adminToken')
      },
      fileList: [],
      buttonContent: "",
      title: "",
      flag: false,
      visible: false,
      moduleMessageOrigin: {},
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

    handleRemove(file) {
      console.log(file);
      this.$refs.changePicture.clearFiles();
    },

    handlePictureCardPreview(file) {
      this.dialogImageUrl = file.url;
      this.dialogVisible = true;
    },


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

    /*复原信息*/
    resetModuleMessage() {
      this.moduleMessage = JSON.parse(JSON.stringify(this.moduleMessageOrigin));
    },

    importPictureSuccess(response) {
      if (response.code === 200) {
        console.log(response)
        this.newPictureName = response.data;
      }
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


    onSubmit() {
      if (this.flag) {
        this.moduleMessage.moduleImagePath = this.newPictureName;
        updateModule(this.moduleMessage)
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
        this.moduleMessageOrigin = JSON.parse(JSON.stringify(data));
        this.url = "api/word/wordModuleImage/" + this.moduleMessageOrigin.moduleId;
        this.srcList[0] = this.url
      }
      this.visible = true
    },

    // 对话框的关闭
    closed() {
      this.moduleMessage = {};
      if (!this.flag && !this.importLoading) {
        this.flagText = "将文件拖到此处，或<em>点击上传"
        this.fileCount = 0
        this.moduleMessage = {};
        this.fileList = []
        this.jsonFlag = false;
        this.jpgFlag = false
        this.twoFlag = false
      } else if (this.flag) {
        this.url = '';
        this.$refs.changePicture.clearFiles();
      }

      this.$emit('searchAgain')
      this.visible = false
    },
  }
}
</script>

<style scoped>

</style>

