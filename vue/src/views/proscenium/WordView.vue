<template>
  <div>
    <div class="card onOffCard">
      <el-row :gutter="2" type="flex" align="middle">
        <el-col :span="8"><span>4月6日单词训练</span></el-col>
        <el-col :span="5">
          <el-select v-model="value" placeholder="请选择" style="width: 90px">
            <el-option label="美音" value="mei"></el-option>
            <el-option label="英音" value="ying"></el-option>
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
              @click="keyboard"
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
                showEn ? 'purple' : 'deep_grey'
              } font_26 iconfont icon-wangluo`"
              @click="showEn = !showEn"
            ></span>
          </el-tooltip>
        </el-col>
        <!-- 开关深色模式 -->
        <el-col :span="2">
          <el-tooltip effect="dark" content="开关深色模式" placement="top">
            <span
              :class="`font_26 iconfont icon-${
                isDeep ? 'sun purple' : 'moon deep_grey'
              }`"
              @click="isDeep = !isDeep"
            ></span>
          </el-tooltip>
        </el-col>
        <el-col :span="4">
          <div
            @click="Start"
            :class="`white ${
              isStart ? 'bg_c1_grey' : 'bg_purple'
            }  radius_10 line_hei_50 text_center pointer`"
          >
            {{ isStart ? "Pause" : "Start" }}
          </div>
        </el-col>
      </el-row>
    </div>
    <div
      class="word flex_center_center"
      :style="`left: calc(50% - ${length * 20}px)`"
      
    >
      <div
        class="wid_40 margin_r_5"
        v-for="(item, index) in wordArr"
        :key="index"
      >
      <input @keydown="keyDown"/>
        <div v-show="rwList[index] === 1">{{ item }}</div>
        <div v-show="rwList[index] === 2" class="red">{{ item }}</div>
        <div v-show="rwList[index] === 0" class="green">{{ item }}</div>
        <div v-show="rwList[index] === -1" class="lineBottom"></div>
        <div v-show="rwList[index] === -2" class="lineBottom redBottom"></div>
      </div>
    </div>
    <div v-show="showEn" class="wordZh">占有，拥有</div>
    <div class="card recordCard flex_center">
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
    </div>
  </div>
</template>

<script>
import { getWord } from '@/api/word.js'
export default {
  name: "WordView",
  components: {},
  data() {
    return {
      value: '英音',
      length: 0,//单词长度
      wordArr: ['p', 'o', 's', 's', 'e', 's', 's'],
      rwList: [],
      ans: [],
      isCirculate: false,//循环播放
      isKeyboard: true,//键盘声
      showEng: true,//显示英文
      showEn: true,//显示中文
      isDeep: false,//深色模式
      isStart: false,//是否开始
    }
  },
  methods: {
    getList() {
      getWord({ userId: 1 })
        .then((res) => {
          console.log('res', res.data)
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
    keyDown(e) {
      console.log(e.key, e.keyCode) 
      console.log(e.target.value)
    },
    Start() {
      this.isStart = !this.isStart
    },
    // 循环播放
    circulate() {
      this.isCirculate = !this.isCirculate
    },
    // 键盘声
    keyboard() {
      this.isKeyboard = !this.isKeyboard
    },
    en() {
      this.showEng = !this.showEng
      for (let i = 0; i < this.wordArr.length; i++) {
        if (this.showEng) this.rwList[i] = 1
        else this.rwList[i] = -1
      }
    }
  },

  created() {
    // this.getList()
    // 显示绿色字、  黑色字、   红色字、   底部线黑色、  底部线红色
    //     答对后、  显 未答、  显 答错 、  不显未答、  不显答错
    //        0  、  1      、   2     、    -1   、    -2

    this.length = this.wordArr.length
    for (let i = 0; i < this.length; i++) {
      this.rwList[i] = 1
    }
  },
  beforeCreate() {
    document.querySelector('body').setAttribute('style', 'background:#faf9ff')
  },
  beforeDestroy() {
    document.querySelector('body').removeAttribute('style')
  }
};
</script>

<style scoped>
.card {
  border-radius: 10px;
  background: #fff;
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

.onOffCard {
  position: absolute;
  left: 42%;
  top: 5%;
  width: 43%;
  padding: 20px;
  font-size: 22px;
}

.recordCard {
  position: absolute;
  left: 26%;
  top: 60%;
  width: 48%;
  padding: 50px 16px;
}

.word {
  position: absolute;
  /* left: 40%; */
  top: 35%;
  font-size: 60px;
  color: #4b5563;
  letter-spacing: 12px;
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
  position: absolute;
  left: 45%;
  top: 50%;
  font-size: 24px;
  letter-spacing: 7px;
  color: #4b5563;
}
</style>