package io.lottery.modules.lottery.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.R;
import io.lottery.modules.lottery.entity.LiuheEntity;
import io.lottery.modules.lottery.service.LiuheService;
import io.lottery.modules.lottery.service.impl.LiuheSpiderProcessorLiuhkb;
import us.codecraft.webmagic.Spider;

/**
 * 六合彩开奖数据
 *
 * @author
 * @email
 * @date 2018-09-08 08:46:46
 */
@RestController
@RequestMapping("lottery/liuhe")
public class LiuheController {
	@Autowired
	private LiuheService liuheService;

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("lottery:liuhe:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = liuheService.queryPage(params);

		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("lottery:liuhe:info")
	public R info(@PathVariable("id") Integer id) {
		LiuheEntity liuhe = liuheService.selectById(id);

		return R.ok().put("liuhe", liuhe);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("lottery:liuhe:save")
	public R save(@RequestBody LiuheEntity liuhe) {
		liuheService.insert(liuhe);
		liuheService.execute();
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("lottery:liuhe:update")
	public R update(@RequestBody LiuheEntity liuhe) {
		liuheService.updateById(liuhe);
		liuheService.execute();
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("lottery:liuhe:delete")
	public R delete(@RequestBody Integer[] ids) {
		liuheService.deleteBatchIds(Arrays.asList(ids));
		liuheService.execute();
		return R.ok();
	}

	/**
	 * 采集数据
	 */
	@RequestMapping("/caiji")
	@RequiresPermissions("lottery:liuhe:save")
	public R caiji(String url) {
		LiuheSpiderProcessorLiuhkb proccessor = new LiuheSpiderProcessorLiuhkb();
		// 开启多线程爬取内容
		Spider.create(proccessor).addUrl(url).thread(2).run();
		return R.ok();
	}

	/**
	 * 重新开奖历史数据
	 * 
	 * @desc
	 * @author xg
	 * @param url
	 * @return
	 * @author 2018年9月12日
	 */
	@RequestMapping("/genKjjl")
	@RequiresPermissions("lottery:liuhe:save")
	public R genKjjl(String url) {
		liuheService.genKjjl();
		return R.ok();
	}

}
