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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
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
     * 主功能
     * @return
     */
    @Override
    public ResponseVo<List<CategoryVo>> selectAll() {
        //查出所有数据
        List<Category> categories = categoryMapper.selectAll();

        //查出一级目录(parent_id=0)
//        for (Category category : categories) {
//            if (category.getParentId().equals(ROOT_PARENT_ID)) {
//                CategoryVo categoryVo = new CategoryVo();
//                BeanUtils.copyProperties(category,categoryVo);
//                categoryVoList.add(categoryVo);
//            }
//        }

        /**
         *  查出一级目录  使用Lambda语法 + stream流式处理
         *  (findSubCategory)查询二级目录三级目录等
         */
        List<CategoryVo> categoryVoList = categories.stream()
                .filter(e -> e.getParentId().equals(ROOT_PARENT_ID))
                .map(this::category2CategoryVo)
                .sorted(Comparator.comparing(CategoryVo::getParentId).reversed())
                .collect(Collectors.toList());
        findSubCategory(categoryVoList,categories);
        return ResponseVo.success(categoryVoList);
    }

    /**
     * 使用递归
     * @param id
     * @param resultSet 结果集合
     */
    @Override
    public void findSubCategoryId(Integer id, Set<Integer> resultSet) {
        List<Category> categories = categoryMapper.selectAll();
        //避免数据库的过多查询
        findSubCategoryId(id,resultSet,categories);
    }

    /**
     * 查询子目录
     * @param id
     * @param resultSet
     * @param categories
     */
    private void findSubCategoryId(Integer id, Set<Integer> resultSet, List<Category> categories) {
        for (Category category : categories) {
            if (category.getParentId().equals(id)) {
                resultSet.add(category.getId());
                findSubCategoryId(category.getId(),resultSet,categories);
            }
        }
    }




    /**
     *  使用递归直接调用自身的一种方法
     * @param categoryVoList
     * @param categories
     */
    private void findSubCategory(List<CategoryVo> categoryVoList,List<Category> categories) {
        for (CategoryVo categoryVo : categoryVoList) {
            List<CategoryVo> subCategoryVoList = new ArrayList<>();
            for (Category category : categories) {
                //如果查到内容，设置subCategory，继续往下查
                if (categoryVo.getId().equals(category.getParentId())) {
                    CategoryVo subCategoryVo = category2CategoryVo(category);
                    subCategoryVoList.add(subCategoryVo);
                }
                subCategoryVoList.sort(Comparator.comparing(CategoryVo::getParentId).reversed());
                categoryVo.setSubCategories(subCategoryVoList);
                //递归，查询三级目录
                findSubCategory(subCategoryVoList,categories);
            }
        }
    }

    /**
     *checking bean property types, copying bean properties, etc.
     * @param category class to instantiate
     * @return the new instance
     */
    private CategoryVo category2CategoryVo(Category category) {
        CategoryVo categoryVo = new CategoryVo();
        BeanUtils.copyProperties(category,categoryVo);
        return categoryVo;
    }
}
