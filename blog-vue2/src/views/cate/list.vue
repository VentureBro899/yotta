<template>
  <div class="app-container">
    <div class="toolbar">
      <el-button type="primary" @click="onAdd">添加分类</el-button>
      <div class="search">
        <el-input v-model="key" placeholder="请输入关键字" />
        <el-button @click="search">搜索</el-button>
      </div>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column align="center" label="排序" width="95">
        <template slot-scope="scope">
          {{ scope.$index }}
        </template>
      </el-table-column>
      <el-table-column align="center" label="编号" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="名称" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="描述" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.detail }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-dialog title="修改分类" :visible.sync="dialogFormVisible" @close="closeDialog">
      <el-form ref="form" class="dialogForm" :model="form" label-width="120px">
        <el-form-item label="名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.detail" type="textarea" />
        </el-form-item>
        <el-form-item>
          <el-button v-if="dialogForadd" type="primary" @click="handleAdd">添加</el-button>
          <el-button v-if="!dialogForadd" type="success" @click="onUpdate">确认</el-button>
          <el-button @click="onCancel">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
    <div class="footer">
      <el-button class="delSelection" @click="delSelection()">删除选中</el-button>
      <el-pagination background layout="prev, pager, next" :total="total" :page-size="size" @current-change="currentChange" />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      current: 1,
      size: 10,
      list: null,
      listLoading: true,
      dialogFormVisible: false,
      dialogForadd: true,
      form: {
        name: '',
        detail: ''
      },
      multipleSelection: [],
      total: 0,
      key: ''
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      this.$request.get(`/cate/list/${this.size}/${this.current}/${this.key}`).then(res => {
        this.list = res.data.list
        this.total = res.data.total
        this.listLoading = false
      })
    },
    search() {
      this.current = 1
      this.fetchData()
    },
    handleEdit(id) {
      this.dialogForadd = false
      this.dialogFormVisible = true
      this.$nextTick(() => {
        // target传字符串相当于document.querySelector()获取元素
        const loadingService = this.$loading({ target: '.dialogForm', fullscreen: false })
        this.$request
          .get(`/cate/` + id)
          .then(res => {
            this.form = res.data
            loadingService.close()
          })
          .catch(() => {
            loadingService.close()
            this.dialogFormVisible = false
          })
      })
    },
    closeDialog(done) {
      this.form = {
        id: 0,
        name: '',
        detail: '',
        style: 1
      }
    },
    onAdd() {
      this.dialogForadd = true
      this.dialogFormVisible = true
    },
    handleAdd() {
      this.$request.post(`/cate/`, this.form).then(res => {
        this.$message.success('添加成功')
        this.dialogFormVisible = false
        this.fetchData()
      })
    },
    onUpdate() {
      this.$request.put('/cate', this.form).then(res => {
        this.$message({
          message: '修改成功!',
          type: 'success'
        })
        this.form = {
          id: 0,
          name: '',
          detail: ''
        }
        this.fetchData()
        this.dialogFormVisible = false
      })
    },
    onCancel() {
      this.dialogFormVisible = false
    },
    // 处理多选
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    // 删除选中
    delSelection() {
      const idsOfdel = []
      this.$confirm('此操作将删除所选项, 不可恢复，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.multipleSelection.forEach(item => {
            idsOfdel.push(item.id)
          })

          this.$request.delete('/cate', { data: idsOfdel }).then(res => {
            this.$message.success('已删除选中项')
            this.currrentCut(idsOfdel.length)
            this.fetchData()
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },
    // 删除某项
    handleDelete(id) {
      this.$confirm('此操作将删除所选项, 不可恢复，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$request.delete('/cate', { data: [id] }).then(res => {
            this.$message.success('已删除所选项')
            this.currrentCut(1)
            this.fetchData()
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消删除'
          })
        })
    },

    // 判断删除后页码是否需要自减
    currrentCut(num) {
      // 总条数减去删除的项数得到删除后的总页数，总页数向上取整小于当前页码就将当前页码置为总页数(最后一页)
      const pageTotal = Math.ceil((this.total - num) / this.size)
      this.current = pageTotal < this.current ? pageTotal : this.current
    },
    currentChange(current) {
      this.current = current
      this.fetchData()
    }
  }
}
</script>
<style lang="scss">
.toolbar {
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
  .search {
    display: flex;
    justify-content: flex-end;
  }
}
.footer {
  margin: 20px;
  display: flex;
  justify-content: space-between;
  .delSelection {
    position: relative;
  }
}
</style>
