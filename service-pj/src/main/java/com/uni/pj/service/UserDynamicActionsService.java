package com.uni.pj.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.uni.pj.common.ResponseResult;
import com.uni.pj.dtos.DynamicActionDto;
import com.uni.pj.pojos.UserDynamicActions;

public interface UserDynamicActionsService extends IService<UserDynamicActions> {

    ResponseResult actions(DynamicActionDto dynamicActionDto);

    UserDynamicActions selectactions(Long id, Integer userId);
}
