package com.xiao.mall.service.impl;

import com.xiao.mall.dao.CategoryMapper;
import com.xiao.mall.pojo.Category;
import com.xiao.mall.service.ICategoryService;
import com.xiao.mall.vo.CategoryVo;
import com.xiao.mall.vo.ResponseVo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.xiao.mall.consts.MallConst.ROOT_PARENT_ID;

/**
 * @author ：降蓝
 * @description：TODO
 * @date ：2021/2/11 10:18
 */
@Service
public class CategoryServiceImpl implements ICategoryService {
    @Autowired
    private CategoryMapper categoryMapper;


    /**
     * TODO 提问两种方法哪种好，为什么？
     * TODO Lambda中 this::category2CategoryVo是什么意思
     * @return
     */
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        List<Category> categories = categoryMapper.selectAll();

        //查出一级目录(parent_id=0)
//        for (Category category : categories) {
//            if (category.getParentId().equals(ROOT_PARENT_ID)) {
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category,categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }

//       查出一级目录  使用Lambda语法 + stream流式处理
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .collect(Collectors.toList());
        //查询子目录
        findSubCategory(categoryVoList,categories);
        return ResponseVo.success(categoryVoList);
    }

    private void findSubCategory(List<CategoryVo> categoryVoList,List<Category> categories) {
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for (Category category : categories) {
                //如果查到内容，设置subCategory，继续往下查
                if (categoryVo.getId().equals(category.getParentId())) {
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                categoryVo.setSubCategories(subCategoryVoList);
            }
        }
    }

    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;

    }
}
