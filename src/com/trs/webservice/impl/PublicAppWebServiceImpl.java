package com.trs.webservice.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.jws.WebService;

import net.sf.json.JSONArray;

import org.springframework.beans.factory.annotation.Autowired;

import com.trs.model.AppGroup;
import com.trs.model.AppInfo;
import com.trs.model.AppUser;
import com.trs.model.AppViewInfo;
import com.trs.service.PublicAppBaseService;
import com.trs.util.CMyString;
import com.trs.web2frame.util.XmlHelper;
import com.trs.webservice.PublicAppWebService;
import com.trs.webservice.WSConstant;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: PublicAppWebServiceImpl
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-16 下午04:33:39
 * @version 1.0
 */
@WebService(endpointInterface = "com.trs.webservice.PublicAppWebService")
public class PublicAppWebServiceImpl implements PublicAppWebService{
	
	private final String NULL_PARAM = "0";//接口所需参数为空
	private final String SYSTEM_ERROR = "1";//应用没有绑定元数据表或其他问题导致的数据关联不上。
	@Autowired
	private PublicAppBaseService pubAppBaseService;
	/**
	 * 查询应用数据
	 * Description: 查询应用数据 <BR>   
	 * @author liu.zhuan
	 * @date 2014-4-18 下午06:16:13
	 * @param xmlDoc
	 * 格式为：
	 * <root>
	 * 		<condition>
	 * 			<selectFields>field1,field2,...</selectFields>
	 * 			<appId>0</appId>当应用编号为0时根据视图编号查询,即应用编号的优先级高于视图编号
	 * 			<viewId>10</viewId>视图编号不可以为空或为0
	 * 			<status>0</status>
	 * 			<maxResult>10</maxResult>
	 * 		</condition>
	 *</root>
	 * @return json字符串或errorcode:0表示参数列表没有输入，1表示系统可能出现的异常。
	 * @version 1.0
	 */
	public String queryAppData(String xmlDoc) {
		try {
			if(CMyString.isEmpty(xmlDoc)){
				return WSConstant.NO_PARAM;
			}
			Map xmlMap = XmlHelper.parseXml2Json(XmlHelper.parse2XML(xmlDoc));
			Map condition = (Map)((Map)xmlMap.get("ROOT")).get("CONDITION");
			String sFields = (String)((Map)condition.get("SELECTFIELDS")).get("NODEVALUE");
			String sAppId = (String)((Map)condition.get("APPID")).get("NODEVALUE");
			String sViewId = (String)((Map)condition.get("VIEWID")).get("NODEVALUE");
			String sStatus = (String)((Map)condition.get("STATUS")).get("NODEVALUE");
			String sMaxResult = (String)((Map)condition.get("MAXRESULT")).get("NODEVALUE");
			//String pageSize = (String)condition.get("maxResult");
			if(CMyString.isEmpty(sAppId) || CMyString.isEmpty(sViewId) || CMyString.isEmpty(sFields) || CMyString.isEmpty(sMaxResult)){
				return WSConstant.NO_PARAM;
			}
			long nAppId = Long.valueOf(sAppId);
			long nViewId = Long.valueOf(sViewId);
			AppViewInfo appView = (AppViewInfo)pubAppBaseService.findById(AppViewInfo.class, nViewId);
//			AppInfo appInfo = (AppInfo)pubAppBaseService.findById(AppInfo.class, nAppId);
			if(appView == null){
				return WSConstant.NO_VIEW;
			}
			String tableName = appView.getMainTableName();
			if(CMyString.isEmpty(tableName)){
				return WSConstant.NO_META;
			}
			List<Object> parameters = new ArrayList<Object>();
			StringBuffer sWhere = new StringBuffer("1 = 1");
			if(nAppId != 0){
				sWhere.append(" and appId = ?");
				parameters.add(nAppId);
			} else {
				List<Object> appInfos = pubAppBaseService.find("appId", AppInfo.class.getName(), "viewId = ?", "", nViewId);
				String sAppIds = CMyString.join((ArrayList<Object>)appInfos, ",");
				if(CMyString.isEmpty(sAppIds)){
					return WSConstant.NO_APP;
				}
				sWhere.append(" and appId in (")
					.append(sAppIds)
					.append(")");
			}
			if(!CMyString.isEmpty(sStatus)){
				sWhere.append(" and status = ?");
				parameters.add(Integer.valueOf(sStatus));
			}
			List<Object> dataList = pubAppBaseService.queryAppInfos(tableName, sFields, sWhere.toString(), parameters, "crtime desc", Integer.valueOf(sMaxResult));
			JSONArray jsonData = JSONArray.fromObject(dataList);
			return jsonData.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return WSConstant.SYS_ERROR;
		}
	}
	/**
	 * 查询用户数据
	 * Description: 查询用户数据<BR>   
	 * @author liu.zhuan
	 * @date 2014-4-18 下午06:16:13
	 * @param xmlDoc
	 * 格式为：
	 * <root>
	 * 		<condition>
	 * 			<selectFields>field1,field2,...</selectFields>
	 * 			<maxResult>10</maxResult>
	 * 		</condition>
	 *</root>
	 * @return json字符串或errorcode:0表示参数列表没有输入，1表示系统可能出现的异常。
	 * @version 1.0
	 */
	public String queryUserData(String xmlDoc) {
		try {
			if(CMyString.isEmpty(xmlDoc)){
				return WSConstant.NO_PARAM;
			}
			Map xmlMap = XmlHelper.parseXml2Json(XmlHelper.parse2XML(xmlDoc));
			Map condition = (Map)((Map)xmlMap.get("ROOT")).get("CONDITION");
			String sFields = (String)((Map)condition.get("SELECTFIELDS")).get("NODEVALUE");
			//String sStatus = (String)((Map)condition.get("STATUS")).get("NODEVALUE");
			String sMaxResult = (String)((Map)condition.get("MAXRESULT")).get("NODEVALUE");
			//String pageSize = (String)condition.get("maxResult");
			if(CMyString.isEmpty(sFields)){
				return WSConstant.NO_PARAM;
			}
			if(CMyString.isEmpty(sMaxResult)){
				return WSConstant.NO_PARAM;
			}
			String sFieldArr[] = sFields.split(",");
			StringBuffer sSelectField = new StringBuffer("new map(");
			for (String fieldName : sFieldArr) {
				if(fieldName.equals("password")){
					continue;
				}
				sSelectField.append(fieldName)
					.append(" as ")
					.append(fieldName.toUpperCase())
					.append(",");
			}
			sSelectField = new StringBuffer(sSelectField.substring(0, sSelectField.length() - 1));
			sSelectField.append(")");
			List<Object> dataList = pubAppBaseService.find(sSelectField.toString(), AppUser.class.getName(), "status = 0 and isDeleted = 0", "crtime desc", 0, Integer.parseInt(sMaxResult), null);
			JSONArray jsonData = JSONArray.fromObject(dataList);
			return jsonData.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return WSConstant.SYS_ERROR;
		}
	}
	/**
	 * 查询组织数据
	 * Description: 查询组织数据<BR>   
	 * @author liu.zhuan
	 * @date 2014-4-18 下午06:16:13
	 * @param xmlDoc
	 * 格式为：
	 * <root>
	 * 		<condition>
	 * 			<selectFields>field1,field2,...</selectFields>
	 * 			<maxResult>10</maxResult>
	 * 		</condition>
	 *</root>
	 * @return json字符串或errorcode:0表示参数列表没有输入，1表示系统可能出现的异常。
	 * @version 1.0
	 */
	public String queryGroupData(String xmlDoc) {
		try {
			if(CMyString.isEmpty(xmlDoc)){
				return WSConstant.NO_PARAM;
			}
			Map xmlMap = XmlHelper.parseXml2Json(XmlHelper.parse2XML(xmlDoc));
			Map condition = (Map)((Map)xmlMap.get("ROOT")).get("CONDITION");
			String sFields = (String)((Map)condition.get("SELECTFIELDS")).get("NODEVALUE");
			//String sStatus = (String)((Map)condition.get("STATUS")).get("NODEVALUE");
			String sMaxResult = (String)((Map)condition.get("MAXRESULT")).get("NODEVALUE");
			//String pageSize = (String)condition.get("maxResult");
			if(CMyString.isEmpty(sFields)){
				return WSConstant.NO_PARAM;
			}
			if(CMyString.isEmpty(sMaxResult)){
				return WSConstant.NO_PARAM;
			}
			String sFieldArr[] = sFields.split(",");
			StringBuffer sSelectField = new StringBuffer("new map(");
			for (String fieldName : sFieldArr) {
				if(fieldName.equals("password")){
					continue;
				}
				sSelectField.append(fieldName)
					.append(" as ")
					.append(fieldName.toUpperCase())
					.append(",");
			}
			sSelectField = new StringBuffer(sSelectField.substring(0, sSelectField.length() - 1));
			sSelectField.append(")");
			List<Object> dataList = pubAppBaseService.find(sSelectField.toString(), AppGroup.class.getName(), "", "crtime desc", 0, Integer.parseInt(sMaxResult), null);
			JSONArray jsonData = JSONArray.fromObject(dataList);
			return jsonData.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return WSConstant.SYS_ERROR;
		}
	}

}
