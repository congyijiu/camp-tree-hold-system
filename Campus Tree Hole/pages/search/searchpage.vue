<template>
  <view class="content">
    <view class="top">
      <u-icon name="arrow-left" @click="ToSearch"></u-icon>
    </view>
    <scroll-view scroll-y style="height: 100vh;width: 100%;" @scrolltolower="onreachBottom">
      <view class="swiper-item-all">
        <view class="page1">
          <box :box="item" v-for="(item,index) in resultData" :key="index" class="item" v-if="index%2==0">
          </box>
        </view>
        <view class="page2">
          <box :box="item" v-for="(item,index) in resultData" :key="index" class="item" v-if="index%2==1">
          </box>
        </view>
      </view>

    </scroll-view>
  </view>
</template>

<script>
import box from '../../components/box.vue'
import {
  search
} from '@/apis/detail.js'
export default {
  components: {
    box
  },
  data() {
    return {
      title: "",
      searchData: {
        pageIndex: 1,
        pageSize: 10,
        content: "",
        type: 1
      },
      resultData: []
    }
  },
  async onLoad(option) {
    this.searchData.content = option.title
    if (this.searchData.content != "") {
      search(this.searchData).then(res => {
        this.resultData = res.data.records
      })
    }
  },
  methods: {
    onreachBottom() {
      search(this.searchData).then(res => {
        this.resultData.concat(res.data.records)
      })
    },
    ToSearch() {
      uni.reLaunch({
        url: '/pages/search/search'
      })
    }
  }
}
</script>

<style>
.content {
  background-color: #f3f4f6;
}

.page1 {
  width: 360rpx;
}

.page2 {

  width: 360rpx;
}

.swiper-item-all {
  display: flex;
  flex-direction: row;
  justify-content: space-evenly;
}

.top {
  display: flex;
  flex-direction: row;
  height: 10vh;
  margin-left: 20rpx;
  margin-top: 30rpx;
}
</style>