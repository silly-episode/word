<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户</el-breadcrumb-item>
      <el-breadcrumb-item>用户登录日志</el-breadcrumb-item>
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
          <el-form-item label="登录方式">
            <el-select
                v-model="queryInfo.loginType"
                clearable
                style="width: 125px">
              <el-option
                  v-for="status in loginTypeList"
                  :key="status.value"
                  :label="status.label"
                  :value="status.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="登录结果">
            <el-select
                v-model="queryInfo.result"
                clearable
                style="width: 130px">
              <el-option
                  v-for="item in resultList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="搜索">
            <el-input
                v-model="queryInfo.accountOrTelOrNickNameOrUserId"
                autocomplete="off"
                clearable
                placeholder="ID、账号、电话、用户名"
                type="text"
                @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="commonUserLog">
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
            :cell-style="{'text-align':'center'}"
            :data="logList"
            :header-cell-style="{'text-align':'center'}"
            :height="tableHeight === 0 ? 'calc(100vh - 301px)' : tableHeight"
            border
            style="margin: auto; width: 100%; text-align: center"
            highlight-current-row stripe>
          <template slot="empty">
            <el-empty description="暂无数据"></el-empty>
          </template>
          <el-table-column label="序号" min-width="4%">
            <template v-slot="scope">
          <span>{{
              scope.$index + (queryInfo.pageNum - 1) * queryInfo.pageSize + 1
            }}</span>
            </template>
          </el-table-column>
          <el-table-column label="登录时间" min-width="10%" prop="loginTime"></el-table-column>
          <el-table-column label="ID" min-width="12%" prop="userId"></el-table-column>
          <el-table-column label="账号" min-width="10%" prop="account"></el-table-column>
          <el-table-column label="昵称" min-width="10%" prop="nickName"></el-table-column>
          <el-table-column label="电话" min-width="7%" prop="tel"></el-table-column>
          <el-table-column label="状态" min-width="4%" prop="userStatus"></el-table-column>
          <el-table-column label="方式" min-width="6%" prop="loginType"></el-table-column>
          <el-table-column label="IP" min-width="8%" prop="ip"></el-table-column>
          <el-table-column label="结果" min-width="11%" prop="logRemark"></el-table-column>
        </el-table>
      </div>

      <!-- 分页区 -->
      <div class="flex_center_center">
        <lh-pagination v-show="total > 0" :limit.sync="queryInfo.pageSize" :page.sync="queryInfo.pageNum"
                       :total="total" @pagination="commonUserLog"/>
      </div>

    </el-card>
    <UserInfo ref="UserInfo"></UserInfo>


  </div>
</template>

<script>

import {commonUserLog, logExcelImport} from "@/api/admin.js"
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
        accountOrTelOrNickNameOrUserId: "",
        /*登录方式*/
        loginType: "",
        /*登录结果*/
        result: "",
        // 当前页码
        pageNum: 1,
        // 每页显示条数
        pageSize: 10,
      },
      //登录方式列表
      loginTypeList: [
        // 用户状态，0正常，1锁定，2待删除
        {label: '账号登录', value: 'pwd'},
        {label: '验证码登录', value: 'sms'},
      ],
      //登录结果列表
      resultList: [
        {label: '登录成功', value: '0'},
        {label: '账户不存在', value: '1'},
        {label: '验证码不存在', value: '2'},
        {label: '验证码错误', value: '3'},
        {label: '密码错误', value: '4'},
        {label: '账户锁定', value: '5'},
      ],
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
    this.commonUserLog();
    // //默认选择条件为降序
    // this.queryInfo.integrationOrderByAsc = this.resultList[1].value
  },
  methods: {


    //导出
    logExcelImport() {
      this.queryInfo.beginTime = this.pickDate.beginDate
      this.queryInfo.endTime = this.pickDate.endDate
      let params = this.queryInfo;
      console.log(params)
      let dateTime = dayjs().format('YYYY-MM-DD');
      this.exportLoading = true
      logExcelImport(params)
          .then(response => {
            fileDownload(response, "Word-用户登录日志表(" + dateTime + ').xlsx')
          })
          .catch((err) => {
            this.$message.error("导出失败")
          })
          .finally(() => {
            this.exportLoading = false
          })
    },

    //查询
    commonUserLog() {
      this.queryInfo.beginTime = this.pickDate.beginDate
      this.queryInfo.endTime = this.pickDate.endDate
      let params = this.queryInfo;
      this.searchLoading = true;
      commonUserLog(params)
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
      this.commonUserLog();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.commonUserLog();
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
