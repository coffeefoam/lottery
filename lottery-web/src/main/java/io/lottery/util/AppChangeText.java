package io.lottery.util;

import java.util.Map;
import java.util.Map.Entry;

/**
 * 
 * @desc 专门为app端sb处理文字，拼接成他们想要的字符串
 * @author abo
 * @date 2018年8月14日 下午3:49:46
 *
 */
public class AppChangeText {

	public static Map<Object, Object> changeWord(Map<Object, Object> map, String code) {
		String str = "";
		String title = "";
		String[] codes = code.split("_");
		String lottId = codes[0];// 彩种
		String type = codes[1];// 接口类型
		for (Entry<Object, Object> temp1 : map.entrySet()) {
			if (type.equals("dslz") || type.equals("hsdslz")) {
				if (temp1.getKey().toString().equals("dan")) {
					str += "单(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("shuang")) {
					str += "双(" + temp1.getValue() + ")";
				}
			}
			if (type.equals("dxlz") || type.equals("wsdxlz")) {
				if (temp1.getKey().toString().equals("da")) {
					str += "大(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("xiao")) {
					str += "小(" + temp1.getValue() + ")";
				}
			}
			if (type.equals("hmqhlz")) {
				if (temp1.getKey().toString().equals("qian")) {
					str += "前(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("hou")) {
					str += "后(" + temp1.getValue() + ")";
				}
			}
			if (type.equals("hmlz")) {
				if (temp1.getKey().toString().equals("gou")) {
					str += "总来(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("cha")) {
					str += "没来(" + temp1.getValue() + ")";
				}
			}
			if (type.equals("lhlz")) {
				if (temp1.getKey().toString().equals("long")) {
					str += "龍(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("hu")) {
					str += "虎(" + temp1.getValue() + ")";
				}
			}
			if (type.equals("zfblz")) {
				if (temp1.getKey().toString().equals("zhong")) {
					str += "中(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("fa")) {
					str += "发(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("bai")) {
					str += "白(" + temp1.getValue() + ")";
				}
			}
			if (type.equals("dnxblz")) {
				if (temp1.getKey().toString().equals("dong")) {
					str += "东(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("nan")) {
					str += "南(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("xi")) {
					str += "西(" + temp1.getValue() + ")";
				}
				if (temp1.getKey().toString().equals("bei")) {
					str += "北(" + temp1.getValue() + ")";
				}
			}

		}
		if (lottId.equals("bjsc") || lottId.equals("xyft")) {
			title = returnBjscNumberName(map.get("num") + "");
		}
		if (lottId.contains("ssc") || lottId.equals("gdklsf") || lottId.equals("xync") || lottId.contains("11x5")) {
			title = returnSscNumberName(map.get("num") + "");
		}
		if (type.equals("dslz") || type.equals("hsdslz")) {
			title += "单双";
			map.remove("dan");
			map.remove("shuang");
		}
		if (type.equals("dxlz") || type.equals("wsdxlz")) {
			title += "大小";
			map.remove("da");
			map.remove("xiao");
		}
		if (type.equals("hmqhlz") || type.equals("hmlz")) {
			title = "号码" + map.get("num").toString().replace("num", "");
			map.remove("qian");
			map.remove("hou");
			map.remove("gou");
			map.remove("cha");
		}
		if (type.equals("zfblz")) {
			title += "中发白";
			map.remove("zhong");
			map.remove("fa");
			map.remove("bai");
		}
		if (type.equals("dnxblz")) {
			title += "方位";
			map.remove("dong");
			map.remove("nan");
			map.remove("xi");
			map.remove("bei");
		}
		if (type.equals("lhlz")) {
			title += "龍虎";
		}
		map.put("title", title);
		map.put("jrlj", str);
		map.remove("num");
		map.remove("long");
		map.remove("hu");
		return map;
	}

	private static String returnBjscNumberName(String num) {
		String str = "";
		if (num.equals("num1")) {
			str = "冠军";
		} else if (num.equals("num2")) {
			str = "亚军";
		} else if (num.equals("num3")) {
			str = "第三名";
		} else if (num.equals("num4")) {
			str = "第四名";
		} else if (num.equals("num5")) {
			str = "第五名";
		} else if (num.equals("num6")) {
			str = "第六名";
		} else if (num.equals("num7")) {
			str = "第七名";
		} else if (num.equals("num8")) {
			str = "第八名";
		} else if (num.equals("num9")) {
			str = "第九名";
		} else if (num.equals("num10")) {
			str = "第十名";
		} else if (num.equals("num0")) {
			str = "冠亚和";
		}
		return str;
	}

	private static String returnSscNumberName(String num) {
		String str = "";
		if (num.equals("num1")) {
			str = "第一球";
		} else if (num.equals("num2")) {
			str = "第二球";
		} else if (num.equals("num3")) {
			str = "第三球";
		} else if (num.equals("num4")) {
			str = "第四球";
		} else if (num.equals("num5")) {
			str = "第五球";
		} else if (num.equals("num6")) {
			str = "第六球";
		} else if (num.equals("num7")) {
			str = "第七球";
		} else if (num.equals("num8")) {
			str = "第八球";
		} else if (num.equals("num9")) {
			str = "第九球";
		} else if (num.equals("num10")) {
			str = "第十球";
		} else if (num.equals("num") || num.equals("num0")) {
			str = "总和";
		}
		return str;
	}

	public static String returnTypeNameByType(String type) {
		String str = "";
		if (type.equals("wzds") || type.equals("hds")) {
			str = "单双";
		} else if (type.equals("wzdx") || type.equals("hdx")) {
			str = "大小";
		} else if (type.equals("wzlh")) {
			str = "龙虎";
		} else if (type.equals("wzhds")) {
			str = "合单双";
		} else if (type.equals("wzhdx") || type.equals("hwdx")) {
			str = "尾大小";
		}
		return str;
	}

	public static String changeWord(String[] array, String lottId) {
		String num = array[0];
		String type = array[1];
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			num = returnBjscNumberName(num);
		}
		if (lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.GDKLSF)) {
			num = returnSscNumberName(num);
		}
		// 因为双色球只有一对龙虎，所以前缀去掉
		if (lottId.equals(LotteryConstant.CQSSC) && type.equals("wzlh")) {
			num = "";
		}
		type = returnTypeNameByType(type);
		return num + type;
	}

	public static String changeWord(String key, String lottId) {
		String num = key;
		if (lottId.equals(LotteryConstant.BJSC) || lottId.equals(LotteryConstant.XYFT)) {
			num = returnBjscNumberName(num);
		}
		if (lottId.equals(LotteryConstant.CQSSC) || lottId.equals(LotteryConstant.GDKLSF)) {
			num = returnSscNumberName(num);
		}
		return num;
	}
}
