package io.lottery.modules.cms.service;

import io.lottery.common.utils.PageUtils;
import io.lottery.modules.cms.entity.CmsCategoryEntity;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

/**
 * 栏目表
 *
 * @author
 * @email
 * @date 2018-07-04 21:11:40
 */
public interface CmsCategoryService extends IService<CmsCategoryEntity> {

	PageUtils queryPage(Map<String, Object> params);

	List<CmsCategoryEntity> findFrontList();
}
