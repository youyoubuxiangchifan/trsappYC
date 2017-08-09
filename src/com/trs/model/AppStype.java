package com.trs.model;

import java.util.Date;


/**
 * AppStype entity. @author MyEclipse Persistence Tools
 */

public class AppStype  implements java.io.Serializable {


    // Fields    

     private Long appStypeId;
     private String stypeName;
     private String cruser;
     private Date crtime;


    // Constructors

    /** default constructor */
    public AppStype() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppStype(Long appStypeId) {
        this.appStypeId = appStypeId;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param appStypeId
	* @param stypeName
	* @param cruser
	* @param crtime 
	*/ 
	public AppStype(Long appStypeId, String stypeName, String cruser,
			Date crtime) {
		this.appStypeId = appStypeId;
		this.stypeName = stypeName;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	 * @return appStypeId 
	 */
	public Long getAppStypeId() {
		return appStypeId;
	}

	/** 
	 * 设置属性： appStypeId
	 * @param appStypeId 
	 */
	public void setAppStypeId(Long appStypeId) {
		this.appStypeId = appStypeId;
	}

	/** 
	 * @return stypeName 
	 */
	public String getStypeName() {
		return stypeName;
	}

	/** 
	 * 设置属性： stypeName
	 * @param stypeName 
	 */
	public void setStypeName(String stypeName) {
		this.stypeName = stypeName;
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