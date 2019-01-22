package io.lottery.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public interface CmsService {

	Map<String, Object> index();

	Map<String, Object> buildList(String catAlias, Integer pageNo, boolean isShowAll);

	Map<String, Object> article(Long id);

	Map<String, Object> getArtList(String catAlias);

	/**
	 * 
	 * @desc 按照查询条件返回资讯列表，先去redis和数据库比对，条数一样的话就直接查询redis，如果不一样就重新查询数据库生成redis
	 * @author abo
	 * @date 2018年8月31日 上午10:39:45
	 * @param lottId
	 *            彩种
	 * @param limit
	 *            限定条数
	 * @param pageNo
	 *            页码
	 * @return
	 */
	JSONArray artListToRedisByLottId(String lottId, int limit, int pageNo);

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
	JSONArray artListToRedisByLottIdAndSearchKey(String lottId, int limit, int page, String searchKey);

}
