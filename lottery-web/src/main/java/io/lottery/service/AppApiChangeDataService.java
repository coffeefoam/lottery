package io.lottery.service;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * 
 * @desc 根据app需求调整数据格式
 * @author abo
 * @date 2018年8月7日 上午11:06:03
 *
 */
public interface AppApiChangeDataService {

	/**
	 * 
	 * @desc 修改走势图数据
	 * @author abo
	 * @date 2018年8月7日 上午11:08:14
	 * @param data
	 * @param lottId
	 * @return
	 */
	public JSONArray changeZstData(String data, String type);

	/**
	 * 
	 * @desc 修改开奖记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:15:58
	 * @param data
	 * @param lottId
	 * @return
	 */
	public JSONArray changeKjjlData(JSONArray data, String lottId);

	/**
	 * 
	 * @desc 修改两面投注参考记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:16:36
	 * @param data
	 * @return
	 */
	public JSONObject changeLmlzckData(String data);

	/**
	 * 
	 * @desc 修改位置遗漏号码遗漏记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:17:32
	 * @param data
	 * @return
	 */
	public JSONArray changeWzylHmylData(String data, String code);

	/**
	 * 
	 * @desc 修改单双路珠记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:19:00
	 * @param data
	 * @param type
	 * @return
	 */
	public JSONArray changeDslzData(String data, String type);

	/**
	 * 
	 * @desc 修改单双大小历史记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:20:05
	 * @param data
	 * @return
	 */
	public JSONArray changeDsDxLsData(JSONArray data);

	/**
	 * 
	 * @desc 修改龙虎历史记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:22:11
	 * @param data
	 * @return
	 */
	public JSONArray changeLhlsData(JSONArray data);

	/**
	 * 
	 * @desc 修改冠亚和历史记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:20:50
	 * @param data
	 * @return
	 */
	public JSONArray changeGyhlsData(JSONArray data);

	/**
	 * 
	 * @desc 修改冠亚和遗漏记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:27:08
	 * @param data
	 * @return
	 */
	public JSONArray changeGyhylData(String data);

	/**
	 * 
	 * @desc 修改号码前后路珠记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:28:37
	 * @param data
	 * @param codStringe
	 * @return
	 */
	public JSONArray changeQhlzData(String data, String codStringe);

	/**
	 * 
	 * @desc 修改两面投注参考记录数据
	 * @author abo
	 * @date 2018年8月7日 上午11:28:50
	 * @param data
	 * @param lottId
	 * @return
	 */
	public JSONArray changeLmtzckData(JSONArray data, String lottId);

	/**
	 * 
	 * @desc 修改冠亚和路珠记录数据
	 * @author abo
	 * @date 2018年8月8日 下午5:13:47
	 * @param data
	 * @return
	 */
	public JSONArray changeGyhLzData(String data);

	/**
	 * 
	 * @desc 修改时时彩号码遗漏记录数据（号码遗漏，单双，大小，龙虎，总和单双，总和大小）
	 * @author abo
	 * @date 2018年8月8日 下午5:13:47
	 * @param data
	 * @param code
	 * @return
	 */
	public JSONArray changeSscHmylData(String data, String code);

	/**
	 * 
	 * @desc 修改总和路珠记录数据（里面有大小，单双）
	 * @author abo
	 * @date 2018年8月8日 下午5:13:47
	 * @param data
	 * @return
	 */
	public JSONArray changeZhlzData(String data, String code);

	/**
	 * 
	 * @desc 修改尾数大小路珠记录数据
	 * @author abo
	 * @date 2018年8月9日 下午1:47:46
	 * @param data
	 * @return
	 */
	public JSONArray changeWsdxlzData(String data, String code);

	/**
	 * 
	 * @desc 修改奇偶路珠记录数据
	 * @author abo
	 * @date 2018年8月9日 下午2:02:48
	 * @param data
	 * @return
	 */
	public JSONArray changeJolzData(String data, String type);

	/**
	 * 
	 * @desc 修改历史号码统计记录数据
	 * @author abo
	 * @date 2018年8月9日 下午2:02:48
	 * @param data
	 * @param code
	 * @return
	 */
	public JSONArray changeLshmtjData(JSONArray array, String code);

	/**
	 * 
	 * @desc 修改上下段位走势数据
	 * @author abo
	 * @date 2018年8月9日 下午2:02:48
	 * @param data
	 * @return
	 */
	public JSONArray changeSxdwzsData(JSONArray array);

	/**
	 * 
	 * @desc 数据比对
	 * @author abo
	 * @date 2018年8月15日 下午2:53:51
	 * @param currentOb
	 * @param oldOb
	 * @return
	 */
	public JSONArray changeSjdbData(Object currentOb, Object oldOb);

	/**
	 * 
	 * @desc 狙击杀号数据封装
	 * @author abo
	 * @date 2018年8月22日 上午10:48:28
	 * @param map
	 * @return
	 */
	public JSONArray changeJjshData(Map<Object, Object> map);

	/**
	 * 
	 * @desc 号码推荐计划
	 * @author abo
	 * @date 2018年8月25日 上午10:57:04
	 * @param map
	 * @param searchkey
	 * @return
	 */
	public JSONArray changeHmtjjhData(Map<Object, Object> map, String searchkey);

	/**
	 * 
	 * @desc 号码计划列表
	 * @author abo
	 * @date 2018年8月25日 下午2:06:33
	 * @param map
	 * @param lottId 
	 * @return
	 */
	public JSONArray changeHmtjjhlbData(Map<Object, Object> map, String lottId);

	/**
	 * 
	 * @desc 两面推荐计划
	 * @author abo
	 * @date 2018年8月27日 下午2:49:43
	 * @param map
	 * @param searchkey
	 * @param position
	 * @return
	 */
	public JSONArray changeLmtjjhData(Map<Object, Object> map, String searchkey, String position);

	/**
	 * 
	 * @desc 两面计划列表
	 * @author abo
	 * @date 2018年8月27日 下午4:27:38
	 * @param map
	 * @return
	 */
	public JSONArray changelmtjjhlbData(Map<Object, Object> map, String lottId);

	/**
	 * 
	 * @desc pc蛋蛋总和路珠
	 * @author abo
	 * @date 2018年9月5日 下午2:59:01
	 * @param data
	 * @return
	 */
	public JSONArray changePcddZhlzData(String data);
}
