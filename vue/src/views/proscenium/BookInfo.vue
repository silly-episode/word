<template>
  <div class="container">
<!--  表头  -->
    <div class="title flex_between_center border_ccc">
      <input
          @blur="edit"
          :disabled="!editFlag"
          class="font_22 font_bold"
          type="text"
          ref='changeBookNameInput'
          :value="bookName"
      />
      <!-- <span class="font_22 font_bold">{{ bookName }}</span> -->

      <div class="flex_center">
        <el-input
            v-model="search"
            autocomplete="off"
            clearable
            placeholder="单词、释义 (模糊搜索)"
            style="margin-right: 10px;width: 50%"
            type="text"
            @input="() => $forceUpdate()"
        ></el-input>
        <el-button icon="el-icon-search" type="info" @click="getBookInfo">
          查询
        </el-button>
        <el-button v-if="list.length>0" icon="el-icon-s-promotion" type="success">
          <router-link
              :to="{
            name: 'word',
            params: { bookId,search, pageNum, pageSize },
          }"
          >温故知新
          </router-link>
        </el-button>
        <el-button
            v-if="list.length>0"
            :icon="`el-icon-${exportLoading?'loading':'download'}`"
            type="warning"
            @click="wordListPdf">
          导出
        </el-button>
        <el-button icon="el-icon-edit" type="primary" @click="changeBookName">重命名</el-button>
        <el-button icon="el-icon-delete-solid" type="danger" @click="deleteBook">删除单词本</el-button>
      </div>
    </div>

    <!-- 表体   -->
    <div class="shadow isbody padding_30">
      <div class="noTableScrollBar">
        <el-table
            v-loading="searchLoading"
            :data="list"
            style="width: 100%"
            :height="tableHeight === 0 ? 'calc(100vh - 327px)' : tableHeight"
            :header-cell-style="{ 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }"
            highlight-current-row
            stripe
        >
          <template slot="empty">
            <el-empty description="快去收藏单词吧！"></el-empty>
          </template>
          <el-table-column
              label="序号"
              min-width="10%"
          >
            <template v-slot="scope">
          <span>{{
              scope.$index + (pageNum - 1) * pageSize + 1
            }}</span>
            </template>
          </el-table-column>
          <el-table-column label="单词" min-width="20%" prop="word">
            <template slot-scope="scope">
              <el-popover placement="top" trigger="hover">
                <p>例句: {{ scope.row.sentenceEn }}</p>
                <p>翻译: {{ scope.row.sentenceZh }}</p>
                <div slot="reference" class="name-wrapper">
                  <el-tag size="medium">{{ scope.row.word }}</el-tag>
                </div>
              </el-popover>
            </template>
          </el-table-column>
          <el-table-column label="词性" prop="pos" width=min-width="10%">
          </el-table-column>
          <el-table-column label="释义" min-width="20%" prop="trans">
            <template slot-scope="scope">
              <el-tag
                  :type="'success'"
                  disable-transitions>{{ scope.row.trans }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="添加时间" min-width="20%" prop="wordInsertTime">
            <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span style="margin-left: 10px">{{ scope.row.wordInsertTime }}</span>
            </template>
          </el-table-column>
          <el-table-column label="相关操作" min-width="20%">
            <template slot-scope="scope">
              <el-button
                  icon="el-icon-postcard"
                  size="mini"
                  type="primary"
                  @click="showEditDialog(scope.row,scope.$index + (pageNum - 1) * pageSize + 1)"
              >修正
              </el-button>
              <el-button
                  type="danger"
                  icon="el-icon-delete"
                  @click="remove(scope.row.wordId)"
                  size="mini"
              >移除
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="flex_center_center">
        <lh-pagination
            v-show="total > 0"
            :limit.sync="pageSize"
            :page.sync="pageNum"
            :total="total"
            @pagination="getBookInfo"
        />
      </div>
    </div>

    <div>
      <el-dialog
          :center="true"
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
              :model="wordMessage"
              label-width="120px"
              style="margin: 0 7% 0 0"
          >
            <el-row :gutter="50">
              <el-col :span="12">
                <el-form-item aria-rowindex="50px" label="添加时间" prop="wordInsertTime">
                  <el-input v-model.trim="wordMessage.wordInsertTime" :disabled="true"></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="单词" prop="word">
                  <el-input v-model.trim="wordMessage.word"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row :gutter="50">
              <el-col :span="12">
                <el-form-item aria-rowindex="50px" label="词性" prop="pos">
                  <el-input v-model.trim="wordMessage.pos"></el-input>
                </el-form-item>
              </el-col>

              <el-col :span="12">
                <el-form-item label="释义" prop="trans">
                  <el-input v-model.trim="wordMessage.trans"></el-input>
                </el-form-item>
              </el-col>
            </el-row>

            <el-row>
              <el-form-item label="例句" prop="sentenceEn">
                <el-input v-model="wordMessage.sentenceEn" :rows="4" maxlength="200" show-word-limit
                          type="textarea"></el-input>
              </el-form-item>
            </el-row>

            <el-row>
              <el-form-item label="翻译" prop="sentenceZh">
                <el-input v-model="wordMessage.sentenceZh" :rows="4" maxlength="200" show-word-limit
                          type="textarea"></el-input>
              </el-form-item>
            </el-row>

            <el-form-item class="flex_center_center">
              <el-button type="primary" @click="updateBookOfWord">提交</el-button>
              <el-button type="info" @click="closed">关闭</el-button>
            </el-form-item>
          </el-form>
        </div>

      </el-dialog>


    </div>


  </div>
</template>

<script>
import {bookInfo, deleteBook, deleteWord, editBook, updateBookOfWord, wordListPdf} from '@/api/wordList'
import LhPagination from "@/components/lhPublic/lhPagination";
import dayjs from "dayjs";
import fileDownload from "js-file-download";

export default {
  data() {
    return {
      tableHeight: 0,
      clientWidth: document.body.clientWidth, // 文档宽度
      searchLoading: false,
      exportLoading: false,
      editFlag: false,
      wordMessage: {},
      visible: false,
      bookId: '',
      bookName: '',
      search: "",
      pageNum: 1,
      pageSize: 10,
      list: [],
      total: 0,
      title: ""
    }
  },
  components: { LhPagination },
  methods: {

    // 导出
    wordListPdf() {
      let dateTime = dayjs().format("YYYY-MM-DD HH:mm:ss");
      this.exportLoading = true;
      wordListPdf(this.bookId)
          .then(response => {
            fileDownload(response, this.bookName + "(" + dateTime + ").pdf")
          })
          .catch((err) => {
            this.$message.error("导出失败")
          })
          .finally(() => {
            this.exportLoading = false;
          })
    },

    /*查询*/
    getBookInfo() {
      const data = {
        bookId: this.bookId,
        pageNum: this.pageNum,
        pageSize: this.pageSize,
        search: this.search
      }
      this.searchLoading = true;
      bookInfo(data)
          .then((res) => {
            // console.log('res', res)
            if (res.code == 200) {
              this.bookName = res.data.book.bookName
              this.list = res.data.word.records
              this.total = res.data.word.total
            }
          })
          .catch((err) => {
            console.log('err', err)
          })
          .finally(() => {
            this.searchLoading = false;
          })
    },

    init() {
      this.$nextTick(() => {
      })
    },

    showEditDialog(data, wordIndex) {
      this.wordMessage = JSON.parse(JSON.stringify(data))
      this.title = "第" + wordIndex + "个单词"
      this.visible = true;
    },

    closed() {
      this.getBookInfo();
      this.wordMessage = {};
      this.title = ""
      this.visible = false
    },


    updateBookOfWord() {
      updateBookOfWord(this.wordMessage)
          .then((res) => {
            this.$notify.success({
              title: '成功',
              message: res.msg,
            });
          })
          .catch((err) => {
            this.$notify.error({
              title: '失败',
              message: err.msg,
            });
          })
          .finally(() => {
            this.closed();
          })

    },


    remove(wordId) {
      deleteWord(wordId)
          .then((res) => {
            console.log('res', res)
            if (res.code === 200) {
              this.getBookInfo()
              this.$notify.success({
                title: '成功',
                message: res.msg,
              });
            }
          })
          .catch((err) => {
            console.log('err', err)
          })
    },

    changeBookName() {
      this.editFlag = true
      // this.$refs.changeBookNameInput.focus();

    },


    edit(e) {
      this.editFlag = false
      const data = {
        bookId: this.bookId,
        bookName: e.target.value
      }
      if (data.bookName.length > 8) {
        this.$notify.warning({
          title: "警告",
          message: "单词本名称最多八个字符"
        })
        return
      }
      editBook(data)
          .then((res) => {
            if (res.code === 200) {
              this.getBookInfo()
              this.$notify.success({
                title: "成功",
                message: "修改成功！"
              })
            }
          })
          .catch((err) => {
            console.log('err', err)
          });
    },
    deleteBook() {
      deleteBook(this.bookId)
        .then((res) => {
          console.log('res', res)
          if (res.code == 200) {
            this.$router.back() //后退
            this.$message.success('删除成功！')
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },

    // 监听pagesize的改变
    handleSizeChange(newSize) {
      this.getBookInfo();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.pageNum = newPage;
      this.getBookInfo();
    },

  },
  created() {
    if (this.$route.params.bookId) {
      window.sessionStorage.setItem('bookId', this.$route.params.bookId)
      this.bookId = this.$route.params.bookId
      this.getBookInfo()
    } else {
      const bookId = window.sessionStorage.getItem('bookId')
      this.bookId = bookId
      this.getBookInfo()
    }
  }
}
</script>

<style scoped>

.noTableScrollBar /deep/ .el-table__body-wrapper::-webkit-scrollbar {
  width: 0;
  /*滚动条宽度*/
}


.title {
  padding: 1rem;
  border-radius: 10px;
  margin-bottom: 1rem;
}

.is-body {
  border-radius: 10px;
  overflow: hidden;
  padding: 1rem;
  width: 100%;
}

.container {
  width: 1200px;
  margin: 0 auto;
}

.title {
  border-radius: 10px;
}

.shadow {
  box-shadow: 0px 0px 10px 3px #30333cba;
}
</style>
