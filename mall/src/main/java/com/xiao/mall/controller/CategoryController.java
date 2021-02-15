package com.xiao.mall.controller;

import com.xiao.mall.enums.ResponseEnum;
import com.xiao.mall.service.ICategoryService;
import com.xiao.mall.vo.CategoryVo;
import com.xiao.mall.vo.ResponseVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/11 10:20
 */
@RestController
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;


    @GetMapping("/categories")
    public ResponseVo<List<CategoryVo>> selectAll() {
        return categoryService.selectAll();
    }

}
