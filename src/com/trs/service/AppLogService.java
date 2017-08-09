/**   
* Description: TODO 
* Title: AppLogService.java 
* @Package com.trs.service 
* @author jin.yu 
* @date 2014-3-20 下午03:29:05 
* @version V1.0   
*/
package com.trs.service;

import org.hibernate.envers.Audited;
import org.springframework.stereotype.Service;

import com.trs.dao.AppLogDao;
import com.trs.dbhibernate.Page;
import com.trs.model.AppLog;
import com.trs.model.AppUser;
import com.trs.util.DateUtil;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: AppLogService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-20 下午03:29:05
 * @version 1.0
 */
@Service
public class AppLogService extends BaseService{
@Audited
private AppLogDao appLogDao;
	
	/**
	 * 
	* Description: 通过传入的日志对象在日志表中新建一条日志信息<BR>   
	* @author jin.yu
	* @date 2014-3-20 下午03:39:08
	* @param appLog 日志对象 
	* @version 1.0
	 */
	public void addAppLog(AppLog appLog) {
		save(appLog);
	}
	
	/**
	 * 
	* Description:  通过传入的日志类型、日志说明、操作用户对象在日志表中新建一条日志信息<BR>   
	* @author jin.yu
	* @date 2014-3-20 下午05:12:40
	* @param logType 日志类型  1=新增,2=修改,3=删除,4=登陆
	* @param logDesc 日志说明描述
	* @param appUser 操作用户对象
	* @version 1.0
	 */
	public void addAppLog(int logType,String logDesc,AppUser appUser){
		AppLog appLog = new AppLog();
		appLog.setLogtype(logType);
		appLog.setLogdesc(logDesc);
		appLog.setLoguser(appUser.getUsername());
		appLog.setLoguserip(appUser.getIp());
		appLog.setCruser(appUser.getUsername());
		save(appLog);
	}
	
	/**
	 * 
	* Description: 通过日志ID查询日志对象信息 <BR>   
	* @author jin.yu
	* @date 2014-3-27 下午11:19:26
	* @param appLongId 日志ID
	* @return AppLog 日志对象
	* @version 1.0
	 */
	public AppLog findAppLog(Long appLongId) {
		AppLog appLog = (AppLog)findById(AppLog.class, appLongId);
		return appLog;
	}
}
