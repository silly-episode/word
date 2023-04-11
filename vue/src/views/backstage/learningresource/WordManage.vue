<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>学习资源</el-breadcrumb-item>
      <el-breadcrumb-item>单词管理</el-breadcrumb-item>
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
          <el-form-item label="上级模块">
            <el-select
                v-model="queryInfo.superiorModule"
                clearable
                style="width: 110px">
              <el-option
                  v-for="status in superiorModuleList"
                  :key="status.key"
                  :label="status.label"
                  :value="status.key">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="学习人数排序">
            <el-select
                v-model="queryInfo.studyNumberOrderByAsc"
                clearable
                style="width: 90px">
              <el-option
                  v-for="status in studyNumberOrderByAscList"
                  :key="status.key"
                  :label="status.label"
                  :value="status.value">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="模块名称">
            <el-input
                v-model="queryInfo.moduleName"
                autocomplete="off"
                clearable
                placeholder="请输入"
                style="width: 150px"
                type="text"
                @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>

          <el-button icon="el-icon-search" type="primary" @click="moduleSearch">
            查询
          </el-button>


          <el-button icon="el-icon-circle-plus-outline" type="warning" @click="show('module')">
            录入
          </el-button>

        </el-form>
      </div>

      <!-- 英语模块列表区 -->
      <div class="noTableScrollBar">
        <el-table
            v-loading="searchLoading"
            :data="sentenceList"
            :header-cell-style="{'text-align':'center'}"
            :height="tableHeight === 0 ? 'calc(100vh - 301px)' : tableHeight"
            border
            highlight-current-row
            stripe style="margin: auto; width: 100%; text-align: center"
            @selection-change="handleSelectionChange">
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
          <el-table-column align="center" label="创建时间" min-width="12%" prop="wordModuleCreateTime"></el-table-column>
          <el-table-column align="center" label="上级模块" min-width="7%" prop="superiorModule">
            <template v-slot="scope">
              <span>{{ superiorModuleList[scope.row.superiorModule].label }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="名称" min-width="15%" prop="moduleName"></el-table-column>
          <el-table-column align="center" label="状态" min-width="5%" prop="wordModuleStatus" show-overflow-tooltip>
            <template v-slot="scope">
              <span>{{ statusList[scope.row.wordModuleStatus] }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="词数" min-width="8%" prop="wordCount"
                           show-overflow-tooltip></el-table-column>
          <el-table-column align="center" label="学习人数" min-width="8%" prop="studyNumber"
                           show-overflow-tooltip></el-table-column>
          <el-table-column label="单词模块说明" min-width="28%" prop="remark" show-overflow-tooltip
                           text-align="left"></el-table-column>
          <el-table-column align="center" fixed="right" label="操作" min-width="13%">
            <template v-slot="scope">
              <!-- 修改按钮 -->
              <el-tooltip
                  :enterable="false"
                  content="修改"
                  effect="dark"
                  placement="top"
              >
                <el-button
                    icon="el-icon-edit-outline"
                    size="small"
                    type="primary"
                    @click="show(scope.row)"
                ></el-button>
              </el-tooltip>
              <!-- 锁定/解锁按钮 -->
              <el-tooltip
                  :enterable="false"
                  content="锁定/解锁"
                  effect="dark"
                  placement="top"
              >
                <el-button
                    icon="el-icon-key"
                    size="small"
                    type="success"
                    @click="lockOrUnLockModule(scope.row.moduleId,scope.row.wordModuleStatus)"
                ></el-button>
              </el-tooltip>
              <!-- 删除按钮 -->
              <el-tooltip
                  :enterable="false"
                  content="删除"
                  effect="dark"
                  placement="top"
              >
                <el-button
                    icon="el-icon-delete"
                    size="small"
                    type="danger"
                    @click="deleteModule(scope.row.moduleId)"
                ></el-button>
              </el-tooltip>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页区 -->
      <div class="flex_center_center">
        <lh-pagination v-show="total > 0" :limit.sync="queryInfo.pageSize" :page.sync="queryInfo.pageNum"
                       :total="total" @pagination="moduleSearch"/>
      </div>
    </el-card>
    <WordInfo ref="WordInfo" @searchAgain="moduleSearch"></WordInfo>
  </div>
</template>

<script>

import {deleteModule, lockOrUnLockModule, moduleSearch} from "@/api/admin.js"
import LhPagination from "@/components/lhPublic/lhPagination";
import WordInfo from "@/views/backstage/learningresource/WordInfo";

export default {
  data() {
    return {
      headers: {
        Authorization: "Bearer " + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2NzU5MTk2MzcsImlhdCI6MTY3NTgzMzIzNywiYWNjb3VudCI6Ijg5NzkxNzcyOCJ9.kjGMhBFkBFxf_D6G-srMkPogilpL91vE_EEE3n71ozA"
      },
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
        moduleName: "",
        superiorModule: "",
        studyNumberOrderByAsc: "",
        // 当前页码
        pageNum: 1,
        // 每页显示条数
        pageSize: 10,
      },
      //上级模块列表
      statusList: ['正常', '锁定'],
      superiorModuleList: [
        {label: '出国必用', key: "0"},
        {label: '大学词汇', key: "1"},
        {label: '高中词汇', key: "2"},
        {label: '初中词汇', key: "3"},
        {label: '小学词汇', key: "4"},
      ],
      studyNumberOrderByAscList: [
        {label: "降序", value: false, key: 1},
        {label: "升序", value: true, key: 2},
      ],
      // 用于保存获取到的用户列表
      sentenceList: [],
      // 总数据条数
      total: 0,
      importLoading: false,
      searchLoading: false,
    };
  },
  components: {LhPagination, WordInfo},
  created() {
    // 发送数据请求，获取用户列表数据
    this.moduleSearch();
  },
  methods: {

    deleteModule(moduleId) {
      this.$prompt('防止误操作,请输入:“谨慎操作,建议用锁定代替删除。”', '确认框', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        center: true,
        inputPattern: /^谨慎操作,建议用锁定代替删除。$/,
        inputErrorMessage: '输入内容错误'
      }).then(({value}) => {
        deleteModule(moduleId)
            .then((res) => {
              this.$message.success(res.msg)
            })
            .catch((err) => {
              this.$message.error(err.msg)
            })
            .finally(() => {
              this.moduleSearch();
            })
      })
    },

    //锁定和解锁单词模块
    lockOrUnLockModule(moduleId, wordModuleStatus) {
      this.$confirm("请确定是否 锁定/解锁 该模块", {
        cancelButtonText: "取消",//取消按钮文字更换
        confirmButtonText: "确认",//确认按钮文字更换
        showClose: true,//是否显示右上角关闭按钮
        type: "warning",//提示类型 success/info/warning/error
      })
          .then(() => {
            let params = {
              moduleId: moduleId,
              lockType: "unLock"
            }
            if ("1" !== wordModuleStatus) {
              params.lockType = "lock"
            }
            lockOrUnLockModule(params)
                .then((res) => {
                  this.$message.success(res.msg)
                })
                .catch((err) => {
                  this.$message.success(err.msg)
                })
                .finally(() => {
                      this.moduleSearch()
                    }
                );
          })
          .catch((err) => {
//捕获异常
          });
    },

    // 查看管理员详情
    show(row) {
      this.$refs.WordInfo.showEditDialog(row)
    },

    iconChange() {
      this.importLoading = true;
    },

    importSuccess(response) {
      console.log(response)
      this.importLoading = false;
      this.$message.success(response.msg);
      this.moduleSearch();
    },
    importError(err) {
      this.importLoading = false;
      this.$message.error(err.msg);
    },

    /*批量删除励志语言*/
    deleteWords() {
      if (this.multipleSelection.length === 0) {
        this.$message.info("请选择待删除的语句");
        return;
      }
      this.$confirm('此操作将永久所选中的Sentence', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteWords(this.multipleSelection)
            .then((res) => {
              this.$message.success(res.msg);
            })
            .catch((err) => {
              this.$message.error(err.msg);
            })
            .finally(() => {
              this.moduleSearch();
            })
      });
    },

    //查询
    moduleSearch() {
      this.queryInfo.beginTime = this.pickDate.beginDate
      this.queryInfo.endTime = this.pickDate.endDate
      let params = this.queryInfo;
      this.searchLoading = true;
      moduleSearch(params)
          .then(response => {
            this.sentenceList = response.data.records;
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
      this.moduleSearch();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.moduleSearch();
    },
    handleSelectionChange(val) {
      this.multipleSelection = val;
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
