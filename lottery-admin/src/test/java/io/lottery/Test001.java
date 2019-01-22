package io.lottery;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class Test001 {
	
	
	public static void main(String[] args) {
		//2018年生肖
		/*鼠：11,23,35,47
		牛：10,22,34,46
		虎：9,21,33,45
		兔：8,20,32,44
		龙：7,19,31,43
		蛇：6,18,30,42
		马：5,17,29,41
		羊：4,16,28,40
		猴：3,15,27,39
		鸡：2,14,26,38
		狗：1,13,25,37,49
		猪：12,24,36,48*/
		
		//2017年生肖
		/*鼠	10, 22, 34, 46
		牛	9, 21, 33, 45
		虎	8, 20, 32, 44
		兔	7, 19, 31, 43
		龍	6, 18, 30, 42
		蛇	5, 17, 29, 41
		馬	4, 16, 28, 40
		羊	3, 15, 27, 39
		猴	2, 14, 26, 38
		雞	1, 13, 25, 37, 49
		狗	12, 24, 36, 48
		豬	11, 23, 35, 47*/
		
		//2019年生肖
		/*鼠	12, 24, 36, 48
		牛	11, 23, 35, 47
		虎	10, 22, 34, 46
		兔	9, 21, 33, 45
		龍	8, 20, 32, 44
		蛇	7, 19, 31, 43
		馬	6, 18, 30, 42
		羊	5, 17, 29, 41
		猴	4, 16, 28, 40
		雞	3, 15, 27, 39
		狗	2, 14, 26, 38
		豬	1, 13, 25, 37, 49*/
		
		//2020年生肖
		/*鼠	1, 13, 25, 37, 49
		牛	12, 24, 36, 48
		虎	11, 23, 35, 47
		兔	10, 22, 34, 46
		龍	9, 21, 33, 45
		蛇	8, 20, 32, 44
		馬	7, 19, 31, 43
		羊	6, 18, 30, 42
		猴	5, 17, 29, 41
		雞	4, 16, 28, 40
		狗	3, 15, 27, 39
		豬	2, 14, 26, 38*/
		
		//2021年生肖
		/*鼠	2, 14, 26, 38
		牛	1, 13, 25, 37, 49
		虎	12, 24, 36, 48
		兔	11, 23, 35, 47
		龍	10, 22, 34, 46
		蛇	9, 21, 33, 45
		馬	8, 20, 32, 44
		羊	7, 19, 31, 43
		猴	6, 18, 30, 42
		雞	5, 17, 29, 41
		狗	4, 16, 28, 40
		豬	3, 15, 27, 39*/
		
		List<Integer> shulist= Arrays.asList(2, 14, 26, 38);
		List<Integer> niulist= Arrays.asList(1, 13, 25, 37, 49);
		List<Integer> hulist= Arrays.asList(12, 24, 36, 48);
		List<Integer> tulist= Arrays.asList(11, 23, 35, 47);
		List<Integer> longlist= Arrays.asList(10, 22, 34, 46);
		List<Integer> shelist= Arrays.asList(9, 21, 33, 45);
		List<Integer> malist= Arrays.asList(8, 20, 32, 44);
		List<Integer> yanglist= Arrays.asList(7, 19, 31, 43);
		List<Integer> houlist= Arrays.asList(6, 18, 30, 42);
		List<Integer> jilist= Arrays.asList(5, 17, 29, 41);
		List<Integer> goulist= Arrays.asList(4, 16, 28, 40);
		List<Integer> zhulist= Arrays.asList(3, 15, 27, 39);
		
		
		Map<String,List<Integer>> map=new HashMap<String,List<Integer>>();
		map.put("鼠", shulist);
		map.put("牛", niulist);
		map.put("虎", hulist);
		map.put("兔", tulist);
		map.put("龙", longlist);
		map.put("蛇", shelist);
		map.put("马", malist);
		map.put("羊", yanglist);
		map.put("猴", houlist);
		map.put("鸡", jilist);
		map.put("狗", goulist);
		map.put("猪", zhulist);
		System.err.println(new JSONObject(new HashMap(map)));
		
		String str="{\"鼠\":[11,23,35,47],\"鸡\":[2,14,26,38],\"蛇\":[6,18,30,42],\"羊\":[4,16,28,40],\"猪\":[12,24,36,48],\"马\":[5,17,29,41],\"虎\":[9,21,33,45],\"兔\":[8,20,32,44],\"猴\":[3,15,27,39],\"狗\":[1,13,25,37,49],\"龙\":[7,19,31,43],\"牛\":[10,22,34,46]}";
		Map m=JSON.parseObject(str, HashMap.class);
		//System.err.println(m);
		
		
		Map<String,Integer> sxCountMap=new HashMap<String,Integer>();
		sxCountMap.put("鼠",0);
		sxCountMap.put("牛",0);
		sxCountMap.put("虎",0);
		sxCountMap.put("兔",0);
		sxCountMap.put("龙",0);
		sxCountMap.put("蛇",0);
		sxCountMap.put("马",0);
		sxCountMap.put("羊",0);
		sxCountMap.put("猴",0);
		sxCountMap.put("鸡",0);
		sxCountMap.put("狗",0);
		sxCountMap.put("猪",0);
		String key="蛇";
		for (int i=0;i<5;i++) {
			sxCountMap.put(key, sxCountMap.get(key)+1);
		}
		key="狗";
		for (int i=0;i<6;i++) {
			sxCountMap.put(key, sxCountMap.get(key)+1);
		}
		key="兔";
		for (int i=0;i<3;i++) {
			sxCountMap.put(key, sxCountMap.get(key)+1);
		}
		key="鼠";
		for (int i=0;i<3;i++) {
			sxCountMap.put(key, sxCountMap.get(key)+1);
		}
		System.err.println(sxCountMap);
		
		int i=0;
		System.err.println(++i);
		System.err.println(++i);
		
		 String[] SX_12 = { "猪", "狗", "鸡", "猴", "羊", "马", "蛇", "龙", "兔", "虎", "牛", "鼠" };
		
		List<String> sxList = Arrays.asList(SX_12);
		
		System.err.println(sxList);
		
		String aa="08";
		System.err.println(Integer.valueOf(aa));
		
	}
}
