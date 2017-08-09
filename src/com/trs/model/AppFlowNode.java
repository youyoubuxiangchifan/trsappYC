package com.trs.model;

import java.util.Date;


/**
 * AppFlowNode entity. @author MyEclipse Persistence Tools
 */

public class AppFlowNode  implements java.io.Serializable {


    // Fields    

     private Long nodeId;
     private Long flowId;
     private String nodeName;
     private String nodeDesc;
     private Integer nodeOrder;
     private Integer together;
     private Integer actions;
     private String nodeUser;
     private String nodeDep;
     private Long nodeDocStatus;
     private String nodeRole;
     private Integer limitDayNum;
     private String ruleName;
     private Integer isassign;
     private Integer isaccept;
     private Integer isemail;
     private Integer ismessage;
     private Integer isFinish;
     private String cruser;
     private Date crtime;
     private String nextNodeIds;
     private String operRuleName;
 

    // Constructors

	/** default constructor */
    public AppFlowNode() {
    	this.crtime = new java.util.Date(System.currentTimeMillis());
    }

	/** minimal constructor */
    public AppFlowNode(Long nodeId) {
        this.nodeId = nodeId;
    }

	/** 
	* <p>Description: TODO</p> 
	* @param nodeId
	* @param flowId
	* @param nodeName
	* @param nodeDesc
	* @param nodeOrder
	* @param together
	* @param actions
	* @param nodeUser
	* @param nodeDep
	* @param nodeDocStatus
	* @param nodeRole
	* @param limitDayNum
	* @param ruleName
	* @param isassign
	* @param isaccept
	* @param isemail
	* @param ismessage
	* @param isFinish
	* @param cruser
	* @param crtime
	* @param nextNodeIds
	* @param operRuleName
	* @param flowStatus 
	*/ 
	public AppFlowNode(Long nodeId, Long flowId, String nodeName,
			String nodeDesc, Integer nodeOrder, Integer together,
			Integer actions, String nodeUser, String nodeDep,
			Long nodeDocStatus, String nodeRole, Integer limitDayNum,
			String ruleName, Integer isassign, Integer isaccept,
			Integer isemail, Integer ismessage, Integer isFinish,
			String cruser, Date crtime, String nextNodeIds,
			String operRuleName) {
		this.nodeId = nodeId;
		this.flowId = flowId;
		this.nodeName = nodeName;
		this.nodeDesc = nodeDesc;
		this.nodeOrder = nodeOrder;
		this.together = together;
		this.actions = actions;
		this.nodeUser = nodeUser;
		this.nodeDep = nodeDep;
		this.nodeDocStatus = nodeDocStatus;
		this.nodeRole = nodeRole;
		this.limitDayNum = limitDayNum;
		this.ruleName = ruleName;
		this.isassign = isassign;
		this.isaccept = isaccept;
		this.isemail = isemail;
		this.ismessage = ismessage;
		this.isFinish = isFinish;
		this.cruser = cruser;
		this.crtime = crtime;
		this.nextNodeIds = nextNodeIds;
		this.operRuleName = operRuleName;
	}

	public String getNextNodeIds() {
		return nextNodeIds;
	}

	public void setNextNodeIds(String nextNodeIds) {
		this.nextNodeIds = nextNodeIds;
	}

	/** 
	 * @return nodeId 
	 */
	public Long getNodeId() {
		return nodeId;
	}

	/** 
	 * 设置属性： nodeId
	 * @param nodeId 
	 */
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}

	/** 
	 * @return flowId 
	 */
	public Long getFlowId() {
		return flowId;
	}

	/** 
	 * 设置属性： flowId
	 * @param flowId 
	 */
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}

	/** 
	 * @return nodeName 
	 */
	public String getNodeName() {
		return nodeName;
	}

	/** 
	 * 设置属性： nodeName
	 * @param nodeName 
	 */
	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	/** 
	 * @return nodeDesc 
	 */
	public String getNodeDesc() {
		return nodeDesc;
	}

	/** 
	 * 设置属性： nodeDesc
	 * @param nodeDesc 
	 */
	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}

	/** 
	 * @return nodeOrder 
	 */
	public Integer getNodeOrder() {
		return nodeOrder;
	}

	/** 
	 * 设置属性： nodeOrder
	 * @param nodeOrder 
	 */
	public void setNodeOrder(Integer nodeOrder) {
		this.nodeOrder = nodeOrder;
	}

	/** 
	 * @return together 
	 */
	public Integer getTogether() {
		return together;
	}

	/** 
	 * 设置属性： together
	 * @param together 
	 */
	public void setTogether(Integer together) {
		this.together = together;
	}

	/** 
	 * @return actions 
	 */
	public Integer getActions() {
		return actions;
	}

	/** 
	 * 设置属性： actions
	 * @param actions 
	 */
	public void setActions(Integer actions) {
		this.actions = actions;
	}

	/** 
	 * @return nodeUser 
	 */
	public String getNodeUser() {
		return nodeUser;
	}

	/** 
	 * 设置属性： nodeUser
	 * @param nodeUser 
	 */
	public void setNodeUser(String nodeUser) {
		this.nodeUser = nodeUser;
	}

	/** 
	 * @return nodeDep 
	 */
	public String getNodeDep() {
		return nodeDep;
	}

	/** 
	 * 设置属性： nodeDep
	 * @param nodeDep 
	 */
	public void setNodeDep(String nodeDep) {
		this.nodeDep = nodeDep;
	}

	/** 
	 * @return nodeDocStatus 
	 */
	public Long getNodeDocStatus() {
		return nodeDocStatus;
	}

	/** 
	 * 设置属性： nodeDocStatus
	 * @param nodeDocStatus 
	 */
	public void setNodeDocStatus(Long nodeDocStatus) {
		this.nodeDocStatus = nodeDocStatus;
	}

	/** 
	 * @return nodeRole 
	 */
	public String getNodeRole() {
		return nodeRole;
	}

	/** 
	 * 设置属性： nodeRole
	 * @param nodeRole 
	 */
	public void setNodeRole(String nodeRole) {
		this.nodeRole = nodeRole;
	}

	/** 
	 * @return limitDayNum 
	 */
	public Integer getLimitDayNum() {
		return limitDayNum;
	}

	/** 
	 * 设置属性： limitDayNum
	 * @param limitDayNum 
	 */
	public void setLimitDayNum(Integer limitDayNum) {
		this.limitDayNum = limitDayNum;
	}

	
	public String getRuleName() {
		return ruleName;
	}

	public void setRuleName(String ruleName) {
		this.ruleName = ruleName;
	}

	/** 
	 * @return isassign 
	 */
	public Integer getIsassign() {
		return isassign;
	}

	/** 
	 * 设置属性： isassign
	 * @param isassign 
	 */
	public void setIsassign(Integer isassign) {
		this.isassign = isassign;
	}

	/** 
	 * @return isaccept 
	 */
	public Integer getIsaccept() {
		return isaccept;
	}

	/** 
	 * 设置属性： isaccept
	 * @param isaccept 
	 */
	public void setIsaccept(Integer isaccept) {
		this.isaccept = isaccept;
	}

	/** 
	 * @return isemail 
	 */
	public Integer getIsemail() {
		return isemail;
	}

	/** 
	 * 设置属性： isemail
	 * @param isemail 
	 */
	public void setIsemail(Integer isemail) {
		this.isemail = isemail;
	}

	/** 
	 * @return ismessage 
	 */
	public Integer getIsmessage() {
		return ismessage;
	}

	/** 
	 * 设置属性： ismessage
	 * @param ismessage 
	 */
	public void setIsmessage(Integer ismessage) {
		this.ismessage = ismessage;
	}
	public Integer getIsFinish() {
		return isFinish;
	}

	public void setIsFinish(Integer isFinish) {
		this.isFinish = isFinish;
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
	 * @return operRuleName 
	 */
	public String getOperRuleName() {
		return operRuleName;
	}

	/** 
	 * 设置属性： operRuleName
	 * @param operRuleName 
	 */
	public void setOperRuleName(String operRuleName) {
		this.operRuleName = operRuleName;
	}
}