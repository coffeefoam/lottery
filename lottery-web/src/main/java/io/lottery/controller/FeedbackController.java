package io.lottery.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.lottery.common.utils.IPUtils;
import io.lottery.common.utils.R;
import io.lottery.entity.FeedbackEntity;
import io.lottery.service.FeedbackService;

/**
 * @author xg
 * @email
 * @date 2017-03-23 15:47
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {
	
	@Autowired 
	FeedbackService feedbackService;
	
	//防止无限制插入数据
	private static Map<String,Long> timeMap =  new HashMap<String,Long>();

	/**
	 * @desc 插入反馈
	 * @author xg
	 * @author 2018年7月16日
	 */
	@RequestMapping("/insertContent")
    public R insertContent(String content,HttpServletRequest request){
		if(StringUtils.isNotBlank(content)) {
			FeedbackEntity feedbackEntity = new FeedbackEntity();
			feedbackEntity.setContent(content);
			Long currenttimes=System.currentTimeMillis();
			String ip = IPUtils.getIpAddr(request);
			if(timeMap.get(ip)==null) {
				feedbackService.insertContent(feedbackEntity);
				timeMap.put(ip, currenttimes);
			}else {
				if(currenttimes-timeMap.get(ip)>30*1000) {
					feedbackService.insertContent(feedbackEntity);
					timeMap.put(ip, currenttimes);
				}else {
					return R.error("频繁提交反馈的间隔时间为30秒，请稍后再试");
				}
			}
			//剔除当前时间30秒之前的数据
			this.clearTimeMap(currenttimes);
			return R.ok();
		}else {
			return R.error("反馈内容不能为空");
		}
    }
	
	
	/**
	 * 剔除当前时间30秒之前的数据
	* @author ms
	* @date 2018年8月27日 上午10:31:06
	 */
	private void clearTimeMap(Long currenttimes) {
		Iterator<Map.Entry<String, Long>> it = timeMap.entrySet().iterator();
		while (it.hasNext()) {
		    if(currenttimes- it.next().getValue()>30*1000) {
		    	it.remove();
			}
		}
	}
	
}
