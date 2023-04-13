<template>
  <el-card>
    <el-form ref="queryParams" :inline="true" :model="pickDate">
      <el-form-item label="开始时间">
        <el-date-picker
          v-model="pickDate.beginDate"
          :picker-options="{
            disabledDate: (time) => {
              let _this = this;
              if (_this.pickDate.endDate) {
                let edtTime = _this.pickDate.endDate.replace(/-/g, '/');
                return time.getTime() > new Date(edtTime);
              }
            },
            firstDayOfWeek: 1,
          }"
          placeholder="选择日期"
          style="width: 200px"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间">
        <el-date-picker
          v-model="pickDate.endDate"
          :picker-options="{
            disabledDate: (time) => {
              let _this = this;
              if (_this.pickDate.beginDate) {
                let startTime = _this.pickDate.beginDate.replace(/-/g, '/');
                return time.getTime() < new Date(startTime);
              }
            },
            firstDayOfWeek: 1,
          }"
          placeholder="选择日期"
          style="width: 200px"
          type="datetime"
          value-format="yyyy-MM-dd HH:mm:ss"
        >
        </el-date-picker>
      </el-form-item>
      <el-form-item label="搜索关键字">
        <el-input style="width: 150px" v-model="queryInfo.search"></el-input>
      </el-form-item>
      <el-form-item label="字数下限">
        <el-input
          style="width: 90px"
          v-model="queryInfo.countLow"
          type="number"
        ></el-input>
      </el-form-item>
      <el-form-item label="字数上限">
        <el-input
          style="width: 100px"
          v-model="queryInfo.countUp"
          type="number"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button icon="el-icon-search" type="primary" @click="getArticleList">
          查询
        </el-button>
      </el-form-item>
    </el-form>
    <!-- 用户列表区 -->
    <el-table
      :data="articleList"
      border
      stripe
      :header-cell-style="{ 'text-align': 'center' }"
      :cell-style="{ 'text-align': 'center' }"
    >
      <el-table-column type="index" min-width="10%"></el-table-column>
      <el-table-column
        v-for="item in column"
        :key="item.label"
        :label="item.label"
        :prop="item.prop"
      ></el-table-column>
      <el-table-column label="操作" min-width="12%">
        <template slot-scope="scope">
          <el-button
            type="primary"
            icon="el-icon-key"
            @click="removeUserById('unLock', scope.row.openId)"
            size="mini"
          ></el-button>
        </template>
      </el-table-column>
    </el-table>
    <!-- 分页区 -->
    <el-pagination
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
      :current-page="queryInfo.pageNum"
      :page-sizes="[1, 2, 5, 10]"
      :page-size="queryInfo.pageSize"
      layout="total, sizes, prev, pager, next, jumper"
      :total="total"
    >
    </el-pagination>
  </el-card>
</template>

<script>

import { articleSearch } from '@/api/article.js'
import dayjs from "dayjs";
export default {
  data() {
    return {
      dayjs: dayjs,
      articleList: [],
      queryInfo: {
        beginTime: '',
        endTime: '',
        countUp: 100000,
        countLow: 0,
        search: "",
        pageNum: 1,
        pageSize: 5,
      },
      pickDate: { beginDate: "", endDate: "" },
      column: [
        {
          label: '标题',
          prop: 'articleTitle'
        }, {
          label: '作者',
          prop: 'articleAuthor'
        }, {
          label: '文章字数',
          prop: 'wordNumber'
        }, {
          label: '练习人数',
          prop: 'articleStudyNumber'
        }, {
          label: '内容',
          prop: 'content'
        }
      ]
    }

  },
  methods: {
    getArticleList() {
      this.queryInfo.beginTime = dayjs(this.pickDate.beginDate).valueOf()
      this.queryInfo.endTime = dayjs(this.pickDate.endDate).valueOf()
      console.log(this.queryInfo)
      articleSearch(this.queryInfo)
        .then((res) => {
          console.log('res', res)
          if (res.code == 200) {
            this.articleList = res.data.records
            this.total = res.data.total
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },



    handleSizeChange(newSize) {// 监听pagesize的改变
      this.queryInfo.pageSize = newSize;
      this.getArticleList();
    },

    handleCurrentChange(newPage) {// 监听页码值改变
      this.queryInfo.pageNum = newPage;
      this.getArticleList();
    },
  },
  created() {
    this.getArticleList()
  }
}
</script>

<style>
</style>