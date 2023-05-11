<template>
  <div>

    <el-breadcrumb separator-class="el-icon-arrow-right">
      <el-breadcrumb-item :to="{ path: '/home' }">首页</el-breadcrumb-item>
      <el-breadcrumb-item>系统监控</el-breadcrumb-item>
      <el-breadcrumb-item>服务信息</el-breadcrumb-item>
    </el-breadcrumb>
    <el-card style="min-height: 100%">
      <div class="app-container">
        <el-row>
          <el-col :span="12" class="card-box">
            <el-card>
              <div slot="header"><span><i class="el-icon-cpu"></i> {{
                  "CPU ( " + server.cpuInfo.cpuModel + " )"
                }}</span>
              </div>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table cellspacing="0" style="width: 100%;">
                  <thead>
                  <tr>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">属性</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">值</div>
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">核心数</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.cpuInfo" class="cell">{{ server.cpuInfo.cpuNum }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">用户使用率</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.cpuInfo" class="cell">{{ server.cpuInfo.user + " %" }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">系统使用率</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.cpuInfo" class="cell">{{ server.cpuInfo.sys + " %" }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">当前空闲率</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.cpuInfo" class="cell">{{ server.cpuInfo.free + " %" }}</div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </el-card>
          </el-col>

          <el-col :span="12" class="card-box">
            <el-card>
              <div slot="header"><span><i class="el-icon-tickets"></i> 内存</span></div>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table cellspacing="0" style="width: 100%;">
                  <thead>
                  <tr>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">属性</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">内存</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">JVM</div>
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">总内存</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.memoryInfo" class="cell">{{ server.memoryInfo.memoryTotal }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.memoryInfo" class="cell">{{ server.memoryInfo.jvmMemoryTotal }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">已用内存</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.memoryInfo" class="cell">{{ server.memoryInfo.usedMemory }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.memoryInfo" class="cell">{{ server.memoryInfo.jvmUsedMemory }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">剩余内存</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.memoryInfo" class="cell">{{ server.memoryInfo.remainMemory }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.memoryInfo" class="cell">{{ server.memoryInfo.jvmRemainMemory }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">使用率</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.memoryInfo" :class="{'text-danger': server.memoryInfo.memoryPercent > 80}"
                           class="cell">{{ server.memoryInfo.memoryPercent + " %" }}
                      </div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.memoryInfo" :class="{'text-danger': server.memoryInfo.jvmMemoryPercent > 80}"
                           class="cell">{{ server.memoryInfo.jvmMemoryPercent + " %" }}
                      </div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </el-card>
          </el-col>

          <el-col :span="24" class="card-box">
            <el-card>
              <div slot="header">
                <span><i class="el-icon-monitor"></i> 服务器信息</span>
              </div>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table cellspacing="0" style="width: 100%;">
                  <tbody>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">服务器名称</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.serverInfo" class="cell">{{ server.serverInfo.serverName }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">用户名称</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.serverInfo" class="cell">{{ server.serverInfo.serverUser }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">操作系统</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.serverInfo" class="cell">{{ server.serverInfo.serverSystem }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">系统版本</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.serverInfo" class="cell">{{ server.serverInfo.serverVersion }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">服务器IP</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.serverInfo" class="cell">{{ server.serverInfo.serverIp }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">系统架构</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.serverInfo" class="cell">{{ server.serverInfo.serverArchitecture }}</div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </el-card>
          </el-col>

          <el-col :span="24" class="card-box">
            <el-card>
              <div slot="header">
                <span><i class="el-icon-coffee-cup"></i> Java虚拟机信息</span>
              </div>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table cellspacing="0" style="width: 100%;table-layout:fixed;">
                  <tbody>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">Java名称</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.jvmName }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">Java版本</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.jvmVersion }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">启动时间</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.jvmStartTime }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">运行时长</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.jvmRunTime }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">项目路径</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.projectPath }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">处理级别</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.processingLevel + " Level" }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">安装路径</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.jvmPath }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">进程标识</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.jvmPid + " PID" }}</div>
                    </td>
                  </tr>
                  <tr>
                    <td class="el-table__cell is-leaf" colspan="1">
                      <div class="cell">运行参数</div>
                    </td>
                    <td class="el-table__cell is-leaf" colspan="3">
                      <div v-if="server.jvmInfo" class="cell">{{ server.jvmInfo.runParams }}</div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </el-card>
          </el-col>

          <el-col :span="24" class="card-box">
            <el-card>
              <div slot="header">
                <span><i class="el-icon-receiving"></i> 磁盘状态</span>
              </div>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table cellspacing="0" style="width: 100%;">
                  <thead>
                  <tr>
                    <th class="el-table__cell el-table__cell is-leaf">
                      <div class="cell">磁盘名称</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">磁盘标签</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">挂载区域</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">文件系统</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">可用空间</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">已用空间</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">总空间</div>
                    </th>
                    <th class="el-table__cell is-leaf">
                      <div class="cell">已用百分比</div>
                    </th>
                  </tr>
                  </thead>
                  <tbody v-if="server.fileInfoList">
                  <tr v-for="(sysFile, index) in server.fileInfoList" :key="index">
                    <td class="el-table__cell is-leaf">
                      <div class="cell">{{ sysFile.name }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">{{ sysFile.label }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">{{ sysFile.mount }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">{{ sysFile.fsType }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">{{ sysFile.freeSpace }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">{{ sysFile.unFreeSpace }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">{{ sysFile.totalSpace }}</div>
                    </td>
                    <td class="el-table__cell is-leaf">
                      <div class="cell">{{ sysFile.unFreePercent + " %" }}</div>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </div>
    </el-card>

  </div>
</template>

<script>
import {systemInfo} from "@/api/admin.js";
import {Loading} from "element-ui";

export default {
  name: "Server",
  data() {
    return {
      // 服务器信息
      server: {},
      tableHeight: 0,
      radio: window.sessionStorage.getItem("ratio"),
      clientWidth: document.body.clientWidth, // 文档宽度
      loadingInstance: "",
    };
  },
  created() {
    this.openLoading();
    this.getList();
  },
  methods: {
    /** 查询服务器信息 */
    getList() {
      systemInfo()
          .then(response => {
            this.server = response.data;
            this.closeLoading();
          });
    },

    // 打开加载层
    openLoading() {
      this.loadingInstance = Loading.service({
        lock: true,
        text: "正在加载服务监控数据，请稍候！",
        spinner: "el-icon-loading",
        background: "rgba(0, 0, 0, 0.7)",
      })
    },
    // 关闭遮罩层
    closeLoading() {
      this.loadingInstance.close();
    }

  }
};
</script>


<style scoped>

.app-container {
  padding: 20px;
}

.card-box {
  padding-right: 15px;
  padding-left: 15px;
  margin-bottom: 10px;
}

.cell .el-tag {
  margin-right: 0;
}

.text-danger {
  color: #ed5565;
}

</style>
