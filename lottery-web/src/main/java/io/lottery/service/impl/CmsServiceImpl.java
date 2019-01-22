package io.lottery.service.impl;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.config.RedisService;
import io.lottery.common.constant.CommonConst;
import io.lottery.common.utils.DateUtils;
import io.lottery.common.utils.HtmlUtils;
import io.lottery.common.utils.RedisUtils;
import io.lottery.constant.CmsConst;
import io.lottery.entity.CmsArticleDataEntity;
import io.lottery.entity.CmsArticleEntity;
import io.lottery.entity.CmsCategoryEntity;
import io.lottery.service.CmsArticleDataService;
import io.lottery.service.CmsArticleService;
import io.lottery.service.CmsCategoryService;
import io.lottery.service.CmsService;
import io.lottery.util.LotteryConstant;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

@Service
public class CmsServiceImpl implements CmsService {

	@Autowired
	private CmsCategoryService cmsCategoryService;

	@Autowired
	private CmsArticleService cmsArticleService;

	@Autowired
	private CmsArticleDataService cmsArticleDataService;

	@Autowired
	private RedisUtils redisUtils;
	@Autowired
	private RedisService redisservice;

	/**
	 * 构造首页数据
	 */
	@Override
	public Map<String, Object> index() {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询出所有的栏目
		List<CmsCategoryEntity> categoryList = redisUtils.getHashList(CommonConst.REDIS_CMS_DATA,
				CommonConst.REDIS_CMS_CAtLIST_INDEX_SHOW_ALL_YES + "true", CmsCategoryEntity.class);
		if (categoryList == null) {
			categoryList = cmsCategoryService.findFrontList(CmsConst.CATEGORY_SHOW_ALL_YES);
			redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA,
					CommonConst.REDIS_CMS_CAtLIST_INDEX_SHOW_ALL_YES + "true", categoryList);
		}
		if (categoryList != null && categoryList.size() > 0) {
			for (CmsCategoryEntity c : categoryList) {
				// 每个栏目有6篇文章

				List<CmsArticleEntity> artList = redisUtils.getHashList(CommonConst.REDIS_CMS_DATA,
						CommonConst.REDIS_CMS_ARTLIST + c.getId() + "_startNum_" + 0, CmsArticleEntity.class);
				if (artList == null) {
					artList = cmsArticleService.findByCatId(c.getId(), 0, 6);
					redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA,
							CommonConst.REDIS_CMS_ARTLIST + c.getId() + "_startNum_" + 0, artList);
				}
				// 查找文章内容
				if (artList != null && artList.size() > 0) {
					CmsArticleEntity art = artList.remove(0);
					if (art != null) {
						CmsArticleDataEntity data = redisUtils.getHashObject(CommonConst.REDIS_CMS_DATA,
								CommonConst.REDIS_CMS_ARTICLE_DATA + art.getId(), CmsArticleDataEntity.class);
						if (data == null) {
							data = cmsArticleDataService.getArticleData(art.getId());
							redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA,
									CommonConst.REDIS_CMS_ARTICLE_DATA + art.getId(), data);
						}
						if (data != null) {
							art.setArticleData(data);
						}
					}
					c.setTuijianArtId(art.getId());
					c.setTuijianTitle(art.getTitle());
					if (art.getArticleData() != null) {
						String content = art.getArticleData().getContent();
						if (StringUtils.isNotBlank(content)) {
							// 清楚文章中的html标签
							content = HtmlUtils.html2Text(content);
							// 截取文章内容
							if (content.length() > 60) {
								content = content.substring(0, 60);
							}
							c.setTuijianContent(content);
						}
					}
					c.setArtList(artList);
				}
			}
		}

		map.put("categoryList", categoryList);
		map.put("lottory_type", "jiqiao");
		return map;
	}

	/**
	 * 获取文章列表页数据
	 */
	@Override
	public Map<String, Object> buildList(String catAlias, Integer pageNo, boolean isShowAll) {
		Map<String, Object> map = new HashMap<String, Object>();
		if (pageNo == null || pageNo == 0) {
			pageNo = 1;
		}
		// 查询栏目
		CmsCategoryEntity cat = redisUtils.getHashObject(CommonConst.REDIS_CMS_DATA,
				CommonConst.REDIS_CMS_CAT + catAlias, CmsCategoryEntity.class);
		if (cat == null) {
			cat = cmsCategoryService.getByType(catAlias);
			redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_CAT + catAlias, cat);
		}

		if (cat != null) {
			Long catId = cat.getId();
			// 通过栏目查询文章分页列表
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

			// 如果超过总页数，跳到第一页
			if (pageNo > pageNum) {
				pageNo = 1;
			}

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

			// 上一页和下一页
			int prePageNo = 0;
			int nextPageNo = 0;
			if (pageNo + 1 <= endPageNo) {
				nextPageNo = pageNo + 1;
			}
			if (pageNo - 1 >= startPageNo) {
				prePageNo = pageNo - 1;
			}

			// 查询所有的栏目
			List<CmsCategoryEntity> categoryList = redisUtils.getHashList(CommonConst.REDIS_CMS_DATA,
					CommonConst.REDIS_CMS_CAtLIST_INDEX_SHOW_ALL_YES + isShowAll, CmsCategoryEntity.class);
			if (categoryList == null) {
				categoryList = cmsCategoryService.findFrontList(isShowAll);
				redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA,
						CommonConst.REDIS_CMS_CAtLIST_INDEX_SHOW_ALL_YES + isShowAll, categoryList);
			}

			// 该栏目下的文章
			List<CmsArticleEntity> artList = redisUtils.getHashList(CommonConst.REDIS_CMS_DATA,
					CommonConst.REDIS_CMS_ARTLIST + catId + "_startNum_" + startNum, CmsArticleEntity.class);
			if (artList == null || artList.size() < 1) {
				artList = cmsArticleService.findByCatId(catId, startNum, 6);
				redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA,
						CommonConst.REDIS_CMS_ARTLIST + catId + "_startNum_" + startNum, artList);
			}

			if (artList != null && artList.size() > 0) {
				for (CmsArticleEntity art : artList) {
					CmsArticleDataEntity data = redisUtils.getHashObject(CommonConst.REDIS_CMS_DATA,
							CommonConst.REDIS_CMS_ARTICLE_DATA + art.getId(), CmsArticleDataEntity.class);
					if (data == null) {
						data = cmsArticleDataService.getArticleData(art.getId());
						redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA,
								CommonConst.REDIS_CMS_ARTICLE_DATA + art.getId(), data);
					}

					if (data != null) {
						String content = data.getContent();
						if (StringUtils.isNotBlank(content)) {
							// 将内容中的HTML标签去掉
							content = HtmlUtils.html2Text(content);
							// 截取显示长度
							if (content.length() > 400) {
								content = content.substring(0, 400);
							}
							data.setContent(content);
						}
						art.setArticleData(data);
					}
				}
			}

			map.put("catId", catId);
			map.put("artList", artList);
			map.put("categoryList", categoryList);
			map.put("category", cat);

			// 分页条数
			map.put("pageNo", pageNo);
			map.put("pageNum", pageNum);
			map.put("startPageNo", startPageNo);
			map.put("endPageNo", endPageNo);

			map.put("nextPageNo", nextPageNo);
			map.put("prePageNo", prePageNo);
		}
		return map;
	}

	@Override
	public Map<String, Object> article(Long id) {
		Map<String, Object> map = new HashMap<String, Object>();

		CmsArticleEntity art = redisUtils.getHashObject(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARCTCLE + id,
				CmsArticleEntity.class);
		if (art == null) {
			art = cmsArticleService.selectById(id);
			redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARCTCLE + id, art);
		}

		// 查找栏目实体
		CmsCategoryEntity category = cmsCategoryService.selectById(art.getCategoryId());
		if (art != null) {
			// 查询文章数据内容
			CmsArticleDataEntity data = redisUtils.getHashObject(CommonConst.REDIS_CMS_DATA,
					CommonConst.REDIS_CMS_ARTICLE_DATA + art.getId(), CmsArticleDataEntity.class);
			if (data == null) {
				data = cmsArticleDataService.getArticleData(art.getId());
				redisUtils.setHashObject(CommonConst.REDIS_CMS_DATA, CommonConst.REDIS_CMS_ARTICLE_DATA + art.getId(),
						data);
			}
			if (data != null) {
				art.setArticleData(data);
			}
			// 查询推荐列表
			List<CmsArticleEntity> remenList = cmsArticleService.findRemenList(art.getCategoryId(), 10);
			List<CmsArticleEntity> tuijianList = cmsArticleService.findTuijianList(art.getCategoryId(), 10);

			map.put("remenList", remenList);
			map.put("tuijianList", tuijianList);
		}

		// 查找文章的下一篇
		CmsArticleEntity nextArt = cmsArticleService.selectNext(id, category.getId());
		// 查找文章的上一篇
		CmsArticleEntity preArt = cmsArticleService.selectPre(id, category.getId());

		map.put("art", art);
		map.put("nextArt", nextArt);
		map.put("preArt", preArt);
		map.put("category", category);

		map.put("lottory_type", "jiqiao");
		return map;
	}

	/**
	 * 查询热门列表
	 */
	@Override
	public Map<String, Object> getArtList(String catAlias) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 查询栏目
		CmsCategoryEntity cat = cmsCategoryService.getByType(catAlias);
		if (cat != null) {
			List<CmsArticleEntity> artList = cmsArticleService.findTuijianList(cat.getId(), 8);

			map.put("artList", artList);
			map.put("cat", cat);
		}
		return map;
	}

	/**
	 * 
	 * @desc 按照查询条件返回资讯列表，先去redis和数据库比对，条数一样的话就直接查询redis，如果不一样就重新查询数据库生成redis
	 * @author abo
	 * @date 2018年8月23日 下午4:27:31
	 * @param lottId
	 *            彩种
	 * @param limit
	 *            分页条数
	 * @param pageNo
	 *            查询的页码
	 * @return
	 */
	public JSONArray artListToRedisByLottId(String lottId, int limit, int pageNo) {
		String alias = LotteryConstant.lotteryJqCategoryMap.get(lottId);
		CmsCategoryEntity cat = cmsCategoryService.getByType(alias);
		// 查询数据库条数，如果条数和redis不一致就去数据库重新查询，然后重新做到redis
		int dbCount = cmsArticleService.countByCatId(cat.getId());
		// 去redis里面获取，10000这里代码拿全部
		Map<Object, Object> map = redisservice.getHashMapsByLimit(lottId + "_" + LotteryDict.jqzxlb, 10000);
		int mapSize = map.size();
		int redisMapSize = 0;// redis里面的总数
		// 计算redis里面的总数
		for (Map.Entry<Object, Object> entry : map.entrySet()) {
			if (entry.getKey().equals(mapSize + "")) {
				JSONArray temp = JSONArray.parseArray(entry.getValue().toString());
				// 最后一页有可能没有20条，所以这里把最后一页单独拿出来count一下。
				redisMapSize = (mapSize - 1) * limit + temp.size();
			}
		}
		JSONArray ja = new JSONArray();
		// 如果为空的话就直接从数据库获取，然后放redis，如果不为空还要验证是否数据数量是否一直。看需不需重新放缓存
		if (map.isEmpty() || (redisMapSize != dbCount)) {
			// 按时间倒序出来
			List<CmsArticleEntity> list = cmsArticleService.findTuijianList(cat.getId(), 0);
			System.out.println(list.size());
			CmsArticleEntity cmsArticleEntity = null;
			int page = 1;
			// 按照limit分页放入缓存
			for (int i = 0; i < list.size(); i++) {
				// 按照限定条数进行分页，放入缓存
				if (ja.size() != 0 && ja.size() % limit == 0) {
					redisservice.setHashMap(lottId + "_" + LotteryDict.jqzxlb, page + "", ja.toJSONString());
					ja = new JSONArray();
					page++;
				}
				cmsArticleEntity = list.get(i);
				JSONObject json = new JSONObject();
				String date = DateUtils.format(cmsArticleEntity.getCreateDate(), "yyyy-MM-dd");
				json.put("desc", cmsArticleEntity.getDescription());
				json.put("title", cmsArticleEntity.getTitle());
				json.put("date", date);
				// 子页面的访问路径
				json.put("url", "/m/appjiqiao/" + cmsArticleEntity.getId() + ".html");
				ja.add(json);
			}
			// 解决最后小于20条的记录
			if (ja.size() > 0) {
				redisservice.setHashMap(lottId + "_" + LotteryDict.jqzxlb, page + "", ja.toJSONString());
			}
			System.out.println(lottId + "map.isEmpty()==" + map.isEmpty() + "或者" + "redisMapSize:dbCount  "
					+ redisMapSize + ":" + dbCount);
			System.out.println(lottId + "资讯map为空或者长度不一致，重新查询数据库并缓存到redis");
		} else {
			System.out.println(lottId + "资讯map一致，直接查询redis");
		}
		Object object = redisservice.getHashMap(lottId + "_" + LotteryDict.jqzxlb, pageNo + "");
		if (object != null) {
			ja = JSONArray.parseArray(object.toString());
		}
		return ja;
	}

	/**
	 * 
	 * @desc 按照查询条件返回资讯列表
	 * @author abo
	 * @date 2018年8月23日 下午4:27:31
	 * @param lottId
	 *            彩种
	 * @param limit
	 *            分页条数
	 * @param page
	 *            页码
	 * @param searchKey
	 *            查询条件
	 * @return
	 */
	@Override
	public JSONArray artListToRedisByLottIdAndSearchKey(String lottId, int limit, int page, String searchKey) {
		String alias = LotteryConstant.lotteryJqCategoryMap.get(lottId);
		CmsCategoryEntity cat = cmsCategoryService.getByType(alias);
		List<CmsArticleEntity> list = cmsArticleService.findArtListBySearchKey(cat.getId(), searchKey,
				(page - 1) * limit, page * limit);
		JSONArray ja = new JSONArray();
		CmsArticleEntity cmsArticleEntity = null;
		// 按照limit分页放入缓存
		for (int i = 0; i < list.size(); i++) {
			cmsArticleEntity = list.get(i);
			JSONObject json = new JSONObject();
			String date = DateUtils.format(cmsArticleEntity.getCreateDate(), "yyyy-MM-dd");
			json.put("desc", cmsArticleEntity.getDescription());
			json.put("title", cmsArticleEntity.getTitle());
			json.put("date", date);
			// 子页面的访问路径
			json.put("url", "/m/appjiqiao/" + cmsArticleEntity.getId() + ".html");
			ja.add(json);
		}
		return ja;
	}

}
