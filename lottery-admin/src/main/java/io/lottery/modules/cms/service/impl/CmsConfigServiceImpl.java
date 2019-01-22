package io.lottery.modules.cms.service.impl;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;
import io.lottery.modules.cms.dao.CmsConfigDao;
import io.lottery.modules.cms.entity.CmsConfigEntity;
import io.lottery.modules.cms.service.CmsConfigService;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service("cmsConfigService")
public class CmsConfigServiceImpl extends ServiceImpl<CmsConfigDao, CmsConfigEntity> implements CmsConfigService {

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		Page<CmsConfigEntity> page = this.selectPage(new Query<CmsConfigEntity>(params).getPage(), new EntityWrapper<CmsConfigEntity>());

		return new PageUtils(page);
	}

}
