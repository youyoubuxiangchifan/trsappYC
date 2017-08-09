package com.trs.model;

import java.util.Date;

import com.trs.util.CMyDateTime;

/**
 * 
 * @Description:<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jinyu
 * @ClassName: AppRole
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-6 下午09:19:59
 * @version 1.0
 */

public class AppRole implements java.io.Serializable {

	// Fields

	private Long roleId;
	private String rolename;
	private String roledesc;
	private int roletype;
	private Long appid;
	private String cruser;
	private Date crtime;
	private String appName;

	// Constructors

	/** default constructor */
	public AppRole() {
		this.crtime = new java.util.Date(System.currentTimeMillis());
	}

	/** minimal constructor */
	public AppRole(Long roleId, Long appid) {
		this.roleId = roleId;
		this.appid = appid;
	}

	/** 
	* <p>Description: TODO</p> 
	* @param roleId
	* @param rolename
	* @param roledesc
	* @param roletype
	* @param appid
	* @param cruser
	* @param crtime 
	*/ 
	public AppRole(Long roleId, String rolename, String roledesc,
			int roletype, Long appid, String cruser, Date crtime) {
		this.roleId = roleId;
		this.rolename = rolename;
		this.roledesc = roledesc;
		this.roletype = roletype;
		this.appid = appid;
		this.cruser = cruser;
		this.crtime = crtime;
	}

	/** 
	 * @return roleId 
	 */
	public Long getRoleId() {
		if(roleId==null)
			return 0l;
		else
			return roleId;
	}

	/** 
	 * 设置属性： roleId
	 * @param roleId 
	 */
	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

	/** 
	 * @return rolename 
	 */
	public String getRolename() {
		return rolename==null?"":rolename;
	}

	/** 
	 * 设置属性： rolename
	 * @param rolename 
	 */
	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

	/** 
	 * @return roledesc 
	 */
	public String getRoledesc() {
		
		return roledesc==null?"":roledesc;
	}

	/** 
	 * 设置属性： roledesc
	 * @param roledesc 
	 */
	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	/** 
	 * @return roletype 
	 */
	public int getRoletype() {
		return roletype;
	}

	/** 
	 * 设置属性： roletype
	 * @param roletype 
	 */
	public void setRoletype(Integer roletype) {
		this.roletype = roletype;
	}

	/** 
	 * @return appid 
	 */
	public Long getAppid() {
		return appid==null?0l:appid;
	}

	/** 
	 * 设置属性： appid
	 * @param appid 
	 */
	public void setAppid(Long appid) {
		this.appid = appid;
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
	public String getCrData() {
		CMyDateTime date = new CMyDateTime();
		date.setDateTime(crtime);
		return date.toString(CMyDateTime.DEF_DATE_FORMAT_PRG);
	}
	/** 
	 * 设置属性： crtime
	 * @param crtime 
	 */
	public void setCrtime(Date crtime) {
		this.crtime = crtime;
	}

	public String getAppName() {
		return appName;
	}
    
	public void setAppName(String appName) {
		this.appName = appName;
	}
	
}