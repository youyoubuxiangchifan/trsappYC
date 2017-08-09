package com.trs.model;

import java.util.Date;

/**
 * 
 * @Description:<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jinyu
 * @ClassName: AppRoleUser
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-6 下午09:21:03
 * @version 1.0
 */

public class AppRoleUser implements java.io.Serializable {

	// Fields
	private Long roleuserId;
	private Long roleId;
	private Long userId;
	private Integer isAppAdmin;
	private String cruser;
	private Date crtime;
	
	public AppRoleUser(Long roleuserId, Long roleId, Long userId,
			Integer isAppAdmin, String cruser, Date crtime) {
		super();
		this.roleuserId = roleuserId;
		this.roleId = roleId;
		this.userId = userId;
		this.isAppAdmin = isAppAdmin;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	* <p>Description: TODO</p> 
	* @param roleuserId
	* @param roleId
	* @param userId
	* @param cruser
	* @param crtime 
	*/ 
	public AppRoleUser(Long roleuserId, Long roleId, Long userId,
			String cruser, Date crtime) {
		this.roleuserId = roleuserId;
		this.roleId = roleId;
		this.userId = userId;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	// Constructors

	/** default constructor */
	public AppRoleUser() {
		this.crtime = new java.util.Date(System.currentTimeMillis());
	}

	/** minimal constructor */
	public AppRoleUser(Long roleuserId) {
		this.roleuserId = roleuserId;
	}
	/** minimal constructor */
	public AppRoleUser(Long roleID,Long userID) {
		this.roleId = roleID;
		this.userId = userID;
	}
	/** 
	 * @return roleuserId 
	 */
	public Long getRoleuserId() {
		return roleuserId;
	}

	/** 
	 * 设置属性： roleuserId
	 * @param roleuserId 
	 */
	public void setRoleuserId(Long roleuserId) {
		this.roleuserId = roleuserId;
	}

	/** 
	 * @return roleId 
	 */
	public Long getRoleId() {
		return roleId;
	}

	/** 
	 * 设置属性： roleId
	 * @param roleId 
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/** 
	 * @return userId 
	 */
	public Long getUserId() {
		return userId;
	}

	/** 
	 * 设置属性： userId
	 * @param userId 
	 */
	public void setUserId(Long userId) {
		this.userId = userId;
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

	/**
	 * @return the isAppAdmin
	 */
	public Integer getIsAppAdmin() {
		return isAppAdmin;
	}

	/** 
	 * 设置属性： isAppAdmin
	 * @param isAppAdmin 
	 */
	public void setIsAppAdmin(Integer isAppAdmin) {
		this.isAppAdmin = isAppAdmin;
	}
	
}