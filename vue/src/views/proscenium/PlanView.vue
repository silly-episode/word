<template>
  <el-dialog
      :center="true"
      :visible="visiblePlan"
      :close-on-click-modal="false"
      :title="`${editFlag ? '修改单词计划' : '添加单词计划'}`"
      width="40%"
      @close="closedPlan">

    <el-form
        ref="formRef"
        label-width="120px"
        style="margin: 0 7% 0 0;">
      <el-row>
        <el-form-item label="计划名称" prop="signature">
          <el-input
              v-model="planName"
              maxlength="8"
              placeholder="请填写计划名称"
              show-word-limit
              type="text">
          </el-input>
        </el-form-item>
      </el-row>

      <el-row>
        <el-col :span="12">
          <el-form-item aria-rowindex="50px" label="单词数量" prop="userId">
            <el-select
                v-model="dayWord"
                @change="changeOptions">
              <el-option
                  v-for="item in options"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>

        <el-col :span="12">
          <el-form-item label="完成天数" prop="registerTime">
            <el-select
                v-model="days"
                @change="changeDayOptions">
              <el-option
                  v-for="item in dayOptions"
                  :key="item"
                  :label="item"
                  :value="item">
              </el-option>
            </el-select>
          </el-form-item>
        </el-col>
      </el-row>
      <el-row class="flex_center_center">
        <el-button @click="closedPlan">取 消</el-button>
        <el-button type="primary" @click="addPlan">确 定</el-button>
      </el-row>

    </el-form>

  </el-dialog>
</template>

<script>
import {addPlan} from '@/api/wordList'

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
      dayOptions: [],
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
        let days = parseInt(this.wordCount / i)
        if (this.wordCount % this.dayWord) days++
        this.dayOptions.push(days)
      }
      this.dayOptions = this.dayOptions.filter((item, index, arr) => arr.indexOf(item) === index);
    },

    changeOptions() {
      let days = parseInt(this.wordCount / this.dayWord)
      if (this.wordCount % this.dayWord) days++
      this.days = days
    },

    changeDayOptions() {
      let dayWord = Math.ceil((this.wordCount / this.days));
      if (this.wordCount % this.days) {
        this.dayWord++
      }
      this.dayWord = dayWord
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
          if (res.code === 200) {
            this.closedPlan()
            this.$emit('ok')
            this.$notify.success({
              title: '成功',
              message: `${this.editFlag ? '修改' : '加入'}成功！`,
              offset: 60
            });
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
      this.dayOptions = []
    },
  }
}
</script>

<style scoped>

.el-dialog__body {
  padding-bottom: 0;
}

</style>
