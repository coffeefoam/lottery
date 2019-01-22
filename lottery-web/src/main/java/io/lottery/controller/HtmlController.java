package io.lottery.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.lottery.common.utils.FreeMarkerUtils;
import io.lottery.constant.CmsConst;
import io.lottery.entity.CmsCategoryEntity;
import io.lottery.service.CmsArticleService;
import io.lottery.service.CmsCategoryService;
import io.lottery.service.CmsService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * HTML生成
 * 
 * @author R6
 *
 */
@Controller
public class HtmlController {

	private Logger logger = LoggerFactory.getLogger(HtmlController.class);

	private String tpl = "E:/workspace/Java/lottery-security/lottery-web/src/main/resources/view";// 基本模板路径

	private String out = "D:/software/nginx-1.14.0/html";

	@Autowired
	private CmsService cmsService;

	@Autowired
	private CmsArticleService cmsArticleService;

	@Autowired
	private CmsCategoryService cmsCategoryService;

	/**
	 * 生成大菜单和彩种页面
	 * 
	 * @param pwd
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/gen")
	public String genHtml(String pwd) {
		if (!"123456".equals(pwd)) {
			return "invalid password";
		}

		// 將静态文件拷贝到发布目录
		Map<String, Object> map = new HashMap<String, Object>();
		// 生成首頁
		map.put("lottory_type", "index");
		String indexTpl = "/index.html";
		FreeMarkerUtils.createHTMLFile(tpl, indexTpl, out + indexTpl, map);
		map.clear();

		// 获取彩种
		Map<String, List<String>> cmap = builCzMap();
		// 循环key生成各彩种首页
		if (cmap != null && !cmap.isEmpty()) {
			Set<String> keySet = cmap.keySet();
			if (keySet != null && keySet.size() > 0) {
				for (String key : keySet) {
					// 获取生成首页的数据
					// 查询首页文章
					Map<String, Object> indexmap = cmsService.getArtList(key);
					map.putAll(indexmap);
					map.put("lottory_type", key);
					map.put("lottory_func", "");
					String tplName = "/" + key + "/index.html";
					FreeMarkerUtils.createHTMLFile(tpl, tplName, out + tplName, map);
					map.clear();

					// 生成各彩种的统计页面
					List<String> list = cmap.get(key);
					if (list != null && !list.isEmpty()) {
						for (String str : list) {
							// 生成静态分页
							if ("jiqiao".equals(str)) {
								// 查询栏目
								CmsCategoryEntity cat = cmsCategoryService.getByType(key);
								// 查詢总条数
								int total = cmsArticleService.countByCatId(cat.getId());
								// 统计总页数
								int pageNum = total / 6;
								if (total % 6 != 0) {
									pageNum += 1;
								}

								// 循环生成列表页
								for (int pageNo = 0; pageNo <= pageNum; pageNo++) {
									map.put("lottory_type", key);
									map.put("lottory_func", str);
									// 生成静态列表
									Map<String, Object> cmsmap = cmsService.buildList(key, pageNo, CmsConst.CATEGORY_SHOW_ALL_NO);
									map.putAll(cmsmap);
									String tplName3 = "/" + key + "/" + str + "/index.html";
									String outName = tplName3;
									if (pageNo == 0) {
										outName = "/" + key + "/" + str + "/index.html";
									} else {
										outName = "/" + key + "/" + str + "/" + pageNo + ".html";
									}
									try {
										FreeMarkerUtils.createHTMLFile(tpl, tplName3, out + outName, map);
										map.clear();
									} catch (Exception e) {
										logger.error("生成出错:{}", e);
										map.clear();
										continue;
									}
								}
							} else {
								map.put("lottory_type", key);
								map.put("lottory_func", str);
								String tplName2 = "/" + key + "/" + str + "/index.html";
								try {
									FreeMarkerUtils.createHTMLFile(tpl, tplName2, out + tplName2, map);
									map.clear();
								} catch (Exception e) {
									logger.error("生成出错:{}", e);
									map.clear();
									continue;
								}
							}
						}
					}
				}
			}
		}

		// 走势图
		List<String> zoushituList = buildZst();
		if (zoushituList != null && !zoushituList.isEmpty()) {
			for (String str : zoushituList) {
				map.put("lottory_type", "zoushitu");
				map.put("lottory_func", str);
				String tplName2 = "/zoushitu/" + str + "/index.html";
				FreeMarkerUtils.createHTMLFile(tpl, tplName2, out + tplName2, map);
				map.clear();
			}
		}
		// 开奖直播
		map.put("lottory_type", "zhibo");
		map.put("lottory_func", "");
		String zhibo = "/zhibo/index.html";
		FreeMarkerUtils.createHTMLFile(tpl, zhibo, out + zhibo, map);
		map.clear();

		// 推荐计划
		List<String> numberplanList = new ArrayList<String>();
		numberplanList.add("pk10plan");
		numberplanList.add("xyftplan");
		numberplanList.add("cqsscplan");
		if (numberplanList != null && !numberplanList.isEmpty()) {
			for (String str : numberplanList) {
				map.put("lottory_type", "numberplan");
				map.put("lottory_func", "");
				String numberplan = "/numberplan/" + str + "/index.html";
				FreeMarkerUtils.createHTMLFile(tpl, numberplan, out + numberplan, map);
				map.clear();
			}
		}

		// 长龙统计
		map.put("lottory_type", "tongji");
		map.put("lottory_func", "");
		String tongji = "/tongji/changlongtixing/index.html";
		FreeMarkerUtils.createHTMLFile(tpl, tongji, out + tongji, map);
		map.clear();

		// // app下载
		// map.put("lottory_type", "home");
		// map.put("lottory_func", "");
		// String download = "/home/download/index.html";
		// FreeMarkerUtils.createHTMLFile(tpl, download, out + download, map);
		// map.clear();

		return "gen ok";
	}

	/**
	 * 构建彩种数据
	 * 
	 * @return
	 */
	public Map<String, List<String>> builCzMap() {
		// 彩种游戏种类
		Map<String, List<String>> czmap = new LinkedHashMap<String, List<String>>();

		// pk10和子页面
		List<String> pk10 = new ArrayList<String>();
		czmap.put("pk10", pk10);
		pk10.add("kaijiang");// 开奖
		pk10.add("shipin");// 开奖直播
		pk10.add("yilou");// 号码遗漏
		pk10.add("zonghe");// 综合露珠分析
		pk10.add("shuangmianluzhu");// 单双大小露珠
		pk10.add("haomaluzhu");// 号码前后露珠
		pk10.add("longhuluzhu");// 龙虎露珠
		pk10.add("longhu");// 龙虎历史
		pk10.add("guanyaheluzhu");//
		pk10.add("guanyahe");
		pk10.add("guanyaheyilou");
		pk10.add("shuangmian");
		pk10.add("shuangmianzoushitu");
		pk10.add("touzhu");
		pk10.add("zoushitu");
		pk10.add("jiqiao");
		pk10.add("changlongday");

		// shishicai
		List<String> shishicai = new ArrayList<String>();
		czmap.put("shishicai", shishicai);
		shishicai.add("kaijiang");
		shishicai.add("shipin");
		shishicai.add("zonghe");
		shishicai.add("yilou");
		shishicai.add("haomaluzhu");
		shishicai.add("longhuluzhu");
		shishicai.add("shuangmianluzhu");
		shishicai.add("zongheluzhu");
		shishicai.add("shuangmian");
		shishicai.add("haomatongji");
		shishicai.add("touzhu");
		shishicai.add("zoushitu");
		shishicai.add("shuangmianzoushitu");
		shishicai.add("jiqiao");
		shishicai.add("changlongday");

		// gdkl10
		List<String> gdkl10 = new ArrayList<String>();
		czmap.put("gdkl10", gdkl10);
		gdkl10.add("kaijiang");
		gdkl10.add("shipin");
		gdkl10.add("zonghe");
		gdkl10.add("heshudanshuangluzhu");
		gdkl10.add("longhuluzhu");
		gdkl10.add("zongheluzhu");
		gdkl10.add("weishudaxiaoluzhu");
		gdkl10.add("shuangmianluzhu");
		gdkl10.add("zhongfabailuzhu");
		gdkl10.add("dongnanxibeiluzhu");
		gdkl10.add("haomaluzhu");
		gdkl10.add("haomatongji");
		gdkl10.add("shuangmian");
		gdkl10.add("zoushitu");
		gdkl10.add("shuangmianzoushitu");
		gdkl10.add("touzhu");
		gdkl10.add("jiqiao");
		gdkl10.add("changlongday");

		// xyft
		List<String> xyft = new ArrayList<String>();
		czmap.put("xyft", xyft);
		xyft.add("kaijiang");
		xyft.add("shipin");
		xyft.add("zonghe");
		xyft.add("shuangmianluzhu");
		xyft.add("haomaluzhu");
		xyft.add("longhuluzhu");
		xyft.add("longhu");
		xyft.add("guanyaheluzhu");
		xyft.add("guanyahe");
		xyft.add("shuangmian");
		xyft.add("shuangmianzoushitu");
		xyft.add("touzhu");
		xyft.add("zoushitu");
		xyft.add("jiqiao");
		xyft.add("changlongday");

		// xync
		List<String> xync = new ArrayList<String>();
		czmap.put("xync", xync);
		xync.add("kaijiang");
		xync.add("shipin");
		xync.add("zonghe");
		xync.add("heshudanshuangluzhu");
		xync.add("longhuluzhu");
		xync.add("zongheluzhu");
		xync.add("weishudaxiaoluzhu");
		xync.add("shuangmianluzhu");
		xync.add("zhongfabailuzhu");
		xync.add("dongnanxibeiluzhu");
		xync.add("haomaluzhu");
		xync.add("haomatongji");
		xync.add("shuangmian");
		xync.add("shuangmianzoushitu");
		xync.add("zoushitu");
		xync.add("touzhu");
		xync.add("jiqiao");

		// gd11x5
		List<String> gd11x5 = new ArrayList<String>();
		czmap.put("gd11x5", gd11x5);
		gd11x5.add("kaijiang");
		gd11x5.add("shipin");
		gd11x5.add("zonghe");
		gd11x5.add("yilou");
		gd11x5.add("haomaluzhu");
		gd11x5.add("longhuluzhu");
		gd11x5.add("shuangmianluzhu");
		gd11x5.add("zongheluzhu");
		gd11x5.add("shuangmian");
		gd11x5.add("haomatongji");
		gd11x5.add("touzhu");
		gd11x5.add("zoushitu");
		gd11x5.add("shuangmianzoushitu");
		gd11x5.add("jiqiao");

		// kl8
		List<String> kl8 = new ArrayList<String>();
		czmap.put("kl8", kl8);
		kl8.add("kaijiang");
		kl8.add("zongheluzhu");
		// kl8.add("oddevenluzhu");
		kl8.add("shangxialuzhu");
		kl8.add("shipin");
		kl8.add("jiqiao");

		// jsk3
		List<String> jsk3 = new ArrayList<String>();
		czmap.put("jsk3", jsk3);
		jsk3.add("kaijiang");
		jsk3.add("shipin");
		// jsk3.add("yilou");
		jsk3.add("haomaluzhu");
		jsk3.add("zongheluzhu");
		jsk3.add("haomatongji");
		jsk3.add("zoushitu");
		jsk3.add("jiqiao");

		// xjssc
		List<String> xjssc = new ArrayList<String>();
		czmap.put("xjssc", xjssc);
		xjssc.add("kaijiang");
		xjssc.add("shipin");
		xjssc.add("zonghe");
		xjssc.add("yilou");
		xjssc.add("haomaluzhu");
		xjssc.add("longhuluzhu");
		xjssc.add("shuangmianluzhu");
		xjssc.add("zongheluzhu");
		xjssc.add("shuangmian");
		xjssc.add("haomatongji");
		xjssc.add("touzhu");
		xjssc.add("zoushitu");
		xjssc.add("shuangmianzoushitu");
		xjssc.add("jiqiao");

		// tjssc
		List<String> tjssc = new ArrayList<String>();
		czmap.put("tjssc", tjssc);
		tjssc.add("kaijiang");
		tjssc.add("shipin");
		tjssc.add("zonghe");
		tjssc.add("yilou");
		tjssc.add("haomaluzhu");
		tjssc.add("longhuluzhu");
		tjssc.add("shuangmianluzhu");
		tjssc.add("zongheluzhu");
		tjssc.add("shuangmian");
		tjssc.add("haomatongji");
		tjssc.add("touzhu");
		tjssc.add("zoushitu");
		tjssc.add("shuangmianzoushitu");
		tjssc.add("jiqiao");

		// jx11x5
		List<String> jx11x5 = new ArrayList<String>();
		czmap.put("jx11x5", jx11x5);
		jx11x5.add("kaijiang");
		jx11x5.add("shipin");
		jx11x5.add("zonghe");
		jx11x5.add("yilou");
		jx11x5.add("haomaluzhu");
		jx11x5.add("longhuluzhu");
		jx11x5.add("shuangmianluzhu");
		jx11x5.add("zongheluzhu");
		jx11x5.add("shuangmian");
		jx11x5.add("haomatongji");
		jx11x5.add("touzhu");
		jx11x5.add("zoushitu");
		jx11x5.add("shuangmianzoushitu");
		jx11x5.add("jiqiao");

		return czmap;
	}

	/**
	 * 构建走势图数据
	 * 
	 * @return
	 */
	private List<String> buildZst() {
		List<String> zoushituList = new ArrayList<String>();
		zoushituList.add("pk10");
		// zoushituList.add("shishicai");
		// zoushituList.add("gdkl10");
		// zoushituList.add("xyft");
		// zoushituList.add("xync");
		// zoushituList.add("gd11x5");
		// zoushituList.add("jsk3");
		// zoushituList.add("xjssc");
		// zoushituList.add("tjssc");
		// zoushituList.add("jx11x5");
		return zoushituList;
	}

	/**
	 * 文章详情页
	 * 
	 * @param pwd
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/genJiqiao")
	public String genJiqiao(String pwd) {
		List<Long> idList = cmsArticleService.getAllArtIdList();
		if (idList != null && !idList.isEmpty()) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("lottory_type", "jiqiao");
			map.put("lottory_func", "");
			for (Long id : idList) {
				// 生成静态列表
				Map<String, Object> map2 = cmsService.article(id);
				map.putAll(map2);
				String tplName3 = "/jiqiao/article.html";
				String outName = "/jiqiao/" + id + ".html";

				FreeMarkerUtils.createHTMLFile(tpl, tplName3, out + outName, map);
				map.clear();
			}
		}

		return "genJiqiao ok";
	}

	/**
	 * 文章列表页
	 * 
	 * @param pwd
	 * @return
	 */
	@ResponseBody
	@RequestMapping("/genList")
	public String genList(String pwd) {
		// 生成资讯首页
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("lottory_type", "jiqiao");
		map.put("lottory_func", "");

		// 生成静态列表
		Map<String, Object> map2 = cmsService.index();
		map.putAll(map2);
		String tplName3 = "/jiqiao/index.html";
		FreeMarkerUtils.createHTMLFile(tpl, tplName3, out + tplName3, map);
		map.clear();

		// 生成文章列表页
		List<String> list = new ArrayList<String>();
		list.add("pk10");
		list.add("shishicai");
		list.add("gdkl10");
		list.add("xyft");
		list.add("xync");
		list.add("gd11x5");
		list.add("kl8");
		list.add("jsk3");
		list.add("xjssc");
		list.add("tjssc");
		list.add("jx11x5");

		if (list != null && !list.isEmpty()) {
			for (String str : list) {
				// 查询栏目
				CmsCategoryEntity cat = cmsCategoryService.getByType(str);
				// 查詢总条数
				int total = cmsArticleService.countByCatId(cat.getId());
				// 统计总页数
				int pageNum = total / 6;
				if (total % 6 != 0) {
					pageNum += 1;
				}

				// 循环生成列表页
				for (int pageNo = 0; pageNo <= pageNum; pageNo++) {
					map.put("lottory_type", "jiqiao");
					map.put("lottory_func", "");
					Map<String, Object> map3 = cmsService.buildList(str, pageNo, CmsConst.CATEGORY_SHOW_ALL_YES);
					map.putAll(map3);
					String tplname = "/jiqiao/list.html";
					String outName = "/jiqiao/" + str + "/index.html";
					if (pageNo != 0) {
						outName = "/jiqiao/" + str + "/" + pageNo + ".html";
					}
					FreeMarkerUtils.createHTMLFile(tpl, tplname, out + outName, map);
					map.clear();
				}

			}
		}

		return "genList ok";
	}

}
