<template>
  <div>
    <el-card>
      <div ref="loginLogChart" :style="`zoom:${1.25*0.9*radio}`" class="loginLogChart"/>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {getLoginLogStatus} from "@/api/admin.js"

export default {
  name: 'LineChart',
  data() {
    return {
      radio: window.sessionStorage.getItem("ratio"),
      value: '',
      chart: null,
      xData: [],
      yData: [],
    }
  },

  created() {
    this.getLoginLogStatus()
    console.log("radio", this.radio)
  },


  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  methods: {
    getLoginLogStatus() {
      getLoginLogStatus()
          .then((res) => {
            console.log(res)
            this.xData = res.data.xData;
            this.yData = res.data.yData;
            this.$nextTick(() => {
              this.initChart();
            })
          })
    },


    initChart() {
      this.chart = echarts.init(this.$refs.loginLogChart)

      this.chart.setOption({
        title: {
          text: '用户活跃度',
          textStyle: {
            fontSize: 22,
            color: '#2d8cf0'
          },
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            type: 'shadow'
          },
          textStyle: {
            fontSize: '18',
            color: '#000'  // 设置文本颜色 默认#FFF
          },
          // formatter:function(params){
          //   console.log(params)
          //   return params[0].name;
          // }

        },
        legend: {
          data: ['活跃度']

        },
        grid: {
          left: '2%',
          right: '1%',
          bottom: '1%',
          containLabel: true
        },
        dataZoom: [
          {
            type: "slider",
            realtime: true, //拖动滚动条时是否动态的更新图表数据
            height: 25, //滚动条高度
            start: 0, //滚动条开始位置（共100等份）
            end: 100//结束位置（共100等份）
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
            name: '活跃度',
            type: 'line',
            stack: 'Total',
            data: this.yData
          }
        ]
      })
    }
  }
}

</script>

<style scoped>
.loginLogChart {
  width: 100%;
  height: 315px;
}
</style>

