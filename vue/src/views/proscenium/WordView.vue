<template>
  <div :class="`${isDeep ? 'bg_dk' : 'bg_fa'} hei_per100`">
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
          <el-tooltip effect="dark" content="开关例句" placement="top">
            <span
              :class="`${
                showZh ? 'purple' : 'deep_grey'
              } font_26 iconfont icon-wangluo`"
              @click="zh"
            ></span>
          </el-tooltip>
        </el-col>
        <!-- 开关深色模式 -->
        <el-col :span="2">
          <el-tooltip effect="dark" content="开关深色模式" placement="top">
            <span
              :class="`font_26 purple iconfont icon-${isDeep ? 'moon' : 'sun'}`"
              @click="isDeep = !isDeep"
            ></span>
          </el-tooltip>
        </el-col>
        <el-col :span="4">
          <div
            @click="Start"
            :class="`white ${
              isStart ? 'bg_c1_grey' : 'bg_purple'
            } radius_10 line_hei_50 text_center pointer margin_r_10`"
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
      <div class="font_40">
        {{ enterFlag == 1 ? (stage ? "第一阶段" : "第二阶段") : "" }}已完成！
      </div>
      <div class="hei_100"></div>
      <div :class="`flex_${stage ? 'between' : 'center'}_center font_22`">
        <div v-if="stage" @click="again" class="btn">Again</div>
        <div @click="next" class="btn">{{ isBack ? "Back" : "Next" }}</div>
      </div>
    </div>
    <div v-else>
      <div v-if="isStart">
        <div
          :class="`${shake ? 'shake' : ''} word flex_center_center fontWidth`"
        >
          <!--          :style="`left: calc(50% - ${wordlength * 18}px)`"-->
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
          <el-popover placement="right" width="240" v-model="visible">
            <p class="font_bold font_16 margin_b_10 text_center">
              请选择单词本
            </p>
            <template v-if="bookList.length > 0">
              <el-checkbox-group v-model="checkList" class="padding_10">
                <el-checkbox
                  v-for="item in bookList"
                  :key="item.bookId"
                  :label="item.bookId"
                  class="margin_b_10 block"
                  >{{ item.bookName }}</el-checkbox
                >
              </el-checkbox-group>
            </template>
            <div class="text_center margin_t_b_10" v-else>暂无单词本</div>

            <div style="text-align: right; margin: 0">
              <el-button size="mini" type="text" @click="visible = false"
                >取消</el-button
              >
              <el-button type="primary" size="mini" @click="collectWord"
                >确定</el-button
              >
            </div>
            <i
              v-if="stage"
              slot="reference"
              :class="`el-icon-star-${
                collectList[wordIndex] ? 'on orange' : 'off grey'
              } font_30 margin_l_30`"
            ></i>
          </el-popover>
        </div>
        <div class="wordZh flex_column_center_center">
          <!--          :style="`left: calc(50% - ${sentenceLength * 5.4}px)`"-->
          <div>
            <span class="margin_r_20">词性：{{ pos }}</span>
            <span>词意：{{ trans }}</span>
          </div>

          <div v-show="showZh" class="margin_t_10">
            <div class="margin_b_15">例句释义：{{ sentenceZh }}</div>
            <div>例句：{{ sentenceEn }}</div>
          </div>
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
import { collectWord, getWordByNum, getWordByUserId } from '@/api/word.js'
import { allBook, bookInfo } from '@/api/wordList'

export default {
  name: "WordView",
  components: { Timer },
  data() {
    return {
      wordPlan: {},
      isBack: false,
      pages: 0,
      params: {},
      stage: true,//ture第一阶段，false第二阶段
      enterFlag: 0,//计划进的练习页面1,模块进的0,单词本进的-1，
      skipSum: 0,
      visible: false,//展示选择收藏单词本的弹窗
      checkList: [],
      complete: false,//二十个单词都完成了
      wordIndex: 0,
      title: '',
      value: '英音',
      shake: false,//是否显示错误抖动类名
      List: [],//二十个单词对象列表
      wordlength: 0,//单词长度
      sentenceLength: 0,
      word: '',
      wordArr: [],
      rwList: [],
      collectList: [],
      inputWord: [],
      bookList: [],//单词本列表
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
    getList(params) {
      // 模块进
      if (params.num && params.moduleId) {
        this.enterFlag = 0
        getWordByNum(params)
          .then((res) => {
            // console.log('res', res.data.word)
            if (res.code == 200) {
              this.List = res.data.word
              this.title = res.data.wordPlan.moduleName
              for (let i = 0; i < this.List.length; i++) {
                this.collectList[i] = false
              }
              this.getWord(res.data.word[0].content.word)
            }
          })
          .catch((err) => {
            console.log('err', err)
          })
      }
      // 计划进
      else if (params.userId) {
        this.enterFlag = 1
        getWordByUserId({ userId: params.userId })
          .then((res) => {
            // console.log('res', res.data.word)
            if (res.code == 200) {
              this.List = res.data.word
              this.wordPlan = res.data.wordPlan
              // console.log('wordPlan', this.wordPlan)
              this.title = res.data.wordPlan.moduleName
              for (let i = 0; i < this.List.length; i++) {
                this.collectList[i] = false
              }
              this.getWord(res.data.word[0].content.word)
            }
          })
          .catch((err) => {
            console.log('err', err)
          })
      }
      // 单词本进
      else if (params.bookId) {
        this.enterFlag = -1
        bookInfo({
          bookId: params.bookId,
          pageNum: params.pageNum,
          pageSize: params.pageSize
        })
          .then((res) => {
            // console.log('res', res.data.word)
            if (res.code == 200) {
              this.pages = res.data.word.pages
              this.List = res.data.word.records
              this.title = res.data.book.bookName
              for (let i = 0; i < this.List.length; i++) {
                this.collectList[i] = false
              }
              this.getWord(res.data.word.records[0])
            }
          })
          .catch((err) => {
            console.log('err', err)
          })
      }
    },

    getWord(data) {
      // console.log(data)
      this.word = data.wordHead || data.word
      this.wordArr = this.word.split('')
      if (this.enterFlag == -1 ? data.sentenceEn : data.content.sentence) {
        this.sentenceEn = this.enterFlag == -1 ? data.sentenceEn : data.content.sentence.sentences[0].sContent
        this.sentenceLength = this.sentenceEn.length
        this.sentenceZh = this.enterFlag == -1 ? data.sentenceZh : data.content.sentence.sentences[0].sCn
      }
      this.trans = this.enterFlag == -1 ? data.trans : data.content.trans[0].tranCn
      if (this.enterFlag == -1 ? data.pos : data.content.syno)
        this.pos = this.enterFlag == -1 ? data.pos : data.content.syno.synos[0].pos

      // console.log('word', this.word)
      this.wordlength = this.wordArr.length
      for (let i = 0; i < this.wordlength; i++) {
        if (this.showEng) this.rwList[i] = 1
        else this.rwList[i] = -1
      }
      this.playWord()
    },

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

    // 收藏单词
    collectWord() {
      this.visible = false
      if (this.checkList.length < 1 || !this.checkList) return
      const data = {
        bookId: this.checkList,
        word: this.word,
        trans: this.trans || '',
        pos: this.pos || '',
        sentenceEn: this.sentenceEn || '',
        sentenceZh: this.sentenceZh || ''
      }
      // console.log(data)
      collectWord(data)
        .then((res) => {
          console.log(res)
          if (res.code == 200) {
            this.$set(this.collectList, this.wordIndex, true)
            this.checkList = []
            this.$message.success('收藏成功！')
          }
        })
        .catch((err) => {
          console.log('err', err)
        })

    },

    // 键盘事件
    keyUp(e) {
      if (this.isStart) {
        this.info.inputNum++;
        let ss = this.trunTime(this.$refs.jishi.content)
        if (ss > 0) this.info.velocity = (this.info.inputNum / ss).toFixed(2);

        this.inputWord.push(e.key)
        const inputLength = this.inputWord.length
        if (this.wordArr[inputLength - 1].toLowerCase() === e.key) {
          this.$set(this.rwList, inputLength - 1, 0)
          // this.rwList[inputLength - 1] = 0
          this.info.correctNum++;
          if (this.isKeyboard) {
            this.audio.autoplay = true;
            this.audio.src = this.clickUrl;
          }
          if (inputLength >= this.wordlength) this.finish()
        }
        else {
          if (this.showEng) this.$set(this.rwList, inputLength - 1, 2)
          else this.$set(this.rwList, inputLength - 1, -2)
          this.audio.autoplay = true;
          this.audio.src = this.beepUrl;
          this.hasWrong()
        }
        this.info.correctRate = (this.info.correctNum / this.info.inputNum).toFixed(2)

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
      this.skip(false)
      // console.log('完成了', this.inputWord)
    },

    // 开始
    Start() {
      if (this.complete) return;
      this.isStart = !this.isStart
      if (this.isStart) this.playWord()
    },

    // 跳过
    skip(flag) {
      if (!this.stage && !flag) {
        this.skipSum++
        console.log('自打次数', this.skipSum)
      }
      if (this.complete) return;
      this.word = ''
      this.wordArr = []
      this.inputWord = []
      this.sentenceEn = ''
      this.sentenceZh = ''
      this.pos = ''
      this.trans = ''
      this.wordIndex++
      if (this.wordIndex < this.List.length)
        if (this.enterFlag == -1) this.getWord(this.List[this.wordIndex])
        else this.getWord(this.List[this.wordIndex].content.word)
      else {
        this.Start()
        this.complete = true
      }
    },

    // 再练一次
    again() {
      this.afresh(this.params)
      if (this.enterFlag == -1) this.getWord(this.List[0])//计划
      else this.getWord(this.List[0].content.word)
    },

    // 下一阶段/返回
    next() {
      if (this.isBack) this.$router.back()
      else {
        if (this.enterFlag == 1) {//是计划
          if (this.stage) {//进入第二阶段
            this.stage = false
            this.complete = false
            this.wordIndex = 0
            this.showZh = false
            this.showEng = false
            this.getWord(this.List[0].content.word)
            this.Start()
          } else {
            console.log('进入第三阶段')
            this.$router.push({
              name: 'exam',
              params: {
                handSum: this.skipSum,
                wordPlan: this.wordPlan
              }
            })
          }
        }
        else if (this.enterFlag == 0) {//是模块
          if (this.params.max > this.params.num) {
            this.params.num++
            this.afresh(this.params)
          } else {
            this.isBack = true
            this.$message.warning('已经到最后一页了！')
          }
        } else {//是单词本
          if (this.pages > this.params.pageNum) {
            this.params.pageNum++
            this.afresh(this.params)
          } else {
            this.isBack = true
            this.$message.warning('已经到最后一页了！')
          }
        }
      }

    },

    afresh(params) {
      this.getList(params)
      this.complete = false
      this.wordIndex = 0
      this.Start()
    },

    // 循环播放
    circulate() {
      this.isCirculate = !this.isCirculate
      this.audioWord.loop = this.isCirculate;
    },
    // 英文显示
    en() {
      if (this.stage) {
        this.showEng = !this.showEng
        for (let i = 0; i < this.wordlength; i++) {
          if (this.rwList[i] == 0) continue;//已经有正确字母了就保存
          if (this.showEng) this.rwList[i] = 1
          else this.rwList[i] = -1
        }
      }

    },

    // 中文显示
    zh() {
      if (this.stage) this.showZh = !this.showZh
    },

    // 时间转换
    trunTime(str) {
      let arr = str.split(':')
      arr[0] = arr[0] - 0
      arr[1] = arr[1] - 0
      return arr[0] * 60 + arr[1]
    },
  },
  created() {
    // console.log(this.$route.params)
    this.params = this.$route.params
    if (JSON.stringify(this.$route.params) !== "{}") {
      // console.log(this.$route.params)
      window.sessionStorage.setItem('params', JSON.stringify(this.$route.params))
      this.params = this.$route.params
      this.getList(this.params)
    } else {
      const params = JSON.parse(window.sessionStorage.getItem('params'))
      this.params = params
      this.getList(params)
    }

    // this.getList()
    // 显示绿色字、  黑色字、   红色字、   底部线黑色、  底部线红色
    //     答对后、  显 未答、  显 答错 、  不显未答、  不显答错
    //        0  、  1      、   2     、    -1   、    -2
  },
  mounted() {
    // 绑定监听事件
    window.addEventListener("keyup", this.keyUp);
    this.getBookList()
  },

  beforeDestroy() {
    window.removeEventListener('keyup', this.keyUp)
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
  width: 100%;
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
  width: 100%;
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
  text-align: center;
  left: 30%;
  top: 30%;
  width: 40%;
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


</style>
