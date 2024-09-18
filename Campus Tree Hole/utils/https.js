import {
	config
} from '@/config.js'
 
export const apiResquest = (prams) => { //prams 为我们需要调用的接口API的参数 下面会贴具体代码
 
	// 判断请求类型
	let headerData = {
		'content-type': 'application/json'
	}
 
	let dataObj = null
        //因为我们的GET和POST请求结构不同这里我们做处理，大家根据自己后台接口所需结构灵活做调整吧
	if (prams.method === "GET") {
		headerData = {
			'token': uni.getStorageSync('token')
		}
	} else {
		dataObj = prams.data
		headerData ={
			'content-type':'application/json',
			'token': uni.getStorageSync('token')
		}
	}
	return new Promise((resolve, reject) => {
		let url = config.base_url + prams.url; //请求的网络地址和局地的api地址组合
		return uni.request({
			url: url,
			data: dataObj,
			method: prams.method,
			sslVerify:false,
			header: headerData,
			success: (res) => {
				uni.hideLoading()//这里是成功的返回码，大家根据自己的实际情况调整
				if(res.data.code == '52'|| res.data.code == '51'){
					uni.reLaunch({
						url: "/pages/user/login" 
					})
				}
				if (res.data.code != '200') {
					console.log(res)
					uni.showToast({
						title: res.data.errorMessage,
						duration: 1000,
						icon: "none"
					})
					return;
				}
				resolve(res.data);
				console.log(res.data)
			},
			fail: (err) => {
				reject(err);
				console.log(err)
				uni.hideLoading()
			},
			complete: () => {
				console.log('请求完成')
				uni.hideLoading()
			}
		});
	})
}