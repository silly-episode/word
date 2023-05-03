<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>学习资源</el-breadcrumb-item>
      <el-breadcrumb-item>文章管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="min-height: 100%">
      <!-- 搜索与筛选区域 -->
      <div>
        <el-form ref="queryParams" :inline="true" :model="pickDate">
          <el-form-item label="开始时间">
            <el-date-picker
                v-model="pickDate.beginDate"
                :picker-options="{
              disabledDate: (time) => {
                let _this=this
                if (_this.pickDate.endDate) {
                   let edtTime = _this.pickDate.endDate.replace(/-/g, '/');
                   return time.getTime() > new Date(edtTime);
                  }
          }, firstDayOfWeek: 1}"
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
                let _this=this
                if (_this.pickDate.beginDate) {
                let startTime = _this.pickDate.beginDate.replace(/-/g, '/');
                return time.getTime() < new Date(startTime);
                }
          }, firstDayOfWeek: 1}"
                placeholder="选择日期"
                style="width: 200px"
                type="datetime"
                value-format="yyyy-MM-dd HH:mm:ss"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="字数上限">
            <el-input
                v-model.trim.number="queryInfo.countUp"
                autocomplete="off"
                clearable
                placeholder="请输入"
                style="width: 90px"
                type="text"
                @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-form-item label="字数下限">
            <el-input
                v-model.trim.number="queryInfo.countLow"
                autocomplete="off"
                clearable
                placeholder="请输入"
                style="width: 90px"
                type="text"
                @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-form-item label="搜索">
            <el-input
                v-model="queryInfo.search"
                autocomplete="off"
                clearable
                placeholder="ID、标题、作者"
                type="text"
                @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="articleSearch(true)">
            查询
          </el-button>
          <el-button icon="el-icon-plus" type="warning" @click="show('article')">
            添加文章
          </el-button>
        </el-form>
      </div>

      <!-- 文章列表区 -->
      <div class="noTableScrollBar">
        <el-table
            v-loading="searchLoading"
            :data="articleList"
            :header-cell-style="{'text-align':'center'}"
            :height="tableHeight === 0 ? `calc(10/9.0*${radio}*100vh - 301px)` : tableHeight"
            border
            highlight-current-row
            stripe
            style="margin: auto; width: 100%; text-align: center">
          <template slot="empty">
            <el-empty description="暂无数据"></el-empty>
          </template>
          <el-table-column align="center" label="序号" min-width="4%">
            <template v-slot="scope">
          <span>{{
              scope.$index + (queryInfo.pageNum - 1) * queryInfo.pageSize + 1
            }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="ID" min-width="12%" prop="articleId"></el-table-column>
          <el-table-column align="center" label="标题" min-width="12%" prop="articleTitle"></el-table-column>
          <el-table-column align="center" label="作者" min-width="11%" prop="articleAuthor"></el-table-column>
          <el-table-column align="center" label="字数" min-width="5%" prop="wordNumber"></el-table-column>
          <el-table-column align="center" label="练习次数" min-width="6%" prop="articleStudyNumber"></el-table-column>
          <el-table-column align="center" label="添加时间" min-width="11%" prop="articleCreateTime">
            <template v-slot="scope">
              <span>{{
                  dayjs(parseInt(scope.row.articleCreateTime)).format('YYYY-MM-DD HH:mm:ss')
                }}</span>
            </template>
          </el-table-column>

          <el-table-column label="描述" min-width="28%" prop="remark" show-overflow-tooltip
                           text-align="left"></el-table-column>
          <el-table-column align="center" label="操作" min-width="9%">
            <template v-slot="scope">
              <!-- 保存按钮 -->
              <el-tooltip
                  :enterable="false"
                  content="修改"
                  effect="dark"
                  placement="top"
              >
                <el-button
                    icon="el-icon-postcard"
                    size="mini"
                    type="primary"
                    @click="show(scope.row)"
                ></el-button>
              </el-tooltip>
              <!-- 删除按钮 -->
              <el-tooltip
                  :enterable="false"
                  content="删除"
                  effect="dark"
                  placement="top">
                <el-button
                    icon="el-icon-delete"
                    size="mini"
                    type="danger"
                    @click="deleteArticle(scope.row.articleId)"
                ></el-button>
              </el-tooltip>


            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页区 -->
      <div class="flex_center_center">
        <lh-pagination v-show="total > 0" :limit.sync="queryInfo.pageSize" :page.sync="queryInfo.pageNum"
                       :total="total" @pagination="articleSearch(true)"/>
      </div>

    </el-card>
    <ArticleInfo ref="ArticleInfo" @searchAgain="articleSearch"></ArticleInfo>


  </div>
</template>

<script>

import {articleSearch, deleteArticle} from "@/api/admin.js"
import ArticleInfo from "./ArticleInfo";
import fileDownload from "js-file-download";
import dayjs from "dayjs";
import LhPagination from "@/components/lhPublic/lhPagination";

export default {
  data() {
    return {
      dayjs: dayjs,
      tableHeight: 0,
      radio: window.sessionStorage.getItem("ratio"),
      clientWidth: document.body.clientWidth, // 文档宽度
      //起始时间和截止时间的时间列表
      pickDate: {beginDate: "", endDate: ""},
      // 获取用户列表的参数对象
      queryInfo: {
        /*开始时间*/
        beginTime: "",
        /*结束时间*/
        endTime: "",
        /*账号、电话、用户名、用户id*/
        countUp: "",
        /*用户状态*/
        countLow: "",
        /*搜索*/
        search: "",
        // 当前页码
        pageNum: 1,
        // 每页显示条数
        pageSize: 10,
      },
      // 用于保存获取到的用户列表
      articleList: [],
      // 总数据条数
      total: 0,
      exportLoading: false,
      searchLoading: false,
    };
  },
  components: {LhPagination, ArticleInfo},
  created() {
    // 发送数据请求，获取用户列表数据
    this.articleSearch(true);
  },
  methods: {
    // 查看管理员详情
    show(row) {
      this.$refs.ArticleInfo.showEditDialog(row);
    },

    delay(ms) {
      return new Promise(resolve => setTimeout(resolve, ms));
    },


    //查询
    async articleSearch(flag) {
      /*延时查询等待ES数据处理*/
      if (!flag) {
        await this.delay(500); // 等待0.5秒
      }

      this.queryInfo.beginTime = dayjs(this.pickDate.beginDate).valueOf();
      this.queryInfo.endTime = dayjs(this.pickDate.endDate).valueOf()
      let params = this.queryInfo;
      this.searchLoading = true;
      // console.log(params)
      articleSearch(params)
          .then((res) => {
            console.log(res)
            this.articleList = res.data.records
            this.total = res.data.total
          })
          .catch((err) => {
            console.log(err.msg)
            this.$message.error(err.msg)
          })
          .finally(() => {
            this.searchLoading = false;
          })
    },


    // 导出
    articleListExcel() {
      let dateTime = dayjs().format('YYYY-MM-DD');
      this.exportLoading = true;
      articleListExcel(this.queryInfo)
          .then(response => {
            fileDownload(response, "Word-用户信息表(" + dateTime + ').xlsx')
          })
          .catch((err) => {
            this.$message.error("导出失败")
          })
          .finally(() => {
            this.exportLoading = false;
          })
    },


    // 监听pagesize的改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      this.articleSearch(true);
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.articleSearch(true);
    },

    // 根据id删除对应的用户信息
    deleteArticle(articleId) {
      this.$confirm("此操作会立刻 删除 该该文章的一切信息，请谨慎操作!", {
        cancelButtonText: "取消",//取消按钮文字更换
        confirmButtonText: "确认",//确认按钮文字更换
        showClose: true,//是否显示右上角关闭按钮
        type: "warning",//提示类型 success/info/warning/error
      })
          .then(() => {
            console.log('确认')
            deleteArticle(articleId)
                .then((res) => {
                  this.$message.success(res.msg)
                })
                .catch((err) => {
                  this.$message.error(err.msg)
                })
                .finally(() => {
                  this.articleSearch()
                })
          })
          .catch((err) => {
//捕获异常
          });
    }
  }
};
</script>

<style scoped>


.noTableScrollBar /deep/ .el-table__body-wrapper::-webkit-scrollbar {
  width: 0;
  /*滚动条宽度*/
}


</style>
