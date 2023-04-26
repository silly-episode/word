<template>
  <div>
    <el-main class="margin_0_120">
      <div class="flex_between_center padding_0_50">
        <el-image :src="path" class="radius_10 wid_300 hei_200"></el-image>
        <div
          class="wid_per60 bg_f9 hei_200 radius_10 padding_30 box-size font_18"
        >
          <div class="flex_center">
            <div class="flex_wrap_center">
              <span class="margin_r_30 line_hei_40"
                >模块名称：{{ moduleInfo.moduleName }}</span
              >
              <span class="margin_r_30 line_hei_40"
                >学习人数：{{ moduleInfo.studyNumber }}</span
              >
              <span class="margin_r_30 line_hei_40"
                >单词数量：{{ moduleInfo.wordCount }}</span
              >
              <span class="margin_r_30 line_hei_40"
                >创建时间：{{ moduleInfo.wordModuleCreateTime }}</span
              >
            </div>
            <el-button type="primary" :disabled="planExist" @click="joinPlan"
              >加入单词计划</el-button
            >
          </div>
          <div class="flex_center">
            <span class="margin_15_0 line_hei_40"
              >说明：{{ moduleInfo.remark }}</span
            >
          </div>
        </div>
      </div>
      <el-divider>Chapter</el-divider>
      <div class="margin_t_30 flex_wrap_center radius_10 padding_30 box-size">
        <div
          class="pointer wid_100 hei_80 text_center bg_baby_purple margin_r_40 margin_b_30 radius_10"
          v-for="item in sum"
          :key="item"
          @click="goTo(item)"
        >
          <p class="font_18 font_bold margin_t_20 purple">Chapter{{ item }}</p>
          <p class="font_16 margin_t_10 purple">20词</p>
        </div>

        <div
          class="pointer wid_100 hei_80 text_center bg_baby_purple margin_r_40 margin_b_30 radius_10"
          v-if="remainder"
          @click="goTo(sum + 1)"
        >
          <p class="font_18 font_bold margin_t_20 purple">
            Chapter{{ sum + 1 }}
          </p>
          <p class="font_16 margin_t_10 purple">{{ remainder }}词</p>
        </div>
      </div>
    </el-main>
    <PlanView ref="plan"></PlanView>
    <Login ref="login"></Login>
  </div>
</template>

<script>
import { wordModuleById } from '@/api/wordList'
import { imageUrl } from '@/utils/img.js'
import PlanView from './PlanView.vue'
import Login from './Login.vue'
export default {
  name: 'WordModule',
  data() {
    return {
      token: '',
      path: '',
      planExist: false,
      moduleId: '',
      moduleInfo: {},
      sum: 0,
      remainder: 0
    }
  },
  components: { PlanView, Login },
  methods: {
    getWordModule() {
      wordModuleById(this.moduleId)
        .then((res) => {
          // console.log('单词模块信息',res)
          if (res.code == 200) {
            this.planExist = res.data.planExist
            this.moduleInfo = res.data.wordModule
            this.path = imageUrl(this.moduleId)
            this.sum = parseInt(res.data.wordModule.wordCount / 20)
            this.remainder = res.data.wordModule.wordCount % 20
          }
        })
        .catch((err) => {
          console.log(err)
        })
    },

    joinPlan() {
      if (!this.token) this.$refs.login.showLogin()
      else {
        this.$refs.plan.show({ wordCount: this.moduleInfo.wordCount, moduleId: this.moduleId })
        this.planExist = true
      }
    },

    goTo(num) {
      this.$router.push({
        name: 'word',
        params: { num, moduleId: this.moduleId, max: this.sum + 1 }
      })
    },

  },
  created() {
    this.$bus.$off('module').$on('module', () => {
      console.log('单词模块')
      this.getWordModule()
    })
    this.token = window.sessionStorage.getItem('token')
    if (this.$route.params.moduleId) {
      window.sessionStorage.setItem('moduleId', this.$route.params.moduleId)
      this.moduleId = this.$route.params.moduleId
      this.getWordModule()
    } else {
      const moduleId = window.sessionStorage.getItem('moduleId')
      this.moduleId = moduleId
      this.getWordModule()
    }
  },
}
</script>

<style>
</style>