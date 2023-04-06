<template>
  <el-dialog
      :visible="visible"
      style="margin: 0 0 0 12%"
      title="查看用户信息"
      width="60%"
      @close="closed"
  >
    <el-form
        ref="formRef"
        :model="editForm"
        label-width="120px"
        style="margin: 0px 7%"
    >
      <el-form-item v-for="item in cols" :key="item.lable" :label="item.lable" :prop="item.props">
        <el-input v-model="editForm[item.props]" disabled></el-input>
      </el-form-item>
    </el-form>
  </el-dialog>
</template>

<script>
export default {
  name: 'UserInfo',
  data() {
    return {
      visible: false,
      editForm: {},// 在修改信息时查询到的用户信息对象
      cols: [
        {
          lable: '账号',
          props: 'account'
        }, {
          lable: '邮箱',
          props: 'email'
        }, {
          lable: '积分',
          props: 'integration'
        }, {
          lable: '锁定/解锁时间',
          props: 'lockTime'
        }, {
          lable: '昵称',
          props: 'nickName'
        }, {
          lable: 'qq',
          props: 'qq'
        },
        {
          lable: '注册时间',
          props: 'registerTime'
        },
        {
          lable: '描述',
          props: 'remark'
        },
        {
          lable: '签名',
          props: 'signature'
        },
        {
          lable: '电话',
          props: 'tel'
        },
        {
          lable: 'userId',
          props: 'userId'
        },
        {
          lable: '用户状态',
          props: 'userStatus'
        },
        {
          lable: '微信',
          props: 'wechat'
        },
      ]
    }
  },
  methods: {
    showEditDialog(data) {
      console.log('从表格中获取到的用户信息', data)
      this.visible = true
      if (data) this.editForm = data;
    },

    // 对话框的关闭
    closed() {
      this.visible = false
      this.editForm = {}
      this.$refs.formRef.resetFields();
    },
  }
}
</script>

<style>
</style>
