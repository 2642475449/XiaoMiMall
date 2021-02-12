package com.xiao.mall.service;

import com.xiao.mall.vo.CategoryVo;
import com.xiao.mall.vo.ResponseVo;

import java.util.List;

public interface ICategoryService {
    /**
     * 查询所有类目
     */
    ResponseVo<List<CategoryVo>> selectAll();

}
