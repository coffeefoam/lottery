package io.lottery.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * freemarker生成静态页面
 * 
 * @author R6
 *
 */
public class FreeMarkerUtils {

	private static Logger logger = LoggerFactory.getLogger(FreeMarkerUtils.class);

	/*
	 * 根据ftl模板文件,生成静态HTML文件
	 * 
	 * @param ftlPath FTL模板文件路径,例如["c:/liang/template.ftl"]
	 * 
	 * @param filePath 生成HMTL文件路径，例如["d:/liang/lianggzone.html"]
	 * 
	 * @param data Map数据
	 * 
	 * @return
	 */
	public static boolean createHTMLFile(String ftlPath, String ftlName, String filePath, Map<?, ?> data) {
		Writer out = null;
		// 获取HMTL文件目录
		String fileDirectory = StringUtils.substringBeforeLast(filePath, "/");
		// 获取HMTL文件名
		// String fileName = StringUtils.substringAfterLast(filePath, "/");
		// 获取HMTL文件目录
//		String ftlDirectory = StringUtils.substringBeforeLast(ftlPath, "/");
//		String	ftlDirectory=ftlPath;
		// 获取HMTL文件名
		// String ftlName = StringUtils.substringAfterLast(ftlPath, "/");

		// fileName=filePath+"index.html";
		try {
			// 文件递归创建生成文件目录
			File realDirectory = new File(fileDirectory);
			if (!realDirectory.exists()) {
				realDirectory.mkdirs();
			}
			// step1 获取freemarker的配置
			@SuppressWarnings("deprecation")
			Configuration freemarkerCfg = new Configuration();

			// TemplateLoader templateLoader=new templateLoader
			// freemarkerCfg.setTemplateLoader(templateLoader);
			freemarkerCfg.setSharedVariable("ctxPath", data.get("ctxPath"));
			// step2 设置freemarker模板所放置的位置(文件夹)
			freemarkerCfg.setDirectoryForTemplateLoading(new File(ftlPath));
			// step3 设置freemarker模板编码
			freemarkerCfg.setEncoding(Locale.getDefault(), "UTF-8");
			freemarkerCfg.setNumberFormat("#");// 指定数字格式化格式
			// step4 找到对应freemarker模板并实例化
			Template template = freemarkerCfg.getTemplate(ftlName);
			// step5 初始化一个IO流
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(filePath)), "UTF-8"));
			// step6 模板渲染出所要的内容
			template.process(data, out);
		} catch (TemplateException e) {
			logger.error("模板解析出错:{}", e);
			return false;
		} catch (IOException e) {
			logger.error("IO出错:{}", e);
			return false;
		} finally {
			try {
				out.flush();
				out.close();
			} catch (IOException e) {
				logger.error("关闭流出错:{}", e);
				e.printStackTrace();
			}
		}
		return true;
	}
}
