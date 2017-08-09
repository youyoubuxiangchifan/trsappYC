/**   
* Description: TODO 
* Title: MessageSend.java 
* @Package com.trs.service 
* @author jin.yu 
* @date 2014-4-11 下午02:41:17 
* @version V1.0   
*/
package com.trs.service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import org.apache.log4j.Logger;
import org.hibernate.dialect.Ingres10Dialect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.job.SendOverTimeInfo;
import com.trs.model.AppMsgTemp;
import com.trs.model.AppOvertimeInfo;
import com.trs.util.CMyString;
import com.trs.util.DateUtil;
import com.trs.util.email.SimpleMail;
import com.trs.util.email.SimpleMailSender;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: MessageSend
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-11 下午02:41:17
 * @version 1.0
 */

@Service
public class MessageSend extends BaseService{
	private static  Logger LOG =  Logger.getLogger(MessageSend.class);
	@Autowired
	private AppSysConfigService appSysConfigService ;
	@Autowired
	private AppBaseService appBaseService;
	
	/**
	 * 
	* Description:  <BR>   
	* @author jin.yu
	* @date 2014-4-15 下午04:46:16
	* @param crtime 信息创建时间
	* @param day 信息的超期天数
	* @return boolean true为已超期 false为未超期
	* @version 1.0
	 */
	public boolean isTimeOut(Date crtime,int day){
		//是否超期标识
		boolean timeOutFlag = false;
		try {
			//节假日取得
			List<Object> restDateList = appSysConfigService.findWday();
			//信件流转时所需的日期取得
			List<String> timeOutList = DateUtil.findDates(crtime,DateUtil.now());
			int workDate = 0;
			for (Object obj : timeOutList) {
				if (!restDateList.contains(obj)) {
					workDate++;
				}
			}
			//判断信件是否超期
			if (workDate > day) {
				timeOutFlag = true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return timeOutFlag;
	}
	/** 
	* Description: 保存 <BR>   
	* author liujian
	* date 2014-4-16 下午05:50:49
	* @param dataList
	* version 1.0
	*/
	public void saveOverTimeInfo(List<Object> dataList){
		for(int j=0;j<dataList.size();j++){//遍历关联查询出的数据
			Map fieldMap = new HashMap<String, String>();
			fieldMap = (Map)dataList.get(j);
			Date crtime = DateUtil.parseDate("2014-04-12");//创建时间(Integer)fieldMap.get("CRTIME")
			if(this.isTimeOut(crtime, 3)){//是否超期(Integer)fieldMap.get("LIMIT_DAY_NUM")
				String smscontent = "您有一条待办信息["+(String)fieldMap.get("TITLE")+"]需要办理！";
				String emailcontent = "您有一条待办信息["+(String)fieldMap.get("TITLE")+"]需要办理！";
				AppMsgTemp emailAppMsgTemp = null;
				AppMsgTemp sysAppMsgTemp = null;
				try {
					emailAppMsgTemp = appSysConfigService.findMsgTemp(3,1);//邮件提醒模板
					sysAppMsgTemp = appSysConfigService.findMsgTemp(3,2);//短信提醒模板
					smscontent = sysAppMsgTemp.getTempContent().replace("{@title}", "["+(String)fieldMap.get("TITLE")+"]");
					smscontent = smscontent.replace("{@crtime}","CRTIME" );//(String)fieldMap.get("CRTIME")
					emailcontent = emailAppMsgTemp.getTempContent().replace("{@title}", "["+(String)fieldMap.get("TITLE")+"]");
					emailcontent = emailcontent.replace("{@crtime}", "CRTIME");
				} catch (Exception e) {
					LOG.error("获取邮件模板异常！");
					e.printStackTrace();
				}				

				
				System.out.println(emailcontent);
				AppOvertimeInfo appOvertimeInfo = new AppOvertimeInfo();
				appOvertimeInfo.setAppId(Long.valueOf((String)fieldMap.get("APPID")));//应用编号
				appOvertimeInfo.setMetadataId(Long.valueOf((String)fieldMap.get("METADATAID")));//数据编号
				appOvertimeInfo.setIsEmailWarn(Integer.parseInt((String)fieldMap.get("IS_EMAIL_WARN"))); //邮件发送开关
				appOvertimeInfo.setIsSmsWarn(Integer.parseInt((String)fieldMap.get("IS_SMS_WARN"))); //短信发送开关
				appOvertimeInfo.setUsername((String)fieldMap.get("USERNAME")); //用户名
				appOvertimeInfo.setMoblie((String)fieldMap.get("MOBLIE")); //用户手机
				appOvertimeInfo.setEmail((String)fieldMap.get("EMAIL")); //用户邮箱
				appOvertimeInfo.setSmsstatus(0); //短信发送状态
				appOvertimeInfo.setMailstatus(0); //邮件发送状态
				appOvertimeInfo.setSmscontent(smscontent);
				appOvertimeInfo.setEmailcontent(emailcontent);
				appBaseService.save(appOvertimeInfo);
			}						 
		}
	}
	/** 
	* Description: 群发邮件 <BR>   
	* author liujian
	* date 2014-4-23 上午11:28:31
	* @param recipients 接收人邮箱集合 List
	* @param type 模板类型 int 1：办理提醒 、2：退回提醒 、3：超期提醒 、4：催办提醒
	* @param dataTitle  数据标题-提醒数据的标题
	* @return 
	* version 1.0
	*/
	public boolean sendEmail(List recipients,int type,String dataTitle){
		boolean sendFlag = true;
		String smtpHostName = null;
		String username = null;
		String password = null;
		String subject="";
		String emailContent="";
		AppMsgTemp emailAppMsgTemp = null;
		try {
			smtpHostName = appSysConfigService.findSysConfigCon("EMAILSTMP"); //"stmp.163.com"
			username = appSysConfigService.findSysConfigCon("EMAILNAME"); //"emailName@163.com"
			password = appSysConfigService.findSysConfigCon("EMAILPWD");
			emailAppMsgTemp = appSysConfigService.findMsgTemp(type,1);//邮件提醒模板
			subject=emailAppMsgTemp.getTempTitle();
			emailContent=emailAppMsgTemp.getTempContent().replace("{@title}", "["+dataTitle+"]");
		} catch (Exception e1) {
			LOG.error("获取邮件模板异常！");
			e1.printStackTrace();
		}
		if(CMyString.isEmpty(smtpHostName)||CMyString.isEmpty(username)||CMyString.isEmpty(password)||recipients.size()<=0){
			return false;
		}
		SimpleMail simpleMail = new SimpleMail();
		simpleMail.setSubject(subject);
		simpleMail.setContent(emailContent);
		SimpleMailSender simpleMailSender = new SimpleMailSender(smtpHostName, username, password);
		try {
			simpleMailSender.send(recipients, simpleMail);
		} catch (AddressException e) {
			LOG.error("群发邮件异常！");
			sendFlag = false;
			e.printStackTrace();
		} catch (MessagingException e) {
			LOG.error("群发邮件异常！");
			sendFlag = false;
			e.printStackTrace();
		}
		return sendFlag;
	}
	/** 
	* Description: 群发邮件 <BR>   
	* author liujian
	* date 2014-4-23 上午11:33:02
	* @param recipients 接收人邮箱集合 List
	* @param subject 邮件主题
	* @param emailContent 邮件内容
	* @return
	* version 1.0
	*/
	public boolean sendEmail(List recipients,String subject,String emailContent){
		boolean sendFlag = true;
		String smtpHostName = null;
		String username = null;
		String password = null;

		try {
			smtpHostName = appSysConfigService.findSysConfigCon("EMAILSTMP"); //"stmp.163.com"
			username = appSysConfigService.findSysConfigCon("EMAILNAME"); //"emailName@163.com"
			password = appSysConfigService.findSysConfigCon("EMAILPWD");
		} catch (Exception e1) {
			LOG.error("获取邮件模板异常！");
			e1.printStackTrace();
		}
		if(CMyString.isEmpty(smtpHostName)||CMyString.isEmpty(username)||CMyString.isEmpty(password)||recipients.size()<=0){
			return false;
		}
		SimpleMail simpleMail = new SimpleMail();
		simpleMail.setSubject(subject);
		simpleMail.setContent(emailContent);
		SimpleMailSender simpleMailSender = new SimpleMailSender(smtpHostName, username, password);
		try {
			simpleMailSender.send(recipients, simpleMail);
		} catch (AddressException e) {
			LOG.error("群发邮件异常！");
			sendFlag = false;
			e.printStackTrace();
		} catch (MessagingException e) {
			LOG.error("群发邮件异常！");
			sendFlag = false;
			e.printStackTrace();
		}
		return sendFlag;
	}
	/** 
	* Description: 单发邮件 <BR>   
	* author liujian
	* date 2014-4-23 上午11:33:02
	* @param recipients 接收人邮箱集合 List
	* @param subject 邮件主题
	* @param emailContent 邮件内容
	* @return
	* version 1.0
	*/
	public boolean sendEmail(String email,String subject,String emailContent){
		boolean sendFlag = true;
		String smtpHostName = null;
		String username = null;
		String password = null;

		try {
			smtpHostName = appSysConfigService.findSysConfigCon("EMAILSTMP"); //"stmp.163.com"
			username = appSysConfigService.findSysConfigCon("EMAILNAME"); //"emailName@163.com"
			password = appSysConfigService.findSysConfigCon("EMAILPWD");
		} catch (Exception e1) {
			LOG.error("获取邮件模板异常！");
			e1.printStackTrace();
		}
		if(CMyString.isEmpty(smtpHostName)||CMyString.isEmpty(username)||CMyString.isEmpty(password)||CMyString.isEmpty(email)){
			return false;
		}
		SimpleMail simpleMail = new SimpleMail();
		simpleMail.setSubject(subject);
		simpleMail.setContent(emailContent);
		SimpleMailSender simpleMailSender = new SimpleMailSender(smtpHostName, username, password);
		try {
			simpleMailSender.send(email, simpleMail);
		} catch (AddressException e) {
			LOG.error("发送邮件异常！");
			sendFlag = false;
			e.printStackTrace();
		} catch (MessagingException e) {
			LOG.error("发送邮件异常！");
			sendFlag = false;
			e.printStackTrace();
		}
		return sendFlag;
	}
	/** 
	* Description: 群发短信 <BR>   
	* author liujian
	* date 2014-4-23 上午11:28:31
	* @param recipients 接收人手机号集合 List
	* @param type 模板类型 int 1：办理提醒 、2：退回提醒 、3：超期提醒 、4：催办提醒
	* @param dataTitle  数据标题-提醒数据的标题
	* @return 
	* version 1.0
	*/
	public boolean sendSms(List recipients,int type,String dataTitle){
		boolean sendFlag = true;
		String smsContent="";
		AppMsgTemp sysAppMsgTemp = null;
		try {
			sysAppMsgTemp = appSysConfigService.findMsgTemp(type,2);//短信提醒模板
			smsContent = sysAppMsgTemp.getTempContent().replace("{@title}", "["+dataTitle+"]");
		} catch (Exception e1) {
			LOG.error("获取短信模板异常！");
			e1.printStackTrace();
		}
		if(recipients.size()<=0||sysAppMsgTemp==null){
			return false;
		}
		//暂时未实现发送短信
		return sendFlag;
	}	
	/** 
	* Description: 单发短信 <BR>   
	* author liujian
	* date 2014-4-23 上午11:28:31
	* @param moblie 接收人手机号
	* @param dataTitle  数据标题-提醒数据的标题
	* @return 
	* version 1.0
	*/
	public boolean sendSms(String moblie,String smsContent){
		boolean sendFlag = true;
		if(CMyString.isEmpty(moblie)||CMyString.isEmpty(smsContent)){
			return false;
		}
		//暂时未实现发送短信
		sendFlag = false;
		return sendFlag;
	}	
}
