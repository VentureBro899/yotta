<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="标题">
        <el-input v-model="form.title" />
      </el-form-item>

      <el-form-item label="幻灯片">
        <el-upload class="upload-demo" drag name="file" :action="uploadurl" :show-file-list="false" :data="postData" :before-upload="beforeUploadFile"
          :on-success="successUploadFile" :on-remove="onRemove">
          <img v-if="form.picture" :src="imgdomain + form.picture" fit="contain" style="height: 100%;width: 100%;">
          <div v-else>
            <i class="el-icon-upload" />
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="背景色">
        <el-color-picker v-model="form.bgcolor" show-alpha />
      </el-form-item>
      <el-form-item label="优先级数">
        <el-input v-model="form.reorder" />
      </el-form-item>
      <el-form-item label="路由">
        <el-input v-model="form.route" />
      </el-form-item>
      <el-form-item label="展示">
        <el-switch v-model="form.showing" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">
          添加
        </el-button>
        <el-button @click="onCancel">
          清空所有
        </el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import * as qiniu from 'qiniu-js'
import dateformat from '@/utils/dateformat'
import uuid from '@/utils/uuid'

export default {
  data() {
    return {
      form: {
        title: '',
        picture: '',
        bgcolor: '#39f',
        reorder: 1,
        route: '',
        showing: true
      },
      postData: {
        token: '',
        key: ''
      },
      // 七牛云文件上传配置
      qiniuConfig: {
        useCdnDomain: true,
        region: qiniu.region.z0
      },
      uploadurl: ''
    }
  },
  computed: {
    imgdomain() {
      return this.$store.getters.imgdomain
    }
  },
  mounted() {
    // 获取上传token
    this.$request('/auth/uploadtoken').then(res => {
      this.postData.token = res.data.uptoken
      switch (res.data.area) {
        case '0':
          this.qiniuConfig.region = qiniu.region.z0
          break
        case '1':
          this.qiniuConfig.region = qiniu.region.z1
          break
        case '2':
          this.qiniuConfig.region = qiniu.region.z2
          break
        case '3':
          this.qiniuConfig.region = qiniu.region.na0
          break
        case '4':
          this.qiniuConfig.region = qiniu.region.as0
          break
        default:
          console.log(res.data.area + '参数错误')
      }
      // 获取上传地址
      qiniu.getUploadUrl(this.qiniuConfig, this.postData.token).then(res => {
        this.uploadurl = res
      })
    })
  },
  methods: {
    beforeUploadFile(file) {
      this.postData.key = dateformat(new Date(), 'yyyy/MM/dd/') + uuid() + file.name.substring(file.name.indexOf('.', file.name.length - 5))
      const isJPG = file.type === 'image/jpeg'
      const isPNG = file.type === 'image/png'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isPNG) {
        this.$message.error('上传图片只能是 JPG/PNG 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传图片大小不能超过 2MB!')
        return false
      }
      return (isJPG || isPNG) && isLt2M
    },
    successUploadFile(response, file, fileList) {
      this.form.picture = response.key
      this.$message.success('上传成功')
    },
    onRemove() {
      this.picture = ''
      this.$message('已移除幻灯片')
    },
    onSubmit() {
      this.$request.post('/slider', this.form).then(res => {
        this.$message({
          message: res.msg,
          type: 'success'
        })
        this.onClear()
        this.$router.push('/slider/list')
      })
    },
    onCancel() {
      this.$confirm('此操作将重置所有选项, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.onClear()
          this.$message({
            type: 'success',
            message: '重置成功!'
          })
        })
        .catch(() => {
          this.$message({
            type: 'info',
            message: '已取消重置'
          })
        })
    },
    onClear() {
      this.form = {
        title: '',
        picture: '',
        bgcolor: '#39f',
        reorder: 1,
        route: '',
        showing: true
      }
    }
  }
}
</script>

<style scoped>
.line {
  text-align: center;
}
</style>

