package com.boying.cpapi.util.Yilou;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 11选5遗漏统计相关公共方法
 * @author ms
 */
public class X11x5YilouHelp {
	//11选5所有号码
	static String[] x11x5number=new String[] {"1","2","3","4","5","6","7","8","9","10","11"};
	//11选5所有号码
	static String[] x11x5position=new String[] {"num1","num2","num3","num4","num5"};
	/**
	 * 11选5号码遗漏（遗漏输赢暂未计算）
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject x11x5hmyl(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String number:x11x5number) {
			JSONObject obj2=new JSONObject(true);
			for(String position:x11x5position) {
				int jrcxmax=YilouUtil.hmcxcs(clist,number,position);
				int jrylmax=YilouUtil.hmzdyl(clist,number,position);
				int bzylmax=YilouUtil.hmzdyl(wlist,number,position);
				int byylmax=YilouUtil.hmzdyl(mlist,number,position);
				JSONObject ojb1=new JSONObject(true);
				ojb1.put("jrcx", jrcxmax);
				ojb1.put("jryl", jrylmax);
				ojb1.put("bzyl", bzylmax);
				ojb1.put("byyl", byylmax);
				obj2.put(position, ojb1);
			}
			resultobj.put(number, obj2);
		}
		return resultobj;
	}
	
	/**
	 * 11选5位置遗漏中的号码遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject x11x5wzyl(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String position:x11x5position) {
			JSONObject ojb=new JSONObject(true);
			for(String number:x11x5number) {
				int jrcxmax=YilouUtil.hmcxcs(clist, number, position);
				int jrylmax=YilouUtil.hmzdyl(clist,number,position);
				int bzylmax=YilouUtil.hmzdyl(wlist,number,position);
				int byylmax=YilouUtil.hmzdyl(mlist,number,position);
				JSONObject ojb1=new JSONObject(true);
				ojb1.put("jrcx", jrcxmax);
				ojb1.put("jryl", jrylmax);
				ojb1.put("bzyl", bzylmax);
				ojb1.put("byyl", byylmax);
				ojb.put(number, ojb1);
			}
			resultobj.put(position, ojb);
		}
		return resultobj;
	}
	
}