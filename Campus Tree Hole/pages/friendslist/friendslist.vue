<template>
	<view class="content">
		<u-navbar  title="朋友" :is-back="false" :background="background" height="70"></u-navbar>
		<view class="followlist"  v-for="(item,index) in followsList" :key="index" @click="toOtherUser(item.id)">
			<u-avatar :src="item.avatarUrl" class="avatar"></u-avatar>
			<text>{{item.username}}</text>
		</view>
		
	</view>
</template>

<script>
	import {followsList} from'@/apis/user.js'
	export default {
		data() {
			return {
				
				background: {
					backgroundImage: 'linear-gradient(45deg, rgb(28, 187, 180), rgb(141, 198, 63))'
				},
				userinfo:{},
				followsList:[],
				followdata:{
					pageIndex:1,
					pageSize:20,
					userId:""
				}
			}
		},
		onLoad() {
			this.userinfo = uni.getStorageSync('userinfo');
			 this.followdata.userId = this.userinfo.id
			followsList(this.followdata).then(res=>{
				this.followsList = res.data.records
			})
		},
		onReachBottom() {
			this.followdata.pageIndex++
			this.followdata.userId = this.userinfo.id
			followsList(this.followdata).then(res=>{
				const newData = res.data.records
				this.followsList.concat(newData)
			})		
		},
		methods: {
			toOtherUser(item)
			{
				uni.navigateTo({
					url:`/pages/user/other-users?userId=${item}`
				})
			}
		}
	}
</script>

<style>
	.content{
		background-color: #f3f4f6;
		height: 100vh;
	}
.followlist{
	display: flex;
	flex-direction: row;
	height: 150rpx;
	width: 90%;
	background-color: #fff;
	margin: auto;
	border-radius: 30rpx;
	margin-top: 20rpx;
	margin-bottom: 20rpx;
	
}
.followlist text{
	margin-left: 30rpx;
	margin-top: 70rpx;
}
.avatar{
	margin-left: 50rpx;
	margin-top: 40rpx;
}
</style>
