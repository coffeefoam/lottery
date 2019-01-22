package io.lottery.modules.job.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.lottery.modules.lottery.service.LiuheService;

/**
 * 六合开奖任务
 * 
 * @desc
 * @author xg
 * @author 2018年9月12日
 */
@Component("liuheTask")
public class LiuheTask {

	private Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private LiuheService liuheService;

	/**
	 * 六合彩开奖查询
	 * 
	 * @desc
	 * @author xg
	 * @param params
	 * @author 2018年9月12日
	 */
	public void kaijiang(String params) {
		logger.info("我是带参数的kaijiang方法，正在被执行，参数为：" + params);
		liuheService.getKjData();

	}

}
