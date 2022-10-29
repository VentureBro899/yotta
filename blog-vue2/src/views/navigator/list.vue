<template>
  <div class="app-container">
    <el-tree :props="defaultProps" :data="data" node-key="id" default-expand-all :expand-on-click-node="false">
      <span slot-scope="{ node, data }" class="custom-tree-node">
        <span>{{ data.title }}</span>
        <span>
          <el-button type="text" size="mini" @click="() => append(node, data)">
            添加子导航
          </el-button>
          <el-button type="text" size="mini" @click="() => edit(node, data)">
            修改
          </el-button>
          <el-button type="text" size="mini" @click="() => remove(node, data)">
            删除
          </el-button>
        </span>
      </span>
    </el-tree>
    <el-link class="addHead" type="primary" :underline="false" @click="addHead">新增顶级导航</el-link>
    <el-dialog title="更新导航" :visible.sync="dialogFormVisible" @close="closeDialog">
      <el-form ref="form" class="dialogForm" :model="form" label-width="120px">
        <el-form-item label="标题">
          <el-input v-model="form.title" />
        </el-form-item>
        <el-form-item label="导向分类">
          <el-select v-model="form.cid" filterable remote clearable reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod" :loading="loading"
            @change="redefaultOptions">
            <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
          </el-select>
        </el-form-item>
        <el-form-item label="路由">
          <el-input v-model="form.route" />
          如果路由不为空，优先使用路由作为导航目标
        </el-form-item>
        <el-form-item label="图标">
          <el-collapse>
            <el-collapse-item :title="form.icon ? form.icon : '请选择图标'">
              <div class="iconfont-list">
                <div v-for="(item,index) in iconlist" :key="index" class="iconfont-item" @click="selectIcon(item)">
                  <i :class="item" :style="item === form.icon ? 'color:#409EFF' : ''" />
                </div>
              </div>
            </el-collapse-item>
          </el-collapse>
        </el-form-item>
        <el-form-item>
          <el-button v-if="dialogForadd" type="primary" @click="onAdd">添加</el-button>
          <el-button v-if="!dialogForadd" type="success" @click="onUpdate">确认</el-button>
          <el-button @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import iconnamelist from '@/assets/fonts/iconarray.js'
export default {
  data() {
    return {
      defaultProps: {
        children: 'children', // 指定孩子节点的标签
        label: 'title' // 指定孩子节点的数据变量名
      },
      data: [],
      dialogFormVisible: false,
      dialogForadd: true,
      form: {
        title: '',
        pid: 0,
        route: '',
        cid: '',
        icon: ''
      },
      // 分类参数
      loading: true,
      options: [], // 匹配关键字的部分分类
      allOptions: [], // 所有分类
      currentNodeData: null,
      iconlist: iconnamelist,
      // 顶部导航标记
      head: false
    }
  },
  mounted() {
    this.$request.get('/navigator/list').then(res => {
      this.data = res.data
    })
    // 获取分类名称列表
    this.$request.get('/cate/list/10000/1').then(res => {
      res.data.list.forEach(item => {
        this.options.push({ label: item.name, value: item.id })
      })
      this.loading = false
      this.allOptions = this.options
    })
  },
  methods: {
    // 递归更新节点
    updateNode(data, id, title) {
      if (data.length === 0) {
        return
      } else {
        data.forEach(item => {
          if (item.id === id) {
            item.title = title
          }
          this.updateNode(item.children, id, title)
        })
        return
      }
    },

    append(node, data) {
      this.dialogForadd = true
      this.dialogFormVisible = true
      this.currentNodeData = data
    },
    // 添加顶级导航
    addHead() {
      this.append()
      this.head = true
    },
    onAdd() {
      const data = this.currentNodeData
      this.form.pid = data ? data.id : 0
      this.$request.post(`/navigator/`, this.form).then(res => {
        this.$message.success('添加成功')
        if (this.head) {
          this.data.push({
            id: res.data,
            title: this.form.title,
            pid: 0,
            children: []
          })
          this.head = false
          this.closeDialog()
          return
        }
        const newChild = {
          id: res.data,
          title: this.form.title,
          pid: data.id,
          children: []
        }
        if (!data.children) {
          this.$set(data, 'children', [])
        }
        data.children.push(newChild)
        this.closeDialog()
      })
    },
    // node是节点，data是节点数据
    remove(node, data) {
      this.$confirm('此操作将删除所选项, 不可恢复，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$request.delete('/navigator', { data: [data.id] }).then(res => {
            this.$message.success('已删除所选项')
            const parent = node.parent
            console.log(parent.data.children, parent.data)
            const children = parent.data.children || parent.data
            const index = children.findIndex(d => d.id === data.id)
            children.splice(index, 1)
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },

    edit(node, data) {
      this.dialogForadd = false
      this.dialogFormVisible = true
      this.currentNodeData = data
      this.$nextTick(() => {
        // target传字符串相当于document.querySelector()获取元素
        const loadingService = this.$loading({
          target: '.dialogForm',
          fullscreen: false
        })
        this.$request
          .get(`/navigator/` + data.id)
          .then(res => {
            this.form = res.data
            loadingService.close()
          })
          .catch(() => {
            loadingService.close()
            this.closeDialog()
          })
      })
    },
    selectIcon(item) {
      this.form.icon = item
    },

    onUpdate() {
      this.$request.put('/navigator', this.form).then(res => {
        this.updateNode(this.data, this.form.id, this.form.title)
        this.$message({
          message: '修改成功!',
          type: 'success'
        })
        this.closeDialog()
      })
    },
    onCancel() {
      this.closeDialog()
    },

    closeDialog(done) {
      this.form = {
        title: '',
        pid: '',
        route: '',
        cid: '',
        icon: ''
      }
      this.currentNodeData = null
      this.dialogFormVisible = false
    },

    // 分类查询
    remoteMethod(query) {
      if (query !== '') {
        this.loading = true
        setTimeout(() => {
          this.loading = false
          this.options = this.options.filter(item => {
            return item.label.toLowerCase().indexOf(query.toLowerCase()) > -1
          })
        }, 200)
      } else {
        this.options = this.allOptions
      }
    },
    redefaultOptions() {
      this.options = this.allOptions
    }
  }
}
</script>

<style scoped lang="scss">
.line {
  text-align: center;
}
.addHead {
  margin: 10px;
  text-decoration: none;
}
.iconfont-list {
  display: flex;
  flex-wrap: wrap;
  height: 200px;
  overflow-y: scroll;
  .iconfont-item {
    box-sizing: border-box;
    width: 10%;
    padding: 10px;
    text-align: center;
  }
}
</style>
