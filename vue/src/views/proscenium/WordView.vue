<template>
  <div>
    <div class="word">possess</div>
    <div class="card recordCard flex_center">
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
      <div class="flex-1 flex_column_center_center">
        <span class="numCss">00:12</span>
        <span class="textCss">时间</span>
      </div>
    </div>
  </div>
</template>

<script>
import { getWord } from '@/api/word.js'
export default {
  name: "WordView",
  components: {},
  created() {
    this.getList()
  },
  methods: {
    getList() {
      getWord({ userId: 1 })
        .then((res) => {
          console.log('res', res.data)
        })
        .catch((err) => {
          console.log('err', err)
        })
    }
  }
};
</script>

<style scoped>
Page {
  background-color: #faf9ff;
}
.card {
  background: #fff;
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
  position: absolute;
  left: 25%;
  top: 70%;
  width: 50%;
  padding: 40px 16px;
  border-radius: 10px;
}

.word {
  position: absolute;
  left: 42%;
  top: 35%;
  font-size: 60px;
  letter-spacing: 7px;
  color: #4b5563;
}
</style>