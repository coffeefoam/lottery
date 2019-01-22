package io.lottery.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import io.lottery.common.config.RedisService;
import io.lottery.service.ChatroomApiService;

/**
 * 聊天室
 * 
 * @desc
 * @author xg
 * @author 2018年9月17日
 */
@Service
public class ChatroomApiServiceImpl implements ChatroomApiService {

	@Autowired
	private RedisService redisservice;

	@Override
	public Object kjjl(String code) {
		Map<Object, Object> map = redisservice.getHashMapsByLimit(code,80);
		List<Map<String, Object>> listmap = new ArrayList<>();
		if (map != null) {
			for (Entry<Object, Object> entry : map.entrySet()) {
				JSONObject obj = JSON.parseObject(String.valueOf(entry.getValue()));
				JSONArray arr = obj.getJSONArray("rank");
				List<Integer> rank = new ArrayList<Integer>();
				List<String> dxranklist = new ArrayList<String>();
				List<String> dsranklist = new ArrayList<String>();
				List<Integer> duiziList = new ArrayList<Integer>();
				if (arr != null && arr.size() > 0) {
					for (Object o : arr) {
						Integer num = Integer.valueOf(String.valueOf(o));
						rank.add(num);
						// 大小
						dxranklist.add(num < 6 ? "小" : "大");
						// 单双
						dsranklist.add(num % 2 == 0 ? "双" : "单");
						// 设置对子初值
						duiziList.add(0);
					}
				}
				Map<String, Object> map2 = new HashMap<String, Object>();
				map2.put("periods", obj.get("periods"));
				map2.put("starttime", obj.get("starttime"));
				map2.put("rank", rank);
				map2.put("dxrank", dxranklist);
				map2.put("dsrank", dsranklist);
				map2.put("dzrank", duiziList);

				listmap.add(map2);
			}
			// 计算对子
			for (int i = 0; i < listmap.size(); i++) {
				if (i != 0) {
					Map<String, Object> curr = listmap.get(i);
					Map<String, Object> prev = listmap.get(i - 1);

					@SuppressWarnings("unchecked")
					List<Integer> currRank = (List<Integer>) curr.get("rank");
					@SuppressWarnings("unchecked")
					List<Integer> prevRank = (List<Integer>) prev.get("rank");

					@SuppressWarnings("unchecked")
					List<Integer> currDzRank = (List<Integer>) curr.get("dzrank");
					@SuppressWarnings("unchecked")
					List<Integer> prevDzRank = (List<Integer>) prev.get("dzrank");

					// 球数
					int ballNum = 0;
					if (code.contains("bjsc")) {
						ballNum = 10;
					} else if (code.contains("cqssc")) {
						ballNum = 5;
					}
					// 遍历球数，比对纵向前后两个球号码是否一样
					for (int j = 0; j < ballNum; j++) {
						if (currRank.get(j).equals(prevRank.get(j))) {
							currDzRank.set(j, 1);
							prevDzRank.set(j, 1);
						}
					}

				}
			}

		}

		return listmap;
	}

}
