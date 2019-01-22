package com.boying.cpapi.mapper.idata;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

/**
 * 
 * @desc lottery mapper类
 * @author abo
 * @date 2018年6月23日 下午4:04:57
 *
 */
public interface LotteryMapper {

	/**
	 * 
	 * @desc 按照彩票类型插入不同的彩票表
	 * @author abo
	 * @date 2018年6月23日 下午4:04:33
	 *
	 */
	@InsertProvider(type = LotterySqlFactory.class, method = "insertLottery")
	void insertByMap(Map<String, Object> lotteryMap);

	/**
	 * 
	 * @desc 批量彩票开奖结果数据
	 * @author abo
	 * @date 2018年6月23日 下午8:10:27
	 *
	 */
	@InsertProvider(type = LotterySqlFactory.class, method = "batchInsertByLottery")
	void batchInsertByListmap(@Param("list") List<Map<String, Object>> list, @Param("tableName") String tableName, @Param("insertNum") String insertNum);

	/**
	 * 
	 * @desc 根据期数查询是否存在
	 * @author abo
	 * @date 2018年7月10日 下午5:12:21
	 * @param table
	 * @param periods
	 * @return
	 */
	@Select(" SELECT count(1) FROM ${table} WHERE periods = ${periods} ")
	int findCountByPeriods(@Param("table") String table, @Param("periods") String periods);

	@Update(" update  ${table} set processtime = '${processtime}' WHERE periods = ${periods} ")
	int updateProcessTimeByPeriods(@Param("table") String table, @Param("periods") String periods, @Param("processtime") String processtime);

	/**
	 * 查询用户信息
	 * 
	 * @param username
	 * @return
	 */
	@Select("SELECT * from sys_user u WHERE u.username=#{username}")
	Map<String, Object> getUser(@Param("username") String username);
	
	
	@Select(" SELECT count(1) FROM ${table} WHERE periods = #{periods} AND DATE_FORMAT(time,'%Y-%m-%d') = #{time}")
	int findCountByTimePeriods(@Param("table") String table, @Param("periods") String periods,@Param("time") String time);	
	
	@Select(" SELECT count(1) FROM ${table} ")
	int findCount(@Param("table") String table);

	@Select("SELECT * FROM ${table} ORDER BY id DESC LIMIT ${start},${limit}")
	List<Map<String, Object>> findPage(@Param("table") String table,@Param("start") int start,@Param("limit") int limit);
	
	@Update("update ${table} set starttime = '${starttime}' WHERE periods = ${periods} AND DATE_FORMAT(time,'%Y-%m-%d') = #{time}")
	int updateProcessStarttimeByTimePeriods(@Param("table") String table, @Param("periods") String periods,@Param("time") String time, @Param("starttime") String starttime);


}