package com.boying.cpapi.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.RabbitConfig;
import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.mapper.pdata.LotteryProcessMapper;
import com.boying.cpapi.util.LotteryConstant;

import io.lottery.common.config.LotteryDict;

@Component
public class PushDataServices {
	@Resource
	private LotteryProcessMapper lotteryProcessMapper;
	@Autowired
	private RedisService redisservice;
	@Autowired
	private RabbitConfig rabbitConfig;
	
	/**
	 * 推送最新开奖数据、与开奖记录数据到MQ服务器
	* @author ms
	* @date 2018年10月29日 下午1:38:06
	 */
	public void pushDataToRabbitMQ(String lottId) {
		JSONObject dataobj = new JSONObject(true);
		JSONObject rabbitobj = new JSONObject(true);
		
		//最新开奖数据
		String zxkjdata=redisservice.get(lottId + "_" + LotteryDict.zxkj);
		rabbitobj.put("type", 0);//最新开奖
		dataobj.put("key", lottId + "_" + LotteryDict.zxkj);
		dataobj.put("value", JSON.parseObject(zxkjdata));
		rabbitobj.put("data", dataobj);
		rabbitConfig.rabbitMQ_Send(rabbitobj.toJSONString());
		
		//历史开奖数据
		List<String> periodlist=lotteryProcessMapper.findPeriodsByOrder(LotteryConstant.tableNameMap.get(lottId), 80);
		List<Object> kkjllist=redisservice.getHashMapList(lottId + "_" + LotteryDict.kjjl, periodlist);
		rabbitobj.put("type", 1);//开奖记录
		dataobj.put("key", lottId + "_" + LotteryDict.kjjl);
		dataobj.put("value", JSON.parseArray(kkjllist.toString()));
		rabbitobj.put("data", dataobj);
		rabbitConfig.rabbitMQ_Send(rabbitobj.toJSONString());
		
	}

}
