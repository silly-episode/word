<template>
  <div class="bg">
    <div class="Transarea">
      <div class="orgLang">
        <span>源语言</span>
        <el-select v-model="orgValue" placeholder="请选择">
          <el-option
            v-for="item in orgLangOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>

      <div class="transLang">
        <span>翻译为</span>
        <el-select v-model="transValue" placeholder="请选择">
          <el-option
            v-for="item in transLangOptions"
            :key="item.value"
            :label="item.label"
            :value="item.value"
          >
          </el-option>
        </el-select>
      </div>
    </div>
    <div class="flex_center_center">
      <div class="box">
        <textarea
            v-model="content"
            class="textarea"
            maxlength="1001"
            placeholder="输入、粘贴文本"
        ></textarea>
        <div class="wordNum">
          <span v-if="content.length < 1001">{{ content.length }}/1000</span>
          <span v-else>您输入的翻译字数已超过限制，系统已自动截断</span>
        </div>
        <i
          v-if="content.length > 0"
          class="close iconfont icon-close"
          v-on:click="empty()"
        ></i>

        <div class="arrowBtn">
          <img
            v-if="!translateFlag"
            alt=""
            src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4KPHN2ZyB3aWR0aD0iMzJweCIgaGVpZ2h0PSIzMnB4IiB2aWV3Qm94PSIwIDAgMzIgMzIiIHZlcnNpb249IjEuMSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIiB4bWxuczp4bGluaz0iaHR0cDovL3d3dy53My5vcmcvMTk5OS94bGluayI+CiAgICA8dGl0bGU+566t5aS0PC90aXRsZT4KICAgIDxnIGlkPSLpobXpnaItMSIgc3Ryb2tlPSJub25lIiBzdHJva2Utd2lkdGg9IjEiIGZpbGw9Im5vbmUiIGZpbGwtcnVsZT0iZXZlbm9kZCI+CiAgICAgICAgPGcgaWQ9Iue/u+ivkV/mnKrovpPlhaXlhoXlrrlfMTkyMCIgdHJhbnNmb3JtPSJ0cmFuc2xhdGUoLTk0NC4wMDAwMDAsIC0zMTcuMDAwMDAwKSI+CiAgICAgICAgICAgIDxnIGlkPSLnvJbnu4QiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDkzMC4wMDAwMDAsIDMwMy4wMDAwMDApIj4KICAgICAgICAgICAgICAgIDxnIGlkPSLnrq3lpLQiIHRyYW5zZm9ybT0idHJhbnNsYXRlKDE0LjAwMDAwMCwgMTQuMDAwMDAwKSI+CiAgICAgICAgICAgICAgICAgICAgPHJlY3QgaWQ9IuefqeW9oiIgZmlsbD0iIzAwMDAwMCIgZmlsbC1ydWxlPSJub256ZXJvIiBvcGFjaXR5PSIwIiB4PSIwIiB5PSIwIiB3aWR0aD0iMzIiIGhlaWdodD0iMzIiPjwvcmVjdD4KICAgICAgICAgICAgICAgICAgICA8cGF0aCBkPSJNMTcuNDE3NzU5NCw3LjQyMDU2MjUxIEwyOC4xMzIxNTgyLDE1LjM1NzE1NDIgQzI4LjQ4NzE5MjEsMTUuNjIwMTQyMyAyOC41NjE4MSwxNi4xMjExNDgxIDI4LjI5ODgyMTksMTYuNDc2MTgyIEMyOC4yNTE4MDA4LDE2LjUzOTY2MDYgMjguMTk1NjM2OCwxNi41OTU4MjQ2IDI4LjEzMjE1ODIsMTYuNjQyODQ1OCBMMTcuNDE3OTMxMywyNC41NzkzMTAxIEMxNy4wNjI4OTc0LDI0Ljg0MjI5ODIgMTYuNTYxODkxNiwyNC43Njc2ODAzIDE2LjI5ODkwMzUsMjQuNDEyNjQ2NCBDMTYuMTQ5NTc3MywyNC4yMTEwNTYgMTYuMTAzNDYzNSwyMy45NTEwOTIxIDE2LjE3NDM0MjUsMjMuNzEwNDQwNSBMMTcuNDEwODkxMywxOS41MTIwNTE0IEMxNy40NzMzMDYxLDE5LjMwMDEzNzggMTcuMzUyMTEzNCwxOS4wNzc3NTA4IDE3LjE0MDE5OTgsMTkuMDE1MzM2IEMxNy4xMDM0OTI4LDE5LjAwNDUyNDcgMTcuMDY1NDI0NCwxOC45OTkwMzY2IDE3LjAyNzE1ODQsMTguOTk5MDM5NCBMNSwxOC45OTk5MjYzIEM0LjQ0NzcxNTI1LDE5LjAwMDA0MDcgMy45OTk5NjY5OSwxOC41NTIzNTg1IDMuOTk5OTI2MjcsMTguMDAwMDczNyBDMy45OTk5MjYyNywxOC4wMDAwNDkyIDMuOTk5OTI2MjcsMTguMDAwMDI0NiA0LDE4IEw0LDE0IEM0LjAwMDA3Mzc0LDEzLjQ0Nzc0NCA0LjQ0Nzc0NDA1LDEzLjAwMDA0MDcgNSwxMi45OTk5MjYzIEwxNy4wMjYyNzA4LDEyLjk5OTAzOTQgQzE3LjI0NzE4NDcsMTIuOTk5MDIzMSAxNy40MjYyNTc1LDEyLjgxOTkyMzggMTcuNDI2MjQxMywxMi41OTkwMDk5IEMxNy40MjYyMzg0LDEyLjU2MDc3MDkgMTcuNDIwNzUyNCwxMi41MjI3Mjk5IDE3LjQwOTk1MDgsMTIuNDg2MDQ4MiBMMTYuMTc0MTU4Myw4LjI4OTM5MDY0IEMxNi4wNDkzNTE3LDcuODY1NTU2ODEgMTYuMjkxNzYxMiw3LjQyMDc5NTg2IDE2LjcxNTU5NSw3LjI5NTk4OTI0IEMxNi45NTYyMzU4LDcuMjI1MTI3NTkgMTcuMjE2MTgxMiw3LjI3MTI0NTM3IDE3LjQxNzc1OTQsNy40MjA1NjI1MSBaIiBpZD0i6Lev5b6EIiBmaWxsPSIjMkIyQzJFIj48L3BhdGg+CiAgICAgICAgICAgICAgICA8L2c+CiAgICAgICAgICAgIDwvZz4KICAgICAgICA8L2c+CiAgICA8L2c+Cjwvc3ZnPg=="
            v-on:click="translate()"
          />
          <img
            v-if="translateFlag"
            alt=""
            src="data:image/svg+xml;base64,PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0idXRmLTgiPz4KPHN2ZyB4bWxucz0iaHR0cDovL3d3dy53My5vcmcvMjAwMC9zdmciIHhtbG5zOnhsaW5rPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5L3hsaW5rIiBzdHlsZT0ibWFyZ2luOiBhdXRvOyBiYWNrZ3JvdW5kOiByZ2JhKDI1NSwgMjU1LCAyNTUsIDApOyBkaXNwbGF5OiBibG9jazsgc2hhcGUtcmVuZGVyaW5nOiBhdXRvOyIgd2lkdGg9IjQwcHgiIGhlaWdodD0iNDBweCIgdmlld0JveD0iMCAwIDEwMCAxMDAiIHByZXNlcnZlQXNwZWN0UmF0aW89InhNaWRZTWlkIj4KPGNpcmNsZSBjeD0iODQiIGN5PSI1MCIgcj0iNC44MDA1MyIgZmlsbD0iIzY5NmE2ZCI+CiAgICA8YW5pbWF0ZSBhdHRyaWJ1dGVOYW1lPSJyIiByZXBlYXRDb3VudD0iaW5kZWZpbml0ZSIgZHVyPSIwLjQ2Mjk2Mjk2Mjk2Mjk2MjlzIiBjYWxjTW9kZT0ic3BsaW5lIiBrZXlUaW1lcz0iMDsxIiB2YWx1ZXM9IjEwOzAiIGtleVNwbGluZXM9IjAgMC41IDAuNSAxIiBiZWdpbj0iMHMiPjwvYW5pbWF0ZT4KICAgIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9ImZpbGwiIHJlcGVhdENvdW50PSJpbmRlZmluaXRlIiBkdXI9IjEuODUxODUxODUxODUxODUxNnMiIGNhbGNNb2RlPSJkaXNjcmV0ZSIga2V5VGltZXM9IjA7MC4yNTswLjU7MC43NTsxIiB2YWx1ZXM9IiM2OTZhNmQ7IzFiMWQxZjsjMmIyYzJlOyM0YTRjNGY7IzY5NmE2ZCIgYmVnaW49IjBzIj48L2FuaW1hdGU+CjwvY2lyY2xlPjxjaXJjbGUgY3g9IjMzLjY3NTEiIGN5PSI1MCIgcj0iMTAiIGZpbGw9IiM2OTZhNmQiPgogIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9InIiIHJlcGVhdENvdW50PSJpbmRlZmluaXRlIiBkdXI9IjEuODUxODUxODUxODUxODUxNnMiIGNhbGNNb2RlPSJzcGxpbmUiIGtleVRpbWVzPSIwOzAuMjU7MC41OzAuNzU7MSIgdmFsdWVzPSIwOzA7MTA7MTA7MTAiIGtleVNwbGluZXM9IjAgMC41IDAuNSAxOzAgMC41IDAuNSAxOzAgMC41IDAuNSAxOzAgMC41IDAuNSAxIiBiZWdpbj0iMHMiPjwvYW5pbWF0ZT4KICA8YW5pbWF0ZSBhdHRyaWJ1dGVOYW1lPSJjeCIgcmVwZWF0Q291bnQ9ImluZGVmaW5pdGUiIGR1cj0iMS44NTE4NTE4NTE4NTE4NTE2cyIgY2FsY01vZGU9InNwbGluZSIga2V5VGltZXM9IjA7MC4yNTswLjU7MC43NTsxIiB2YWx1ZXM9IjE2OzE2OzE2OzUwOzg0IiBrZXlTcGxpbmVzPSIwIDAuNSAwLjUgMTswIDAuNSAwLjUgMTswIDAuNSAwLjUgMTswIDAuNSAwLjUgMSIgYmVnaW49IjBzIj48L2FuaW1hdGU+CjwvY2lyY2xlPjxjaXJjbGUgY3g9IjY3LjY3NSIgY3k9IjUwIiByPSIxMCIgZmlsbD0iIzRhNGM0ZiI+CiAgPGFuaW1hdGUgYXR0cmlidXRlTmFtZT0iciIgcmVwZWF0Q291bnQ9ImluZGVmaW5pdGUiIGR1cj0iMS44NTE4NTE4NTE4NTE4NTE2cyIgY2FsY01vZGU9InNwbGluZSIga2V5VGltZXM9IjA7MC4yNTswLjU7MC43NTsxIiB2YWx1ZXM9IjA7MDsxMDsxMDsxMCIga2V5U3BsaW5lcz0iMCAwLjUgMC41IDE7MCAwLjUgMC41IDE7MCAwLjUgMC41IDE7MCAwLjUgMC41IDEiIGJlZ2luPSItMC40NjI5NjI5NjI5NjI5NjI5cyI+PC9hbmltYXRlPgogIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9ImN4IiByZXBlYXRDb3VudD0iaW5kZWZpbml0ZSIgZHVyPSIxLjg1MTg1MTg1MTg1MTg1MTZzIiBjYWxjTW9kZT0ic3BsaW5lIiBrZXlUaW1lcz0iMDswLjI1OzAuNTswLjc1OzEiIHZhbHVlcz0iMTY7MTY7MTY7NTA7ODQiIGtleVNwbGluZXM9IjAgMC41IDAuNSAxOzAgMC41IDAuNSAxOzAgMC41IDAuNSAxOzAgMC41IDAuNSAxIiBiZWdpbj0iLTAuNDYyOTYyOTYyOTYyOTYyOXMiPjwvYW5pbWF0ZT4KPC9jaXJjbGU+PGNpcmNsZSBjeD0iMTYiIGN5PSI1MCIgcj0iMCIgZmlsbD0iIzJiMmMyZSI+CiAgPGFuaW1hdGUgYXR0cmlidXRlTmFtZT0iciIgcmVwZWF0Q291bnQ9ImluZGVmaW5pdGUiIGR1cj0iMS44NTE4NTE4NTE4NTE4NTE2cyIgY2FsY01vZGU9InNwbGluZSIga2V5VGltZXM9IjA7MC4yNTswLjU7MC43NTsxIiB2YWx1ZXM9IjA7MDsxMDsxMDsxMCIga2V5U3BsaW5lcz0iMCAwLjUgMC41IDE7MCAwLjUgMC41IDE7MCAwLjUgMC41IDE7MCAwLjUgMC41IDEiIGJlZ2luPSItMC45MjU5MjU5MjU5MjU5MjU4cyI+PC9hbmltYXRlPgogIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9ImN4IiByZXBlYXRDb3VudD0iaW5kZWZpbml0ZSIgZHVyPSIxLjg1MTg1MTg1MTg1MTg1MTZzIiBjYWxjTW9kZT0ic3BsaW5lIiBrZXlUaW1lcz0iMDswLjI1OzAuNTswLjc1OzEiIHZhbHVlcz0iMTY7MTY7MTY7NTA7ODQiIGtleVNwbGluZXM9IjAgMC41IDAuNSAxOzAgMC41IDAuNSAxOzAgMC41IDAuNSAxOzAgMC41IDAuNSAxIiBiZWdpbj0iLTAuOTI1OTI1OTI1OTI1OTI1OHMiPjwvYW5pbWF0ZT4KPC9jaXJjbGU+PGNpcmNsZSBjeD0iMTYiIGN5PSI1MCIgcj0iNS4xOTg1NSIgZmlsbD0iIzFiMWQxZiI+CiAgPGFuaW1hdGUgYXR0cmlidXRlTmFtZT0iciIgcmVwZWF0Q291bnQ9ImluZGVmaW5pdGUiIGR1cj0iMS44NTE4NTE4NTE4NTE4NTE2cyIgY2FsY01vZGU9InNwbGluZSIga2V5VGltZXM9IjA7MC4yNTswLjU7MC43NTsxIiB2YWx1ZXM9IjA7MDsxMDsxMDsxMCIga2V5U3BsaW5lcz0iMCAwLjUgMC41IDE7MCAwLjUgMC41IDE7MCAwLjUgMC41IDE7MCAwLjUgMC41IDEiIGJlZ2luPSItMS4zODg4ODg4ODg4ODg4ODg4cyI+PC9hbmltYXRlPgogIDxhbmltYXRlIGF0dHJpYnV0ZU5hbWU9ImN4IiByZXBlYXRDb3VudD0iaW5kZWZpbml0ZSIgZHVyPSIxLjg1MTg1MTg1MTg1MTg1MTZzIiBjYWxjTW9kZT0ic3BsaW5lIiBrZXlUaW1lcz0iMDswLjI1OzAuNTswLjc1OzEiIHZhbHVlcz0iMTY7MTY7MTY7NTA7ODQiIGtleVNwbGluZXM9IjAgMC41IDAuNSAxOzAgMC41IDAuNSAxOzAgMC41IDAuNSAxOzAgMC41IDAuNSAxIiBiZWdpbj0iLTEuMzg4ODg4ODg4ODg4ODg4OHMiPjwvYW5pbWF0ZT4KPC9jaXJjbGU+CjwhLS0gW2xkaW9dIGdlbmVyYXRlZCBieSBodHRwczovL2xvYWRpbmcuaW8vIC0tPjwvc3ZnPg=="
            v-on:click="translateFlag = !translateFlag"
          />
        </div>
      </div>
      <div class="box">
        <div class="textarea">
          {{ transWord }}
        </div>
        <div class="bottomBtn">
          <el-button type="primary" @click="changeName(0)">小驼峰命名</el-button>
          <el-button type="success" @click="changeName(1)">大驼峰命名</el-button>
          <el-button type="info" @click="recoverResult">还原</el-button>
          <el-button type="danger" @click="clearResult">清空</el-button>
        </div>
        <div class="copy" @click="copyText(transWord)">
          <i class="iconfont icon-fuzhi margin_r_5" style="font-size: 20px"></i>
          <div class="pointer">复制</div>
          <div v-if="copy" class="copied">已复制</div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {translate} from '@/api/translate'

export default {
  name: "TranslateView",
  data() {
    return {
      content: '',  //输入字符
      transWord: '',//翻译后
      transWordOrigin: '',
      copy: false,
      translateFlag: false,
      orgLangOptions: [{
        value: 'auto',
        label: '自动'
      }, {
        value: 'en',
        label: '英文'
      }, {
        value: 'zh',
        label: '中文'
      }, {
        value: 'fr',
        label: '法语'
      }, {
        value: 'de',
        label: '德语'
      }, {
        value: 'ru',
        label: '俄语'
      }],
      transLangOptions: [{
        value: 'en',
        label: '英文'
      }, {
        value: 'zh',
        label: '中文'
      }, {
        value: 'fr',
        label: '法语'
      }, {
        value: 'de',
        label: '德语'
      }, {
        value: 'ru',
        label: '俄语'
      }],
      orgValue: 'auto',
      transValue: 'en',
    }
  },
  methods: {
    empty() {
      this.content = ''
    },

    translate() {
      this.translateFlag = !this.translateFlag
      const data = {
        sourceText: this.content,
        source: this.orgValue,
        target: this.transValue,
      }
      translate(data) //调用翻译接口
        .then((res) => {
          this.transWord = res.data
          this.transWordOrigin = res.data
          this.translateFlag = !this.translateFlag
        })
        .catch((err) => {
          console.log(err)
        })
    },
    changeName(e) {
      if (e === 0) {
        // 将字符串转换为小驼峰命名
        this.transWord = this.transWord.replace(/(?:^\w|[A-Z]|\b\w)/g, (word, index) => {
          return index === 0 ? word.toLowerCase() : word.toUpperCase();
        }).replace(/\s+/g, '')
      } else if (e === 1) {
        // 将字符串转换为大驼峰命名
        this.transWord = this.transWord.replace(/(?:^\w|[A-Z]|\b\w)/g, (word) => {
          return word.toUpperCase();
        }).replace(/\s+/g, '')
      }
      // console.log(' this.transWord', this.transWord)
    },
    /*结果还原*/
    recoverResult() {
      this.transWord = this.transWordOrigin;
    },

    clearResult() {
      this.transWord = ""
      this.content = ""
    },

    copyText(copytext) {
      navigator.clipboard.writeText(copytext).then(() => {
        this.copy = true;
        setTimeout(() => {
          this.copy = false;
        }, 1000);
      });
    }
  }
};
</script>

<style scoped>
.bg {
  height: 100%;
  background: #f3f3f3;
  padding: 60px;
  margin: 0 auto;
}
::v-deep .el-input__inner {
  border: none;
  box-shadow: none;
  width: 85px;
  font-size: 17px;
  color: black;
  background-color: transparent;
}

.Transarea {
  width: 100%;
  font-size: 18px;
  display: flex;
  justify-content: center;
  padding-bottom: 5px;
  position: relative;
}

.orgLang,
.transLang {
  width: 40%;
  height: 32px;
  line-height: 32px;
  text-align: center;
  display: flex;
  align-items: center;
  box-sizing: border-box;
}

.transLang {
  padding-left: 4px;
}

.box {
  position: relative;
  width: 40%;
  margin-right: 10px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 9px 27px 0 rgb(0 0 0 / 8%);
  box-sizing: border-box;
  padding: 30px 28px 30px 32px;
}

.textarea {
  width: 100%;
  resize: none;
  height: 434px;
  background: #fff;
  overflow: hidden;
  border-radius: 4px;
  font-size: 14px;
  line-height: 24px;
  color: rgba(0, 0, 0, 0.65);
  box-sizing: border-box;
}

.wordNum {
  position: absolute;
  bottom: 16px;
  right: 14px;
  text-align: right;
  display: flex;
  line-height: 20px;
}

.arrowBtn {
  width: 60px;
  height: 60px;
  box-sizing: border-box;
  bottom: 44%;
  z-index: 2;
  right: -33px;
  border: 6px solid rgba(0, 0, 0, 0.15);
  border-radius: 50%;
  position: absolute;
  background: #fff;
  cursor: pointer;
}

.arrowBtn img {
  width: 100%;
  height: 55%;
  margin-top: 11px;
}

.close {
  position: absolute;
  top: 26px;
  right: 16px;
  cursor: pointer;
}

.bottomBtn {
  position: absolute;
  bottom: 16px;
  left: 80px;
  display: flex;
  align-items: center;
}

.copy {
  position: absolute;
  bottom: 450px;
  right: 15px;
  display: flex;
  align-items: center;
}

.copied {
  position: absolute;
  bottom: 26px;
  right: 0px;
  width: 50px;
  height: 24px;
  line-height: 24px;
  text-align: center;
  font-size: 12px;
  color: #858585;
  border-radius: 6px;
  border: 1px solid #d1cece;
}
</style>
