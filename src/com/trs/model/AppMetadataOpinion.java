package com.trs.model;

import java.util.Date;


/**
 * AppMetadataOpinion entity. @author MyEclipse Persistence Tools
 */

public class AppMetadataOpinion  implements java.io.Serializable {


    // Fields    

     private Long opinionId;
     private Long metadataId;
     private Long mainTableId;
     private String mainTableName;
     private String userName;
     private String trueName;
     private String email;
     private String telPhone;
     private String telAddress;
     private String content;
     private Integer status;
     private Integer isPublic;
     private Date submitTime;


    // Constructors

    /** default constructor */
    public AppMetadataOpinion() {
    }

	/** minimal constructor */
    public AppMetadataOpinion(Long opinionId) {
        this.opinionId = opinionId;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param opinionId
	* @param metadataId
	* @param mainTableId
	* @param mainTableName
	* @param userName
	* @param trueName
	* @param email
	* @param telPhone
	* @param telAddress
	* @param content
	* @param status
	* @param isPublic
	* @param submitTime 
	*/ 
	public AppMetadataOpinion(Long opinionId, Long metadataId,
			Long mainTableId, String mainTableName, String userName,
			String trueName, String email, String telPhone, String telAddress,
			String content, Integer status, Integer isPublic, Date submitTime) {
		this.opinionId = opinionId;
		this.metadataId = metadataId;
		this.mainTableId = mainTableId;
		this.mainTableName = mainTableName;
		this.userName = userName;
		this.trueName = trueName;
		this.email = email;
		this.telPhone = telPhone;
		this.telAddress = telAddress;
		this.content = content;
		this.status = status;
		this.isPublic = isPublic;
		this.submitTime = submitTime;
	}

	/** 
	 * @return opinionId 
	 */
	public Long getOpinionId() {
		return opinionId;
	}

	/** 
	 * 设置属性： opinionId
	 * @param opinionId 
	 */
	public void setOpinionId(Long opinionId) {
		this.opinionId = opinionId;
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
	 * @return mainTableId 
	 */
	public Long getMainTableId() {
		return mainTableId;
	}

	/** 
	 * 设置属性： mainTableId
	 * @param mainTableId 
	 */
	public void setMainTableId(Long mainTableId) {
		this.mainTableId = mainTableId;
	}

	/** 
	 * @return mainTableName 
	 */
	public String getMainTableName() {
		return mainTableName;
	}

	/** 
	 * 设置属性： mainTableName
	 * @param mainTableName 
	 */
	public void setMainTableName(String mainTableName) {
		this.mainTableName = mainTableName;
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
    
    
}