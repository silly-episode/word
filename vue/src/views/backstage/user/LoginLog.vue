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
      <el-row :gutter="24">
        <el-col :span="8">
          <el-date-picker
              v-model="timeList"
              end-placeholder="结束日期"
              range-separator="至"
              start-placeholder="开始日期"
              type="datetimerange"
              @change="userSearch">
          </el-date-picker>
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
        <el-col :span="4">
          <el-select v-model="queryInfo.integrationOrderByAsc" placeholder="请选择积分排序" @change="userSearch">
            <el-option label="升序" value="true"></el-option>
            <el-option label="降序" value="false"></el-option>
          </el-select>
        </el-col>
        <el-col :span="2">
          <el-button @click="leadingOut">导出</el-button>
        </el-col>
      </el-row>
      <!-- 用户列表区 -->
      <el-table :cell-style="{'text-align':'center'}" :data="userlist" :header-cell-style="{'text-align':'center'}"
                border stripe>
        <el-table-column label="序号" type="index" width="50px"></el-table-column>
        <el-table-column label="账号" prop="account"></el-table-column>
        <el-table-column label="账号" prop="account"></el-table-column>
        <el-table-column label="用户名" prop="nickName"></el-table-column>
        <el-table-column label="注册时间" prop="registerTime"></el-table-column>
        <el-table-column label="用户状态" prop="userStatus"></el-table-column>
        <el-table-column label="描述" prop="remark"></el-table-column>
        <el-table-column label="操作" width="250px">
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
            <el-button
                icon="el-icon-key"
                size="mini"
                type="primary"
                @click="show(scope.row)"
            ></el-button>
            <!-- 删除按钮 -->
            <el-button
                icon="el-icon-delete"
                size="mini"
                type="danger"
                @click="removeUserById(scope.row.userId)"
            ></el-button>


          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区 -->
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
    </el-card>
    <!--    <UserInfo ref="UserInfo" ></UserInfo>-->
  </div>
</template>

<script>
import {Loginlog} from "@/api/admin.js"
import UserInfo from "./UserInfo";

export default {
  data() {
    return {
      cols: [{
        lable: '账号',
        props: 'account'
      }, {
        lable: 'ip',
        props: 'ip'
      }, {
        lable: '登录id',
        props: 'loginId'
      }, {
        lable: '登录描述',
        props: 'loginRemark'
      }, {
        lable: '登陆时间',
        props: 'loginTime'
      }, {
        lable: '登陆类型',
        props: 'loginType'
      }, {
        lable: '昵称',
        props: 'nickName'
      }, {
        lable: 'result',
        props: 'result'
      }, {
        lable: '电话',
        props: 'tel'
      }, {
        lable: 'userId',
        props: 'userId'
      }, {
        lable: '用户状态',
        props: 'userStatus'
      }],
      //起始时间和截止时间的时间列表
      timeList: ['', ''],
      // 获取用户列表的参数对象
      queryInfo: {
        beginTime: "",
        endTime: "",
        pageNum: 1,
        pageSize: 5,
        query: ''
      },
      // 用于保存获取到的用户列表
      loginLogList: [],
      // 总数据条数
      total: 0
    };
  },
  components: {UserInfo},
  created() {
    // 发送数据请求，获取用户列表数据
    this.getLoginlog();
  },
  methods: {
    getLoginlog() {
      Loginlog(this.queryInfo)
          .then((res) => {
            console.log(res)
            this.loginLogList = res.data.records
            this.total = res.data.total
          })
          .catch((err) => {
            console.log(err.message)
          })

    },


    // 查看用户详情
    show(row) {
      this.$refs.UserInfo.showEditDialog(row)
      // console.log((row))
    },

    resetPwd(userId) {
      resetPwd({userId})
          .then((res) => {
            console.log('res', res)
          })
          .catch((err) => {
            console.log('err', err)
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
