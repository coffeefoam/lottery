package io.lottery.modules.sys.service.impl;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;
import io.lottery.modules.sys.dao.SysDictDao;
import io.lottery.modules.sys.entity.SysDictEntity;
import io.lottery.modules.sys.service.SysDictService;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;

@Service("sysDictService")
public class SysDictServiceImpl extends ServiceImpl<SysDictDao, SysDictEntity> implements SysDictService {

	@Autowired
	private SysDictDao sysDictDao;

	@Override
	public PageUtils queryPage(Map<String, Object> params) {
		String name = (String) params.get("name");

		Page<SysDictEntity> page = this.selectPage(new Query<SysDictEntity>(params).getPage(), new EntityWrapper<SysDictEntity>().like(StringUtils.isNotBlank(name), "name", name));

		return new PageUtils(page);
	}

	@Override
	public List<SysDictEntity> findList(String type) {
		return sysDictDao.findList(type);
	}

}
