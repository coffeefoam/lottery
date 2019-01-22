package io.lottery.modules.operation.service.impl;

import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.Query;

import io.lottery.modules.operation.dao.OpFeedbackDao;
import io.lottery.modules.operation.entity.OpFeedbackEntity;
import io.lottery.modules.operation.service.OpFeedbackService;


@Service("opFeedbackService")
public class OpFeedbackServiceImpl extends ServiceImpl<OpFeedbackDao, OpFeedbackEntity> implements OpFeedbackService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        Page<OpFeedbackEntity> page = this.selectPage(
                new Query<OpFeedbackEntity>(params).getPage(),
                new EntityWrapper<OpFeedbackEntity>().orderBy("createtime", false)
        );

        return new PageUtils(page);
    }

}
