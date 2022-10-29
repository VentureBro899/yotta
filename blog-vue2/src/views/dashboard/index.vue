<template>
  <div class="dashboard-container">
    <div class="square-list">
      <div class="square-item">
        <div class="item-name">文章总数</div>
        <div class="item-sum">{{ articleCount }}</div>
      </div>
      <div class="square-item">
        <div class="item-name">总点赞数</div>
        <div class="item-sum">{{ thumbCount }}</div>
      </div>
      <div class="square-item">
        <div class="item-name">总浏览量</div>
        <div class="item-sum">{{ viewCount }}</div>
      </div>
      <div class="square-item">
        <div class="item-name">总评论数</div>
        <div class="item-sum">0</div>
      </div>
    </div>
    <div class="echart-list">
      111
      <div id="tendency" />
    </div>
  </div>
</template>

<script>
import { mapGetters } from 'vuex'
import * as echarts from 'echarts/core'
import { GridComponent } from 'echarts/components'
import { LineChart } from 'echarts/charts'
import { UniversalTransition } from 'echarts/features'
import { CanvasRenderer } from 'echarts/renderers'

echarts.use([GridComponent, LineChart, CanvasRenderer, UniversalTransition])

export default {
  name: 'Dashboard',
  data() {
    return {
      articleCount: 0,
      viewCount: 0,
      thumbCount: 0,
      trendData: {},
      option: {
        xAxis: {
          type: 'category',
          data: ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun']
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: [150, 230, 224, 218, 135, 147, 260],
            type: 'line'
          }
        ]
      }
    }
  },
  mounted() {
    this.$request.get('/statistic/articleCount').then(res => {
      this.articleCount = res.data
    })
    this.$request.get('/statistic/viewCount').then(res => {
      this.viewCount = res.data
    })
    this.$request.get('/statistic/thumbCount').then(res => {
      this.thumbCount = res.data
    })
    this.$request.get('/statistic/articleTrend').then(res => {
      const chartDom = document.getElementById('tendency')
      const myChart = echarts.init(chartDom)
      myChart.setOption({
        xAxis: {
          type: 'category',
          data: Object.keys(res.data)
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            data: Object.values(res.data),
            type: 'line'
          }
        ]
      })
    })
  },
  computed: {
    ...mapGetters(['name'])
  }
}
</script>

<style lang="scss" scoped>
.dashboard-container {
  margin: 10px;
  color: #fff;
}
.square-list {
  display: flex;
  flex-wrap: nowrap;
  justify-content: space-around;
  .square-item {
    box-sizing: border-box;
    padding: 20px;
    width: 23%;
    .item-name {
      float: left;
      font-size: 18px;
    }
    .item-sum {
      float: right;
      margin-top: 20px;
      font-size: 40px;
    }
  }
  .square-item:nth-child(1) {
    background-color: #4d06e6;
  }
  .square-item:nth-child(2) {
    background-color: rgb(244, 8, 141);
  }
  .square-item:nth-child(3) {
    background-color: rgb(255, 204, 0);
  }
  .square-item:nth-child(4) {
    background-color: rgb(11, 152, 240);
  }
}
#tendency {
  min-height: 500px;
  width: 100%;
}
</style>
