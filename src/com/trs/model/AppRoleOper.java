package com.trs.model;

import java.util.Date;


/**
 * AppRoleOper entity. @author MyEclipse Persistence Tools
 */

public class AppRoleOper  implements java.io.Serializable {


    // Fields    

     private Long roleoperid;
     private Long roleId;
     private Long operId;
     private Long appId;
 	private String cruser;
    private Date crtime;
    
     public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

    // Constructors

    /** default constructor */
    public AppRoleOper() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppRoleOper(Long roleoperid) {
        this.roleoperid = roleoperid;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param roleoperid
	* @param roleId
	* @param operId
	* @param appId
	* @param cruser
	* @param crtime 
	*/ 
	public AppRoleOper(Long roleoperid, Long roleId, Long operId, Long appId,
			String cruser, Date crtime) {
		this.roleoperid = roleoperid;
		this.roleId = roleId;
		this.operId = operId;
		this.appId = appId;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	 * @return roleoperid 
	 */
	public Long getRoleoperid() {
		return roleoperid;
	}

	/** 
	 * 设置属性： roleoperid
	 * @param roleoperid 
	 */
	public void setRoleoperid(Long roleoperid) {
		this.roleoperid = roleoperid;
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
	 * @return operId 
	 */
	public Long getOperId() {
		return operId;
	}

	/** 
	 * 设置属性： operId
	 * @param operId 
	 */
	public void setOperId(Long operId) {
		this.operId = operId;
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