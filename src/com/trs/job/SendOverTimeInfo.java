package com.trs.job;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.trs.model.AppOvertimeInfo;
import com.trs.service.AppBaseService;
import com.trs.service.AppSysConfigService;
import com.trs.service.MessageSend;
import com.trs.util.CMyString;
import com.trs.util.email.SimpleMail;
import com.trs.util.email.SimpleMailSender;
import com.trs.web.UserController;

/**
 * Description: 定时向超期信件管理的用户发送预警信息<BR>
 * 
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * author liujian
 * ClassName: SendOverTimeInfo
 * Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * date 2014-4-16 下午05:18:53
 * version 1.0
 */
@Service
public class SendOverTimeInfo{
	private static  Logger LOG =  Logger.getLogger(SendOverTimeInfo.class);
	@Autowired
	private AppBaseService appBaseService;
	@Autowired
	private MessageSend messageSend;
	@Autowired
	private AppSysConfigService appSysConfigService;
	@Scheduled(cron = "0 23 8 * * ?")
	public void execute() throws JobExecutionException {
		String sSelectFields = "";//查询应用表的字段
		String sFrom = "AppOvertimeInfo";//查询超期信息表的对象
		String sWhere = "(isEmailWarn = 1 and mailstatus = 0) or (isSmsWarn = 1 and smsstatus = 0)"; //查询超期信息表的条件
		String sOrder =" overtimeId desc"; //查询超期信息表排序条件
		List<Object> parameters = new ArrayList<Object>();
		List<Object> dataList = new ArrayList<Object>();
//		String smtpHostName = null;
//		String username = null;
//		String password = null;
//		try {
//			smtpHostName = appSysConfigService.findSysConfigCon("EMAILSTMP"); //"stmp.163.com"
//			username = appSysConfigService.findSysConfigCon("EMAILNAME"); //"emailName@163.com"
//			password = appSysConfigService.findSysConfigCon("EMAILPWD");
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			e1.printStackTrace();
//		} 
//		if(CMyString.isEmpty(smtpHostName)&&CMyString.isEmpty(username)&&CMyString.isEmpty(password)){
//			return;
//		}
		
//		SimpleMail simpleMail = new SimpleMail();
		try {
			dataList = appBaseService.find(sSelectFields, sFrom, sWhere, sOrder, parameters);
			for(int i=0;i<dataList.size();i++){
				AppOvertimeInfo appOvertimeInfo = new AppOvertimeInfo();
				appOvertimeInfo = (AppOvertimeInfo)dataList.get(i);
				if(appOvertimeInfo.getIsEmailWarn()==1 && appOvertimeInfo.getMailstatus()==0){//开启了邮件提醒
					//发送邮件
//					simpleMail.setSubject("办件超期提醒");
//					simpleMail.setContent(appOvertimeInfo.getEmailcontent());
					//SimpleMailSender simpleMailSender = new SimpleMailSender(smtpHostName, username, password);
//					if(simpleMailSender.sendMail(appOvertimeInfo.getEmail(), simpleMail)==1){//邮件发送成功
//						appOvertimeInfo.setMailstatus(1);
//						appBaseService.update(appOvertimeInfo);
//					}
					if(messageSend.sendEmail(appOvertimeInfo.getEmail(), "办件超期提醒", appOvertimeInfo.getEmailcontent())){
						appOvertimeInfo.setMailstatus(1);
						appBaseService.update(appOvertimeInfo);
					}
				}
				if(appOvertimeInfo.getIsSmsWarn()==1 && appOvertimeInfo.getSmsstatus()==0){//开启了短信提醒
					//发送短信
					if(messageSend.sendSms(appOvertimeInfo.getMoblie(),appOvertimeInfo.getEmailcontent())){
						System.out.println("发送短信!");
					}
					
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			LOG.error("超期提醒-取超期信息列表异常！");
			e.printStackTrace();
		}
	}
}
