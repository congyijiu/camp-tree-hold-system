package com.uni.pj.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.uni.pj.common.ResponseResult;
import com.uni.pj.common.entity.AppHttpCodeEnum;
import com.uni.pj.dynamic.dtos.FavoritePageDto;
import com.uni.pj.dynamic.dtos.PageDto;
import com.uni.pj.dynamic.pojos.Dynamic;
import com.uni.pj.dynamic.vos.DynamicDetailVo;
import com.uni.pj.dynamic.vos.PageVo;
import com.uni.pj.mapper.DynamicMapper;
import com.uni.pj.service.DynamicService;
import com.uni.pj.utils.AppThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;


/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author author
 * @since 2023-11-21
 */
@Service
@Slf4j
public class DynamicServiceImpl extends ServiceImpl<DynamicMapper, Dynamic> implements DynamicService {




    /**
     * 分页查询
     * @param pageDto
     * @return
     */
    @Override
    public ResponseResult mypage(PageDto pageDto) {

        //判断参数是否为空
        if(pageDto == null){
            return ResponseResult.errorResult(400,"参数不能为空");
        }
        if(pageDto.getPageIndex() == null || pageDto.getPageSize() == null){
            return ResponseResult.errorResult(400,"分页参数不能为空");
        }
        //获取pageDto参数
        Integer pageIndex = pageDto.getPageIndex();
        Integer pageSize = pageDto.getPageSize();
        Integer type = pageDto.getType();
        String keyWord = pageDto.getKeyWord();

        //如果type==1，查询所有动态
        if(type == 1){
            type = null;
        }

        //创建分页对象
        Page<PageVo> page = new Page<>(pageIndex, pageSize);


        //执行分页查询
        IPage<PageVo> dynamicPage = baseMapper.selectMyPage(page,keyWord,type);

        return ResponseResult.okResult(dynamicPage);
    }

    @Override
    public ResponseResult detail(Long id) {
        Dynamic dynamic = this.getById(id);
        DynamicDetailVo dynamicDetailVo = new DynamicDetailVo();
        BeanUtils.copyProperties(dynamic,dynamicDetailVo);
        dynamicDetailVo.setImageUrlList(dynamic.getImageUrl());
        //todo:查询评论数，点赞数，收藏数

//        List<Dynamic> list = this.list();
//        for (Dynamic dynamic : list) {
//            String imageUrl = dynamic.getImageUrl();
//            String[] split = imageUrl.split(",");
//            String newImageUrl = "";
//            for (String s : split) {
//                int startIndex = s.lastIndexOf("/") + 1;
//                int endIndex = s.indexOf("!", startIndex);
//                String result = s.substring(startIndex, endIndex);
//                String newUrl = "https://sns-img-hw.xhscdn.com/"+result+"?imageView2/2/w/1080/format/webp";
//                newImageUrl += newUrl + ",";
//            }
//            if (newImageUrl.endsWith(",")) {
//                newImageUrl = newImageUrl.substring(0, newImageUrl.length() - 1);
//            }else {
//                newImageUrl = newImageUrl.substring(0, newImageUrl.length());
//            }
//            dynamic.setImageUrl(newImageUrl);
//            this.updateById(dynamic);
//        }




        return ResponseResult.okResult(dynamicDetailVo);
    }

    /**
     * 分页查询收藏
     *
     * @param pageDto
     * @return
     */
    @Override
    public ResponseResult favoritePage(FavoritePageDto pageDto) {
        //1.判断参数是否为空
        if (pageDto == null) {
            log.info("参数为空");
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_INVALID);
        }
        if (pageDto.getPageIndex() == null || pageDto.getPageSize() == null) {
            log.info("分页参数为空");
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        //2.获取分页参数
        Integer pageIndex = pageDto.getPageIndex();
        Integer pageSize = pageDto.getPageSize();
        Integer userId = pageDto.getUserId();
        if (userId == null) {
            log.info("用户id为空");
            return ResponseResult.errorResult(AppHttpCodeEnum.PARAM_REQUIRE);
        }
        log.info("pageIndex:{},pageSize:{},userId:{}", pageIndex, pageSize, userId);

        //3.创建分页对象
        Page<PageVo> page = new Page<>(pageIndex, pageSize);

        //4.获取当前用户id，查询当前用户是否点赞
        Long currentUserId = AppThreadLocalUtil.getAppUser();

        int i = Integer.parseInt(currentUserId.toString());

        baseMapper.selectfavoritePage(page, pageDto, i);
        return ResponseResult.okResult(page);
    }
}
