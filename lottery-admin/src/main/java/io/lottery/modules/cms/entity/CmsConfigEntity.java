package io.lottery.modules.cms.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * cms内容系统配置信息
 * 
 * @author
 * @email
 * @date 2018-07-14 14:36:34
 */
@TableName("cms_config")
public class CmsConfigEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Long id;
	/**
	 * 首页标题
	 */
	private String title;
	/**
	 * 关键字
	 */
	private String keywords;
	/**
	 * 首页描述
	 */
	private String description;
	/**
	 * 输出HTML的文件夹路径
	 */
	private String outHtmlPath;
	/**
	 * 模板文件路径
	 */
	private String ftlPath;
	/**
	 * 生成的HTML路径，相对路径
	 */
	private String generateDir;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新时间
	 */
	private Date updateDate;

	/**
	 * 代理主机
	 */
	private String proxyHost;

	/**
	 * 代理端口
	 */
	private Integer proxyPort;

	/**
	 * 用户名
	 */
	private String proxyUsername;

	/**
	 * 代理密码
	 */
	private String proxyPassword;

	/**
	 * 是否开启代理
	 */
	private Boolean proxyEnable;

	public Boolean getProxyEnable() {
		return proxyEnable;
	}

	public void setProxyEnable(Boolean proxyEnable) {
		this.proxyEnable = proxyEnable;
	}

	public Integer getProxyPort() {
		return proxyPort;
	}

	public void setProxyPort(Integer proxyPort) {
		this.proxyPort = proxyPort;
	}

	public String getProxyHost() {
		return proxyHost;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public String getProxyUsername() {
		return proxyUsername;
	}

	public void setProxyUsername(String proxyUsername) {
		this.proxyUsername = proxyUsername;
	}

	public String getProxyPassword() {
		return proxyPassword;
	}

	public void setProxyPassword(String proxyPassword) {
		this.proxyPassword = proxyPassword;
	}

	/**
	 * 设置：
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：首页标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取：首页标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置：关键字
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * 获取：关键字
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * 设置：首页描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取：首页描述
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置：输出HTML的文件夹路径
	 */
	public void setOutHtmlPath(String outHtmlPath) {
		this.outHtmlPath = outHtmlPath;
	}

	/**
	 * 获取：输出HTML的文件夹路径
	 */
	public String getOutHtmlPath() {
		return outHtmlPath;
	}

	/**
	 * 设置：模板文件路径
	 */
	public void setFtlPath(String ftlPath) {
		this.ftlPath = ftlPath;
	}

	/**
	 * 获取：模板文件路径
	 */
	public String getFtlPath() {
		return ftlPath;
	}

	/**
	 * 设置：生成的HTML路径，相对路径
	 */
	public void setGenerateDir(String generateDir) {
		this.generateDir = generateDir;
	}

	/**
	 * 获取：生成的HTML路径，相对路径
	 */
	public String getGenerateDir() {
		return generateDir;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreateDate() {
		return createDate;
	}

	/**
	 * 设置：更新时间
	 */
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	/**
	 * 获取：更新时间
	 */
	public Date getUpdateDate() {
		return updateDate;
	}
}
