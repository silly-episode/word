<template>
  <div>
    <el-card>
      <div ref="chartLine" class="charts"/>
    </el-card>
  </div>
</template>

<script>
import * as echarts from 'echarts'
// import {} from "@/api/admin.js"
export default {
  name: 'analysis',
  data() {
    return {
      value: '123',
      studyTotal: [],
    }
  },
  created() {
    // this.getachievement();

  },
  mounted() { this.createEcharts() },
  methods: {
    getachievement() {
      getachievement()
        .then((res) => {
          this.achievement = res.data;
          this.$nextTick(() => {
            this.createEcharts();
          })
        })
    },
    createEcharts() {
      let chartLine = echarts.init(this.$refs.chartLine)
      let option = {
        title: {
          left: 'center',
          text: '旅客时间分布',
          textStyle: {
            color: '#fff',
            fontSize: 35,
            fontWeight: 'normal'
          }
        },
        // 设置折线图的位置
        grid: {
          x: 50,
          y: 90,
          x2: 30,
          y2: 30
        },
        // 设置折线图的位置
        xAxis: {
          type: 'category',
          boundaryGap: false,
          axisLabel: {
            interval: 0,
            fontSize: 20
          },
          data: ['1月', '2月', '3月', '4月', '5月', '6月', '7月', '8月', '9月', '10月', '11月', '12月']
        },
        yAxis: {
          name: '人次',
          // y轴名称的位置
          nameTextStyle: {
            align: "right",
            fontSize: 20
          },
          type: 'value',
          min: 0,
          max: 100,
          // 只能设置偶数
          splitNumber: 6,
          axisLabel: {
            fontSize: 20
          }
        },
        series: [
          {
            data: [15, 23, 22, 21, 13, 17, 60, 22, 21, 13, 17, 60],
            type: 'line',
            // symbol: 'circle',     //设定为实心点
            symbolSize: 15,   //设定圆圈的大小

          }
        ]
      }
      // 使用刚指定的配置项和数据显示图表。
      chartLine.setOption(option);

    },
  }
}
</script>

<style scoped>
.charts {
  width: 90%;
  height: 70vh;
}
</style>
