package io.lottery.dao;

import io.lottery.entity.FeedbackEntity;

public interface FeedbackDao {
	
	/**
	 * @desc 插入反馈
	 * @author xg
	 * @param feedbackEntity
	 * @return
	 * @author 2018年7月16日
	 */
	int insertContent(FeedbackEntity feedbackEntity);
	
}
