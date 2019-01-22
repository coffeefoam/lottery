package io.lottery.modules.operation.controller;

import io.lottery.common.utils.DateUtils;
import io.lottery.common.utils.EncryptUtils;
import io.lottery.common.utils.Global;
import io.lottery.common.utils.HttpUtils;
import io.lottery.common.utils.R;
import io.lottery.modules.sys.controller.AbstractController;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 调度管理类
 * 
 * @author R6
 *
 */
@RestController
@RequestMapping("/scheduledManager")
public class ScheduledManagerController extends AbstractController {

	private Logger log = LoggerFactory.getLogger(ScheduledManagerController.class);

	/**
	 * 信息
	 */
	@RequestMapping("/rebuild")
	@RequiresPermissions("operation:scheduledManager:rebuild")
	public R rebuild(String date) {
		try {
			// 校验日期格式
			if (StringUtils.isBlank(date)) {
				return R.error("日期不能为空");
			}
			if (!DateUtils.isValidDate(date)) {
				return R.error("日期格式错误");
			}
			String url = Global.getConfig("myserver.url.scheduled") + "/api/rebuild";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("date", date);
			param.put("username", getUser().getUsername());
			param.put("pwd", getUser().getPassword());

			String paramStr = JSON.toJSONString(param);
			// 加密
			paramStr = EncryptUtils.encryptStr(paramStr, Global.getConfig("encrypt.key"));
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("param", paramStr);

			String respStr = HttpUtils.executePost(url, paramMap);
			JSONObject json = JSON.parseObject(respStr);

			Integer code = json.getInteger("code");
			String msg = json.getString("msg");
			if (code == 0) {
				return R.ok(msg);
			} else {
				return R.error(msg);
			}
		} catch (IOException e) {
			log.error("调用接口出错:{}", e);
		}
		return R.error("手动重新生成数据失败");
	}
	
	/**
	 * 从接口重新获取数据
	 * @desc
	 * @author lj
	 * @param date
	 * @return
	 * @author 2018年9月22日
	 */
	@RequestMapping("/getDataBydate")
	@RequiresPermissions("operation:scheduledManager:rebuild")
	public R getDataBydate(String date) {
		try {
			// 校验日期格式
			if (StringUtils.isBlank(date)) {
				return R.error("日期不能为空");
			}
			if (!DateUtils.isValidDate(date)) {
				return R.error("日期格式错误");
			}
			String url = Global.getConfig("myserver.url.scheduled") + "/api/getDataBydate";
			Map<String, Object> param = new HashMap<String, Object>();
			param.put("date", date);
			param.put("username", getUser().getUsername());
			param.put("pwd", getUser().getPassword());

			String paramStr = JSON.toJSONString(param);
			// 加密
			paramStr = EncryptUtils.encryptStr(paramStr, Global.getConfig("encrypt.key"));
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("param", paramStr);

			String respStr = HttpUtils.executePost(url, paramMap);
			JSONObject json = JSON.parseObject(respStr);

			Integer code = json.getInteger("code");
			String msg = json.getString("msg");
			if (code == 0) {
				return R.ok(msg);
			} else {
				return R.error(msg);
			}
		} catch (IOException e) {
			log.error("调用接口出错:{}", e);
		}
		return R.error("重新获取数据失败");
	}

}
