package com.trs.model;

import java.util.Date;

/**
 * AppGroup entity. @author MyEclipse Persistence Tools
 */

public class AppGroup implements java.io.Serializable {

	// Fields

	private Long groupId;
	private String gname;
	private String gdesc;
	private Integer isIndependent;
	private Integer grouporder;
	private Integer containChild;
	private Long parentId;
	private String attribute;
	private String gsimname;
	private String cruser;
	private Date crtime;
	private Long independGroupId;

	// Constructors

	/** default constructor */
	public AppGroup() {
		this.crtime = new java.util.Date(System.currentTimeMillis());
	}

	/** minimal constructor */
	public AppGroup(Long groupId) {
		this.groupId = groupId;
	}



	/** 
	* <p>Description: TODO</p> 
	* @param groupId
	* @param gname
	* @param gdesc
	* @param isIndependent
	* @param grouporder
	* @param containChild
	* @param parentId
	* @param attribute
	* @param gsimname
	* @param cruser
	* @param crtime
	* @param independGroupId 
	*/ 
	public AppGroup(Long groupId, String gname, String gdesc,
			Integer isIndependent, Integer grouporder, Integer containChild,
			Long parentId, String attribute, String gsimname, String cruser,
			Date crtime, Long independGroupId) {
		this.groupId = groupId;
		this.gname = gname;
		this.gdesc = gdesc;
		this.isIndependent = isIndependent;
		this.grouporder = grouporder;
		this.containChild = containChild;
		this.parentId = parentId;
		this.attribute = attribute;
		this.gsimname = gsimname;
		this.cruser = cruser;
		this.crtime = crtime;
		this.independGroupId = independGroupId;
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
	 * @return gname 
	 */
	public String getGname() {
		return gname;
	}

	/** 
	 * 设置属性： gname
	 * @param gname 
	 */
	public void setGname(String gname) {
		this.gname = gname;
	}

	/** 
	 * @return gdesc 
	 */
	public String getGdesc() {
		return gdesc;
	}

	/** 
	 * 设置属性： gdesc
	 * @param gdesc 
	 */
	public void setGdesc(String gdesc) {
		this.gdesc = gdesc;
	}

	/** 
	 * @return isIndependent 
	 */
	public Integer getIsIndependent() {
		return isIndependent;
	}

	/** 
	 * 设置属性： isIndependent
	 * @param isIndependent 
	 */
	public void setIsIndependent(Integer isIndependent) {
		this.isIndependent = isIndependent;
	}

	/** 
	 * @return grouporder 
	 */
	public Integer getGrouporder() {
		return grouporder;
	}

	/** 
	 * 设置属性： grouporder
	 * @param grouporder 
	 */
	public void setGrouporder(Integer grouporder) {
		this.grouporder = grouporder;
	}

	/** 
	 * @return containChild 
	 */
	public Integer getContainChild() {
		return containChild;
	}

	/** 
	 * 设置属性： containChild
	 * @param containChild 
	 */
	public void setContainChild(Integer containChild) {
		this.containChild = containChild;
	}

	/** 
	 * @return parentId 
	 */
	public Long getParentId() {
		return parentId;
	}

	/** 
	 * 设置属性： parentId
	 * @param parentId 
	 */
	public void setParentId(Long parentId) {
		this.parentId = parentId;
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
	 * @return gsimname 
	 */
	public String getGsimname() {
		return gsimname;
	}

	/** 
	 * 设置属性： gsimname
	 * @param gsimname 
	 */
	public void setGsimname(String gsimname) {
		this.gsimname = gsimname;
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
	 * @return independGroupId 
	 */
	public Long getIndependGroupId() {
		return independGroupId;
	}

	/** 
	 * 设置属性： independGroupId
	 * @param independGroupId 
	 */
	public void setIndependGroupId(Long independGroupId) {
		this.independGroupId = independGroupId;
	}
	
	
}