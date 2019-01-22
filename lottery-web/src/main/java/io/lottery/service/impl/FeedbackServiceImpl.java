package io.lottery.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import io.lottery.dao.FeedbackDao;
import io.lottery.entity.FeedbackEntity;
import io.lottery.service.FeedbackService;

@Service
public class FeedbackServiceImpl implements FeedbackService {
	
	@Autowired
	FeedbackDao feedbackDao;

	@Override
	public int insertContent(FeedbackEntity feedbackEntity) {
		return feedbackDao.insertContent(feedbackEntity);
	}

}
