<template>
	<view class="container">
		<view class="first-box">
			<u-icon class="back" name="arrow-left" color="#fff" size="40" @click="backTo"></u-icon>
			<!-- 	<view class="go-out">
				<navigator url="/pages/login/login"  hover-class="none">
					<image src="../../static/  (2).png" class="go-out-img"></image>
				</navigator>
			</view> -->
			<view class="user-info animate__animated animate__rubberBand">
				<view class="left">
					<image :src="otherUserinfo.avatarUrl" class="user-img"></image>
				</view>

				<view class="right">
					<view class="user-name">
						<text>{{otherUserinfo.username}}</text>
						<image src="@/static/male.png" class="user-img"></image>
					</view>
					<view class="user-des">
						<text class="user-hobbies">{{otherUserinfo.hobbies}}</text>
					</view>
					<view class="user-like-fans">
						<text class="like">{{otherUserinfo.follows}}关注</text>
						<text class="fans">{{otherUserinfo.fans}}粉丝</text>
						<button @click="toChat">发送消息</button>
					</view>
				</view>

			</view>
			<view class="body animate__animated animate__bounceInUp">
				<view>
					<u-tabs-swiper ref="uTabs" :list="list" :current="current" @change="tabsChange" :is-scroll="false"
						swiperWidth="750"></u-tabs-swiper>
				</view>
				<swiper :current="swiperCurrent" @transition="transition" @animationfinish="animationfinish"
					:style="[{height: swiperHeight+ 'rpx'}]">
					<swiper-item class="swiper-item">
						<scroll-view scroll-y @scrolltolower="onreachBottom" style="height: 100vh;width: 100%;">
							<view class="swiper-item-all">
								<view class="page1">
									<box :box="item" v-for="(item,index) in opusData" :key="index" class="item"
										v-if="index%2==0"></box>
								</view>
								<view class="page2">
									<box :box="item" v-for="(item,index) in opusData" :key="index" class="item"
										v-if="index%2==1"></box>
								</view>
							</view>

						</scroll-view>
					</swiper-item>
					<swiper-item class="swiper-item">
						<scroll-view scroll-y style="height: 100vh;width: 100%;" @scrolltolower="onreachBottom">
							<view class="swiper-item-all">
								<u-empty mode="favor" :show="show"></u-empty>
								<view class="page1">
									<box :box="item" v-for="(item,index) in collectionData" :key="index" class="item"
										v-if="index%2==0"></box>
								</view>
								<view class="page2">
									<box :box="item" v-for="(item,index) in collectionData" :key="index" class="item"
										v-if="index%2==1"></box>
								</view>
							</view>

						</scroll-view>
					</swiper-item>
				</swiper>
				

			</view>
		</view>
	</view>

</template>
<script>
	import box from '../../components/box.vue'
	import {
		otherUser
	} from '@/apis/user.js'
	import {
		search
	} from '@/apis/detail.js'
	import {
		collectionQuery
	} from '@/apis/detail.js'
	export default {
		components: {
			box
		},
		data() {
			return {
				username: "林甲锋",
				sex: "男",
				favor: "玩游戏",
				userId: "",
				otherUserinfo: {},
				list: [{
					name: '作品'
				}, {
					name: '收藏'
				}],
				// 因为内部的滑动机制限制，请将tabs组件和swiper组件的current用不同变量赋值
				current: 0, // tabs组件的current值，表示当前活动的tab选项
				swiperCurrent: 0, // swiper组件的current值，表示当前那个swiper-item是活动的
				searchData: {
					pageIndex: 1,
					pageSize: 20,
					type: 1,
					userId: ""
				},
				opusData: [],
				swiperHeight: 1000,
				collectionData: [],
				show:false,
				opusShow:false
			}
		},
		async onLoad(option) {
			this.userId = option.userId
			try {
				const res = await otherUser(this.userId)
				this.otherUserinfo = res.data
				console.log(this.otherUserinfo)
			} catch (e) {
				//TODO handle the exception
			}
			//查询用户作品
			try {
				this.searchData.userId = this.userId
				const res = await search(this.searchData)
				this.opusData = res.data.records
				if(this.collectionData=="")
				{
					this.opusShow = true
				}
			} catch (e) {
				//TODO handle the exception
			}
			//查询用户收藏
			try {
				this.searchData.userId = this.userId
				const res = await collectionQuery(this.searchData)
				this.collectionData = res.data.records
				if(this.collectionData=="")
				{
					this.show = true
				}
			} catch (e) {
				//TODO handle the exception
			}
		},
		methods: {
			toset() {
				uni.navigateTo({
					url: '/pages/seting/seting',
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
			}, // tabs通知swiper切换
			tabsChange(index) {
				this.swiperCurrent = index;
			},
			// swiper-item左右移动，通知tabs的滑块跟随移动
			transition(e) {
				let dx = e.detail.dx;
				this.$refs.uTabs.setDx(dx);
			},
			// 由于swiper的内部机制问题，快速切换swiper不会触发dx的连续变化，需要在结束时重置状态
			// swiper滑动结束，分别设置tabs和swiper的状态
			animationfinish(e) {
				let current = e.detail.current;
				this.$refs.uTabs.setFinishCurrent(current);
				this.swiperCurrent = current;
				this.current = current;
			},
			// scroll-view到底部加载更多
			async onreachBottom() {
				try {
					this.searchData.userId = this.userinfo.id
					this.searchData.pageIndex = this.searchData.pageIndex + 1
					const res = await search(this.searchData)
					const newData = res.data.records
					//拼接新请求的数据
					this.opusData = this.opusData.concat(newData);
				} catch (e) {
					//TODO handle the exception
				}
			},
			backTo()
			{
				uni.reLaunch({
					url:'/pages/index/index'
				})
			},
			toChat()
			{
				uni.reLaunch({
					url:`/pages/chat/index?userId=${this.userId}`
				})
			}
		}
	}
</script>

<style>
	.container {
		/* border: 1px red solid; */
		background-color: whitesmoke;
		/* background-image: url("../../static/true.jpg"); */
		background-size: 100% 100%;
		height: 100vh;
		display: flex;
		/* justify-content: center;
	align-items: center; */
		
	}

	.first-box {
		background-image: url("../../static/user-background.webp");
		background-size: 100% 100%;
		height: 500rpx;
		width: 100%;
		/* display: flex;
	justify-content: center;
	align-items: center; */
		
	}

	.go-out {
		height: 80rpx;
		display: flex;
		justify-content: flex-end;
		align-items: center;
		padding: 15rpx;
		/* background-color: #fff; */
	}

	.go-out-img {
		width: 50rpx;
		height: 50rpx;
	}

	.user-info {
		height: 350rpx;
		/* background: #000; */
		display: flex;
		padding: 10rpx;
		/* margin: 20rpx; */
		align-items: center;
	}

	.user-img {
		width: 200rpx;
		height: 200rpx;
		border-radius: 100%;
		margin-left: 10rpx;
		margin-right: 20rpx;
		border: 3px rgb(52, 224, 212) solid;
	}


	.user-info .right {
		width: 480rpx;
		height: 200rpx;
		/* background-color: pink; */
	}

.user-des text{
		font-size: 24rpx;
		 display: -webkit-box;
		  -webkit-line-clamp: 2;
		  -webkit-box-orient: vertical;
		  overflow: hidden;
	}
	.user-name {
		font-size: 45rpx;
		color: rgb(52, 224, 212);
		font-weight: bold;
		display: flex;
		align-items: center;

	}

	.user-name image {
		margin-left: 20rpx;
		width: 50rpx;
		height: 50rpx;
		border: none;
	}


	.user-des {
		height: 80rpx;
		width: 430rpx;
		color: aliceblue;
	}

	.user-like-fans {
		height: 80rpx;
		color: aliceblue;
		font-size: 35rpx;
		display: flex;
		/* justify-content: space-evenly; */
		align-items: center;
		/* position: absolute; */
		/* bottom: 0px; */
		/* vertical-align: bottom; */
	}

	.like {
		bottom: 0px;
		margin-right: 30rpx;
		width: 150rpx;
	}

	.fans {
		margin-right: 40rpx;
	}




	.btn {
		display: flex;
		/* justify-items: auto; */
		/* justify-content: end; */
	}

	.btn2 {

		margin-left: 48rpx;
	}

	.btn1 button,
	.btn2 button {
		/* width: 100rpx; */
		/* 	display: flex;
	justify-self: start; */
		width: 140rpx;
		background-color: transparent;
		/* border-style:none; */
		border: 2px rgb(52, 224, 212) solid;
		color: aliceblue;
		-webkit-border-radius: 28;
		-moz-border-radius: 28;
		border-radius: 28px;
		font-family: Arial;
		color: #ffffff;
		/* font-size: 16px; */
		/* padding: 10px 20px 10px 20px; */
		text-decoration: none;
		font-size: 20rpx;
	}

	/* .firstpart{
	width: 100%;
	height: 600rpx;
	background-image: linear-gradient(15deg, #13547a 0%, #80d0c7 100%);
	display: flex;
	border-radius: 20rpx
	
}
.userinfo{
	width: 400rpx;
	height: 200rpx;
	display: flex;
	flex-direction: column;
	
	
}
.userimg{
	 border-radius: 80rpx;
	  width:160rpx;
	  height:160rpx;	
	  margin: 50rpx;
	
}
.name{
		margin-top: 70px;
		margin-left: 0;
		
}
.sex{
	margin-top: 10rpx;
	margin-left: 0;
	font-weight: 200;
	font-size: 30%;
	
	
}
.seting{
	color: antiquewhite;
	height: 55rpx;
	width: 55rpx;
	margin-top: 50rpx;
	
}
.favor{
	margin-top: 10rpx;
	margin-left: 0;
	font-weight: 200;
	font-size: 30%;
} */
	.body {
		height: 800rpx;
		background-color: #f3f4f6;
		border-radius: 20rpx;
	}

	.swiper-item {
		height: 800rpx;
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
	.user-hobbies{
		font-size: 16rpx;
		text-overflow:ellipsis;
		display:-webkit-box;
		-webkit-box-orient:vertical;
		-webkit-line-clamp:2;
	}
	.back{
		/*  #ifdef APP-PLUS */
			margin-top: 60rpx;
		/*  #endif  */
		margin-top: 40rpx;
		margin-left: 40rpx;
		
	}
	.user-like-fans button {
		/* width: 100rpx; */
	
		background-color: transparent;
		/* border-style:none; */
		border: 2px rgb(52, 224, 212) solid;
		color: aliceblue;
	
		/* background: #3498db;
	    background-image: -webkit-linear-gradient(top, #3498db, #2980b9);
	    background-image: -moz-linear-gradient(top, #3498db, #2980b9);
	    background-image: -ms-linear-gradient(top, #3498db, #2980b9);
	    background-image: -o-linear-gradient(top, #3498db, #2980b9);
	    background-image: linear-gradient(to bottom, #3498db, #2980b9); */
		-webkit-border-radius: 28;
		-moz-border-radius: 28;
		border-radius: 28px;
		font-family: Arial;
		color: #ffffff;
		font-size: 16px;
		/* padding: 10px 20px 10px 20px; */
		text-decoration: none;
	
		font-size: 25rpx;
	
	}
</style>
