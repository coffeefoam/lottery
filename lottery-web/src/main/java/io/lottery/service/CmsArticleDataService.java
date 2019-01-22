package io.lottery.service;

import io.lottery.common.utils.PageUtils;
import io.lottery.entity.CmsArticleDataEntity;

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
}
