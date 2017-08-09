package com.trs.model;

import java.util.Date;

/**
 * AppRecordLock entity. @author MyEclipse Persistence Tools
 */

public class AppRecordLock implements java.io.Serializable {

	// Fields

	private Long lockId;
	private String tableName;
	private Long recordId;
	private String lockUser;
	private Date lockTime;

	// Constructors

	/** default constructor */
	public AppRecordLock() {
		this.lockTime = new java.util.Date(System.currentTimeMillis());
	}

	/** minimal constructor */
	public AppRecordLock(Long lockId) {
		this.lockId = lockId;
	}

	/** 
	* <p>Description: TODO</p> 
	* @param lockId
	* @param tableName
	* @param recordId
	* @param lockUser
	* @param lockTime 
	*/ 
	public AppRecordLock(Long lockId, String tableName, Long recordId,
			String lockUser, Date lockTime) {
		this.lockId = lockId;
		this.tableName = tableName;
		this.recordId = recordId;
		this.lockUser = lockUser;
		this.lockTime = lockTime;
	}

	/** 
	 * @return lockId 
	 */
	public Long getLockId() {
		return lockId;
	}

	/** 
	 * 设置属性： lockId
	 * @param lockId 
	 */
	public void setLockId(Long lockId) {
		this.lockId = lockId;
	}

	/** 
	 * @return tableName 
	 */
	public String getTableName() {
		return tableName;
	}

	/** 
	 * 设置属性： tableName
	 * @param tableName 
	 */
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	/** 
	 * @return recordId 
	 */
	public Long getRecordId() {
		return recordId;
	}

	/** 
	 * 设置属性： recordId
	 * @param recordId 
	 */
	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	/** 
	 * @return lockUser 
	 */
	public String getLockUser() {
		return lockUser;
	}

	/** 
	 * 设置属性： lockUser
	 * @param lockUser 
	 */
	public void setLockUser(String lockUser) {
		this.lockUser = lockUser;
	}

	/** 
	 * @return lockTime 
	 */
	public Date getLockTime() {
		return lockTime;
	}

	/** 
	 * 设置属性： lockTime
	 * @param lockTime 
	 */
	public void setLockTime(Date lockTime) {
		this.lockTime = lockTime;
	}
}