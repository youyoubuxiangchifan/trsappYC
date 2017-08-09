package com.trs.model;

import java.util.Date;

/**
 * AppOvertimeInfo entity. @author MyEclipse Persistence Tools
 */

public class AppOvertimeInfo implements java.io.Serializable {

	// Fields

	private Long overtimeId;
	private Long appId;
	private Long metadataId;
	private int isEmailWarn;
	private int isSmsWarn;
	private String username;
	private String moblie;
	private String email;
	private int mailstatus;
	private int smsstatus;
	private String smscontent;
	private String emailcontent;
	private Date crtime;

	// Constructors

	public Date getCrtime() {
		return crtime;
	}

	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}

	/** default constructor */
	public AppOvertimeInfo() {
	}

	/** minimal constructor */
	public AppOvertimeInfo(Long overtimeId) {
		this.overtimeId = overtimeId;
	}

	/** full constructor */


	// Property accessors

	public Long getOvertimeId() {
		return this.overtimeId;
	}

	public AppOvertimeInfo(Long overtimeId, Long appId, Long metadataId,
			int isEmailWarn, int isSmsWarn, String username, String moblie,
			String email, int mailstatus, int smsstatus, String smscontent,
			String emailcontent, Date crtime) {
		super();
		this.overtimeId = overtimeId;
		this.appId = appId;
		this.metadataId = metadataId;
		this.isEmailWarn = isEmailWarn;
		this.isSmsWarn = isSmsWarn;
		this.username = username;
		this.moblie = moblie;
		this.email = email;
		this.mailstatus = mailstatus;
		this.smsstatus = smsstatus;
		this.smscontent = smscontent;
		this.emailcontent = emailcontent;
		this.crtime = new java.util.Date(System.currentTimeMillis());
	}

	public void setOvertimeId(Long overtimeId) {
		this.overtimeId = overtimeId;
	}

	public Long getAppId() {
		return this.appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getMetadataId() {
		return this.metadataId;
	}

	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}

	public int getIsEmailWarn() {
		return this.isEmailWarn;
	}

	public void setIsEmailWarn(int isEmailWarn) {
		this.isEmailWarn = isEmailWarn;
	}

	public int getIsSmsWarn() {
		return this.isSmsWarn;
	}

	public void setIsSmsWarn(int isSmsWarn) {
		this.isSmsWarn = isSmsWarn;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMoblie() {
		return this.moblie;
	}

	public void setMoblie(String moblie) {
		this.moblie = moblie;
	}

	public String getEmail() {
		return this.email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getMailstatus() {
		return this.mailstatus;
	}

	public void setMailstatus(int mailstatus) {
		this.mailstatus = mailstatus;
	}

	public int getSmsstatus() {
		return this.smsstatus;
	}

	public void setSmsstatus(int smsstatus) {
		this.smsstatus = smsstatus;
	}

	public String getSmscontent() {
		return this.smscontent;
	}

	public void setSmscontent(String smscontent) {
		this.smscontent = smscontent;
	}

	public String getEmailcontent() {
		return this.emailcontent;
	}

	public void setEmailcontent(String emailcontent) {
		this.emailcontent = emailcontent;
	}

}