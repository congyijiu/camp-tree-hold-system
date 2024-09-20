package com.uni.pj.controller;

import com.uni.pj.common.ResponseResult;
import com.uni.pj.dynamic.dtos.FavoritePageDto;
import com.uni.pj.dynamic.dtos.PageDto;
import com.uni.pj.service.DynamicService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author congyijiu
 * @create 2023-11-21-15:53
 */


@RestController
@RequestMapping("/dynamic")
@Tag(name = "动态相关接口")
@CrossOrigin(origins = "*", maxAge = 3600)
public class DynamicController {
    @Autowired
    private DynamicService dynamicService;

    /**
     * 分页查询
     * @param pageDto
     * @return
     */
    @PostMapping("/page")
    public ResponseResult page(@RequestBody PageDto pageDto){
        return dynamicService.mypage(pageDto);
    }

    /**
     * 查看动态详情
     * @param id
     * @return
     */
    @PostMapping("/detail/{dynamicId}")
    public ResponseResult detail(@PathVariable("dynamicId") Long id){
        return dynamicService.detail(id);
    }


    /**
     * 分页查询用户收藏的动态
     */
    @Operation(summary = "分页查询用户收藏的动态")
    @PostMapping("/favorite/page")
    public ResponseResult favoritePage(@RequestBody FavoritePageDto pageDto){
        return dynamicService.favoritePage(pageDto);
    }





}
