package com.uni.pj.controller;



import com.uni.pj.common.ResponseResult;
import com.uni.pj.service.UsersService;
import com.uni.pj.users.dtos.UserLoginDto;
import com.uni.pj.users.dtos.UserRegisterDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author congyijiu
 * @create 2023-11-21-11:34
 */
@RestController
@RequestMapping("/user")
@Tag(name = "用户相关接口")
@CrossOrigin(origins = "*", maxAge = 3600)
public class UserController {

    @Autowired
    private UsersService usersService;

    /**
     * 登录
     * @param userLoginDto
     * @return
     */
    @PostMapping("/login")
    @Operation(summary = "登录")
    public ResponseResult login(@RequestBody UserLoginDto userLoginDto) {
        return usersService.login(userLoginDto);
    }


    /**
     * 注册
     * @param userRegisterDto
     * @return
     */
    @PostMapping("/register")
    @Operation(summary = "注册")
    public ResponseResult register(@RequestBody UserRegisterDto userRegisterDto) {
        return usersService.register(userRegisterDto);
    }

    /**
     * 根据id查询用户信息
     */
    @GetMapping("/info/{id}")
    @Operation(summary = "根据id查询用户信息")
    public ResponseResult getUserInfoById(@PathVariable("id") Integer id) {
        return usersService.getUserInfoById(id);
    }

    /**
     *  查询当前登录用户信息
     */
    @GetMapping("/info")
    @Operation(summary = "查询当前登录用户信息")
    public ResponseResult getUserInfo() {
        return usersService.getUserInfo();
    }
}
