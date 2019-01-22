package io.lottery.dao;

import io.lottery.entity.CmsCategoryEntity;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 栏目表
 * 
 * @author
 * @email
 * @date 2018-07-04 21:11:40
 */
public interface CmsCategoryDao extends BaseMapper<CmsCategoryEntity> {

	String getCategoryChildList(@Param("categoryId") String categoryId);

}
