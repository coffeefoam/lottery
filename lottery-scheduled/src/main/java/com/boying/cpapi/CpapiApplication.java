package com.boying.cpapi;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@MapperScan("com.boying.cpapi.mapper")//在代码里面实现了读写分离，所以不再此对mapper进行扫描
@EnableScheduling //@EnableScheduling 注解的作用是发现注解@Scheduled的任务并后台执行。
@EnableTransactionManagement
@EnableCaching
public class CpapiApplication extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(CpapiApplication.class);
	}

	
	public static void main(String[] args) {
		SpringApplication.run(CpapiApplication.class, args);
	}
}
