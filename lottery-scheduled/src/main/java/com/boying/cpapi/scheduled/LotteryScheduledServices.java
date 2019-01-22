package com.boying.cpapi.scheduled;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.boying.cpapi.service.LotteryService;
import com.boying.cpapi.util.DateUtil;
import com.boying.cpapi.util.TimeDict;

import lombok.extern.slf4j.Slf4j;

/**
 * 
 * Description:定时器
 *
 * @author abo
 * @date 2018年5月2日
 */
@Slf4j
@Component
public class LotteryScheduledServices {

	@Autowired
	private LotteryService lotteryService;

	/**
	 * 5秒请求一次开奖数据
	 */
	@Scheduled(fixedDelay = TimeDict.Five_Seconds)
	public void getLotteryData() {
		lotteryService.getLotteryData();
	}

	/**
	 * 六合彩按照时间段请求数据
	 */
	@Scheduled(cron = "0 0/1 21 * * ?")
	public void getLhcData() {
		lotteryService.getLhcData();
	}

	/**
	 * @desc 根据日期来补齐当天当前时间以前的记录
	 * @author abo
	 * @date 2018年6月23日 下午6:31:09
	 */
	@Scheduled(fixedDelay = TimeDict.Ten_Minutes)
	public void getLotteryDataByDay() {
		// 当前时间
		String currentDate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
		lotteryService.getLotteryDataByDayAndExecute(currentDate);
		System.out.println("do complate");
	}

	/**
	 * @desc 根据当前月来获取所有记录
	 * @author abo
	 * @date 2018年6月25日 下午2:42:21
	 */
	// @Scheduled(fixedDelay = TimeDict.Ten_Minutes)
	public void getLotteryDataByCurrentMonth() {
		String currentDate = DateUtil.geCurrentDate(DateUtil.PATTERN_DATE);
		// 获取当前月的总天数
		int monthDay = Integer.parseInt(currentDate.substring(currentDate.length() - 2, currentDate.length()));
		// 截取年月字符串例如2018-06-02截取成2018-06
		currentDate = currentDate.substring(0, 7);
		for (int i = 1; i <= monthDay; i++) {
			String day = currentDate + "-" + i;
			lotteryService.getLotteryDataByDay(day);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.info(day + "抽取完成=========");
		}
	}

	/**
	 * 
	 * @desc 根据当某个日期查询某当月的所有开奖记录
	 * @author abo
	 * @date 2018年6月25日 下午2:42:21
	 */
	// @Scheduled(fixedDelay = TimeDict.Ten_Minutes)
	public void getLotteryDataByDateStr() {
		String currentDate = "2018-06-01";
		// 获取当前月的总天数
		int monthDay = DateUtil.currentDateStrMonthDays(currentDate);
		// 截取年月字符串例如2018-06-02截取成2018-06
		currentDate = currentDate.substring(0, 7);
		for (int i = 1; i <= monthDay; i++) {
			String day = currentDate + "-" + i;
			lotteryService.getLotteryDataByDay(day);
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			log.info(day + "抽取完成=========");
		}
	}
}
