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
      <el-row :gutter="30">
        <el-col :span="8.5">
          <el-date-picker
              v-model="timeList"
              end-placeholder="结束日期"
              range-separator="至"
              start-placeholder="开始日期"
              type="datetimerange"
              @change="userSearch">
          </el-date-picker>
        </el-col>
        <el-col :span="4">
          <el-select v-model="queryInfo.userStatus" placeholder="请选择用户状态" @change="userSearch">
            <el-option
                v-for="item in userStatusList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="3">
          <el-select v-model="queryInfo.integrationOrderByAsc" placeholder="请选择积分排序" @change="userSearch">
            <el-option
                v-for="status in integrationStatusList"
                :key="status.value"
                :label="status.name"
                :value="status.value"
            />
          </el-select>
        </el-col>
        <el-col :span="6">
          <el-input
              v-model="queryInfo.accountOrTelOrNickNameOrUserId"
              clearable
              placeholder="请输入内容"
              @clear="userSearch"
          >
            <el-button
                slot="append"
                icon="el-icon-search"
                @click="userSearch"
            ></el-button>
          </el-input>
        </el-col>
        <el-col :span="2">
          <el-button type="primary" @click="userListExcel">导出</el-button>
        </el-col>
      </el-row>
      <!-- 用户列表区 -->
      <el-table
          :cell-style="{'text-align':'center'}"
          :data="userList"
          :header-cell-style="{'text-align':'center'}"
          border
          highlight-current-row stripe>
        <el-table-column label="序号" type="index" width="50"></el-table-column>
        <el-table-column label="账号" prop="account" width="150"></el-table-column>
        <el-table-column label="用户名" prop="nickName" width="150"></el-table-column>
        <el-table-column label="注册时间" prop="registerTime" width="200"></el-table-column>
        <el-table-column label="用户状态" prop="userStatus" width="100"></el-table-column>
        <el-table-column label="用户积分" prop="integration" width="100"></el-table-column>
        <el-table-column label="描述" prop="remark"></el-table-column>
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
                  @click="removeUserById(scope.row.userId)"
              ></el-button>
            </el-tooltip>


          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区 -->
      <div class="flex_center_center">
        <el-pagination
            :current-page="queryInfo.pageNum"
            :page-size="queryInfo.pageSize"
            :page-sizes="[10, 30, 50]"
            :total="total"
            layout="total, sizes, prev, pager, next, jumper"
            @size-change="handleSizeChange"
            @current-change="handleCurrentChange"
        >
        </el-pagination>
      </div>

    </el-card>
    <UserInfo ref="UserInfo"></UserInfo>
  </div>
</template>

<script>

import {lockOrUnLockUser, resetPwd, userListExcel, userSearch} from "@/api/admin.js"
import UserInfo from "./UserInfo";
import fileDownload from "js-file-download";
import dayjs from "dayjs";

export default {
  data() {
    return {
      //起始时间和截止时间的时间列表
      timeList: ['', ''],
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
        // 用户状态，0正常，1锁定，2删除
        {label: '正常', value: '0'},
        {label: '锁定', value: '1'},
        {label: '删除', value: '2'}
      ],
      //积分状态列表
      integrationStatusList: [
        {value: true, name: "升序"},
        {value: false, name: "降序"}
      ],
      // 用于保存获取到的用户列表
      userList: [],
      // 总数据条数
      total: 0
    };
  },
  components: {UserInfo},
  created() {
    // 发送数据请求，获取用户列表数据
    this.userSearch();
    //默认选择条件为降序
    this.queryInfo.integrationOrderByAsc = this.integrationStatusList[1].value
  },
  methods: {
    //查询
    userSearch() {
      let params = this.queryInfo;
      if (this.timeList && this.timeList[0] && this.timeList[1]) {
        params.beginTime = this.turnDateString(this.timeList[0]).hasTime
        params.endTime = this.turnDateString(this.timeList[1]).hasTime
      } else {
        params.beginTime = "";
        params.endTime = "";
      }
      console.log(params)
      userSearch(params)
          .then((res) => {
            console.log(res)
            this.userList = res.data.records
            this.total = res.data.total
          })
          .catch((err) => {
            console.log(err.message)
          })

    },

    //转标准时间
    turnDateString(msec) {
      let datetime = new Date(msec);
      let year = datetime.getFullYear();
      let month = datetime.getMonth();
      let date = datetime.getDate();
      let hour = datetime.getHours();
      let minute = datetime.getMinutes();
      let second = datetime.getSeconds();

      let result1 = year +
          '-' +
          ((month + 1) >= 10 ? (month + 1) : '0' + (month + 1)) +
          '-' +
          ((date + 1) < 10 ? '0' + date : date) +
          ' ' +
          ((hour + 1) < 10 ? '0' + hour : hour) +
          ':' +
          ((minute + 1) < 10 ? '0' + minute : minute) +
          ':' +
          ((second + 1) < 10 ? '0' + second : second);

      let result2 = year +
          '-' +
          ((month + 1) >= 10 ? (month + 1) : '0' + (month + 1)) +
          '-' +
          ((date + 1) < 10 ? '0' + date : date);

      return {
        hasTime: result1,
        withoutTime: result2
      };

    },

    //锁定和解锁用户
    lockOrUnLockUser(userId, userStatus) {
      let params = {
        userId: userId,
        type: ""
      }
      if ("锁定" === userStatus) {
        params.type = "unLock"
      } else {
        params.type = "lock"
      }
      lockOrUnLockUser(params)
          .then((res) => {
            this.$message.success(res.data)
          })
          .catch((err) => {
            this.$message.success(err.data)
          })
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
      // console.log((row))
    },

    //重置用户密码
    resetPwd(userId) {
      resetPwd({userId})
          .then((res) => {
            console.log('res', res)
            this.$message.success(res.data);
          })
          .catch((err) => {
            this.$message.error(err.data);
          })
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
    removeUserById(id) {
      this.$confirm("是否确认删除", {
        confirmButtonText: "确认",//确认按钮文字更换
        cancelButtonText: "取消",//取消按钮文字更换
        showClose: true,//是否显示右上角关闭按钮
        type: "warning",//提示类型 success/info/warning/error
      })
          .then(() => {
            console.log('确认')
          })
          .catch((err) => {
//捕获异常
          });
    },
  },
};
</script>

<style scoped>
</style>
