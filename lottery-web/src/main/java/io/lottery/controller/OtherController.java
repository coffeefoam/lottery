package io.lottery.controller;

import io.lottery.common.config.RedisService;
import io.lottery.common.utils.DateUtils;
import io.lottery.common.utils.R;
import io.lottery.entity.CmsArticleEntity;
import io.lottery.service.CmsArticleService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author
 * @email
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/other")
public class OtherController {

	@Autowired
	private RedisService redisservice;

	@Autowired
	private CmsArticleService cmsArticleService;

	@RequestMapping("/feedback")
	public R list(String code) {
		if (StringUtils.isNotBlank(code)) {
			return R.ok(redisservice.get(code));
		} else {
			return R.error("接口编码不能为空");
		}
	}

	/**
	 * 获取文章的数据
	 * 
	 * @param id
	 * @return
	 */
	@RequestMapping("/cmsArtData")
	public R list(Long id) {
		// 统计点击数量
		cmsArticleService.count(id);
		Long hits = cmsArticleService.selectCountById(id);

		// 获取热门和推荐文章
		// 查询推荐列表
		CmsArticleEntity art = cmsArticleService.selectById(id);
		List<CmsArticleEntity> remenList = cmsArticleService.findRemenList(art.getCategoryId(), 10);
		List<CmsArticleEntity> tuijianList = cmsArticleService.findTuijianList(art.getCategoryId(), 10);

		// 减少传输字段
		List<Map<String, Object>> remenList2 = new ArrayList<Map<String, Object>>();
		if (remenList != null && remenList.size() > 0) {
			for (CmsArticleEntity art2 : remenList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", art2.getId());
				map.put("title", art2.getTitle());
				map.put("createDate", DateUtils.format(art2.getCreateDate(), "MM-dd"));
				remenList2.add(map);
			}
		}

		List<Map<String, Object>> tuijianList2 = new ArrayList<Map<String, Object>>();
		if (tuijianList != null && tuijianList.size() > 0) {
			for (CmsArticleEntity art2 : tuijianList) {
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("id", art2.getId());
				map.put("title", art2.getTitle());
				map.put("createDate", DateUtils.format(art2.getCreateDate(), "MM-dd"));
				tuijianList2.add(map);
			}
		}

		R r = R.ok();
		r.put("hits", hits);
		r.put("remenList", remenList2);
		r.put("tuijianList", tuijianList2);
		return r;
	}
}
