<template>
	<view class="content">
		<view class="box1" @click = "toboxdetail">
			<view class="picture">
				<image mode="widthFix" class="picture" :src="box.imageUrl"></image>
			</view>
			<view class="title">
				<text class="word">{{box.title}}</text>
			</view>
			<view class="uni-user">
			<view class="userinfo">
				<image class="userimg" :src="box.avatarUrl"></image>
				<text class="username">{{box.username}}</text>
			</view>
			<view class="liked">
				<u-icon name="heart" class="like"></u-icon>
				<!-- <image class="like" src="/static/support.png"></image> -->
				<text class="likeCount">{{box.likeCount}}</text>
			</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name:"box",
		props: {
			box: {
				type: Object,
				default () {
					return {}
				}
			}
		},
		data() {
			return {
				 
				username:this.box.username,
				userImg:this.box.avatarUrl,
				boxId:this.box.id,
				
			}
		},
		onLoad() {
			
		},
		methods: {
			toboxdetail(){
				uni.navigateTo({
				       url: `/pages/boxdetail/boxdetail?username=${this.username}&boxId=${this.boxId}&userImg=${this.userImg}`,
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
			}
				
		}
	}
</script>

<style>
.content{
	margin-bottom: 10rpx;
	break-inside: avoid;
	}
.box1{
	width: 360rpx;
	flex-direction: column;
	border-radius: 10rpx;
	background-color: #fff
}
.picture{
	width: 100%;
	border-radius: 10rpx;
}
.title{
	width: 349rpx;
	height: auto;
	display: flex;
}
.userinfo{
	margin-top: 10rpx;
	display: flex;
	width: 200rpx;
}
.word{
	font-size: 25rpx;
	font-weight: 500;
	 overflow:hidden;
	   text-overflow:ellipsis;
	   display:-webkit-box;
	   -webkit-box-orient:vertical;
	   -webkit-line-clamp:2;
}
.userimg{
	width: 40rpx;
	height: 40rpx;
	 border-radius: 80rpx;
	 margin-bottom: 7rpx;
}
.username{
	font-size: 20rpx;
	font-weight: 300;
	margin-bottom: 5rpx;
	margin-left: 5rpx;
	margin-top: 15rpx;
}
.like{
	width: 35rpx;
	height: 35rpx;
	margin-top: 15rpx;
	margin-left: 60rpx;
}
.likeCount{
	font-size: 20rpx;
	font-weight: 400;
	margin-left: 10rpx;
	margin-top: 20rpx;
}
.uni-user{
	display: flex;
	flex-direction: row;	
}
.liked{
	display: flex;
}
</style>
