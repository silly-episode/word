<template>
  <div id="app">
    <!-- 路由占位符 -->
    <router-view></router-view>
  </div>
</template>
<script>

</script>
<style>
* {
  margin: 0;
  padding: 0;
}

body,
input,
select,
textarea {
  font: 14px/1 Microsoft Yahei, Helvetica, PingFang SC, Helvetica Neue,
  sans-serif, Arial, Verdana;
  color: #333;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}

input,
select,
textarea {
  outline: none;
  border: none;
  border-radius: 0;
  -moz-appearance: none;
  appearance: none;
  -webkit-appearance: none;
  background-color: transparent;
  box-sizing: content-box;
}

img {
  vertical-align: middle;
  border-style: none;
}

.pointer {
  cursor: pointer;
}

.hide {
  display: none;
}

/* 超出省略号 */
.ellipsis {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

/* 文本居中 */
.text_center {
  text-align: center;
}

/* 固定盒子大小 */
.box-size {
  box-sizing: border-box;
}

/* 圆角 */
.radius_per50 {
  border-radius: 50%;
}

.radius_20 {
  border-radius: 20px;
}

/* flex布局 */
.flex {
  display: flex;
}

.flex1 {
  flex: 1;
}

.flex_end {
  display: flex;
  justify-content: flex-start;
  align-items: flex-end;
}

.flex_center {
  display: flex;
  justify-content: flex-start;
  align-items: center;
}

.flex_center_center {
  display: flex;
  justify-content: center;
  align-items: center;
}

.flex_between_center {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.flex_between_wrap_center {
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  align-items: center;
}

.flex_wrap_center {
  display: flex;
  justify-content: flex-start;
  flex-wrap: wrap;
  align-items: center;
}

.flex_column_center {
  display: flex;
  flex-direction: column;
  align-items: center;
}

/* margin-padding */

.margin_15_0 {
  margin: 15px 0;
}

.margin_t_10 {
  margin-top: 10px;
}

.margin_t_20 {
  margin-top: 20px;
}

.margin_t_50 {
  margin-top: 50px;
}

.margin_r_5 {
  margin-right: 5px;
}

.margin_b_5 {
  margin-bottom: 5px;
}

.margin_b_10 {
  margin-bottom: 10px;
}

.margin_b_15 {
  margin-bottom: 15px;
}

.margin_b_20 {
  margin-bottom: 20px;
}

.margin_b_30 {
  margin-bottom: 30px;
}

.margin_t_b_10 {
  margin: 10px 0;
}

.padding_t_20 {
  padding-top: 20px;
}

/* 字体 */
.font_bold {
  font-weight: bold;
}

.font_20 {
  font-size: 20px;
}

/* 颜色 */
.blue {
  color: #1296db;
}

.white {
  color: #fff;
}

.black {
  color: #000;
}

/* 背景色 */
.bg_grey {
  background-color: #eee;
}

.bg_c1_grey {
  background-color: #c1c1c1;
}

.bg_white {
  background-color: #fff;
}

.line_hei_25 {
  line-height: 25px;
}

.line_hei_40 {
  line-height: 40px;
}

/* 宽高 */
.wid_hei_20_20 {
  width: 20px;
  height: 20px;
}

.wid_hei_30_30 {
  width: 30px;
  height: 30px;
}

.wid_hei_40_40 {
  width: 40px;
  height: 40px;
}

.wid_hei_50_50 {
  width: 50px;
  height: 50px;
}

.wid_hei_200_200 {
  width: 200px;
  height: 200px;
}

.wid_hei_per100 {
  width: 100%;
  height: 100%;
}

.wid_per30 {
  width: 30%;
}

.wid_per33 {
  width: 33%;
}

.wid_per40 {
  width: 40%;
}

.wid_per50 {
  width: 50%;
}

.wid_per55 {
  width: 55%;
}

.wid_per60 {
  width: 60%;
}

.wid_per90 {
  width: 90%;
}

.wid_per100 {
  width: 100%;
}

.hei_per100 {
  height: 100%;
}

.hei_10 {
  height: 10px;
}

.hei_20 {
  height: 20px;
}

.hei_30 {
  height: 30px;
}

.hei_40 {
  height: 40px;
}

.hei_50 {
  height: 50px;
}

.hei_100 {
  height: 100px;
}

.hei_150 {
  height: 150px;
}
</style>


