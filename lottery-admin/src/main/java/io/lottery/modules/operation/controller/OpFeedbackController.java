package io.lottery.modules.operation.controller;

import java.util.Arrays;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.lottery.modules.operation.entity.OpFeedbackEntity;
import io.lottery.modules.operation.service.OpFeedbackService;
import io.lottery.common.utils.PageUtils;
import io.lottery.common.utils.R;



/**
 * 用户反馈
 *
 * @author 
 * @email 
 * @date 2018-07-16 20:08:38
 */
@RestController
@RequestMapping("operation/opfeedback")
public class OpFeedbackController {
    @Autowired
    private OpFeedbackService opFeedbackService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("operation:opfeedback:list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = opFeedbackService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    @RequiresPermissions("operation:opfeedback:info")
    public R info(@PathVariable("id") Integer id){
			OpFeedbackEntity opFeedback = opFeedbackService.selectById(id);

        return R.ok().put("opFeedback", opFeedback);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @RequiresPermissions("operation:opfeedback:save")
    public R save(@RequestBody OpFeedbackEntity opFeedback){
			opFeedbackService.insert(opFeedback);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    @RequiresPermissions("operation:opfeedback:update")
    public R update(@RequestBody OpFeedbackEntity opFeedback){
			opFeedbackService.updateById(opFeedback);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @RequiresPermissions("operation:opfeedback:delete")
    public R delete(@RequestBody Integer[] ids){
			opFeedbackService.deleteBatchIds(Arrays.asList(ids));

        return R.ok();
    }

}
