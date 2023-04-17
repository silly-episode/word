<template>
  <el-container>
    <el-aside width="10%"></el-aside>
    <el-main class="main">
      <div class="margin_b_30">
        <h2><i></i>我的单词本</h2>
        <div class="flex_wrap_center margin_t_30">
          <div
            @click="goTo(item.bookId)"
            v-for="item in bookList"
            :key="item.bookId"
            class="li flex_column_center_center"
          >
            <p class="font_16 font_bold">{{ item.bookName }}</p>
            <p class="font_12 grey margin_t_10 margin_b_5">
              单词总数：{{ item.wordCount }}
            </p>
            <p class="font_12 grey">
              创建时间：{{ item.bookCreateTime.substring(0, 10) }}
            </p>
          </div>
          <div class="li flex_center_center" @click="showAddBook">
            <i class="el-icon-plus font_30 grey"></i>
          </div>
        </div>
      </div>
      <div class="margin_t_30">
        <h2>
          <i></i>正在背诵的词表:

          <span v-if="JSON.stringify(mainPlan) == '{}'">暂未选择词表</span>
          <span v-else>{{ mainPlan.moduleName }}</span>
        </h2>
        <p class="font_16 font_bold margin_l_20 margin_t_20">主计划</p>
        <div
          class="font_16 border_ccc flex_between_center padding_20 margin_t_20"
        >
          <template v-if="JSON.stringify(mainPlan) == '{}'">
            暂无主计划</template
          >
          <template v-else>
            <div class="wid_per70">
              <p>计划名：{{ mainPlan.planName }}</p>
              <p class="margin_t_20">
                开始背诵时间：{{ mainPlan.planCreateTime }}
              </p>

              <p class="margin_t_20">每日完成量：{{ mainPlan.dayWord }}</p>
              <div class="flex_center margin_t_20">
                <div class="wid_per50">
                  <el-progress :percentage="percentage"></el-progress>
                </div>
                <p>
                  单词量：
                  <span class="blue font_bold">{{ mainPlan.finishedWord }}</span
                  >/{{ mainPlan.allWord }}
                </p>
              </div>
            </div>
            <div>
              <el-button
                type="success"
                icon="el-icon-edit"
                @click="editMain"
              ></el-button>
              <el-button type="primary" @click="startRecite">
                开始背诵
              </el-button>
            </div></template
          >
        </div>
        <div>
          <p class="font_16 font_bold margin_l_20 margin_t_20">次计划</p>
          <el-table
            :data="planList"
            style="width: 100%"
            :header-cell-style="{ 'text-align': 'center' }"
            :cell-style="{ 'text-align': 'center' }"
          >
            <el-table-column prop="planName" label="计划名"> </el-table-column>
            <el-table-column prop="moduleName" label="单词模块">
            </el-table-column>
            <el-table-column prop="allWord" label="单词总量"> </el-table-column>
            <el-table-column prop="dayWord" label="每日完成量">
            </el-table-column>
            <el-table-column label="操作" width="220px">
              <template slot-scope="scope">
                <el-button
                  type="success"
                  icon="el-icon-edit"
                  @click="$refs.plan.show(scope.row)"
                  size="mini"
                ></el-button>
                <el-button
                  type="danger"
                  icon="el-icon-delete"
                  @click="removePlan(scope.row.planId)"
                  size="mini"
                ></el-button
                ><el-button
                  type="primary"
                  size="mini"
                  :disabled="scope.row.planStatus == '1'"
                  @click="setMain(scope.row.planId)"
                  >{{
                    scope.row.planStatus == "0" ? "设为主计划" : "已完成"
                  }}</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-main>
    <el-aside width="32%">
      <div
        class="border_ccc wid_215 font_14 padding_b_10 margin_l_20 flex_column_center_center"
      >
        <el-avatar
          :size="100"
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        >
        </el-avatar>
        <p class="margin_t_20">{{ userInfo.nickName }}</p>
        <p class="margin_t_b_10">
          积分:<span class="blue font_bold margin_l_10">{{
            userInfo.integration
          }}</span>
        </p>

        <el-popover placement="bottom" width="200" v-model="visible">
          <p>我发誓今天要背完今日份单词！如果做不到我就变猪头！</p>
          <div style="text-align: right; margin: 0">
            <el-button size="mini" type="text" @click="visible = false"
              >取消</el-button
            >
            <el-button type="primary" size="mini" @click="vow">确定</el-button>
          </div>
          <el-button
            slot="reference"
            type="primary"
            round
            :disabled="btnDisable"
            >{{ btnDisable ? "发誓中" : "我要发誓" }}</el-button
          >
        </el-popover>
      </div>
      <div
        class="box-size border_ccc wid_215 font_14 margin_t_20 padding_10 margin_l_20 flex_column_center_center"
      >
        <el-tabs :stretch="true" v-model="activeName">
          <el-tab-pane label="总积分排行" name="1">
            <div class="flex_between_center margin_b_20">
              <span>排名</span><span>用户名</span><span>分数</span>
            </div>
            <div
              v-for="(item, index) in IntegralList"
              :key="item.userId"
              class="flex_between_center margin_b_10"
            >
              <span
                :class="`${
                  index == 0 || index == 1 || index == 2
                    ? 'bg_orange'
                    : 'bg_c1_grey'
                } white span`"
              >
                {{ index + 1 }}
              </span>
              {{ item.nickName }}
              <span>{{ item.integration }}</span>
            </div>
            <el-pagination small layout="prev, pager, next" :total="total1">
            </el-pagination>
          </el-tab-pane>
          <el-tab-pane label="动态" name="2">
            <div
              v-for="item in SwearList"
              :key="item.userId"
              class="flex_center"
            >
              <el-avatar :size="40" :src="item.headImage"></el-avatar>
              <div class="margin_l_20">
                <p class="margin_b_10">{{ item.nickName }}</p>
                <p v-if="item.swearStatus == '1'">已完成，奖励10积分</p>
                <p v-if="item.swearStatus == '0'">未背完，变猪头</p>
              </div>
            </div>
            <el-pagination small layout="prev, pager, next" :total="total2">
            </el-pagination>
          </el-tab-pane>
        </el-tabs>
      </div>
    </el-aside>
    <el-dialog
      title="新增单词本"
      :visible="visibleAdd"
      :close-on-click-modal="false"
      width="40%"
      @close="closedAdd"
    >
      <el-input
        v-model="bookName"
        placeholder="请输入新增单词本名字"
      ></el-input>
      <span slot="footer" class="dialog-footer">
        <el-button @click="closedAdd">取 消</el-button>
        <el-button type="primary" @click="addBook">确 定</el-button>
      </span>
    </el-dialog>
    <PlanView ref="plan" @ok="getPlanList"></PlanView>
    <Login ref="login"></Login>
  </el-container>
</template>

<script>
import { allBook, allPlan, deletePlan, setMain, addBook, hotIntegration, swearSearch, swear } from '@/api/wordList'
import { userInfo } from '@/api/user'
import { avatarUrl } from '@/utils/img.js'
import PlanView from './PlanView.vue'
import Login from './Login.vue'

export default {
  data() {
    return {
      activeName: '1',
      token: '',
      visible: false,
      percentage: 0,
      userName: '测试号1',
      bookList: [],
      bookName: '',
      visibleAdd: false,
      mainPlan: {},
      planList: [],
      userInfo: {},
      IntegralList: [],//积分排行
      queryInfo1: { // 获取积分列表的参数对象   
        pageNum: 1,
        pageSize: 5,
      },
      total1: 0,
      SwearList: [],//发誓列表
      queryInfo2: { // 获取发誓列表的参数对象   
        pageNum: 1,
        pageSize: 5,
      },
      total2: 0,
      btnDisable: false,
    }
  },
  components: { PlanView, Login },
  methods: {
    getBookList() {
      allBook()
        .then((res) => {
          // console.log(res)
          if (res.code == 200) this.bookList = res.data
        })
        .catch((err) => {
          console.log('err', err)
        })
    },

    getPlanList() {
      allPlan()
        .then((res) => {
          // console.log(res)
          if (res.code == 200) {
            this.mainPlan = res.data.mainPlan
            this.planList = res.data.commonPlan
            if (this.mainPlan) this.percentage = (this.mainPlan.finishedWord * 100) / this.mainPlan.allWord - 0
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },

    getUserInfo() {
      userInfo()
        .then((res) => {
          // console.log(res)
          if (res.code == 200) {

          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },

    getIntegralList() {
      hotIntegration(this.queryInfo1)
        .then((res) => {
          // console.log(res)
          if (res.code == 200) {
            this.IntegralList = res.data.records
            this.total1 = res.data.total
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },

    getSwearList() {
      swearSearch(this.queryInfo2)
        .then((res) => {
          // console.log(res)
          if (res.code == 200) {
            this.SwearList = this.trunImg(res.data.records)
            this.total2 = res.data.total
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },

    trunImg(List) {
      let list = List
      list.forEach((item, index) => {
        list[index].headImage = avatarUrl(item.userId)
      })
      return list
    },

    removePlan(planId) {
      this.$confirm("确认删除吗？", {
        cancelButtonText: "取消",
        confirmButtonText: "确认",
        showClose: true,
        type: "warning",
      })
        .then((res) => {
          deletePlan(planId)
            .then((res) => {
              if (res.code == 200) {
                this.$message.success('删除成功！')
                this.getPlanList()
              }
            })
        })
    },

    setMain(planId) {
      const data = {
        old: this.mainPlan.planId,
        new: planId
      }
      setMain(data)
        .then((res) => {
          if (res.code == 200) this.getPlanList()
          // console.log('res', res)
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    showAddBook() {
      if (this.token) this.visibleAdd = true
      else this.$refs.login.showLogin()
    },
    addBook() {
      const bookName = this.bookName
      addBook({ bookName })
        .then((res) => {
          // console.log(res)
          if (res.code == 200) {
            this.visibleAdd = false
            this.$message.success('新增成功！')
            this.getBookList()
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    closedAdd() {
      this.visibleAdd = false
      this.bookName = ''
    },

    editMain() {
      if (this.token) this.$refs.plan.show(this.mainPlan)
      else this.$refs.login.showLogin()
    },

    startRecite() {
      if (this.token) this.$router.push({
        name: 'word',
        params: { userId: this.userInfo.userId }
      })
      else this.$refs.login.showLogin()
    },

    goTo(bookId) {
      this.$router.push({
        name: 'bookInfo',
        params: { bookId }
      })
    },
    // 发誓
    vow() {
      this.visible = false
      if (this.token) {
        this.btnDisable = true
        swear()
          .then((res) => {
            // console.log('res', res)
            if (res.code == 200) {
              this.userInfo.swear = true
              window.sessionStorage.setItem('userInfo', JSON.stringify(this.userInfo))
              this.$message.success('发誓成功！')
            }
          })
          .catch((err) => {
            console.log('err', err)
          })
      } else this.$refs.login.showLogin()

    },
    init() {
      this.getBookList()
      this.getPlanList()
      // this.getIntegralList()
      // this.getSwearList()
      const userInfo = JSON.parse(window.sessionStorage.getItem('userInfo'))
      console.log('userInfo', userInfo)
      if (userInfo) {
        this.userInfo = userInfo
        this.btnDisable = userInfo.swear
      }
    }
  },
  created() {
    const token = window.sessionStorage.getItem('token')
    this.token = token
    this.getIntegralList()
    this.getSwearList()
    if (token) this.init()
  },
  mounted() {
    this.$bus.$on('beLogin', this.init)
    // console.log('token', this.token)
    if (!this.token) this.$refs.login.showLogin()
  },
  beforeDestroy() {
    this.$bus.$off('beLogin')
  }

}
</script>

<style scoped>
.el-tabs {
  width: 100%;
}
.span {
  width: 16px;
  height: 16px;
  display: block;
  line-height: 16px;
  font-weight: bold;
  text-align: center;
  font-size: 12px;
  margin-right: 10px;
}

.main {
  border-left: 1px solid #ccc;
  border-right: 1px solid #ccc;
  color: #333;
}
.main h2 {
  font-size: 18px;
  margin: 0 0 10px;
  padding-top: 10px;
}

.main h2 i {
  display: block;
  float: left;
  height: 16px;
  width: 5px;
  background: #3b93ee;
  margin-right: 5px;
}

.li {
  width: 140px;
  height: 86px;
  border-radius: 10px;
  box-shadow: 0 9px 9px rgba(0, 0, 0, 0.25), 0 4px 12px rgba(0, 0, 0, 0.22);
  margin-right: 25px;
  margin-bottom: 20px;
  /* position: relative; */
  /* z-index: 1; */
  /* text-align: center; */
  /* float: left; */
  cursor: pointer;
}
</style>