package io.lottery.modules.cms.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import io.lottery.modules.cms.entity.CmsArticleEntity;

import com.baomidou.mybatisplus.mapper.BaseMapper;

/**
 * 文章表
 * 
 * @author
 * @email
 * @date 2018-07-04 21:11:41
 */
public interface CmsArticleDao extends BaseMapper<CmsArticleEntity> {

	List<CmsArticleEntity> findByCatId(@Param("cid") Long cid, @Param("startNum") int startNum, @Param("num") int num);

	List<CmsArticleEntity> findRemenList(@Param("categoryId") Long categoryId, @Param("num") int num);

	List<CmsArticleEntity> findTuijianList(@Param("categoryId") Long categoryId, @Param("num") int num);

	CmsArticleEntity selectNext(@Param("id") Long id, @Param("catId") Long catId);

	CmsArticleEntity selectPre(@Param("id") Long id, @Param("catId") Long catId);

	boolean count(@Param("id") Long id);

	Long selectCountById(@Param("id") Long id);

	boolean countByTitle(@Param("title") String title);

	Long countTotal();

	List<String> getArtIdListByCid(@Param("cid") String cid);
}
