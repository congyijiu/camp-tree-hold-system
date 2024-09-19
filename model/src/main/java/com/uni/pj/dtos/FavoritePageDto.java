package com.uni.pj.dtos;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(name = "分页查询收藏参数")
@Data
public class FavoritePageDto {
    /**
     * 页码
     */
    Integer pageIndex;

    /**
     * 每页条数
     */
    Integer pageSize;

    /**
     * 用户id
     */
    @Schema(description = "当查询某个用户的收藏时，需要传入用户id")
    Integer userId;
}