<template>
  <div class="box-size">
    <div class="hei_20"></div>
    <div class="flex_around_center">
      <img class="wid_380" src="@/assets/wordLog.png" alt="" />
      <div class="card recordCard flex_center">
        <div class="flex-1 flex_column_center_center">
          <span class="numCss"
            ><Timer :flag="isStart" ref="jishi"></Timer
          ></span>
          <span class="textCss">时间</span>
        </div>
        <div
            v-for="item in cols"
            :key="item.label"
            class="flex-1 flex_column_center_center"
        >
          <div class="numCss">{{ info[item.props] }}</div>
          <span class="textCss">{{ item.label }}</span>
        </div>
      </div>
      <div
          @click="Start"
          :class="`white ${
          isStart ? 'bg_c1_grey' : 'bg_green'
        }  radius_10 text_center pointer wid_100 hei_50 flex_center_center font_18`"
      >
        {{ isStart ? "Pause" : "Start" }}
      </div>
      <div
          class="white bg_purple radius_10 text_center pointer wid_100 hei_50 flex_center_center font_18"
          @click="empty"
      >
        Restart
      </div>
      <div
          class="white bg_red radius_10 text_center pointer wid_100 hei_50 flex_center_center font_18"
          @click="End"
      >
        End
      </div>
    </div>
    <div class="autoCenter wid_1000">
      <div class="hei_70"></div>
      <div class="margin_t_10" v-for="(item, index) in articleArr" :key="index">
        <p class="index_content-row">
          <span
            v-for="(i, ind) in item"
            :key="ind"
            :class="`${
              ansArr[index][ind] == 1
                ? 'green'
                : ansArr[index][ind] == 0
                ? 'red'
                : 'black'
            }`"
            >{{ i }}</span
          >
        </p>
        <input
          :name="index"
          :ref="`input${index}`"
          :maxlength="item.length"
          @input="input"
          @keydown.delete.prevent
          :readonly="!(index == inputIndex && isStart)"
          type="text"
          class="index_content-row"
        />
      </div>
    </div>
  </div>
</template>

<script>
import Timer from '@/components/Timer.vue'
import {getArticle} from '@/api/article.js'
import {examResult} from '@/api/word.js'

export default {
  components: {Timer},
  data() {
    return {
      articleId: '',
      articleArr: [],
      ansArr: [],
      inputIndex: 0,
      isStart: false,
      info: {
        inputNum: 0,
        velocity: 0,
        correctNum: 0,
        correctRate: 0,
      },
      cols: [{label: '输入数', props: 'inputNum'},
        {label: '速度(字/分钟)', props: 'velocity'},
        {label: '正确数', props: 'correctNum'},
        {label: '正确率', props: 'correctRate'}],
    }
  },
  methods: {
    getArticle() {
      getArticle(this.articleId)
        .then((res) => {
          // console.log('res', res.data
          if (res.code === 200) {
            const articleArr = this.fnAddBr(res.data.content)
            let ansArr = []
            articleArr.forEach((item, index) => {
              let arr = []
              articleArr[index] = item.split('')
              /*修改删除前后空格*/
              if (articleArr[index][0] === " ") {
                articleArr[index].shift()
              }
              if (articleArr[index][articleArr[index].length] === " ") {
                articleArr[index].pop()
              }
              articleArr[index].forEach((i, ind) => {
                arr[ind] = -1
              });
              ansArr.push(arr)
            });
            this.articleArr = articleArr
            this.ansArr = ansArr
            // console.log(articleArr)
          }
        })
          .catch((err) => {
            console.log('err', err)
          })
    },

    Start() {
      this.isStart = !this.isStart
    },

    empty() {
      window.location.reload()
      // let time = this.trunTime(this.$refs.jishi.content)
      // if (time == 0) return
      // this.articleArr.forEach((item,i) => {
      //   // console.log('input',this.$refs[`input${i}`])
      //   this.$refs[`input${i}`].value=''
      //   // console.log('this.articleArr[i]',this.articleArr[i])
      //   this.articleArr[i].forEach((it, ind) => {
      //     this.arr[ind] = -1
      //   });
      // })
      //
      // this.isStart = false
      // this.info= {
      //   inputNum: 0,
      //   velocity: 0,
      //   correctNum: 0,
      //   correctRate: 0,
      // }

    },

    input(e) {
      const {value, name} = e.target
      // console.log('value', value)
      // console.log('value.length', value.length)
      // console.log('this.articleArr[name].length', this.articleArr[name].length)
      if (value.length >= this.articleArr[name].length) {
        this.inputIndex++
        this.$refs[`input${this.inputIndex}`][0].focus()
      }
      if (e.data) {
        this.info.inputNum++;
        let ss = this.trunTime(this.$refs.jishi.content)
        if (ss > 0) this.info.velocity = (this.info.inputNum / ss * 60).toFixed(2);

        // console.log(this.articleArr[name][value.length - 1])
        // console.log(e.data)
        if (this.articleArr[name][value.length - 1].toLowerCase() == e.data) {
          this.$set(this.ansArr[name], value.length - 1, 1)
          this.info.correctNum++
        }
        else this.$set(this.ansArr[name], value.length - 1, 0)
        this.info.correctRate = (this.info.correctNum / this.info.inputNum).toFixed(2)
      }
    },

    // 时间转换
    trunTime(str) {
      let arr = str.split(':')
      arr[0] = arr[0] - 0
      arr[1] = arr[1] - 0
      return arr[0] * 60 + arr[1]
    },

    fnAddBr(str) {
      // const regex = new RegExp(`(?<=\\S{79})(?=\\S)`, 'g');
      // return str.replace(regex, '-\n');
      return [...str.match(/.{1,75}/g)];
      // return str;
    },

    // 结束练习
    End() {
      let time = this.trunTime(this.$refs.jishi.content)
      if (time == 0) return
      if (window.sessionStorage.getItem("token")) {
        let { info } = this
        info.time = time
        info.resultType = 1
        // console.log('info', info)
        examResult(info)
          .then(res => {
            console.log('res', res)
            if (res.code === 200) {
              this.$notify.success({
                title: '成功',
                message: res.msg,
                offset: 60
              });
            }
          })
      } else console.log('未登录')
      this.$router.back()
    }
  },
  created() {
    if (this.$route.params.articleId) {
      window.sessionStorage.setItem('articleId', this.$route.params.articleId)
      this.articleId = this.$route.params.articleId
      this.getArticle()
    } else {
      const articleId = window.sessionStorage.getItem('articleId')
      this.articleId = articleId
      this.getArticle()
    }

  },
}
</script>

<style scoped>
.index_content-row {
  font-family: "Lucida Console";
  display: block;
  /* overflow: hidden; */
  width: 100%;
  border: 0;
  border-bottom: 1px solid #ddd;
  outline: none;
  height: 30px;
  line-height: 30px;
  font-size: 21px;
}

.card {
  border-radius: 10px;
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

.recordCard {
  width: 48%;
  padding: 30px 16px;
}
</style>
