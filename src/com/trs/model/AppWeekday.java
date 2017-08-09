package com.trs.model;

import java.util.Date;

/**
 * AppWeekday entity. @author MyEclipse Persistence Tools
 */

public class AppWeekday implements java.io.Serializable {

	// Fields

	private Long weekdayId;
	private Integer wyear;
	private Integer wmonth;
	private Date weekday;
	private Integer wdate;
	private String cruser;
	private Date crtime;

	// Constructors

	/** default constructor */
	public AppWeekday() {
		this.crtime = new java.util.Date(System.currentTimeMillis());
	}

	/** minimal constructor */
	public AppWeekday(Long weekdayId) {
		this.weekdayId = weekdayId;
	}

	/** 
	* <p>Description: TODO</p> 
	* @param weekdayId
	* @param wyear
	* @param wmonth
	* @param weekday
	* @param wdate
	* @param cruser
	* @param crtime 
	*/ 
	public AppWeekday(Long weekdayId, Integer wyear, Integer wmonth, Date weekday,
			Integer wdate, String cruser, Date crtime) {
		this.weekdayId = weekdayId;
		this.wyear = wyear;
		this.wmonth = wmonth;
		this.weekday = weekday;
		this.wdate = wdate;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	 * @return weekdayId 
	 */
	public Long getWeekdayId() {
		return weekdayId;
	}

	/** 
	 * 设置属性： weekdayId
	 * @param weekdayId 
	 */
	public void setWeekdayId(Long weekdayId) {
		this.weekdayId = weekdayId;
	}

	/** 
	 * @return wyear 
	 */
	public Integer getWyear() {
		return wyear;
	}

	/** 
	 * 设置属性： wyear
	 * @param wyear 
	 */
	public void setWyear(Integer wyear) {
		this.wyear = wyear;
	}

	/** 
	 * @return wmonth 
	 */
	public Integer getWmonth() {
		return wmonth;
	}

	/** 
	 * 设置属性： wmonth
	 * @param wmonth 
	 */
	public void setWmonth(Integer wmonth) {
		this.wmonth = wmonth;
	}

	/** 
	 * @return weekday 
	 */
	public Date getWeekday() {
		return weekday;
	}

	/** 
	 * 设置属性： weekday
	 * @param weekday 
	 */
	public void setWeekday(Date weekday) {
		this.weekday = weekday;
	}

	/** 
	 * @return wdate 
	 */
	public Integer getWdate() {
		return wdate;
	}

	/** 
	 * 设置属性： wdate
	 * @param wdate 
	 */
	public void setWdate(Integer wdate) {
		this.wdate = wdate;
	}

	/** 
	 * @return cruser 
	 */
	public String getCruser() {
		return cruser;
	}

	/** 
	 * 设置属性： cruser
	 * @param cruser 
	 */
	public void setCruser(String cruser) {
		this.cruser = cruser;
	}

	/** 
	 * @return crtime 
	 */
	public Date getCrtime() {
		return crtime;
	}

	/** 
	 * 设置属性： crtime
	 * @param crtime 
	 */
	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}
}