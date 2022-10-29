
<template>
  <div class="app-container">
    <div class="toolbar">
      <div class="search">
        <el-input v-model="key" placeholder="请输入文章标题" />
        <el-button @click="search">搜索文章</el-button>
      </div>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column align="center" label="ID" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="标题">
        <template slot-scope="scope">
          {{ scope.row.title }}
        </template>
      </el-table-column>
      <el-table-column label="分类" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.cname }}</span>
        </template>
      </el-table-column>
      <el-table-column label="浏览量" width="110" align="center">
        <template slot-scope="scope">
          <span>{{ scope.row.viewsum }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" label="发布时间" width="200">
        <template slot-scope="scope">
          <i class="el-icon-time" />
          <span>{{ scope.row.postdate }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
    <div class="footer">
      <el-button class="delSelection" @click="delSelection()">删除选中</el-button>
      <el-pagination background layout="prev, pager, next" :total="total" @current-change="currentChange" />
    </div>
  </div>
</template>

<script>
export default {
  data() {
    return {
      current: 1,
      size: 10,
      key: '',
      list: null,
      listLoading: true,
      multipleSelection: [],

      // 分页
      total: 0
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    // 获取列表数据
    fetchData() {
      this.listLoading = true
      this.$request.get(`/article/list/${this.size}/${this.current}/${this.key}`).then(res => {
        this.list = res.data.list
        this.total = res.data.total
        this.listLoading = false
      })
    },
    search() {
      this.current = 1
      this.fetchData()
    },
    // 删除某项
    handleDelete(id) {
      this.$confirm('此操作将删除所选项, 不可恢复，是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.$request.delete('/article', { data: [id] }).then(res => {
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

    handleEdit(id) {
      this.$router.push('/article/update/' + id)
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

          this.$request.delete('/article', { data: idsOfdel }).then(res => {
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
    // 页数改变
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
  justify-content: flex-end;
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
