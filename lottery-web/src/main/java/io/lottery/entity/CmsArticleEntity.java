package io.lottery.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 文章表
 * 
 * @author
 * @email
 * @date 2018-07-04 21:11:41
 */
@TableName("cms_article")
public class CmsArticleEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	@TableField(exist = false)
	private CmsArticleDataEntity articleData;// 文章数据

	public CmsArticleDataEntity getArticleData() {
		return articleData;
	}

	public void setArticleData(CmsArticleDataEntity articleData) {
		this.articleData = articleData;
	}


	/**
	 * 编号
	 */
	@TableId
	private Long id;
	/**
	 * 栏目编号
	 */
	private Long categoryId;

	@TableField(exist = false)
	private String categoryName;// 栏目名称
	/**
	 * 标题
	 */
	private String title;
	/**
	 * 文章链接
	 */
	private String link;
	/**
	 * 标题颜色
	 */
	private String color;
	/**
	 * 文章图片
	 */
	private String image;
	/**
	 * 关键字
	 */
	private String keywords;
	/**
	 * 描述、摘要
	 */
	private String description;
	/**
	 * 权重，越大越靠前
	 */
	private Integer weight;
	/**
	 * 权重期限
	 */
	private Date weightDate;
	/**
	 * 点击数
	 */
	private Long hits;
	/**
	 * 推荐位，多选
	 */
	private String posid;
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
	 * 设置：栏目编号
	 */
	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}

	/**
	 * 获取：栏目编号
	 */
	public Long getCategoryId() {
		return categoryId;
	}

	/**
	 * 设置：标题
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * 获取：标题
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * 设置：文章链接
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * 获取：文章链接
	 */
	public String getLink() {
		return link;
	}

	/**
	 * 设置：标题颜色
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * 获取：标题颜色
	 */
	public String getColor() {
		return color;
	}

	/**
	 * 设置：文章图片
	 */
	public void setImage(String image) {
		this.image = image;
	}

	/**
	 * 获取：文章图片
	 */
	public String getImage() {
		return image;
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
	 * 设置：描述、摘要
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * 获取：描述、摘要
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * 设置：权重，越大越靠前
	 */
	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	/**
	 * 获取：权重，越大越靠前
	 */
	public Integer getWeight() {
		return weight;
	}

	/**
	 * 设置：权重期限
	 */
	public void setWeightDate(Date weightDate) {
		this.weightDate = weightDate;
	}

	/**
	 * 获取：权重期限
	 */
	public Date getWeightDate() {
		return weightDate;
	}

	/**
	 * 设置：点击数
	 */
	public void setHits(Long hits) {
		this.hits = hits;
	}

	/**
	 * 获取：点击数
	 */
	public Long getHits() {
		return hits;
	}

	/**
	 * 设置：推荐位，多选
	 */
	public void setPosid(String posid) {
		this.posid = posid;
	}

	/**
	 * 获取：推荐位，多选
	 */
	public String getPosid() {
		return posid;
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

	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

}
