package io.lottery.common.config;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSONArray;

/**
 * @ClassName: RedisService
 * @Description: Redis操作类
 */
@Service
public class RedisService {

	@Autowired
	private StringRedisTemplate stringRedisTemplate;

	@Resource(name = "stringRedisTemplate")
	private ValueOperations<String, String> valOpsStr;

	/**
	 * @Description :设置超时key,自增，用于统计
	 * 
	 */
	public void increment(String key, long value) {
		valOpsStr.increment(key, value);
	}

	/**
	 * @Description :存储
	 */
	public void set(String key, String value) {
		valOpsStr.set(key, value);
	}

	/**
	 * @Description :获取
	 */
	public String get(String key) {
		return valOpsStr.get(key);
	}

	/**
	 * @Description :根据key移除
	 */
	public void remove(String key) {
		stringRedisTemplate.delete(key);
	}

	/**
	 * @Description :设置超时key,单位为秒钟
	 * 
	 */
	public void set(String key, String value, int offset) {
		valOpsStr.set(key, value, offset, TimeUnit.SECONDS);
	}

	/**
	 * @Description :根据前缀得到value
	 * 
	 */
	public List<String> getByPrefix(String prefix) {
		Set<String> keys = stringRedisTemplate.keys(prefix + "*");
		if (keys == null || keys.size() == 0) {
			return null;
		}
		List<String> list = new ArrayList<>();
		for (String string : keys) {
			list.add(valOpsStr.get(string));
		}
		return list;
	}

	/**
	 * @Description :根据前缀得到value
	 */
	public void delByPrefix(String prefix) {
		Set<String> keys = stringRedisTemplate.keys(prefix + "*");
		if (keys == null || keys.size() == 0) {
			return;
		}
		for (String key : keys) {
			stringRedisTemplate.delete(key);
		}
	}

	/**
	 * 存储set集合
	 * 
	 * @param key
	 * @param value
	 */
	public void setHashSet(String key, String value) {
		stringRedisTemplate.opsForSet().add(key, value);
	}

	/**
	 * 获取存储的set集合
	 * 
	 * @param key
	 * @return
	 */
	public Set<String> getHashSet(String key) {
		return stringRedisTemplate.opsForSet().members(key);
	}

	/**
	 * 根据key删除set集合中的某一条
	 * 
	 * @param key
	 * @param values
	 */
	public void removeHashSet(String key, String values) {
		stringRedisTemplate.opsForSet().remove(key, values);
	}

	/**
	 * 根据前缀获取set集合
	 * 
	 * @param prefix
	 * @return
	 */
	public Set<String> getSetByPrefix(String prefix) {
		Set<String> keyset = stringRedisTemplate.keys(prefix + "*");
		if (keyset == null || keyset.size() == 0) {
			return null;
		}
		Set<String> reipset = new HashSet<String>();
		for (String keys : keyset) {
			Set<String> ipset = stringRedisTemplate.opsForSet().members(keys);
			reipset.addAll(ipset);
		}
		return reipset;
	}

	/**
	 * 
	 * @desc 往redis里面放map键值队
	 * @author abo
	 * @date 2018年7月9日 下午3:05:13
	 * @param key
	 * @param hashKey
	 *            日期作为键或者彩种做key
	 * @param value
	 */
	public void setHashMap(String key, Object hashKey, Object value) {
		stringRedisTemplate.opsForHash().put(key, hashKey, value);
	}

	/**
	 * 
	 * @desc 往redis里面放map键值队
	 * @author abo
	 * @date 2018年7月9日 下午3:11:06
	 * @param key
	 * @param hashKey
	 *            日期作为键或者彩种做key
	 * @return
	 */
	public Object getHashMap(String key, Object hashKey) {
		return stringRedisTemplate.opsForHash().get(key, hashKey);
	}

	public List<Object> getHashMapList(String key) {
		Set<Object> hashkeys = stringRedisTemplate.opsForHash().keys(key);
		return stringRedisTemplate.opsForHash().multiGet(key, hashkeys);
	}

	/**
	 * 
	 * @desc 根据key获取整个hashmap
	 * @author abo
	 * @date 2018年7月11日 下午5:30:32
	 * @param key
	 * @return
	 */
	public Map<Object, Object> getHashMaps(String key) {
		return stringRedisTemplate.opsForHash().entries(key);
	}

	/**
	 * 
	 * @desc 按日期查询，按照最后80条查询
	 * @author abo
	 * @date 2018年8月10日 下午4:00:15
	 * @param key
	 *            彩种接口名
	 * @param date
	 *            日期
	 * @return
	 */
	public Map<Object, Object> getHashMapsByDate(String key, String date) {
		// 取彩种的id
		String k = key.split("_")[0];
		Object object = null;
		if (!StringUtils.isEmpty(date)) {
			object = getHashMap(k + "_" + LotteryDict.limitperiodsbydate, date);
		} else {
			object = getHashMap(LotteryDict.limitperiods, k);
		}
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		if (object != null) {
			String temp = object.toString().replaceAll("\\[", "");
			temp = temp.toString().replaceAll("\\]", "");
			temp = temp.toString().replaceAll(" ", "");
			String[] array = temp.split(",");
			List<Object> list = Arrays.asList(array);
			List<Object> l = stringRedisTemplate.opsForHash().multiGet(key, list);
			JSONArray ja = JSONArray.parseArray(l.toString());
			if (list != null && list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					if (ja.get(i) == null) {
						continue;
					}
					map.put(list.get(i), ja.get(i));
				}
			}
		}
		return map;
	}

	/**
	 * 
	 * @desc 按照彩种最后20条查询
	 * @author abo
	 * @date 2018年8月11日 上午10:32:56
	 * @param code
	 * @return
	 */
	public Map<Object, Object> getHashMapsByLimit(String code) {
		Set<Object> set = stringRedisTemplate.opsForHash().keys(code);
		List<Object> list = new ArrayList<Object>();
		list.addAll(set);
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return o2.toString().compareTo(o1.toString());
			}
		});// 使用Collections的sort方法，并且重写compare方法
		int ss = 0;
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (ss == 20) {
					break;
				}
				map.put(list.get(i), this.stringRedisTemplate.opsForHash().get(code, list.get(i)));
				ss++;
			}
		}
		return map;
	}

	/**
	 * 
	 * @desc 根据参数返回指定条数
	 * @author abo
	 * @date 2018年8月22日 下午3:35:10
	 * @param code 彩种接口名
	 * @param limit 返回条数
	 * @return
	 */
	public Map<Object, Object> getHashMapsByLimit(String code, int limit) {
		Set<Object> set = stringRedisTemplate.opsForHash().keys(code);
		List<Object> list = new ArrayList<Object>();
		list.addAll(set);
		Collections.sort(list, new Comparator<Object>() {
			public int compare(Object o1, Object o2) {
				return o2.toString().compareTo(o1.toString());
			}
		});// 使用Collections的sort方法，并且重写compare方法
		Map<Object, Object> map = new LinkedHashMap<Object, Object>();
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i++) {
				if (i > limit - 1) {
					break;
				}
				map.put(list.get(i), this.stringRedisTemplate.opsForHash().get(code, list.get(i)));
			}
		}
		return map;
	}

}
