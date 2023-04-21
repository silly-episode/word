<template>
  <div class="bg_fa hei_per100">
    <img class="wordLog" src="@/assets/wordLog.png" alt="" />
    <div class="bg_white card recordCard">
      <el-row :gutter="4" type="flex" align="middle">
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
    <div class="QA flex_center_center">
      <div></div>
    </div>
  </div>
</template>

<script>
import Timer from '@/components/Timer.vue'
export default {
  components: { Timer },
  data() {
    return {
      isStart: false,
      complete: false,
      List: []
    }
  },
  methods: {
    getList() {
      const List=[
        {}
      ]
    },

    Start() {
      if (this.complete) return;
      this.isStart = !this.isStart
      // if (this.isStart) this.playWord()
    },
    skip() { }
  },
  created() {
    console.log(this.$route.params)
  }
}

</script>

<style scoped>
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
  top: 40%;
  width: 100%;
}
</style>