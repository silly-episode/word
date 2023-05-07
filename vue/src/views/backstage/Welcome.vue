<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <el-card class="mgb20" shadow="hover" style="height: 252px">
          <div class="user-info">
            <div class="user-info-cont">
              <div class="user-info-name">1</div>
              <div>2</div>
            </div>
          </div>
          <div class="user-info-list">
            上次登录时间：
            <span>2022-10-01</span>
          </div>
          <div class="user-info-list">
            上次登录地点：
            <span>东莞</span>
          </div>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card :body-style="{ padding: '0px' }" shadow="hover">
              <div class="grid-content grid-con-1">
                <el-icon class="grid-con-icon el-icon-user">
                  <User/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ totalMessage.userTotal }}</div>
                  <div>用户总数</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card :body-style="{ padding: '0px' }" shadow="hover">
              <div class="grid-content grid-con-2">
                <el-icon class="grid-con-icon el-icon-coin">
                  <ChatDotRound/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ totalMessage.wordModuleTotal }}</div>
                  <div>单词模块</div>
                </div>
              </div>
            </el-card>
          </el-col>
          <el-col :span="8">
            <el-card :body-style="{ padding: '0px' }" shadow="hover">
              <div class="grid-content grid-con-3">
                <el-icon class="grid-con-icon el-icon-document">
                  <Goods/>
                </el-icon>
                <div class="grid-cont-right">
                  <div class="grid-num">{{ totalMessage.articleTotal }}</div>
                  <div>文章数量</div>
                </div>
              </div>
            </el-card>
          </el-col>
        </el-row>


        <el-card shadow="hover" style="height: 403px">
          <template #header>
            <div class="clearfix">

              <el-form ref="queryParams" :inline="true" :model="pickDate">
                <el-form-item>
                  <el-button style="font-size: medium" type="text">待办事项</el-button>
                </el-form-item>
                <el-form-item>
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
                      placeholder="开始时间"
                      style="width: 200px"
                      type="datetime"
                      value-format="yyyy-MM-dd HH:mm:ss"
                  >
                  </el-date-picker>
                </el-form-item>
                <el-form-item>
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
                      placeholder="结束时间"
                      style="width: 200px"
                      type="datetime"
                      value-format="yyyy-MM-dd HH:mm:ss"
                  >
                  </el-date-picker>
                </el-form-item>
                <el-form-item>
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
                <el-form-item>
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
                <el-form-item>
                  <el-input
                      v-model="queryInfo.search"
                      autocomplete="off"
                      clearable
                      placeholder="事项"
                      type="text"
                      @input="() => $forceUpdate()"
                  ></el-input>
                </el-form-item>
                <el-button icon="el-icon-search" type="text" @click="">
                  查询
                </el-button>
                <el-button icon="el-icon-plus" type="text" @click="">
                  添加
                </el-button>
              </el-form>

            </div>
          </template>

          <el-table
              v-loading="searchLoading"

              :data="todoList"
              :header-cell-style="{'text-align':'center'}"
              highlight-current-row
              stripe
              style="margin: auto;
            width: 100%;
            text-align: center"

          >
            <template slot="empty">
              <el-empty description="去添加待办事项吧！"></el-empty>
            </template>
            <el-table-column label="完成" min-width="5%">
              <template v-slot="scope">
                <el-checkbox v-model="scope.row.mattersStatus"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column label="事项" min-width="35%">
              <template v-slot="scope">
                <div
                    :class="{
										'todo-item-del': scope.row.mattersStatus
									}"
                    class="todo-item"
                >
                  {{ scope.row.title }}
                </div>
              </template>
            </el-table-column>

            <el-table-column label="添加时间" min-width="15%">

            </el-table-column>

            <el-table-column label="完成时间" min-width="15%">

            </el-table-column>

            <el-table-column label="操作" min-width="15%">

            </el-table-column>

          </el-table>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card shadow="hover">
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {adminInfo, getTotalMessage, matterSearch} from "@/api/admin.js"

export default {
  data() {
    return {
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
      searchLoading: false,
      adminInfo: {},
      totalMessage: {},
      total: '',
      todoList: {},


    }
  },
  created() {
    this.getAdminInfo();
    this.getTotalMessage();
    this.matterSearch();
  },
  methods: {

    /*获取管理员信息*/
    getAdminInfo() {
      adminInfo().then((res) => {
        this.adminInfo = res.data;
      })
    },

    /*获取管理员信息*/
    getTotalMessage() {
      getTotalMessage().then((res) => {
        this.totalMessage = res.data;
      })
    },

    /*分页条件搜索事项*/
    matterSearch() {
      matterSearch(this.queryInfo)
          .then((res) => {
            this.total = res.data.total
            this.todoList = res.data.records;
          })
    },


  },


}
</script>

<style scoped>
.el-row {
  margin-bottom: 20px;
}

.grid-content {
  display: flex;
  align-items: center;
  height: 100px;
}

.grid-cont-right {
  flex: 1;
  text-align: center;
  font-size: 14px;
  color: #999;
}

.grid-num {
  font-size: 30px;
  font-weight: bold;
}

.grid-con-icon {
  font-size: 50px;
  width: 100px;
  height: 100px;
  text-align: center;
  line-height: 100px;
  color: #fff;
}

.grid-con-1 .grid-con-icon {
  background: rgb(45, 140, 240);
}

.grid-con-1 .grid-num {
  color: rgb(45, 140, 240);
}

.grid-con-2 .grid-con-icon {
  background: rgb(100, 213, 114);
}

.grid-con-2 .grid-num {
  color: rgb(100, 213, 114);
}

.grid-con-3 .grid-con-icon {
  background: rgb(242, 94, 67);
}

.grid-con-3 .grid-num {
  color: rgb(242, 94, 67);
}

.user-info {
  display: flex;
  align-items: center;
  padding-bottom: 20px;
  border-bottom: 2px solid #ccc;
  margin-bottom: 20px;
}

.user-info-cont {
  padding-left: 50px;
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 30px;
  color: #222;
}

.user-info-list {
  font-size: 14px;
  color: #999;
  line-height: 25px;
}

.user-info-list span {
  margin-left: 70px;
}

.mgb20 {
  margin-bottom: 20px;
}

.todo-item {
  font-size: 14px;
}

.todo-item-del {
  text-decoration: line-through;
  color: #999;
}

.schart {
  width: 100%;
  height: 300px;
}
</style>
