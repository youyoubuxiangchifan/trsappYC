package com.trs.model;

import java.util.Date;


/**
 * AppFlow entity. @author MyEclipse Persistence Tools
 */

public class AppFlow  implements java.io.Serializable {


    // Fields    

     private Long flowId;
     private String flowName;
     private String flowDesc;
     private Long snodeId;
     private Long enodeId;
     private String cruser;
     private Date crtime;
     private String flowJsonData;//保存工作流程图json数据
	
	/** 
	* Description: TODO<BR> 
	*/ 
	public AppFlow() {
		this.crtime = new java.util.Date(System.currentTimeMillis());
		// TODO Auto-generated constructor stub
	}
	/** 
	* Description: TODO<BR>
	* @param flowId
	* @param flowName
	* @param flowDesc
	* @param snodeId
	* @param enodeId
	* @param cruser
	* @param crtime 
	*/ 
	public AppFlow(Long flowId, String flowName, String flowDesc,
			Long snodeId, Long enodeId, String cruser, Date crtime, String flowJsonData) {
		super();
		this.flowId = flowId;
		this.flowName = flowName;
		this.flowDesc = flowDesc;
		this.snodeId = snodeId;
		this.enodeId = enodeId;
		this.cruser = cruser;
		this.crtime = crtime;
		this.flowJsonData = flowJsonData;
	}
	public String getFlowJsonData() {
		return flowJsonData;
	}
	public void setFlowJsonData(String flowJsonData) {
		this.flowJsonData = flowJsonData;
	}
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public String getFlowName() {
		return flowName;
	}
	public void setFlowName(String flowName) {
		this.flowName = flowName;
	}
	public String getFlowDesc() {
		return flowDesc;
	}
	public void setFlowDesc(String flowDesc) {
		this.flowDesc = flowDesc;
	}
	public Long getSnodeId() {
		return snodeId;
	}
	public void setSnodeId(Long snodeId) {
		this.snodeId = snodeId;
	}
	public Long getEnodeId() {
		return enodeId;
	}
	public void setEnodeId(Long enodeId) {
		this.enodeId = enodeId;
	}
	public String getCruser() {
		return cruser;
	}
	public void setCruser(String cruser) {
		this.cruser = cruser;
	}
	public Date getCrtime() {
		return crtime;
	}
	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}
     
}