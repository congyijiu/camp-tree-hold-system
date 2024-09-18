<template>
	<view class="content">
		<view class="top animate__animated animate__bounceInUp">
			<view class="tab-top">
				<view class="top-search">
					<u-icon class="search" name="search" @click="toSearch" size="34rpx"></u-icon>
				</view>
			</view>
			<view class="tab-content">
				<tabs :list="typelist" :current="currentIndex" @tabItemClick="itemClick" marright="25px"
					activeWeight='600'></tabs>
			</view>
		</view>

		<swiper :autoplay="false" @change="changeSwiper" :current="currentIndex"
			:style="{ height: swiperHeight + 'px' }">
			<swiper-item class="swiper-item" v-for="(i,tabindex) in typelist" :key="tabindex">
				<view class="page1 animate__animated animate__backInLeft" :id="'content-wrap' + tabindex " v-if="show">
					<box :box="item" v-for="(item,index) in typelist[tabindex].records" :key="index" class="item"
						v-if="index%2==0"></box>
				</view>
				<view class="page2 animate__animated animate__backInRight" :id="'content-wrap' + tabindex ">
					<box :box="item" v-for="(item,index) in typelist[tabindex].records" :key="index" class="item"
						v-if="index%2==1"></box>
				</view>
			</swiper-item>
		</swiper>
	</view>
</template>

<script>
	import box from '../../components/box.vue'
	import tabs from '../../components/yixuan-tabs/yixuan-tabs.vue'
	import {
		list
	} from '@/apis/list.js'
	import {
		getPage
	} from '@/apis/loadPage.js'
	export default {
		components: {
			box,
			tabs
		},
		data() {
			return {
				isActive: 1,
				tabList: [],
				typelist: [],
				schoolList: [],
				foodList: [],
				//滑块的高度(单位px)
				swiperHeight: 0,
				//当前索引
				currentIndex: 0,
				imgHeight: [],
				pageData: {
					//页码
					pageIndex: 1,
					//每页多少条数据
					pagesize: 20,
					//页面类型
					type: 1,
				},
				current: 0,
				show:true
			}
		},
		async onLoad(args) {
			//首页数据请求
			try {
				const res = await getPage(this.pageData);
				// console.log(res)
				this.tabList = res.data.records;
				this.tabList.forEach(item => {
					if (item.imageUrl) {
						// 检查 imageUrl 是否存在且不为 undefined
						const imageUrlArray = item.imageUrl.split(',');
						// 更新 item 中的 imageUrl
						item.imageUrl = imageUrlArray[0];
					}
				});
				console.log("请求数据成功", this.tabList);
			} catch (err) {
				console.log("请求数据失败", err);
			}
			//校园数据请求
			try {
				this.pageData.type = 2
				const res = await getPage(this.pageData);
				// console.log(res)
				this.schoolList = res.data.records;
				this.schoolList.forEach(item => {
					if (item.imageUrl) {
						// 检查 imageUrl 是否存在且不为 undefined
						const imageUrlArray = item.imageUrl.split(',');
						// 更新 item 中的 imageUrl
						item.imageUrl = imageUrlArray[0];
					}
				});
				console.log("请求数据成功", this.schoolList);
			} catch (err) {
				console.log("请求数据失败", err);
			}
			//美食数据请求
			try {
				this.pageData.type = 3
				const res = await getPage(this.pageData);
				// console.log(res)
				this.foodList = res.data.records;
				this.foodList.forEach(item => {
					if (item.imageUrl) {
						// 检查 imageUrl 是否存在且不为 undefined
						const imageUrlArray = item.imageUrl.split(',');
						// 更新 item 中的 imageUrl
						item.imageUrl = imageUrlArray[0];
					}
				});
				console.log("请求数据成功", this.foodList);
			} catch (err) {
				console.log("请求数据失败", err);
			}
			try {
				//顶部导航栏接口请求
				const res = await list();
				this.typelist = res.data
				this.typelist.forEach(item => {
					if (item.id == 1) {
						item.records = this.tabList
					}
					if (item.id == 2) {
						item.records = this.schoolList
					}
					if (item.id == 3) {
						item.records = this.foodList
					}
				})
				// console.log(res)
			} catch (e) {
				//TODO handle the exception
			}

			this.$nextTick(() => {
				this.setSwiperHeight();
			});
		},
		async onReachBottom() {
			try {
				if(this.currentIndex == 0)
				{
					this.pageData.type = 1
					// 在每次触底时增加 pageIndex
					this.pageData.pageIndex++;
					const res = await getPage(this.pageData);
					const newData = res.data.records;
					newData.forEach(item => {
						if (item.imageUrl) {
							const imageUrlArray = item.imageUrl.split(',');
							item.imageUrl = imageUrlArray[0];
						}
					});
					// 拼接新数据到原有数组
					this.tabList = this.tabList.concat(newData);
					this.typelist[0].records = this.tabList
					this.show = false
					this.show = true
					
				}
				if(this.currentIndex == 1)
				{
					this.pageData.type = 2
					// 在每次触底时增加 pageIndex
					this.pageData.pageIndex++;
					const res = await getPage(this.pageData);
					const newData = res.data.records;
					newData.forEach(item => {
						if (item.imageUrl) {
							const imageUrlArray = item.imageUrl.split(',');
							item.imageUrl = imageUrlArray[0];
						} 
					});
					// 拼接新数据到原有数组
					this.schoolList = this.schoolList.concat(newData);
					this.typelist[1].records = this.schoolList
					this.show = false
					this.show = true
					
					
				}
				
				//动态设置swiper的高度，使用nextTick延时设置
				this.$nextTick(() => {
					this.setSwiperHeight();
				});
			} catch (err) {
				console.log("请求数据失败", err);
			}
		},
		methods: {
			//手动切换题目
			changeSwiper(e) {
				this.currentIndex = e.detail.current;
				//动态设置swiper的高度，使用nextTick延时设置
				this.$nextTick(() => {
					this.setSwiperHeight();
				});
			},
			//动态设置swiper的高度
			setSwiperHeight() {
				let element = "#content-wrap" + this.currentIndex;
				let query = uni.createSelectorQuery().in(this);
				query.select(element).boundingClientRect();
				query.exec((res) => {
					if (res && res[0]) {
						this.swiperHeight = res[0].height;
					}
				});
			},
			itemClick(item, index) {
				this.currentIndex = index
			},
			toSearch() {
				uni.navigateTo({
					url: '/pages/search/search',
					animationType: 'pop-in',
					animationDuration: 200,
					success(result) {
						console.log('navigateTo success', result.errMsg)
					},
					fail(error) {
						console.log('navigateTo fail', error.errMsg)
					},
					complete(result) {
						console.log('navigateTo complete', result.errMsg)
					},
				})
			},
			change(index) {
				this.current = index;
			}
		},
	}
</script>


<style>
	.content {
		display: flex;
		flex-direction: column;
		margin-top: 50rpx;
		background-color: whitesmoke;
	}

	.tab-top {
		height: 90rpx;
		background-color: #fff;
		width: 750rpx;
		display: flex;
		flex-direction: row;
		/* justify-content: flex-end; */
		/* border: 1px solid red; */
	}

	.tab {
		width: 650rpx;
	}

	.top-search {
		width: 100rpx;
		display: flex;
		justify-content: flex-end;
	}

	.search {
		width: 65rpx;
		height: 65rpx;
		margin-top: 10rpx;
	}

	.tab-content {
		height: 55rpx;
		background-color: #fff;
		border: 1px solid gainsboro;
	}


	.swiper-item {
		background-color: whitesmoke;
		display: flex;
		flex-wrap: wrap;
		justify-content: space-evenly;
		/* border: 1px solid blue; */
	}

	.page1 {
		width: 360rpx;
	}

	.page2 {

		width: 360rpx;
	}

	.item {
		/* border: 1px solid red; */
		margin-top: 0rpx;
	}

	.top {
		background-color: #f1f1f1;
		position: -webkit-sticky;
		position: sticky;
		top: var(--window-top);
		z-index: 99;
		/* 		// // 设置定位效果为“吸顶”
		//   position: sticky;
		//   // 吸顶的“位置”
		//   top: 0;
		//   // 提高层级，防止被轮播图覆盖
		//   z-index: 999; */
	}
</style>