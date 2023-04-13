<template>
  <div class="container">
    <div class="title flex_between_center border_ccc">
      <input
        @blur="edit"
        :disabled="!editFlag"
        class="font_22 font_bold"
        type="text"
        :value="bookName"
      />
      <!-- <span class="font_22 font_bold">{{ bookName }}</span> -->
      <el-button type="success">
        <router-link
          :to="{
            name: 'word',
            params: { bookId },
          }"
          >开始练习</router-link
        >
      </el-button>
      <div>
        <el-button type="primary" @click="editFlag = true"
          >修改单词本</el-button
        >
        <el-button type="danger" @click="deleteBook">删除该单词本</el-button>
        <el-button type="danger">导出</el-button>
      </div>
    </div>

    <div class="shadow isbody padding_30">
      <el-table
        :data="list"
        style="width: 100%"
        :header-cell-style="{ 'text-align': 'center' }"
        :cell-style="{ 'text-align': 'center' }"
      >
        <el-table-column
          type="index"
          label="序号"
          min-width="10%"
        ></el-table-column>
        <el-table-column prop="word" label="单词"> </el-table-column>
        <el-table-column prop="ukphone" label="词性" width="50px">
        </el-table-column>
        <el-table-column prop="meaning" label="释义"> </el-table-column>
        <el-table-column prop="wordInsertTime" label="加入时间">
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
      <el-pagination
        align="center"
        @current-change="handleCurrentChange"
        :current-page="pageNum"
        :page-sizes="[1, 2, 5, 10]"
        :page-size="pageSize"
        layout="prev, pager, next"
        :total="total"
      >
      </el-pagination>
    </div>
  </div>
</template>

<script>
import { bookInfo, deleteWord, deleteBook, editBook } from '@/api/wordList'
export default {
  data() {
    return {
      editFlag: false,
      bookId: '',
      bookName: '',
      pageNum: 1,
      pageSize: 10,
      list: [],
      total: 0
    }
  },

  methods: {
    // .then((res) => {
    //   console.log('res', res)
    //   if (res.code == 200) { }
    // })
    // .catch((err) => {
    //   console.log('err', err)
    // })
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
    edit(e) {
      this.editFlag = false
      const data = {
        bookId: this.bookId,
        bookName: e.target.value
      }
      editBook(data)
        .then((res) => {
          // console.log('res', res)
          if (res.code == 200) {
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

    handleCurrentChange(newPage) {// 监听页码值改变
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

<style>
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
  width: 960px;
  margin: 0 auto;
}

.title {
  border-radius: 10px;
}

.shadow {
  box-shadow: 0px 0px 10px 3px #30333cba;
}
</style>