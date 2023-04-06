<template>
  <div>
    <el-header class="bg_f4">
      <img
        class="radius_per50 wid_hei_50_50"
        mode="aspectFill"
        src="@/assets/logo.png"
        alt=""
      />
      <span class="margin_l_15">Word背单词</span>

      <el-menu
        :default-active="activeIndex"
        class="el-menu-demo"
        mode="horizontal"
        @select="handleSelect"
      >
        <el-menu-item index="1">单词模板</el-menu-item>
        <el-menu-item index="2">我的单词</el-menu-item>
      </el-menu>
    </el-header>
    <!-- <nav>
      <router-link to="/word">Word</router-link>
      |
      <router-link to="/translate">Translate</router-link>
    </nav>
    <router-view/> -->
  </div>
</template>

<script>

export default {
  name: "HomeView",
};
</script>
