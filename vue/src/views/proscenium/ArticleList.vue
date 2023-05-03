<template>
  <div>
    <!--<div>-->
    <!--  <span>励志语</span>-->
    <!--</div>-->
    <el-card style="min-height: 100%">
      <!-- 搜索与筛选区域 -->
      <div>
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
              placeholder="选择日期时间"
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
              placeholder="选择日期时间"
              style="width: 200px"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
            >
            </el-date-picker>
          </el-form-item>
          <el-form-item label="字数上限">
            <el-input
              v-model.number="queryInfo.countUp"
              autocomplete="off"
              clearable
              placeholder="请输入"
              style="width: 160px"
              type="number"
              @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-form-item label="字数下限">
            <el-input
              v-model.number="queryInfo.countLow"
              autocomplete="off"
              clearable
              placeholder="请输入"
              style="width: 160px"
              type="number"
              @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-form-item label="搜索">
            <el-input
              v-model.trim="queryInfo.search"
              autocomplete="off"
              clearable
              placeholder="标题、作者"
              style="width: 255px"
              type="text"
              @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-button
            icon="el-icon-search"
            type="primary"
            @click="articleSearch"
          >
            查询
          </el-button>
        </el-form>
      </div>

      <!-- 文章列表区 -->
      <div class="noTableScrollBar">
        <el-table
            v-loading="searchLoading"
            :data="articleList"

            :header-cell-style="{ 'text-align': 'center' }"
            :height="tableHeight === 0 ? `calc(${radio}*100vh - 301px)` : tableHeight"
            border
            highlight-current-row
            stripe
            style="margin: auto; width: 100%; text-align: center"
        >
          <template slot="empty">
            <el-empty description="暂无数据"></el-empty>
          </template>
          <el-table-column align="center" label="序号" min-width="5%">
            <template v-slot="scope">
              <span>{{
                scope.$index + (queryInfo.pageNum - 1) * queryInfo.pageSize + 1
              }}</span>
            </template>
          </el-table-column>
          <el-table-column
              align="center"
              label="标题"
              min-width="18%"
              prop="articleTitle"
          ></el-table-column>
          <el-table-column
            align="center"
            label="作者"
            min-width="11%"
            prop="articleAuthor"
          ></el-table-column>
          <el-table-column
            align="center"
            label="字数"
            min-width="8%"
            prop="wordNumber"
          ></el-table-column>
          <el-table-column
              align="center"
              label="练习次数"
              min-width="8%"
              prop="articleStudyNumber"
          ></el-table-column>
          <el-table-column
            align="center"
            label="添加时间"
            min-width="15%"
            prop="articleCreateTime"
          >
            <template v-slot="scope">
              <span>{{
                dayjs(parseInt(scope.row.articleCreateTime)).format(
                  "YYYY-MM-DD HH:mm:ss"
                )
              }}</span>
            </template>
          </el-table-column>

          <el-table-column
              label="内容描述"
              min-width="25%"
              prop="remark"
              show-overflow-tooltip
              text-align="left"
          ></el-table-column>
          <el-table-column align="center" label="Action" min-width="10%">
            <template v-slot="scope">
              <el-button icon="el-icon-postcard" size="medium" type="danger"
                ><router-link
                  :to="{
                    name: 'article',
                    params: { articleId: scope.row.articleId },
                  }"
                  >练习吧</router-link
                >
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页区 -->
      <div class="flex_center_center">
        <lh-pagination
          v-show="total > 0"
          :limit.sync="queryInfo.pageSize"
          :page.sync="queryInfo.pageNum"
          :total="total"
          @pagination="articleSearch"
        />
      </div>
    </el-card>
  </div>
</template>

<script>

import {articleSearch} from "@/api/admin.js"
import dayjs from "dayjs";
import LhPagination from "@/components/lhPublic/lhPagination";

export default {
  data() {
    return {
      dayjs: dayjs,
      tableHeight: 0,
      clientWidth: document.body.clientWidth, // 文档宽度
      radio: window.sessionStorage.getItem("ratio"),
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
  components: { LhPagination },
  created() {
    // 发送数据请求，获取用户列表数据
    this.articleSearch();
  },
  methods: {
    //查询
    articleSearch() {
      this.queryInfo.beginTime = dayjs(this.pickDate.beginDate).valueOf()
      this.queryInfo.endTime = dayjs(this.pickDate.endDate).valueOf()
      let params = this.queryInfo;
      this.searchLoading = true;
      console.log(params)
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

    // 监听pagesize的改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      this.articleSearch();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.articleSearch();
    },

  }
};
</script>

<style scoped>
.noTableScrollBar /deep/ .el-table__body-wrapper::-webkit-scrollbar {
  width: 0;
  /*滚动条宽度*/
}
</style>
