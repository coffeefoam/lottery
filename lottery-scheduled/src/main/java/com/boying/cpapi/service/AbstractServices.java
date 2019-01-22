package com.boying.cpapi.service;

import java.text.DecimalFormat;
import java.util.Map.Entry;

import org.apache.commons.lang3.time.StopWatch;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.boying.cpapi.service.cldata.LotteryClService;
import com.boying.cpapi.service.pdata.Lottery11x5Service;
import com.boying.cpapi.service.pdata.LotteryBjscAndXyftService;
import com.boying.cpapi.service.pdata.LotteryCommonService;
import com.boying.cpapi.service.pdata.LotteryGdklsfService;
import com.boying.cpapi.service.pdata.LotteryJsk3Service;
import com.boying.cpapi.service.pdata.LotteryKl8Service;
import com.boying.cpapi.service.pdata.LotteryPcddService;
import com.boying.cpapi.service.pdata.LotterySscService;
import com.boying.cpapi.service.pdata.LotteryXyncService;
import com.boying.cpapi.service.tjdata.LotteryAppJjshService;
import com.boying.cpapi.service.tjdata.LotteryAppTjjhService;
import com.boying.cpapi.service.zsdata.LotteryAppSjdbService;
import com.boying.cpapi.service.zsdata.LotteryBjscftZstService;
import com.boying.cpapi.service.zsdata.LotteryJsk3ZstService;
import com.boying.cpapi.service.zsdata.LotteryZstCommonService;
import com.boying.cpapi.util.LotteryConstant;

import io.lottery.common.exception.RRException;
import io.lottery.common.utils.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AbstractServices {
	@Autowired
	private LotteryCommonService commonService;
	@Autowired
	private LotterySscService sscService;
	@Autowired
	private LotteryBjscAndXyftService bjscAndXyftService;
	@Autowired
	private Lottery11x5Service x11x5Service;
	@Autowired
	private LotteryGdklsfService gdklsfService;
	@Autowired
	private LotteryKl8Service kl8Service;
	@Autowired
	private LotteryJsk3Service jsk3Service;
	@Autowired
	private LotteryXyncService xyncService;
	@Autowired
	private LotteryClService clService;
	@Autowired
	private LotteryZstCommonService lotteryZstCommonService;// 走势图公用service
	@Autowired
	private LotteryJsk3ZstService lotteryJsk3ZstService;// 江苏快3走势图service
	@Autowired
	private LotteryBjscftZstService lotteryBjscftZstService;// 北京赛车、幸运飞艇走势图service
	@Autowired
	private LotteryAppSjdbService lotteryAppSjdbService;
	@Autowired
	private LotteryAppJjshService lotteryAppJjshService;
	@Autowired
	private LotteryAppTjjhService lotteryAppTjjhService;

	@Autowired
	private LotteryPcddService lotteryPcddService;
	@Autowired
	private PushDataServices pushDataServices;

	private DecimalFormat df = new DecimalFormat("0.00");// 格式化小数

	public static boolean REBUILD_DATA_IS_RUNNING = false;// 是否正在重建数据

	/**
	 * 针对单一彩种生成接口数据
	 * 
	 * @author ms
	 * @date 2018年7月11日 下午3:02:29
	 */
	public void handle(String lottId) {
		StopWatch sw = new StopWatch();
		sw.start();
		// 公共业务都执行
		try {
			// 返回每个彩种限制条数的期数
			commonService.makeLimitPeriods(lottId);
			// 返回每个彩种当天的所有期数
			commonService.makePeriodsByDate(lottId, null);
			commonService.execute(lottId);
			// 推荐计划之号码计划，北京赛车，幸运飞艇，重庆时时彩，广东快乐十分
			if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)
					|| lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.GDKLSF)) {
				lotteryAppTjjhService.execute(lottId);
			}
		} catch (RRException e) {
			e.printStackTrace();
			log.error(ExceptionUtil.getExceptionStackTrace(e));
		}
		try {
			if (LotteryConstant.CQSSC.equals(lottId) || LotteryConstant.TJSSC.equals(lottId)
					|| LotteryConstant.XJSSC.equals(lottId)) {
				sscService.execute(lottId);
			} else if (lottId.equals(LotteryConstant.GD11X5) || lottId.equals(LotteryConstant.JX11X5)) {
				x11x5Service.execute(lottId);
			} else if (LotteryConstant.BJSC.equals(lottId) || LotteryConstant.XYFT.equals(lottId)) {
				bjscAndXyftService.execute(lottId);
				lotteryBjscftZstService.execute(lottId);// 走势图执行service
			} else if (LotteryConstant.GDKLSF.equals(lottId)) {
				gdklsfService.execute(lottId);
			} else if (LotteryConstant.XYNC.equals(lottId)) {
				xyncService.execute(lottId);
			} else if (LotteryConstant.JSK3.equals(lottId)) {
				jsk3Service.execute(lottId);
				lotteryJsk3ZstService.execute();// 走势图执行service
			} else if (LotteryConstant.KL8.equals(lottId)) {
				kl8Service.execute(lottId);
			} else if (lottId.equals(LotteryConstant.PCDD)) {
				lotteryPcddService.execute(lottId);
			}
			// 处理长龙(快3、快乐8 暂不做长龙统计业务)
			if (!LotteryConstant.KL8.equals(lottId) && !LotteryConstant.JSK3.equals(lottId)) {
				clService.execute(lottId);
			}
			// 走势图执行service
			lotteryZstCommonService.execute(lottId);
			// 生成当天app前端数据对比接口数据
			lotteryAppSjdbService.execute(lottId);
			// app狙击杀号,北京赛车、幸运飞艇、重庆时时彩才有
			if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)
					|| lottId.equals(LotteryConstant.CQSSC)) {
				lotteryAppJjshService.execute(lottId);
			}
			//推送数据到MQ服务器
			pushDataServices.pushDataToRabbitMQ(lottId);
			log.info("彩种:" + lottId + "作业执行完毕！" + "执行时间：" + df.format(sw.getTime() / 1000) + "s");
		} catch (RRException e) {
			e.printStackTrace();
			log.error(ExceptionUtil.getExceptionStackTrace(e));
		}
	}

	/**
	 * 重新生成开奖记录和两面投注参考
	 * 
	 * @param date
	 */
	public void rebuild(String date) {
		try {
			long start = System.currentTimeMillis();

			REBUILD_DATA_IS_RUNNING = true;// 正在跑
			// 开奖记录
			sscService.getKjjl(LotteryConstant.CQSSC, date);
			sscService.getKjjl(LotteryConstant.XJSSC, date);
			sscService.getKjjl(LotteryConstant.TJSSC, date);
			x11x5Service.getKjjl(LotteryConstant.GD11X5, date);
			x11x5Service.getKjjl(LotteryConstant.JX11X5, date);

			bjscAndXyftService.getKjjl(LotteryConstant.BJSC, date);
			bjscAndXyftService.getKjjl(LotteryConstant.XYFT, date);

			gdklsfService.getKjjl(LotteryConstant.GDKLSF, date);
			xyncService.getKjjl(LotteryConstant.XYNC, date);
			jsk3Service.getKjjl(LotteryConstant.JSK3, date);
			kl8Service.getKjjl(LotteryConstant.KL8, date);
			lotteryPcddService.getKjjl(LotteryConstant.PCDD, date);

			// 两面投注参考
			for (Entry<String, Integer> map : LotteryConstant.lotteryNumMap.entrySet()) {
				String lottId = map.getKey();
				if (LotteryConstant.BJSC.equals(lottId)) {
					bjscAndXyftService.getLmtzck(lottId, date);
				} else {
					commonService.getLmtzck(lottId, date);
				}
				// 返回每个彩种当天的所有期数
				commonService.makePeriodsByDate(lottId, date);
			}

			REBUILD_DATA_IS_RUNNING = false;// 生成数据已经跑完

			long end = System.currentTimeMillis();// 结束时间
			log.info("手动执行数据生成耗费时间:" + (end - start));
		} catch (Exception e) {
			REBUILD_DATA_IS_RUNNING = false;
			log.error("手动生成数据出错:{}", e);
		}
	}

}
