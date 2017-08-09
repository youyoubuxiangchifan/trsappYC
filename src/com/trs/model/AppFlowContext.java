package com.trs.model;

/**
 * 工作流自定义规则及自定义操作系统传参对象
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: AppFlowContext
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-22 下午07:50:56
 * @version 1.0
 */
public class AppFlowContext {
	/**
	 * 应用ID 不为空
	 */
	private Long appId; 
	/**
	 * //工作流ID 不为空
	 */
	private Long flowId;
	/**
	 * 当前节点编号 不为空
	 */
	private Long nodeId;
	/**
	 *下一个节点编号 初始化节点一定为空
	 */
	private Long nextNodeId;
	/**
	 * 数据编号  初始化节点一定为空
	 */
	private Long metadateId; 
	/**
	 * 当前流转轨迹编号  初始化节点一定为空
	 */
	private Long flowdocId;
	/**
	 * 操作 
	 */
	private String operType;
	/**
	 * 当前操作用户  前台提交数据触发工作流为空
	 */
	private AppUser loginUser;
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	public Long getFlowId() {
		return flowId;
	}
	public void setFlowId(Long flowId) {
		this.flowId = flowId;
	}
	public Long getNodeId() {
		return nodeId;
	}
	public void setNodeId(Long nodeId) {
		this.nodeId = nodeId;
	}
	public Long getNextNodeId() {
		return nextNodeId;
	}
	public void setNextNodeId(Long nextNodeId) {
		this.nextNodeId = nextNodeId;
	}
	public Long getMetadateId() {
		return metadateId;
	}
	public void setMetadateId(Long metadateId) {
		this.metadateId = metadateId;
	}
	public Long getFlowdocId() {
		return flowdocId;
	}
	public void setFlowdocId(Long flowdocId) {
		this.flowdocId = flowdocId;
	}
	public String getOperType() {
		return operType;
	}
	public void setOperType(String operType) {
		this.operType = operType;
	}
	public AppUser getLoginUser() {
		return loginUser;
	}
	public void setLoginUser(AppUser loginUser) {
		this.loginUser = loginUser;
	}
	
	
}
