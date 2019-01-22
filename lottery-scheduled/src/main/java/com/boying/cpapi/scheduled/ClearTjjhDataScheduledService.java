package com.boying.cpapi.scheduled;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.boying.cpapi.config.RedisService;
import com.boying.cpapi.service.pdata.LotteryBjscAndXyftService;
import com.boying.cpapi.service.pdata.LotterySscService;
import com.boying.cpapi.service.tjdata.LotteryAppJjshService;
import com.boying.cpapi.service.tjdata.LotteryAppTjjhService;
import com.boying.cpapi.util.LotteryConstant;

import io.lottery.common.config.LotteryDict;

/**
 * @ClassName: ClearTjjhDataScheduledService
 * @Description: 定时删除推荐计划的数据
 * @author 老王
 * @date 2018年8月7日 下午5:52:56
 *
 */
@Component
public class ClearTjjhDataScheduledService {

	@Resource
	private RedisService redisservice;

	@Autowired
	private LotteryAppJjshService lotteryAppJjshService;

	@Autowired
	private LotteryAppTjjhService lotteryAppTjjhService;
	@Autowired
	private LotterySscService lotterySscService;
	@Autowired
	private LotteryBjscAndXyftService lotteryBjscAndXyftService;

	// 每天凌晨一点执行
	@Scheduled(cron = "0 0 3 * * ? ")
	public void clearLotteryData() {
		redisservice.remove(LotteryConstant.BJSC + "_" + LotteryDict.tjjh);
		lotteryBjscAndXyftService.recommend(LotteryConstant.BJSC );
	}
	
	// 每天凌晨0点2分执行
	@Scheduled(cron = "0 2 0 * * ? ")
	public void clearCqsscLotteryData() {
		redisservice.remove(LotteryConstant.CQSSC + "_" + LotteryDict.hmtjjh);
		redisservice.remove(LotteryConstant.CQSSC + "_" + LotteryDict.lmtjjh);
		lotteryAppTjjhService.execute(LotteryConstant.CQSSC);
		
		redisservice.remove(LotteryConstant.CQSSC + "_" + LotteryDict.tjjh);
		lotterySscService.recommend(LotteryConstant.CQSSC);
	}

	// 每天凌晨5点执行
	@Scheduled(cron = "0 0 5 * * ? ")
	// @Scheduled(fixedDelay = TimeDict.One_Minute)
	public void getXyft() {
		redisservice.remove(LotteryConstant.XYFT + "_" + LotteryDict.tjjh);
		lotteryBjscAndXyftService.recommend(LotteryConstant.XYFT );
		
		// 顺道清除app的阻击杀号数据,北京赛车、幸运飞艇、重庆时时彩
		redisservice.remove(LotteryConstant.BJSC + "_" + LotteryDict.jjsh);
		redisservice.remove(LotteryConstant.XYFT + "_" + LotteryDict.jjsh);
		redisservice.remove(LotteryConstant.CQSSC + "_" + LotteryDict.jjsh);
		// 清理完以后然后产生每天的第一条
		lotteryAppJjshService.execute(LotteryConstant.BJSC);
		lotteryAppJjshService.execute(LotteryConstant.XYFT);
		lotteryAppJjshService.execute(LotteryConstant.CQSSC);
		System.out.println("执行推荐计划清理数据完成");

		// 清除app的号码推荐计划数据,北京赛车、幸运飞艇、重庆时时彩、广东快乐十分
		redisservice.remove(LotteryConstant.BJSC + "_" + LotteryDict.hmtjjh);
		redisservice.remove(LotteryConstant.XYFT + "_" + LotteryDict.hmtjjh);
		redisservice.remove(LotteryConstant.GDKLSF + "_" + LotteryDict.hmtjjh);

		// 清除app的号码推荐计划数据,北京赛车、幸运飞艇、重庆时时彩、广东快乐十分
		redisservice.remove(LotteryConstant.BJSC + "_" + LotteryDict.lmtjjh);
		redisservice.remove(LotteryConstant.XYFT + "_" + LotteryDict.lmtjjh);
		redisservice.remove(LotteryConstant.GDKLSF + "_" + LotteryDict.lmtjjh);
		// 清理完以后然后产生每天的第一条
		lotteryAppTjjhService.execute(LotteryConstant.BJSC);
		lotteryAppTjjhService.execute(LotteryConstant.XYFT);
		lotteryAppTjjhService.execute(LotteryConstant.GDKLSF);
		System.out.println("执行推荐计划清理数据完成");
		
	}

}
