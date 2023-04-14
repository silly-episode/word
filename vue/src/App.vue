<template>
  <div id="building">
    <div id="app">
      <!-- 路由占位符 -->
      <router-view></router-view>
    </div>
  </div>
</template>
<script>

</script>
<style>
#app {
  font-family: "Avenir", Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 0px;
}

body {
  margin: 0;
  padding: 0;
  border: 0;
}

#building {
  background: url("@/assets/布偶猫.jpeg");
  width: 100%;
  height: 100%;
  position: fixed;
  background-size: 100% 100%;
}
</style>


