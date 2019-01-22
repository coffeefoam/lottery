package com.boying.cpapi.util.Yilou;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 时时彩遗漏统计相关公共方法
 * @author ms
 */
public class SscYilouHelp {
	//时时彩所有号码
	static String[] sscnumber=new String[] {"0","1","2","3","4","5","6","7","8","9"};
	//时时彩所有号码
	static String[] sscposition=new String[] {"num1","num2","num3","num4","num5"};
	/**
	 * 时时彩号码遗漏（遗漏输赢暂未计算）
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject sschmyl(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(int i=0;i<sscnumber.length;i++) {
			String number=sscnumber[i];
			JSONObject obj2=new JSONObject(true);
			for(int j=0;j<sscposition.length;j++) {
				String position=sscposition[j];
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
	 * 时时彩位置遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject sscwzyl(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		//位置号码
		JSONObject wzhmobj=sscwzhm(clist, wlist, mlist);
		resultobj.put("wzhm", wzhmobj);
		//位置单双
		JSONObject wzdsobj=sscwzds(clist, wlist, mlist);
		resultobj.put("wzds", wzdsobj);
		//位置大小
		JSONObject wzdxobj=sscwzdx(clist, wlist, mlist);
		resultobj.put("wzdx", wzdxobj);
		//位置总和单双
		JSONObject wzzhdsobj=sscwzzhds(clist, wlist, mlist);
		resultobj.put("wzzhds", wzzhdsobj);
		//位置总和大小
		JSONObject wzzhdxobj=sscwzzhdx(clist, wlist, mlist);
		resultobj.put("wzzhdx", wzzhdxobj);
		//位置龙虎（时时彩只计算第一位龙虎）
		JSONObject wzlhobj=sscwzlh(clist, wlist, mlist);
		resultobj.put("wzlh", wzlhobj);
		
		return resultobj;
	}
	
	/**
	 * 位置遗漏中的号码遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject sscwzhm(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String position:sscposition) {
			JSONObject ojb=new JSONObject(true);
			for(String number:sscnumber) {
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
	
	/**
	 * 位置遗漏中的单双遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject sscwzds(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String position:sscposition) {
			JSONObject ojb=new JSONObject(true);
			
			int jrcxmax=YilouUtil.dscxcs(clist, true, position);
			int jrylmax=YilouUtil.dszdyl(clist, true, position);
			int bzylmax=YilouUtil.dszdyl(wlist, true, position);
			int byylmax=YilouUtil.dszdyl(mlist, true, position);
			JSONObject ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax);
			ojb1.put("jryl", jrylmax);
			ojb1.put("bzyl", bzylmax);
			ojb1.put("byyl", byylmax);
			ojb.put("单", ojb1);
			
			int jrcxmax2=YilouUtil.dscxcs(clist, false, position);
			int jrylmax2=YilouUtil.dszdyl(clist, false, position);
			int bzylmax2=YilouUtil.dszdyl(wlist, false, position);
			int byylmax2=YilouUtil.dszdyl(mlist, false, position);
			ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax2);
			ojb1.put("jryl", jrylmax2);
			ojb1.put("bzyl", bzylmax2);
			ojb1.put("byyl", byylmax2);
			ojb.put("双", ojb1);
			resultobj.put(position, ojb);
		}
		return resultobj;
	}
	/**
	 * 位置遗漏中的大小遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject sscwzdx(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String position:sscposition) {
			JSONObject ojb=new JSONObject(true);
			
			int jrcxmax=YilouUtil.dxcxcs(clist, true, position, 4);
			int jrylmax=YilouUtil.dxzdyl(clist, true, position, 4);
			int bzylmax=YilouUtil.dxzdyl(wlist, true, position, 4);
			int byylmax=YilouUtil.dxzdyl(mlist, true, position, 4);
			JSONObject ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax);
			ojb1.put("jryl", jrylmax);
			ojb1.put("bzyl", bzylmax);
			ojb1.put("byyl", byylmax);
			ojb.put("小", ojb1);
			
			int jrcxmax2=YilouUtil.dxcxcs(clist, false, position, 4);
			int jrylmax2=YilouUtil.dxzdyl(clist, false, position, 4);
			int bzylmax2=YilouUtil.dxzdyl(wlist, false, position, 4);
			int byylmax2=YilouUtil.dxzdyl(mlist, false, position, 4);
			ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax2);
			ojb1.put("jryl", jrylmax2);
			ojb1.put("bzyl", bzylmax2);
			ojb1.put("byyl", byylmax2);
			ojb.put("大", ojb1);
			
			resultobj.put(position, ojb);
		}
		return resultobj;
	}
	
	
	
	/**
	 * 位置遗漏中的总和单双遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject sscwzzhds(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		int jrcxmax=YilouUtil.zhdscxcs(clist, sscposition, true);
		int jrylmax=YilouUtil.zhdszdyl(clist,sscposition,  true);
		int bzylmax=YilouUtil.zhdszdyl(wlist,sscposition, true);
		int byylmax=YilouUtil.zhdszdyl(mlist,sscposition, true);
		JSONObject ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("总和单", ojb1);
	
		int jrcxmax2=YilouUtil.zhdscxcs(clist,sscposition, false);
		int jrylmax2=YilouUtil.zhdszdyl(clist,sscposition, false);
		int bzylmax2=YilouUtil.zhdszdyl(wlist,sscposition, false);
		int byylmax2=YilouUtil.zhdszdyl(mlist,sscposition, false);
		ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax2);
		ojb1.put("jryl", jrylmax2);
		ojb1.put("bzyl", bzylmax2);
		ojb1.put("byyl", byylmax2);
		resultobj.put("总和双", ojb1);
			
		return resultobj;
	}

	/**
	 * 位置遗漏中的总和大小遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject sscwzzhdx(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		int number=22;
		int jrcxmax=YilouUtil.zhdxcxcs(clist, sscposition, true, number);
		int jrylmax=YilouUtil.zhdxzdyl(clist, sscposition, true, number);
		int bzylmax=YilouUtil.zhdxzdyl(wlist, sscposition, true, number);
		int byylmax=YilouUtil.zhdxzdyl(mlist, sscposition, true, number);
		JSONObject ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("总和大", ojb1);

		int jrcxmax2=YilouUtil.zhdxcxcs(clist,sscposition, false, number);
		int jrylmax2=YilouUtil.zhdxzdyl(clist,sscposition, false, number);
		int bzylmax2=YilouUtil.zhdxzdyl(wlist,sscposition, false, number);
		int byylmax2=YilouUtil.zhdxzdyl(mlist,sscposition, false, number);
		ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax2);
		ojb1.put("jryl", jrylmax2);
		ojb1.put("bzyl", bzylmax2);
		ojb1.put("byyl", byylmax2);
		resultobj.put("总和小", ojb1);
			
		return resultobj;
	}
	
	/**
	 * 位置遗漏中的龙虎遗漏计算   时时彩只计算第一位龙虎
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject sscwzlh(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		List<List<Integer>> cclist=YilouUtil.getListValue(clist, sscposition);
		List<List<Integer>> wwlist=YilouUtil.getListValue(wlist, sscposition);
		List<List<Integer>> mmlist=YilouUtil.getListValue(mlist, sscposition);
		JSONObject resultobj=new JSONObject(true);
		int jrcxmax=0,jrylmax=0,bzylmax=0,byylmax;
		//龙
		JSONObject ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, true, 0);
		jrylmax=YilouUtil.lhzdyl(cclist, true, 0);
		bzylmax=YilouUtil.lhzdyl(wwlist, true, 0);
		byylmax=YilouUtil.lhzdyl(mmlist, true, 0);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("冠军龙", ojb1);
		//虎
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, false, 0);
		jrylmax=YilouUtil.lhzdyl(cclist, false, 0);
		bzylmax=YilouUtil.lhzdyl(wwlist, false, 0);
		byylmax=YilouUtil.lhzdyl(mmlist, false, 0);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("冠军虎", ojb1);
		return resultobj;
	}
	
}