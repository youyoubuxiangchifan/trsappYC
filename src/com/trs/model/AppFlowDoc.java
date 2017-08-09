package com.trs.model;

import java.util.Date;


/**
 * AppFlowDoc entity. @author MyEclipse Persistence Tools
 */

public class AppFlowDoc{


    // Fields    

     private Long flowDocId;
     private Long nodeId;
     private Long prenodeid;
     private String postUser;
     private Date postTime;
     private String postDesc;
     private String tousers;
     private Date crtime;
     private String cruser;
     private Integer flag;
     private Long objid;
     private Date receivetime;
     private Integer worked;
     private Date worktime;
     private Long preflowdocid;
     private Integer accepted;
 	private Date accepttime;
    private String togetherusers;
    private Long appId;
    private Integer isOwnerWork;
    private String operateType;
    private Long flowStatus;
    
    
	
	/** 
	* <p>Description: TODO</p> 
	* @param flowDocId
	* @param nodeId
	* @param prenodeid
	* @param postUser
	* @param postTime
	* @param postDesc
	* @param tousers
	* @param crtime
	* @param cruser
	* @param flag
	* @param objid
	* @param receivetime
	* @param worked
	* @param worktime
	* @param preflowdocid
	* @param accepted
	* @param accepttime
	* @param togetherusers
	* @param appId
	* @param isOwnerWork
	* @param operateType
	* @param flowStatus 
	*/ 
	public AppFlowDoc(Long flowDocId, Long nodeId, Long prenodeid,
			String postUser, Date postTime, String postDesc, String tousers,
			Date crtime, String cruser, Integer flag, Long objid,
			Date receivetime, Integer worked, Date worktime, Long preflowdocid,
			Integer accepted, Date accepttime, String togetherusers,
			Long appId, Integer isOwnerWork, String operateType,
			Long flowStatus) {
		this.flowDocId = flowDocId;
		this.nodeId = nodeId;
		this.prenodeid = prenodeid;
		this.postUser = postUser;
		this.postTime = postTime;
		this.postDesc = postDesc;
		this.tousers = tousers;
		this.crtime = crtime;
		this.cruser = cruser;
		this.flag = flag;
		this.objid = objid;
		this.receivetime = receivetime;
		this.worked = worked;
		this.worktime = worktime;
		this.preflowdocid = preflowdocid;
		this.accepted = accepted;
		this.accepttime = accepttime;
		this.togetherusers = togetherusers;
		this.appId = appId;
		this.isOwnerWork = isOwnerWork;
		this.operateType = operateType;
		this.flowStatus = flowStatus;
	}
	/** 
	 * @return operateType 
	 */
	public String getOperateType() {
		return operateType;
	}
	/** 
	 * 设置属性： operateType
	 * @param operateType 
	 */
	public void setOperateType(String operateType) {
		this.operateType = operateType;
	}

	
	/** 
	* Description: TODO<BR> 
	*/ 
	public AppFlowDoc() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getFlowDocId() {
		return flowDocId;
	}
	public void setFlowDocId(Long flowDocId) {
		this.flowDocId = flowDocId;
	}
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	public Long getPrenodeid() {
		return prenodeid;
	}
	public void setPrenodeid(Long prenodeid) {
		this.prenodeid = prenodeid;
	}
	public String getPostUser() {
		return postUser;
	}
	public void setPostUser(String postUser) {
		this.postUser = postUser;
	}
	public Date getPostTime() {
		return postTime;
	}
	public void setPostTime(Date postTime) {
		this.postTime = postTime;
	}
	public String getPostDesc() {
		return postDesc;
	}
	public void setPostDesc(String postDesc) {
		this.postDesc = postDesc;
	}
	public String getTousers() {
		return tousers;
	}
	public void setTousers(String tousers) {
		this.tousers = tousers;
	}
	public Date getCrtime() {
		return crtime;
	}
	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}
	public String getCruser() {
		return cruser;
	}
	public void setCruser(String cruser) {
		this.cruser = cruser;
	}
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public Long getObjid() {
		return objid;
	}
	public void setObjid(Long objid) {
		this.objid = objid;
	}
	public Date getReceivetime() {
		return receivetime;
	}
	public void setReceivetime(Date receivetime) {
		this.receivetime = receivetime;
	}
	public Integer getWorked() {
		return worked;
	}
	public void setWorked(Integer worked) {
		this.worked = worked;
	}
	public Date getWorktime() {
		return worktime;
	}
	public void setWorktime(Date worktime) {
		this.worktime = worktime;
	}
	public Long getPreflowdocid() {
		return preflowdocid;
	}
	public void setPreflowdocid(Long preflowdocid) {
		this.preflowdocid = preflowdocid;
	}
	public Integer getAccepted() {
		return accepted;
	}
	public void setAccepted(Integer accepted) {
		this.accepted = accepted;
	}
	public Date getAccepttime() {
		return accepttime;
	}
	public void setAccepttime(Date accepttime) {
		this.accepttime = accepttime;
	}
	public String getTogetherusers() {
		return togetherusers;
	}
	public void setTogetherusers(String togetherusers) {
		this.togetherusers = togetherusers;
	}
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Integer getIsOwnerWork() {
		return isOwnerWork;
	}
	public void setIsOwnerWork(Integer isOwnerWork) {
		this.isOwnerWork = isOwnerWork;
	}
	/** 
	 * @return flowStatus 
	 */
	public Long getFlowStatus() {
		return flowStatus;
	}
	/** 
	 * 设置属性： flowStatus
	 * @param flowStatus 
	 */
	public void setFlowStatus(Long flowStatus) {
		this.flowStatus = flowStatus;
	}
}