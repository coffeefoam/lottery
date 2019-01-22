package io.lottery.service;

import io.lottery.entity.FeedbackEntity;

/**
 * @desc 反馈业务
 * @author xg
 * @author 2018年7月16日
 */
public interface FeedbackService   {

	/**
	 * @desc 插入反馈
	 * @author xg
	 * @param feedbackEntity
	 * @return
	 * @author 2018年7月16日
	 */
	int insertContent(FeedbackEntity feedbackEntity);

}

