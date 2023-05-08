<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="8">
        <!--        <el-card shadow="hover">-->
        <!--  -->
        <!--&lt;!&ndash;          <div class="user-info">&ndash;&gt;-->
        <!--&lt;!&ndash;            <div class="user-info-cont">&ndash;&gt;-->
        <!--&lt;!&ndash;              <div class="user-info-name">1</div>&ndash;&gt;-->
        <!--&lt;!&ndash;              <div>2</div>&ndash;&gt;-->
        <!--&lt;!&ndash;            </div>&ndash;&gt;-->
        <!--&lt;!&ndash;          </div>&ndash;&gt;-->
        <!--&lt;!&ndash;          <div class="user-info-list">&ndash;&gt;-->
        <!--&lt;!&ndash;            上次登录时间：&ndash;&gt;-->
        <!--&lt;!&ndash;            <span>2022-10-01</span>&ndash;&gt;-->
        <!--&lt;!&ndash;          </div>&ndash;&gt;-->
        <!--&lt;!&ndash;          <div class="user-info-list">&ndash;&gt;-->
        <!--&lt;!&ndash;            上次登录地点：&ndash;&gt;-->
        <!--&lt;!&ndash;            <span>东莞</span>&ndash;&gt;-->
        <!--&lt;!&ndash;          </div>&ndash;&gt;-->
        <!--        </el-card>-->
        <div id="he-plugin-standard"></div>
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
                <el-button icon="el-icon-plus" type="text" @click="openDialog('todoInfo')">
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
              stripe
              :row-class-name="tableRowClassName"
              style="margin: auto;
              width: 100%;
              text-align: center"
          >
            <template slot="empty">
              <el-empty description="去添加待办事项吧！"></el-empty>
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
                    @click="openDialog(scope.row)"
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
      <el-col :span="12">
        <el-card shadow="hover">
        </el-card>
      </el-col>
    </el-row>


    <!--    事项详情-->
    <el-dialog
        :close-on-click-modal="false"
        :lock-scroll="true"
        :visible="visible"
        style="margin: 0 0 0 12%"
        top="80px"
        width="97%"
        @close="closed">

      <el-form
          ref="formRef"
          :inline="true"
          :model="todoInfo"
      >

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
          <el-button type="danger">清空正文</el-button>
          <el-button type="primary" @click="submitTodo">保 存</el-button>
        </el-form-item>
      </el-form>
      <my-editor3
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


export default {
  components: {LhPagination, MyEditor, MyEditor3},
  data() {
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
      adminInfo: {},
      totalMessage: {},
      total: 0,
      todoList: [],
      todoInfo: {
        mattersTitle: "",
        mattersImportance: '2',
        mattersContent: "",
      },
      visible: false,
      todoFlag: "",
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
        "width": "450",
        "height": "150",
        "background": "1",
        "dataColor": "FFFFFF",
        "language": "zh",
        "key": "465a66420c744e46af2fee004936bb00"
      }
    };
    (function (d) {
      var c = d.createElement('link')
      c.rel = 'stylesheet'
      c.href = 'https://widget.qweather.net/standard/static/css/he-standard.css?v=1.4.0'
      var s = d.createElement('script')
      s.src = 'https://widget.qweather.net/standard/static/js/he-standard.js?v=1.4.0'
      var sn = d.getElementsByTagName('script')[0]
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

    /*修改基础信息*/
    updateAdminInfo() {
      updateAdminInfo()
          .then()
    },
    /*修改密码*/
    updateAdminPwd() {
      updateAdminPwd()
          .then()
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
      this.todoInfo.mattersContent = content;
      // console.log(content)
    },

    /*保存事项*/
    submitTodo() {
      /*修改*/
      if (this.todoFlag) {
        updateMatter(this.todoInfo)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              this.$message.error(err.msg)
            });
      } else {
        /*新增*/
        addMatter(this.todoInfo)
            .then((res) => {
              this.$message.success(res.msg)
              this.closed();
            })
            .catch((err) => {
              debugger
              console.log("error")
              this.$message.error(err.msg)
            });
      }
    },

    /*打开dialog*/
    openDialog(todoInfo) {
      if (todoInfo === "todoInfo") {
        this.todoFlag = false;
      } else {
        this.todoInfo = JSON.parse(JSON.stringify(todoInfo));
        this.todoFlag = true;
      }
      this.visible = true
    },

    /*关闭dialog*/
    closed() {
      if (this.todoFlag) {

      } else {
        this.todoInfo.mattersImportance = '2'
      }


      this.visible = false
      this.matterSearch();
    },
    /*改变todo表格颜色*/
    tableRowClassName({row, rowIndex}) {
      console.log(row.mattersStatus)
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
