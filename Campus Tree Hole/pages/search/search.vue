<template>
  <view>
    <u-search placeholder="请输入关键字" v-model="searchData.content" shape="round" @search="Search"
              margin="100rpx 0 0 0 " @custom="Search" @change="change" :action-text="actionText"></u-search>
    <u-cell-group v-for="(item,index) in resultData" :key="index">
      <u-cell-item :title="item.title" :arrow="false" :border-top="false" icon="search" @click="ToSearchPage(item)"></u-cell-item>
    </u-cell-group>
  </view>

</template>

<script>
import {
  search
} from '@/apis/detail.js'
export default {
  data() {
    return {
      searchData: {
        pageIndex: 1,
        pageSize: 8,
        content: "",
        type: 1
      },
      resultData: [],
      actionText:"取消"
    }
  },
  methods: {
    Search() {
      if(this.searchData.content!="")
      {
        search(this.searchData).then(res => {
          this.resultData = res.data.records
        })
        uni.navigateTo({
          url:`/pages/search/searchpage?title=${this.searchData.content}`
        })
      }
      else{
        uni.reLaunch({
          url:'/pages/index/index'
        })
      }


    },
    change() {
      this.actionText = "搜索"
      if(this.searchData.content=="")
      {
        this.actionText = "取消"
      }
      setTimeout(() => {
        search(this.searchData).then(res => {
          this.resultData = res.data.records
        })
      }, 700)
    },
    ToSearchPage(item){
      uni.reLaunch({
        url:`/pages/search/searchpage?title=${item.title}`
      })
    }

  }
}
</script>
<style>

</style>