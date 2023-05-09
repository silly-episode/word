<template>
  <div ref="charts" class="charts"/>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'LineChart',
  props: {
    xData: {
      type: Array,
      required: true
    },
    yData1: {
      type: Array,
      required: true
    },
    yData2: {
      type: Array,
      required: true
    }
  },
  data() {
    return {
      value: '全部渠道',
      chart: null
    }
  },
  watch: {
    pie_percent: {
      handler(newVal, oldVal) {
        if (newVal.length > 0) {

        }
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.$nextTick(() => {
      this.initChart()
    })
  },
  methods: {
    initChart() {
      this.chart = echarts.init(this.$refs.charts)

      this.chart.setOption({
        title: {
          text: '年销售折线图'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['销售量', '销售额']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
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
            name: '销售量',
            type: 'line',
            stack: 'Total',
            data: this.yData1
          },
          {
            name: '销售额',
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
.charts {
  width: 420px;
  height: 390px;
}
</style>
