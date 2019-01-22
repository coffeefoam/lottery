package io.lottery.controller;

import io.lottery.entity.CmsCategoryEntity;
import io.lottery.service.CmsService;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @ClassName: IndexController
 * @Description: 主页
 * @author 老王
 * @date 2018年7月17日 下午2:46:57
 *
 */
@Controller
@RequestMapping("/")
public class IndexController {

	@Autowired
	private CmsService cmsService;

	@RequestMapping({ "/m", "/m/index.html" })
	public String index_m(Model model) {
		return "wap/index";
	}

	/**
	 * @Description :自动匹配两级菜单
	 * @author 老王
	 * 
	 */
	@RequestMapping({ "/m/{lottory_type}", "/m/{lottory_type}.html", "/m/{lottory_type}/index.html" })
	public String sub_url_m(@PathVariable String lottory_type, Model model) {
		// 查询首页文章
		Map<String, Object> map = cmsService.getArtList(lottory_type);

		model.addAllAttributes(map);
		model.addAttribute("lottory_type", lottory_type);
		model.addAttribute("lottory_func", "");
		return "/wap/" + lottory_type + "/index";
	}

	/**
	 * @Description :手机端自动匹配三级菜单
	 * @author 老王
	 * 
	 */
	@RequestMapping({ "/m/{lottory_type}/{lottory_func}", "/m/{lottory_type}/{lottory_func}.html", "/m/{lottory_type}/{lottory_func}/index.html" })
	public String sub_url_m(@PathVariable String lottory_type, @PathVariable String lottory_func, Model model) {
		model.addAttribute("lottory_type", lottory_type);
		model.addAttribute("lottory_func", lottory_func);
		return "/wap/" + lottory_type + "/" + lottory_func + "/index";
	}

	@RequestMapping({ "/", "index.html" })
	public String index(Model model) {
		// 后期请优化
		Map<String, Object> map = cmsService.index();
		@SuppressWarnings("unchecked")
		List<CmsCategoryEntity> categoryList = (List<CmsCategoryEntity>) map.get("categoryList");
		if (categoryList != null && categoryList.size() > 2) {
			categoryList = categoryList.subList(0, 3);
		}
		map.put("categoryList", categoryList);
		model.addAllAttributes(map);
		model.addAttribute("lottory_type", "index");
		return "index";
	}

	/**
	 * @Description :自动匹配两级菜单
	 * @author 老王
	 * 
	 */
	@RequestMapping({ "/{lottory_type}", "/{lottory_type}.html", "/{lottory_type}/index.html" })
	public String sub_url(@PathVariable String lottory_type, Model model) {
		// 查询首页文章
		Map<String, Object> map = cmsService.getArtList(lottory_type);

		model.addAllAttributes(map);
		model.addAttribute("lottory_type", lottory_type);
		model.addAttribute("lottory_func", "");
		return lottory_type + "/index";

		// return lottory_type;
	}

	/**
	 * @Description :自动匹配三级菜单
	 * @author 老王
	 * 
	 */
	@RequestMapping({ "/{lottory_type}/{lottory_func}", "/{lottory_type}/{lottory_func}.html", "/{lottory_type}/{lottory_func}/index.html" })
	public String sub_url(@PathVariable String lottory_type, @PathVariable String lottory_func, Model model) {
		model.addAttribute("lottory_type", lottory_type);
		model.addAttribute("lottory_func", lottory_func);
		return lottory_type + "/" + lottory_func + "/index";
	}
	
	@RequestMapping("/robots.txt")
	public String robots() {
		return "robots.txt";
	}

}
