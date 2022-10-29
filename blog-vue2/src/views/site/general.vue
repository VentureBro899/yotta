<!-- eslint-disable vue/max-attributes-per-line -->
<template>
  <div class="app-container">
    <el-form ref="form" :model="form" label-width="120px">
      <el-form-item label="网站标题：">
        <el-input v-model="form.site_title" />
      </el-form-item>
      <el-form-item label="网站副标题：">
        <el-input v-model="form.site_subtitle" />
      </el-form-item>
      <el-form-item label="网站关键字：">
        <el-input v-model="form.site_keywords" />
      </el-form-item>
      <el-form-item label="网站描述：">
        <el-input v-model="form.site_description" />
      </el-form-item>

      <el-form-item label="博客logo：">
        <el-upload class="upload-demo" drag name="file" :action="uploadurl" :show-file-list="false" :data="postData" :before-upload="beforeUploadFile"
          :on-success="successUploadLogo">
          <img v-if="form.site_logo" :src="imgdomain + form.site_logo" fit="fill" style="height: 100%;width: 100%;">
          <div v-else>
            <i class="el-icon-upload" />
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="博主名称：">
        <el-input v-model="form.site_owner" />
      </el-form-item>
      <el-form-item label="博主头像：">
        <el-upload class="upload-avatar" drag name="file" :action="uploadurl" :show-file-list="false" :data="postData" :before-upload="beforeUploadFile"
          :on-success="successUploadAvatar">
          <img v-if="form.site_owner_avatar" :src="imgdomain + form.site_owner_avatar" fit="fill" style="height: 100%;width: 100%;">
          <div v-else>
            <i class="el-icon-upload" />
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
          </div>
        </el-upload>
      </el-form-item>
      <el-form-item label="座右铭：">
        <el-input v-model="form.site_owner_motto" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">
          确认
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
        site_title: '',
        site_subtitle: '',
        site_keywords: '',
        site_description: '',
        site_logo: '',
        site_owner: '',
        site_owner_avatar: '',
        site_owner_motto: ''
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
    // 获取数据
    this.$request.get(`/siteconfig`).then(res => {
      this.form = res.data
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
    successUploadLogo(response, file, fileList) {
      this.form.site_logo = response.key
      this.$message.success('上传成功')
    },
    successUploadAvatar(response, file, fileList) {
      this.form.site_owner_avatar = response.key
      this.$message.success('上传成功')
    },
    onSubmit() {
      this.$request.put('/siteconfig', this.form).then(res => {
        this.$message({
          message: res.msg,
          type: 'success'
        })
      })
    }
  }
}
</script>

<style scoped>
.line {
  text-align: center;
}
</style>

