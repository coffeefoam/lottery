package io.lottery.modules.cms.service.impl;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;
import io.lottery.modules.cms.dao.CmsCategoryDao;
import io.lottery.modules.cms.entity.CmsCategoryEntity;
import io.lottery.modules.cms.service.CmsCategoryService;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service("cmsCategoryService")
public class CmsCategoryServiceImpl extends ServiceImpl<CmsCategoryDao, CmsCategoryEntity> implements CmsCategoryService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {

		Page<CmsCategoryEntity> page = this.selectPage(new Query<CmsCategoryEntity>(params).getPage(), new EntityWrapper<CmsCategoryEntity>());

		return new PageUtils(page);
	}

	@Override
	public List<CmsCategoryEntity> findFrontList() {
		return this.selectList(new EntityWrapper<CmsCategoryEntity>().and("parent_id != {0}", 0).orderBy("sort"));
	}

}
