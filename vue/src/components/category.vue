<template>
  <div>
    <el-card shadow="hover" style="height: 250px">
      <!--      <div slot="header" class="header" >-->
      <!--        <div class="category-header">-->
      <!--          <span>销售额类别占比</span>-->
      <!--        </div>-->
      <!--      </div>-->
      <div ref="charts" class="charts"/>
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
      value: '123',
      studyTotal: [],
    }
  },
  created() {
    if (this.totalFlag) {
      this.getArticleStudyTotal();
    } else {
      this.getWordModuleStudyTotal();
    }
  },
  methods: {
    getArticleStudyTotal() {
      getArticleStudyTotal()
        .then((res) => {
          // console.log('studyTsotal', res.data)
          this.studyTotal = res.data;
          this.$nextTick(() => {
            this.createEcharts();
          })
        })
    },

    getWordModuleStudyTotal() {
      getWordModuleStudyTotal()
        .then((res) => {
          // console.log('studyTotal', res.data)
          this.studyTotal = res.data;
          this.$nextTick(() => {
            this.createEcharts();
          })
        })
    },
    createEcharts() {
      const Chart = echarts.init(this.$refs.charts)
      Chart.setOption({
        title: {
          text: '',
          subtext: '',
          left: 'center',
          top: 'center'
        },
        tooltip: {
          trigger: 'item'
        },
        series: [
          {
            name: '详细数据',
            type: 'pie',
            radius: ['40%', '70%'],
            center: ['50%', '32%'],
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
.charts {
  width: 100%;
  height: 350px;
}
</style>
