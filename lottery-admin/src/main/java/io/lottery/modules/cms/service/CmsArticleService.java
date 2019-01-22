package io.lottery.modules.cms.service;

import io.lottery.common.utils.PageUtils;
import io.lottery.modules.cms.entity.CmsArticleEntity;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * 文章表
 *
 * @author
 * @email
 * @date 2018-07-04 21:11:41
 */
public interface CmsArticleService extends IService<CmsArticleEntity> {

	PageUtils queryPage(Map<String, Object> params);

	List<CmsArticleEntity> findByCatId(Long id, int startNum, int num);

	int countByCatId(Long catId);

	List<CmsArticleEntity> findRemenList(Long categoryId, int num);

	List<CmsArticleEntity> findTuijianList(Long categoryId, int num);

	CmsArticleEntity selectNext(Long id, Long catId);

	CmsArticleEntity selectPre(Long id, Long catId);

	boolean count(Long id);

	Long selectCountById(Long id);

	boolean countByTitle(String title);

	Long countTotal();

	List<String> getArtIdListByCid(String cid);
}
