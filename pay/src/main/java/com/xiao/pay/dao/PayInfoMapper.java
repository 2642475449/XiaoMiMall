package com.xiao.pay.dao;

import com.xiao.pay.pojo.PayInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface PayInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PayInfo record);

    int insertSelective(PayInfo record);

    PayInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(PayInfo record);

    int updateByPrimaryKey(PayInfo record);

    PayInfo selectByOrderNo(Long orderNo);
}