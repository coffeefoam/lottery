package io.lottery.modules.cms.controller;

import io.lottery.common.constant.CommonConst;
import io.lottery.common.service.RedisService;
import io.lottery.common.utils.R;
import io.lottery.modules.cms.entity.CmsCategoryEntity;
import io.lottery.modules.cms.entity.CmsConfigEntity;
import io.lottery.modules.cms.service.CmsArticleDataService;
import io.lottery.modules.cms.service.CmsArticleService;
import io.lottery.modules.cms.service.CmsCategoryService;
import io.lottery.modules.cms.service.CmsConfigService;
import io.lottery.modules.cms.service.impl.SpiderProcessor;
import io.lottery.modules.sys.entity.SysUserEntity;
import io.lottery.modules.sys.shiro.ShiroUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.HtmlUtils;

import com.baomidou.mybatisplus.mapper.EntityWrapper;

import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

/**
 * 栏目表
 *
 * @author
 * @email
 * @date 2018-07-04 21:11:40
 */
@RestController
@RequestMapping("/cms/category")
public class CmsCategoryController {

	private Logger logger = LoggerFactory.getLogger(CmsCategoryController.class);

	@Autowired
	private CmsCategoryService cmsCategoryService;

	@Autowired
	private CmsConfigService cmsConfigService;

	@Autowired
	private CmsArticleService cmsArticleService;

	@Autowired
	private CmsArticleDataService cmsArticleDataService;

	@Autowired
	private RedisService redisService;

	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	/**
	 * 保存统计进度信息
	 */
	public static Map<String, Object> conutSpiderMap = new ConcurrentHashMap<String, Object>();

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("cms:category:list")
	public List<CmsCategoryEntity> list(@RequestParam Map<String, Object> params) {
		List<CmsCategoryEntity> list = cmsCategoryService.selectList(new EntityWrapper<CmsCategoryEntity>().orderBy("sort"));
		return list;
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("cms:category:info")
	public R info(@PathVariable("id") String id) {
		CmsCategoryEntity cmsCategory = cmsCategoryService.selectById(id);
		// 查询父级名称
		if (cmsCategory.getId() != 0) {
			CmsCategoryEntity p = cmsCategoryService.selectById(cmsCategory.getParentId());
			if (p != null) {
				cmsCategory.setParentName(p.getName());
			}
		} else {
			cmsCategory.setParentName("顶级栏目");
		}

		return R.ok().put("cmsCategory", cmsCategory);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("cms:category:save")
	public R save(@RequestBody CmsCategoryEntity cmsCategory) {
		cmsCategoryService.insert(cmsCategory);

		// 更新redis栏目信息缓存
		Set<String> set = redisService.getHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_CAT);
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, set);
		Set<String> set2 = redisService.getHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_CAtLIST_INDEX_SHOW_ALL_YES);
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, set2);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("cms:category:update")
	public R update(@RequestBody CmsCategoryEntity cmsCategory) {
		cmsCategoryService.updateById(cmsCategory);
		// 更新redis栏目信息缓存
		Set<String> set = redisService.getHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_CAT);
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, set);
		Set<String> set2 = redisService.getHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_CAtLIST_INDEX_SHOW_ALL_YES);
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, set2);
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("cms:category:delete")
	public R delete(@RequestBody String ids) {
		// 先删除对应的文章数据
		List<String> idList = Arrays.asList(ids);
		if (idList != null && idList.size() > 0) {
			for (String id : idList) {
				List<String> artIdList = cmsArticleService.getArtIdListByCid(id);
				String idStr = "";
				if (artIdList != null && artIdList.size() > 0) {
					for (String str : artIdList) {
						idStr += "," + str;
					}
					if (StringUtils.isNotBlank(idStr) && idStr.contains(",")) {
						idStr = idStr.substring(1);
					}
					// 批量删除文章数据
					cmsArticleService.deleteBatchIds(artIdList);
					cmsArticleDataService.deleteByArtIds(artIdList);
				}
			}
		}
		cmsCategoryService.deleteBatchIds(idList);
		// 更新缓存
		redisService.delete(CommonConst.REDIS_CMS_DATA);
		return R.ok();
	}

	/**
	 * 爬取网站内容
	 */
	@RequestMapping("/spiderContent")
	@RequiresPermissions("cms:article:update")
	public R spiderContent(Long catId, String url, Model model, HttpServletRequest request) {

		if (StringUtils.isBlank(url)) {
			return R.error("url地址不能为空");
		}

		try {
			url = URLDecoder.decode(url, "UTF-8");
			url = HtmlUtils.htmlUnescape(url);
		} catch (UnsupportedEncodingException e) {
			logger.error("解码出现异常:{}", e);
		}

		// 解析url链接地址
		if (!url.contains("pageIndex")) {
			url += "&pageIndex=1";
		}
		// 设置栏目id
		final SpiderProcessor proccessor = new SpiderProcessor();
		proccessor.setCategoryId(catId);

		SysUserEntity user = ShiroUtils.getUserEntity();
		proccessor.setUser(user);

		final CmsConfigEntity conf = cmsConfigService.selectOne(null);

		// 统计采集进度
		conutSpiderMap.put(user.getUserId() + "_" + proccessor.getCategoryId(), "准备采集页面内容");

		final String url2 = url;
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				Spider spider = Spider.create(proccessor);
				HttpClientDownloader httpClientDownloader = new HttpClientDownloader();

				if (conf.getProxyEnable()) {
					// 设置代理
					Proxy proxy = null;
					// 如果有账号密码，则设置账号密码
					if (StringUtils.isNotBlank(conf.getProxyUsername()) && StringUtils.isNotBlank(conf.getProxyPassword())) {
						proxy = new Proxy(conf.getProxyHost(), conf.getProxyPort(), conf.getProxyUsername(), conf.getProxyPassword());
					} else {
						proxy = new Proxy(conf.getProxyHost(), conf.getProxyPort());
					}
					httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(proxy));
				}

				spider.setDownloader(httpClientDownloader);
				// 开启多线程爬取内容
				spider.addUrl(url2).thread(5).run();

				// 执行完成后，在进行更新状态
				conutSpiderMap.put(user.getUserId() + "_" + proccessor.getCategoryId(), "采集任务已完成");
				// 更新缓存
				redisService.delete(CommonConst.REDIS_CMS_DATA);
			}
		};
		// 提交到线程池中，异步执行
		cachedThreadPool.submit(runner);

		return R.ok("正在执行采集任务，请稍后查看状态");
	}

	/**
	 * 获取批量执行任务状态
	 */
	@RequestMapping("/getSpiderStatus")
	@RequiresPermissions("cms:article:update")
	public R getSpiderStatus() {
		SysUserEntity user = ShiroUtils.getUserEntity();
		Set<String> set = conutSpiderMap.keySet();
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		if (set != null && set.size() > 0) {
			for (String key : set) {
				if (key.startsWith(user.getUserId().toString())) {
					String[] arr = key.split("_");
					if (arr != null && arr.length > 0) {
						Long catId = Long.valueOf(arr[1]);
						// 查询栏目信息
						CmsCategoryEntity cat = cmsCategoryService.selectById(catId);
						if (cat != null) {
							Map<String, Object> map = new HashMap<String, Object>();
							map.put("catId", cat.getId());
							map.put("catName", cat.getName());
							map.put("status", conutSpiderMap.get(key));
							list.add(map);
						}
					}
				}
			}
		}

		R r = R.ok();
		if (list != null && list.size() > 0) {
			r.put("tasks", list);
		}
		return r;
	}
}
