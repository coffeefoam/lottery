package io.lottery.service.impl;

import io.lottery.dao.CmsArticleDao;
import io.lottery.entity.CmsArticleEntity;
import io.lottery.service.CmsArticleService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service("cmsArticleService")
public class CmsArticleServiceImpl extends ServiceImpl<CmsArticleDao, CmsArticleEntity> implements CmsArticleService {

	@Autowired
	private CmsArticleDao cmsArticleDao;

	@Override
	public List<CmsArticleEntity> findByCatId(Long id, int startNum, int num) {
		return cmsArticleDao.findByCatId(id, startNum, num);
	}

	@Override
	public int countByCatId(Long catId) {
		return this.selectCount(new EntityWrapper<CmsArticleEntity>().and(catId != 0, "category_id ={0}", catId));
	}

	/**
	 * 查询热门列表
	 */
	@Override
	public List<CmsArticleEntity> findRemenList(Long categoryId, int num) {
		return cmsArticleDao.findRemenList(categoryId, num);
	}

	@Override
	public List<CmsArticleEntity> findTuijianList(Long categoryId, int num) {
		return cmsArticleDao.findTuijianList(categoryId, num);
	}

	@Override
	public CmsArticleEntity selectNext(Long id, Long catId) {
		return cmsArticleDao.selectNext(id, catId);
	}

	@Override
	public CmsArticleEntity selectPre(Long id, Long catId) {
		return cmsArticleDao.selectPre(id, catId);
	}

	@Override
	public boolean count(Long id) {
		return cmsArticleDao.count(id);
	}

	@Override
	public Long selectCountById(Long id) {
		return cmsArticleDao.selectCountById(id);
	}

	@Override
	public List<Long> getAllArtIdList() {
		return cmsArticleDao.getAllArtIdList();
	}

	@Override
	public List<CmsArticleEntity> findArtListBySearchKey(Long id, String title, int startnum, int num) {
		return cmsArticleDao.findArtListBySearchKey(id, title, startnum, num);
	}
}
