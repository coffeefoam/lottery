package com.boying.cpapi.util.Yilou;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;

/**
 * 快三遗漏统计相关公共方法
 * @author ms
 */
public class K3YilouHelp {
	//快三的号码
	static String[] k3number=new String[] {"1","2","3","4","5","6"};
	//快三位置
	static String[] k3position=new String[] {"num1","num2","num3"};
	
	/**
	 * 快3 总和大小（遗漏输赢暂未计算）
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject k3zhdaxiao(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		int number=10;
		int jrcxmax=YilouUtil.zhdxcxcs(clist, k3position, true, number);
		int jrylmax=YilouUtil.zhdxzdyl(clist, k3position, true, number);
		int bzylmax=YilouUtil.zhdxzdyl(wlist, k3position, true, number);
		int byylmax=YilouUtil.zhdxzdyl(mlist, k3position, true, number);
		JSONObject ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("总和大", ojb1);

		int jrcxmax2=YilouUtil.zhdxcxcs(clist,k3position, false, number);
		int jrylmax2=YilouUtil.zhdxzdyl(clist,k3position, false, number);
		int bzylmax2=YilouUtil.zhdxzdyl(wlist,k3position, false, number);
		int byylmax2=YilouUtil.zhdxzdyl(mlist,k3position, false, number);
		ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax2);
		ojb1.put("jryl", jrylmax2);
		ojb1.put("bzyl", bzylmax2);
		ojb1.put("byyl", byylmax2);
		resultobj.put("总和小", ojb1);
			
		return resultobj;
	}
	
	/**
	 * 快3 总和单双（遗漏输赢暂未计算）
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject k3zhdanshuang(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		int jrcxmax=YilouUtil.zhdscxcs(clist, k3position, true);
		int jrylmax=YilouUtil.zhdszdyl(clist,k3position,  true);
		int bzylmax=YilouUtil.zhdszdyl(wlist,k3position, true);
		int byylmax=YilouUtil.zhdszdyl(mlist,k3position, true);
		JSONObject ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax);
		ojb1.put("jryl", jrylmax);
		ojb1.put("bzyl", bzylmax);
		ojb1.put("byyl", byylmax);
		resultobj.put("总和单", ojb1);
	
		int jrcxmax2=YilouUtil.zhdscxcs(clist,k3position, false);
		int jrylmax2=YilouUtil.zhdszdyl(clist,k3position, false);
		int bzylmax2=YilouUtil.zhdszdyl(wlist,k3position, false);
		int byylmax2=YilouUtil.zhdszdyl(mlist,k3position, false);
		ojb1=new JSONObject(true);
		ojb1.put("jrcx", jrcxmax2);
		ojb1.put("jryl", jrylmax2);
		ojb1.put("bzyl", bzylmax2);
		ojb1.put("byyl", byylmax2);
		resultobj.put("总和双", ojb1);
			
		return resultobj;
	}
	
	/**
	 * 快3总和点数（遗漏输赢暂未计算）
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject k3zhdianshu(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		//所有总和值集合
		List<Integer> numberlist=new ArrayList<Integer>();
		for(int i=3;i<19;i++) {
			numberlist.add(i);
		}
		for(Integer number:numberlist) {
			int jrcxmax=YilouUtil.zhdianshucxcs(clist, k3position, number);
			int jrylmax=YilouUtil.zhdianshuzdyl(clist,k3position,  number);
			int bzylmax=YilouUtil.zhdianshuzdyl(wlist,k3position, number);
			int byylmax=YilouUtil.zhdianshuzdyl(mlist,k3position, number);
			JSONObject ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax);
			ojb1.put("jryl", jrylmax);
			ojb1.put("bzyl", bzylmax);
			ojb1.put("byyl", byylmax);
			resultobj.put(number.toString(),ojb1);
		}
		return resultobj;
	}
	
	/**
	 * 快3短牌（遗漏输赢暂未计算）
	 * @param clist  当天数据
	 * @param wlist	  本周数据
	 * @param mlist  本月数据
	 * @return
	 */
	public static JSONObject k3duanpai(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		for(String number:k3number) {
			int jrcxmax=YilouUtil.duanpaicxcs(clist, k3position, number);
			int jrylmax=YilouUtil.duanpaizdyl(clist,k3position,  number);
			int bzylmax=YilouUtil.duanpaizdyl(wlist,k3position, number);
			int byylmax=YilouUtil.duanpaizdyl(mlist,k3position, number);
			JSONObject ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax);
			ojb1.put("jryl", jrylmax);
			ojb1.put("bzyl", bzylmax);
			ojb1.put("byyl", byylmax);
			resultobj.put(number+","+number,ojb1);
		}
		return resultobj;
	}
	
	/**
	 * 快3长牌（遗漏输赢暂未计算）
	 * @param clist
	 * @param wlist
	 * @param mlist
	 * @return
	 */
	public static JSONObject k3changpai(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		//快三所有长牌集合
		List<List<Integer>> numberlist =new ArrayList<List<Integer>>();
		for (int i = 1; i <= 6; i++) {
			for (int j = i + 1; j <= 6; j++) {
				List<Integer> l=new ArrayList<Integer>();
				l.add(i);
				l.add(j);
				numberlist.add(l);
			}
		}
		//所有期开奖数字集合
		List<List<Integer>> cclist=YilouUtil.getListValue(clist, k3position);
		List<List<Integer>> wwlist=YilouUtil.getListValue(wlist, k3position);
		List<List<Integer>> mmlist=YilouUtil.getListValue(mlist, k3position);
		
		for(List<Integer> nlist:numberlist) {
			int jrcxmax=YilouUtil.changpaicxcs(cclist,nlist);
			int jrylmax=YilouUtil.changpaizdyl(cclist,nlist);
			int bzylmax=YilouUtil.changpaizdyl(wwlist,nlist);
			int byylmax=YilouUtil.changpaizdyl(mmlist,nlist);
			JSONObject ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax);
			ojb1.put("jryl", jrylmax);
			ojb1.put("bzyl", bzylmax);
			ojb1.put("byyl", byylmax);
			resultobj.put(nlist.get(0)+","+nlist.get(1),ojb1);
		}
		return resultobj;
	}
	
	/**
	 * 快3 三军（遗漏输赢暂未计算）
	 * @param clist
	 * @param wlist
	 * @param mlist
	 * @return
	 */
	public static JSONObject k3sanjun(List<Map<String,Object>> clist,List<Map<String,Object>> wlist,List<Map<String,Object>> mlist) {
		JSONObject resultobj=new JSONObject(true);
		//所有期开奖数字集合
		List<List<Integer>> cclist=YilouUtil.getListValue(clist, k3position);
		List<List<Integer>> wwlist=YilouUtil.getListValue(wlist, k3position);
		List<List<Integer>> mmlist=YilouUtil.getListValue(mlist, k3position);
		
		for(String number:k3number) {
			int jrcxmax=YilouUtil.sanjuncxcs(cclist,Integer.valueOf(number));
			int jrylmax=YilouUtil.sanjunzdyl(cclist,Integer.valueOf(number));
			int bzylmax=YilouUtil.sanjunzdyl(wwlist,Integer.valueOf(number));
			int byylmax=YilouUtil.sanjunzdyl(mmlist,Integer.valueOf(number));
			JSONObject ojb1=new JSONObject(true);
			ojb1.put("jrcx", jrcxmax);
			ojb1.put("jryl", jrylmax);
			ojb1.put("bzyl", bzylmax);
			ojb1.put("byyl", byylmax);
			resultobj.put(number,ojb1);
		}
		return resultobj;
	}
	
	
}