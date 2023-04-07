<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
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
          <el-form-item label="积分排序">
            <el-select
                v-model="queryInfo.integrationOrderByAsc"
                clearable
                style="width: 120px">
              <el-option
                  v-for="status in integrationStatusList"
                  :key="status.value"
                  :label="status.name"
                  :value="status.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户状态">
            <el-select
                v-model="queryInfo.userStatus"
                clearable
                style="width: 100px">
              <el-option
                  v-for="item in userStatusList"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="用户搜索">
            <el-input
                v-model="queryInfo.accountOrTelOrNickNameOrUserId"
                autocomplete="off"
                clearable
                placeholder="ID、账号、电话、用户名"
                type="text"
                @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-button icon="el-icon-search" type="primary" @click="userSearch"
          >查询
          </el-button
          >
          <el-button icon="el-icon-download" type="success" @click="userListExcel"
          >导出
          </el-button
          >
        </el-form>
      </div>

      <!-- 用户列表区 -->
      <div class="noTableScrollBar">
        <el-table
            :cell-style="{'text-align':'center'}"
            :data="userList"
            :header-cell-style="{'text-align':'center'}"
            :height="tableHeight === 0 ? 'calc(100vh - 301px)' : tableHeight"
            border
            highlight-current-row stripe>
          <template slot="empty">
            <el-empty description="暂无数据"></el-empty>
          </template>
          <el-table-column label="序号" width="50">
            <template v-slot="scope">
          <span>{{
              scope.$index + (queryInfo.pageNum - 1) * queryInfo.pageSize + 1
            }}</span>
            </template>
          </el-table-column>
          <el-table-column label="账号" prop="account" width="150"></el-table-column>
          <el-table-column label="用户名" prop="nickName" width="150"></el-table-column>
          <el-table-column label="注册时间" prop="registerTime" width="200"></el-table-column>
          <el-table-column label="用户状态" prop="userStatus" width="100"></el-table-column>
          <el-table-column label="用户积分" prop="integration" width="100"></el-table-column>
          <el-table-column label="描述" prop="remark">
            <template v-slot="scope">
              <el-input
                  v-model="scope.row.remark"
                  autocomplete="off"
                  placeholder="请输入用户描述"
                  style="width: 70%"
                  type="text"
                  @input="() => $forceUpdate()"
              ></el-input>
              <el-button
                  slot="append"
                  plain
                  type="primary"
                  @click="updateRemark(scope.row.userId,scope.row.remark)"
              >提交
              </el-button>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="250">
            <template v-slot="scope">
              <!-- 查看按钮 -->
              <el-tooltip
                  :enterable="false"
                  content="查看用户详情"
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
              <!-- 重置密码按钮 -->
              <el-tooltip
                  :enterable="false"
                  content="重置密码"
                  effect="dark"
                  placement="top"
              >
                <el-button
                    icon="el-icon-setting"
                    size="mini"
                    type="warning"
                    @click="resetPwd(scope.row.userId)"
                ></el-button>
              </el-tooltip>
              <!-- 锁定 -->
              <el-tooltip
                  :enterable="false"
                  content="锁定/解锁用户"
                  effect="dark"
                  placement="top">
                <el-button
                    icon="el-icon-key"
                    size="mini"
                    type="primary"
                    @click="lockOrUnLockUser(scope.row.userId,scope.row.userStatus)"
                ></el-button>
              </el-tooltip>

              <!-- 删除按钮 -->
              <el-tooltip
                  :enterable="false"
                  content="删除用户"
                  effect="dark"
                  placement="top">
                <el-button
                    icon="el-icon-delete"
                    size="mini"
                    type="danger"
                    @click="deleteUser(scope.row.userId)"
                ></el-button>
              </el-tooltip>


            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页区 -->
      <div class="flex_center_center">
        <lh-pagination v-show="total > 0" :limit.sync="queryInfo.pageSize" :page.sync="queryInfo.pageNum"
                       :total="total" @pagination="userSearch"/>
      </div>

    </el-card>
    <UserInfo ref="UserInfo"></UserInfo>


  </div>
</template>

<script>

import {deleteUser, lockOrUnLockUser, resetPwd, updateRemark, userListExcel, userSearch} from "@/api/admin.js"
import UserInfo from "./UserInfo";
import fileDownload from "js-file-download";
import dayjs from "dayjs";
import LhPagination from "@/components/lhPublic/lhPagination";

export default {
  data() {
    return {
      tableHeight: 0,
      clientWidth: document.body.clientWidth, // 文档宽度
      //起始时间和截止时间的时间列表
      timeList: ['', ''],
      pickDate: {beginDate: "", endDate: ""},
      // 获取用户列表的参数对象
      queryInfo: {
        /*开始时间*/
        beginTime: "",
        /*结束时间*/
        endTime: "",
        /*账号、电话、用户名、用户id*/
        accountOrTelOrNickNameOrUserId: "",
        /*用户状态*/
        userStatus: "",
        /*积分升序或降序,*/
        integrationOrderByAsc: false,
        // 当前页码
        pageNum: 1,
        // 每页显示条数
        pageSize: 10,
      },
      //用户状态列表
      userStatusList: [
        // 用户状态，0正常，1锁定，2待删除
        {label: '正常', value: '0'},
        {label: '锁定', value: '1'},
        {label: '待删除', value: '2'}
      ],
      //积分状态列表
      integrationStatusList: [
        {value: true, name: "积分升序"},
        {value: false, name: "积分降序"}
      ],
      // 用于保存获取到的用户列表
      userList: [],
      // 总数据条数
      total: 0
    };
  },
  components: {LhPagination, UserInfo},
  created() {
    // 发送数据请求，获取用户列表数据
    this.userSearch();
    //默认选择条件为降序
    this.queryInfo.integrationOrderByAsc = this.integrationStatusList[1].value
  },
  methods: {
    //查询
    userSearch() {
      this.queryInfo.beginTime = this.pickDate.beginDate
      this.queryInfo.endTime = this.pickDate.endDate
      let params = this.queryInfo;
      userSearch(params)
          .then((res) => {
            console.log(res)
            this.userList = res.data.records
            this.total = res.data.total
          })
          .catch((err) => {
            console.log(err.msg)
            this.$message.error(err.msg)
          })
    },
    //修改描述
    updateRemark(userId, remark) {
      let params = {
        userId: userId,
        remark: remark
      }
      updateRemark(params)
          .then((res) => {
            this.$message.success(res.msg);
          })
          .catch((err) => {
            this.$message.error(err.msg);
          })
    },


    //锁定和解锁用户
    lockOrUnLockUser(userId, userStatus) {
      this.$confirm("请确定是否 锁定/解锁 该用户", {
        cancelButtonText: "取消",//取消按钮文字更换
        confirmButtonText: "确认",//确认按钮文字更换
        showClose: true,//是否显示右上角关闭按钮
        type: "warning",//提示类型 success/info/warning/error
      })
          .then(() => {
            let params = {
              userId: userId,
              lockType: "unLock"
            }
            if ("锁定" !== userStatus) {
              params.lockType = "lock"
            }
            lockOrUnLockUser(params)
                .then((res) => {
                  this.$message.success(res.msg)
                })
                .catch((err) => {
                  this.$message.success(err.msg)
                })
                .finally(() => {
                      this.userSearch()
                    }
                );
          })
          .catch((err) => {
//捕获异常
          });
    },

    // 导出
    userListExcel() {
      let dateTime = dayjs().format('YYYY-MM-DD');
      userListExcel()
          .then(response => {
            fileDownload(response, "Word-用户信息表(" + dateTime + ').xlsx')
          })
    },

    // 查看用户详情
    show(row) {
      this.$refs.UserInfo.showEditDialog(row)
    },

    //重置用户密码
    resetPwd(userId) {
      this.$confirm("是否 重置 该用户密码", {
        cancelButtonText: "取消",//取消按钮文字更换
        confirmButtonText: "确认",//确认按钮文字更换
        showClose: true,//是否显示右上角关闭按钮
        type: "warning",//提示类型 success/info/warning/error
      })
          .then(() => {
            resetPwd({userId})
                .then((res) => {
                  console.log('res', res)
                  this.$message.success(res.msg);
                })
                .catch((err) => {
                  this.$message.error(err.msg);
                })
          })
          .catch((err) => {
//捕获异常
          });
    },


    // 监听pagesize的改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      this.userSearch();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.userSearch();
    },

    // 根据id删除对应的用户信息
    deleteUser(userId) {
      this.$confirm("此操作会立刻 删除 该用户的一切信息，请谨慎操作!", {
        cancelButtonText: "取消",//取消按钮文字更换
        confirmButtonText: "确认",//确认按钮文字更换
        showClose: true,//是否显示右上角关闭按钮
        type: "warning",//提示类型 success/info/warning/error
      })
          .then(() => {
            console.log('确认')
            deleteUser(userId)
                .then((res) => {
                  this.$message.success(res.msg)
                })
                .catch((err) => {
                  this.$message.error(err.msg)
                })
                .finally(() => {
                  this.userSearch()
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
