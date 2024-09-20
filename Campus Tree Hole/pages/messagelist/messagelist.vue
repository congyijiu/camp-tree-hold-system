<template>
	<view class="content">
		<u-navbar title="消息" :is-back="false" :background="background" height="70"></u-navbar>
		<uni-list>
			<uni-list-chat v-for="(item,index) in messageList" :key="index" :avatar-circle="true"
				:avatar="item.avatarUrl" :title="item.username" :note="item.content" :clickable="true"
				:badge-text="item.noreadNum" @click="toChatPage(item.userId)">
			</uni-list-chat>
		</uni-list>
	</view>
</template>

<script>
	import {
		otherUser
	} from '@/apis/user.js'
	import {
		friendList,
		messageList
	} from '@/apis/message.js'
	export default {
		data() {
			return {
				userinfo: {},
				sendUserId: "",
				sendUserinfo: [],
				sendUserMessage: "",
				messageCount: 0,
				background: {
					backgroundImage: 'linear-gradient(45deg, rgb(28, 187, 180), rgb(141, 198, 63))'
				},
				messageData: {
					index: 1,
					size: 9999
				},
				messageList: []
			}
		},
		async onLoad() {
			this.userinfo = uni.getStorageSync('userinfo');
			this.openSocket()
			const res = await friendList(this.messageData)
			console.log(res, '123')

			this.messageList = res.data.records
			console.log(this.messageList)
		},
		methods: {
			//开启socket连接
			openSocket() {
				//与服务器建立连接，接受消息
				uni.onSocketMessage((res) => {
					const sendUser = JSON.parse(res.data)
					var repeat = 0
					this.messageList.forEach(item => {
						if (item.userId == sendUser.sendUserId) {
							item.content = sendUser.content
							var temp = item
							temp.noreadNum++
							let index = this.messageList.indexOf(item)
							console.log(index)
							this.messageList.splice(index,1)
							this.messageList.unshift(temp)
							repeat = 1
						}


					})
					if (repeat == 0) {
						otherUser(sendUser.sendUserId).then(result => {
							console.log(result)
							let ob = {
								userId: sendUser.sendUserId,
								username: result.userName,
								avatarUrl: result.avatarUrl,
								type: "text",
								content: sendUser.content,
								time: null,
								noreadNum: 1
							}
							this.messageList.unshift(ob)
						})

					}
					console.log(this.messageList)
				})
			},
			toChatPage(item) {
				uni.navigateTo({
					url: `/pages/chat/index?userId=${item}`
				})
			}
		}
	}
</script>

<style>
	.content {
		background-color: #f3f4f6;


	}
</style>