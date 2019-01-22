package io.lottery.modules.cms.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 栏目表
 * 
 * @author
 * @email
 * @date 2018-07-04 21:11:40
 */
@TableName("cms_category")
public class CmsCategoryEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	private Long tuijianArtId;// 推荐文章id

	@TableField(exist = false)
	private String tuijianTitle;// 推荐标题

	@TableField(exist = false)
	private String tuijianContent;// 推荐内容

	private String alias;// 别名

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Long getTuijianArtId() {
		return tuijianArtId;
	}

	public void setTuijianArtId(Long tuijianArtId) {
		this.tuijianArtId = tuijianArtId;
	}

	public String getTuijianTitle() {
		return tuijianTitle;
	}

	public void setTuijianTitle(String tuijianTitle) {
		this.tuijianTitle = tuijianTitle;
	}

	public String getTuijianContent() {
		return tuijianContent;
	}

	public void setTuijianContent(String tuijianContent) {
		this.tuijianContent = tuijianContent;
	}

	@TableField(exist = false)
	private List<CmsArticleEntity> artList;// 文章列表

	public List<CmsArticleEntity> getArtList() {
		return artList;
	}

	public void setArtList(List<CmsArticleEntity> artList) {
		this.artList = artList;
	}

	/**
	 * 编号
	 */
	@TableId
	private Long id;
	/**
	 * 父级编号
	 */
	private Long parentId;

	@TableField(exist = false)
	private String parentName;// 父类名称
	/**
	 * 所有父级编号
	 */
	private String parentIds;

	/**
	 * 栏目模块
	 */
	private String module;
	/**
	 * 栏目名称
	 */
	private String name;
	/**
	 * 栏目图片
	 */
	private String image;
	/**
	 * 链接
	 */
	private String href;
	/**
	 * 目标
	 */
	private String target;
	/**
	 * 描述
	 */
	private String description;
	/**
	 * 关键字
	 */
	private String keywords;
	/**
	 * 排序（升序）
	 */
	private Integer sort;
	/**
	 * 是否在导航中显示
	 */
	private String inMenu;
	/**
	 * 是否在分类页中显示列表
	 */
	private String inList;
	/**
	 * 展现方式
	 */
	private String showModes;
	/**
	 * 是否允许评论
	 */
	private String allowComment;
	/**
	 * 是否需要审核
	 */
	private String isAudit;
	/**
	 * 自定义列表视图
	 */
	private String customListView;
	/**
	 * 自定义内容视图
	 */
	private String customContentView;
	/**
	 * 视图配置
	 */
	private String viewConfig;
	/**
	 * 创建者
	 */
	private String createBy;
	/**
	 * 创建时间
	 */
	private Date createDate;
	/**
	 * 更新者
	 */
	private String updateBy;
	/**
	 * 更新时间
	 */
	private Date updateDate;
	/**
	 * 备注信息
	 */
	private String remarks;
	/**
	 * 删除标记
	 */
	private String delFlag;

	/**
	 * 设置：编号
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 获取：编号
	 */
	public Long getId() {
		return id;
	}

	/**
	 * 设置：父级编号
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * 获取：父级编号
	 */
	public Long getParentId() {
		return parentId;
	}

	/**
	 * 设置：所有父级编号
	 */
	public void setParentIds(String parentIds) {
		this.parentIds = parentIds;
	}

	/**
	 * 获取：所有父级编号
	 */
	public String getParentIds() {
		return parentIds;
	}

	/**
	 * 设置：栏目模块
	 */
	public void setModule(String module) {
		this.module = module;
	}

	/**
	 * 获取：栏目模块
	 */
	public String getModule() {
		return module;
	}

	/**
	 * 设置：栏目名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取：栏目名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置：栏目图片
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 获取：栏目图片
	 */
	public String getImage() {
		return image;
	}

	/**
	 * 设置：链接
	 */
	public void setHref(String href) {
		this.href = href;
	}

	/**
	 * 获取：链接
	 */
	public String getHref() {
		return href;
	}

	/**
	 * 设置：目标
	 */
	public void setTarget(String target) {
		this.target = target;
	}

	/**
	 * 获取：目标
	 */
	public String getTarget() {
		return target;
	}

	/**
	 * 设置：描述
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取：描述
	 */
	public String getDescription() {
		return description;
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
	 * 设置：排序（升序）
	 */
	public void setSort(Integer sort) {
		this.sort = sort;
	}

	/**
	 * 获取：排序（升序）
	 */
	public Integer getSort() {
		return sort;
	}

	/**
	 * 设置：是否在导航中显示
	 */
	public void setInMenu(String inMenu) {
		this.inMenu = inMenu;
	}

	/**
	 * 获取：是否在导航中显示
	 */
	public String getInMenu() {
		return inMenu;
	}

	/**
	 * 设置：是否在分类页中显示列表
	 */
	public void setInList(String inList) {
		this.inList = inList;
	}

	/**
	 * 获取：是否在分类页中显示列表
	 */
	public String getInList() {
		return inList;
	}

	/**
	 * 设置：展现方式
	 */
	public void setShowModes(String showModes) {
		this.showModes = showModes;
	}

	/**
	 * 获取：展现方式
	 */
	public String getShowModes() {
		return showModes;
	}

	/**
	 * 设置：是否允许评论
	 */
	public void setAllowComment(String allowComment) {
		this.allowComment = allowComment;
	}

	/**
	 * 获取：是否允许评论
	 */
	public String getAllowComment() {
		return allowComment;
	}

	/**
	 * 设置：是否需要审核
	 */
	public void setIsAudit(String isAudit) {
		this.isAudit = isAudit;
	}

	/**
	 * 获取：是否需要审核
	 */
	public String getIsAudit() {
		return isAudit;
	}

	/**
	 * 设置：自定义列表视图
	 */
	public void setCustomListView(String customListView) {
		this.customListView = customListView;
	}

	/**
	 * 获取：自定义列表视图
	 */
	public String getCustomListView() {
		return customListView;
	}

	/**
	 * 设置：自定义内容视图
	 */
	public void setCustomContentView(String customContentView) {
		this.customContentView = customContentView;
	}

	/**
	 * 获取：自定义内容视图
	 */
	public String getCustomContentView() {
		return customContentView;
	}

	/**
	 * 设置：视图配置
	 */
	public void setViewConfig(String viewConfig) {
		this.viewConfig = viewConfig;
	}

	/**
	 * 获取：视图配置
	 */
	public String getViewConfig() {
		return viewConfig;
	}

	/**
	 * 设置：创建者
	 */
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	/**
	 * 获取：创建者
	 */
	public String getCreateBy() {
		return createBy;
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
	 * 设置：更新者
	 */
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	/**
	 * 获取：更新者
	 */
	public String getUpdateBy() {
		return updateBy;
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

	/**
	 * 设置：备注信息
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * 获取：备注信息
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * 设置：删除标记
	 */
	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	/**
	 * 获取：删除标记
	 */
	public String getDelFlag() {
		return delFlag;
	}

	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

}
