<template>
  <div>
    <img class="wordLog" src="@/assets/wordLog.png" alt="" />
    <div :class="`${isDeep ? 'bg_drak grey' : 'bg_white'} card onOffCard`">
      <el-row :gutter="2" type="flex" align="middle">
        <el-col :span="8"
          ><span>{{ title }}</span></el-col
        >
        <el-col :span="5">
          <el-select v-model="value" style="width: 90px">
            <el-option label="美音" value="0"></el-option>
            <el-option label="英音" value="1"></el-option>
          </el-select>
        </el-col>
        <!-- 循环播放单词发音 -->
        <el-col :span="2">
          <el-tooltip effect="dark" content="循环播放单词发音" placement="top">
            <span
              :class="`${
                isCirculate ? 'purple' : 'deep_grey'
              } iconfont icon-xunhuan font_26 `"
              @click="circulate"
            ></span>
          </el-tooltip>
        </el-col>
        <!-- 开关键盘声音 -->
        <el-col :span="2">
          <el-tooltip effect="dark" content="开关键盘声音" placement="top"
            ><span
              :class="`font_26 iconfont icon-${
                isKeyboard ? 'xiaolaba purple' : 'jingyin deep_grey'
              }`"
              @click="isKeyboard = !isKeyboard"
            ></span
          ></el-tooltip>
        </el-col>
        <!-- 开关英文 -->
        <el-col :span="2">
          <el-tooltip effect="dark" content="开关英文" placement="top">
            <span
              :class="`font_26 iconfont icon-${
                showEng ? 'yanjing purple' : 'biyan deep_grey'
              }`"
              @click="en"
            ></span>
          </el-tooltip>
        </el-col>
        <!-- 开关中文 -->
        <el-col :span="2">
          <el-tooltip effect="dark" content="开关中文" placement="top">
            <span
              :class="`${
                showZh ? 'purple' : 'deep_grey'
              } font_26 iconfont icon-wangluo`"
              @click="showZh = !showZh"
            ></span>
          </el-tooltip>
        </el-col>
        <!-- 开关深色模式 -->
        <el-col :span="2">
          <el-tooltip effect="dark" content="开关深色模式" placement="top">
            <span
              :class="`font_26 purple iconfont icon-${isDeep ? 'moon' : 'sun'}`"
              @click="deep"
            ></span>
          </el-tooltip>
        </el-col>
        <el-col :span="4">
          <div
            @click="Start"
            :class="`white ${
              isStart ? 'bg_c1_grey' : 'bg_purple'
            }  radius_10 line_hei_50 text_center pointer margin_r_10`"
          >
            {{ isStart ? "Pause" : "Start" }}
          </div>
        </el-col>
        <el-col :span="4">
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
    <div v-if="complete" class="complete">
      <div class="font_60">已完成！</div>
      <div class="hei_100"></div>
      <div class="flex_between_center font_22">
        <div @click="again" class="btn">Again</div>
        <div @click="back" class="btn">Back</div>
      </div>
    </div>
    <div v-else>
      <div v-if="isStart">
        <div
          :class="`${shake ? 'shake' : ''} word flex_center_center fontWidth`"
          :style="`left: calc(50% - ${wordlength * 18}px)`"
        >
          <div
            class="wid_36 margin_r_5 text_center"
            v-for="(item, index) in wordArr"
            :key="index"
          >
            <div v-show="rwList[index] == 1">{{ item }}</div>
            <div v-show="rwList[index] == 2" class="red">{{ item }}</div>
            <div v-show="rwList[index] == 0" class="green">{{ item }}</div>
            <div v-show="rwList[index] == -1" class="lineBottom"></div>
            <div
              v-show="rwList[index] == -2"
              class="lineBottom redBottom"
            ></div>
          </div>
        </div>
        <div
          v-show="showZh"
          class="wordZh"
          :style="`left: calc(50% - ${sentenceLength * 5.4}px)`"
        >
          <div class="margin_b_15">例句：{{ sentenceEn }}</div>
          <div class="margin_b_15">例句释义：{{ sentenceZh }}</div>
          <span class="margin_r_10">{{ pos }}</span>
          <span>{{ trans }}</span>
        </div>
      </div>
      <div v-else class="tip">按任意键开始</div>
    </div>

    <div
      :class="`${
        isDeep ? 'bg_drak grey' : 'bg_white'
      } card recordCard flex_center`"
    >
      <div class="flex-1 flex_column_center_center">
        <span class="numCss"><Timer :flag="isStart" ref="jishi"></Timer></span>
        <span class="textCss">时间</span>
      </div>
      <div
        v-for="item in cols"
        :key="item.lable"
        class="flex-1 flex_column_center_center"
      >
        <div class="numCss">{{ info[item.props] }}</div>
        <span class="textCss">{{ item.lable }}</span>
      </div>
    </div>
  </div>
</template>
<script>
import Timer from '@/components/Timer.vue'
import { getWord } from '@/api/word.js'
export default {
  name: "WordView",
  components: { Timer },
  data() {
    return {
      complete: false,//二十个单词都完成了
      wordIndex: 0,
      title: '',
      value: '英音',
      shake: false,//是否显示错误抖动类名
      List: [],//二十个单词对象列表
      wordlength: 0,//单词长度
      sentenceLength: 0,
      wordArr: [],
      rwList: [],
      inputWord: [],
      sentenceEn: '',
      sentenceZh: '',
      pos: '',
      trans: '',
      isCirculate: false,//循环播放
      isKeyboard: true,//键盘声
      showEng: true,//显示英文
      showZh: true,//显示中文
      isDeep: false,//深色模式
      isStart: false,//是否开始
      info: {
        inputNum: 0,
        velocity: 0,
        correctNum: 0,
        correctRate: 0,
      },
      cols: [{ lable: '输入数', props: 'inputNum' },
      { lable: '速度(字/秒)', props: 'velocity' },
      { lable: '正确数', props: 'correctNum' },
      { lable: '正确率', props: 'correctRate' },],

      audio: new Audio(),// 实例化一个音频播放器对象 音效音频
      audioWord: new Audio(),//单词音频
      beepUrl: require("@/assets/tipMusic/beep.wav"),
      clickUrl: require("@/assets/tipMusic/click.wav"),
      hintUrl: require("@/assets/tipMusic/hint.wav"),
    }
  },
  methods: {
    getList() {
      getWord({ userId: 1 })
        .then((res) => {
          // console.log('res', res.data.word)
          if (res.code == 200) {
            this.List = res.data.word
            this.title = res.data.wordPlan.moduleName
            this.getWord(res.data.word[0].content.word)
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },

    getWord(data) {
      // console.log(data)
      this.wordArr = data.wordHead.split('')
      this.sentenceEn = data.content.sentence.sentences[0].sContent
      this.sentenceLength = this.sentenceEn.length
      this.sentenceZh = data.content.sentence.sentences[0].sCn
      this.trans = data.content.trans[0].tranCn
      this.pos = data.content.trans[0].pos

      this.wordlength = this.wordArr.length
      for (let i = 0; i < this.wordlength; i++) {
        if (this.showEng) this.rwList[i] = 1
        else this.rwList[i] = -1
      }
      this.playWord()
    },

    // 键盘事件
    keyDown(e) {
      if (this.isStart) {
        this.info.inputNum++;
        let ss = this.trunTime(this.$refs.jishi.content)
        if (ss > 0) this.info.velocity = (this.info.inputNum / ss).toFixed(2);

        this.inputWord.push(e.key)
        const inputLength = this.inputWord.length
        if (this.wordArr[inputLength - 1] === e.key) {
          this.$set(this.rwList, inputLength - 1, 0)
          // this.rwList[inputLength - 1] = 0
          this.info.correctNum++;
          if (this.isKeyboard) {
            this.audio.autoplay = true;
            this.audio.src = this.clickUrl;
          }

        }
        else {
          if (this.showEng) this.$set(this.rwList, inputLength - 1, 2)
          else this.$set(this.rwList, inputLength - 1, -2)
          this.audio.autoplay = true;
          this.audio.src = this.beepUrl;
          this.hasWrong()
        }
        this.info.correctRate = (this.info.correctNum / this.info.inputNum).toFixed(2)
        if (inputLength >= this.wordlength) this.finish()
      } else this.Start()
    },

    // 填错字母后：抖动、清空输入内容
    hasWrong() {
      // console.log('单词输错了')
      this.shake = true
      setTimeout(() => {
        this.playWord()
        // console.log('进入了定时器,关闭抖动')
        this.shake = false
        this.inputWord = []
        if (this.showEng) {
          for (let i = 0; i < this.wordlength; i++) {
            this.rwList[i] = 1
          }
        } else {
          for (let i = 0; i < this.wordlength; i++) {
            this.rwList[i] = -1
          }
        }
      }, 300)
    },

    // 播放单词音频
    playWord() {
      this.audioWord.autoplay = true;
      this.audioWord.loop = this.isCirculate;
      let type = this.value
      if (type == '英音') type = 1
      let word = this.wordArr.join('')
      this.audioWord.src = `https://dict.youdao.com/dictvoice?type=${type}&audio=${word}`;
    },

    // 本页单词完成后
    finish() {
      this.audio.src = this.hintUrl;
      this.skip()
      // console.log('完成了', this.inputWord)
    },

    // 开始
    Start() {
      if (this.complete) return;
      this.isStart = !this.isStart
      if (this.isStart) this.playWord()
    },

    // 跳过
    skip() {
      if (this.complete) return;
      this.wordArr = []
      this.inputWord = []
      this.sentenceEn = ''
      this.sentenceZh = ''
      this.pos = ''
      this.trans = ''
      this.wordIndex++
      if (this.wordIndex < this.List.length)
        this.getWord(this.List[this.wordIndex].content.word)
      else {
        this.Start()
        this.complete = true
      }
    },

    // 再练一次
    again() {
      this.complete = false
      this.wordIndex = 0
      this.getWord(this.List[0].content.word)
      this.Start()
    },

    back() {
      console.log('返回')
    },

    // 循环播放
    circulate() {
      this.isCirculate = !this.isCirculate
      this.audioWord.loop = this.isCirculate;
    },
    // 深色模式
    deep() {
      this.isDeep = !this.isDeep
      if (this.isDeep) this.bgDark()
      else this.bgWhite()
    },
    // 英文显示
    en() {
      this.showEng = !this.showEng
      for (let i = 0; i < this.wordlength; i++) {
        if (this.rwList[i] == 0) continue;//已经有正确字母了就保存
        if (this.showEng) this.rwList[i] = 1
        else this.rwList[i] = -1
      }
    },

    // 时间转换
    trunTime(str) {
      let arr = str.split(':')
      arr[0] = arr[0] - 0
      arr[1] = arr[1] - 0
      return arr[0] * 60 + arr[1]
    },

    bgWhite() {
      document.querySelector('body').setAttribute('style', 'background:#faf9ff')
    },

    bgDark() {
      document.querySelector('body').setAttribute('style', 'background:#111726')
    }
  },
  created() {
    const { params } = this.$route
    if (params) {
      if (params.num && params.moduleId) this.getList(params)
      else if (params.userId) this.getList(params.userId)
      else if (params.bookId) this.getList(params.bookId)
    }

    // this.getList()
    // 显示绿色字、  黑色字、   红色字、   底部线黑色、  底部线红色
    //     答对后、  显 未答、  显 答错 、  不显未答、  不显答错
    //        0  、  1      、   2     、    -1   、    -2
  },
  mounted() {
    // 绑定监听事件
    window.addEventListener("keydown", this.keyDown);
    // if (isStart && !isFinish) {
    //   window.addEventListener('keydown', onKeydown)
    // }
  },
  beforeCreate() {
    document.querySelector('body').setAttribute('style', 'background:#faf9ff')
  },
  beforeDestroy() {
    window.removeEventListener('keydown')
  },

};
</script>

<style scoped>
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

.onOffCard {
  position: absolute;
  left: 40%;
  top: 5%;
  width: 50%;
  padding: 20px;
  font-size: 22px;
}

.recordCard {
  position: absolute;
  left: 26%;
  top: 68%;
  width: 48%;
  padding: 50px 16px;
}

.word {
  position: absolute;
  left: 38%;
  top: 35%;
  font-size: 60px;
  color: #4b5563;
  letter-spacing: 12px;
}

.tip {
  position: absolute;
  left: 45%;
  top: 35%;
  font-size: 30px;
  color: #545d6b;
}

.lineBottom {
  margin-top: 70px;
  height: 0.5px;
  border-bottom: 5px solid #4b5563;
}

.redBottom {
  border-bottom: 5px solid #dc2626;
}

.wordZh {
  text-align: center;
  position: absolute;
  /* left: 30%; */
  top: 49%;
  font-size: 22px;
  color: #4b5563;
}

.btn {
  color: white;
  background-color: #818cf8;
  border-radius: 10px;
  line-height: 50px;
  text-align: center;
  cursor: pointer;
  padding: 1px 30px;
}

.complete {
  position: absolute;
  left: 34%;
  top: 30%;
  width: 470px;
  text-align: center;
  color: #4b5563;
}

.shake {
  border-color: red;
  animation: shake 800ms ease-in-out;
}
@keyframes shake {
  /* 动画需：“对称”实现 */
  10%,
  90% {
    transform: translate3d(-1px, 0, 0);
  }
  20%,
  80% {
    transform: translate3d(+2px, 0, 0);
  }
  30%,
  70% {
    transform: translate3d(-4px, 0, 0);
  }
  40%,
  60% {
    transform: translate3d(+4px, 0, 0);
  }
  50% {
    transform: translate3d(-4px, 0, 0);
  }
}

.fontWidth {
  font-family: "Lucida Console";
}
</style>