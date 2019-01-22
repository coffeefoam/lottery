package io.lottery.modules.cms.controller.front;

import io.lottery.common.utils.Global;
import io.lottery.common.utils.HtmlUtils;
import io.lottery.modules.cms.entity.CmsArticleDataEntity;
import io.lottery.modules.cms.entity.CmsArticleEntity;
import io.lottery.modules.cms.entity.CmsCategoryEntity;
import io.lottery.modules.cms.entity.CmsConfigEntity;
import io.lottery.modules.cms.service.CmsArticleDataService;
import io.lottery.modules.cms.service.CmsArticleService;
import io.lottery.modules.cms.service.CmsCategoryService;
import io.lottery.modules.cms.service.CmsConfigService;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jiqiao")
public class CmsFrontController {

	@Autowired
	private CmsArticleService cmsArticleService;

	@Autowired
	private CmsCategoryService cmsCategoryService;

	@Autowired
	private CmsArticleDataService cmsArticleDataService;

	@Autowired
	private CmsConfigService cmsConfigService;

	private String ctxPath;// 项目访问路径

	public CmsFrontController() {
		ctxPath = Global.getConfig("server.servlet.context-path");
	}

	/**
	 * 文章首頁
	 * 
	 * @param model
	 * @return
	 */
	@RequestMapping("/index.html")
	public String index(Model model) {
		// 查询配置文件
		CmsConfigEntity config = cmsConfigService.selectOne(null);

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
					c.setTuijianArtId(art.getId());
					c.setTuijianTitle(art.getTitle());

					String content = art.getArticleData().getContent();
					if (StringUtils.isNotBlank(content)) {
						// 清楚文章中的html标签
						content = HtmlUtils.html2Text(content);
						if (content.length() > 60) {
							c.setTuijianContent(content.substring(0, 60));
						} else {
							c.setTuijianContent(content);
						}
					}

					c.setArtList(artList);
				}
			}
		}
		model.addAttribute("config", config);
		model.addAttribute("ctxPath", ctxPath);
		model.addAttribute("categoryList", categoryList);
		return "modules/cms/front/index";
	}

	/**
	 * 文章列表
	 * 
	 * @param catId
	 * @param model
	 * @return
	 */
	@RequestMapping("/list/{catId}-{pageNo}.html")
	public String list(@PathVariable("catId") Long catId, @PathVariable("pageNo") Integer pageNo, Model model) {
		if (pageNo == null || pageNo == 0) {
			pageNo = 1;
		}

		// 查詢总条数
		int total = cmsArticleService.countByCatId(catId);
		// 统计总页数
		int pageNum = total / 6;
		if (total % 6 != 0) {
			pageNum += 1;
		}

		// 计算开始和结束条数
		int startNum = (pageNo - 1) * 6;
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

		// 查找栏目实体对象
		CmsCategoryEntity category = cmsCategoryService.selectById(catId);
		// 查询所有的栏目
		List<CmsCategoryEntity> categoryList = cmsCategoryService.findFrontList();
		// 该栏目下的文章
		List<CmsArticleEntity> artList = cmsArticleService.findByCatId(catId, startNum, 6);

		if (artList != null && artList.size() > 0) {
			for (CmsArticleEntity art : artList) {
				CmsArticleDataEntity data = cmsArticleDataService.getArticleData(art.getId());
				if (data != null) {
					String content = data.getContent();
					if (StringUtils.isNotBlank(content)) {
						content = HtmlUtils.html2Text(content);
						data.setContent(content);
					}
					art.setArticleData(data);
				}
			}
		}
		model.addAttribute("catId", catId);
		model.addAttribute("artList", artList);
		model.addAttribute("categoryList", categoryList);
		model.addAttribute("category", category);

		// 分页条数
		model.addAttribute("pageNo", pageNo);
		model.addAttribute("pageNum", pageNum);

		model.addAttribute("startPageNo", startPageNo);
		model.addAttribute("endPageNo", endPageNo);
		model.addAttribute("ctxPath", ctxPath);

		return "modules/cms/front/list";
	}

	/**
	 * 文章详情
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/article/{id}.html")
	public String article(@PathVariable("id") Long id, Model model) {
		CmsArticleEntity art = cmsArticleService.selectById(id);
		// 查找栏目实体
		CmsCategoryEntity category = cmsCategoryService.selectById(art.getCategoryId());
		if (art != null) {
			// 查询文章数据内容
			CmsArticleDataEntity data = cmsArticleDataService.getArticleData(art.getId());
			if (data != null) {
				art.setArticleData(data);
			}

			// 查询推荐列表
			List<CmsArticleEntity> remenList = cmsArticleService.findRemenList(art.getCategoryId(), 10);
			List<CmsArticleEntity> tuijianList = cmsArticleService.findTuijianList(art.getCategoryId(), 10);

			model.addAttribute("remenList", remenList);
			model.addAttribute("tuijianList", tuijianList);
		}

		// 查找文章的下一篇
		CmsArticleEntity nextArt = cmsArticleService.selectNext(id, category.getId());

		// 查找文章的上一篇
		CmsArticleEntity preArt = cmsArticleService.selectPre(id, category.getId());

		model.addAttribute("art", art);
		model.addAttribute("nextArt", nextArt);
		model.addAttribute("preArt", preArt);
		model.addAttribute("category", category);
		model.addAttribute("ctxPath", ctxPath);

		return "modules/cms/front/article";
	}

}
