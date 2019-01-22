package io.lottery.modules.cms.service.impl;

import java.io.File;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.RandomUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lottery.common.utils.Global;
import io.lottery.common.utils.HttpUtils;
import io.lottery.common.utils.SpringContextUtils;
import io.lottery.modules.cms.controller.CmsCategoryController;
import io.lottery.modules.cms.entity.CmsArticleDataEntity;
import io.lottery.modules.cms.entity.CmsArticleEntity;
import io.lottery.modules.cms.service.CmsArticleDataService;
import io.lottery.modules.cms.service.CmsArticleService;
import io.lottery.modules.sys.entity.SysUserEntity;
import io.lottery.modules.sys.shiro.ShiroUtils;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 蜘蛛处理类
 * 
 * @author R6
 *
 */
public class SpiderProcessor implements PageProcessor {

	private Logger logger = LoggerFactory.getLogger(SpiderProcessor.class);

	private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
	private Site site = Site.me().setDomain("www.66icp.com").setSleepTime(6000).setUserAgent(userAgent);

	private static final String URL_LIST = "https://www\\.66icp\\.com/jiqiaolist\\?lotteryId=\\d+&pageIndex=\\d+";
	private static final String URL_POST = "https://www\\.66icp\\.com/jiqiao/\\d+\\.html";// 子页面

	private CmsArticleService cmsArticleService = SpringContextUtils.getBean(CmsArticleService.class);

	private CmsArticleDataService cmsArticleDataService = SpringContextUtils.getBean(CmsArticleDataService.class);

	/**
	 * 栏目编号
	 */
	private Long categoryId;

	private SysUserEntity user;

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		try {
			CmsCategoryController.conutSpiderMap.put(user.getUserId() + "_" + categoryId, "正在采集页面内容");
			// 列表页
			if (page.getUrl().regex(URL_LIST).match()) {
				List<String> list = page.getHtml().xpath("//a[@target=\"newWindow\"]").links().regex(URL_POST).all();
				page.addTargetRequests(list);
				page.addTargetRequests(page.getHtml().links().regex(URL_LIST).all()); // 是否当前页，如果注释掉就是当前页，如果开启就全部页
				// 文章页
			} else {
				// 获取jsoup对象
				Document doc = Jsoup.parse(page.getHtml().get());

				// 列表页获取
				// 标题
				String title = doc.getElementsByTag("title").text();
				title = title.substring(0, title.lastIndexOf("-")).trim();

				// 如果有重复的文章，自动跳过
				boolean b = cmsArticleService.countByTitle(title);
				if (b) {
					return;
				}

				Elements els = doc.getElementsByTag("meta");
				// 关键字
				String keywords = els.select("meta[name=keywords]").get(0).attr("content");
				// 描述
				String description = els.select("meta[name=description]").get(0).attr("content");

				Elements contentEls = doc.getElementsByClass("content").select("div");

				// 查找下载图片
				Elements imgEls = contentEls.select("img");
				if (imgEls != null && !imgEls.isEmpty()) {
					Iterator<Element> iterator = imgEls.iterator();
					while (iterator.hasNext()) {
						Element element = (Element) iterator.next();
						String src = element.attr("src");
						if (StringUtils.isNotBlank(src)) {
							Calendar now = Calendar.getInstance();
							int year = now.get(Calendar.YEAR);
							int month = now.get(Calendar.MONTH) + 1;
							int day = now.get(Calendar.DAY_OF_MONTH);

							SysUserEntity user = ShiroUtils.getUserEntity();

							String urlPath = Global.USERFILES_BASE_URL + user.getUserId() + "/" + year + "/" + month
									+ "/" + day;
							String realPath = Global.getUserfilesBaseDir() + urlPath;
							File dirPath = new File(realPath);
							if (!dirPath.exists()) {
								dirPath.mkdirs();
							}

							String originalFilename = src.substring(src.lastIndexOf("/"));

							// 用原文件名，便于区分
							String filename2 = "/" + originalFilename;
							// File file2 = new File(realPath + filename2);

							HttpUtils.executeDownloadFile(src, realPath + filename2);

							// FileUtils.copyInputStreamToFile(upfile.getInputStream(),
							// file2);

							String urlFilePath = Global.getConfig("server.servlet.context-path") + urlPath + filename2;
							// 更改实际的src地址
							element.attr("src", urlFilePath);

						}
					}

				}

				// 移除里面的标签
				Elements divLabels = contentEls.select("div.labels").remove();
				divLabels.select("span").remove();
				divLabels.select("a").remove();

				// 移除a标签的链接
				contentEls.select("a").removeAttr("href");

				// 文章内容
				String content = contentEls.html();

				// 保存爬取的内容
				// 文章基本信息
				CmsArticleEntity art = new CmsArticleEntity();
				art.setCategoryId(categoryId);

				// 获取文章的发布时间
				String createDateStr = doc.getElementsByClass("desc").select("span").get(0).text();
				Date date = null;
				if (StringUtils.isNotBlank(createDateStr)) {
					try {
						date = DateUtils.parseDate(createDateStr, "yyyy-MM-dd");
					} catch (ParseException e) {
						logger.error("时间格式化出错:{}", e);
					}
				}
				art.setCreateDate(date);
				art.setUpdateDate(date);
				art.setTitle(title);
				art.setDescription(description);// 文章描述
				art.setKeywords(keywords);

				// 产生随机点击数
				long hits = RandomUtils.nextInt(100, 1000);
				art.setHits(hits);
				cmsArticleService.insert(art);

				// 文章文本内容
				CmsArticleDataEntity dataArt = new CmsArticleDataEntity();
				dataArt.setContent(content);
				dataArt.setArticleId(art.getId());
				cmsArticleDataService.insert(dataArt);

			}
		} catch (Exception e) {
			logger.error("执行页面解析出错:{}", e);
			return;
		}
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	public SysUserEntity getUser() {
		return user;
	}

	public void setUser(SysUserEntity user) {
		this.user = user;
	}

}
