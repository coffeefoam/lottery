package com.boying.cpapi.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @desc 彩票常量类
 * @author abo
 * @date 2018年6月22日 下午4:19:30
 *
 */
public class LotteryConstant {

	public final static String BJSC = "bjsc";
	public final static String CQSSC = "cqssc";
	public final static String GD11X5 = "gd11x5";
	public final static String JX11X5 = "jx11x5";
	public final static String GDKLSF = "gdklsf";
	public final static String XYFT = "xyft";
	public final static String XYNC = "xync";
	public final static String SDC = "sdc";
	public final static String TJSSC = "tjssc";
	public final static String XJSSC = "xjssc";
	public final static String JSK3 = "jsk3";
	public final static String PCDD = "pcdd";
	public final static String KL8 = "kl8";
	public final static String LHC = "lhc";

	public final static String DA = "大";
	public final static String XIAO = "小";
	public final static String GYHDA = "和大";// 冠亚和大
	public final static String GYHXIAO = "和小";// 冠亚和小
	public final static String GYHDAN = "和单";// 冠亚和大
	public final static String GYHSHUANG = "和双";// 冠亚和小
	public final static String DAN = "单";
	public final static String SHUANG = "双";
	public final static String LONG = "龙";
	public final static String HU = "虎";
	public final static String HE = "和";
	public final static String HEDAN = "合单";
	public final static String HESHUANG = "合双";
	public final static String WXIAO = "尾小";
	public final static String WDA = "尾大";
	public final static String TC = "通吃";

	public final static String ZL = "杂六";
	public final static String SZ = "顺子";
	public final static String BZ = "豹子";
	public final static String DZ = "对子";
	public final static String BS = "半顺";
	public final static String ZHONG = "中";
	public final static String SHANG = "上";
	public final static String XIA = "下";

	public final static String JI = "奇";
	public final static String OU = "偶";

	public final static String HUO = "火";
	public final static String MU = "木";
	public final static String SHUI = "水";
	public final static String TU = "土";
	public final static String JIN = "金";

	public final static String QIAN = "前";
	public final static String HOU = "后";

	public final static String FAN = "反";
	public final static String ZHENG = "正";
	public final static String CHONG = "重";
	
	public final static String GREEN = "绿";
	public final static String GRAY = "灰";
	public final static String BLUE = "蓝";
	public final static String RED = "红";
	
	

	public final static int limitListSize = 80;// 限制返回的条数

	public final static int LZ_COLUMN_NUM = 55;// 路珠55列的常亮

	public static final Map<String, String> tableNameMap;
	public static final Map<String, Integer> lotteryNumMap;
	public static final Map<String, String> lotteryColumnStrMap;
	public static final Map<String, Integer> lotteryNumberRangeMap;
	public static final Map<String, Integer> lotteryBigBorderMap;
	public static final Map<String, Integer> lotteryIdOrderCode;
	public static final Map<String, Integer> lotteryTotalBorderMap;
	public static final Map<String, Integer> lotteryTotalPeriodMap;
	public static final Map<String, Object> lotteryShapeMap;
	public static final Map<String, Object> lotteryClurlMap;
	static {
		tableNameMap = new HashMap<String, String>();
		tableNameMap.put("bjsc", "t_bs_bjsc");
		tableNameMap.put("cqssc", "t_bs_cqssc");
		tableNameMap.put("gd11x5", "t_bs_gd11x5");
		tableNameMap.put("jx11x5", "t_bs_jx11x5");
		tableNameMap.put("gdklsf", "t_bs_gdklsf");
		tableNameMap.put("xyft", "t_bs_xyft");
		tableNameMap.put("xync", "t_bs_xync");
		// tableNameMap.put("sdc", "t_bs_sdc");//圣地彩暂时没有所以先注释掉
		tableNameMap.put("tjssc", "t_bs_tjssc");
		tableNameMap.put("xjssc", "t_bs_xjssc");
		tableNameMap.put("jsk3", "t_bs_jsk3");
		tableNameMap.put("pcdd", "t_bs_pcdd");// pc蛋蛋暂时没有所以先注释掉
		tableNameMap.put("kl8", "t_bs_kl8");
		tableNameMap.put("lhc", "t_bs_liuhe");//六合彩

		lotteryNumMap = new HashMap<String, Integer>();
		lotteryNumMap.put("bjsc", 10);
		lotteryNumMap.put("cqssc", 5);
		lotteryNumMap.put("gd11x5", 5);
		lotteryNumMap.put("jx11x5", 5);
		lotteryNumMap.put("gdklsf", 8);
		lotteryNumMap.put("xyft", 10);
		lotteryNumMap.put("xync", 8);
		// lotteryNumMap.put("sdc", 5);
		lotteryNumMap.put("tjssc", 5);
		lotteryNumMap.put("xjssc", 5);
		lotteryNumMap.put("jsk3", 3);
		lotteryNumMap.put("pcdd", 3);
		lotteryNumMap.put("kl8", 21);

		lotteryColumnStrMap = new HashMap<String, String>();
		lotteryColumnStrMap.put("bjsc", "num1,num2,num3,num4,num5,num6,num7,num8,num9,num10");
		lotteryColumnStrMap.put("cqssc", "num1,num2,num3,num4,num5");
		lotteryColumnStrMap.put("gd11x5", "num1,num2,num3,num4,num5");
		lotteryColumnStrMap.put("jx11x5", "num1,num2,num3,num4,num5");
		lotteryColumnStrMap.put("gdklsf", "num1,num2,num3,num4,num5,num6,num7,num8");
		lotteryColumnStrMap.put("xyft", "num1,num2,num3,num4,num5,num6,num7,num8,num9,num10");
		lotteryColumnStrMap.put("xync", "num1,num2,num3,num4,num5,num6,num7,num8");
		// lotteryColumnStrMap.put("sdc",
		// "num1,num2,num3,num4,num5,num6,num7,num8,num9,num10");//圣地彩暂时没有所以先注释掉
		lotteryColumnStrMap.put("tjssc", "num1,num2,num3,num4,num5");
		lotteryColumnStrMap.put("xjssc", "num1,num2,num3,num4,num5");
		lotteryColumnStrMap.put("jsk3", "num1,num2,num3");
		lotteryColumnStrMap.put("pcdd", "num1,num2,num3");// pc蛋蛋暂时没有所以先注释掉
		lotteryColumnStrMap.put("kl8", "num1,num2,num3,num4,num5,num6,num7,num8,num9,num10,num11,num12,num13,num14,num15,num16,num17,num18,num19,num20,num21");
		lotteryColumnStrMap.put("lhc", "num1,num2,num3,num4,num5,num6,num7");
		
		// 号码大小范围 后缀S为最小值B为最大值
		lotteryNumberRangeMap = new HashMap<String, Integer>();
		lotteryNumberRangeMap.put("bjsS", 1);
		lotteryNumberRangeMap.put("bjsB", 10);
		lotteryNumberRangeMap.put("cqsscS", 0);
		lotteryNumberRangeMap.put("cqsscB", 9);
		lotteryNumberRangeMap.put("gd11x5S", 1);
		lotteryNumberRangeMap.put("gd11x5B", 11);
		lotteryNumberRangeMap.put("jx11x5S", 1);
		lotteryNumberRangeMap.put("jx11x5B", 11);
		lotteryNumberRangeMap.put("gdklsfS", 1);
		lotteryNumberRangeMap.put("gdklsfB", 20);
		lotteryNumberRangeMap.put("xyftS", 1);
		lotteryNumberRangeMap.put("xyftB", 10);
		lotteryNumberRangeMap.put("xyncS", 1);
		lotteryNumberRangeMap.put("xyncB", 20);
		// lotteryNumberRangeMap.put("sdcS", 0);
		// lotteryNumberRangeMap.put("sdcB", 9);
		lotteryNumberRangeMap.put("tjsscS", 0);
		lotteryNumberRangeMap.put("tjsscB", 9);
		lotteryNumberRangeMap.put("xjsscS", 0);
		lotteryNumberRangeMap.put("xjsscB", 9);
		lotteryNumberRangeMap.put("jsk3S", 1);
		lotteryNumberRangeMap.put("jsk3B", 6);
		lotteryNumberRangeMap.put("pcddS", 0);
		lotteryNumberRangeMap.put("pcddB", 9);
		lotteryNumberRangeMap.put("kl8S", 1);
		lotteryNumberRangeMap.put("kl8B", 80);
		lotteryNumberRangeMap.put("bjscS", 1);
		lotteryNumberRangeMap.put("bjscB", 10);

		// 号码大号里最小的号
		lotteryBigBorderMap = new HashMap<String, Integer>();
		lotteryBigBorderMap.put("bjsc", 6);
		lotteryBigBorderMap.put("cqssc", 5);
		lotteryBigBorderMap.put("gd11x5", 6);
		lotteryBigBorderMap.put("jx11x5", 6);
		lotteryBigBorderMap.put("gdklsf", 11);
		lotteryBigBorderMap.put("xyft", 6);
		lotteryBigBorderMap.put("xync", 11);
		// lotteryBigBorderMap.put("sdc",5);//圣地彩暂时没有所以先注释掉
		lotteryBigBorderMap.put("tjssc", 5);
		lotteryBigBorderMap.put("xjssc", 5);
		lotteryBigBorderMap.put("jsk3", 4);

		// 开奖时间的彩种ID
		lotteryIdOrderCode = new HashMap<String, Integer>();
		lotteryIdOrderCode.put(BJSC, 2);
		lotteryIdOrderCode.put(XYFT, 3);
		lotteryIdOrderCode.put(CQSSC, 4);
		lotteryIdOrderCode.put(GDKLSF, 5);
		lotteryIdOrderCode.put(XYNC, 6);
		lotteryIdOrderCode.put(GD11X5, 7);
		lotteryIdOrderCode.put(KL8, 8);
		lotteryIdOrderCode.put(PCDD, 8);
		lotteryIdOrderCode.put(JSK3, 9);
		lotteryIdOrderCode.put(XJSSC, 14);
		lotteryIdOrderCode.put(TJSSC, 15);
		lotteryIdOrderCode.put(JX11X5, 21);

		// 号码大号里最小的号 lottery+T 0数值是和 1大号里最小的号
		lotteryTotalBorderMap = new HashMap<String, Integer>();
		lotteryTotalBorderMap.put("cqssc", 23);
		lotteryTotalBorderMap.put("cqsscT", 1);
		lotteryTotalBorderMap.put("gdklsf", 84);
		lotteryTotalBorderMap.put("gdklsfT", 0);
		lotteryTotalBorderMap.put("xync", 84);
		lotteryTotalBorderMap.put("xyncT", 0);
		lotteryTotalBorderMap.put("sdc", 24);
		lotteryTotalBorderMap.put("sdcT", 0);
		lotteryTotalBorderMap.put("gd11x5", 30);
		lotteryTotalBorderMap.put("gd11x5T", 0);
		lotteryTotalBorderMap.put("kl8", 810);
		lotteryTotalBorderMap.put("kl8T", 0);
		lotteryTotalBorderMap.put("pcdd", 14);
		lotteryTotalBorderMap.put("pcddT", 1);
		lotteryTotalBorderMap.put("jsk3", 11);
		lotteryTotalBorderMap.put("jsk3T", 1);
		lotteryTotalBorderMap.put("xjssc", 23);
		lotteryTotalBorderMap.put("xjsscT", 1);
		lotteryTotalBorderMap.put("tjssc", 23);
		lotteryTotalBorderMap.put("tjsscT", 1);
		lotteryTotalBorderMap.put("jx11x5", 30);
		lotteryTotalBorderMap.put("jx11x5T", 0);

		/**
		 * 每天开出的总期数
		 */
		lotteryTotalPeriodMap = new HashMap<String, Integer>();
		lotteryTotalPeriodMap.put("bjsc", 179);
		lotteryTotalPeriodMap.put("cqssc", 120);
		lotteryTotalPeriodMap.put("gd11x5", 84);
		lotteryTotalPeriodMap.put("jx11x5", 84);
		lotteryTotalPeriodMap.put("gdklsf", 84);
		lotteryTotalPeriodMap.put("xyft", 180);
		lotteryTotalPeriodMap.put("xync", 97);
		// lotteryTotalPeriodMap.put("sdc", 5);
		lotteryTotalPeriodMap.put("tjssc", 84);
		lotteryTotalPeriodMap.put("xjssc", 96);
		lotteryTotalPeriodMap.put("jsk3", 82);
		lotteryTotalPeriodMap.put("pcdd", 179);
		lotteryTotalPeriodMap.put("kl8", 179);

		lotteryShapeMap = new HashMap<String, Object>();
		lotteryShapeMap.put("大小", "ab");
		lotteryShapeMap.put("小大", "ab");
		lotteryShapeMap.put("单双", "ab");
		lotteryShapeMap.put("双单", "ab");
		lotteryShapeMap.put("龍虎", "ab");
		lotteryShapeMap.put("虎龍", "ab");
		lotteryShapeMap.put("大大小", "aab");
		lotteryShapeMap.put("小小大", "aab");
		lotteryShapeMap.put("单单双", "aab");
		lotteryShapeMap.put("双双单", "aab");
		lotteryShapeMap.put("龍龍虎", "aab");
		lotteryShapeMap.put("虎虎龍", "aab");
		lotteryShapeMap.put("大大小小", "aabb");
		lotteryShapeMap.put("小小大大", "aabb");
		lotteryShapeMap.put("单单双双", "aabb");
		lotteryShapeMap.put("双双单单", "aabb");
		lotteryShapeMap.put("龍龍虎虎", "aabb");
		lotteryShapeMap.put("虎虎龍龍", "aabb");

		lotteryClurlMap = new HashMap<String, Object>();
		// 北京赛车
		lotteryClurlMap.put("bjsc_dx", "pk10/shuangmianluzhu");
		lotteryClurlMap.put("bjsc_ds", "pk10/shuangmianluzhu");
		lotteryClurlMap.put("bjsc_lh", "pk10/longhuluzhu");
		lotteryClurlMap.put("bjsc_gyh", "pk10/guanyaheluzhu");
		lotteryClurlMap.put("bjsc_shape_dx", "pk10/shuangmianluzhu");
		lotteryClurlMap.put("bjsc_shape_ds", "pk10/shuangmianluzhu");
		lotteryClurlMap.put("bjsc_shape_ls", "pk10/longhuluzhu");
		lotteryClurlMap.put("bjsc_shape_gyh", "pk10/guanyaheluzhu");
		// 重庆时时彩
		lotteryClurlMap.put("cqssc_dx", "shishicai/shuangmianluzhu");
		lotteryClurlMap.put("cqssc_ds", "shishicai/shuangmianluzhu");
		lotteryClurlMap.put("cqssc_lh", "shishicai/longhuluzhu");
		lotteryClurlMap.put("cqssc_shape_dx", "shishicai/shuangmianluzhu");
		lotteryClurlMap.put("cqssc_shape_ds", "shishicai/shuangmianluzhu");
		lotteryClurlMap.put("cqssc_shape_lh", "shishicai/longhuluzhu");
		// 广东11x5
		lotteryClurlMap.put("gd11x5_dx", "gd11x5/shuangmianluzhu");
		lotteryClurlMap.put("gd11x5_ds", "gd11x5/shuangmianluzhu");
		lotteryClurlMap.put("gd11x5_lh", "gd11x5/longhuluzhu");
		lotteryClurlMap.put("gd11x5_shape_dx", "gd11x5/shuangmianluzhu");
		lotteryClurlMap.put("gd11x5_shape_ds", "gd11x5/shuangmianluzhu");
		lotteryClurlMap.put("gd11x5_shape_lh", "gd11x5/longhuluzhu");
		// 广东快乐十分
		lotteryClurlMap.put("gdklsf_dx", "gdkl10/shuangmianluzhu");
		lotteryClurlMap.put("gdklsf_ds", "gdkl10/shuangmianluzhu");
		lotteryClurlMap.put("gdklsf_lh", "gdkl10/longhuluzhu");
		lotteryClurlMap.put("gdklsf_shape_dx", "gdkl10/shuangmianluzhu");
		lotteryClurlMap.put("gdklsf_shape_ds", "gdkl10/shuangmianluzhu");
		lotteryClurlMap.put("gdklsf_shape_lh", "gdkl10/longhuluzhu");

		/*
		 * lotteryClurlMap.put("jsk", "jsk3"); lotteryClurlMap.put("kl8", "kl8");
		 */
		// 江西11x5
		lotteryClurlMap.put("jx11x5_dx", "jx11x5/shuangmianluzhu");
		lotteryClurlMap.put("jx11x5_ds", "jx11x5/shuangmianluzhu");
		lotteryClurlMap.put("jx11x5_lh", "jx11x5/longhuluzhu");
		lotteryClurlMap.put("jx11x5_shape_dx", "jx11x5/shuangmianluzhu");
		lotteryClurlMap.put("jx11x5_shape_ds", "jx11x5/shuangmianluzhu");
		lotteryClurlMap.put("jx11x5_shape_lh", "jx11x5/longhuluzhu");
		// 天津时时彩
		lotteryClurlMap.put("tjssc_dx", "tjssc/shuangmianluzhu");
		lotteryClurlMap.put("tjssc_ds", "tjssc/shuangmianluzhu");
		lotteryClurlMap.put("tjssc_lh", "tjssc/longhuluzhu");
		lotteryClurlMap.put("tjssc_shape_dx", "tjssc/shuangmianluzhu");
		lotteryClurlMap.put("tjssc_shape_ds", "tjssc/shuangmianluzhu");
		lotteryClurlMap.put("tjssc_shape_lh", "tjssc/longhuluzhu");
		// 新疆时时彩
		lotteryClurlMap.put("xjssc_dx", "xjssc/shuangmianluzhu");
		lotteryClurlMap.put("xjssc_ds", "xjssc/shuangmianluzhu");
		lotteryClurlMap.put("xjssc_lh", "xjssc/longhuluzhu");
		lotteryClurlMap.put("xjssc_shape_dx", "xjssc/shuangmianluzhu");
		lotteryClurlMap.put("xjssc_shape_ds", "xjssc/shuangmianluzhu");
		lotteryClurlMap.put("xjssc_shape_lh", "xjssc/longhuluzhu");
		// 幸运飞艇
		lotteryClurlMap.put("xyft_dx", "xyft/shuangmianluzhu");
		lotteryClurlMap.put("xyft_ds", "xyft/shuangmianluzhu");
		lotteryClurlMap.put("xyft_lh", "xyft/longhuluzhu");
		lotteryClurlMap.put("xyft_gyh", "xyft/guanyaheluzhu");
		lotteryClurlMap.put("xyft_shape_dx", "xyft/shuangmianluzhu");
		lotteryClurlMap.put("xyft_shape_ds", "xyft/shuangmianluzhu");
		lotteryClurlMap.put("xyft_shape_lh", "xyft/longhuluzhu");
		lotteryClurlMap.put("xyft_shape_gyh", "xyft/guanyaheluzhu");
		// 幸运农场
		lotteryClurlMap.put("xync_dx", "xync/shuangmianluzhu");
		lotteryClurlMap.put("xync_ds", "xync/shuangmianluzhu");
		lotteryClurlMap.put("xync_lh", "xync/longhuluzhu");
		lotteryClurlMap.put("xync_shape_dx", "xync/shuangmianluzhu");
		lotteryClurlMap.put("xync_shape_ds", "xync/shuangmianluzhu");
		lotteryClurlMap.put("xync_shape_lh", "xync/longhuluzhu");
	}
}
