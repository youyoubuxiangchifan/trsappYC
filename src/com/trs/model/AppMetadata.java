package com.trs.model;

import java.util.Date;


/**
 * AppMetadata entity. @author MyEclipse Persistence Tools
 */

public class AppMetadata  implements java.io.Serializable {


    // Fields    

     private Long metadataId;
     private Long groupId;
     private Date crtime;
     private String cruser;
     private String userName;
     private String trueName;
     private String userSex;
     private Short userAge;
     private String telAddress;
     private String telPhone;
     private String email;
     private String queryNumber;
     private String queryPwd;
     private String title;
     private String content;
     private Integer isPublic;
     private Integer handleType;
     private Date submitTime;
     private Integer status;
     private String replyContent;
     private String replyUser;
     private Date replyTime;


    // Constructors

    /** default constructor */
    public AppMetadata() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppMetadata(Long metadataId) {
        this.metadataId = metadataId;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param metadataId
	* @param groupId
	* @param crtime
	* @param cruser
	* @param userName
	* @param trueName
	* @param userSex
	* @param userAge
	* @param telAddress
	* @param telPhone
	* @param email
	* @param queryNumber
	* @param queryPwd
	* @param title
	* @param content
	* @param isPublic
	* @param handleType
	* @param submitTime
	* @param status
	* @param replyContent
	* @param replyUser
	* @param replyTime 
	*/ 
	public AppMetadata(Long metadataId, Long groupId, Date crtime,
			String cruser, String userName, String trueName, String userSex,
			Short userAge, String telAddress, String telPhone, String email,
			String queryNumber, String queryPwd, String title, String content,
			Integer isPublic, Integer handleType, Date submitTime,
			Integer status, String replyContent, String replyUser,
			Date replyTime) {
		this.metadataId = metadataId;
		this.groupId = groupId;
		this.crtime = crtime;
		this.cruser = cruser;
		this.userName = userName;
		this.trueName = trueName;
		this.userSex = userSex;
		this.userAge = userAge;
		this.telAddress = telAddress;
		this.telPhone = telPhone;
		this.email = email;
		this.queryNumber = queryNumber;
		this.queryPwd = queryPwd;
		this.title = title;
		this.content = content;
		this.isPublic = isPublic;
		this.handleType = handleType;
		this.submitTime = submitTime;
		this.status = status;
		this.replyContent = replyContent;
		this.replyUser = replyUser;
		this.replyTime = replyTime;
	}

	/** 
	 * @return metadataId 
	 */
	public Long getMetadataId() {
		return metadataId;
	}

	/** 
	 * 设置属性： metadataId
	 * @param metadataId 
	 */
	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}

	/** 
	 * @return groupId 
	 */
	public Long getGroupId() {
		return groupId;
	}

	/** 
	 * 设置属性： groupId
	 * @param groupId 
	 */
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
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
	 * @return userName 
	 */
	public String getUserName() {
		return userName;
	}

	/** 
	 * 设置属性： userName
	 * @param userName 
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/** 
	 * @return trueName 
	 */
	public String getTrueName() {
		return trueName;
	}

	/** 
	 * 设置属性： trueName
	 * @param trueName 
	 */
	public void setTrueName(String trueName) {
		this.trueName = trueName;
	}

	/** 
	 * @return userSex 
	 */
	public String getUserSex() {
		return userSex;
	}

	/** 
	 * 设置属性： userSex
	 * @param userSex 
	 */
	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	/** 
	 * @return userAge 
	 */
	public Short getUserAge() {
		return userAge;
	}

	/** 
	 * 设置属性： userAge
	 * @param userAge 
	 */
	public void setUserAge(Short userAge) {
		this.userAge = userAge;
	}

	/** 
	 * @return telAddress 
	 */
	public String getTelAddress() {
		return telAddress;
	}

	/** 
	 * 设置属性： telAddress
	 * @param telAddress 
	 */
	public void setTelAddress(String telAddress) {
		this.telAddress = telAddress;
	}

	/** 
	 * @return telPhone 
	 */
	public String getTelPhone() {
		return telPhone;
	}

	/** 
	 * 设置属性： telPhone
	 * @param telPhone 
	 */
	public void setTelPhone(String telPhone) {
		this.telPhone = telPhone;
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
	 * @return queryNumber 
	 */
	public String getQueryNumber() {
		return queryNumber;
	}

	/** 
	 * 设置属性： queryNumber
	 * @param queryNumber 
	 */
	public void setQueryNumber(String queryNumber) {
		this.queryNumber = queryNumber;
	}

	/** 
	 * @return queryPwd 
	 */
	public String getQueryPwd() {
		return queryPwd;
	}

	/** 
	 * 设置属性： queryPwd
	 * @param queryPwd 
	 */
	public void setQueryPwd(String queryPwd) {
		this.queryPwd = queryPwd;
	}

	/** 
	 * @return title 
	 */
	public String getTitle() {
		return title;
	}

	/** 
	 * 设置属性： title
	 * @param title 
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/** 
	 * @return content 
	 */
	public String getContent() {
		return content;
	}

	/** 
	 * 设置属性： content
	 * @param content 
	 */
	public void setContent(String content) {
		this.content = content;
	}

	/** 
	 * @return isPublic 
	 */
	public Integer getIsPublic() {
		return isPublic;
	}

	/** 
	 * 设置属性： isPublic
	 * @param isPublic 
	 */
	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	/** 
	 * @return handleType 
	 */
	public Integer getHandleType() {
		return handleType;
	}

	/** 
	 * 设置属性： handleType
	 * @param handleType 
	 */
	public void setHandleType(Integer handleType) {
		this.handleType = handleType;
	}

	/** 
	 * @return submitTime 
	 */
	public Date getSubmitTime() {
		return submitTime;
	}

	/** 
	 * 设置属性： submitTime
	 * @param submitTime 
	 */
	public void setSubmitTime(Date submitTime) {
		this.submitTime = submitTime;
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
	 * @return replyContent 
	 */
	public String getReplyContent() {
		return replyContent;
	}

	/** 
	 * 设置属性： replyContent
	 * @param replyContent 
	 */
	public void setReplyContent(String replyContent) {
		this.replyContent = replyContent;
	}

	/** 
	 * @return replyUser 
	 */
	public String getReplyUser() {
		return replyUser;
	}

	/** 
	 * 设置属性： replyUser
	 * @param replyUser 
	 */
	public void setReplyUser(String replyUser) {
		this.replyUser = replyUser;
	}

	/** 
	 * @return replyTime 
	 */
	public Date getReplyTime() {
		return replyTime;
	}

	/** 
	 * 设置属性： replyTime
	 * @param replyTime 
	 */
	public void setReplyTime(Date replyTime) {
		this.replyTime = replyTime;
	}
}