<template>
  <el-dialog
    :title="`${editFlag ? '修改计划' : '设置计划'}`"
    :visible="visiblePlan"
    :close-on-click-modal="false"
    width="40%"
    @close="closedPlan"
  >
    <el-input v-model="planName" placeholder="请填写计划名称"></el-input>
    <div class="flex_center margin_t_20">
      <el-select
        v-model="dayWord"
        placeholder="请计划每天完成的单词数"
        @change="changeOptions"
      >
        <el-option
          v-for="item in options"
          :key="item"
          :label="item"
          :value="item"
        >
        </el-option>
      </el-select>
      <span class="margin_l_20">{{ days }}</span>
    </div>

    <span slot="footer" class="dialog-footer">
      <el-button @click="closedPlan">取 消</el-button>
      <el-button type="primary" @click="addPlan">确 定</el-button>
    </span>
  </el-dialog>
</template>

<script>
import { addPlan } from '@/api/wordList'
export default {
  name: 'PlanView',
  data() {
    return {
      editFlag: false,
      visiblePlan: false,
      moduleId: '',
      planId: '',
      wordCount: '',
      options: [],
      planName: '',
      dayWord: '',
      days: '',
    }
  },
  methods: {
    show(data) {
      this.visiblePlan = true
      this.wordCount = data.wordCount
      this.moduleId = data.moduleId
      if (data.planId) {
        this.editFlag = true
        this.planId = data.planId
        this.wordCount = data.allWord
        this.planName = data.planName
        this.dayWord = data.dayWord
        // console.log(data)
      }
      for (let i = 1; i < this.wordCount + 1; i++) {
        this.options.push(i)
      }
    },

    changeOptions() {
      let days = (this.wordCount / this.dayWord).toFixed(0)
      if (this.wordCount % this.dayWord) days++
      this.days = '预计' + days + '天完成'
    },

    addPlan() {
      const data = {
        planId: this.planId,
        planName: this.planName,
        moduleId: this.moduleId,
        allWord: this.wordCount,
        dayWord: this.dayWord
      }
      // console.log('计划信息', data)
      addPlan(data)
        .then((res) => {
          // console.log(res)
          if (res.code == 200) {
            this.closedPlan()
            this.$emit('ok')
            this.$message.success(`${this.editFlag ? '修改' : '加入'}成功！`)
          }

        })
        .catch((err) => {
          console.log('err', err)
        })
    },

    closedPlan() {
      this.visiblePlan = false
      this.planName = ''
      this.dayWord = ''
      this.days = ''
      this.planId = ''
      this.options = []
    },
  }
}
</script>

<style>
</style>