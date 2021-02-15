package com.xiao.mall.service;

import com.xiao.mall.vo.CategoryVo;
import com.xiao.mall.vo.ResponseVo;

import java.util.List;
import java.util.Set;

public interface ICategoryService {
    /**
     * 查询所有类目
     */
    ResponseVo<List<CategoryVo>> selectAll();

    /**
     * 查询子类
     * @param id
     * @param resultSet
     */
    void findSubCategoryId(Integer id, Set<Integer> resultSet);

}
