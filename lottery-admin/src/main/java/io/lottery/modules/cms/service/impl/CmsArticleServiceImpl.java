package io.lottery.modules.cms.service.impl;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;
import io.lottery.modules.cms.dao.CmsArticleDao;
import io.lottery.modules.cms.dao.CmsCategoryDao;
import io.lottery.modules.cms.entity.CmsArticleEntity;
import io.lottery.modules.cms.service.CmsArticleService;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service("cmsArticleService")
public class CmsArticleServiceImpl extends ServiceImpl<CmsArticleDao, CmsArticleEntity> implements CmsArticleService {

	@Autowired
	private CmsCategoryDao cmsCategoryDao;

	@Autowired
	private CmsArticleDao cmsArticleDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String categoryId = (String) params.get("categoryId");
		String title = (String) params.get("title");
		String ids = cmsCategoryDao.getCategoryChildList(categoryId);
		Page<CmsArticleEntity> page = this.selectPage(
				new Query<CmsArticleEntity>(params).getPage(),
				new EntityWrapper<CmsArticleEntity>().and(StringUtils.isNotBlank(categoryId), "category_id IN(" + ids + ")").and(StringUtils.isNotBlank(title),
						"title like concat('%',{0},'%')", title));
		return new PageUtils(page);
	}

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
	public boolean countByTitle(String title) {
		return cmsArticleDao.countByTitle(title);
	}

	@Override
	public Long countTotal() {
		return cmsArticleDao.countTotal();
	}

	@Override
	public List<String> getArtIdListByCid(String cid) {
		return cmsArticleDao.getArtIdListByCid(cid);
	}
}
