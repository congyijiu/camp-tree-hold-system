<template>
	<view>
		<u-navbar back-text="返回" title="发布动态"></u-navbar>
		<u-upload ref="uUpload" :auto-upload="false" max-count="6"></u-upload>
		<u-input class="input" v-model="publishData.title" placeholder="有趣的标题更能吸引人"></u-input>
		<u-line length="80%" />
		<u-input class="textarea" v-model="publishData.content" type="textarea" placeholder="添加正文"
			height="200"></u-input>
		<u-button class="custom-style" @tap="submit" size="medium">发布</u-button>
		<u-toast ref="uToast" />
		<u-toast ref="Toast" />
		<u-toast ref="UToast" />
		<u-modal v-model="show" :content="content" @confirm="confirm" :async-close="true"
			:mask-close-able="true"></u-modal>
		<u-select v-model="selectShow" :list="list" @confirm="selectConfirm"></u-select>
		
	</view>
</template>

<script>
	import {
		publish
	} from '@/apis/detail.js'
	export default {
		data() {
			return {
				sourceType: ['album', 'camera'],
				publishData: {
					title: "",
					content: "",
					imageUrls: [],
					typeId: 1
				},
				show: false,
				content: "确认发布？",
				list: [{
						value: '2',
						label: '校园'
					},
					{
						value: '3',
						label: '美食'
					}
				],
				selectShow:false
			}
		},
		methods: {
			async submit() {
				this.show = !this.show
				let files = [];
				files = this.$refs.uUpload.lists;
				let userinfo = ''
				uni.getStorage({
					key: 'userinfo',
					success: function(res) {
						console.log('awawawawaw', res.data);
						userinfo = res.data
					}
				});
				if (files.length == 0) {
					this.$refs.uToast.show({
						title: '请至少选择一张图片',
						type: 'error',
					})
				}
				if (this.title == "") {
					this.$refs.Toast.show({
						title: '请写出您的标题',
						type: 'error',
					})
				}
				if (files.length != 0) {
					// 创建一个 Date 对象
					var currentDate = new Date();
					// 获取年、月、日、小时、分钟和秒
					var year = currentDate.getFullYear();
					var month = ('0' + (currentDate.getMonth() + 1)).slice(-2); // 月份从0开始，需要加1
					var day = ('0' + currentDate.getDate()).slice(-2);
					var hours = ('0' + currentDate.getHours()).slice(-2);
					var minutes = ('0' + currentDate.getMinutes()).slice(-2);
					var seconds = ('0' + currentDate.getSeconds()).slice(-2);
					// 格式化时间
					var formattedTime = year + '-' + month + '-' + day + ' ' + hours + ':' + minutes + ':' + seconds;
					// 输出格式化后的时间
					console.log('当前时间：' + formattedTime);
					files.forEach(item => {
						let cloudPath = '/dynamic/' + userinfo.id + '/' + formattedTime + '/' + item.file.name
						uniCloud.uploadFile({
							filePath: item.url,
							cloudPath: cloudPath,
							cloudPathAsRealPath: true
						}).then(result => {
							console.log(result.fileID)
							this.publishData.imageUrls.push(result.fileID)
						})

					})
				}
				console.log('awa', this.publishData.imageUrls)
			},
			confirm() {
				setTimeout(() => {
					console.log(this.publishData)
					publish(this.publishData)
					this.$refs.Toast.show({
						title: '发布成功',
						type: 'success',
						url: '/pages/user/user',
						duration: 1000
					})
					// 2秒后自动关闭
					this.show = false;
					uni.reLaunch({
						url: '/pages/user/user'
					})
				}, 1500)

			},
			selectConfirm(e)
			{
				
				this.publishData.typeId = e[0].value
			}

		}
	}
</script>

<style scoped>
	.custom-style {
		background-color: #fa3534;
		color: #fff;
		width: 600rpx;
		margin-top: 400rpx;
		margin-left: 75rpx;
	}

	.input {
		margin-left: 20rpx;
		margin-top: 50rpx;
	}

	.textarea {
		margin-top: 20rpx;
		margin-left: 20rpx;
	}
</style>
