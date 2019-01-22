package io.lottery.service.impl;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;
import io.lottery.dao.CmsArticleDataDao;
import io.lottery.entity.CmsArticleDataEntity;
import io.lottery.service.CmsArticleDataService;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service("cmsArticleDataService")
public class CmsArticleDataServiceImpl extends ServiceImpl<CmsArticleDataDao, CmsArticleDataEntity> implements CmsArticleDataService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<CmsArticleDataEntity> page = this.selectPage(new Query<CmsArticleDataEntity>(params).getPage(), new EntityWrapper<CmsArticleDataEntity>());

		return new PageUtils(page);
	}

	@Override
	public CmsArticleDataEntity getArticleData(Long id) {
		return this.selectOne(new EntityWrapper<CmsArticleDataEntity>().and(id != 0, "article_id ={0}", id));
	}

}
