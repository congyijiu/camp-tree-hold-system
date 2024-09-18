//导入封装好了的接口
import { apiResquest } from '@/utils/https.js'

 //登录接口
export const login = (query) => {
	return apiResquest({
		url: '/user/login',
		method: 'POST',
		header:{'content-type':'application/x-www-form-urlencoded'},
		data: {
			username:query.username,
			password:query.password
		},
	})
}

