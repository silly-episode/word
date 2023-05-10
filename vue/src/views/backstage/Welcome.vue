<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <div id="he-plugin-standard"></div>
        <el-card shadow="hover" style="height: 355px; margin-top: 20px">
          <template>
            <div class="user-info">
              <div class="user-info-cont">
                <div>个人信息</div>
              </div>
            </div>
            <div class="user-info-list">
              <el-row :gutter="50">
                <el-col :span="12">
                  <span>持有人：</span>
                </el-col>
                <el-col :span="12">
                  <span>{{ adminInfo.keepName }}</span>
                </el-col>
              </el-row>
              <el-row :gutter="50">
                <el-col :span="12">
                  账号：
                </el-col>
                <el-col :span="12">
                  <span>{{ adminInfo.account }}</span>
                </el-col>
              </el-row>
              <el-row :gutter="50">
                <el-col :span="12">
                  角色：
                </el-col>
                <el-col :span="12">
                  <span>{{ roleList[adminInfo.role] }}</span>
                </el-col>
              </el-row>
              <el-row :gutter="50">
                <el-col :span="12">
                  联系电话：
                </el-col>
                <el-col :span="12">
                  <span>{{ adminInfo.tel }}</span>
                </el-col>
              </el-row>
              <el-row :gutter="50">
                <el-col :span="12">
                  添加日期：
                </el-col>
                <el-col :span="12">
                  <span v-text="`${adminInfo.addCreateTime.substring(0,10)}`"></span>
                </el-col>
              </el-row>
            </div>

            <div class="flex_center_center">
              <el-button icon="el-icon-edit" type="primary" @click="openAdminDialog(true)">修改信息</el-button>
              <el-button icon="el-icon-lock" type="danger" @click="openAdminDialog(false)">修改密码</el-button>
            </div>

          </template>
        </el-card>
      </el-col>
      <el-col :span="16">
        <el-row :gutter="20" class="mgb20">
          <el-col :span="8">
            <el-card :body-style="{ padding: '0px' }" shadow="hover">
              <div class="grid-content grid-con-1">
                <el-icon class="grid-con-icon el-icon-user">

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
          <!--         检索 -->
          <template>
            <div>
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
                      v-model="queryInfo.matterStatus"
                      placeholder="状态"
                      clearable
                      style="width: 90px">
                    <el-option
                        v-for="item in matterStatusList"
                        :key="item.value"
                        :label="item.label"
                        :value="item.value">
                    </el-option>
                  </el-select>
                </el-form-item>
                <el-form-item>
                  <el-select
                      v-model="queryInfo.matterImportance"
                      placeholder="程度"
                      clearable
                      style="width: 75px">
                    <el-option
                        v-for="item in matterImportanceList"
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
                      style="width: 120px"
                      @input="() => $forceUpdate()"
                  ></el-input>
                </el-form-item>
                <el-button icon="el-icon-search" type="text" @click="matterSearch">
                  查询
                </el-button>
                <el-button icon="el-icon-plus" type="text" @click="openTodoDialog('todoInfo')">
                  添加
                </el-button>
              </el-form>
            </div>
          </template>
          <!--   表格       -->
          <el-table
              :cell-style="{'text-align':'center'}"
              v-loading="searchLoading"
              :data="todoList"
              :header-cell-style="{'text-align':'center'}"
              :height="tableHeight === 0 ? `calc(10/9.0*${radio}*100vh - 590px)` : tableHeight"
              highlight-current-row
              :row-class-name="tableRowClassName"
              style="margin: auto;
              width: 100%;
              text-align: center"
          >
            <template slot="empty">
              <el-empty style="padding-top:100px"></el-empty>
            </template>
            <el-table-column label="完成" min-width="5%">
              <template v-slot="scope">
                <el-checkbox v-model="scope.row.mattersStatus"
                             @change="changeMatterStatus(scope.row.mattersId,scope.row.mattersStatus)"></el-checkbox>
              </template>
            </el-table-column>
            <el-table-column
                label="事项主题"
                min-width="30%"
                show-overflow-tooltip>
              <template v-slot="scope">
                <div
                    :class="{
										'todo-item-del': scope.row.mattersStatus
									}"
                    class="todo-item"
                >
                  {{ scope.row.mattersTitle }}
                </div>
              </template>
            </el-table-column>

            <el-table-column label="添加时间" min-width="20%" prop="mattersInsertTime">

            </el-table-column>

            <el-table-column label="完成时间" min-width="15%" prop="mattersFinishTime">
              <template v-slot="scope">
                <span v-text="`${scope.row.mattersFinishTime==null?'待完成':scope.row.mattersFinishTime}`"></span>
              </template>
            </el-table-column>

            <el-table-column label="操作" min-width="15%">
              <template v-slot="scope">
                <!-- 保存按钮 -->
                <el-button
                    icon="el-icon-postcard"
                    size="mini"
                    type="text"
                    @click="openTodoDialog(scope.row)"
                >详情
                </el-button>
                <!-- 删除按钮 -->
                <el-button
                    icon="el-icon-delete"
                    size="mini"
                    type="text"
                    @click="deleteMatter(scope.row.mattersId)"
                >删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
          <!-- 分页区 -->
          <div class="flex_center_center">
            <lh-pagination v-show="total > 0" :limit.sync="queryInfo.pageSize" :page.sync="queryInfo.pageNum"
                           :total="total" @pagination="matterSearch"/>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :span="12">
        <el-card shadow="hover">

        </el-card>
      </el-col>
      <el-col :span="6">
        <Category :total-flag=false></Category>
      </el-col>
      <el-col :span="6">
        <Category :total-flag=true></Category>
      </el-col>
    </el-row>


    <!-- 修改个人信息Dialog -->
    <el-dialog
        :close-on-click-modal="false"
        :lock-scroll="true"
        :visible="adminVisible"
        style="margin: 0 0 0 12%"
        title="修改个人信息"
        top="200px"
        width="50%"
        @close="adminDialogClosed">
      <el-form
          v-if="adminFlag"
          ref="formRef"
          :model="adminChangedInfo"
          label-width="80px"
          style="margin: 0 7% 0 7%"
      >
        <el-form-item label="账号" prop="account">
          <el-input v-model.trim="adminChangedInfo.account" type="number"></el-input>
        </el-form-item>

        <el-form-item label="持有人" prop="keepName">
          <el-input v-model.trim="adminChangedInfo.keepName"></el-input>
        </el-form-item>
        <el-form-item label="联系电话" prop="tel">
          <el-input v-model.trim="adminChangedInfo.tel" type="number"></el-input>
        </el-form-item>
      </el-form>

      <el-form
          v-if="!adminFlag"
          ref="form"
          :model="adminPwd"
          :rules="rules"
          label-width="80px"
          style="margin: 0 7% 0 7%">
        <el-form-item label="旧密码" prop="oldPassword">
          <el-input
              v-model="adminPwd.oldPassword"
              placeholder="请输入旧密码"
              show-password
              type="password"
          />
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input
              v-model="adminPwd.newPassword"
              placeholder="请输入新密码"
              show-password
              type="password"
          />
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input
              v-model="adminPwd.confirmPassword"
              placeholder="请确认新密码"
              show-password
              type="password"
          />
        </el-form-item>
      </el-form>


      <div class="flex_center_center">
        <el-button type="danger" @click="adminSubmit">提交</el-button>
        <el-button type="info" @click="adminDialogClosed">关闭</el-button>
      </div>


    </el-dialog>

    <!--    事项详情-->
    <el-dialog
        v-if="todoVisible"
        :visible.sync="todoVisible"
        destroy-on-close
        :close-on-click-modal="false"
        :lock-scroll="true"
        style="margin: 0 0 0 12%"
        top="80px"
        width="97%"
        @close="todoDialogClosed">

      <el-form
          ref="todoForm"
          :inline="true"
          :model="todoInfo">
        <el-form-item aria-rowindex="50px" label="事项主题" prop="mattersTitle">
          <el-input
              v-model.number="todoInfo.mattersTitle"
              clearable
              maxlength="50"
              show-word-limit
              style="width: 440px"
          ></el-input>
        </el-form-item>
        <el-form-item label="重要程度" prop="mattersImportance">
          <el-select
              v-model="todoInfo.mattersImportance"
              placeholder="程度"
              style="width: 100px">
            <el-option
                v-for="item in matterImportanceList"
                :key="item.value"
                :label="item.label"
                :value="item.value">
            </el-option>
          </el-select>
        </el-form-item>

        <el-form-item v-if="todoFlag" aria-rowindex="50px" label="添加时间" prop="mattersInsertTime">
          <el-input v-model.trim="todoInfo.mattersInsertTime" :disabled="true"></el-input>
        </el-form-item>

        <el-form-item v-if="todoFlag" label="完成时间" prop="mattersFinishTime">
          <el-input
              v-model.trim="todoInfo.mattersFinishTime"
              :disabled="true"
              placeholder="待完成"
          ></el-input>
        </el-form-item>

        <el-form-item class="flex_center_center">
          <el-button type="danger" @click="clearToDoContent">清空正文</el-button>
          <el-button type="primary" @click="submitTodo">保 存</el-button>
        </el-form-item>
      </el-form>
      <my-editor3
          ref="myEditor"
          :content="todoInfo.mattersContent"
          @changeData="todoChanged"
      ></my-editor3>

      <!--      <div>-->
      <!--        <MyEditor-->
      <!--            :content="content"-->
      <!--            :readOnlys="readOnlys"-->
      <!--            @changeData="hChangeData"-->
      <!--            @uploadImg="hUploadImg"-->
      <!--        />-->
      <!--      </div>-->
      <!--      label-width="120px"-->
      <!--      style="margin: 0 7% 0 0"-->

    </el-dialog>


  </div>
</template>

<script>
import {
  addMatter,
  adminInfo,
  changeMatterStatus,
  deleteMatter,
  getTotalMessage,
  matterSearch,
  updateAdminInfo,
  updateAdminPwd,
  updateMatter
} from "@/api/admin.js";
import LhPagination from "@/components/lhPublic/lhPagination";
import MyEditor from "@/components/MyEditor"
import MyEditor3 from "@/components/MyEditor3"
import Category from "@/components/category";

export default {
  components: {LhPagination, MyEditor, MyEditor3, Category},
  data() {
    const equalToPassword = (rule, value, callback) => {
      if (this.adminPwd.newPassword !== value) {
        callback(new Error("两次输入的密码不一致"));
      } else {
        callback();
      }
    };
    return {
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
        /*事项状态*/
        matterStatus: "",
        /*事项重要度*/
        matterImportance: "",
        /*搜索*/
        search: "",
        // 当前页码
        pageNum: 1,
        // 每页显示条数
        pageSize: 5,
      },
      searchLoading: false,
      adminInfo: {addCreateTime: ""},
      totalMessage: {},
      total: 0,
      todoList: [],
      todoInfo: {
        mattersTitle: "",
        mattersImportance: '2',
        mattersContent: "",
      },
      todoVisible: false,
      todoFlag: "",
      adminFlag: "",
      adminVisible: false,
      adminChangedInfo: {},
      adminPwd: {
        oldPassword: undefined,
        newPassword: undefined,
        confirmPassword: undefined
      },
      // 表单校验
      rules: {
        oldPassword: [
          {required: true, message: "旧密码不能为空", trigger: "blur"}
        ],
        newPassword: [
          {required: true, message: "新密码不能为空", trigger: "blur"},
        ],
        confirmPassword: [
          {required: true, message: "确认密码不能为空", trigger: "blur"},
          {required: true, validator: equalToPassword, trigger: "blur"}
        ]
      },
      // 角色列表
      roleList: ['超级管理员', '普通管理员'],
      /*事项状态*/
      matterStatusList: [
        {label: '未完成', value: false},
        {label: '已完成', value: true}
      ],
      /*事项重要程度*/
      matterImportanceList: [
        {label: '轻', value: '1'},
        {label: '中', value: '2'},
        {label: '重', value: '3'}
      ],

    }
  },
  created() {
    this.getAdminInfo();
    this.getTotalMessage();
    this.matterSearch();
    /*测试天气组件*/
    window.WIDGET = {
      "CONFIG": {
        "layout": "1",
        "width": "475",
        "height": "150",
        "background": "1",
        "dataColor": "FFFFFF",
        "language": "zh",
        "borderRadius": "5",
        "city": "CN101250109",
        "key": "465a66420c744e46af2fee004936bb00"
      }
    };
    (function (d) {
      const c = d.createElement('link');
      c.rel = 'stylesheet'
      c.href = 'https://widget.qweather.net/standard/static/css/he-standard.css?v=1.4.0'
      const s = d.createElement('script');
      s.src = 'https://widget.qweather.net/standard/static/js/he-standard.js?v=1.4.0'
      const sn = d.getElementsByTagName('script')[0];
      sn.parentNode.insertBefore(c, sn)
      sn.parentNode.insertBefore(s, sn)
    })(document);
  },
  methods: {

    hUploadImg(file, insertFn) {
      console.log(file)
      // 插入图片，调接口返回图片url,通过插入conteng
      let imgUrl = 'https://img1.baidu.com/it/u=608404415,1639465523&fm=253&fmt=auto&app=120&f=JPEG?w=1280&h=800'
      insertFn(imgUrl);

      // 设置只读
      this.readOnlys = true;

    },

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
    /*删除事项*/
    deleteMatter(mattersId) {
      deleteMatter(mattersId)
          .then((res) => {
            this.$notify.success({
              title: '成功',
              message: "删除成功",
              offset: 60
            });
          })
          .catch((err) => {
            console.log(err)
          })
          .finally(() => {
            this.matterSearch();
          })
    },
    /*改变事项状态*/
    changeMatterStatus(mattersId, mattersStatus) {
      changeMatterStatus(mattersId, mattersStatus)
          .then((res) => {
            console.log(res)
          })
          .catch((err) => {
            console.log(err)
          })
          .finally(() => {
            this.matterSearch();
          })
    },


    /*分页条件搜索事项*/
    matterSearch() {
      this.queryInfo.beginTime = this.pickDate.beginDate
      this.queryInfo.endTime = this.pickDate.endDate
      let params = this.queryInfo;
      this.searchLoading = true;
      matterSearch(params)
          .then((res) => {
            this.total = res.data.total
            console.log(res)
            this.todoList = res.data.records;
          })
          .catch((err) => {
            console.log(err)
          })
          .finally(() => [
            this.searchLoading = false
          ]);
    },

    /*接受富文本编辑器中的内容*/
    todoChanged(content) {
      console.log('content', content)
      this.todoInfo.mattersContent = content;
      // console.log(content)
    },

    /*清空todo的正文*/
    clearToDoContent() {
      this.$refs.myEditor.clearContent();
    },

    /*保存事项*/
    submitTodo() {
      /*修改*/
      if (this.todoFlag) {
        updateMatter(this.todoInfo)
            .then((res) => {
              this.$message.success(res.msg)
              this.todoDialogClosed();
            })
            .catch((err) => {
              this.$message.error(err.msg)
            });
      } else {
        /*新增*/
        addMatter(this.todoInfo)
            .then((res) => {
              this.$message.success(res.msg)
              this.todoDialogClosed();
            })
            .catch((err) => {
              debugger
              console.log("error")
              this.$message.error(err.msg)
            });
      }
    },

    /*提交管理员信息（修改基础信息和修改密码）*/
    adminSubmit() {
      if (this.adminFlag) {
        updateAdminInfo(this.adminChangedInfo)
            .then((res) => {
              this.$notify.success({
                title: '成功',
                message: "修改成功",
                offset: 60
              });
            })
            .catch((err) => {
              console.log(err)
            })
            .finally(() => {
              this.getAdminInfo();
            })
      } else {
        this.$refs["form"].validate(valid => {
          if (valid) {
            updateAdminPwd(this.adminPwd).then(res => {
              this.adminPwd = {
                oldPassword: undefined,
                newPassword: undefined,
                confirmPassword: undefined
              }
            })
          }
        })
      }
      this.adminDialogClosed();
    },

    /*打开修改信息的dialog*/
    openAdminDialog(flag) {
      /*修改信息为true*/
      /*修改密码为false*/
      this.adminFlag = flag;
      if (flag) {
        this.adminChangedInfo = JSON.parse(JSON.stringify(this.adminInfo))
      }
      this.adminVisible = true
    },
    /*关闭修改 信息的 dialog*/
    adminDialogClosed() {

      if (this.adminFlag) {
        this.adminChangedInfo = {};
      } else {
        this.adminPwd = {};
      }
      this.getAdminInfo();
      this.adminVisible = false
    },


    /*打开dialog*/
    openTodoDialog(todoInfo) {
      if (todoInfo === "todoInfo") {
        this.todoFlag = false;
      } else {
        this.todoInfo = JSON.parse(JSON.stringify(todoInfo));
        console.log('todoInfo', this.todoInfo)
        this.todoFlag = true;
      }
      this.todoVisible = true
    },

    /*关闭dialog*/
    todoDialogClosed() {
      if (this.todoFlag) {

      } else {
        this.todoInfo.mattersImportance = '2'
      }


      this.todoInfo = {
        mattersTitle: "",
        mattersImportance: '2',
        mattersContent: "",
      }
      this.todoVisible = false
      this.matterSearch();
    },
    /*改变todo表格颜色*/
    tableRowClassName({row, rowIndex}) {
      if (!row.mattersStatus) {
        return 'warning-row';
      } else {
        return '';
      }
    },


    // 监听pagesize的改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      this.matterSearch();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.matterSearch();
    },

  },


}
</script>

<style scoped>


/deep/ .el-row {
  margin-bottom: 20px;
}

/deep/ .el-table .warning-row {
  background: oldlace;
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
  flex: 1;
  font-size: 14px;
  color: #999;
}

.user-info-cont div:first-child {
  font-size: 22px;
  color: #0086b3;
  display: flex;
  justify-content: center;
  align-items: center;
}

.user-info-list {
  border-bottom: 2px solid #ccc;
  font-size: 22px;
  color: #999;
  line-height: 20px;
  margin-bottom: 15px;
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
