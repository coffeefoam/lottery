package io.lottery.modules.cms.service.impl;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;
import io.lottery.modules.cms.dao.CmsArticleDataDao;
import io.lottery.modules.cms.entity.CmsArticleDataEntity;
import io.lottery.modules.cms.service.CmsArticleDataService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service("cmsArticleDataService")
public class CmsArticleDataServiceImpl extends ServiceImpl<CmsArticleDataDao, CmsArticleDataEntity> implements CmsArticleDataService {

	@Autowired
	private CmsArticleDataDao cmsArticleDataDao;
	
	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<CmsArticleDataEntity> page = this.selectPage(new Query<CmsArticleDataEntity>(params).getPage(), new EntityWrapper<CmsArticleDataEntity>());

		return new PageUtils(page);
	}

	@Override
	public CmsArticleDataEntity getArticleData(Long id) {
		return this.selectOne(new EntityWrapper<CmsArticleDataEntity>().and(id != 0, "article_id ={0}", id));
	}

	@Override
	public void deleteByArtIds(List<String> artIdList) {
		cmsArticleDataDao.deleteByArtIds(artIdList);
	}

}
