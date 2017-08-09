package com.trs.model;

import java.util.Date;

/**
 * AppComment entity. @author MyEclipse Persistence Tools
 */

public class AppComment implements java.io.Serializable {

	// Fields

	private Long commentId;
	private Long dataId;
	private String commentContent;
	private Date crtime;
	private Short commentScore;
	private String commentUser;
	private Integer commentStatus;
	private String checkUser;
	private Date checkTime;
	private String cruser;
	private Long appId;

	// Constructors

	/** default constructor */
	public AppComment() {
		this.crtime = new java.util.Date(System.currentTimeMillis());
	}

	/** minimal constructor */
	public AppComment(Long commentId) {
		this.commentId = commentId;
	}

	/** 
	* <p>Description: TODO</p> 
	* @param commentId
	* @param dataId
	* @param commentContent
	* @param crtime
	* @param commentScore
	* @param commentUser
	* @param commentStatus
	* @param checkUser
	* @param checkTime
	* @param cruser
	* @param appId 
	*/ 
	public AppComment(Long commentId, Long dataId, String commentContent,
			Date crtime, Short commentScore, String commentUser,
			Integer commentStatus, String checkUser, Date checkTime,
			String cruser, Long appId) {
		this.commentId = commentId;
		this.dataId = dataId;
		this.commentContent = commentContent;
		this.crtime = new java.util.Date(System.currentTimeMillis());
		this.commentScore = commentScore;
		this.commentUser = commentUser;
		this.commentStatus = commentStatus;
		this.checkUser = checkUser;
		this.checkTime = checkTime;
		this.cruser = cruser;
		this.appId = appId;
	}

	/** 
	 * @return commentId 
	 */
	public Long getCommentId() {
		return commentId;
	}

	/** 
	 * 设置属性： commentId
	 * @param commentId 
	 */
	public void setCommentId(Long commentId) {
		this.commentId = commentId;
	}

	/** 
	 * @return dataId 
	 */
	public Long getDataId() {
		return dataId;
	}

	/** 
	 * 设置属性： dataId
	 * @param dataId 
	 */
	public void setDataId(Long dataId) {
		this.dataId = dataId;
	}

	/** 
	 * @return commentContent 
	 */
	public String getCommentContent() {
		return commentContent;
	}

	/** 
	 * 设置属性： commentContent
	 * @param commentContent 
	 */
	public void setCommentContent(String commentContent) {
		this.commentContent = commentContent;
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
	 * @return commentScore 
	 */
	public Short getCommentScore() {
		return commentScore;
	}

	/** 
	 * 设置属性： commentScore
	 * @param commentScore 
	 */
	public void setCommentScore(Short commentScore) {
		this.commentScore = commentScore;
	}

	/** 
	 * @return commentUser 
	 */
	public String getCommentUser() {
		return commentUser;
	}

	/** 
	 * 设置属性： commentUser
	 * @param commentUser 
	 */
	public void setCommentUser(String commentUser) {
		this.commentUser = commentUser;
	}

	/** 
	 * @return commentStatus 
	 */
	public Integer getCommentStatus() {
		return commentStatus;
	}

	/** 
	 * 设置属性： commentStatus
	 * @param commentStatus 
	 */
	public void setCommentStatus(Integer commentStatus) {
		this.commentStatus = commentStatus;
	}

	/** 
	 * @return checkUser 
	 */
	public String getCheckUser() {
		return checkUser;
	}

	/** 
	 * 设置属性： checkUser
	 * @param checkUser 
	 */
	public void setCheckUser(String checkUser) {
		this.checkUser = checkUser;
	}

	/** 
	 * @return checkTime 
	 */
	public Date getCheckTime() {
		return checkTime;
	}

	/** 
	 * 设置属性： checkTime
	 * @param checkTime 
	 */
	public void setCheckTime(Date checkTime) {
		this.checkTime = checkTime;
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

	
}