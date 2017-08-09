/**   
* Description: TODO 
* Title: AppSysConfigService.java 
* @Package com.trs.service 
* @author jin.yu 
* @date 2014-3-19 下午03:35:14 
* @version V1.0   
*/
package com.trs.service;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.dao.AppSysConfigDao;
import com.trs.model.AppDatastatus;
import com.trs.model.AppMsgTemp;
import com.trs.model.AppOper;
import com.trs.model.AppSysConfig;
import com.trs.model.AppWeekday;
import com.trs.util.CMyString;
import com.trs.util.DateUtil;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: AppSysConfigService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-19 下午03:35:14
 * @version 1.0
 */
@Service
public class AppSysConfigService extends BaseService{
	private static  Logger LOG =  Logger.getLogger(AppSysConfigService.class);
	@Autowired
	private AppSysConfigDao appSysConfigDao ;
	/**
	 * 
	* Description: 新增一个应用操作 <BR>   
	* @author jin.yu
	* @date 2014-3-19 下午03:51:07
	* @param appOper 新增操作对象
	* @throws Exception
	* @version 1.0
	 */
	public void addAppOper(AppOper appOper) throws Exception {
		if(!existOper(appOper.getOpname())){
			appSysConfigDao.save(appOper);
		}
	}
	
	/**
	 * 
	* Description:  修改一个应用操作<BR>   
	* @author jin.yu
	* @date 2014-3-19 下午03:51:47
	* @param appOper 修改操作对象
	* @throws Exception
	* @version 1.0
	 */
	public void updateAppOper(AppOper appOper) throws Exception {
		appSysConfigDao.update(appOper);
	}
	
	/**
	 * 
	* Description: 取得所有应用操作<BR>   
	* @author jin.yu
	* @date 2014-3-19 下午07:57:42
	* @return List list中内容顺序（编号、操作名称、操作描述）
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> findAppOper() throws Exception{
		List<Object> appOperList = find("new list(operid,opname,opdesc)", AppOper.class.getName(), "", "", null);
		return appOperList;
	}
	
	/**
	 * 
	* Description: 判断系统中应用操作是否已存在<BR>   
	* @author jin.yu
	* @date 2014-3-27 下午01:45:35
	* @param operFlag 应用操作标识
	* @return boolean  true为已存在，false为不存在
	* @throws Exception
	* @version 1.0
	 */
	public boolean existOper(String operFlag) throws Exception{
		return existData(AppOper.class.getName(), "operFlag = ?", operFlag);
	}
	
	/**
	 * 
	* Description:  新增一个节点状态<BR>   
	* @author jin.yu
	* @date 2014-3-19 下午05:28:13
	* @param appDatastatus 节点状态对象
	* @throws Exception
	* @version 1.0
	 */
	public void addAppDataStatus(AppDatastatus appDatastatus) throws Exception {
		if(!existData(AppDatastatus.class.getName(), "statusname = ?", appDatastatus.getStatusname())){
			appSysConfigDao.save(appDatastatus);
		}
	}
	
	/**
	 * 
	* Description:  修改一个节点状态<BR>   
	* @author jin.yu
	* @date 2014-3-19 下午05:28:13
	* @param appDatastatus 节点状态对象
	* @throws Exception
	* @version 1.0
	 */
	public void updateAppDataStatus(AppDatastatus appDatastatus) throws Exception {
		appSysConfigDao.update(appDatastatus);
	}
	
	/**
	 * 
	* Description: 取得所有节点状态 <BR>   
	* @author jin.yu
	* @date 2014-3-19 下午08:56:57
	* @return List list中内容顺序（编号、状态名称、状态描述）
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> findAppDataStatus() throws Exception{
		List<Object> appDataStatus = find("new list(datastatusId,statusname,statusdesc)", AppDatastatus.class.getName(), "", "", null);
		return appDataStatus;
	}
	
	/**
	 * 
	* Description: 根据节点状态编号取得节点状态的名称 <BR>   
	* @author jin.yu
	* @date 2014-4-8 下午03:17:31
	* @param datastatusId 节点状态ID 
	* @return String 节点状态名称
	* @version 1.0
	 */
	public String getStatusName(Long datastatusId){
		String statusName = "";//节点状态名称
		AppDatastatus appDatastatus = (AppDatastatus)findById(AppDatastatus.class, datastatusId);//节点状态信息取得
		if (appDatastatus != null) {//判断节点信息是否存在
			statusName = appDatastatus.getStatusname();
		}
		return statusName;
	}
	
	/**
	 * 
	* Description: 根据节点状态编号取得节点状态对象 <BR>   
	* @author jin.yu
	* @date 2014-4-8 下午03:20:55
	* @param datastatusId 节点状态ID
	* @return AppDatastatus节点状态对象
	* @version 1.0
	 */
	public AppDatastatus getDataStatus(Long datastatusId) {
		AppDatastatus appDatastatus = (AppDatastatus)findById(AppDatastatus.class, datastatusId);//节点状态信息取得
		return appDatastatus;
	}
	
	/**
	 * 
	* Description: 判断节点状态是否已经存在 <BR>   
	* @author jin.yu
	* @date 2014-3-27 下午02:56:19
	* @param statusname 节点状态名称
	* @return boolean  true为已存在，false为不存在
	* @throws Exception
	* @version 1.0
	 */
	public boolean existDataStatus(String statusname) throws Exception{
		return existData(AppDatastatus.class.getName(), "statusname = ?", statusname);
	}
	
	/**
	 * 
	* Description: 新增一个用户自定义系统配置 <BR>   
	* @author jin.yu
	* @date 2014-3-20 下午02:29:29
	* @param appSysConfig 自定义系统配置对象
	* @throws Exception
	* @version 1.0
	 */
	public void addAppSysConfig(AppSysConfig appSysConfig) throws Exception{
		String configName = appSysConfig.getConfigName();
		configName = configName.toUpperCase();
		if(!existSysConfigCon(configName)){
			appSysConfig.setConfigName(configName);
			appSysConfigDao.save(appSysConfig);
		}
	}
	
	/**
	 * 
	* Description:  修改用户自定义系统配置<BR>   
	* @author jin.yu
	* @date 2014-3-20 下午02:31:26
	* @param appSysConfig 自定义系统配置对象
	* @throws Exception
	* @version 1.0
	 */
	public void updateAppSysConfig(AppSysConfig appSysConfig) throws Exception{
		String configName = appSysConfig.getConfigName();
		configName = configName.toUpperCase();
		appSysConfig.setConfigName(configName);
		appSysConfigDao.update(appSysConfig);
	}
	
	/**
	 * 
	* Description:  通过系统配置名称取得系统配置对象<BR>   
	* @author jin.yu
	* @date 2014-3-20 下午02:40:41
	* @param configName 系统配置名称
	* @return AppSysConfig 系统配置对象
	* @throws Exception
	* @version 1.0
	 */
	public AppSysConfig findAppSysConfig(String configName) throws Exception{
		Object object = findObject(AppSysConfig.class.getName(), "configName = ?", configName);
		AppSysConfig appSysConfig = new AppSysConfig();
		if(object != null){
			appSysConfig = (AppSysConfig)object;
		}else{
			throw new Exception("要查询的对象不存在！");
		}
		return appSysConfig;
	}
	
	/**
	 * 
	* Description:  通过系统配置名称取得系统配置的内容<BR>   
	* @author jin.yu
	* @date 2014-3-22 上午11:54:12
	* @param configName 系统配置名称
	* @return String 系统配置内容
	* @throws Exception
	* @version 1.0
	 */
	public String findSysConfigCon(String configName) throws Exception {
		String configValue = "";
		Object object = findObject("AppSysConfig", "configName = ?", configName);
		AppSysConfig appSysConfig = new AppSysConfig();
		if(object != null){
			appSysConfig = (AppSysConfig)object;
			configValue = appSysConfig.getConfigValue();
		}else{
			throw new Exception("要查询的对象不存在！");
		}
		return configValue;
	}
	/**
	 * 
	* Description:  通过系统配置名称取得系统配置的内容，带默认值<BR>   
	* @author jin.yu
	* @date 2014-3-22 上午11:54:12
	* @param configName 系统配置名称
	* @return String 系统配置内容
	* @throws Exception
	* @version 1.0
	 */
	public String findSysConfigCon(String configName, String defaultValue) throws Exception {
		Object object = findObject("AppSysConfig", "configName = ?", configName);
		if(object != null){
			AppSysConfig appSysConfig = (AppSysConfig)object;
			return appSysConfig.getConfigValue();
		}else{
			return defaultValue;
		}
	}
	/**
	 * 
	* Description: 判断系统配置名称是否已经存在<BR>   
	* @author jin.yu
	* @date 2014-3-27 上午09:53:53
	* @param configName 系统配置名称
	* @return boolean true为已存在，false为不存在
	* @version 1.0
	 * @throws Exception 
	 */
	public boolean existSysConfigCon(String configName) throws Exception{
		configName = configName.toUpperCase();
		return existData(AppSysConfig.class.getName(), "configName = ?", configName);
	}
	
	/**
	 * 
	* Description: 新增一个信息提醒模板<BR>   
	* @author jin.yu
	* @date 2014-4-2 上午10:45:59
	* @param appMsgTemp 提醒模板对象
	* @throws Exception
	* @version 1.0
	 */
	public void addMsgTemp(AppMsgTemp appMsgTemp) throws Exception{
		if (!existMsgTemp(appMsgTemp.getRemindType(),appMsgTemp.getTempType())) {
			appSysConfigDao.save(appMsgTemp);
		}else{
			throw new Exception("提醒模板已存在！");
		}
	}
	
	/**
	 * 
	* Description: 修改应用信息提醒模板内容 <BR>   
	* @author jin.yu
	* @date 2014-4-2 上午10:51:27
	* @param appMsgTemp 提醒模板对象
	* @version 1.0
	 */
	public void updateMsgTemp(AppMsgTemp appMsgTemp){
		appSysConfigDao.update(appMsgTemp);
	}
	
	/**
	 * 
	* Description: 通过提醒分类,应用ID,模板类型查询提醒信息模板 <BR>   
	* @author jin.yu
	* @date 2014-4-2 上午10:57:56
	* @param remindType 提醒分类 1：办理提醒，2：退回提醒，3：超期提醒，4：催办提醒
	* @param tempType 模板类型 1:邮件，2：短信
	* @return AppMsgTemp 提醒模板对象
	* @version 1.0
	 * @throws Exception 
	 */
	public AppMsgTemp findMsgTemp(int remindType, int tempType) throws Exception{
		List<Object> pList = new ArrayList<Object>();
		pList.add(remindType);
		pList.add(tempType);
		AppMsgTemp appMsgTemp = (AppMsgTemp)findObject(AppMsgTemp.class.getName(), "remindType = ? and tempType = ?", pList);
		return appMsgTemp;
	}
	
	/**
	 * 
	* Description: 判断信息提醒模板是否已存在 <BR>   
	* @author jin.yu
	* @date 2014-4-2 上午10:41:30
	* @param remindType 提醒分类 1：办理提醒，2：退回提醒，3：超期提醒，4：催办提醒
	* @param tempType 模板类型 1:邮件，2：短信
	* @return boolean true为已存在，false为不存在
	* @throws Exception
	* @version 1.0
	 */
	public boolean existMsgTemp(int remindType,int tempType) throws Exception{
		//查询条件设定
		List<Object> pList = new ArrayList<Object>();
		pList.add(remindType);
		pList.add(tempType);
		//查询数据是否已存在
		return existData(AppMsgTemp.class.getName(), "remindType = ? and tempType = ?", pList);
	}
	/**
	 * 
	* Description:查询一年的节假日 <BR>   
	* @author zhangzun
	* @date 2014-4-8 下午09:44:37
	* @param year
	* @return String 以,号隔开的日期字符串
	* @throws Exception
	* @version 1.0
	 */
	public String findWday(int year) throws Exception{
		StringBuilder result = new StringBuilder();
		List<Object> list = this.find("weekday", AppWeekday.class.getName(), "wyear=?", "weekday", year);
	    if(list!=null && list.size()>0){
	    	for(Object date : list){
	    		result.append(date.toString().subSequence(0, 10)+",");
	    	}
	    	
	    }
	    if(result.length()>1){
    	    return (String) result.substring(0, result.length()-1);
    	}
	    return result.toString();
	}
	/**
	 * 
	* Description:保存一年的节假日安排 <BR>   
	* @author zhangzun
	* @date 2014-4-8 下午09:46:42
	* @param dates ,号隔开的节假日
	* @param year 节假日的年份
	* @return
	* @version 1.0
	 * @throws Exception 
	 */
	public void saveWday(String dates,int year) throws Exception{
		if(CMyString.isEmpty(dates)){
			throw new Exception("参数不能为null");
		}
		List<Object> list = this.find("", AppWeekday.class.getName(), "wyear=?", "weekday", year);
		String[] arr_dates = dates.split(",");
		List<String> date_list = Arrays.asList(arr_dates);
		List<Object> add_dates = new ArrayList<Object>();
		List<Object> del_dates = new ArrayList<Object>();
		AppWeekday wDay = null;
		for(int i=0;i<date_list.size();i++){//比较计算新增的节假日
			boolean ishas = false;
			Date date = DateUtil.parseDate(date_list.get(i));
			for(int j=0;j<list.size();j++){
				wDay = (AppWeekday)list.get(j);
				if(date.compareTo(wDay.getWeekday())==0){
					ishas = true;
					break;
				}
					
			}
			if(!ishas){
				wDay = new  AppWeekday();
				wDay.setWyear(year);
				wDay.setWmonth(DateUtil.getMonth(date));
				wDay.setWdate(DateUtil.getDay(date));
				wDay.setWeekday(date);
				wDay.setCrtime(new Date());
				add_dates.add(wDay);
			}
		}
		for(int i=0;i<list.size();i++){
			wDay = (AppWeekday)list.get(i);
			if(!date_list.contains(DateUtil.formatDate(wDay.getWeekday()))){
				del_dates.add(wDay);
			}
		}
		appSysConfigDao.saveOrUpdateAll(add_dates);
		appSysConfigDao.deleteAll(del_dates);
	}
	
	/**
	 * 
	* Description: 取得所有的节假日数据 <BR>   
	* @author jin.yu
	* @date 2014-4-11 下午03:45:09
	* @return List 节假日期集合
	* @throws Exception
	* @version 1.0
	 */
	public List<Object> findWday() throws Exception{
		List<Object> list = this.find("weekday", AppWeekday.class.getName(), "", "", null);
	    return list;
	}
	/**
	 * 系统初始化
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-8-28 下午04:13:55
	* Last Modified:
	* @param sqlList sql语句参数
	* @throws SQLException
	* @throws IOException
	* @throws Exception
	* @version 1.0
	 */
	public void saveBaseSql(List<String> sqlList) throws SQLException, IOException, Exception{
		this.getBaseDao().saveBaseSqls(sqlList);
	}
	
}
