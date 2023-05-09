<template>
  <div>

    <!--      <div slot="header" class="header">-->
    <!--        <div class="category-header">-->
    <!--          <span>销售额类别占比</span>-->
    <!--        </div>-->
    <!--      </div>-->
    <el-card>

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
  mounted() {
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
          name: 'Access From',
          type: 'pie',
          radius: ['40%', '70%'],
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
      grid: {
        left: 0,
        top: 0,
        right: 0,
        bottom: 0,
        containLabel: true  // 这个啥？看下面
      },
    })
    Chart.on('mouseover', (par) => {
      // 获取鼠标移上的数据
      const {name, value} = par
      Chart.setOption({
        title: {
          text: name,
          subtext: value
        }
      })
    })
  },
  methods: {
    getArticleStudyTotal() {
      getArticleStudyTotal()
          .then((res) => {
            this.studyTotal = res.data;
          })
    },

    getWordModuleStudyTotal() {
      getWordModuleStudyTotal()
          .then((res) => {
            this.studyTotal = res.data;
          })
    },

  },

}
</script>

<style scoped>
.charts {
  width: 100%;
  height: 350px;
}
</style>
