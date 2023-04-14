<template>
  <el-container>
    <el-aside width="15%"></el-aside>
    <el-main>
      <div></div>
    </el-main>
    <el-aside width="15%"></el-aside>
  </el-container>
</template>

<script>
import { userInfo } from '@/api/user'
import { avatarUrl } from '@/utils/img.js'
export default {
  methods: {
    getUserInfo() {
      userInfo()
        .then((res) => {
          // console.log(res)
          if (res.code == 200) {
            this.userInfo = res.data
            // this.headImage = avatarUrl(res.data.userId)
          }
        })
        .catch((err) => {
          console.log('err', err)
        })
    },
  },
  created() {
    // this.getUserInfo()
  },
}
</script>

<style>
</style>