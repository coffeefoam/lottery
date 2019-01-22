package com.boying.cpapi.controller;

import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.boying.cpapi.config.MyProps;
import com.boying.cpapi.service.AbstractServices;
import com.boying.cpapi.service.LotteryService;

import io.lottery.common.utils.EncryptUtils;
import io.lottery.common.utils.R;

/**
 * 可能用到的接口
 * 
 * @author R6
 *
 */
@RestController
@RequestMapping("/api")
public class ApiController {

	private Logger logger = LoggerFactory.getLogger(ApiController.class);

	@Autowired
	private AbstractServices abstractServices;

	@Autowired
	private LotteryService lotteryService;

	@Autowired
	private MyProps myprops;

	@RequestMapping("/rebuild")
	public R rebuild(String param) {
		try {
			// 解密
			String key = myprops.getEncrypt().get("key");
			String param2 = EncryptUtils.decryptStr(param, key);
			JSONObject obj = JSON.parseObject(param2);

			String date = obj.getString("date");
			String username = obj.getString("username");
			String pwd = obj.getString("pwd");

			if (StringUtils.isBlank(pwd)) {
				return R.error("密码不能为空");
			}
			// 验证用户账号密码
			Map<String, Object> userMap = lotteryService.getUser(username);
			if (userMap == null) {
				return R.error("用户信息不存在");
			}

			// 验证密码
			String password = String.valueOf(userMap.get("password"));
			if (pwd.equals(password)) {
				if (AbstractServices.REBUILD_DATA_IS_RUNNING) {
					return R.error("正在执行生成数据任务，请稍后重试");
				} else {
					// 异步执行
					new Thread(new Runnable() {
						public void run() {
							abstractServices.rebuild(date);
						}
					}).start();
					return R.ok("执行任务已经提交，稍后将完成...");
				}

			} else {
				return R.error("用户密码出错");
			}
		} catch (Exception e) {
			logger.error("生成数据出错:{}", e);
			return R.error("生成数据出错");
		}
	}

	/**
	 * 重新抽取数据
	 * 
	 * @desc
	 * @author lj
	 * @param param
	 * @return
	 * @author 2018年9月22日
	 */
	@RequestMapping("/getDataBydate")
	public R getDataBydate(String param) {
		try {
			// 解密
			String key = myprops.getEncrypt().get("key");
			String param2 = EncryptUtils.decryptStr(param, key);
			JSONObject obj = JSON.parseObject(param2);

			String date = obj.getString("date");
			String username = obj.getString("username");
			String pwd = obj.getString("pwd");

			if (StringUtils.isBlank(pwd)) {
				return R.error("密码不能为空");
			}
			// 验证用户账号密码
			Map<String, Object> userMap = lotteryService.getUser(username);
			if (userMap == null) {
				return R.error("用户信息不存在");
			}

			// 验证密码
			String password = String.valueOf(userMap.get("password"));
			if (pwd.equals(password)) {
				if (AbstractServices.REBUILD_DATA_IS_RUNNING) {
					return R.error("正在执行从接口抓取数据，请稍后重试");
				} else {
					// 异步执行
					new Thread(new Runnable() {
						public void run() {
							lotteryService.getLotteryDataByDayAndExecute(date);
						}
					}).start();
					return R.ok("执行任务已经提交，稍后将完成...");
				}
			} else {
				return R.error("用户密码出错");
			}
		} catch (Exception e) {
			logger.error("从接口抓取数据出错:{}", e);
			return R.error("从接口抓取数据出错");
		}
	}
}
