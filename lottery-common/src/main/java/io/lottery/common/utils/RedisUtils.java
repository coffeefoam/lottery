/**
 * Copyright 2018 
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package io.lottery.common.utils;

import com.alibaba.fastjson.JSON;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Redis工具类
 *
 * @author chenshun
 * @email
 * @date 2017-07-17 21:12
 */
@Component
public class RedisUtils {
	@Autowired
	private RedisTemplate<String, ?> redisTemplate;
	@Resource(name = "redisTemplate")
	private ValueOperations<String, String> valueOperations;
	@Resource(name = "redisTemplate")
	private HashOperations<String, String, Object> hashOperations;
	@Resource(name = "redisTemplate")
	private ListOperations<String, Object> listOperations;
	@Resource(name = "redisTemplate")
	private SetOperations<String, Object> setOperations;
	@Resource(name = "redisTemplate")
	private ZSetOperations<String, Object> zSetOperations;
	/** 默认过期时长，单位：秒 */
	public final static long DEFAULT_EXPIRE = 60 * 60 * 24;
	/** 不设置过期时长 */
	public final static long NOT_EXPIRE = -1;

	public void set(String key, Object value, long expire) {
		valueOperations.set(key, toJson(value));
		if (expire != NOT_EXPIRE) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
	}

	public void set(String key, Object value) {
		set(key, value, DEFAULT_EXPIRE);
	}

	public <T> T get(String key, Class<T> clazz, long expire) {
		String value = valueOperations.get(key);
		if (expire != NOT_EXPIRE) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
		return value == null ? null : fromJson(value, clazz);
	}

	public <T> T get(String key, Class<T> clazz) {
		return get(key, clazz, NOT_EXPIRE);
	}

	public String get(String key, long expire) {
		String value = valueOperations.get(key);
		if (expire != NOT_EXPIRE) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
		return value;
	}

	public String get(String key) {
		return get(key, NOT_EXPIRE);
	}

	public void delete(String key) {
		redisTemplate.delete(key);
	}

	/**
	 * Object转成JSON数据
	 */
	private String toJson(Object object) {
		if (object instanceof Integer || object instanceof Long || object instanceof Float || object instanceof Double || object instanceof Boolean || object instanceof String) {
			return String.valueOf(object);
		}
		return JSON.toJSONString(object);
	}

	/**
	 * JSON数据，转成Object
	 */
	private <T> T fromJson(String json, Class<T> clazz) {
		return JSON.parseObject(json, clazz);
	}

	/**
	 * 获取json数组数据
	 * 
	 * @param key
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getList(String key, Class<T> clazz) {
		return getList(key, clazz, NOT_EXPIRE);
	}

	public String getList(String key, long expire) {
		String value = valueOperations.get(key);
		if (expire != NOT_EXPIRE) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
		return value;
	}

	public <T> List<T> getList(String key, Class<T> clazz, long expire) {
		String value = valueOperations.get(key);
		if (expire != NOT_EXPIRE) {
			redisTemplate.expire(key, expire, TimeUnit.SECONDS);
		}
		return value == null ? null : fromJsonArray(value, clazz);
	}

	/**
	 * JSON Array数据，转成List
	 */
	private <T> List<T> fromJsonArray(String json, Class<T> clazz) {
		return JSON.parseArray(json, clazz);
	}

	/**
	 * 模糊匹配
	 * 
	 * @param pattern
	 * @return
	 */
	public Set<String> getKeys(String pattern) {
		return redisTemplate.keys(pattern);
	}

	/**
	 * 模糊查询，并删除key
	 * 
	 * @param pattern
	 */
	public void deleteKeys(String pattern) {
		if (StringUtils.isBlank(pattern)) {
			return;
		}
		Set<String> keys = getKeys(pattern);
		if (keys != null && !keys.isEmpty()) {
			for (String key : keys) {
				delete(key);
			}
		}
	}

	/**
	 * 将对象设置到hash中
	 * 
	 * @param key
	 * @param hashKey
	 * @param value
	 */
	public void setHashObject(String key, Object hashKey, Object value) {
		redisTemplate.opsForHash().put(key, hashKey, JSON.toJSONString(value));
	}

	/**
	 * 从hash中获取单个对象
	 * 
	 * @param key
	 * @param hashKey
	 * @param clazz
	 * @return
	 */
	public <T> T getHashObject(String key, Object hashKey, Class<T> clazz) {
		Object obj = redisTemplate.opsForHash().get(key, hashKey);
		return JSON.parseObject(String.valueOf(obj), clazz);
	}

	/**
	 * 从hash中获取list集合
	 * 
	 * @param key
	 * @param hashKey
	 * @param clazz
	 * @return
	 */
	public <T> List<T> getHashList(String key, Object hashKey, Class<T> clazz) {
		Object obj = redisTemplate.opsForHash().get(key, hashKey);
		return JSON.parseArray(String.valueOf(obj), clazz);
	}
	
	
	/**
	 * 模糊查询以sameKey开通的hashKey
	 * 
	 * @param key
	 * @param sameKey
	 * @return
	 */
	public Set<String> getHashKeys(String key, String sameKey) {
		Set<Object> set = redisTemplate.opsForHash().keys(key);
		System.out.println("set.size=" + set.size());
		Set<String> keySet = new HashSet<String>();
		for (Object key1 : set) {
			String keyStr = String.valueOf(key1);
			if (keyStr.contains(sameKey)) {
				keySet.add(keyStr);
			}
		}
		System.out.println("keySet.size=" + keySet.size());
		return keySet;
	}

	/**
	 * 删除hash中的key值
	 * 
	 * @param key
	 * @param hashKeys
	 */
	public void deleteHashKeys(String key, Object... hashKeys) {
		redisTemplate.opsForHash().delete(key, hashKeys);
	}

	/**
	 * 删除hash中的key值
	 * 
	 * @param key
	 * @param hashKeys
	 */
	public void deleteHashKeys(String key, Set<String> hashKeys) {
		redisTemplate.opsForHash().delete(key, hashKeys);
	}


}
