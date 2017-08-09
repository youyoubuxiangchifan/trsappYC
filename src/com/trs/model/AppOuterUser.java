package com.trs.model;

import java.util.Date;


/**
 * AppOuterUser entity. @author MyEclipse Persistence Tools
 */

public class AppOuterUser  implements java.io.Serializable {


    // Fields    

     private Long userId;
     private String username;
     private String password;
     private String truename;
     private String moblie;
     private String email;
     private String address;
     private String zipcode;
     private Integer status;
     private Date lastModify;
     private Date loginTime;
     private Date logoutTime;
     private Integer isDeleted;
     private String attribute;
     private String cruser;
     private Date crtime;


    // Constructors

    /** default constructor */
    public AppOuterUser() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppOuterUser(Long userId) {
        this.userId = userId;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param userId
	* @param username
	* @param password
	* @param truename
	* @param moblie
	* @param email
	* @param address
	* @param zipcode
	* @param status
	* @param lastModify
	* @param loginTime
	* @param logoutTime
	* @param isDeleted
	* @param attribute
	* @param cruser
	* @param crtime 
	*/ 
	public AppOuterUser(Long userId, String username, String password,
			String truename, String moblie, String email, String address,
			String zipcode, Integer status, Date lastModify, Date loginTime,
			Date logoutTime, Integer isDeleted, String attribute,
			String cruser, Date crtime) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.truename = truename;
		this.moblie = moblie;
		this.email = email;
		this.address = address;
		this.zipcode = zipcode;
		this.status = status;
		this.lastModify = lastModify;
		this.loginTime = loginTime;
		this.logoutTime = logoutTime;
		this.isDeleted = isDeleted;
		this.attribute = attribute;
		this.cruser = cruser;
		this.crtime = crtime;
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
	 * @return username 
	 */
	public String getUsername() {
		return username;
	}

	/** 
	 * 设置属性： username
	 * @param username 
	 */
	public void setUsername(String username) {
		this.username = username;
	}

	/** 
	 * @return password 
	 */
	public String getPassword() {
		return password;
	}

	/** 
	 * 设置属性： password
	 * @param password 
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/** 
	 * @return truename 
	 */
	public String getTruename() {
		return truename;
	}

	/** 
	 * 设置属性： truename
	 * @param truename 
	 */
	public void setTruename(String truename) {
		this.truename = truename;
	}

	/** 
	 * @return moblie 
	 */
	public String getMoblie() {
		return moblie;
	}

	/** 
	 * 设置属性： moblie
	 * @param moblie 
	 */
	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}

	/** 
	 * @return email 
	 */
	public String getEmail() {
		return email;
	}

	/** 
	 * 设置属性： email
	 * @param email 
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/** 
	 * @return address 
	 */
	public String getAddress() {
		return address;
	}

	/** 
	 * 设置属性： address
	 * @param address 
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/** 
	 * @return zipcode 
	 */
	public String getZipcode() {
		return zipcode;
	}

	/** 
	 * 设置属性： zipcode
	 * @param zipcode 
	 */
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	/** 
	 * @return status 
	 */
	public Integer getStatus() {
		return status;
	}

	/** 
	 * 设置属性： status
	 * @param status 
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/** 
	 * @return lastModify 
	 */
	public Date getLastModify() {
		return lastModify;
	}

	/** 
	 * 设置属性： lastModify
	 * @param lastModify 
	 */
	public void setLastModify(Date lastModify) {
		this.lastModify = lastModify;
	}

	/** 
	 * @return loginTime 
	 */
	public Date getLoginTime() {
		return loginTime;
	}

	/** 
	 * 设置属性： loginTime
	 * @param loginTime 
	 */
	public void setLoginTime(Date loginTime) {
		this.loginTime = loginTime;
	}

	/** 
	 * @return logoutTime 
	 */
	public Date getLogoutTime() {
		return logoutTime;
	}

	/** 
	 * 设置属性： logoutTime
	 * @param logoutTime 
	 */
	public void setLogoutTime(Date logoutTime) {
		this.logoutTime = logoutTime;
	}

	/** 
	 * @return isDeleted 
	 */
	public Integer getIsDeleted() {
		return isDeleted;
	}

	/** 
	 * 设置属性： isDeleted
	 * @param isDeleted 
	 */
	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	/** 
	 * @return attribute 
	 */
	public String getAttribute() {
		return attribute;
	}

	/** 
	 * 设置属性： attribute
	 * @param attribute 
	 */
	public void setAttribute(String attribute) {
		this.attribute = attribute;
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