package com.uni.pj.service;
import com.baomidou.mybatisplus.extension.service.IService;
import com.uni.pj.common.ResponseResult;
import com.uni.pj.users.dtos.UserLoginDto;
import com.uni.pj.users.dtos.UserRegisterDto;
import com.uni.pj.users.pojo.Users;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author author
 * @since 2023-11-21
 */
public interface UsersService extends IService<Users> {

    ResponseResult login(UserLoginDto userLoginDto);

    ResponseResult register(UserRegisterDto userRegisterDto);

    /**
     * 获取当前登录用户信息
     * @return
     */
    ResponseResult getUserInfo();


    /**
     * 根据id查询用户信息
     * @param id
     * @return
     */
    ResponseResult getUserInfoById(Integer id);
}
