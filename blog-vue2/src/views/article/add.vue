<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="文章标题">
        <el-input v-model="form.title" />
      </el-form-item>
      <el-form-item label="所属分类">
        <el-select v-model="form.cid" filterable remote clearable reserve-keyword placeholder="请输入关键词" :remote-method="remoteMethod" :loading="loading" @change="redefaultOptions">
          <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value" />
        </el-select>
      </el-form-item>
      <el-form-item label="样式风格">
        <el-select v-model="form.style" placeholder="选择样式">
          <el-option label="1" value="1" />
          <el-option label="2" value="2" />
        </el-select>
      </el-form-item>
      <el-form-item label="封面缩略图">
        <el-upload class="upload-demo" drag name="file" :limit="1" :action="uploadurl" :file-list="fileList" list-type="picture" :data="postData" :before-upload="beforeUploadFile"
          :on-success="successUploadFile" :on-exceed="overflowLimit" :on-remove="onRemove">
          <i class="el-icon-upload" />
          <div class="el-upload__text">
            将文件拖到此处，或<em>点击上传</em>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="发布时间">
        <el-date-picker v-model="form.postdate" type="datetime" placeholder="选择日期时间" align="right" :picker-options="pickerOptions" value-format="yyyy-MM-dd HH:mm:ss" />
      </el-form-item>
      <el-form-item label="草稿">
        <el-switch v-model="form.hidden" />
      </el-form-item>
      <el-form-item label="推荐">
        <el-switch v-model="form.recommend" />
      </el-form-item>
      <el-form-item label="正文">
        <mavon-editor ref="md" v-model="form.maincontent" placeholder="捣鼓完技术、写下你的感受吧..." @imgAdd="imgAdd" />
      </el-form-item>
      <el-form-item label="简介">
        <el-input v-model="form.description" type="textarea" />
      </el-form-item>
      <el-form-item label="阅读量">
        <el-input v-model.number="form.viewsum" />
      </el-form-item>
      <el-form-item label="点赞数">
        <el-input v-model.number="form.thumbsum" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">
          发布
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
        cid: '',
        style: 1,
        coverpicture: '',
        description: '',
        maincontent: '',
        hidden: false,
        recommend: false,
        postdate: '',
        viewsum: 0,
        thumbsum: 0
      },
      loading: true,
      options: [], // 匹配关键字的部分分类
      allOptions: [], // 所有分类
      pickerOptions: {
        shortcuts: [
          {
            text: '今天',
            onClick(picker) {
              picker.$emit('pick', new Date())
            }
          },
          {
            text: '明天',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24)
              picker.$emit('pick', date)
            }
          },
          {
            text: '一周后',
            onClick(picker) {
              const date = new Date()
              date.setTime(date.getTime() + 3600 * 1000 * 24 * 7)
              picker.$emit('pick', date)
            }
          }
        ]
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
      uploadurl: '',
      fileList: []
    }
  },
  computed: {},
  mounted() {
    // 获取分类名称列表
    this.$request.get('/cate/list/10000/1').then(res => {
      res.data.list.forEach(item => {
        this.options.push({ label: item.name, value: item.id })
      })
      this.loading = false
      this.allOptions = this.options
    })

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
      const isGIF = file.type === 'image/gif'
      const isLt2M = file.size / 1024 / 1024 < 2

      if (!isJPG && !isPNG && !isGIF) {
        this.$message.error('上传头像图片只能是 JPG/PNG/GIF 格式!')
        return false
      }
      if (!isLt2M) {
        this.$message.error('上传头像图片大小不能超过 2MB!')
        return false
      }
      return (isJPG || isPNG || isGIF) && isLt2M
    },
    successUploadFile(response, file, fileList) {
      this.form.coverpicture = response.key
      this.$message.success('上传成功')
    },
    overflowLimit() {
      this.$message.warning('只允许有一张缩略图')
    },
    onRemove() {
      this.coverpicture = ''
      this.$message('已移除缩略图')
    },
    // md编辑器图片上传处理
    imgAdd(pos, file) {
      const that = this
      const key = dateformat(new Date(), 'yyyy/MM/dd/') + uuid() + file.name.substring(file.name.indexOf('.', file.name.length - 5))
      const observable = qiniu.upload(file, key, this.postData.token, null, this.qiniuConfig)
      observable.subscribe({
        error(err) {
          console.log(err)
          that.$message.danger('上传失败')
        },
        complete(res) {
          that.$refs.md.$img2Url(pos, that.$store.getters.imgdomain + res.key)
        }
      })
    },
    onSubmit() {
      this.$request.post('/article', this.form).then(res => {
        this.$message({
          message: res.msg,
          type: 'success'
        })
        this.form = {
          title: '',
          cid: '',
          style: 1,
          coverpicture: '',
          description: '',
          maincontent: '',
          hidden: false,
          recommend: false,
          postdate: '',
          view: 0,
          thumb: 0
        }
        this.fileList = []
        this.$router.push('/article/list')
      })
    },
    onCancel() {
      this.$confirm('此操作将重置所有选项, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
        .then(() => {
          this.form = {
            title: '',
            cid: '',
            syle: 1,
            coverpicture: '',
            description: '',
            maincontent: '',
            recommend: false,
            hidden: false,
            postdate: '',
            view: 0,
            thumb: 0
          }
          this.fileList = []
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

<style scoped>
.line {
  text-align: center;
}
</style>

