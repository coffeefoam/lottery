package io.lottery.modules.lottery.entity;

import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;

import java.io.Serializable;
import java.util.Date;

/**
 * 六合彩开奖数据
 * 
 * @author
 * @email
 * @date 2018-09-08 08:46:46
 */
@TableName("t_bs_liuhe")
public class LiuheEntity implements Serializable {
	private static final long serialVersionUID = 1L;

	/**
	 * 
	 */
	@TableId
	private Integer id;
	/**
	 * 
	 */
	private Date time;
	/**
	 * 期数
	 */
	private String periods;
	/**
	 * 平码1
	 */
	private String num1;
	/**
	 * 平码1
	 */
	private String num2;
	/**
	 * 平码1
	 */
	private String num3;
	/**
	 * 平码1
	 */
	private String num4;
	/**
	 * 平码1
	 */
	private String num5;
	/**
	 * 平码1
	 */
	private String num6;
	/**
	 * 特码
	 */
	private String num7;
	/**
	 * 创建时间
	 */
	private Date createtime;
	/**
	 * 加工时间
	 */
	private Date processtime;
	/**
	 * 开奖时间
	 */
	private Date starttime;

	/**
	 * 设置：
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * 获取：
	 */
	public Integer getId() {
		return id;
	}

	/**
	 * 设置：
	 */
	public void setTime(Date time) {
		this.time = time;
	}

	/**
	 * 获取：
	 */
	public Date getTime() {
		return time;
	}

	/**
	 * 设置：期数
	 */
	public void setPeriods(String periods) {
		this.periods = periods;
	}

	/**
	 * 获取：期数
	 */
	public String getPeriods() {
		return periods;
	}

	/**
	 * 设置：平码1
	 */
	public void setNum1(String num1) {
		this.num1 = num1;
	}

	/**
	 * 获取：平码1
	 */
	public String getNum1() {
		return num1;
	}

	/**
	 * 设置：平码1
	 */
	public void setNum2(String num2) {
		this.num2 = num2;
	}

	/**
	 * 获取：平码1
	 */
	public String getNum2() {
		return num2;
	}

	/**
	 * 设置：平码1
	 */
	public void setNum3(String num3) {
		this.num3 = num3;
	}

	/**
	 * 获取：平码1
	 */
	public String getNum3() {
		return num3;
	}

	/**
	 * 设置：平码1
	 */
	public void setNum4(String num4) {
		this.num4 = num4;
	}

	/**
	 * 获取：平码1
	 */
	public String getNum4() {
		return num4;
	}

	/**
	 * 设置：平码1
	 */
	public void setNum5(String num5) {
		this.num5 = num5;
	}

	/**
	 * 获取：平码1
	 */
	public String getNum5() {
		return num5;
	}

	/**
	 * 设置：平码1
	 */
	public void setNum6(String num6) {
		this.num6 = num6;
	}

	/**
	 * 获取：平码1
	 */
	public String getNum6() {
		return num6;
	}

	public String getNum7() {
		return num7;
	}

	public void setNum7(String num7) {
		this.num7 = num7;
	}

	/**
	 * 设置：创建时间
	 */
	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	/**
	 * 获取：创建时间
	 */
	public Date getCreatetime() {
		return createtime;
	}

	/**
	 * 设置：加工时间
	 */
	public void setProcesstime(Date processtime) {
		this.processtime = processtime;
	}

	/**
	 * 获取：加工时间
	 */
	public Date getProcesstime() {
		return processtime;
	}

	/**
	 * 设置：开奖时间
	 */
	public void setStarttime(Date starttime) {
		this.starttime = starttime;
	}

	/**
	 * 获取：开奖时间
	 */
	public Date getStarttime() {
		return starttime;
	}
}
