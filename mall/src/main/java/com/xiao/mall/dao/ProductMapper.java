package com.xiao.mall.dao;

import com.xiao.mall.pojo.Cart;
import com.xiao.mall.pojo.Product;
import com.xiao.mall.vo.CartProductVo;
import com.xiao.mall.vo.CartVo;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Repository
public interface ProductMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Product record);

    int insertSelective(Product record);

    Product selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Product record);

    int updateByPrimaryKey(Product record);

    /**
     * 通过category_id查询数据id
     * @param categoryIdSet
     * @return
     */
    List<Product> selectByCategoryIdSet(@Param("categoryIdSet") Set<Integer> categoryIdSet);

    List<Product> selectByProductIdSet(@Param("productIdSet") Set<Integer> productIdSet);

}