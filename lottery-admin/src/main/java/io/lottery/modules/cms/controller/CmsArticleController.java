package io.lottery.modules.cms.controller;

import io.lottery.common.constant.CommonConst;
import io.lottery.common.service.RedisService;
import io.lottery.common.utils.ArithUtils;
import io.lottery.common.utils.FreeMarkerUtils;
import io.lottery.common.utils.Global;
import io.lottery.common.utils.HtmlUtils;
import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.R;
import io.lottery.modules.cms.entity.CmsArticleDataEntity;
import io.lottery.modules.cms.entity.CmsArticleEntity;
import io.lottery.modules.cms.entity.CmsCategoryEntity;
import io.lottery.modules.cms.entity.CmsConfigEntity;
import io.lottery.modules.cms.service.CmsArticleDataService;
import io.lottery.modules.cms.service.CmsArticleService;
import io.lottery.modules.cms.service.CmsCategoryService;
import io.lottery.modules.cms.service.CmsConfigService;
import io.lottery.modules.sys.entity.SysUserEntity;
import io.lottery.modules.sys.shiro.ShiroUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * 文章表
 *
 * @author
 * @email
 * @date 2018-07-04 21:11:41
 */

@RestController
@RequestMapping("cms/article")
public class CmsArticleController {

	private Logger logger = LoggerFactory.getLogger(CmsArticleController.class);

	@Autowired
	private CmsArticleService cmsArticleService;

	@Autowired
	private CmsCategoryService cmsCategoryService;

	@Autowired
	private CmsArticleDataService cmsArticleDataService;

	@Autowired
	private CmsConfigService cmsConfigService;

	@Autowired
	private RedisService redisService;

	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	/**
	 * 保存统计进度信息
	 */
	private static Map<String, Object> conutMap = new ConcurrentHashMap<String, Object>();

	/**
	 * 列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("cms:article:list")
	public R list(@RequestParam Map<String, Object> params) {
		PageUtils page = cmsArticleService.queryPage(params);
		// 遍历，设置栏目名称
		@SuppressWarnings("unchecked")
		List<CmsArticleEntity> list = (List<CmsArticleEntity>) page.getList();
		if (list != null && list.size() > 0) {
			for (CmsArticleEntity e : list) {
				CmsCategoryEntity cat = cmsCategoryService.selectById(e.getCategoryId());
				if (cat != null) {
					e.setCategoryName(cat.getName());
				}
			}
		}
		return R.ok().put("page", page);
	}

	/**
	 * 信息
	 */
	@RequestMapping("/info/{id}")
	@RequiresPermissions("cms:article:info")
	public R info(@PathVariable("id") String id) {
		CmsArticleEntity cmsArticle = cmsArticleService.selectById(id);

		// 查询文章栏目分类
		if (cmsArticle.getCategoryId() != null && cmsArticle.getCategoryId() != 0) {
			CmsCategoryEntity category = cmsCategoryService.selectById(cmsArticle.getCategoryId());
			if (category != null) {
				cmsArticle.setCategoryName(category.getName());
			}
		}
		// 查找文章数据
		CmsArticleDataEntity data = cmsArticleDataService.getArticleData(Long.valueOf(id));
		if (data != null) {
			cmsArticle.setArticleData(data);
		}

		return R.ok().put("cmsArticle", cmsArticle);
	}

	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("cms:article:save")
	public R save(@RequestBody CmsArticleEntity cmsArticle) {
		Date date = new Date();
		cmsArticle.setCreateDate(date);
		cmsArticle.setUpdateDate(date);
		// 保存文章基本信息
		cmsArticleService.insert(cmsArticle);
		// 保存文章数据
		if (cmsArticle.getArticleData() != null && StringUtils.isNotBlank(cmsArticle.getArticleData().getContent())) {
			CmsArticleDataEntity data = new CmsArticleDataEntity();
			data.setArticleId(cmsArticle.getId());
			try {
				String content = cmsArticle.getArticleData().getContent();
				content = URLDecoder.decode(cmsArticle.getArticleData().getContent(), "UTF-8");
				content = URLDecoder.decode(content, "UTF-8");

				data.setContent(content);
			} catch (UnsupportedEncodingException e) {
				logger.error("URLDecoder转码出错：{}", e);
			}
			data.setCopyfrom(cmsArticle.getArticleData().getCopyfrom());
			cmsArticleDataService.insert(data);
		}
		// 清空缓存，清空列表的数据
		Set<String> set2 = redisService.getHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARTLIST);
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, set2);
		return R.ok();
	}

	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("cms:article:update")
	public R update(@RequestBody CmsArticleEntity cmsArticle) {
		Date date = new Date();
		cmsArticle.setUpdateDate(date);
		cmsArticleService.updateById(cmsArticle);
		// 如果内容不为空，则进行内容更新
		// 更新文章内容
		if (cmsArticle.getArticleData() != null && StringUtils.isNotBlank(cmsArticle.getArticleData().getContent())) {
			CmsArticleDataEntity data = cmsArticleDataService.getArticleData(cmsArticle.getId());
			try {
				String content = cmsArticle.getArticleData().getContent();
				content = URLDecoder.decode(cmsArticle.getArticleData().getContent(), "UTF-8");
				content = URLDecoder.decode(content, "UTF-8");

				data.setContent(content);
			} catch (UnsupportedEncodingException e) {
				logger.error("URLDecoder转码出错：{}", e);
			}
			data.setCopyfrom(cmsArticle.getArticleData().getCopyfrom());
			cmsArticleDataService.updateById(data);
		}
		// 清空缓存，清空列表的数据
		Set<String> set2 = redisService.getHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARTLIST);
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, set2);
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARCTCLE + cmsArticle.getId());
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARTICLE_DATA + cmsArticle.getId());
		return R.ok();
	}

	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("cms:article:delete")
	public R delete(@RequestBody String[] ids) {
		List<String> list = Arrays.asList(ids);
		cmsArticleDataService.deleteByArtIds(list);// 删除文章数据
		cmsArticleService.deleteBatchIds(list);

		// 批量删除文章缓存数据
		Set<String> set2 = redisService.getHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARTLIST);
		redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, set2);
		for (String id : list) {
			redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARCTCLE + id);
			redisService.deleteHashKeys(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARTICLE_DATA + id);
		}

		return R.ok();
	}

	/**
	 * 批量生成静态HTML页面
	 */
	@RequestMapping("/batchHtml")
	@RequiresPermissions("cms:article:update")
	public R batchHtml(Model model, HttpServletRequest request) {

		SysUserEntity user = ShiroUtils.getUserEntity();
		Long currentNum = (Long) conutMap.get(user.getUserId() + "_batchHtmlCurrentNum");
		if (currentNum != null && currentNum.longValue() != 0) {
			return R.ok("有批量生成任务正在进行中，请稍后重试");
		}

		// 做成异步执行，提交到线程池
		Runnable runner = new Runnable() {
			@Override
			public void run() {
				// 总的文章条数
				Long artTotalNum = cmsArticleService.countTotal();
				conutMap.put(user.getUserId() + "_batchHtmlTotal", artTotalNum);
				conutMap.put(user.getUserId() + "_batchHtmlCurrentNum", 0L);
				Long currentNum = 0L;

				// 查找配置文件
				CmsConfigEntity config = cmsConfigService.selectOne(null);

				// 生成的文件路径
				String ctxPath = config.getGenerateDir();
				// 处理保存路径
				if (!ctxPath.startsWith("/")) {
					ctxPath = "/" + ctxPath;
				}

				// 生成文件路径
				String genDir = config.getOutHtmlPath();
				genDir += ctxPath;

				// 解析模板文件
				String classpath = "";
				try {
					classpath = ResourceUtils.getURL("classpath:").getPath();
				} catch (FileNotFoundException e) {
					logger.error("获取文件出错:{}", e);
				}
				if (StringUtils.isBlank(classpath)) {
					throw new RuntimeException("模板文件为空，不能继续生成静态文件");
				}
				String ftlPath = classpath + Global.getConfig("freemarker.ftlpath.front");

				try {
					// 生成首页
					Map<String, Object> indexMap = new HashMap<String, Object>();
					// 查询出所有的栏目
					List<CmsCategoryEntity> categoryList = cmsCategoryService.findFrontList();
					if (categoryList != null && categoryList.size() > 0) {
						for (CmsCategoryEntity c : categoryList) {
							// 每个栏目有6篇文章
							List<CmsArticleEntity> artList = cmsArticleService.findByCatId(c.getId(), 0, 6);
							// 查找文章内容
							if (artList != null && artList.size() > 0) {
								for (CmsArticleEntity art : artList) {
									CmsArticleDataEntity data = cmsArticleDataService.getArticleData(art.getId());
									if (data != null) {
										art.setArticleData(data);
									}
								}
								CmsArticleEntity art = artList.remove(0);
								c.setTuijianTitle(art.getTitle());// 推荐文章标题
								c.setTuijianArtId(art.getId());// 推荐文章id
								String content = art.getArticleData().getContent();
								content = HtmlUtils.html2Text(content);
								if (StringUtils.isNotBlank(content)) {
									if (content.length() > 30) {
										c.setTuijianContent(content.substring(0, 30));
									} else {
										c.setTuijianContent(content);
									}
								}
								if (StringUtils.isNotBlank(art.getDescription()) && art.getDescription().length() > 30) {
									c.setTuijianContent(art.getDescription().substring(1, 30));
								}
								c.setArtList(artList);
							}
						}
					}
					indexMap.put("categoryList", categoryList);
					indexMap.put("ctxPath", ctxPath);
					indexMap.put("config", config);
					String indexHtmlFtl = "/index.html";
					String indexOutHtml = genDir + "/jiqiao/index.html";
					FreeMarkerUtils.createHTMLFile(ftlPath, indexHtmlFtl, indexOutHtml, indexMap);

					// 生成列表页
					Map<String, Object> listMap = new HashMap<String, Object>();
					for (CmsCategoryEntity cat : categoryList) {
						Long catId = cat.getId();
						// 查詢该栏目文章总条数
						int total = cmsArticleService.countByCatId(catId);

						// 进行分页
						// 统计总页数
						int pageNum = total / 6;
						if (total % 6 != 0) {
							pageNum += 1;
						}
						// 进行分页生成
						for (int pageNo = 1; pageNo <= pageNum; pageNo++) {
							try {
								// 计算开始和结束条数
								int startNum = (pageNo - 1) * 6;
								// 该栏目下的文章
								List<CmsArticleEntity> artList = cmsArticleService.findByCatId(cat.getId(), startNum, 6);
								if (artList != null && artList.size() > 0) {
									for (CmsArticleEntity art : artList) {
										CmsArticleDataEntity data = cmsArticleDataService.getArticleData(art.getId());
										if (data != null) {
											art.setArticleData(data);
											// 截取文章内容
											String content = data.getContent();
											if (StringUtils.isNotBlank(content)) {
												content = HtmlUtils.html2Text(content);
												data.setContent(content);
											}
										}
									}
								}
								// 开始页码数
								int startPageNo = 0;
								// 结束页码数
								int endPageNo = 0;

								if (pageNum > 5) {
									// 开始页码
									if (pageNo == pageNum - 1) {
										startPageNo = pageNum - 4;
									} else if (pageNo == pageNum) {
										startPageNo = pageNum - 4;
									} else if (pageNo - 2 > 0) {
										startPageNo = pageNo - 2;
									} else {
										startPageNo = 1;
									}
									// 结束页码
									if (pageNo == 1 || pageNo == 2) {
										endPageNo = 5;
									} else if (pageNo + 2 <= pageNum) {
										endPageNo = pageNo + 2;
									} else {
										endPageNo = pageNum;
									}
								} else {
									startPageNo = 1;
									endPageNo = pageNum;
								}

								// 分页进行生成
								listMap.put("catId", catId);
								listMap.put("artList", artList);
								listMap.put("categoryList", categoryList);
								listMap.put("category", cat);

								// 分页条数
								listMap.put("pageNo", pageNo);
								listMap.put("pageNum", pageNum);
								listMap.put("startPageNo", startPageNo);
								listMap.put("endPageNo", endPageNo);
								listMap.put("ctxPath", ctxPath);

								String listHtmlFtl = "/list.html";
								String listOutHtml = genDir + "/jiqiao/list/" + catId + "-" + pageNo + ".html";
								FreeMarkerUtils.createHTMLFile(ftlPath, listHtmlFtl, listOutHtml, listMap);

								// 生成详情页
								if (artList != null && artList.size() > 0) {
									for (CmsArticleEntity art : artList) {
										Map<String, Object> artMap = new HashMap<String, Object>();
										// 查询文章数据内容
										CmsArticleDataEntity data = cmsArticleDataService.getArticleData(art.getId());
										if (data != null) {
											art.setArticleData(data);
											// 替换掉内容中的图片地址
											String content = data.getContent();

											String context = Global.getConfig("server.servlet.context-path");
											if (context.startsWith("/")) {
												context = context.substring(context.indexOf("/"));
											}
											if (StringUtils.isNotBlank(content) && content.contains(context)) {
												// 解析里面的img标签,替换
												Document doc = Jsoup.parse(content);
												Document body = Jsoup.parse(doc.body().outerHtml());
												Elements els = body.select("img");
												if (els != null && els.size() > 0) {
													Iterator<Element> it = els.iterator();
													while (it.hasNext()) {
														Element e = it.next();
														String src = e.attr("src");
														if (StringUtils.isNotBlank(src) && src.contains(context)) {
															src = src.replaceAll(context, "/" + config.getGenerateDir());
															e.attr("src", src);
														}
													}
												}
												data.setContent(body.html());
											}
										}

										// 查询热门、推荐列表
										List<CmsArticleEntity> remenList = cmsArticleService.findRemenList(art.getCategoryId(), 10);
										List<CmsArticleEntity> tuijianList = cmsArticleService.findTuijianList(art.getCategoryId(), 10);

										artMap.put("remenList", remenList);
										artMap.put("tuijianList", tuijianList);

										// 查找文章的下一篇
										CmsArticleEntity nextArt = cmsArticleService.selectNext(art.getId(), cat.getId());
										// 查找文章的上一篇
										CmsArticleEntity preArt = cmsArticleService.selectPre(art.getId(), cat.getId());
										artMap.put("art", art);
										artMap.put("nextArt", nextArt);
										artMap.put("preArt", preArt);
										artMap.put("category", cat);
										artMap.put("ctxPath", ctxPath);
										String artHtmlFtl = "/article.html";
										String artOutHtml = genDir + "/jiqiao/article/" + art.getId().toString() + ".html";
										FreeMarkerUtils.createHTMLFile(ftlPath, artHtmlFtl, artOutHtml, artMap);

										// 统计生成的进度
										currentNum++;
										conutMap.put(user.getUserId() + "_batchHtmlCurrentNum", currentNum);
									}
								}

							} catch (Exception e) {
								logger.error("分页生成出错:{}", e);
								continue;
							}
						}

					}
					// 生成静态文件后，将样式文件拷贝到指定目录
					String srcDir = classpath + "statics/assets";
					String destDir = genDir + "/statics";
					FileUtils.copyDirectoryToDirectory(new File(srcDir), new File(destDir));

					// 拷贝图片文件到指定目录
					String picDir = Global.getUserfilesBaseDir() + Global.USERFILES_BASE_URL;
					String descPicDir = genDir;
					FileUtils.copyDirectoryToDirectory(new File(picDir), new File(descPicDir));

				} catch (Exception e) {
					logger.error("生成报错:{}", e);
				} finally {
					// 该线程执行完了，直接跳出
					conutMap.put(user.getUserId() + "_batchHtmlCurrentNum", artTotalNum);
				}

			}
		};
		// 提交到线程池执行
		cachedThreadPool.submit(runner);

		return R.ok("已提交生成HTML文件，请稍后查询");
	}

	/**O
	 * 查询批量生成html状态
	 * 
	 * @return
	 */
	@RequestMapping("/getBatchHtmlStatus")
	public R getBatchHtmlStatus() {
		SysUserEntity user = ShiroUtils.getUserEntity();
		Long artTotalNum = (Long) conutMap.get(user.getUserId() + "_batchHtmlTotal");
		Long currentNum = (Long) conutMap.get(user.getUserId() + "_batchHtmlCurrentNum");

		R r = R.ok();
		if (currentNum != null && artTotalNum != null && currentNum != 0 && artTotalNum != 0) {
			r.put("status", ArithUtils.round(ArithUtils.div(currentNum, artTotalNum, 4) * 100, 2) + "%");
			r.put("total", artTotalNum);
			r.put("currentNum", currentNum);

			if (currentNum.longValue() != 0 && currentNum.longValue() == artTotalNum.longValue()) {
				conutMap.put(user.getUserId() + "_batchHtmlTotal", 0L);
				conutMap.put(user.getUserId() + "_batchHtmlCurrentNum", 0L);
			}
		}

		return r;
	}
}
