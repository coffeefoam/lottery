package io.lottery.controller;

import io.lottery.constant.CmsConst;
import io.lottery.service.CmsService;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 资讯技巧
 * 
 * @author R6
 *
 */
@Controller
public class JiqiaoController {

	@Autowired
	private CmsService cmsService;

	/**
	 * 手机端--资讯技巧列表页
	 * 
	 * @param lottoryType
	 * @return
	 */
	@RequestMapping({ "/m/{catAlias}/jiqiao/{pageNo}.html", "/m/{catAlias}/jiqiao/{pageNo}" })
	public String list_m(@PathVariable String catAlias, @PathVariable Integer pageNo, Model model) {
		buildList(catAlias, pageNo, model, CmsConst.CATEGORY_SHOW_ALL_NO);
		model.addAttribute("lottory_type", catAlias);
		model.addAttribute("lottory_func", "jiqiao");
		return "/wap/" + catAlias + "/jiqiao/index";
	}

	/**
	 * 手机端技巧列表
	 * 
	 * @param catAlias
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/m/{catAlias}/jiqiao", "/m/{catAlias}/jiqiao/", "/m/{catAlias}/jiqiao/index.html" })
	public String list_m(@PathVariable String catAlias, Model model) {
		model.addAttribute("lottory_type", catAlias);
		model.addAttribute("lottory_func", "jiqiao");
		return list_m(catAlias, 1, model);
	}

	/**
	 * 手机端--资讯技巧列表页获取文章信息列表片段
	 * 
	 * @param lottoryType
	 * @return
	 */
	@RequestMapping("/m/jiqiaoPartial")
	public String list_m2(String catAlias, Integer pageNo, Model model) {
		buildList(catAlias, pageNo, model, CmsConst.CATEGORY_SHOW_ALL_NO);
		model.addAttribute("lottory_type", catAlias);
		model.addAttribute("lottory_func", "jiqiao");
		return "/wap/common/article_list_more";
	}

	/**
	 * 资讯技巧首页--手机端
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/m/jiqiao", "/m/jiqiao.html", "/m/jiqiao/", "/m/jiqiao/index.html" })
	public String index_m(Model model) {
		buildList("pk10", 1, model, CmsConst.CATEGORY_SHOW_ALL_NO);
		model.addAttribute("lottory_type", "pk10");
		return "/wap/jiqiao/index";
	}

	/**
	 * 列表2
	 * 
	 * @param catAlias
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/m/jiqiao/{catAlias}/", "/m/jiqiao/{catAlias}" })
	public String list2_m(@PathVariable String catAlias, Model model) {
		buildList(catAlias, 1, model, CmsConst.CATEGORY_SHOW_ALL_YES);
		model.addAttribute("lottory_type", catAlias);
		return "/wap/jiqiao/index";
	}

	/**
	 * 资讯技巧首页
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/jiqiao", "/jiqiao.html", "/jiqiao/", "/jiqiao/index.html" })
	public String index(Model model) {
		Map<String, Object> map = cmsService.index();
		model.addAllAttributes(map);
		return "/jiqiao/index";
	}

	@RequestMapping({ "{catAlias}/jiqiao", "{catAlias}/jiqiao/", "{catAlias}/jiqiao/index.html" })
	public String list(@PathVariable String catAlias, Model model) {
		model.addAttribute("lottory_type", catAlias);
		return list(catAlias, 1, model);
	}

	/**
	 * 资讯技巧列表页
	 * 
	 * @param lottoryType
	 * @return
	 */
	@RequestMapping({ "{catAlias}/jiqiao/{pageNo}.html", "{catAlias}/jiqiao/{pageNo}" })
	public String list(@PathVariable String catAlias, @PathVariable Integer pageNo, Model model) {
		buildList(catAlias, pageNo, model, CmsConst.CATEGORY_SHOW_ALL_NO);
		model.addAttribute("lottory_type", catAlias);
		return "/" + catAlias + "/jiqiao/index";
	}

	/**
	 * 列表2
	 * 
	 * @param catAlias
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/jiqiao/{catAlias}/", "/jiqiao/{catAlias}" })
	public String list2(@PathVariable String catAlias, Model model) {
		model.addAttribute("lottory_type", "jiqiao");
		return list2(catAlias, 1, model);
	}

	/**
	 * 列表2
	 * 
	 * @param catAlias
	 * @param pageNo
	 * @param model
	 * @return
	 */
	@RequestMapping({ "/jiqiao/{catAlias}/{pageNo}", "/jiqiao/{catAlias}/{pageNo}.html" })
	public String list2(@PathVariable String catAlias, @PathVariable Integer pageNo, Model model) {
		buildList(catAlias, pageNo, model, CmsConst.CATEGORY_SHOW_ALL_YES);
		model.addAttribute("lottory_type", "jiqiao");
		return "/jiqiao/list";
	}

	/**
	 * 查询文章列表
	 * 
	 * @param catAlias
	 * @param pageNo
	 * @param model
	 */
	private void buildList(String catAlias, Integer pageNo, Model model, boolean isShowAll) {
		Map<String, Object> map = cmsService.buildList(catAlias, pageNo, isShowAll);
		model.addAllAttributes(map);
	}

	/**
	 * 文章详情
	 * 
	 * @return
	 */
	@RequestMapping("/jiqiao/{id}.html")
	public String article(@PathVariable Long id, Model model) {
		Map<String, Object> map = cmsService.article(id);
		model.addAllAttributes(map);
		return "/jiqiao/article";
	}

	/**
	 * 手机端H5文章详情
	 * 
	 * @return
	 */
	@RequestMapping("/m/jiqiao/{id}.html")
	public String article_m(@PathVariable Long id, Model model) {
		Map<String, Object> map = cmsService.article(id);
		model.addAllAttributes(map);
		return "/wap/jiqiao/article";
	}
	
	/**
	 * APP端文章详情
	 * 
	 * @return
	 */
	@RequestMapping("/m/appjiqiao/{id}.html")
	public String appjiqiao(@PathVariable Long id, Model model) {
		Map<String, Object> map = cmsService.article(id);
		model.addAllAttributes(map);
		return "/wap/jiqiao/appArticle";
	}

}
