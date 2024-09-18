<template>
  <view class="tab">
    <scroll-view class="scroll-view-content" scroll-x scroll-with-animation :show-scrollbar="false" :scroll-left="scrollLeft">
      <view class="scroll-item-view" :class="scroll ? '' : 'flex-row-left'" id="tab">
        <view
          class="scroll-item"
          :class="[ index == list.length - 1 ? 'no-right' : '' ]"
          :style="{ color: currentIndex == index ? activeColor: '', height: tabHeight, 'line-height': tabHeight, 'font-size': fontSize, width: !scroll ? (100/list.length) + '%' : '', 'margin-right': scroll ? marright : '','font-weight': currentIndex == index ? activeWeight: '', }"
          :id="'tab-item-' + index" v-for="(item, index) in list"
          :key="index"
          @click="clickTab(item, index)">
          {{ item.description }}
        </view>
        <view class="scroll-tab-bar" :style="[tabBarStyle]"></view>
      </view> 
    </scroll-view>
  </view>
</template>

<script> 
export default {
  props: {
    list: {
      type: Array,
      default: () => {
        return []
      }
    },
    // 当前活动tab的索引
    current: {
      type: [Number, String],
      default: 0
    },
    // 菜单底部移动的bar的宽度，单位px
    barWidth: {
      type: [String, Number],
      default: 0
    },
    // 移动bar的高度
    barHeight: {
      type: [String, Number],
      default: 0
    },
    barStyle: {
      type: Object,
      default() {
        return {}
      }
    },
    // 移动bar选中颜色
    activeBarColor: {
      type: String,
      default: '#4990F9'
    },
    // 文字选中颜色
    activeColor: {
      type: String,
			default: '#4990F9'
    },
    // 过渡动画时长, 单位ms
    duration: {
      type: [String, Number],
      default: 0.5
    },
    // tab高度
    height: {
      type: Number,
      default: 54
    },
    // 字体
    fontSize: {
      type: String,
      default: '25rpx'
    },
    // 是否滚动，默认是，否则是不滚动，按个数等分宽度
    scroll: {
      type: Boolean,
      default: true
    },
    // margin-right
    marright: {
      type: String,
      default: '38px'
    },
    // font-weight
    activeWeight: {
      type: String,
      default: 'normal'
    }
  },
  watch: {
    list(n, o) {
      // list变动时，重制内部索引，否则可能导致超出数组边界的情况
      if (n.length !== o.length) this.currentIndex = 0;
      this.$nextTick(() => {
        this.init();
      });
    },
    current: {
      immediate: true,
      handler(nVal, oVal) {
        // 视图更新后再执行移动操作
        this.$nextTick(() => {
          this.currentIndex = nVal;
          this.scrollByIndex();
        });
      }
    }
  },
  computed: {
    tabBarStyle: function() {
      let style = {
        width: this.barWidth + 'rpx',
        height: this.barHeight + 'rpx',
        transform: `translateX(${this.scrollBarLeft}px)`,
        'transition-duration': `${this.barFirstTimeMove ? 0 : this.duration}s`,
        'border-radius': `${this.barHeight / 2}rpx`,
        'background-color': this.activeBarColor
      }
      Object.assign(style, this.barStyle)
      return style
    },
    tabHeight() {
      return this.height + 'rpx'
    }
  },
  data() {
    return {
      scrollLeft: 0,
      componentWidth: 0, // tabs组件宽度，单位为px
      parentLeft: 0, // 父元素(tabs组件)到屏幕左边的距离
      scrollBarLeft: 0, // 移动bar需要通过translateX()移动的距离
      tabItemRectInfo: [],
      currentIndex: 0,
      barFirstTimeMove: true
    };
  },
  mounted() {
    this.init()
  },
  methods: {
    init() {
      console.log("#init")
      this.$nextTick(() => {
        uni.createSelectorQuery().in(this).select(`#tab`).fields({
          size: true,
          rect: true
        }, data => {
          console.log("#tab-data:", data)
          this.parentLeft = data.left;
            // tabs组件的宽度
          this.componentWidth = data.width;
        }).exec();
        this.getTabItemRect()
      })
    },
    getTabItemRect() {
      console.log("getTabItemRect")
      let query = uni.createSelectorQuery().in(this);
      for (let i = 0; i < this.list.length; i++) {
        // 只要size和rect两个参数
        query.select(`#tab-item-${i}`).fields({
          size: true,
          rect: true
        });
      }
      query.exec((data) => {
        console.log("#data:", data)
        this.tabItemRectInfo = data
        this.scrollByIndex()
      })
    },
    scrollByIndex() {
      let tabInfo = this.tabItemRectInfo[this.currentIndex]
      if (!tabInfo) return
      // 活动tab的宽度
      let tabWidth = tabInfo.width
      let offsetLeft = tabInfo.left - this.parentLeft
      // 将活动的tabs-item移动到屏幕正中间，实际上是对scroll-view的移动
      let scrollLeft = offsetLeft - (this.componentWidth - tabWidth) / 2
      this.scrollLeft = scrollLeft < 0 ? 0 : scrollLeft;
      // 计算当前活跃item到组件左边的距离
      let left = tabInfo.left + tabInfo.width / 2 - this.parentLeft
      this.scrollBarLeft = left - this.barWidth / 2;
      if (this.barFirstTimeMove == true) {
        setTimeout(() => {
          this.barFirstTimeMove = false;
        }, 100)
      }
    },
    clickTab(item, index) {
      // 点击当前活动tab，不触发事件
      if (index == this.currentIndex) return
      this.currentIndex = index
      this.$emit('tabItemClick', item, index)
      this.scrollByIndex()
	  console.log(item,index)
    }
  }
}
</script>

<style lang="scss" scoped>
	.tab{
		height: 55rpx;
		margin-top: auto;
		// border: 1px solid black;
	}
  scroll-view {
		box-sizing: border-box;
		height: 100%;
	}
  ::v-deep ::-webkit-scrollbar {
    display: none
  }
  /* #ifndef APP-NVUE */
	::-webkit-scrollbar,
	::-webkit-scrollbar,
	::-webkit-scrollbar {
		display: none;
		width: 0 !important;
		height: 0 !important;
		-webkit-appearance: none;
		background: transparent;
	}
	/* #endif */
  /* #ifdef H5 */
	// 通过样式穿透，隐藏H5下，scroll-view下的滚动条
	scroll-view ::v-deep ::-webkit-scrollbar {
		display: none;
		width: 0 !important;
		height: 0 !important;
		-webkit-appearance: none;
		background: transparent;
	}
	/* #endif */

  .scroll-view-content{
    white-space: nowrap;
    width: 100%;
    position: relative;
    .scroll-item-view{
      width: 100%;
      white-space: nowrap;
      position: relative;
    }
    .scroll-item{
      position: relative;
      padding: 0 10px;
      display: inline-block;
		  text-align: center;
      color: #8C8C8C;
    }
    .mar-r-38 {
      margin-right: 38px;
    }
    .no-right{
      margin: 0;
    }
    .scroll-tab-bar {
      position: absolute;
      bottom: 0;
      z-index: 10;
    }
  }
</style>
 