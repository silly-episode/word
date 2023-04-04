<template>
  <div>
    <!-- 面包屑导航区 -->
    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>用户</el-breadcrumb-item>
      <el-breadcrumb-item>用户管理</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="min-height: 100%">
      <!-- 搜索与添加区域 -->
      <el-row :gutter="20">
        <el-col :span="7">
          <el-input
              v-model="queryInfo.query"
              clearable
              placeholder="请输入内容"
              @clear="getUserList"
          >
            <el-button
                slot="append"
                icon="el-icon-search"
                @click="getUserList"
            ></el-button>
          </el-input>
        </el-col>
        <el-col :span="4">
          <el-button type="primary" @click="addDialogVisible = true"
          >添加用户
          </el-button
          >
        </el-col>
      </el-row>
      <!-- 用户列表区 -->
      <el-table :data="userlist" border stripe>
        <el-table-column label="序号" type="index"></el-table-column>
        <el-table-column label="账号" prop="account"></el-table-column>
        <el-table-column label="用户名" prop="nickName"></el-table-column>
        <el-table-column label="注册时间" prop="registerTime"></el-table-column>
        <el-table-column label="用户状态" prop="userStatus"></el-table-column>
        <el-table-column label="描述">
          <!-- 作用域插槽 -->
          <template v-slot="scope">
            <el-switch
                v-model="scope.row.mg_state"
                @change="userStateChanged(scope.row)"
            >
            </el-switch>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200px">
          <template v-slot="scope">
            <!-- 修改按钮 -->
            <el-button
                icon="el-icon-edit"
                size="mini"
                type="primary"
                @click="showEditDialog(scope.row.id)"
            ></el-button>
            <!-- 删除按钮 -->
            <el-button
                icon="el-icon-delete"
                size="mini"
                type="danger"
                @click="removeUserById(scope.row.id)"
            ></el-button>
            <!-- 分配角色按钮 -->
            <el-tooltip
                :enterable="false"
                content="分配角色"
                effect="dark"
                placement="top"
            >
              <el-button
                  icon="el-icon-setting"
                  size="mini"
                  type="warning"
                  @click="setRole(scope.row)"
              ></el-button>
            </el-tooltip>
          </template>
        </el-table-column>
      </el-table>
      <!-- 分页区 -->
      <el-pagination
          :current-page="queryInfo.pageNum"
          :page-size="queryInfo.pageSize"
          :page-sizes="[10, 30, 50]"
          :total="total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="handleCurrentChange"
      >
      </el-pagination>
      <!-- 添加用户的对话框 -->
      <el-dialog
          :visible.sync="addDialogVisible"
          title="添加用户"
          width="50%"
          @close="addDialogClose"
      >
        <!-- 对话框内容主体区 -->
        <el-form
            ref="addFormRef"
            :model="addForm"
            :rules="addFormRules"
            label-width="70px"
        >
          <el-form-item label="用户名" prop="username">
            <el-input v-model="addForm.username"></el-input>
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="addForm.password"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="addForm.email"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="mobile">
            <el-input v-model="addForm.mobile"></el-input>
          </el-form-item>
        </el-form>
        <!-- 对话框底部按钮区 -->
        <span slot="footer" class="dialog-footer">
          <el-button @click="addDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="addUser">确 定</el-button>
        </span>
      </el-dialog>
      <!-- 修改用户的对话框 -->
      <el-dialog
          :visible.sync="editDialogVisible"
          title="修改用户"
          width="50%"
          @close="editDialogClosed"
      >
        <!-- 对话框主体表单内容 -->
        <el-form
            ref="editFormRef"
            :model="editForm"
            :rules="editFormRules"
            label-width="70px"
        >
          <el-form-item label="用户名">
            <el-input v-model="editForm.username" disabled></el-input>
          </el-form-item>
          <el-form-item label="邮箱" prop="email">
            <el-input v-model="editForm.email"></el-input>
          </el-form-item>
          <el-form-item label="电话" prop="mobile">
            <el-input v-model="editForm.mobile"></el-input>
          </el-form-item>
        </el-form>
        <!-- 对话框底部 -->
        <span slot="footer" class="dialog-footer">
          <el-button @click="editDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="editUserInfo">确 定</el-button>
        </span>
      </el-dialog>
    </el-card>
    <!-- 分配角色的对话框 -->
    <el-dialog
        :visible.sync="setRoleDialogVisible"
        title="分配角色"
        width="50%"
        @close="setRoleDialogClosed"
    >
      <div>
        <p>当前用户：{{ userInfo.username }}</p>
        <p>当前角色：{{ userInfo.role_name }}</p>
        <p>
          分配新角色：
          <el-select v-model="selectedRoleId" placeholder="请选择">
            <el-option
                v-for="item in rolesList"
                :key="item.id"
                :label="item.roleName"
                :value="item.id"
            >
            </el-option>
          </el-select>
        </p>
      </div>
      <span slot="footer" class="dialog-footer">
        <el-button @click="setRoleDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="saveRoleInfo">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {userSearch} from "@/api/admin.js"

export default {
  data() {
    return {
      // // 验证邮箱的自定义规则
      // var checkEmail = (rule, value, cb) => {
      //   // 验证邮箱的正则表达式
      //   const regEmail =
      //     /^[\da-z]+([\-\.\_]?[\da-z]+)*@[\da-z]+([\-\.]?[\da-z]+)*(\.[a-z]{2,})+$/;
      //   if (regEmail.test(value)) {
      //     // 合法的邮箱
      //     return cb();
      //   }
      //   cb(new Error("请输入合法的邮箱"));
      // },
      // // 验证电话号码的自定义规则
      // var checkMobile =  (rule, value, cb) => {
      //   // 验证手机号的正则表达式
      //   const regMobile = /^1[3-9]\d{9}$/;
      //   if (regMobile.test(value)) {
      //     // 合法的手机号
      //     return cb();
      //   }
      //   cb(new Error("请输入合法的电话号码"));
      // },
      // 获取用户列表的参数对象
      queryInfo: {
        /*开始时间*/
        beginTime: "",
        /*结束时间*/
        endTime: "",
        /*账号、电话、用户名、用户id*/
        accountOrTelOrNickNameOrUserId: "",
        /*用户状态*/
        userStatus: "",
        /*积分升序或降序,*/
        integrationOrderByAsc: false,
        // 查询参数
        query: "",
        // 当前页码
        pageNum: 1,
        // 每页显示条数
        pageSize: 5,
      },
      // 用于保存获取到的用户列表
      userList: [],
      // 总数据条数
      total: 0,
      // 控制添加用户对话框的显示与隐藏
      addDialogVisible: false,
      // 添加用户的表单数据
      addForm: {
        username: "",
        password: "",
        email: "",
        mobile: "",
      },
      // 添加用户表单的验证规则对象
      addFormRules: {
        username: [
          {required: true, message: "请输入用户名", trigger: "blur"},
          {
            min: 3,
            max: 10,
            message: "用户名长度在3~10个字符之间",
            trigger: "blur",
          },
        ],
        password: [
          {required: true, message: "请输入密码", trigger: "blur"},
          {
            min: 6,
            max: 15,
            message: "密码长度在6~15个字符之间",
            trigger: "blur",
          },
        ],
        email: [
          {required: true, message: "请输入邮箱", trigger: "blur"},
          // { validator: checkEmail, trigger: "blur" },
        ],
        mobile: [
          {required: true, message: "请输入电话", trigger: "blur"},
          // { validator: checkMobile, trigger: "blur" },
        ],
      },
      // 控制修改用户对话框的显示与隐藏
      editDialogVisible: false,
      // 在修改用户信息时查询到的用户信息对象
      editForm: {},
      // 修改表单的验证规则对象
      editFormRules: {
        email: [
          {required: true, message: "请修改邮箱", trigger: "blur"},
          // { validator: checkEmail, trigger: "blur" },
        ],
        mobile: [
          {required: true, message: "请修改电话", trigger: "blur"},
          // { validator: checkMobile, trigger: "blur" },
        ],
      },
      // 控制分配角色对话框的显示与隐藏
      setRoleDialogVisible: false,
      // 需要被分配角色的用户信息
      userInfo: {},
      // 所有角色的数据列表
      rolesList: [],
      // 已选中的角色id值
      selectedRoleId: "",
    };
  },
  created() {
    // 发送数据请求，获取用户列表数据
    this.userSearch();
  },
  methods: {
    // async getUserList() {
    //   const { data: res } = await this.$http.get("users", {
    //     params: this.queryInfo,
    //   });
    //   if (res.meta.status !== 200) return this.$message("获取用户列表失败！");
    //   this.userlist = res.data.users;
    //   this.total = res.data.total;
    //   console.log(res);
    // },
    userSearch() {
      let params = this.queryInfo;
      userSearch(params)
          .then((res) => {
            console.log(res)
            this.userlist = res.data.records
            this.total = res.data.total
          })
          .catch((err) => {
            console.log(err.message)
          })

    },

    // 监听pagesize的改变
    handleSizeChange(newSize) {
      this.queryInfo.pagesize = newSize;
      this.getUserList();
    },
    // 监听页码值改变
    handleCurrentChange(newPage) {
      this.queryInfo.pagenum = newPage;
      // 页码值改变则发起新的数据请求
      this.getUserList();
    },
    // 监听switch开关状态的改变
    async userStateChanged(userinfo) {
      // 状态改变了，需要改变数据库里的值
      const {data: res} = await this.$http.put(
          `users/${userinfo.id}/state/${userinfo.mg_state}`
      );
      console.log(res);
      if (res.meta.status !== 200) {
        // 如果操作失败，数据库的值没有改变，页面的值也要重置
        userinfo.mg_state = !userinfo.mg_state;
        return this.$message.error("更新用户状态失败！");
      }
      this.$message.success("更新用户状态成功！");
    },
    // 监听添加用户对话框的关闭事件，关闭后重置表单
    addDialogClose() {
      this.$refs.addFormRef.resetFields();
    },
    // 添加新用户
    addUser() {
      // addDialogVisible = false
      this.$refs.addFormRef.validate(async (valid) => {
        // 进行预校验，如果预校验没通过就不进行添加
        if (!valid) return;
        // 如果预校验通过则可以发起添加的网络请求
        const {data: res} = await this.$http.post("users", this.addForm);
        if (res.meta.status !== 201) {
          this.$message.error("添加用户失败！");
        }
        this.$message.success("添加用户成功！");
        // 隐藏添加用户的对话框
        this.addDialogVisible = false;
        // 新增后，刷新用户列表
        this.getUserList();
      });
    },
    // 展示编辑用户的对话框
    async showEditDialog(id) {
      // 根据id获取用户信息
      const {data: res} = await this.$http.get("users/" + id);
      if (res.meta.status !== 200) {
        return this.$message.error("查询用户信息失败");
      }
      // 保存查询到的用户信息
      this.editForm = res.data;
      this.editDialogVisible = true;
    },
    // 监听修改用户对话框的关闭事件
    editDialogClosed() {
      this.$refs.editFormRef.resetFields();
    },
    // 修改用户信息并提交
    editUserInfo() {
      // 点击确认后先进行预验证
      this.$refs.editFormRef.validate(async (valid) => {
        if (!valid) return;
        // 预校验通过后，发起用户信息修改的数据请求
        const {data: res} = await this.$http.put(
            "users/" + this.editForm.id,
            {
              email: this.editForm.email,
              mobile: this.editForm.mobile,
            }
        );
        if (res.meta.status !== 200) {
          return this.$message.error("更新用户信息失败！");
        }
        // 如果更新成功了就隐藏对话框，刷新用户列表
        this.editDialogVisible = false;
        this.getUserList();
        this.$message.success("更新用户信息成功！");
      });
    },
    // 根据id删除对应的用户信息
    async removeUserById(id) {
      // 弹框询问用户是否删除数据，如果取消删除，则用catch捕获错误
      const confirmResult = await this.$confirm(
          "此操作将永久删除该用户, 是否继续?",
          "提示",
          {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning",
          }
      ).catch((err) => err);
      // 如果用户确认删除，confirmResult返回字符串confirm
      // 如果用户取消删除，confirmResult返回字符串cancel
      if (confirmResult !== "confirm") {
        return this.$message.info("已经取消删除！");
      }
      // 发送请求删除数据
      const {data: res} = await this.$http.delete("users/" + id);
      if (res.meta.status !== 200) {
        return this.$message.error("删除用户失败！");
      }
      this.$message.success("删除用户成功！");
      this.getUserList();
    },
    // 展示分配角色的对话框
    async setRole(userinfo) {
      this.userInfo = userinfo;
      // 在展示对话框之前，获取所有角色的列表
      const {data: res} = await this.$http.get("roles");
      if (res.meta.status !== 200) {
        return this.$message.error("获取角色列表失败！");
      }
      this.rolesList = res.data;
      this.setRoleDialogVisible = true;
    },
    // 点击按钮分配角色
    async saveRoleInfo() {
      if (!this.selectedRoleId) {
        // 若用户未选择角色
        return this.$message.error("请选择要分配的角色！");
      }
      // 发起请求保存用户的操作
      const {data: res} = await this.$http.put(
          `users/${this.userInfo.id}/role`,
          {rid: this.selectedRoleId}
      );
      if (res.meta.status !== 200) {
        return this.$message.error("更新用户角色失败！");
      }
      this.$message.success("更新用户角色成功！");
      this.getUserList();
      this.setRoleDialogVisible = false;
    },
    // 监听分配角色对话框的关闭
    setRoleDialogClosed() {
      this.selectedRoleId = "";
      this.userInfo = {};
    },
  },
};
</script>

<style scoped>
</style>
