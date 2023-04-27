<template>
  <div :class="`bgcolor-${isDark ? 'off' : 'on'}`">
    <div class="fixBtn flex_center">
      <div
        @click="again"
        class="padding_0_30 bg_purple radius_10 line_hei_50 text_center pointer margin_r_30"
      >
        Again
      </div>
      <div
        @click="visible = true"
        class="padding_0_30 bg_purple radius_10 line_hei_50 text_center pointer"
      >
        Tip
      </div>
    </div>
    <div class="flex_column_center hei_per84">
      <div class="hei_100"></div>
      <h1 :class="`title title-${isDark ? 'off' : 'on'}`">
        Cool keyboard game
      </h1>
      <h1
        :class="`flex_center_center title title-${isDark ? 'off' : 'on'}`"
        id="score"
      >
        <span class="margin_r_150"> Total : {{ total }}</span>
        <span class="margin_r_150"> Score : {{ score }}</span>
        <div class="flex_center font_20">
          Time:
          <el-statistic
            class="width_per20"
            @finish="hilarity"
            :value="deadline"
            format="mm:ss"
            time-indices
          >
          </el-statistic>
        </div>
      </h1>
      <div class="">
        <!-- First row : ESC, 1, 2, 3, 4, 5, 6, 7, 8, 9, 0, -, + and BACK -->
        <ul class="row row-0">
          <li
            :class="`${item.class}-${isDark ? 'off' : 'on'} ${
              code == item.id ? 'hit' : ''
            } ${selected == item.id ? 'selected' : ''}`"
            v-for="item in row0"
            :key="item.id"
          >
            {{ item.id }}
          </li>
        </ul>
        <!-- Second row : TAB, A, Z, E, R, T, Y, U, I, O, P, "[", "]" and "\" -->
        <ul class="row row-1">
          <li
            :class="`${item.class}-${isDark ? 'off' : 'on'} ${
              code == item.id ? 'hit' : ''
            } ${selected == item.id ? 'selected' : ''}`"
            v-for="item in row1"
            :key="item.id"
          >
            {{ item.id }}
          </li>
        </ul>
        <!-- Third row : CAPS, Q, S, D, F, G, H, J, K, L, M, "%" and ENTER -->
        <ul class="row row-2">
          <li
            :class="`${item.class}-${isDark ? 'off' : 'on'} ${
              code == item.id ? 'hit' : ''
            } ${selected == item.id ? 'selected' : ''}`"
            v-for="item in row2"
            :key="item.id"
          >
            {{ item.id }}
          </li>
        </ul>
        <!-- Fourth row : LEFT-SHIFT, W, X, C, V, B, N, "?", ".", ";", ":" and RIGHT-SHIFT -->
        <ul class="row row-3">
          <li
            :class="`${item.class}-${isDark ? 'off' : 'on'} ${
              code == item.id ? 'hit' : ''
            } ${selected == item.id ? 'selected' : ''}`"
            v-for="item in row3"
            :key="item.id"
          >
            {{ item.id }}
          </li>
        </ul>
      </div>
    </div>
    <div class="toggle beer-pong">
      <input id="beer-pong" type="checkbox" @click="isDark = !isDark" />
      <label class="toggle-item" for="beer-pong"></label>
      <div class="cup">
        <div class="lid"></div>
      </div>
    </div>
    <el-dialog
      :visible="visible"
      width="70%"
      title="键盘手势图"
      center="true"
      @close="visible = false"
    >
      <img style="width: 100%" src="@/assets/tip.png" alt="" />
    </el-dialog>
  </div>
</template>

<script>
import { examResult } from '@/api/word.js'
export default {
  data() {
    return {
      visible: false,//显示弹窗
      isStart: true,//键盘禁用状态，为false禁用键盘
      deadline: Date.now() + 1000 * 60,
      keys: [..."ABCDEFGHIJKLMNOPQRSTUVWXYZ"],
      total: 0,
      score: 0,
      code: '',//按键的ASCII码
      selected: '',//随机晃动的键
      isDark: false,
      row0: [
        { class: "color0", id: "Esc" },
        { class: "color0", id: "1" },
        { class: "color1", id: "2" },
        { class: "color2", id: "3" },
        { class: "color3", id: "4" },
        { class: "color4", id: "5" },
        { class: "color4", id: "6" },
        { class: "color3", id: "7" },
        { class: "color2", id: "8" },
        { class: "color1", id: "9" },
        { class: "color0", id: "0" },
        { class: "color0", id: "-" },
        { class: "color0", id: "+" },
        { class: "width5 color0", id: "Back" }
      ],
      row1: [
        { class: "width5 color0", id: "Tab" },
        { class: "color0", id: "Q" },
        { class: "color1", id: "W" },
        { class: "color2", id: "E" },
        { class: "color3", id: "R" },
        { class: "color4", id: "T" },
        { class: "color4", id: "Y" },
        { class: "color3", id: "U" },
        { class: "color2", id: "I" },
        { class: "color1", id: "O" },
        { class: "color0", id: "P" },
        { class: "color0", id: "[" },
        { class: "color0", id: "]" },
        { class: "color0", id: "\\" }
      ],
      row2: [
        { class: "width6 color0", id: "Caps" },
        { class: "color0", id: "A" },
        { class: "color1", id: "S" },
        { class: "color2", id: "D" },
        { class: "color3", id: "F" },
        { class: "color4", id: "G" },
        { class: "color4", id: "H" },
        { class: "color3", id: "J" },
        { class: "color2", id: "K" },
        { class: "color1", id: "L" },
        { class: "color0", id: ";" },
        { class: "color0", id: "'" },
        { class: "width6 color0", id: "Enter" }
      ],
      row3: [
        { class: "width8 color0", id: "Ctrl" },
        { class: "color0", id: "Z" },
        { class: "color1", id: "X" },
        { class: "color2", id: "C" },
        { class: "color3", id: "V" },
        { class: "color4", id: "B" },
        { class: "color4", id: "N" },
        { class: "color3", id: "M" },
        { class: "color2", id: "," },
        { class: "color2", id: "." },
        { class: "color0", id: "?" },
        { class: "width8 color0", id: "Shift" }

      ]
    }
  },
  methods: {
    closedImg() {
      this.visible = false
    },
    again() {
      this.score = 0
      this.total = 0
      this.deadline = Date.now() + 1000 * 60
      this.isStart = true
    },
    hilarity() {
      this.isStart = false
      this.$notify({
        title: '提示',
        message: '时间已到',
        duration: 0,
      });
      if (window.sessionStorage.getItem("token")) {
        const data = { resultType: 2, score: this.score, total: this.total }
        examResult(data)
          .then(res => {
            // console.log('res', res)
            if (res.code == 200) this.$message.success(res.msg)
          })
      } else console.log('未登录')


    },
    // 获得0到25(密钥)之间的随机数
    getRandomNumber(min, max) {
      min = Math.ceil(min);
      max = Math.floor(max);
      return Math.floor(Math.random() * (max - min + 1)) + min;
    },

    // 随机使页面的其中一个字母处于选定状态
    targetRandomKey() {
      this.selected = this.keys[this.getRandomNumber(0, this.keys.length - 1)]
    },

    // 当键盘被按下时
    keyDown(event) {
      if (!this.isStart) return;
      this.total++
      this.code = String.fromCharCode(event.keyCode)//当前敲的键
      if (this.code == this.selected) {
        this.score++
        this.targetRandomKey();
      } else this.score--
    },

    keyUp() {
      this.code = ''
    }
  },
  mounted() {
    // 绑定监听事件
    window.addEventListener("keydown", this.keyDown);
    window.addEventListener("keyup", this.keyUp);
    this.targetRandomKey();
  },
}
</script>

<style scoped>
.fixBtn {
  position: absolute;
  top: 20%;
  right: 10%;
  color: white;
  font-size: 20px;
}

.title .el-statistic {
  width: auto !important;
  color: rgba(250, 1, 1, 0.7) !important;
  font-size: 1em !important;
  line-height: 0 !important;
}

/* .title .el-statistic .con .number {
  font-size: 18px !important;
} */

* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

*:before,
*:after {
  content: "";
  position: absolute;
}

.bgcolor-off {
  background-color: black;
}

.bgcolor-on {
  background-color: #c6c5c5;
}

.title {
  text-transform: uppercase;
  /* margin: 20px 0; */
  margin-bottom: 40px;
  font-size: 1em;
  letter-spacing: 0.3em;
  transition: color 1s;
}

.title-off {
  color: mintcream;
}

.title-on {
  color: rgba(0, 0, 0, 0.7);
}

.row {
  list-style: none;
  display: flex;
}

li {
  height: 3em;
  width: 3em;
  border-radius: 0.4em;
  line-height: 3em;
  letter-spacing: 1px;
  margin: 0.4em;
  transition: 0.3s;
  text-align: center;
  font-size: 1em;

  transition: background-color 1s, border 1s, color 1s;
}

.width5 {
  width: 5em;
}

.width6 {
  width: 6em;
}

.width8 {
  width: 8em;
}

.color0-off {
  background-color: #ed7b5f;
  border: 2px solid #ed7b5f;
  color: rgba(0, 0, 0, 0.7);
}

.color0-off.selected {
  color: #ed7b5f;
}

.color1-off {
  background-color: #ea9556;
  border: 2px solid #ea9556;
  color: rgba(0, 0, 0, 0.7);
}

.color1-off.selected {
  color: #ea9556;
}

.color2-off {
  background-color: #e1af58;
  border: 2px solid #e1af58;
  color: rgba(0, 0, 0, 0.7);
}

.color2-off.selected {
  color: #e1af58;
}

.color3-off {
  background-color: #d3c766;
  border: 2px solid #d3c766;
  color: rgba(0, 0, 0, 0.7);
}

.color3-off.selected {
  color: #d3c766;
}

.color4-off {
  background-color: #c1de81;
  border: 2px solid #c1de81;
  color: rgba(0, 0, 0, 0.7);
}

.color4-off.selected {
  color: #c1de81;
}

.color0-on {
  background-color: #0d2e60;
  border: 2px solid #0d2e60;
  color: rgba(255, 255, 255, 0.7);
}

.color0-on.selected {
  color: #0d2e60;
}

.color1-on {
  background-color: #224882;
  border: 2px solid #224882;
  color: rgba(255, 255, 255, 0.7);
}

.color1-on.selected {
  color: #224882;
}

.color2-on {
  background-color: #3564a5;
  border: 2px solid #3564a5;
  color: rgba(255, 255, 255, 0.7);
}

.color2-on.selected {
  color: #3564a5;
}

.color3-on {
  background-color: #4882ca;
  border: 2px solid #4882ca;
  color: rgba(255, 255, 255, 0.7);
}

.color3-on .selected {
  color: #4882ca;
}

.color4-on {
  background-color: #59a1f0;
  border: 2px solid #59a1f0;
  color: rgba(255, 255, 255, 0.7);
}

.color4-on.selected {
  color: #59a1f0;
}

.selected {
  background-color: transparent;
  -webkit-animation: vibrate-1 0.3s linear infinite both;
  animation: vibrate-1 0.3s linear infinite both;
}

.hit {
  -webkit-animation: hit 0.3s cubic-bezier(0.39, 0.575, 0.565, 1) both;
  animation: hit 0.3s cubic-bezier(0.39, 0.575, 0.565, 1) both;
}

@-webkit-keyframes hit {
  0% {
    -webkit-transform: scale(1.2);
    transform: scale(1.2);
  }
  100% {
    -webkit-transform: scale(1);
    transform: scale(1);
  }
}

@keyframes hit {
  0% {
    -webkit-transform: scale(1.2);
    transform: scale(1.2);
  }
  100% {
    -webkit-transform: scale(1);
    transform: scale(1);
  }
}

@-webkit-keyframes vibrate-1 {
  0% {
    -webkit-transform: translate(0);
    transform: translate(0);
  }
  20% {
    -webkit-transform: translate(-2px, 2px);
    transform: translate(-2px, 2px);
  }
  40% {
    -webkit-transform: translate(-2px, -2px);
    transform: translate(-2px, -2px);
  }
  60% {
    -webkit-transform: translate(2px, 2px);
    transform: translate(2px, 2px);
  }
  80% {
    -webkit-transform: translate(2px, -2px);
    transform: translate(2px, -2px);
  }
  100% {
    -webkit-transform: translate(0);
    transform: translate(0);
  }
}

@keyframes vibrate-1 {
  0% {
    -webkit-transform: translate(0);
    transform: translate(0);
  }
  20% {
    -webkit-transform: translate(-2px, 2px);
    transform: translate(-2px, 2px);
  }
  40% {
    -webkit-transform: translate(-2px, -2px);
    transform: translate(-2px, -2px);
  }
  60% {
    -webkit-transform: translate(2px, 2px);
    transform: translate(2px, 2px);
  }
  80% {
    -webkit-transform: translate(2px, -2px);
    transform: translate(2px, -2px);
  }
  100% {
    -webkit-transform: translate(0);
    transform: translate(0);
  }
}

input {
  height: 40px;
  left: 0;
  opacity: 0;
  position: absolute;
  top: 0;
  width: 40px;
}

.toggle {
  position: relative;
  display: inline-block;
}

label.toggle-item {
  width: 7em;
  background: #2e394d;
  height: 3em;
  display: inline-block;
  border-radius: 50px;
  margin: 40px;
  position: relative;
  transition: all 0.3s ease;
  transform-origin: 20% center;
  cursor: pointer;
}

label.toggle-item:before {
  display: block;
  transition: all 0.2s ease;
  width: 2.3em;
  height: 2.3em;
  top: 0.25em;
  left: 0.25em;
  border-radius: 2em;
  border: 2px solid #88cf8f;
  transition: 0.3s ease;
}

.beer-pong {
  position: absolute;
  bottom: 0;
  left: 0;
}

.beer-pong label:before {
  background: #f9f9f9;
  box-shadow: inset 0 -3px 0 0 #c6c5c5;
  border: none;
  width: 2.5em;
  height: 2.5em;
  top: 0.25em;
  left: 0.25em;
}

.beer-pong .cup {
  top: -3%;
  right: -118px;
  border-top: 90px solid #f44336;
  border-left: 15px solid transparent;
  border-right: 15px solid transparent;
  height: 3px;
  position: absolute;
  width: 80px;
  transform-origin: bottom right;
  transition: 0.2s cubic-bezier(0.42, 0.5, 0.58, 1);
}

.beer-pong .cup:before {
  box-shadow: 0 -10px 0 0px rgba(39, 39, 39, 0.1),
    0px -20px 0 0px rgba(39, 39, 39, 0.1);
  border-radius: 3px;
  overflow: hidden;
  background: rgba(39, 39, 39, 0.1);
  width: 120%;
  left: -5px;
  height: 4px;
  top: -40px;
}

.beer-pong .cup .lid {
  position: absolute;
  width: 95px;
  height: 8px;
  border-radius: 20px;
  background: #efefef;
  bottom: 86px;
  left: -23px;
}

.beer-pong .cup .lid:after {
  background: #efefef;
  width: 48px;
  height: 5px;
  left: 50%;
  margin-left: -24px;
  top: 94px;
  border-radius: 0 0 3px 3px;
}

#beer-pong:checked ~ .cup {
  animation: 0.2s linear cup 1s forwards;
}

#beer-pong:checked + label:before {
  animation: 2s linear bounce-off forwards;
}

@keyframes cup {
  0% {
    transform: none;
  }
  50% {
    transform: rotate(75deg) translate(10px, 15px);
  }
  90% {
    transform: rotate(70deg) translate(10px, 15px);
  }
  100% {
    transform: rotate(75deg) translate(10px, 15px);
  }
}

@keyframes bounce-off {
  0% {
    transform: translateY(0);
  }
  10%,
  25% {
    transform: translate(-20px, -80px);
  }
  50% {
    transform: rotate(163deg);
    transform-origin: 100px -12px;
  }
  70% {
    transform: rotate(0) translate(-3px, -8px);
    transform-origin: 100px -12px;
  }
  75% {
    transform: translate(20px, -8px);
  }
  80% {
    transform: translate(30px, 0px);
  }
  85% {
    transform: translate(40px, -3px);
  }
  87% {
    transform: translate(46px, 0px);
  }
  90% {
    transform: translate(52px, -1px);
  }
  95% {
    transform: translate(60px, 0px);
  }
  100% {
    transform: translate(64px, 0px);
  }
}
</style>