package io.lottery.modules.lottery.service;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.service.IService;

import io.lottery.common.utils.PageUtils;
import io.lottery.modules.lottery.entity.LiuheEntity;

/**
 * 六合彩开奖数据
 *
 * @author
 * @email
 * @date 2018-09-08 08:46:46
 */
public interface LiuheService extends IService<LiuheEntity> {

	PageUtils queryPage(Map<String, Object> params);

	LiuheEntity getByTimeAndQishu(LiuheEntity liuheEntity);

	/**
	 * 查询最新100期记录
	 */
	List<LiuheEntity> query100List();

	/**
	 * 生成最新100期号码走势
	 */
	void hmzs(List<LiuheEntity> list);

	/**
	 * 生成最新100期特码波色走势
	 */
	void tm_bszs(List<LiuheEntity> list);

	/**
	 * 生成最新100期特码生肖走势
	 */
	void tm_sxzs(List<LiuheEntity> list);

	/**
	 * 生成最新100期特码合数走势
	 */
	void tm_dszs(List<LiuheEntity> list);

	/**
	 * 生成最新100期特码段位走势
	 */
	void tm_dwzs(List<LiuheEntity> list);

	/**
	 * 生成最新100期特码头数走势
	 */
	void tm_tszs(List<LiuheEntity> list);

	/**
	 * 生成最新100期特码尾数走势
	 */
	void tm_wszs(List<LiuheEntity> list);

	/**
	 * 生成最新100期特码五行走势
	 */
	void tm_wxzs(List<LiuheEntity> list);

	void genKjjl();

	LiuheEntity getTheLastOne();

	void getKjData();

	void execute();

	void lotteryVideo();
}
