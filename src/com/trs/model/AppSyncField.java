package com.trs.model;

import java.util.Date;


/**
 * AppSyncField entity. @author MyEclipse Persistence Tools
 */

public class AppSyncField  implements java.io.Serializable {


    // Fields    

     private Long syncFieldId;
     private Long appId;
     private String appFieldName;
     private String appFieldDesc;
     private String toAppName;
     private String toFieldName;
     private String cruser;
     private Date crtime;


    // Constructors

    /** default constructor */
    public AppSyncField() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppSyncField(Long syncFieldId) {
        this.syncFieldId = syncFieldId;
    }

	/** 
	 * @return syncFieldId 
	 */
	public Long getSyncFieldId() {
		return syncFieldId;
	}

	/** 
	 * 设置属性： syncFieldId
	 * @param syncFieldId 
	 */
	public void setSyncFieldId(Long syncFieldId) {
		this.syncFieldId = syncFieldId;
	}

	/** 
	 * @return appId 
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
	 * @return appFieldName 
	 */
	public String getAppFieldName() {
		return appFieldName;
	}

	/** 
	 * 设置属性： appFieldName
	 * @param appFieldName 
	 */
	public void setAppFieldName(String appFieldName) {
		this.appFieldName = appFieldName;
	}

	/** 
	 * @return appFieldDesc 
	 */
	public String getAppFieldDesc() {
		return appFieldDesc;
	}

	/** 
	 * 设置属性： appFieldDesc
	 * @param appFieldDesc 
	 */
	public void setAppFieldDesc(String appFieldDesc) {
		this.appFieldDesc = appFieldDesc;
	}

	/** 
	 * @return toAppName 
	 */
	public String getToAppName() {
		return toAppName;
	}

	/** 
	 * 设置属性： toAppName
	 * @param toAppName 
	 */
	public void setToAppName(String toAppName) {
		this.toAppName = toAppName;
	}

	/** 
	 * @return toFieldName 
	 */
	public String getToFieldName() {
		return toFieldName;
	}

	/** 
	 * 设置属性： toFieldName
	 * @param toFieldName 
	 */
	public void setToFieldName(String toFieldName) {
		this.toFieldName = toFieldName;
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
	* <p>Description: TODO</p> 
	* @param syncFieldId
	* @param appId
	* @param appFieldName
	* @param appFieldDesc
	* @param toAppName
	* @param toFieldName
	* @param cruser
	* @param crtime 
	*/ 
	public AppSyncField(Long syncFieldId, Long appId, String appFieldName,
			String appFieldDesc, String toAppName, String toFieldName,
			String cruser, Date crtime) {
		this.syncFieldId = syncFieldId;
		this.appId = appId;
		this.appFieldName = appFieldName;
		this.appFieldDesc = appFieldDesc;
		this.toAppName = toAppName;
		this.toFieldName = toFieldName;
		this.cruser = cruser;
		this.crtime = crtime;
	}
    
}