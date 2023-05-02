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

      <div>
        <el-button icon="el-icon-s-promotion" type="success">
          <router-link
              :to="{
            name: 'word',
            params: { bookId, pageNum, pageSize },
          }"
          >开始练习
          </router-link
          >
        </el-button>
        <el-button icon="el-icon-edit" type="primary" @click="changeBookName">重命名</el-button>
        <el-button icon="el-icon-delete-solid" type="danger" @click="deleteBook">删除单词本</el-button>
        <el-button
            :icon="`el-icon-${exportLoading?'loading':'download'}`"
            type="warning"
            @click="wordListPdf">
          导出
        </el-button>
      </div>
    </div>

    <!-- 表体   -->
    <div class="shadow isbody padding_30">
      <div class="noTableScrollBar">
        <el-table
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
              type="index"
              label="序号"
              min-width="10%"
          ></el-table-column>
          <el-table-column label="单词" prop="word">
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
          <el-table-column prop="pos" label="词性" width="50px">
          </el-table-column>
          <el-table-column label="释义" prop="trans">
            <template slot-scope="scope">
              <el-tag
                  :type="'success'"
                  disable-transitions>{{ scope.row.trans }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column label="添加时间" prop="wordInsertTime">
            <template slot-scope="scope">
              <i class="el-icon-time"></i>
              <span style="margin-left: 10px">{{ scope.row.wordInsertTime }}</span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="80px">
            <template slot-scope="scope">
              <el-button
                  type="danger"
                  icon="el-icon-delete"
                  @click="remove(scope.row.wordId)"
                  size="mini"
              ></el-button>
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
  </div>
</template>

<script>
import {bookInfo, deleteBook, deleteWord, editBook, wordListPdf} from '@/api/wordList'
import LhPagination from "@/components/lhPublic/lhPagination";
import dayjs from "dayjs";
import fileDownload from "js-file-download";

export default {
  data() {
    return {
      tableHeight: 0,
      clientWidth: document.body.clientWidth, // 文档宽度
      exportLoading: false,
      editFlag: false,
      bookId: '',
      bookName: '',
      pageNum: 1,
      pageSize: 10,
      list: [],
      total: 0
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


    getBookInfo() {
      const data = {
        bookId: this.bookId,
        pageNum: this.pageNum,
        pageSize: this.pageSize
      }
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
    },
    remove(wordId) {
      deleteWord(wordId)
        .then((res) => {
          console.log('res', res)
          if (res.code == 200) {
            this.getBookInfo()
            this.$message.success('删除成功！')
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
      editBook(data)
          .then((res) => {
            if (res.code === 200) {
              this.getBookInfo()
              this.$message.success('修改成功！')
            }
        })
        .catch((err) => {
          console.log('err', err)
        })
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
