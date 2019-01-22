package com.boying.cpapi.service.cldata;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.LotteryConstant;
import com.boying.cpapi.util.LotteryUtil;

import io.lottery.common.config.LotteryDict;
import io.lottery.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

/**
* @Description: 长龙统计
* @author ms
* @date 2018年7月30日
 */
@Slf4j
@Service
public class LotteryClService {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Resource
	private RedisService redisservice;
	
	//定义大小数组
	private static String[] dxarray= {"大","小"};
	//定义单双数组
	private static String[] dsarray= {"单","双"};
	//定义龙虎数组
	private static String[] lharray= {"龍","虎"};
	
	/**
	* 处理长龙业务
	* @author ms
	* @date 2018年7月31日 下午4:32:53
	 */
	public void execute(String lottId) {
		JSONArray resultarray=new JSONArray();
		//号码大小长龙
		this.getDxCl(lottId,resultarray);
		//号码单双长龙
		this.getDsCl(lottId,resultarray);
		//号码龙虎长龙
		this.getLhCl(lottId,resultarray);
		//形态长龙
		this.getShapeCl(lottId,resultarray);
		if(!resultarray.isEmpty()) {
			resultarray=sortByCount(resultarray);
			redisservice.setHashMap(LotteryDict.cltj,lottId, resultarray.toJSONString());
		}
		
	}
	
	/**
	* 跟据count大小进行冒泡排序
	* @author ms
	* @date 2018年8月2日 下午1:47:08
	 */
	private JSONArray sortByCount(JSONArray array) {
		for(int i=0;i<array.size();i++) {
			for(int j=0;j<array.size()-1-i;j++) {
				if(array.getJSONObject(j).getInteger("count")<array.getJSONObject(j+1).getInteger("count")){  
					JSONObject r=array.getJSONObject(j);
					array.set(j, array.getJSONObject(j+1));  
					array.set(j+1, r);  
                } 
			}
		}
		return array;
	}
	
	/**
	 * 计算大小长龙
	* @author ms
	* @date 2018年7月30日 上午11:53:45
	 */
	public JSONArray getDxCl(String lottId,JSONArray resultarray) {
		try {
			// 所有号码
			String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			String[] numarray = numstr.split(",");
			// 取大小路珠数据
			String dxlz = redisservice.get(lottId + "_" + LotteryDict.dxlz);
			if (StringUtils.isNotBlank(dxlz)) {
				JSONObject dxlzobj = JSONObject.parseObject(dxlz);
				for (String num : numarray) {
					JSONArray dxclarray = dxlzobj.getJSONArray(num);
					if(dxclarray!=null&&dxclarray.size()>0) {
						String result = dxclarray.getString(dxclarray.size() - 1);
						for (String dxstr : dxarray) {
							if (result.contains(dxstr) && result.length() > 2) {
								JSONObject resultobj = new JSONObject();
								resultobj.put("position", "第"+num.replace("num", "")+"名 "+dxstr);
								resultobj.put("count", result.length());
								//大小路珠路径
								resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_dx"));
								resultarray.add(resultobj);
							}
						}
					}
				}
				return resultarray;
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(ExceptionUtil.getExceptionStackTrace(e));
			return null;
		}
	}
	
	/**
	 * 计算单双长龙
	* @author ms
	* @date 2018年7月30日 上午11:53:45
	 */
	public JSONArray getDsCl(String lottId,JSONArray resultarray) {
		try {
			//所有号码
			String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			String[] numarray=numstr.split(",");
			//取单双路珠数据
			String dslzstr=redisservice.get(lottId + "_" + LotteryDict.dslz);
			if(StringUtils.isNotBlank(dslzstr)){
				JSONObject dslzobj=JSONObject.parseObject(dslzstr);
				for(String num:numarray) {
					JSONArray dsclarray=dslzobj.getJSONArray(num);
					if(dsclarray!=null&&dsclarray.size()>0) {
						String result=dsclarray.getString(dsclarray.size()-1);
						for(String dsstr:dsarray) {
							if(result.contains(dsstr)&&result.length()>2) {
								JSONObject resultobj=new JSONObject();
								resultobj.put("position", "第"+num.replace("num", "")+"名 "+dsstr);
								resultobj.put("count", result.length());
								//单双路珠路径
								resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_ds"));
								resultarray.add(resultobj);
							}
						}
					}
					
				}
				return resultarray;
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(ExceptionUtil.getExceptionStackTrace(e));
			return null;
		}
	}
	/**
	* 计算龙虎长龙
	* @author ms
	* @date 2018年7月30日 下午2:35:30
	 */
	public JSONArray getLhCl(String lottId,JSONArray resultarray) {
		try {
			//所有号码
			String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
			String[] numarray=numstr.split(",");
			//取龙虎路珠数据
			String lhlzstr=redisservice.get(lottId + "_" + LotteryDict.lhlz);
			if(StringUtils.isNotBlank(lhlzstr)){
				JSONObject lhlzobj=JSONObject.parseObject(lhlzstr);
				for(String num:numarray) {
					JSONArray lhclarray=lhlzobj.getJSONArray(num);
					if(lhclarray!=null&&lhclarray.size()>0) {
						String result=lhclarray.getString(lhclarray.size()-1);
						for(String lhstr:lharray) {
							if(result.contains(lhstr)&&result.length()>2) {
								JSONObject resultobj=new JSONObject();
								resultobj.put("position", "第"+num.replace("num", "")+"名 "+lhstr);
								resultobj.put("count", result.length());
								//单双路珠路径
								resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_lh"));
								resultarray.add(resultobj);
							}
						}
					}
				}
				return resultarray;
			}else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.error(ExceptionUtil.getExceptionStackTrace(e));
			return null;
		}
	}
	
	/**
	 * 计算冠亚和长龙
	 * @author ms
	 * @date 2018年7月30日 上午11:53:45
	 */
	public JSONArray getGyhCl(String lottId,JSONArray resultarray) {
		try {
			//取冠亚和路珠数据
			String gyhlzstr=redisservice.get(lottId + "_" + LotteryDict.gyhlz);
			if(StringUtils.isNotBlank(gyhlzstr)) {
				JSONObject gyhlzobj=JSONObject.parseObject(gyhlzstr);
				//大小
				JSONArray dxclarray=gyhlzobj.getJSONArray("dx");
				if(dxclarray!=null&&dxclarray.size()>0) {
					String resultdx=dxclarray.getString(dxclarray.size()-1);
					for(String dxstr:dxarray) {
						if(resultdx.contains(dxstr)&&resultdx.length()>0) {
							JSONObject resultobj=new JSONObject();
							resultobj.put("position", "冠亚和 "+dxstr);
							resultobj.put("count", resultdx.length());
							//冠亚和大小路珠数据路径
							resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_gyh"));
							resultarray.add(resultobj);
						}
					}
				}
				//单双
				JSONArray dsclarray=gyhlzobj.getJSONArray("ds");
				if(dsclarray!=null&&dsclarray.size()>0) {
					String resultds=dsclarray.getString(dsclarray.size()-1);
					for(String dsstr:dsarray) {
						if(resultds.contains(dsstr)&&resultds.length()>0) {
							JSONObject resultobj=new JSONObject();
							resultobj.put("position", "冠亚和 "+dsstr);
							resultobj.put("count", resultds.length());
							//冠亚和单双路珠数据路径
							resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_gyh"));
							resultarray.add(resultobj);
						}
					}
				}
				return resultarray;
			}else {
				return null;
			}
		}catch (Exception e) {
			e.printStackTrace();
			log.error(ExceptionUtil.getExceptionStackTrace(e));
			return null;
		}
	}
	
	/**
	* @Title: 计算形态长龙
	* @author ms
	* @date 2018年7月30日 下午3:27:12
	 */
	public JSONArray getShapeCl(String lottId,JSONArray resultarray) {
		String numstr = LotteryConstant.lotteryColumnStrMap.get(lottId);
		String[] numarray=numstr.split(",");
		
		//获取位置的大小形态个数（ab、abb、aabb）
		String dxlz = redisservice.get(lottId + "_" + LotteryDict.dxlz);
		if(StringUtils.isNoneBlank(dxlz)) {
			String[] dxshape= {"大小","大大小","大大小小","小大","小小大","小小大大"};
			if (StringUtils.isNotBlank(dxlz)) {
				JSONObject dxlzobj = JSONObject.parseObject(dxlz);
				for (String num : numarray) {
					JSONArray dxclarray = dxlzobj.getJSONArray(num);
					if(dxclarray!=null&&dxclarray.size()>0) {
						List<String> list=JSONArray.toJavaObject(dxclarray,List.class);
						Collections.reverse(list);
						for(String key:dxshape) {
							int count=LotteryUtil.getDataShape(list, key);
							if(count>2) {
								JSONObject resultobj=new JSONObject();
								resultobj.put("position", "第" + num.replace("num", "") + "名 "+key+LotteryConstant.lotteryShapeMap.get(key)+"形态");
								resultobj.put("count", count);
								resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_shape_dx"));
								resultarray.add(resultobj);
							}
						}
					}
				}
			}
		}
		//获取位置的单双形态个数（ab、abb、aabb）
		String dslz = redisservice.get(lottId + "_" + LotteryDict.dslz);
		if(StringUtils.isNoneBlank(dslz)) {
			String[] dsshape= {"单双","单单双","单单双双","双单","双双单","双双单单"};
			if (StringUtils.isNotBlank(dslz)) {
				JSONObject dxlzobj = JSONObject.parseObject(dslz);
				for (String num : numarray) {
					JSONArray dsclarray = dxlzobj.getJSONArray(num);
					if(dsclarray!=null&&dsclarray.size()>0) {
						List<String> list=JSONArray.toJavaObject(dsclarray,List.class);
						Collections.reverse(list);
						for(String key:dsshape) {
							int count=LotteryUtil.getDataShape(list, key);
							if(count>2) {
								JSONObject resultobj=new JSONObject();
								resultobj.put("position", "第" + num.replace("num", "") + "名 "+key+LotteryConstant.lotteryShapeMap.get(key)+"形态");
								resultobj.put("count", count);
								resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_shape_ds"));
								resultarray.add(resultobj);
							}
						}
					}
				}
			}
		}
		//获取位置的龍虎形态个数（ab、abb、aabb）
		String lhlz = redisservice.get(lottId + "_" + LotteryDict.lhlz);
		if(StringUtils.isNoneBlank(lhlz)) {
			String[] lhshape= {"龍虎","龍龍虎","龍龍虎虎","虎龍","虎虎龍","虎虎龍龍"};
			if (StringUtils.isNotBlank(lhlz)) {
				JSONObject lhlzobj = JSONObject.parseObject(lhlz);
				for (String num : numarray) {
					JSONArray lhclarray = lhlzobj.getJSONArray(num);
					if(lhclarray!=null&&lhclarray.size()>0) {
						List<String> list=JSONArray.toJavaObject(lhclarray,List.class);
						Collections.reverse(list);
						for(String key:lhshape) {
							int count=LotteryUtil.getDataShape(list, key);
							if(count>2) {
								JSONObject resultobj=new JSONObject();
								resultobj.put("position", "第" + num.replace("num", "") + "名 "+key+LotteryConstant.lotteryShapeMap.get(key)+"形态");
								resultobj.put("count", count);
								resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_shape_lh"));
								resultarray.add(resultobj);
							}
						}
					}
				}
			}
		}
		
		//冠亚和大小、单双形态个数（ab、abb、aabb）
		String gyhlzstr=redisservice.get(lottId + "_" + LotteryDict.gyhlz);
		if(StringUtils.isNotBlank(gyhlzstr)) {
			String[] shape= {"大小","大大小","大大小小","小大","小小大","小小大大","单双","单单双","单单双双","双单","双双单","双双单单"};
			JSONObject gyhlzobj=JSONObject.parseObject(gyhlzstr);
			//大小
			JSONArray dxclarray=gyhlzobj.getJSONArray("dx");
			if(dxclarray!=null&&dxclarray.size()>0) {
				List<String> list=JSONArray.toJavaObject(dxclarray,List.class);
				Collections.reverse(list);
				for(String key:shape) {
					int count=LotteryUtil.getDataShape(list, key);
					if(count>2) {
						JSONObject resultobj=new JSONObject();
						resultobj.put("position", "冠亚和 "+key+LotteryConstant.lotteryShapeMap.get(key)+"形态");
						resultobj.put("count", count);
						resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_shape_gyh"));
						resultarray.add(resultobj);
					}
				}
			}
			//单双
			JSONArray dsclarray=gyhlzobj.getJSONArray("ds");
			if(dsclarray!=null&&dsclarray.size()>0) {
				List<String> list=JSONArray.toJavaObject(dsclarray,List.class);
				Collections.reverse(list);
				for(String key:shape) {
					int count=LotteryUtil.getDataShape(list, key);
					if(count>2) {
						JSONObject resultobj=new JSONObject();
						resultobj.put("position", "冠亚和 "+key+LotteryConstant.lotteryShapeMap.get(key)+"形态");
						resultobj.put("count", count);
						resultobj.put("url", LotteryConstant.lotteryClurlMap.get(lottId+"_shape_gyh"));
						resultarray.add(resultobj);
					}
				}
			}
		}
		return resultarray;
	}
	
}