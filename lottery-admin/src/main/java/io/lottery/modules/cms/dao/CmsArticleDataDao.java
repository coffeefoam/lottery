package io.lottery.modules.cms.dao;

import java.util.List;

import io.lettuce.core.dynamic.annotation.Param;
import io.lottery.modules.cms.entity.CmsArticleDataEntity;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 文章详表
 * 
 * @author
 * @email
 * @date 2018-07-04 21:11:40
 */
public interface CmsArticleDataDao extends BaseMapper<CmsArticleDataEntity> {

	boolean deleteByArtIds(@Param("artIdList") List<String> artIdList);

}
