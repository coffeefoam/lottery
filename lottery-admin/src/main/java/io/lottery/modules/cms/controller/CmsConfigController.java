package io.lottery.modules.cms.controller;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.R;
import io.lottery.modules.cms.entity.CmsConfigEntity;
import io.lottery.modules.cms.service.CmsConfigService;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * cms内容系统配置信息
 *
 * @author
 * @email
 * @date 2018-07-14 14:36:34
 */
@RestController
@RequestMapping("cms/config")
public class CmsConfigController {
	@Autowired
	private CmsConfigService cmsConfigService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("cms:config:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = cmsConfigService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("cms:config:info")
	public R info(@PathVariable("id") Long id) {
		CmsConfigEntity cmsConfig = cmsConfigService.selectById(id);

		return R.ok().put("cmsConfig", cmsConfig);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("cms:config:save")
	public R save(@RequestBody CmsConfigEntity cmsConfig) {
		Date date = new Date();
		cmsConfig.setCreateDate(date);
		cmsConfig.setUpdateDate(date);
		cmsConfigService.insert(cmsConfig);

		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("cms:config:update")
	public R update(@RequestBody CmsConfigEntity cmsConfig) {

		cmsConfig.setUpdateDate(new Date());
		cmsConfigService.updateById(cmsConfig);

		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("cms:config:delete")
	public R delete(@RequestBody Long[] ids) {
		cmsConfigService.deleteBatchIds(Arrays.asList(ids));

		return R.ok();
	}

}
