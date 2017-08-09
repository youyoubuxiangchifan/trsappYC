package com.trs.model;

import java.util.Date;


/**
 * AppGrpuser entity. @author MyEclipse Persistence Tools
 */

public class AppGrpuser  implements java.io.Serializable {


    // Fields    

     private Long grpuserId;
     private Long groupId;
     private Long userId;
     private Integer isAdmin;
     private String attribute;
     private String cruser;
     private Date crtime;


    // Constructors

    /** default constructor */
    public AppGrpuser() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppGrpuser(Long grpuserId) {
        this.grpuserId = grpuserId;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param grpuserId
	* @param groupId
	* @param userId
	* @param isAdmin
	* @param attribute
	* @param cruser
	* @param crtime 
	*/ 
	public AppGrpuser(Long grpuserId, Long groupId, Long userId,
			Integer isAdmin, String attribute, String cruser, Date crtime) {
		this.grpuserId = grpuserId;
		this.groupId = groupId;
		this.userId = userId;
		this.isAdmin = isAdmin;
		this.attribute = attribute;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	 * @return grpuserId 
	 */
	public Long getGrpuserId() {
		return grpuserId;
	}

	/** 
	 * 设置属性： grpuserId
	 * @param grpuserId 
	 */
	public void setGrpuserId(Long grpuserId) {
		this.grpuserId = grpuserId;
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
	 * @return isAdmin 
	 */
	public Integer getIsAdmin() {
		return isAdmin;
	}

	/** 
	 * 设置属性： isAdmin
	 * @param isAdmin 
	 */
	public void setIsAdmin(Integer isAdmin) {
		this.isAdmin = isAdmin;
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