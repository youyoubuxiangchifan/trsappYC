package com.trs.appcontext;

import java.util.List;
import java.util.Map;
import com.trs.model.AppInfo;
import com.trs.service.MessageSend;
import common.Logger;
/**
 * 多线程发送提醒邮件、短信的类
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: AppSynSendMessage
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-5-6 下午02:18:12
 * @version 1.0
 */
public class AppSynSendMessage  implements Runnable{
	private static Logger logger = Logger.getLogger(AppSynSendMessage.class);

	private MessageSend messageSend;
	private Long appId;

	//	private String content;
//	private String userIds;
//	private String grpIds;
	//0邮件提醒，1短信提醒
	private int mesType;
	private List<String> emails;
	private List<String> tels;
	private Long metadataId;
	

	public AppSynSendMessage(){}
	
	public AppSynSendMessage(int mesType,Long appId,MessageSend messageSend,List<String> emails,List<String> tels,Long metadataId){
		this.mesType=mesType;
		this.appId=appId;
//		this.content=content;
//		this.userIds=userIds;
//		this.grpIds=grpIds;
		this.messageSend=messageSend;
		this.emails=emails;
		this.tels=tels;
		this.metadataId=metadataId;
	}
	public void run() {
		if(mesType==0){//邮件
			sendEmail();
		}else if(mesType==1){//短信
			
		}
	}
	public void sendEmail(){
		try {
			String tableName = messageSend.getMetaTableName(appId, 0);
			AppInfo appInfo = (AppInfo)messageSend.findById(AppInfo.class, appId);
			//字段，需要标题字段 
			List<Object> tilteFileds=messageSend.queryAppFields(appInfo.getViewId(), 3, tableName);
			
			Map<String, Object> medataInfo = messageSend.getAppMedataInfo(metadataId, tableName);
			String titleFiled = (String)tilteFileds.get(0);
			String tilte =(String)medataInfo.get(titleFiled);
			messageSend.sendEmail(emails, 1, tilte);
		} catch (Exception e) {
			logger.error("提醒信息发送失败，失败原因："+e);
		}
	}
	public Long getAppId() {
		return appId;
	}

	public void setAppId(Long appId) {
		this.appId = appId;
	}

	public Long getMetadataId() {
		return metadataId;
	}

	public void setMetadataId(Long metadataId) {
		this.metadataId = metadataId;
	}
//	public String getContent() {
//		return content;
//	}
//	public void setContent(String content) {
//		this.content = content;
//	}
//	public String getUserIds() {
//		return userIds;
//	}
//	public void setUserIds(String userIds) {
//		this.userIds = userIds;
//	}
//	public String getGrpIds() {
//		return grpIds;
//	}
//	public void setGrpIds(String grpIds) {
//		this.grpIds = grpIds;
//	}
	public int getMesType() {
		return mesType;
	}
	public void setMesType(int mesType) {
		this.mesType = mesType;
	}
	public List<String> getEmails() {
		return emails;
	}
	public void setEmails(List<String> emails) {
		this.emails = emails;
	}
	public List<String> getTels() {
		return tels;
	}
	public void setTels(List<String> tels) {
		this.tels = tels;
	}
}
