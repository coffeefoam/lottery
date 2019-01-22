package io.lottery.service.impl;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;
import io.lottery.dao.CmsCategoryDao;
import io.lottery.entity.CmsCategoryEntity;
import io.lottery.service.CmsCategoryService;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
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
	public List<CmsCategoryEntity> findFrontList(boolean isShowAll) {
		Wrapper<CmsCategoryEntity> wrapper = new EntityWrapper<CmsCategoryEntity>().ne("parent_id", 0).orderBy("sort");

		if (!isShowAll) {
			wrapper.eq("in_list", 1);
		}
		return this.selectList(wrapper);
	}

	@Override
	public CmsCategoryEntity getByType(String lottoryType) {
		return this.selectOne(new EntityWrapper<CmsCategoryEntity>().and("alias = {0}", lottoryType));
	}

}
