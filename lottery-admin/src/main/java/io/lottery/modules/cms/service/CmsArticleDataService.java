package io.lottery.modules.cms.service;

import io.lottery.common.utils.PageUtils;
import io.lottery.modules.cms.entity.CmsArticleDataEntity;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * 文章详表
 *
 * @author
 * @email
 * @date 2018-07-04 21:11:40
 */
public interface CmsArticleDataService extends IService<CmsArticleDataEntity> {

	PageUtils queryPage(Map<String, Object> params);

	CmsArticleDataEntity getArticleData(Long id);

	void deleteByArtIds(List<String> artIdList);
}
