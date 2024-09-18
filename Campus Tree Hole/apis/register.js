import { apiResquest } from '@/utils/https.js'

//注册接口
export const register = (query) => {
	return apiResquest({
		url: '/user/register',
		method: 'POST',
		header:{'content-type':'application/x-www-form-urlencoded'},
		data: {
			username:query.username,
			password:query.newpassword,
			passwordAgain:query.confirmPwd
		},
	})
}
