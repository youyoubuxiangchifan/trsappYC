package com.trs.model;

import java.util.Date;

/**
 * AppRoleSys entity. @author MyEclipse Persistence Tools
 */

public class AppRoleSys implements java.io.Serializable {

	// Fields

	private Long roleSysId;
	private Long roleId;
	private Long appId;
	private String cruser;
	private Date crtime;

	// Constructors

	/** default constructor */
	public AppRoleSys() {
		this.crtime = new java.util.Date(System.currentTimeMillis());
	}

	/** minimal constructor */
	public AppRoleSys(Long roleSysId) {
		this.roleSysId = roleSysId;
	}

	/** full constructor */
	public AppRoleSys(Long roleSysId, Long roleId, Long appId, String cruser,
			Date crtime) {
		this.roleSysId = roleSysId;
		this.roleId = roleId;
		this.appId = appId;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	// Property accessors

	public Long getRoleSysId() {
		return this.roleSysId;
	}

	public void setRoleSysId(Long roleSysId) {
		this.roleSysId = roleSysId;
	}

	public Long getRoleId() {
		return this.roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	public Long getAppId() {
		return this.appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public String getCruser() {
		return this.cruser;
	}

	public void setCruser(String cruser) {
		this.cruser = cruser;
	}

	public Date getCrtime() {
		return this.crtime;
	}

	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}

}