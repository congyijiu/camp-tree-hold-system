<template>
	<view class="total">
		<view class="top">
			<image class="back" src="../../static/back.png" @click="backindex"></image>
			<image class="userimg" :src="userImg" @click="ToOtherUser"></image>
			<text class="username" @click="ToOtherUser">{{username}}</text>
			<button class="concern" :style="{backgroundColor: bg_color,color: ft_color,borderColor:bd_color}"
				plain="true" @click="concern" type="default">{{buttoncontent}}</button>
		</view>
		<swiper class="contentPicture" :autoplay="false" indicator-dots="true" :style="{ height: imageHeight + 'rpx' }"
			:current="currentIndex">
			<swiper-item v-for="(item,index) in boxDetail.imageUrlList" :key="index">
				<image class="picture" :src="item" mode="aspectFill" :id="'imageSwiper' + index"></image>
			</swiper-item>

		</swiper>
		<view class="title">
			<uni-title type="h3" :title="boxDetail.title"></uni-title>
		</view>
		<view class="text">
			<text class="textcontent">{{boxDetail.content}}</text>
		</view>
		<view class="comments">
			<view class="title">
				<uni-title type="h5" title="评论"></uni-title>
				<text class="textcontent">共{{boxDetail.commentCount}}条评论</text>

			</view>
			<view class="text">
				<uni-list v-for="(item,index) in commentList" :key="index">

					<uni-list-item :title="item.username" :thumb="item.avatarUrl" :note="item.commentDate"
						thumbSize="medium" :clickable="true" @click="ToCommentUser(item.userId)"></uni-list-item>
					<view class="commentReply">
						<text class="commentsContent">{{item.content}}</text>
						<text class="replyto" @click="replyto(item)">回复</text>
						<view class="commentLike" v-if="update">
							<u-icon @click="commentLike(item)" :name="item.commentHeart" size="25"
								:color="item.commentColor"></u-icon>
							<text style="font-size: 18rpx; marginLeft: 15rpx;">{{item.likeCount}}</text>
						</view>
					</view>

					<text class="queryreply" @click="queryreply(item)"
						v-if="item.replyCount!=0">展开{{item.replyCount}}条回复</text>
					<u-popup v-model="show" :mask="false" mode="bottom" height="500px" :closeable="true"
						border-radius=30>
						<scroll-view scroll-y="true" style="height: 750rpx;">
							<uni-list v-for="(item2,index) in secondaryComment" :key="index">
								<uni-list-item :title="item2.username" :thumb="item2.avatarUrl"
									:note="item2.commentDate" thumbSize="medium"></uni-list-item>
								<view class="commentReply">
									<text>回复@{{item2.replyUsername}}</text>
									<text style="fontSize: 25rpx; marginLeft:20rpx;">{{item2.content}}</text>
									<text class="replyto" @click="replytoSecondaryComment(item2)">回复</text>
									<view class="commentLike" v-if="update">
										<u-icon @click="commentLike(item)" :name="item.commentHeart" size="25"
											:color="item.commentColor"></u-icon>
										<text style="font-size: 18rpx; marginLeft: 15rpx;">{{item.likeCount}}</text>
									</view>
								</view>
							</uni-list>
						</scroll-view>
					</u-popup>
				</uni-list>
			</view>
			<view class="end">-----end-------</view>
			<view>
				<!-- 回复成功消息提示 -->
				<u-toast ref="uToast" />
			</view>
			<view class="reply">
				<u-avatar :src="avatarSrc" size="45" mode="square" class="avatar"></u-avatar>
				<input placeholder="聊聊你的看法吧" class="commentinput" v-model="comment.content" inputmode="text" />
				<button type="primary" @click="sentMsg" class="send-btn">发送</button>
				<view class="like">
					<u-icon :name="heart" size="50" @click="like" :color="color"></u-icon>
					<text>{{boxDetail.likeCount}}</text>
					<u-icon :name="star" size="50" :color="favoriteColor" @click="favorite"></u-icon>
					<text>{{boxDetail.favoriteCount}}</text>
				</view>

			</view>
		</view>

	</view>
</template>

<script>
	import {
		detail
	} from '@/apis/detail.js'
	import {
		addcomments
	} from '@/apis/detail.js'
	import {
		commentsPage
	} from '@/apis/detail.js'
	import {
		follow
	} from '@/apis/user.js'
	import {
		collection
	} from '@/apis/detail.js'
	import {
		commentIsLike
	} from '@/apis/detail.js'
	export default {
		data() {
			return {
				username: "",
				userImg: "",
				imageHeight: 900,
				currentIndex: 0,
				bg_color: "white",
				ft_color: "#FFA500",
				bd_color: "#FFA500",
				buttoncontent: "+关注",
				liked: false,
				boxDetail: [],
				comment: {
					boxId: "",
					content: "",
					toCommentId: "",
					topCommentId: "",
					pageIndex: "1",
					pageSize: "10",
					type: "1"
				},
				commentList: [],
				show: false,
				secondaryComment: [],
				avatarSrc: "",
				heart: "heart",
				star: "star",
				isLike: {
					dynamicId: "",
					islike: ""
				},
				color: "",
				favoriteColor: "",
				isFavorite: {
					dynamicId: "",
					isfavorite: ""
				},
				commentLikeData: {
					commentId: "",
					isLike: "",
				},
				commentHeart: "heart",
				commentColor: "",
				update: true,
				replytoSecondaryCommentShow: false,
				value: '',
				type: 'textarea',
				border: true,
				height: 280,
				autoHeight: true,
			}
		},
		async onLoad(option) {
			this.username = option.username
			this.comment.boxId = option.boxId
			this.userImg = option.userImg
			try {
				const res = await detail(option.boxId);
				this.boxDetail = res.data;
				//判断是否关注
				if (this.boxDetail.isFollow == "null") {
					this.liked = false
					this.buttoncontent = "+关注";
					this.bg_color = "white";
					this.ft_color = "#ffa500";
				}
				if (this.boxDetail.isFollow == 1) {
					this.liked = true
					this.buttoncontent = "已关注";
					this.bg_color = "#ffa500";
					this.ft_color = "#f0f0f0";
				}
				//判断是否点赞
				if (this.boxDetail.isLike == "null" || this.boxDetail.isLike == 0) {
					this.heart = "heart"
				}

				if (this.boxDetail.isLike == 1) {
					this.heart = "heart-fill"
					this.color = "#fa3534"
				}
				//判断是否收藏
				if (this.boxDetail.isFavorite == "null" || this.boxDetail.isFavorite == 0) {
					this.star = "star"
				}

				if (this.boxDetail.isFavorite == 1) {
					this.star = "star-fill"
					this.favoriteColor = "#ff9900"
				}
			} catch (e) {
				console.log(e)
				//TODO handle the exception
			}
			//获取评论列表
			try {
				const res = await commentsPage(this.comment);
				this.commentList = res.data.records;
				this.commentList.forEach(item => {
					if (item.commentDate) {
						const commentDataArray = item.commentDate.split('T');
						item.commentDate = commentDataArray[0] + " " + commentDataArray[1]
					}
					//给每个评论加上点赞图标
					item["commentColor"] = ""
					item["commentHeart"] = "heart"
					//判断是否给评论点赞
					if (item.isLike == 1) {
						item.commentHeart = "heart-fill"
						item.commentColor = "#fa3534"
					}
				})
			} catch (e) {
				//TODO handle the exception
				console.log(e)
			}
			const userinfo = uni.getStorageSync('userinfo');
			this.avatarSrc = userinfo.avatarUrl
		},
		async onReachBottom() {
			try {
				//将评论页码加一
				this.comment.pageIndex++
				//请求评论接口
				const res = await commentsPage(this.comment);
				const newCommentList = res.data.records;
				//将时间格式修改
				newCommentList.forEach(item => {
					if (item.commentDate) {
						const commentDataArray = item.commentDate.split('T');
						item.commentDate = commentDataArray[0] + " " + commentDataArray[1]
					}
					//给每个评论加上点赞图标
					item["commentColor"] = ""
					item["commentHeart"] = "heart"
					//判断是否给评论点赞
					if (item.isLike == 1) {
						item.commentHeart = "heart-fill"
						item.commentColor = "#fa3534"
					}
				})
				//将新请求到的评论拼接到commentList后面
				this.commentList = this.commentList.concat(newCommentList)
			} catch (e) {
				//TODO handle the exception
			}
		},
		methods: {
			backindex() {
				uni.switchTab({
					url: '/pages/index/index',
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
			setSwiperHeight() {
				// let element = "#imageSwiper" + this.currentIndex;
				// let query = uni.createSelectorQuery().in(this);
				// query.select(element).boundingClientRect();
				// query.exec((res) => {
				//   if (res && res[0]) {
				//     this.imageHeight = res[0].height;
				//   }
				// });
			},
			async concern() {
				try {
					await follow(this.boxDetail.userId)
					this.liked = !this.liked
					if (this.liked) {
						this.buttoncontent = "已关注";
						this.bg_color = "#ffa500";
						this.ft_color = "#f0f0f0";
						console.log(this.liked)
					} else {
						this.buttoncontent = "+关注";
						this.bg_color = "white";
						this.ft_color = "#ffa500";
						console.log(this.liked)
					}
				} catch (e) {
					//TODO handle the exception
				}

			},
			//发送评论
			async sentMsg() {
				try {
					//存入发送的评论
					await addcomments(this.comment).then(res => {
						// 获取到缓存的userinfo
						const userinfo = uni.getStorageSync('userinfo');
						//将刚刚写的评论储存到一个对象中，并将这个对象插入commentList中，回显到评论区中
						const myComment = {
							avatarUrl: userinfo.avatarUrl,
							commentDate: "刚刚",
							commentId: res.data.commentId,
							content: res.data.content,
							dynamicId: res.data.dynamicId,
							isDeleted: 0,
							isLike: "",
							likeCount: 0,
							replyCount: 0,
							toCommentId: "",
							topCommentId: "",
							userId: "",
							username: userinfo.username
						}
						//将用户写好的评论插入到commentList中
						this.commentList.splice(1, 0, myComment)
						this.$refs.uToast.show({
							title: '回复成功',
							type: 'success',
						})
						//将输入框中的数据清空
						this.comment.content = ""
					})
				} catch (e) {
					//TODO handle the exception
					console.log(e)
				}
			},
			//回复一级评论
			async replyto(item) {
				uni.showModal({
					title: '回复',
					content: '',
					editable: true, //是否显示输入框
					placeholderText: '回复@' + item.username, //输入框提示内容
					confirmText: '确认',
					cancelText: '取消',
					success: (res) => {
						if (res.confirm) {
							const replyComment = {
								content: res.content,
								boxId: item.dynamicId,
								toCommentId: item.commentId,
								topCommentId: item.commentId
							}
							addcomments(replyComment).then(res => {
								this.$refs.uToast.show({
									title: '回复成功',
									type: 'success',
								})
							})
					item.replyCount = item.replyCount + 1
						}
					}
				});
			},
			//查询更多回复
			async queryreply(item) {
				this.show = true
				const commentData = {
					boxId: item.dynamicId,
					toCommentId: item.commentId,
					topCommentId: item.commentId,
					pageIndex: 1,
					pageSize: 8,
					type: 2
				}
				const res = await commentsPage(commentData)
				this.secondaryComment = res.data.records
				this.secondaryComment.forEach(item => {
					if (item.commentDate) {
						const commentDataArray = item.commentDate.split('T');
						item.commentDate = commentDataArray[0] + " " + commentDataArray[1]
					}
					//给每个评论加上点赞图标
					item["commentColor"] = ""
					item["commentHeart"] = "heart"
					//判断是否给评论点赞
					if (item.isLike == 1) {
						item.commentHeart = "heart-fill"
						item.commentColor = "#fa3534"
					}
				})
			},
			//回复二级评论
			async replytoSecondaryComment(item) {
				uni.showModal({
					title: '回复',
					content: '',
					editable: true, //是否显示输入框
					placeholderText: '回复@' + item.username, //输入框提示内容
					confirmText: '确认',
					cancelText: '取消',
					success: (res) => {
						if (res.confirm) {
							const replyComment = {
								content: res.content,
								boxId: item.dynamicId,
								toCommentId: item.commentId,
								topCommentId: item.topCommentId
							}
							addcomments(replyComment)
						}
					}
				});
			},
			//点赞
			like() {
				if (this.heart == "heart") {
					this.isLike.dynamicId = this.boxDetail.id
					this.isLike.islike = 1
					this.heart = "heart-fill"
					this.color = "#fa3534"
					collection(this.isLike)
					this.boxDetail.likeCount = this.boxDetail.likeCount + 1

				} else {
					this.heart = "heart"
					this.color = ""
					this.isLike.dynamicId = this.boxDetail.id
					this.isLike.islike = 0
					collection(this.isLike)
					this.boxDetail.likeCount = this.boxDetail.likeCount - 1
				}
			},
			//收藏
			favorite() {
				if (this.star == "star") {
					this.star = "star-fill"
					this.favoriteColor = "#ff9900"
					this.isFavorite.dynamicId = this.boxDetail.id
					this.isFavorite.isfavorite = 1
					collection(this.isFavorite)
					this.boxDetail.favoriteCount = this.boxDetail.favoriteCount + 1
				} else {
					this.star = "star"
					this.favoriteColor = ""
					this.isFavorite.dynamicId = this.boxDetail.id
					this.isFavorite.isfavorite = 0
					collection(this.isFavorite)
					this.boxDetail.favoriteCount = this.boxDetail.favoriteCount - 1
				}
			},
			//评论点赞
			commentLike(item) {
				if (item.commentHeart == "heart") {
					item.commentHeart = item.commentHeart + "-" + "fill"
					item.commentColor = "#fa3534"
					this.commentLikeData.commentId = item.commentId
					this.commentLikeData.isLike = 1
					console.log(this.commentLikeData)
					commentIsLike(this.commentLikeData)
					item.likeCount = item.likeCount + 1

				} else {
					this.$set(item, 'commentHeart', 'heart')
					this.$set(item, 'commentColor', '')
					this.update = false
					this.update = true
					this.commentLikeData.commentId = item.commentId
					this.commentLikeData.isLike = 0
					commentIsLike(this.commentLikeData)
					item.likeCount = item.likeCount - 1
				}
			},
			ToOtherUser() {
				uni.reLaunch({
					url: `/pages/user/other-users?userId=${this.boxDetail.userId}`
				})
			},
			ToCommentUser(item) {
				console.log(item)
				uni.reLaunch({
					url: `/pages/user/other-users?userId=${item}`
				})
			}
		}
	}
</script>

<style>
	.total {
		display: flex;
		flex-direction: column;
		background-color: white;
	}

	.top {
		/*  #ifdef APP-PLUS */
		padding-top: 20rpx;
		/*  #endif  */
		display: flex;
		width: 750rpx;
		height: 160rpx;
		/* border: 1px solid black;	 */
		background-color: white;
		position: -webkit-sticky;
		position: sticky;
		top: var(--window-top);
		z-index: 99;
		// // 设置定位效果为“吸顶”
		//   position: sticky;
		//   // 吸顶的“位置”
		//   top: 0;
		//   // 提高层级，防止被轮播图覆盖
		//   z-index: 999;
	}

	.back {
		margin-top: 70rpx;
		margin-left: 30rpx;
		width: 30rpx;
		height: 30rpx;
	}

	.userimg {
		width: 70rpx;
		height: 70rpx;
		border-radius: 40rpx;
		margin-top: 50rpx;
		margin-left: 30rpx;
	}

	.username {
		margin-top: 70rpx;
		margin-left: 20rpx;
	}

	.contentPicture {
		width: 100%;
		/* border: 1px solid red; */
		/* height: 900rpx; */
	}

	.picture {
		width: 100%;
		height: 100%;
	}

	.title {
		width: 700rpx;
		height: 150rpx;
		/* border: 1rpx solid red; */
		margin-left: 25rpx;
	}

	.text {
		width: 700rpx;
		/* border: 1rpx solid red; */
		margin-left: 25rpx;
	}

	.textcontent {
		font-size: 24rpx;
		font-weight: 200;
	}

	.comments {
		width: 750rpx;
		/* border: 1rpx solid red; */
		height: 700rpx;
	}

	.reply {
		width: 100%;
		height: 100rpx;
		display: flex;
		flex-direction: row;
		/* border: 1rpx solid red; */
		background-color: white;
		position: fixed;
		bottom: calc(var(--window-bottom));
		z-index: 1030;
		margin-bottom: 6;
	}

	.commentinput {
		border: black solid 1rpx;
		border-radius: 30rpx;
		background-color: #f0f0f0;
		width: 300rpx;
		text-indent: 10rpx;
		margin-top: 7rpx;
	}

	.concern {
		margin-top: 60rpx;
		height: 50rpx;
		font-size: 20rpx;
		border-radius: 20rpx;
		border-color: #ffa500;
	}

	.send-btn {
		width: 100rpx;
		height: 50rpx;
		font-size: 20rpx;
		font-family: PingFang SC;
		font-weight: 500;
		margin-left: 10rpx;
		margin-top: 8rpx;
	}

	.commentsContent {
		margin-left: 130rpx;
		margin-bottom: auto;
		font-size: 25rpx;
		width: 50%;
	}

	.commentsList {
		font-size: 25rpx;
		margin-bottom: 20rpx;
	}

	.end {
		margin-top: 300rpx;
		font-size: 30rpx;
	}

	.commentReply {
		display: flex;
		flex-direction: row;
	}

	.replyto {
		font-size: 20rpx;
		margin-left: 510rpx;
		margin-top: 10rpx;
		margin-bottom: 10rpx;
		position: absolute;
		bottom: 0;
	}

	.queryreply {
		font-size: 20rpx;
		margin-left: 130rpx;
	}

	.avatar {
		margin-left: 20rpx;
		margin-right: 10rpx;
		margin-top: 10rpx;
	}

	.like {
		width: 250rpx;
	}

	.like text {
		margin-left: 10rpx;
		margin-right: 10rpx;
	}

	.commentLike {
		margin-left: 130rpx;
	}

	.replyinput {
		margin-top: 150rpx;
	}
</style>