package com.trs.service;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.dao.AppSysConfigDao;
import com.trs.model.AppInfo;

/**
 * 统计分析专用服务
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: AppReportService
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-9-1 下午04:17:45
 * @version 1.0
 */
@Service
public class AppReportService extends BaseService{
	@Autowired
	private AppSysConfigDao appSysConfigDao ;
	/**
	 * oracle 按照机构统计数据总量
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-9-1 下午04:51:24
	* Last Modified:
	* @param tableName 要查询的表名
	* @param appId 应用编号
	* @return List<Object>  eg:List<HashMap>
	* @throws SQLException
	* @throws IOException
	* @version 1.0
	 */
	public List<Object> queryGroupOralce(String tableName,Long appId) throws SQLException, IOException{
		StringBuffer strSql = new StringBuffer("select  distinct a.group_id  ,a.app_name,g.gname , count(t.metadataid)  OVER (PARTITION BY t.appid ");
		strSql.append(" ORDER BY t.appid ) as recordCount ");
		strSql.append(" from app_info a, ").append(tableName).append(" t,app_group g  where a.app_id=t.appid and g.group_id=a.group_id");
		strSql.append(" and t.appid=").append(appId);
		List<Object> result = appSysConfigDao.executeQueryObjs(strSql.toString());
		return result;
	
	}
	
	public List<Object> queryTableName() throws SQLException, IOException, Exception{
		String strSql = " select main_table_name,app_id,IS_NEED_THEME,app_name from app_info t where t.deleted = 0 ";
		return appSysConfigDao.executeQueryObjs(strSql);
	}
	public List<Object>  queryTabl_AppInfo(String tableNames) throws Exception{
		return find(null, AppInfo.class.getName(), "mainTableName=?", null, tableNames);
	}
	public List<Object> queryUsOracle(String tableName,Long appId) throws SQLException, IOException{
		StringBuffer strSql = new StringBuffer("select  distinct a.group_id , t.replyuser,g.gname ,a.app_name, count(t.metadataid)  OVER (PARTITION BY t.appid ,t.replyuser");
		strSql.append(" ORDER BY t.appid ) as recordCount ");
	    strSql.append(" from app_info a, ");
	    strSql.append(tableName).append(" t,app_group g  where a.app_id=t.appid and g.group_id=a.group_id and t.replyuser is not null ");
	    strSql.append(" and t.appid= ").append(appId);
	    List<Object> result = appSysConfigDao.executeQueryObjs(strSql.toString());
	    return result;
	}
}
