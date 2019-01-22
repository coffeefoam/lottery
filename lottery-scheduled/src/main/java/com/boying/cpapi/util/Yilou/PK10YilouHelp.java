package com.boying.cpapi.util.Yilou;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * PK10遗漏统计相关公共方法
 * @author ms
 */
public class PK10YilouHelp {
	//pk10所有号码
	static String[] pk10number=new String[] {"1","2","3","4","5","6","7","8","9","10"};
	//pk10所有位置
	static String[] pk10position=new String[] {"num1","num2","num3","num4","num5","num6","num7","num8","num9","num10"};
	/**
	 * pk10号码遗漏（遗漏输赢暂未计算）
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject pk10hmyl(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String number :pk10number) {
			JSONObject obj2=new JSONObject(true);
			for(String position:pk10position) {
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
	 * 位置遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject pk10wzyl(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		//位置号码
		JSONObject wzhmobj=pk10wzhm(clist, wlist, mlist);
		resultobj.put("wzhm", wzhmobj);
		//位置大小
		JSONObject wzdxobj=pk10wzdx(clist, wlist, mlist);
		resultobj.put("wzdx", wzdxobj);
		//位置单双
		JSONObject wzdsobj=pk10wzds(clist, wlist, mlist);
		resultobj.put("wzds", wzdsobj);
		//位置冠亚和大小
		JSONObject wzgyhdxobj=pk10wzgyhdx(clist, wlist, mlist);
		resultobj.put("wzgyhdx", wzgyhdxobj);
		//位置冠亚和单双
		JSONObject wzgyhdsobj=pk10wzgyhds(clist, wlist, mlist);
		resultobj.put("wzgyhds", wzgyhdsobj);
		//位置龙虎（pk10计算前五位龙虎）
		JSONObject wzlhobj=pk10wzlh(clist, wlist, mlist);
		resultobj.put("wzlh", wzlhobj);
		
		return resultobj;
	}
	
	public static JSONObject pk10gyhdianshu(List<Map<String,Object>> clist) {
		JSONObject resultobj=new JSONObject(true);
		String[] array= {"num1","num2"};
		List<Integer> numberlist=new ArrayList<Integer>();
		for(int i=3;i<20;i++) {
			numberlist.add(i);
		}
		for(Integer number: numberlist) {
			int jrcxmax=YilouUtil.zhdianshucxcs(clist, array, number);
			int jryl=YilouUtil.zhdianshuzdyl(clist, array, number);
			JSONObject ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax);
			ojb1.put("jryl", jryl);
			resultobj.put(number.toString(), ojb1);
			
		}
		return resultobj;
	}
	
	/**
	 * 位置遗漏中的号码遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject pk10wzhm(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String position:pk10position) {
			JSONObject ojb=new JSONObject(true);
			for(String number:pk10number) {
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
	 * 位置遗漏中的大小遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject pk10wzdx(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String position:pk10position) {
			JSONObject ojb=new JSONObject(true);
			
			int jrcxmax=YilouUtil.dxcxcs(clist, true, position, 5);
			int jrylmax=YilouUtil.dxzdyl(clist, true, position, 5);
			int bzylmax=YilouUtil.dxzdyl(wlist, true, position, 5);
			int byylmax=YilouUtil.dxzdyl(mlist, true, position, 5);
			JSONObject ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax);
			ojb1.put("jryl", jrylmax);
			ojb1.put("bzyl", bzylmax);
			ojb1.put("byyl", byylmax);
			ojb.put("小", ojb1);
			
			int jrcxmax2=YilouUtil.dxcxcs(clist, false, position, 5);
			int jrylmax2=YilouUtil.dxzdyl(clist, false, position, 5);
			int bzylmax2=YilouUtil.dxzdyl(wlist, false, position, 5);
			int byylmax2=YilouUtil.dxzdyl(mlist, false, position, 5);
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
	 * 位置遗漏中的单双遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject pk10wzds(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String position:pk10position) {
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
	 * 位置遗漏中的冠亚和大小遗漏计算 冠亚和大于11算大 小于或等于11算小
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject pk10wzgyhdx(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		int number=11;
		String[] array= {"num1","num2"};
		int jrcxmax=YilouUtil.zhdxcxcs(clist,array, true, number);
		int jrylmax=YilouUtil.zhdxzdyl(clist,array, true,number);
		int bzylmax=YilouUtil.zhdxzdyl(wlist,array, true,number);
		int byylmax=YilouUtil.zhdxzdyl(mlist,array, true,number);
		JSONObject ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("冠亚和大", ojb1);

		int jrcxmax2=YilouUtil.zhdxcxcs(clist, array, false, number);
		int jrylmax2=YilouUtil.zhdxzdyl(clist,array, true,number);
		int bzylmax2=YilouUtil.zhdxzdyl(wlist,array, true,number);
		int byylmax2=YilouUtil.zhdxzdyl(mlist,array, true,number);
		ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax2);
		ojb1.put("jryl", jrylmax2);
		ojb1.put("bzyl", bzylmax2);
		ojb1.put("byyl", byylmax2);
		resultobj.put("冠亚和小", ojb1);
			
		return resultobj;
	}
	
	/**
	 * 位置遗漏中的冠亚和单双遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject pk10wzgyhds(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		String[] array= {"num1","num2"};
		int jrcxmax=YilouUtil.zhdscxcs(clist,array,true);
		int jrylmax=YilouUtil.zhdszdyl(clist,array,true);
		int bzylmax=YilouUtil.zhdszdyl(wlist,array, true);
		int byylmax=YilouUtil.zhdszdyl(mlist,array, true);
		JSONObject ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("冠亚和单", ojb1);

		int jrcxmax2=YilouUtil.zhdscxcs(clist, array, false);
		int jrylmax2=YilouUtil.zhdszdyl(clist,array, false);
		int bzylmax2=YilouUtil.zhdszdyl(wlist,array, false);
		int byylmax2=YilouUtil.zhdszdyl(mlist,array, false);
		ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax2);
		ojb1.put("jryl", jrylmax2);
		ojb1.put("bzyl", bzylmax2);
		ojb1.put("byyl", byylmax2);
		resultobj.put("冠亚和双", ojb1);
			
		return resultobj;
	}
	
	/**
	 * 位置遗漏中的龙虎遗漏计算
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject pk10wzlh(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		//得到每期具体开出的号码
		List<List<Integer>> cclist=YilouUtil.getListValue(clist, pk10position);
		List<List<Integer>> wwlist=YilouUtil.getListValue(wlist, pk10position);
		List<List<Integer>> mmlist=YilouUtil.getListValue(mlist, pk10position);
		
		JSONObject resultobj=new JSONObject(true);
		int jrcxmax=0,jrylmax=0,bzylmax=0,byylmax=0;
		JSONObject ojb1=new JSONObject(true);
		//冠军 龙
		jrcxmax=YilouUtil.lhcxcs(cclist, true, 0);
		jrylmax=YilouUtil.lhzdyl(cclist, true, 0);
		bzylmax=YilouUtil.lhzdyl(wwlist, true, 0);
		byylmax=YilouUtil.lhzdyl(mmlist, true, 0);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("冠军龙", ojb1);
		//冠军 虎
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
		
		//亚军 龙
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, true, 1);
		jrylmax=YilouUtil.lhzdyl(cclist, true, 1);
		bzylmax=YilouUtil.lhzdyl(wwlist, true, 1);
		byylmax=YilouUtil.lhzdyl(mmlist, true, 1);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("亚军龙", ojb1);
		//亚军 虎
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, false, 1);
		jrylmax=YilouUtil.lhzdyl(cclist, false, 1);
		bzylmax=YilouUtil.lhzdyl(wwlist, false, 1);
		byylmax=YilouUtil.lhzdyl(mmlist, false, 1);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("亚军虎", ojb1);
		
		//第三名 龙
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, true, 2);
		jrylmax=YilouUtil.lhzdyl(cclist, true, 2);
		bzylmax=YilouUtil.lhzdyl(wwlist, true, 2);
		byylmax=YilouUtil.lhzdyl(mmlist, true, 2);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("第三名龙", ojb1);
		//第三名 虎
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, false, 2);
		jrylmax=YilouUtil.lhzdyl(cclist, false, 2);
		bzylmax=YilouUtil.lhzdyl(wwlist, false, 2);
		byylmax=YilouUtil.lhzdyl(mmlist, false, 2);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("第三名虎", ojb1);
		
		//第四名 龙
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, true, 3);
		jrylmax=YilouUtil.lhzdyl(cclist, true, 3);
		bzylmax=YilouUtil.lhzdyl(wwlist, true, 3);
		byylmax=YilouUtil.lhzdyl(mmlist, true, 3);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("第四名龙", ojb1);
		//第四名 虎
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, false, 3);
		jrylmax=YilouUtil.lhzdyl(cclist, false, 3);
		bzylmax=YilouUtil.lhzdyl(wwlist, false, 3);
		byylmax=YilouUtil.lhzdyl(mmlist, false, 3);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("第四名虎", ojb1);
		
		//第五名 龙
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, true, 4);
		jrylmax=YilouUtil.lhzdyl(cclist, true, 4);
		bzylmax=YilouUtil.lhzdyl(wwlist, true, 4);
		byylmax=YilouUtil.lhzdyl(mmlist, true, 4);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("第五名龙", ojb1);
		//第五名 虎
		ojb1=new JSONObject(true);
		jrcxmax=YilouUtil.lhcxcs(cclist, false, 4);
		jrylmax=YilouUtil.lhzdyl(cclist, false, 4);
		bzylmax=YilouUtil.lhzdyl(wwlist, false, 4);
		byylmax=YilouUtil.lhzdyl(mmlist, false, 4);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("第五名虎", ojb1);
		
		return resultobj;
	}
	
}