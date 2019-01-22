package com.boying.cpapi.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myprops")
// 接收application.yml中的myProps下面的属性
public class MyProps {
	/**
	 * 开奖网最新开奖记录
	 */
	private String apiplusurl;
	/**
	 * 开奖网获取全天开奖记录
	 */
	private String dateapiurl;
	/**
	 * 六合彩开奖接口
	 */
	private Map<String, String> lhcapi = new HashMap<>();
	private Map<String, String> encrypt = new HashMap<>(); // 解密key
	
	
	
	
	public Map<String, String> getLhcapi() {
		return lhcapi;
	}
	public void setLhcapi(Map<String, String> lhcapi) {
		this.lhcapi = lhcapi;
	}
	public Map<String, String> getEncrypt() {
		return encrypt;
	}
	public void setEncrypt(Map<String, String> encrypt) {
		this.encrypt = encrypt;
	}
	public String getApiplusurl() {
		return apiplusurl;
	}
	public void setApiplusurl(String apiplusurl) {
		this.apiplusurl = apiplusurl;
	}
	public String getDateapiurl() {
		return dateapiurl;
	}
	public void setDateapiurl(String dateapiurl) {
		this.dateapiurl = dateapiurl;
	}


}
