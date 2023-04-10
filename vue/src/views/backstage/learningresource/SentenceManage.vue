<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>学习资源</el-breadcrumb-item>
      <el-breadcrumb-item>励志语管理</el-breadcrumb-item>
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
          <el-form-item label="出现概率">
            <el-select
                v-model="queryInfo.frequency"
                clearable
                style="width: 125px">
              <el-option
                  v-for="status in frequencyList"
                  :key="status.key"
                  :label="status.label"
                  :value="status.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="作者">
            <el-input
                v-model="queryInfo.author"
                autocomplete="off"
                clearable
                placeholder="请输入"
                type="text"
                style="width: 150px"
                @input="() => $forceUpdate()"
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-search" type="primary" @click="emotionWordsSearch">
              查询
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-delete" type="danger" @click="deleteWords">
              删除
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-button icon="el-icon-circle-plus-outline" type="warning" @click="show('emo')">
              录入
            </el-button>
          </el-form-item>
          <el-form-item>
            <el-upload
                :before-upload="iconChange"
                :on-error="importError"
                :on-success="importSuccess"
                :show-file-list="false"
                accept=".xlsx,.xls"
                action="api/emotionWords/emotionWordsExcel"
            >
              <!--              :headers="{Authorization:}"-->
              <el-button :icon="`el-icon-${this.importLoading?'loading':'upload2'}`" type="success">
                导入
              </el-button>
            </el-upload>
          </el-form-item>
        </el-form>
      </div>

      <!-- 登录日志列表区 -->
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
          <el-table-column
              align="center"
              min-width="3%"
              type="selection">
          </el-table-column>
          <el-table-column align="center" label="序号" min-width="5%">
            <template v-slot="scope">
          <span>{{
              scope.$index + (queryInfo.pageNum - 1) * queryInfo.pageSize + 1
            }}</span>
            </template>
          </el-table-column>
          <el-table-column align="center" label="创建时间" min-width="15%" prop="emoCreateTime"></el-table-column>
          <el-table-column align="center" label="作者" min-width="15%" prop="emoAuthor"></el-table-column>
          <el-table-column align="center" label="概念" min-width="5%" prop="frequency"></el-table-column>
          <el-table-column label="中文" min-width="25%" prop="cnContent" show-overflow-tooltip
                           text-align="left"></el-table-column>
          <el-table-column label="英文" min-width="25%" prop="engContent" show-overflow-tooltip
                           text-align="left"></el-table-column>
          <el-table-column align="center" fixed="right" label="操作" min-width="7%">
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
            </template>
          </el-table-column>
        </el-table>
      </div>

      <!-- 分页区 -->
      <div class="flex_center_center">
        <lh-pagination v-show="total > 0" :limit.sync="queryInfo.pageSize" :page.sync="queryInfo.pageNum"
                       :total="total" @pagination="emotionWordsSearch"/>
      </div>
    </el-card>
    <SentenceInfo ref="SentenceInfo" @searchAgain="emotionWordsSearch"></SentenceInfo>
  </div>
</template>

<script>

import {deleteWords, emotionWordsSearch} from "@/api/admin.js"
import LhPagination from "@/components/lhPublic/lhPagination";
import SentenceInfo from "@/views/backstage/learningresource/SentenceInfo";

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
        author: "",
        frequency: "",
        // 当前页码
        pageNum: 1,
        // 每页显示条数
        pageSize: 10,
      },
      //登录方式列表
      frequencyList: [
        // 用户状态，0正常，1锁定，2待删除
        {label: '高', key: 1},
        {label: '中', key: 2},
        {label: '低', key: 3},
      ],
      // 用于保存获取到的用户列表
      sentenceList: [],
      // 总数据条数
      total: 0,
      importLoading: false,
      searchLoading: false,
      multipleSelection: []
    };
  },
  components: {LhPagination, SentenceInfo},
  created() {
    // 发送数据请求，获取用户列表数据
    this.emotionWordsSearch();
  },
  methods: {

    // 查看管理员详情
    show(row) {
      this.$refs.SentenceInfo.showEditDialog(row)
    },

    iconChange() {
      this.importLoading = true;
    },

    importSuccess(response) {
      console.log(response)
      this.importLoading = false;
      this.$message.success(response.msg);
      this.emotionWordsSearch();
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
              this.emotionWordsSearch();
            })
      });
    },

    //查询
    emotionWordsSearch() {
      this.queryInfo.beginTime = this.pickDate.beginDate
      this.queryInfo.endTime = this.pickDate.endDate
      let params = this.queryInfo;
      this.searchLoading = true;
      emotionWordsSearch(params)
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
      this.emotionWordsSearch();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.emotionWordsSearch();
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
