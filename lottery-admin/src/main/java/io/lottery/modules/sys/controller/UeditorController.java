package io.lottery.modules.sys.controller;

import io.lottery.common.utils.Global;
import io.lottery.modules.sys.dto.UeditorConfigMsg;
import io.lottery.modules.sys.entity.SysUserEntity;
import io.lottery.modules.sys.shiro.ShiroUtils;

import java.io.File;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;

/**
 * 基于ueditor文件上传类
 * 
 * @author NLP
 * 
 */

@Controller
@RequestMapping("/ueditor")
public class UeditorController {

	private Logger logger = LoggerFactory.getLogger(UeditorController.class);

	/**
	 * ueditor获取配置信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/config")
	@ResponseBody
	public Object ueditor(HttpServletRequest request) {
		//将json转成对象，便于返回json对象
		Object obj = JSON.parse(UeditorConfigMsg.UEDITOR_CONFIG);
		return obj;
	}

	/**
	 * ueditor上传图片
	 * 
	 * @param upfile
	 * @param request
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/imgUpload", method = RequestMethod.POST)
	public Map<String, String> ueditorUpload(MultipartFile upfile, HttpServletRequest request) {
		try {
			// 获取用户自定义的文件夹路径
			// String upPath = request.getParameter("upPath");

			Calendar now = Calendar.getInstance();
			int year = now.get(Calendar.YEAR);
			int month = now.get(Calendar.MONTH) + 1;
			int day = now.get(Calendar.DAY_OF_MONTH);

			SysUserEntity user = ShiroUtils.getUserEntity();

			String urlPath = Global.USERFILES_BASE_URL + user.getUserId() + "/" + year + "/" + month + "/" + day;
			String realPath = Global.getUserfilesBaseDir() + urlPath;
			File dirPath = new File(realPath);
			if (!dirPath.exists()) {
				dirPath.mkdirs();
			}

			// 用原文件名，便于区分
			String filename2 = "/" + upfile.getOriginalFilename();
			File file2 = new File(realPath + filename2);

			FileUtils.copyInputStreamToFile(upfile.getInputStream(), file2);

			String urlFilePath = request.getContextPath() + urlPath + filename2;
			Map<String, String> map = new HashMap<String, String>();
			map.put("url", urlFilePath);
			map.put("size", String.valueOf(upfile.getSize()));
			map.put("type", upfile.getContentType());
			map.put("state", "SUCCESS");

			return map;
		} catch (Exception e) {
			logger.error("上传异常：{}", e);
			return new HashMap<String, String>();
		}
	}

}
