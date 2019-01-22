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
public class LiuheSpiderProcessor implements PageProcessor {

	private Logger logger = LoggerFactory.getLogger(LiuheSpiderProcessor.class);

	private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36";
	private Site site = Site.me().setDomain("www.kj5588.com").setSleepTime(2000).setUserAgent(userAgent);

	private static final String URL_LIST = "http://www.kj5588.com/kj/";
	// private static final String URL_POST =
	// "http://www.kj5588.com/kj/\\\\d+\\\\.html";// 子页面

	private static final String URL = "http://www.kj5588.com";
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
				Elements es = doc.select("ul li a");

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
				Elements table = doc.select("table tr[class='infolist']");
				if (table != null && table.size() > 0) {
					Iterator<Element> itb = table.iterator();
					while (itb.hasNext()) {
						Elements tds = itb.next().select("td");
						if (tds != null) {
							Iterator<Element> itd = tds.iterator();
							int index = 1;
							// 保存开奖数据
							LiuheEntity liuheEntity = new LiuheEntity();
							while (itd.hasNext()) {
								Element e = itd.next();
								// 获取开奖日期
								if (index == 1) {// 获取日期
									String datestr = e.text();
									if (StringUtils.isNotBlank(datestr)) {
										liuheEntity.setTime(DateUtils.stringToDate(datestr, "yyyy-MM-dd"));
									}
								} else if (index == 2) {// 获取期数
									String qishu = e.text();
									if (StringUtils.isNotBlank(qishu)) {
										qishu = qishu.substring(0, 3);
										liuheEntity.setPeriods(qishu);
									}
								} else if (index == 3) {// 获取平码号码
									Elements divs = e.select("div.sortDown div.hm");
									if (divs != null && divs.size() > 0) {
										Iterator<Element> hms = divs.iterator();
										if (hms != null && divs.size() > 0) {
											int hmIndex = 1;
											while (hms.hasNext()) {
												Element hme = hms.next();
												String hm = hme.text();
												switch (hmIndex) {
												case 1: {
													liuheEntity.setNum1(hm);
												}
													break;
												case 2: {
													liuheEntity.setNum2(hm);
												}
													break;
												case 3: {
													liuheEntity.setNum3(hm);
												}
													break;
												case 4: {
													liuheEntity.setNum4(hm);
												}
													break;
												case 5: {
													liuheEntity.setNum5(hm);
												}
													break;
												case 6: {
													liuheEntity.setNum6(hm);
												}
													break;
												}
												hmIndex++;
											}
										}
									}
								} else if (index == 4) {// 获取特码
									Elements divs = e.select("div.hm");
									String tema = divs.get(0).text();
									liuheEntity.setNum7(tema);
								} else {
									break;
								}
								index++;
							}
							// 保存数据
							// 先查询数据是否存在
							LiuheEntity liuheEntity2 = liuheService.getByTimeAndQishu(liuheEntity);
							if (liuheEntity2 == null) {
								liuheEntity.setCreatetime(new Date());
								liuheService.insert(liuheEntity);
							}
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
