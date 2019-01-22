package io.lottery.service;

import io.lottery.entity.CmsArticleEntity;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;

/**
 * 文章表
 *
 * @author
 * @email
 * @date 2018-07-04 21:11:41
 */
public interface CmsArticleService extends IService<CmsArticleEntity> {

	List<CmsArticleEntity> findByCatId(Long id, int startNum, int num);

	int countByCatId(Long catId);

	List<CmsArticleEntity> findRemenList(Long categoryId, int num);

	List<CmsArticleEntity> findTuijianList(Long categoryId, int num);
	
	List<CmsArticleEntity> findArtListBySearchKey(Long categoryId,String searchStr,int startnum, int num);

	boolean count(Long id);

	Long selectCountById(Long id);
	
	CmsArticleEntity selectNext(Long id, Long catId);

	CmsArticleEntity selectPre(Long id, Long catId);

	List<Long> getAllArtIdList();
}
