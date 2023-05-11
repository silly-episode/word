<template>
  <div>
    <el-card>
      <!--       :style="`zoom:${1.25*radio}`"-->
      <div ref="resultChart" class="resultChart"/>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {examResult} from "@/api/user.js"

export default {
  name: 'analysis',
  data() {
    return {
      radio: window.sessionStorage.getItem("ratio"),
      value: '',
      chart: null,
      xData: [],
      yData1: [],
      yData2: [],
      yData3: [],
      endIndex: "",

    }
  },

  created() {
    this.examResult()
  },


  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  methods: {
    examResult() {
      examResult()
          .then((res) => {
            console.log(res)
            this.xData = res.data.recordIndex;
            this.yData1 = res.data.planResults;
            this.yData2 = res.data.articleResults;
            this.yData3 = res.data.gameResults;
            this.endIndex = res.data.endIndex;
            this.$nextTick(() => {
              this.initChart();
            })
          })
    },


    initChart() {
      this.chart = echarts.init(this.$refs.resultChart)

      this.chart.setOption({
        title: {
          text: '成绩记录'
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          // formatter:function(params){
          //   console.log(params)
          //   return params[0].name;
          // }

        },
        legend: {
          data: ['计划成绩', '文章练习', '游戏得分']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        dataZoom: [
          {
            type: "slider",
            realtime: true, //拖动滚动条时是否动态的更新图表数据
            height: 25, //滚动条高度
            start: 0, //滚动条开始位置（共100等份）
            end: this.endIndex//结束位置（共100等份）
          }
        ],
        //?
        toolbox: {
          feature: {
            saveAsImage: {}
          }
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.xData
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '游戏得分',
            type: 'line',
            stack: 'Total',
            data: this.yData3
          },
          {
            name: '计划成绩',
            type: 'line',
            stack: 'Total',
            data: this.yData1
          },
          {
            name: '文章练习',
            type: 'line',
            stack: 'Total',
            data: this.yData2
          }
        ]
      })
    }
  }
}

</script>

<style scoped>
.resultChart {
  width: 100%;
  height: 377px;
}
</style>

