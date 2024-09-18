<template>
	<view class="container">
		<view class="centered-box">
			<view class="box">
				<view class="title animate__animated animate__slideInDown"><text>Campus Tree Hole</text></view>
				<view class="title2 animate__animated animate__flip"><text>Sharing happiness</text></view>
				<view class="title2 animate__animated animate__flip"><text>with classmates</text></view>

				<uni-forms-item name="username">
					<view class="uni-form-Account">
						<input class="uni-Account-input animate__animated animate__backInLeft" v-model="username" placeholder="请输入账号" />
					</view>
				</uni-forms-item>


				<uni-forms-item name="password">
					<view class="line"></view>
					<!-- <view class="line"></view> -->
					<view class="uni-form-password">
						<input class="uni-password-input animate__animated animate__backInRight" password="true" v-model="password" placeholder="请输入密码"
							maxlength="16" minlength />
					</view>
				</uni-forms-item>


				<view class="line"></view>


				<view class="uni-form-btn">
					<button class="btn animate__animated animate__backInLeft" type="primary" @click="submit">Sign in</button>
				</view>
				<view class="Toregister animate__animated animate__backInUp">
					<navigator url="/pages/user/register" hover-class="none">
						<text class="register ">Don't have a hole？</text>
					</navigator>
				</view>
			</view>
		</view>


	</view>
</template>

<style>
	.container {
		background-image: url("../../static/loginpic3.jpeg");
		background-size: 100% 100%;
		height: 100vh;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.title {
		font-size: 70rpx;
		font-weight: bold;
		font-style: italic;
		margin-bottom: 20rpx;
	}

	.title2 {
		font-size: 50rpx;
		/* font-weight: normal; */
		color: aliceblue;
		margin-bottom: 30rpx;
	}

	.centered-box {
		display: flex;
		justify-content: center;
		align-items: center;
		text-align: center;
		backdrop-filter: blur(15px);
		border-radius: 15px;
		box-shadow:
			0px 0px 0.9px rgba(0, 0, 0, 0.007),
			0px 0px 2.2px rgba(0, 0, 0, 0.021),
			0px 0px 3.9px rgba(0, 0, 0, 0.037),
			0px 0px 6.4px rgba(0, 0, 0, 0.056),
			0px 0px 10.6px rgba(0, 0, 0, 0.075),
			0px 0px 18.5px rgba(0, 0, 0, 0.097),
			0px 0px 40px rgba(0, 0, 0, 0.12)
	}

	.register {
		font-size: 30rpx;
	}

	.box {
		/* 根据你的需求设置盒子的样式 */
		width: 580rpx;
		height: auto;
		/* border-radius: 10px; */
		/* text-align: center; */
		/* background-image:linear-gradient(120deg,#84fab0 0%, #8fd3f4 100%); */
		/* background-image:linear-gradient(180deg,#2af598 0%, #009efd 100%); */
		/* background-color: #fff; */
		padding: 20px;
		/* 根据需要调整内边距 */

	}



	.uni-Account-input,
	.uni-password-input {
		height: 60rpx;
		background-color: #fff;
		border-bottom: 10rpx;
		border-radius: 8px;

	}
	
	button {
		height: 60rpx;
		font-size: 30rpx;
		line-height: 60rpx;
		background-color: #fff;
		background-color: rgb(3, 145, 119);
		margin-bottom: 40rpx;
		border-radius: 8px;
	}


	.Toregister {
		color: #fff;
	}

	.btn {
		background-color: #00BFA6;
		/* padding: 14px 40px; */
		color: #fff;
		text-transform: uppercase;
		letter-spacing: 2px;
		cursor: pointer;
		border-radius: 10px;
		border: 2px dashed #00BFA6;
		box-shadow: rgba(50, 50, 93, 0.25) 0px 2px 5px -1px, rgba(0, 0, 0, 0.3) 0px 1px 3px -1px;
		transition: .4s;
	}

	.btn span:last-child {
		display: none;
	}

	.btn:hover {
		transition: .4s;
		border: 2px dashed #00BFA6;
		background-color: #fff;
		color: #00BFA6;
	}

	.btn:active {
		background-color: #87dbd0;
	}
</style>