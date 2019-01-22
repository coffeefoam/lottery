package io.lottery.entity;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

/**
 * 北京赛车pk10
 * 
 * @author xg
 * @email
 * @date 2018-07-16
 */
@TableName("sys_feedback")
public class FeedbackEntity implements Serializable {

	private static final long serialVersionUID = -6851895034687072123L;
	/**
	 * id
	 */
	@TableId
	private Integer id;
	
	/**
	 * 反馈内容
	 */
	private String content;

	/**
	 * 创建时间
	 */
	private Date createtime;



	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getCreatetime() {
		return createtime;
	}


}
