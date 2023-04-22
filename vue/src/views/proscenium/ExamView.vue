<template>
  <div class="bg_fa hei_per100">
    <img v-if="!complete" class="wordLog" src="@/assets/wordLog.png" alt="" />
    <div v-if="!complete" class="bg_white card recordCard">
      <el-row :gutter="4" type="flex" align="middle">
        <el-col :span="4" class="text_center"
          >{{ count }}/{{ examList.length }}
        </el-col>
        <el-col :span="8">
          <div class="flex-1 flex_column_center_center">
            <span class="numCss"
              ><Timer :flag="isStart" ref="jishi"></Timer
            ></span>
            <span class="textCss">时间</span>
          </div>
        </el-col>
        <el-col :span="6">
          <div
            @click="Start"
            :class="`white ${
              isStart ? 'bg_c1_grey' : 'bg_purple'
            } radius_10 line_hei_50 text_center pointer margin_r_20`"
          >
            {{ isStart ? "Pause" : "Start" }}
          </div>
        </el-col>
        <el-col :span="6">
          <div
            @click="skip"
            :class="`white radius_10 line_hei_50 text_center pointer ${
              complete ? 'bg_c1_grey' : 'bg_orange'
            }`"
          >
            Skip
          </div>
        </el-col>
      </el-row>
    </div>
    <div v-if="complete" class="complete flex_column_center_center">
      <div class="font_40 margin_b_50">最终得分：{{ sumScore }}</div>
      <div class="flex_between_center font_30 wid_per40 margin_b_50">
        <div>练习阶段分数：{{ (handSum * 100) / examList.length }}</div>
        <div>考试阶段分数：{{ parseInt((count * 100) / examList.length) }}</div>
      </div>
      <div v-if="sumScore > 80" class="font_30">
        <p class="margin_b_50 text_center">恭喜完成！</p>
        <el-button type="primary">
          <router-link to="/wordlist">回首页</router-link>
        </el-button>
      </div>
      <div v-else class="font_30">
        <p class="margin_b_50">成绩未达标，请再次练习</p>
        <div class="flex_between_center wid_500">
          <el-button type="primary">
            ><router-link to="/word">再练一次</router-link>
          </el-button>
          <el-button type="primary">
            ><router-link to="/wordlist">回首页</router-link>
          </el-button>
        </div>
      </div>
    </div>
    <div v-else>
      <div v-if="isStart" class="QA flex_column_center_center">
        <div class="font_60 margin_b_30 letter-spacing10 fontWidth">
          {{ Qword }}
        </div>
        <div
          v-for="(item, index) in choice"
          :key="item.headWord"
          @click="selectAns(index)"
          :class="`bg_${
            ansList[itemIndex] == -1
              ? 'white'
              : index == answerIndex
              ? 'green'
              : itemList[index] == -1
              ? 'red'
              : 'white'
          } ${
            ansList[itemIndex] != index ? 'font_22' : 'font_bold font_30'
          } pointer card text_center wid_500 margin_b_30 radius_20 padding_30 `"
        >
          {{ options[index] }}. {{ item.content.word.content.trans[0].tranCn }}
        </div>
      </div>
      <div v-else class="QA flex_center_center font_30">点击开始键</div>
    </div>
  </div>
</template>

<script>
import Timer from '@/components/Timer.vue'
import { exam,examResult, planScore } from '@/api/word'

export default {
  components: { Timer },
  data() {
    return {
      itemIndex: 0,//当前单词索引值
      isStart: false,
      complete: false,
      sumScore: 0,//总分
      handSum: 0,//第二阶段做对数
      count: 0,//第三阶段做对数
      wordPlan: {},
      examList: [],
      Qword: '',
      choice: [],//四个选项
      ansList: [],
      answerIndex: -1,
      options: ['A', 'B', 'C', 'D'],
      itemList: [0, 0, 0, 0],//四选项状态
    }
  },
  methods: {
    getExamList() {
      exam(this.wordPlan)
        .then(res => {
          // console.log('res', res)
          if (res.code == 200) {
            this.examList = res.data
            res.data.forEach(() => {
              this.ansList.push(-1)
            })
            this.getItem(this.examList[this.itemIndex])
          }
        })
    },
    getItem(data) {
      console.log('ansList', this.ansList)
      this.Qword = data.choice[data.answer].headWord
      this.answerIndex = data.answer
      this.choice = data.choice
    },

    // 选择答案
    selectAns(index) {
      if (this.itemList[index] != 0) return
      this.itemList = [-2, -2, -2, -2]
      const { itemIndex } = this
      if (index == this.answerIndex) {
        // console.log('对了')
        this.$set(this.ansList, itemIndex, index)
        this.$set(this.itemList, index, 1)
        this.count++
      } else {
        // console.log('错了')
        this.$set(this.ansList, itemIndex, index)
        this.$set(this.itemList, index, -1)
      }
      console.log(this.ansList)
    },
    Start() {
      if (this.complete) return;
      this.isStart = !this.isStart
      // if (this.isStart) this.playWord()
    },
    skip() {
      if (this.complete) return;
      this.Qword = ''
      this.choice = []
      this.answerIndex = -1
      this.itemList = [0, 0, 0, 0]//四选项状态
      this.itemIndex++
      if (this.itemIndex < this.examList.length)
        this.getItem(this.examList[this.itemIndex])
      else this.completefn()
    },

    completefn() {
      this.isStart = false
      this.sumScore = parseInt(((this.handSum + this.count) * 50) / this.examList.length)
      const data = {
        grade: this.sumScore,
        planId: this.wordPlan.planId,
        usageTime: ''
      }
      examResult(data)
        .then(res => {
          console.log('发送成绩结果', res)
        })

      if (this.sumScore > 80) {
        planScore(data.planId)
          .then(res => {
            console.log('res', res)
          })
      }
      this.complete = true
    }
  },
  created() {
    if (this.$route.params) {
      this.handSum = this.$route.params.handSum
      this.wordPlan = this.$route.params.wordPlan
      window.sessionStorage.setItem('examInfo', JSON.stringify(this.$route.params))
      this.getExamList()
    } else {
      const examInfo = JSON.parse(window.sessionStorage.getItem('examInfo'))
      this.handSum = examInfo.handSum
      this.wordPlan = examInfo.wordPlan
      this.getExamList()
    }
  }
}

</script>

<style scoped>
.complete {
  position: absolute;
  top: 30%;
  width: 100%;
}
.card {
  border-radius: 10px;
  box-shadow: 0 100px 80px #00000012, 0 41.7776px 33.4221px #0000000d,
    0 22.3363px 17.869px #0000000b, 0 12.5216px 10.0172px #00000009,
    0 6.6501px 5.32008px #00000007, 0 2.76726px 2.21381px #00000005;
}

.wordLog {
  position: absolute;
  left: 8%;
  top: 7%;
  width: 380px;
}

.recordCard {
  position: absolute;
  right: 10%;
  top: 5%;
  width: 30%;
  padding: 20px;
  border-radius: 10px;
  font-size: 22px;

  box-shadow: 0 100px 80px #00000012, 0 41.7776px 33.4221px #0000000d,
    0 22.3363px 17.869px #0000000b, 0 12.5216px 10.0172px #00000009,
    0 6.6501px 5.32008px #00000007, 0 2.76726px 2.21381px #00000005;
}

.numCss {
  transition-duration: 0.3s;
  text-align: center;
  width: 70%;
  font-weight: 700;
  font-size: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e5e7eb;
}

.textCss {
  padding-top: 8px;
  font-size: 16px;
  transition-duration: 0.3s;
}

.QA {
  position: absolute;
  top: 30%;
  width: 100%;
}
</style>