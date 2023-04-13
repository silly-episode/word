<template>
  <el-container>
    <el-aside width="16%">
      <ul class="nav">
        <li
          :class="`${currentIndex == index ? 'selected' : ''}`"
          v-for="(item, index) in lis"
          :key="item.icon"
          @click="selectTab(index)"
        >
          <div
            :class="`${currentIndex == index ? 'selectedTriangle' : ''}`"
          ></div>
          <span :class="`iconfont icon-${item.icon}`"></span>
          {{ item.name }}
        </li>
      </ul>
    </el-aside>
    <el-main class="t">
      <el-carousel :interval="4000" type="card" height="200px">
        <el-carousel-item
          @click.native="goTo(item.moduleId)"
          v-for="item in rotationChart"
          :key="item.moduleId"
        >
          <el-image :src="item.moduleImagePath"></el-image>
        </el-carousel-item>
      </el-carousel>
      <div class="margin_15_0 font_20 font_bold">词表</div>
      <div class="flex_wrap_center">
        <div
          class="box"
          @click="goTo(item.moduleId)"
          v-for="item in bookList"
          :key="item.moduleId"
        >
          <p class="font_bold font_16 margin_b_5 black">
            {{ item.moduleName }}
          </p>
          <p class="line_hei_18">
            {{ (item.wordCount / 20).toFixed(0) }}课，{{ item.wordCount }}词
          </p>
          <p class="line_hei_18">{{ item.studyNumber }}人背诵</p>
        </div>
      </div>
    </el-main>
    <el-aside width="29%">
      <div class="sidebox">
        <div class="bg_grey font_14 font_bold padding_10">新增词表</div>
        <ol class="cibiao">
          <li
            class="flex_center"
            v-for="(item, index) in rotationChart"
            :key="item.moduleId"
            @click="goTo(item.moduleId)"
          >
            <span> {{ index + 1 }} </span>
            {{ item.moduleName }}
          </li>
        </ol>
      </div>
      <div class="sidebox">
        <div class="bg_grey font_14 font_bold padding_10">热门词表</div>
        <ol class="cibiao">
          <li
            class="flex_center"
            v-for="(item, index) in hotList"
            :key="item.moduleId"
            @click="goTo(item.moduleId)"
          >
            <span
              :class="`${
                index == 0 || index == 1 || index == 2
                  ? 'bg_orange'
                  : 'bg_c1_grey'
              } white`"
            >
              {{ index + 1 }}
            </span>
            {{ item.moduleName }}
          </li>
        </ol>
      </div>
    </el-aside>
  </el-container>
</template>

<script>
import { newWordModule, wordModuleBySuperior, hotWordModule } from '@/api/wordList'
import { imageUrl } from '@/utils/img.js'
export default {
  data() {
    return {
      currentIndex: 0,
      rotationChart: [],//轮播图/新增 词表
      hotList: [],  //热门词表
      bookList: [],  //中间放的词表
      lis: [
        {
          name: '出国必用',
          icon: 'chuguo'
        },
        {
          name: '大学词汇',
          icon: 'daxue'
        }, {
          name: '高中词汇',
          icon: 'gaozhong'
        }, {
          name: '初中词汇',
          icon: 'chuzhong'
        }, {
          name: '小学词汇',
          icon: 'xiaoxue'
        }]
    }
  },
  methods: {
    getRotationChart() {
      newWordModule()
        .then((res) => {
          // console.log(res.data)
          if (res.code == 200) this.rotationChart = this.trunUrl(res.data)
        })
        .catch((err) => {
          console.log(err)
        })
    },

    gethotList() {
      hotWordModule()
        .then((res) => {
          // console.log(res.data)
          if (res.code == 200) this.hotList = res.data
        })
        .catch((err) => {
          console.log(err)
        })
    },

    trunUrl(data) {
      data.forEach((item) => {
        item.moduleImagePath = imageUrl(item.moduleId)
      })
      return data
    },

    selectTab(index) {
      this.currentIndex = index
      this.getList()
    },

    getList() {
      wordModuleBySuperior(this.currentIndex)
        .then((res) => {
          // console.log(res)
          if (res.code == 200) this.bookList = res.data
        })
        .catch((err) => {
          console.log(err)
        })
    },

    goTo(moduleId) {
      this.$router.push({
        name: 'module',
        params: { moduleId }
      })
    }
  },
  created() {
    this.getRotationChart()
    this.getList()
    this.gethotList()
  }
}
</script>

<style scoped>
.t {
  border-left: 1px solid #ccc;
  border-right: 1px solid #ccc;
}

.sidebox {
  width: 224px;
  border: 1px solid #e1e1e1;
  background: #fff;
  border-radius: 3px;
  margin: 20px 0 0 30px;
}

.cibiao {
  padding: 6px 10px;
}

.cibiao li {
  line-height: 30px;
  overflow: hidden;
}

.cibiao li span {
  width: 16px;
  height: 16px;
  display: block;
  line-height: 16px;
  font-weight: bold;
  text-align: center;
  font-size: 12px;
  margin-right: 10px;
}

.selected {
  background: #3e87e5;
  color: white;
}

.selectedTriangle {
  position: absolute;
  top: 30px;
  right: -20px;
  width: 0;
  height: 0;
  border: 10px solid transparent;
  border-left-color: #3e87e5;
  line-height: 0; /* 这两行可加可不加 */
  font-size: 0; /* 是为了照顾低版本浏览器 */
}

.nav {
  margin-left: 99px;
}

.nav li {
  text-align: center;
  width: 88px;
  position: relative;
  border: 1px solid #f7f7f7;
  border-right: none;
  padding: 15px 0;
  margin: 15px 0;
  cursor: pointer;
  /* font-weight: bold; */
}

.nav li span {
  display: block;
  font-size: 30px;
  margin: 0 auto 8px;
}

.box {
  width: 150px;
  height: 90px;
  box-sizing: border-box;
  border-radius: 5px;
  font-size: 13px;
  padding: 15px 0;
  border: 1px solid #eeeeee;
  background-color: #f6f6f6;
  color: #777;
  margin-bottom: 14px;
  margin-left: 14px;
  text-align: center;
  cursor: pointer;
}
</style>