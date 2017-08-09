package com.trs.model;

import java.util.List;

/**
 * 工作流自定义规则中加载用户与组织的对应关系对象，类似于formbean
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: FlowGroupAndUsers
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-21 下午08:24:15
 * @version 1.0
 */
public class FlowGroupAndUsers {

	private String groupName;
	private List<AppUser> users;
	private Long groupId;
	public List<AppUser> getUsers() {
		return users;
	}
	public void setUsers(List<AppUser> users) {
		this.users = users;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	
	public Long getGroupId() {
		return groupId;
	}
	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}
	
}
