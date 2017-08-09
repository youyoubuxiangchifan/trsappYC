package com.trs.job;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import sun.util.logging.resources.logging;

import com.trs.model.AppInfo;
import com.trs.service.AppBaseService;
import com.trs.service.AppFlowService;
import com.trs.service.AppSysConfigService;
import com.trs.service.MessageSend;
import com.trs.web.UserController;

/**
 * Description: 定时从数据库中取出超期信件，保存到超期信息表中<BR>
 * 
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * author liujian
 * ClassName: DataOverTimeInfo
 * Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * date 2014-4-16 下午05:17:26
 * version 1.0
 */
@Service
public class DataOverTimeInfo{
	private static  Logger LOG =  Logger.getLogger(DataOverTimeInfo.class);
	@Autowired
	private AppBaseService appBaseService;
	@Autowired
	private MessageSend messageSend;
	@Autowired
	private AppSysConfigService appSysConfigService;
	@Scheduled(cron = "0 23 0 * * ?")
	public void execute() throws JobExecutionException {
		String sSelectFields = "";//查询应用表的字段
		String sFrom = "AppInfo";//查询应用表的对象
		String sWhere = " (isEmailWarn = 1 or isSmsWarn = 1) and appStatus = 0 and deleted = 0 "; //查询应用表的条件
		String sOrder =" crtime desc"; //查询应用表排序条件
		List<Object> parameters = new ArrayList<Object>();
		List<Object> appList = new ArrayList<Object>();	
		try {
			appList = appBaseService.find(sSelectFields, sFrom, sWhere, sOrder, parameters);
		} catch (Exception e) {
			LOG.error("超期提醒-取应用列表异常！");
			e.printStackTrace();
		}
		
		for(int i=0;i<appList.size();i++){
			AppInfo appinfo = new AppInfo();
			appinfo = (AppInfo)appList.get(i);
			String tableName = appinfo.getMainTableName()+" ta ";
			List<Object> dataList = new ArrayList<Object>();
			System.out.println(appinfo.getFlowId());
			if(appinfo.getFlowId()!=0){//有工作流
				String sOverFields = "select ta.appId,ta.metadataid,ta.title,ta.crtime,af.app_name,af.is_email_warn,af.is_sms_warn,af.limit_day_num,au.user_id,au.username,au.moblie,au.email";
				String sOverFrom = " from app_flow_doc ad,app_user au,appmetatableszmessage ta,app_info af";
				String sOverWhere = "  where ad.tousers = au.username and ad.objid = ta.metadataid and ad.app_id = ta.appid and af.app_id = ta.appid and ad.worked = 0 and ta.deleted =0";			 
				try {
					dataList=appBaseService.getBaseDao().find(sOverFields+sOverFrom+sOverWhere, parameters);
					messageSend.saveOverTimeInfo(dataList);
				} catch (Exception e) {
					LOG.error("超期提醒-取有工作流数据列表异常！");
					e.printStackTrace();
				}
			}else{//没有工作流
				String sOverFields = "select ta.appId,ta.metadataid,ta.title,af.is_email_warn,af.is_sms_warn,af.limit_day_num,ta.crtime,au.user_id,au.username,au.moblie,au.email ";
				String sOverFrom = "from "+tableName+", app_info af,app_user au,app_role_sys ars,app_role_user aru";
				String sOverWhere = " where af.app_Id = ta.appId and af.app_Id = ars.app_Id and ars.role_Id = aru.role_Id and aru.user_Id = au.user_Id and af.app_Status = 0 and " +
						" af.deleted=0 and au.is_deleted = 0 and (af.is_email_warn=1 or af.is_sms_warn=1) and ta.deleted=0 and ta.status=0";			 
				try {
					System.out.println(sOverFields+sOverFrom+sOverWhere);
					dataList=appBaseService.getBaseDao().find(sOverFields+sOverFrom+sOverWhere, parameters);
					messageSend.saveOverTimeInfo(dataList);				
				} catch (Exception e) {
					// TODO Auto-generated catch block
					LOG.error("超期提醒-取无工作流数据列表异常！");
					e.printStackTrace();
				}
				
				
			}
			
			
		}
	 
	}
}
