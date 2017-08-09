package com.trs.webservice.impl;

import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;


import org.springframework.beans.factory.annotation.Autowired;

import com.trs.model.AppFieldRel;
import com.trs.model.AppInfo;
import com.trs.service.PublicAppBaseService;
import com.trs.util.CMyString;
import com.trs.util.DateUtil;
import com.trs.util.Global;
import com.trs.util.jsonUtil;
import com.trs.webservice.AppDataWebService;
import com.trs.webservice.WSConstant;
@WebService(endpointInterface = "com.trs.webservice.AppDataWebService")
public class AppDataWebServiceImpl implements AppDataWebService {
	@Autowired
	private PublicAppBaseService pubAppBaseService;
	public String saveOrUpdateAppData(String jsonData, String appType) {
		Map<String, Object> appData = jsonUtil.parseJSON2Map(jsonData);//new HashMap<String, Object>();
		if(appData.isEmpty()){
			return WSConstant.NO_PARAM;
		}
		long appId = 0;
		if(appData.get("APPID") == null){
			return WSConstant.NO_PARAM;
		}
		appId = Long.valueOf(String.valueOf(appData.get("APPID")));
		AppInfo appInfo = (AppInfo)pubAppBaseService.findById(AppInfo.class, appId);
		if(appInfo == null){
			return WSConstant.NO_PARAM;
		}
		String tableName = null;
		if("1".equals(appType)){
			tableName = appInfo.getMainTableName();
		}else if("2".equals(appType)){
			tableName = appInfo.getItemTableName();
			if(appData.get("THEMEID") == null){
				return WSConstant.NO_PARAM;
			}
		}
		if(CMyString.isEmpty(tableName)){
			return WSConstant.NO_META;
		}
		//处理特殊类型字段
		Map<String, String> fieldTypeMap = getFieldTypeByViewId(appInfo.getViewId());
		if(fieldTypeMap == null){
			return WSConstant.SYS_ERROR;
		}
//		System.out.println(fieldTypeMap);
		long metaDataId = 0;
		StringBuffer sSql = new StringBuffer();
		List<Object> parameters  = new ArrayList<Object>();
		//字段拼接
		StringBuffer sFileds = new StringBuffer("");
		//? 参数拼接
		StringBuffer params = new StringBuffer();
		//新增操作
		sSql.append("insert into ").append(tableName).append("(");
		try {
			for (String key : appData.keySet()) {
				sFileds.append(key).append(",");
				params.append("?").append(",");
				if(!CMyString.isEmpty((String)fieldTypeMap.get(key.toUpperCase())) && fieldTypeMap.get(key.toUpperCase()).equals("date") && !CMyString.isEmpty((String)appData.get(key))){
					parameters.add(DateUtil.nowSqlDate((String)appData.get(key)));
				}else{
					parameters.add(appData.get(key));
				}
			}
			if(Global.DB_TYPE.equals("oracle"))
				sSql.append(" METADATAID,");
			sSql.append(sFileds.toString().substring(0,sFileds.length()-1)).append(")").append(" values(");
			if(Global.DB_TYPE.equals("oracle"))
				sSql.append(" HIBERNATE_SEQUENCE.nextval, ");
			sSql.append(params.toString().substring(0,params.length()-1)).append(")");
			metaDataId = pubAppBaseService.getBaseDao().saveBaseSql(sSql.toString(), parameters);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return WSConstant.SYS_ERROR;
		}
		/*long metaDataId = 0;
		try {
			metaDataId = pubAppBaseService.saveOrUpdateMetadata(appData, tableName);
		} catch (SQLException e) {
			e.printStackTrace();
			return WSConstant.SYS_ERROR;
		}*/
		return String.valueOf(metaDataId);
	}
	/* (non-Javadoc)
	 * @see com.trs.webservice.AppDataWebService#saveAppDataComment(java.lang.String)
	 */
	public String saveAppDataComment(String jsonData) {
		Map<String, Object> commentData = jsonUtil.parseJSON2Map(jsonData);//new HashMap<String, Object>();
		if(commentData.isEmpty()){
			return WSConstant.NO_PARAM;
		}
		if(commentData.get("APP_ID") == null){
			return WSConstant.NO_PARAM;
		}
		if(CMyString.isEmpty((String)commentData.get("DATA_ID"))){
			return WSConstant.NO_PARAM;
		}
		StringBuffer sSql = new StringBuffer();
		List<Object> parameters  = new ArrayList<Object>();
		//字段拼接
		StringBuffer sFileds = new StringBuffer("");
		//? 参数拼接
		StringBuffer params = new StringBuffer();
		//新增操作
		sSql.append("insert into ").append("app_comment").append("(");
		for (String key : commentData.keySet()) {
			sFileds.append(key).append(",");
			params.append("?").append(",");
			if("CRTIME".equals(key) && commentData.get(key) != null){
				try {
					parameters.add(DateUtil.nowSqlDate((String)commentData.get(key)));
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					return WSConstant.SYS_ERROR;
				}
			}else{
				parameters.add(commentData.get(key));
			}
		}
		if(Global.DB_TYPE.equals("oracle"))
			sSql.append(" COMMENT_ID,");
		sSql.append(sFileds.toString().substring(0,sFileds.length()-1)).append(")").append(" values(");
		if(Global.DB_TYPE.equals("oracle"))
			sSql.append(" HIBERNATE_SEQUENCE.nextval, ");
		sSql.append(params.toString().substring(0,params.length()-1)).append(")");
		String commentId = "0";
		try {
			Object obj = pubAppBaseService.getBaseDao().saveBaseSql(sSql.toString(), parameters, "COMMENT_ID");
			if(obj != null){
				commentId = obj.toString();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return WSConstant.SYS_ERROR;
		}
		return String.valueOf(commentId);
	}
	/**
	 * Description: 根据视图编号获取视图字段类型 <BR>   
	 * @author liu.zhuan
	 * @date 2014-7-10 上午09:23:05
	 * @param viewId
	 * @return Map<String, String> key为字段名，value为字段类型
	 * @version 1.0
	 */
	private Map<String, String> getFieldTypeByViewId(long viewId){
		Map<String, String> fieldType = new HashMap<String, String>();
		try {
			List<Object> fieldList = pubAppBaseService.find("new list(fieldName,fieldType)", AppFieldRel.class.getName(), " viewId = ? ", null, viewId);
			if(fieldList != null && fieldList.size() > 0){
				List<Object> field = null;
				for (Object object : fieldList) {
					field = (List<Object>)object;
					fieldType.put(field.get(0).toString(), field.get(1).toString());
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		return fieldType;
	}
}
