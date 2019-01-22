package io.lottery.modules.cms.service;

import io.lottery.common.utils.PageUtils;
import io.lottery.modules.cms.entity.CmsConfigEntity;

import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * cms内容系统配置信息
 *
 * @author
 * @email
 * @date 2018-07-14 14:36:34
 */
public interface CmsConfigService extends IService<CmsConfigEntity> {

	PageUtils queryPage(Map<String, Object> params);
}
