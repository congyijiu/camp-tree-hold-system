package com.uni.pj.controller;


import com.uni.pj.common.ResponseResult;
import com.uni.pj.dtos.DynamicActionDto;
import com.uni.pj.service.UserDynamicActionsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user-dynamic-actions")
@Tag(name = "用户动态行为相关接口")
public class UserDynamicActionsController {

    @Autowired
    private UserDynamicActionsService userDynamicActionsService;

    @PostMapping("/Actions")
    @Operation(summary = "用户动态行为")
    public ResponseResult like(@RequestBody DynamicActionDto dynamicActionDto) {
        return userDynamicActionsService.actions(dynamicActionDto);
    }
}