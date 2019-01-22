package Test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSONObject;

import io.lottery.WebApplication;
import io.lottery.common.config.RedisService;
import io.lottery.controller.AppApiController;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = WebApplication.class)
public class CpapiApplicationTests {
	@Autowired
	private RedisService redisservice;
	@Autowired
	private AppApiController appapicontroller;
	
	@Test
	public void test() {
		System.err.println(appapicontroller.appVersion("ios", null));
		System.err.println(appapicontroller.appVersion("ios", "1.0.0"));
		System.err.println(appapicontroller.appVersion("dfs", "123"));
	}
	@Test
	public void contextLoads() {
		JSONObject obj=new JSONObject();
		obj.put("version", "1.0.0");
		obj.put("content", "初始化版本上线");
		obj.put("url", "http://www.baidu.com");
		obj.put("flag", "1");//0:强制更新 1:非强制更新
		obj.put("time", System.currentTimeMillis());
		redisservice.setHashMap("app_version", "ios", obj.toJSONString());
		obj.put("version", "1.0.0");
		obj.put("content", "初始化版本上线");
		obj.put("url", "http://www.baidu.com");
		obj.put("flag", "1");//0:强制更新 1:非强制更新
		obj.put("time", System.currentTimeMillis());
		redisservice.setHashMap("app_version", "android", obj.toJSONString());
	}
	

}
