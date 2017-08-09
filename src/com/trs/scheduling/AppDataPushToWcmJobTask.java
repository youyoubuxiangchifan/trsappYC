/**
 * 
 */
package com.trs.scheduling;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.trs.job.AppDataPushToWcmJob;
import com.trs.model.AppInfo;
import com.trs.model.AppSyncField;
import com.trs.service.AppSysConfigService;
import com.trs.service.PublicAppBaseService;
import com.trs.util.CMyDateTime;
import com.trs.util.CMyString;
import com.trs.util.FileUtil;
import com.trs.util.Global;
import com.trs.web2frame.WCMServiceCaller;
import com.trs.web2frame.dispatch.Dispatch;
import com.trs.web2frame.util.JsonHelper;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppDataPushToWcmJobTask
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-10-29 上午10:39:15
 * @version 1.0
 */
public class AppDataPushToWcmJobTask {
	private static  Logger LOG =  Logger.getLogger(AppDataPushToWcmJobTask.class);
	@Autowired
	private PublicAppBaseService publicAppBaseService;
	@Autowired
	private AppSysConfigService appSysConfigService;
	public void dataPushToWcm(){
		try {
			String logPath = appSysConfigService.findSysConfigCon("APP_LOG_PATH", "d://TRS/TRSAPP/TRSAPPLOGS/");
			FileAppender appender = FileUtil.getLogAppender("[TRSAPPLOG-WCMDATAPUSH] %d - %-5p %x - %m %n", logPath + "WCMDATAPUSH_" + CMyDateTime.now().toString("yyyyMMddhhmm") + ".log");
			LOG.addAppender(appender);
			String sServiceId = "wcm6_document";
	    	String sMethodName = "save"; 
			//1.获取所有需要进行数据推送的应用
			List<Object> fromApps = publicAppBaseService.find("", AppInfo.class.getName(), "isPush = 1 and deleted = 0 and appStatus = 0", "", null);
			if(fromApps == null || fromApps.size() == 0) return;
			//2.遍历应用
			AppInfo appInfo = null;
			List<Object> appSyncFieldList = null;//字段对应关系列表
			AppSyncField synField = null;//字段对应关系
			StringBuffer sSelectField = null;//需查询字段，以[trsapp field] as [wcm field]方式
			StringBuffer sWhere = null;
			List<Object> metaDataList = null;//元数据列表
			List<Object> parameters = null;//查询参数
			Map<String, Object> metaData = null;//元数据对象
			//List<Map<String, Object>> upMetaDataList = null;//用于存放需更新的元数据列表
			Map<String, Object> upMetaData = null;//用于存放需更新的元数据
			Object metaDataId = null;
			LOG.info("数据推送任务开始，遍历所有应用[BEGIN]");
			for (Object object : fromApps) {
				appInfo = (AppInfo) object;
				//查询字段对应
				appSyncFieldList = publicAppBaseService.find("", AppSyncField.class.getName(), "appId = ?", "", appInfo.getAppId());
				if(appSyncFieldList == null || appSyncFieldList.size() == 0){//如果没有设置字段对应则跳过
					LOG.warn("应用没有设置wcm字段对应，应用编号-[" + appInfo.getAppId() + "],推送任务将跳过此应用,请检查应用设置！");
					continue;
				}
				//根据字段对应拼接sql
				sSelectField = new StringBuffer();
				sWhere = new StringBuffer();
				for (Object object2 : appSyncFieldList) {
					synField = (AppSyncField)object2;
					sSelectField.append(synField.getAppFieldName())
						.append(" as ")
						.append(synField.getToFieldName())
						.append(",");
				}
				sSelectField.append(appInfo.getWcmChnlId())
					.append(" as CHANNELID")
					.append(", SYNCFLAG")
					.append(", WCMDOCID")
					//.append(", case when WCMDOCID IS null then 0 when WCMDOCID IS not null then WCMDOCID end")
					.append(", METADATAID");
				String synCondition = appSysConfigService.findSysConfigCon("WCM_SYN_CONDITION_" + appInfo.getAppId(), "");
				sWhere.append("deleted = 0 and APPID = ? and (syncflag != 1 or syncflag is null) ")
					.append(CMyString.showEmpty(synCondition, ""));
				/*if(appInfo.getIsNeedTheme() == 0){
					sWhere.append(" and status = 2");
				}else{
					sWhere.append(" and status = 0");
				}*/
				parameters = new ArrayList<Object>();
				parameters.add(appInfo.getAppId());
				LOG.info("selectField:" + sSelectField);
				LOG.info("where:" + sWhere.toString());
				//查询数据
				metaDataList = publicAppBaseService.queryAppInfos(appInfo.getMainTableName(), sSelectField.toString(), 
						sWhere.toString(), parameters, "crtime asc", 10000);
				if(metaDataList == null || metaDataList.size() == 0){
					LOG.warn("应用没需要推送的数据，应用编号-[" + appInfo.getAppId() + "],推送任务将跳过此应用！");
					continue;
				}
				LOG.info("*************************************TRSAPP-DATAPUSH-BEGIN***********************************************");
				LOG.info("数据推送任务开始,TRSAPP[" + appInfo.getAppName() + "],ID-[" + appInfo.getAppId() + "],WCM栏目ID-[" + appInfo.getWcmChnlId() + "]。");
				LOG.info("共检索出 " + metaDataList.size() + " 条数据。");
				int dcnt = 0;
				if(appInfo.getWcmDocType() == Global.WCM_TYPE_METADATA){//如果应用上设置的文档类型为元数据，则使用元数据导入服务方法
					sServiceId = "wcm6_MetaDataCenter";
					sMethodName = "savemetaviewdata";
				}
				//upMetaDataList = new ArrayList<Map<String, Object>>();
				//推送数据
				for (Object object3 : metaDataList) {
					metaData = (Map<String, Object>) object3;
					metaDataId = metaData.get("METADATAID");
					//清除不需要的数据，直接推送map中的数据到wcm
					metaData.remove("RWN");
					metaData.remove("SYNCFLAG");
					metaData.remove("METADATAID");
					if(CMyString.isEmpty((String)metaData.get("WCMDOCID"))){
						metaData.put("ObjectId", 0);
					} else {
						metaData.put("ObjectId", Integer.parseInt((String)metaData.get("WCMDOCID")));
					}
					metaData.put("DOCTYPE", 20);//默认推送html类型
					//wcm服务调用
					Dispatch oDispatch = WCMServiceCaller.Call(sServiceId, sMethodName, metaData, true);
					//System.out.println(oDispatch.getJson());
					Map oJson = oDispatch.getJson();
					if(oJson.get("FAULT") != null){
						LOG.error("数据推送失败！TRSAPP元数据编号[" + metaDataId + "],异常信息：" + oJson.get("FAULT"));
						continue;
					}
					//存放值
					upMetaData = new HashMap<String, Object>();
					upMetaData.put("METADATAID", metaDataId);
					upMetaData.put("SYNCFLAG", 1);
					if(appInfo.getWcmDocType() == Global.WCM_TYPE_DOCUMENT){
						upMetaData.put("WCMDOCID", oDispatch.getResult());
					}else{
						upMetaData.put("WCMDOCID", JsonHelper.getValueAsString( 
								oJson ,"METAVIEWDATA.METADATAID"));
					}
					//upMetaDataList.add(upMetaData);
					dcnt++;
					publicAppBaseService.saveOrUpdateMetadata(upMetaData, appInfo.getMainTableName());
					LOG.info("TRSAPP元数据编号为[ " + metaDataId + "]的数据导入成功!");
				}
				LOG.info("TRSAPP[" + appInfo.getAppName() + "]数据推送任务结束，共成功导入 " + dcnt + "条数据！");
				LOG.info("*************************************TRSAPP-DATAPUSH-END***********************************************");
				//批量更新推送标识及wcm文档id
				//if(upMetaDataList != null && upMetaDataList.size() > 0)
					//publicAppBaseService.saveOrUpdateMetadatas(upMetaDataList, appInfo.getMainTableName());
			}
			LOG.info("数据推送任务结束[END]");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			LOG.error("数据推送任务执行失败！异常信息：" + e.getMessage());
		}
	}
}
