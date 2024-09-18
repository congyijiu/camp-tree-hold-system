<script>
	import websocket from 'websocket.js'
	export default {
		onLaunch: function() {
			uni.onTabBarMidButtonTap(() => {
						uni.navigateTo({
							url: "/pages/publish/publish",
							animationType: "slide-in-bottom",
							animationDuration: 400
						})
					})		
		websocket.openSocket()
			console.log('App Launch')
		},
		onShow: function() {
			
			console.log('App Show')
		},
		onHide: function() {
			console.log('App Hide')
		}
	}
</script>

<style lang="scss">
	/*每个页面公共css */
	@import "uview-ui/index.scss";
</style>
