package com.trs.model;

import java.util.Date;

/**
 * AppRoleSys entity. @author MyEclipse Persistence Tools
 */

public class AppRoleSysUser implements java.io.Serializable {

	// Fields

	private Long roleSysUserId;
	private Long roleId;
	private Long appId;
	private Long userId;

	// Constructors

	/** default constructor */
	public AppRoleSysUser() {
	}

	/**
	 * @return the roleSysUserId
	 */
	public Long getRoleSysUserId() {
		return roleSysUserId;
	}

	/** 
	 * 设置属性： roleSysUserId
	 * @param roleSysUserId 
	 */
	public void setRoleSysUserId(Long roleSysUserId) {
		this.roleSysUserId = roleSysUserId;
	}

	/**
	 * @return the roleId
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
	 * @return the appId
	 */
	public Long getAppId() {
		return appId;
	}

	/** 
	 * 设置属性： appId
	 * @param appId 
	 */
	public void setAppId(Long appId) {
		this.appId = appId;
	}

	/**
	 * @return the userId
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
	* Description: TODO<BR>
	* @param roleSysUserId
	* @param roleId
	* @param appId
	* @param userId
	* @param cruser
	* @param crtime 
	*/
	public AppRoleSysUser(Long roleSysUserId, Long roleId, Long appId,
			Long userId, String cruser, Date crtime) {
		super();
		this.roleSysUserId = roleSysUserId;
		this.roleId = roleId;
		this.appId = appId;
		this.userId = userId;
	}

	
}