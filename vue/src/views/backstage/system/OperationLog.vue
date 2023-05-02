<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户</el-breadcrumb-item>
      <el-breadcrumb-item>管理员操作日志</el-breadcrumb-item>
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
          <el-form-item label="操作类型">
            <el-select
                v-model="queryInfo.actionType"
                clearable
                style="width: 125px">
              <el-option
                  v-for="status in actionTypeList"
                  :key="status.value"
                  :label="status.label"
                  :value="status.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="日志搜索">
            <el-input
                v-model="queryInfo.search"
                autocomplete="off"
                clearable
                placeholder="ID、持有人、结果"
                type="text"
                @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="actionLogSearch">
            查询
          </el-button>
          <el-button :icon="`el-icon-${this.exportLoading?'loading':'download'}`" type="success"
                     @click="logExcelImport">
            导出
          </el-button>
        </el-form>
      </div>

      <!-- 登录日志列表区 -->
      <div class="noTableScrollBar">
        <el-table
            v-loading="searchLoading"
            :data="logList"
            :header-cell-style="{'text-align':'center'}"
            :height="tableHeight === 0 ? 'calc(100vh - 301px)' : tableHeight"
            border
            highlight-current-row
            stripe style="margin: auto; width: 100%; text-align: center">
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
          <el-table-column align="center" label="操作时间" min-width="12%" prop="actionTime"></el-table-column>
          <el-table-column align="center" label="管理员ID" min-width="14%" prop="adminId"></el-table-column>
          <el-table-column align="center" label="操作人" min-width="10%" prop="keepName"></el-table-column>
          <el-table-column align="center" label="角色" min-width="7%" prop="role">
            <template v-slot="scope">
              <span>{{ roleList[scope.row.role] }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="操作类型" min-width="10%" prop="actionKind">
            <template v-slot="scope">
              <span>{{ actionTypeTran[scope.row.actionKind] }}</span>
            </template>
          </el-table-column>
          <el-table-column label="描述" min-width="41%" prop="remark" text-align="left"></el-table-column>
        </el-table>
      </div>

      <!-- 分页区 -->
      <div class="flex_center_center">
        <lh-pagination v-show="total > 0" :limit.sync="queryInfo.pageSize" :page.sync="queryInfo.pageNum"
                       :total="total" @pagination="actionLogSearch"/>
      </div>

    </el-card>


  </div>
</template>

<script>

import {actionLogExcelImport, actionLogSearch} from "@/api/admin.js"
import fileDownload from "js-file-download";
import dayjs from "dayjs";
import LhPagination from "@/components/lhPublic/lhPagination";

export default {
  data() {
    return {
      tableHeight: 0,
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
        search: "",
        /*登录方式*/
        actionType: "",
        // 当前页码
        pageNum: 1,
        // 每页显示条数
        pageSize: 10,
      },
      //登录方式列表
      actionTypeList: [
        {label: '插入', value: 'INSERT', key: 0},
        {label: '批量插入', value: 'INSERT_BATCH', key: 1},
        {label: '删除', value: 'DELETE', key: 2},
        {label: '批量删除', value: 'DELETE_BATCH', key: 3},
        {label: '修改', value: 'UPDATE', key: 4},
        {label: '导出', value: 'EXPORT', key: 5},
      ],
      actionTypeTran: {
        INSERT: '插入',
        INSERT_BATCH: '批量插入',
        DELETE: '删除',
        DELETE_BATCH: '批量删除',
        UPDATE: '修改',
        EXPORT: '导出'
      },
      roleList: ["超级管理员", "普通管理员"],
      // 用于保存获取到的用户列表
      logList: [],
      // 总数据条数
      total: 0,
      exportLoading: false,
      searchLoading: false,
    };
  },
  components: {LhPagination},
  created() {
    // 发送数据请求，获取用户列表数据
    this.actionLogSearch();

  },
  methods: {


    //导出
    logExcelImport() {
      this.queryInfo.beginTime = this.pickDate.beginDate
      this.queryInfo.endTime = this.pickDate.endDate
      let params = this.queryInfo;
      console.log(params)
      let dateTime = dayjs().format("YYYY-MM-DD HH:mm:ss");
      this.exportLoading = true
      actionLogExcelImport(params)
          .then(response => {
            fileDownload(response, "Word-管理员操作日志表(" + dateTime + ').xlsx')
          })
          .catch((err) => {
            this.$message.error("导出失败")
          })
          .finally(() => {
            this.exportLoading = false
          })
    },

    //查询
    actionLogSearch() {
      this.queryInfo.beginTime = this.pickDate.beginDate
      this.queryInfo.endTime = this.pickDate.endDate
      let params = this.queryInfo;
      this.searchLoading = true;
      actionLogSearch(params)
          .then(response => {
            this.logList = response.data.records;
            this.total = response.data.total
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
      this.actionLogSearch();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.actionLogSearch();
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
