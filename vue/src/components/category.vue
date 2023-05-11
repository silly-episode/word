<template>
  <div>
    <el-card shadow="hover">
      <div slot="header" class="header">
        <div class="category-header">
          <span :class="`${this.headTitle}`" v-text="cardTitle"></span>
        </div>
      </div>
      <div ref="pieChart" :style="`zoom:${1.25*0.9*radio}`" class="pieChart"/>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {getArticleStudyTotal, getWordModuleStudyTotal} from "@/api/admin.js"

export default {
  name: 'Category',
  props: {
    totalFlag: {
      type: Boolean,
      required: true
    },
  },
  data() {
    return {
      radio: window.sessionStorage.getItem("ratio"),
      value: '',
      studyTotal: [],
      cardTitle: "",
      headTitle: "",
      centerTip: ""
    }
  },
  created() {
    if (this.totalFlag) {
      this.getArticleStudyTotal();
      this.cardTitle = "英语文章"
      this.headTitle = "articleTitle"
      this.centerTip = "练习次数"
    } else {
      this.getWordModuleStudyTotal();
      this.cardTitle = "单词模块"
      this.headTitle = "moduleTitle"
      this.centerTip = "学习人数"
    }
  },
  methods: {
    getArticleStudyTotal() {
      getArticleStudyTotal()
        .then((res) => {
          this.studyTotal = res.data;
          this.$nextTick(() => {
            this.createEcharts();
          })
        })
    },

    getWordModuleStudyTotal() {
      getWordModuleStudyTotal()
        .then((res) => {
          this.studyTotal = res.data;
          this.$nextTick(() => {
            this.createEcharts();
          })
        })
    },
    createEcharts() {
      const Chart = echarts.init(this.$refs.pieChart)
      Chart.setOption({
        title: {
          text: this.centerTip,
          subtext: '',
          left: 'center',
          top: 'center',
          textStyle: {
            fontSize: 22
          },
          subtextStyle: {
            fontSize: 20
          }
        },
        tooltip: {
          trigger: 'item',
          textStyle: {
            fontSize: '18',
            color: '#000'  // 设置文本颜色 默认#FFF
          },
        },
        // legend: {
        //   bottom: 'center',
        //   left: '70%',
        //   type: "scroll",
        //   orient: 'vertical',
        //   show: true,
        // },
        series: [
          {
            name: '详细数据',
            type: 'pie',
            radius: ['60%', '95%'],
            // center:['34%','50%'],
            avoidLabelOverlap: false,
            itemStyle: {
              borderRadius: 5,
              borderColor: '#fff',
              borderWidth: 2
            },
            label: {
              show: false,
              position: 'center'
            },
            labelLine: {
              show: false
            },
            data: this.studyTotal
          }
        ],
      })
      Chart.on('mouseover', (par) => {
        // 获取鼠标移上的数据
        const { name, value } = par
        Chart.setOption({
          title: {
            text: name,
            subtext: value
          }
        })
      })
    }
  },

}
</script>

<style scoped>

.moduleTitle {
  font-size: 22px;
  color: #88cf8f;
  display: flex;
  justify-content: center;
  align-items: center;
}


.articleTitle {
  font-size: 22px;
  color: #e46569;
  display: flex;
  justify-content: center;
  align-items: center;
}


.pieChart {
  width: 100%;
  height: 300px;
}
</style>
