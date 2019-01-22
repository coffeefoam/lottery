package io.lottery.controller;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.lottery.common.utils.R;
import io.lottery.service.ChatroomApiService;

/**
 * 对外提供的接口
 * 
 * @desc
 * @author xg
 * @author 2018年9月17日
 */
@RestController
@RequestMapping("/api/chatroom")
public class ChatroomApiController {

	private Logger logger = LoggerFactory.getLogger(ChatroomApiController.class);

	@Autowired
	private ChatroomApiService chatroomApiService;

	/**
	 * 聊天室pk10开奖记录数据
	 * 
	 * @desc
	 * @author xg
	 * @return
	 * @author 2018年9月17日
	 */

	@RequestMapping("/jsonarray")
	public R jsonarray(String code) {
		try {
			if (StringUtils.isBlank(code)) {
				return R.error("code不能为空");
			}
			Object obj = null;
			if (code.contains("kjjl")) {
				obj = chatroomApiService.kjjl(code);
			}
			return R.okwithdata(obj);
		} catch (Exception e) {
			logger.error("接口出错:{}", e);
			return R.error("接口出错");
		}
	}

}
