package io.lottery.modules.operation.service;

import com.baomidou.mybatisplus.service.IService;
import io.lottery.common.utils.PageUtils;
import io.lottery.modules.operation.entity.OpFeedbackEntity;

import java.util.Map;

/**
 * 用户反馈
 *
 * @author 
 * @email 
 * @date 2018-07-16 20:08:38
 */
public interface OpFeedbackService extends IService<OpFeedbackEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

