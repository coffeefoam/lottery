package io.lottery.modules.lottery.dao;

import java.util.List;

import com.baomidou.mybatisplus.mapper.BaseMapper;

import io.lottery.modules.lottery.entity.LiuheEntity;

/**
 * 六合彩开奖数据
 * 
 * @author
 * @email
 * @date 2018-09-08 08:46:46
 */
public interface LiuheDao extends BaseMapper<LiuheEntity> {

	LiuheEntity getByTimeAndQishu(LiuheEntity liuheEntity);

	LiuheEntity getTheLastOne();

	List<String> getYears();

}
