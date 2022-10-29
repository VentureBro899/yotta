<template>
  <div class="app-container">
    <div class="toolbar">
      <div class="search">
        <el-input v-model="key" placeholder="请输入关键字" />
        <el-button @click="search">搜索</el-button>
      </div>
    </div>
    <el-table v-loading="listLoading" :data="list" element-loading-text="Loading" border fit highlight-current-row @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column align="center" label="id" width="95">
        <template slot-scope="scope">
          {{ scope.row.id }}
        </template>
      </el-table-column>
      <el-table-column label="图片" align="center">
        <template slot-scope="scope">
          <el-image :key="scope.row.id" :src="imgdomain + scope.row.picture" lazy fit="contain" />
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-tag :type="scope.row.showing ? 'success' : 'warning'">{{ scope.row.showing ? '展示': '隐藏' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" width="200">
        <template slot-scope="scope">
          <el-button size="mini" @click="handleEdit(scope.row.id)">编辑</el-button>
          <el-button size="mini" type="danger" @click="handleDelete(scope.row.id)">删除</el-button>
        </template>
      </el-table-column>
    </el-table>
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
      multipleSelection: [],
      total: 0,
      key: ''
    }
  },
  computed: {
    imgdomain() {
      return this.$store.getters.imgdomain
    }
  },
  created() {
    this.fetchData()
  },
  methods: {
    fetchData() {
      this.listLoading = true
      this.$request.get(`/slider/list/${this.size}/${this.current}/${this.key}`).then(res => {
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
      this.$router.push('update/' + id)
    },

    onUpdate() {},
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

          this.$request.delete('/slider', { data: idsOfdel }).then(res => {
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
          this.$request.delete('/slider', { data: [id] }).then(res => {
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
  justify-content: flex-end;
  .search {
    display: flex;
    justify-content: flex-end;
  }
}
.el-image img {
  max-height: 450px;
  max-width: 800px;
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
