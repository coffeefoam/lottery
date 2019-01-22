package io.lottery.modules.cms.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 文章详表
 * 
 * @author
 * @email
 * @date 2018-07-04 21:11:40
 */
@TableName("cms_article_data")
public class CmsArticleDataEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long articleId;// 对应文章基本信息id

	public Long getArticleId() {
		return articleId;
	}

	public void setArticleId(Long articleId) {
		this.articleId = articleId;
	}

	/**
	 * 编号
	 */
	@TableId
	private Long id;
	/**
	 * 文章内容
	 */
	private String content;
	/**
	 * 文章来源
	 */
	private String copyfrom;
	/**
	 * 相关文章
	 */
	private String relation;
	/**
	 * 是否允许评论
	 */
	private String allowComment;

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
	 * 设置：文章内容
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/**
	 * 获取：文章内容
	 */
	public String getContent() {
		return content;
	}

	/**
	 * 设置：文章来源
	 */
	public void setCopyfrom(String copyfrom) {
		this.copyfrom = copyfrom;
	}

	/**
	 * 获取：文章来源
	 */
	public String getCopyfrom() {
		return copyfrom;
	}

	/**
	 * 设置：相关文章
	 */
	public void setRelation(String relation) {
		this.relation = relation;
	}

	/**
	 * 获取：相关文章
	 */
	public String getRelation() {
		return relation;
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
}
