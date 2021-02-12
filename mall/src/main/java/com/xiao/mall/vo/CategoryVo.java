package com.xiao.mall.vo;

import lombok.Data;

import java.util.List;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/11 19:07
 */
@Data
public class CategoryVo {

    private Integer id;

    private Integer parentId;

    private String name;

    private Integer sortOrder;

    private List<CategoryVo> subCategories;

}
