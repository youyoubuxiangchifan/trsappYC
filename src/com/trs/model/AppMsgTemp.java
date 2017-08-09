package com.trs.model;

import java.util.Date;

import com.trs.util.CMyDateTime;


/**
 * AppTemple entity. @author MyEclipse Persistence Tools
 */

public class AppMsgTemp {

	private Long tempId;
	private Integer remindType;
	private String tempTitle;
	private String tempContent;
	private Integer tempType;
	private Long appId;
	private String cruser;
	private Date crtime;
	 
	public Long getTempId() {
		return tempId;
	}
	public void setTempId(Long tempId) {
		this.tempId = tempId;
	}
	public Integer getRemindType() {
		return remindType;
	}
	public void setRemindType(Integer remindType) {
		this.remindType = remindType;
	}
	public String getTempTitle() {
		return tempTitle;
	}
	public void setTempTitle(String tempTitle) {
		this.tempTitle = tempTitle;
	}
	public String getTempContent() {
		return tempContent;
	}
	public void setTempContent(String tempContent) {
		this.tempContent = tempContent;
	}
	public Integer getTempType() {
		return tempType;
	}
	public void setTempType(Integer tempType) {
		this.tempType = tempType;
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
	
	public Long getAppId() {
		return appId;
	}
	public void setAppId(Long appId) {
		this.appId = appId;
	}
	
	/** 
	* Description: TODO<BR> 
	*/ 
	public AppMsgTemp() {
		this.crtime = new java.util.Date(System.currentTimeMillis());
	}
	/** 
	* Description: TODO<BR>
	* @param tempId
	* @param remindType
	* @param tempTitle
	* @param tempContent
	* @param tempType
	* @param appId
	* @param cruser
	* @param crtime 
	*/ 
	public AppMsgTemp(Long tempId, Integer remindType, String tempTitle,
			String tempContent, Integer tempType, Long appId, String cruser,
			Date crtime) {
		super();
		this.tempId = tempId;
		this.remindType = remindType;
		this.tempTitle = tempTitle;
		this.tempContent = tempContent;
		this.tempType = tempType;
		this.appId = appId;
		this.cruser = cruser;
		this.crtime = crtime;
	}
     
	/**
	 * 
	* Description:取得crtime，返回yyyy-MM-dd<BR>   
	* @author jin.yu
	* @date 2014-3-27 下午09:54:02
	* @return
	* @version 1.0
	 */
	public String getCrData() {
		CMyDateTime date = new CMyDateTime();
		date.setDateTime(crtime);
		return date.toString(CMyDateTime.DEF_DATE_FORMAT_PRG);
	}
	
	/**
	 * 
	* Description:通过tempType，返回模板类型<BR>   
	* @author jin.yu
	* @date 2014-3-27 下午09:54:02
	* @return String 模板类型：短信或邮件
	* @version 1.0
	 */
	public String getTempTypeName() {
		String sTempType = "";
		if(tempType==1){
			sTempType = "邮件";
		}else if(tempType==2){
			sTempType = "短信";
		}
		return sTempType;
	}
    
	/**
	 * 
	* Description: 通过remindType，返回提醒分类 <BR>   
	* @author jin.yu
	* @date 2014-4-2 下午03:41:44
	* @return String 提醒分类
	* @version 1.0
	 */
	public String getRemindTypeName() {
		String sRemindType = "";
		if(remindType==1){
			sRemindType = "办理提醒";
		}else if(remindType==2){
			sRemindType = "退回提醒";
		}else if(remindType==3){
			sRemindType = "超期提醒";
		}else if(remindType==4){
			sRemindType = "催办提醒";
		}
		return sRemindType;
	}
   
}