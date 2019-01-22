package com.boying.cpapi.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;

@Component
public class HttpAPIService {

	@Autowired
	private CloseableHttpClient httpClient;

	@Autowired
	private RequestConfig config;

	@Value("${http.maxTotal}")
    private Integer maxTotal;

    @Value("${http.defaultMaxPerRoute}")
    private Integer defaultMaxPerRoute;

    @Value("${http.connectTimeout}")
    private Integer connectTimeout;

    @Value("${http.connectionRequestTimeout}")
    private Integer connectionRequestTimeout;

    @Value("${http.socketTimeout}")
    private Integer socketTimeout;

    @Value("${http.staleConnectionCheckEnabled}")
    private boolean staleConnectionCheckEnabled;

	/**
	 * 不带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url) throws Exception {
		HttpGet httpGet = null;
		CloseableHttpResponse response = null;
		try {
			httpGet = new HttpGet(url);// 声明 http get 请求
			httpGet.setConfig(config);// 装载配置信息
			response = this.httpClient.execute(httpGet);// 发起请求
			if (response.getStatusLine().getStatusCode() == 200) {// 判断状态码是否为200
				return EntityUtils.toString(response.getEntity(), "UTF-8");// 返回响应体的内容
			}
		}catch (Exception e) {
			System.err.println(new Date()+"doget请求出错，原因："+e.getMessage());
			e.printStackTrace();
		}finally {
			if(response != null) {
				response.close();
			}
		}
		return null;
	}
	
	/**
	 * 不用代理的get方法，并且设置了超时
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGetNoProxy(String url) throws Exception {
		HttpGet httpGet = null;
		CloseableHttpResponse response = null;
		try {
			httpGet = new HttpGet(url);// 声明 http get 请求
			config = RequestConfig.custom().setConnectTimeout(connectTimeout)
	                .setConnectionRequestTimeout(connectionRequestTimeout)
	                .setSocketTimeout(socketTimeout)
	                .setStaleConnectionCheckEnabled(staleConnectionCheckEnabled).build();
			httpGet.setConfig(config);// 装载配置信息
			response = this.httpClient.execute(httpGet);// 发起请求
			if (response.getStatusLine().getStatusCode() == 200) {// 判断状态码是否为200
				return EntityUtils.toString(response.getEntity(), "UTF-8");// 返回响应体的内容
			}
		}catch (Exception e) {
			System.err.println(new Date()+"doget请求出错，原因："+e.getMessage());
			e.printStackTrace();
		}finally {
			if(response != null) {
				response.close();
			}
		}
		return null;
	}

	/**
	 * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGet(String url, Map<String, Object> map) throws Exception {
		URIBuilder uriBuilder = new URIBuilder(url);
		if (map != null) {
			for (Map.Entry<String, Object> entry : map.entrySet()) {// 遍历map,拼接请求参数
				uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
			}
		}
		return this.doGet(uriBuilder.build().toString());// 调用不带参数的get请求
	}

	/**
	 * 带head参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public String doGetForHead(String url, Map<String, Object> map, Map<String, String> proxymap) {
		// 1. 创建HttpClient对象
		CloseableHttpClient httpClient = HttpClientBuilder.create().build();
		// 2. 创建HttpGet对象
		HttpGet httpGet = new HttpGet(url);
		for (Map.Entry<String, Object> entry : map.entrySet()) {// 遍历map,拼接请求参数
			httpGet.setHeader(entry.getKey(), entry.getValue().toString());
		}

		if (proxymap != null && !proxymap.isEmpty()) {// 判断是否需要代理
			HttpHost proxy = new HttpHost(proxymap.get("proxyhost"), Integer.valueOf(proxymap.get("proxyport")));// 代理ip地址.端口
			config = RequestConfig.custom().setProxy(proxy).build();
		}
		httpGet.setConfig(config);// 装载配置信息
		CloseableHttpResponse response = null;
		try {
			// 3. 执行GET请求
			response = httpClient.execute(httpGet);
			// 4. 获取响应实体
			HttpEntity entity = response.getEntity();
			// 5. 处理响应实体
			if (entity != null) {
				return EntityUtils.toString(entity);
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// 6. 释放资源
			try {
				response.close();
				httpClient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 带参数的get请求，如果状态码为200，则返回body，如果不为200，则返回null
	 * 
	 * @param 支持代理、支持请求头携带参数
	 * @return
	 * @throws Exception
	 */
	public HttpResult doGet_Proxy(String url, Map<String, Object> headmap, Map<String, Object> map,
			Map<String, String> proxymap) {
		try {
			URIBuilder uriBuilder = new URIBuilder(url);
			if (map != null) {// 报文体参数
				for (Map.Entry<String, Object> entry : map.entrySet()) {// 遍历map,拼接请求参数
					uriBuilder.setParameter(entry.getKey(), entry.getValue().toString());
				}
			}
			//System.err.println("get请求连接：" + uriBuilder.build().toString());
			HttpGet httpGet = new HttpGet(uriBuilder.build().toString());// 声明 http get 请求
			if (headmap != null) {// 头信息参数
				for (Map.Entry<String, Object> entry : headmap.entrySet()) {// 遍历map,拼接请求参数
					httpGet.setHeader(entry.getKey(), entry.getValue().toString());
				}
			}
			if (proxymap != null && !proxymap.isEmpty()) {// 判断是否需要代理
				HttpHost proxy = new HttpHost(proxymap.get("proxyhost"), Integer.valueOf(proxymap.get("proxyport")));// 代理ip地址.端口
				config = RequestConfig.custom().setProxy(proxy).build();
			}
			httpGet.setConfig(config);// 装载配置信息
			CloseableHttpResponse response = this.httpClient.execute(httpGet);// 发起请求
			if (response != null) {
				return new HttpResult(response.getStatusLine().getStatusCode(),
						EntityUtils.toString(response.getEntity(), "UTF-8"));
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 不带参数post请求
	 * 
	 * @param url
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url) throws Exception {
		return this.doPost(url, null);
	}

	/**
	 * 带参数的post请求
	 * @param url
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost(String url, Map<String, Object> map) throws Exception {
		// 声明httpPost请求
		HttpPost httpPost = new HttpPost(url);
		// 加入配置信息
		httpPost.setConfig(config);
		// 判断map是否为空，不为空则进行遍历，封装from表单对象
		if (map != null) {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
			}
			// 构造from表单对象
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");
			// 把表单放到post里
			httpPost.setEntity(urlEncodedFormEntity);
		}
		// 发起请求
		CloseableHttpResponse response = this.httpClient.execute(httpPost);
		return new HttpResult(response.getStatusLine().getStatusCode(),
				EntityUtils.toString(response.getEntity(), "UTF-8"));
	}
	
	/**
	 * 带参数的post请求(代理方式)
	 * @param url
	 * @param data form表达格式的参数
	 * @param proxymap
	 */
	public HttpResult doPost_form_Proxy(String url, Map<String, Object> map, Map<String, String> proxymap) {
		HttpPost httpPost = new HttpPost(url);// 声明httpPost请求
		httpPost.addHeader("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
		if (proxymap != null && !proxymap.isEmpty()) {// 判断是否需要代理
			HttpHost proxy = new HttpHost(proxymap.get("proxyhost"), Integer.valueOf(proxymap.get("proxyport")));// 代理ip地址.端口
			config = RequestConfig.custom().setProxy(proxy).build();
		}
		httpPost.setConfig(config);// 加入配置信息
		try {
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			for (Map.Entry<String, Object> entry : map.entrySet()) {
				list.add(new BasicNameValuePair(entry.getKey(), entry.getValue() + ""));
			}
			UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(list, "UTF-8");// 构造from表单对象
			httpPost.setEntity(urlEncodedFormEntity);// 把表单放到post里
			CloseableHttpResponse response = this.httpClient.execute(httpPost);//发起请求
			return new HttpResult(response.getStatusLine().getStatusCode(),EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new HttpResult(404, "发生异常");
		} catch (IOException e) {
			e.printStackTrace();
			return new HttpResult(404, "发生异常");
		}
	}


	/**
	 * 带参数的post请求(代理方式)
	 * @param Authorization 认证的方式
	 * @param data json字符串格式的参数
	 * @param proxymap
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost_Proxy(String url, JSONObject reqjsonobj, Map<String, String> proxymap) {
		HttpPost httpPost = new HttpPost(url);
		if (reqjsonobj.get("Authorization") != null && !"".equals(reqjsonobj.get("Authorization"))) {
			httpPost.setHeader("Authorization", reqjsonobj.get("Authorization").toString());
			reqjsonobj.remove("Authorization");
		}
		StringEntity entity = new StringEntity(reqjsonobj.toJSONString(), "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:23.0) Gecko/20100101 Firefox/23.0");

		// 判断是否需要代理
		if (proxymap != null && !proxymap.isEmpty()) {
			HttpHost proxy = new HttpHost(proxymap.get("proxyhost"), Integer.valueOf(proxymap.get("proxyport")));// 代理ip地址.端口
			config = RequestConfig.custom().setProxy(proxy).setConnectTimeout(connectTimeout)
	                .setConnectionRequestTimeout(connectionRequestTimeout)
	                .setSocketTimeout(socketTimeout)
	                .setStaleConnectionCheckEnabled(staleConnectionCheckEnabled).build();
			httpPost.setConfig(config);
		}
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			return new HttpResult(response.getStatusLine().getStatusCode(),EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new HttpResult(404, "发生异常");
		} catch (IOException e) {
			e.printStackTrace();
			return new HttpResult(404, "发生异常");
		}
	}
	
	/**
	 * 带参数的post请求(代理方式)
	 * @param url
	 * @param data json字符串格式的参数
	 * @param proxymap
	 * @return
	 * @throws Exception
	 */
	public HttpResult doPost_Proxy(String url, String reqstr, Map<String, String> proxymap) {
		HttpPost httpPost = new HttpPost(url);
		httpPost.addHeader("Content-Type", "application/json;charset=utf-8");
		StringEntity entity = new StringEntity(reqstr, "utf-8");
		entity.setContentEncoding("UTF-8");
		entity.setContentType("application/json");
		httpPost.setEntity(entity);
		// 判断是否需要代理
		if (proxymap != null && !proxymap.isEmpty()) {
			HttpHost proxy = new HttpHost(proxymap.get("proxyhost"), Integer.valueOf(proxymap.get("proxyport")));// 代理ip地址.端口
			config = RequestConfig.custom().setProxy(proxy).build();
		}
		httpPost.setConfig(config);
		try {
			CloseableHttpResponse response = httpClient.execute(httpPost);
			return new HttpResult(response.getStatusLine().getStatusCode(),EntityUtils.toString(response.getEntity(), "UTF-8"));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return new HttpResult(404, "发生异常");
		} catch (IOException e) {
			e.printStackTrace();
			return new HttpResult(404, "发生异常");
		}
	}
	

	/**
	 * HTTP POST方式请求，发送XML格式的参数（代理方式）
	 * @param urlstr 访问路径
	 * @param xmlstr xml字符串
	 * @param parammap 配置参数
	 * @param proxymap 代理配置
	 * @return 返回回来的字符串
	 */
	public String doHttpPost_XML(String urlstr,String xmlstr,Map<String, String> parammap,Map<String, String> proxymap) {  
        try {  
            URL url = new URL(urlstr);  
            Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(proxymap.get("proxyhost"), Integer.valueOf(proxymap.get("proxyport"))));  
            
            URLConnection con = url.openConnection(proxy);  
            con.setDoOutput(true);   
            con.setRequestProperty("Pragma", "no-cache");  
            con.setRequestProperty("Cache-Control", "no-cache");  
            con.setRequestProperty("Content-Type", "text/xml");  
            if(parammap != null && !parammap.isEmpty()) {
            	con.setRequestProperty("Affiliate-Login", parammap.get("adminnamelogin"));  
                con.setRequestProperty("Affiliate-Id", parammap.get("adminname")); 
            }
  
            OutputStreamWriter out = new OutputStreamWriter(con.getOutputStream());      
            out.write(new String(xmlstr.getBytes("ISO-8859-1")));  
            out.flush();  
            out.close();  
            BufferedReader br = new BufferedReader(new InputStreamReader(con  
                    .getInputStream()));  
            String line = "";  
            StringBuilder sb=new StringBuilder();
            for (line = br.readLine(); line != null; line = br.readLine()) {  
            	sb.append(line);  
            }  
            return sb.toString();
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return null;
    }  
}