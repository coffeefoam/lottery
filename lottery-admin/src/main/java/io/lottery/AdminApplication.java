package io.lottery;

import io.lottery.common.servlet.UserfilesDownloadServlet;
import io.lottery.common.utils.Global;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@MapperScan(basePackages = { "io.lottery.modules.*.dao" })
public class AdminApplication extends SpringBootServletInitializer {

	public static void main(String[] args) {
		SpringApplication.run(AdminApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(AdminApplication.class);
	}

	/**
	 * 查看图片处理servlet
	 * 
	 * @return
	 */
	@Bean
	public ServletRegistrationBean<UserfilesDownloadServlet> chkServletRegistration() {
		ServletRegistrationBean<UserfilesDownloadServlet> bean = new ServletRegistrationBean<UserfilesDownloadServlet>();
		// 拦截图片的下载地址，重定向图片下载的链接地址
		bean.setServlet(new UserfilesDownloadServlet());
		bean.addUrlMappings(Global.USERFILES_BASE_URL + "*");
		return bean;
	}
}
