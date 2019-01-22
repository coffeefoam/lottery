package io.lottery.modules.lottery.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.lottery.common.utils.DateUtils;
import io.lottery.common.utils.SpringContextUtils;
import io.lottery.modules.lottery.entity.LiuheEntity;
import io.lottery.modules.lottery.service.LiuheService;
import io.lottery.modules.sys.entity.SysUserEntity;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * 蜘蛛处理类
 * 
 * @author R6
 *
 */
public class LiuheSpiderProcessorLiuhkb implements PageProcessor {

	private Logger logger = LoggerFactory.getLogger(LiuheSpiderProcessorLiuhkb.class);

	private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
	private Site site = Site.me().setDomain("www.6hkb.com").setSleepTime(2000).setUserAgent(userAgent);

	private static final String URL_LIST = "https://www.6hkb.com/marksix/history/";
	// private static final String URL_POST =
	// "http://www.kj5588.com/kj/\\\\d+\\\\.html";// 子页面

	private static final String URL = "https://www.6hkb.com";
	private LiuheService liuheService = SpringContextUtils.getBean(LiuheService.class);

	private SysUserEntity user;

	@Override
	public Site getSite() {
		return site;
	}

	@Override
	public void process(Page page) {
		try {
			// 列表页,如果是列表页则进行查找地址
			String urlstr = page.getUrl().get();
			if (!urlstr.endsWith("/") && !urlstr.endsWith(".html")) {
				urlstr += "/";
			}
			if (urlstr.equals(URL_LIST)) {
				Document doc = Jsoup.parse(page.getHtml().get());
				Elements es = doc.select("div.history-years").select("ul li a");

				Iterator<Element> it = es.iterator();
				List<String> urls = new ArrayList<String>();
				while (it.hasNext()) {
					Element e = it.next();
					String href = e.attr("href");
					if (StringUtils.isNotBlank(href)) {
						urls.add(URL + href);
					}
				}
				page.addTargetRequests(urls);
				// 文章页
			} else {

				// 获取jsoup对象
				Document doc = Jsoup.parse(page.getHtml().get());
				Elements trs = doc.select("table.history-table tbody tr");
				if (trs != null && trs.size() > 0) {
					// 遍历每一行数据
					Iterator<Element> itrs = trs.iterator();
					while (itrs.hasNext()) {
						Elements tds1 = itrs.next().select("td");
						Iterator<Element> tds = tds1.iterator();
						int i = 1;
						// 保存开奖数据
						LiuheEntity liuheEntity = new LiuheEntity();
						if (tds != null) {
							while (tds.hasNext()) {
								if (i > 3) {
									break;
								}
								Element td = tds.next();
								switch (i) {
								case 1:
									String str = td.text();
									String[] arr = str.split(" ");
									Date date = DateUtils.stringToDate(arr[0], "yyyy-MM-dd");
									String qishu = arr[1].substring(0, 3);
									liuheEntity.setTime(date);
									liuheEntity.setPeriods(qishu);
									break;
								case 2:
									Elements spans = td.select("span.ball-lg");
									Iterator<Element> is = spans.iterator();
									int i2 = 1;
									while (is.hasNext()) {
										Element iss = is.next();
										String zm = iss.text();
										switch (i2) {
										case 1:
											liuheEntity.setNum1(zm);
											break;
										case 2:
											liuheEntity.setNum2(zm);
											break;
										case 3:
											liuheEntity.setNum3(zm);
											break;
										case 4:
											liuheEntity.setNum4(zm);
											break;
										case 5:
											liuheEntity.setNum5(zm);
											break;
										case 6:
											liuheEntity.setNum6(zm);
											break;
										}
										i2++;
									}
									break;
								case 3:
									Elements span = td.select("span.ball-lg");
									String tema = span.get(0).text();
									liuheEntity.setNum7(tema);
									break;
								}
								i++;
							}
							// 先查询是否存在
							// 先查询数据是否存在
							LiuheEntity liuheEntity2 = liuheService.getByTimeAndQishu(liuheEntity);
							if (liuheEntity2 == null) {
								liuheEntity.setCreatetime(new Date());
								liuheService.insert(liuheEntity);
							}
							// 重新生成一遍开奖历史数据，生成生肖五行
							
							

						}
					}
				}
			}
		} catch (Exception e) {
			logger.error("执行页面解析出错:{}", e);
			return;
		}
	}

	public SysUserEntity getUser() {
		return user;
	}

	public void setUser(SysUserEntity user) {
		this.user = user;
	}

}
