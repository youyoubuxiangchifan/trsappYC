package com.trs.service;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.trs.dao.SynDataDao;
import com.trs.infra.util.CMyString;
import com.trs.model.AppAppendix;
import com.trs.model.AppFieldRel;
import com.trs.model.AppInfo;
import com.trs.model.AppSyncField;
import com.trs.web.AppMgrController;
import com.trs.web2frame.WCMServiceCaller;
import com.trs.web2frame.dispatch.Dispatch;
import com.trs.web2frame.domain.DocumentMgr;
import com.trs.web2frame.domain.GovInfoMgr;
import com.trs.web2frame.entity.WDocument;
import com.trs.web2frame.entity.WGovInfo;
import com.trs.web2frame.util.JsonHelper;
@Service
public class SynDataService extends BaseService{
	@Autowired
	private SynDataDao synDataDao ;
	private static  Logger loger = Logger.getLogger(SynDataService.class);
	private static final String WCMMETAFIELD = "METAVIEWDATA.METADATAID";//wcm元数据id
	private static final String WCMDOCFIELD = "DocumentId";//wcm元数据id
	private static final String IMAGE_EXT = "BMP,JPG,JPEG,PNG,GIF,bmp,jpg,jpeg,png,gif";
	private static final String METADATAID = "METADATAID";//应用数据id
	private static final String APPID = "APPID";//应用数据id
	
	/**
	 * 
	* Description:同步一个应用数据到wcm <BR>   
	* @author zhangzun
	* @date 2014-4-16 下午05:36:42
	* @param appId
	* @param metaDataId
	* @version 1.0
	 */
	public void synMetaDataToWcm(Long appId,Long metaDataId){
		loger.info("测试日志信息");
		loger.error("测试日志信息");
		List<Object> fieldRel = null; //同步字段映射集合
		Map<String,Object> appData = null;
		AppInfo appInfo = null ;  //应用信息
		int chnlId = 0;          //wcm栏目id
		int isHasView = 0;      //栏目是否绑定视图
		try {
			
			fieldRel = getAppSyncField(appId); //获取应用与wcm字段的映射关系
			appInfo = (AppInfo)findById(AppInfo.class, appId);
			chnlId = appInfo.getWcmChnlId().intValue();
			isHasView = isHasView(chnlId);
			if(isHasView<0){   //查询wcm栏目是否绑定视图失败
				loger.info("查询wcm栏目是否绑定视图失败:"+chnlId);
				return;
			}
			appData = getAppdata(appInfo.getMainTableName(),metaDataId,fieldRel);
			if(chnlId<1 || fieldRel==null || appData==null){
				loger.info("同步的数据参数不合法："+"chnlId="+chnlId+"fieldRel="
						 +fieldRel+"appData="+appData);
				 return;
			}
			synDataToWcm(chnlId,fieldRel,appData,appInfo.getMainTableName(),isHasView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("推送数据到wcm发生异常：", e);
		}
		
	}
	/**
	 * 
	* Description:同步一个应用的数据导wcm<BR>   
	* @author zhangzun
	* @date 2014-4-16 下午03:53:29
	* @param appId
	* @version 1.0
	 */
	public void synAppDataToWcm(Long appId){
		List<Object> fieldRel = null; //同步字段映射集合
		List<Object> appDatas = null;//用于存放未同步的应用数据
		AppInfo appInfo = null ;  //应用信息
		int chnlId = 0;          //wcm栏目id
		int isHasView = 0;
		
		try {
			
			fieldRel = getAppSyncField(appId); //获取应用与wcm字段的映射关系
			appInfo = (AppInfo)findById(AppInfo.class, appId);
			chnlId = appInfo.getWcmChnlId().intValue();
			isHasView = isHasView(chnlId);
			if(isHasView<0){   //查询wcm栏目是否绑定视图失败
				loger.info("查询wcm栏目是否绑定视图失败:"+chnlId);
				return;
			}
			appDatas = getAppdatas(appInfo.getMainTableName(),appInfo.getAppId(),fieldRel);
			synDatasToWcm(chnlId,fieldRel,appDatas,appInfo.getMainTableName(),isHasView);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("推送数据到wcm发生异常：", e);
		}
	}
	/**
	 * 
	* Description:同步应用元数据集到wcm<BR>   
	* @author zhangzun
	* @date 2014-4-16 下午05:35:25
	* @param chnlId       wcm栏目id
	* @param fieldRel	    应用同步字段映射
	* @param appDatas      应用数据记录集合
	* @param mainTableName 主表名
	* @param isHasView     栏目是否绑定视图
	* @version 1.0
	 */
	public void synDatasToWcm(int chnlId,List<Object> fieldRel,
			List<Object> appDatas,String mainTableName,int isHasView){
		if(chnlId<1 || appDatas==null || fieldRel==null){
			loger.info("同步的数据参数不合法："+"chnlId="+chnlId+"fieldRel="
					 +fieldRel);
			 return;
		}
		
		for(int i=0;i<appDatas.size();i++){
			Map<String,Object> appData = (Map<String,Object>)appDatas.get(i);
			if(appData==null)
					continue;
			synDataToWcm(chnlId,fieldRel,
					appData,mainTableName,isHasView);
		}
		
	}
	/**
	 * 
	* Description:同步应用元数据到wcm <BR>   
	* @author zhangzun
	* @date 2014-4-16 下午05:33:48
	* @param chnlId       wcm栏目id
	* @param fieldRel	    应用同步字段映射
	* @param appData      应用数据记录
	* @param mainTableName 主表名
	* @param isHasView     栏目是否绑定视图
	* @return
	* @version 1.0
	 */
	public int  synDataToWcm(int chnlId,List<Object> fieldRel,
			Map<String,Object> appData,String mainTableName,int isHasView){
			int isSUc = 0;
			List<Object> appendixs=null;
			Long metadataid = Long.valueOf(appData.get(SynDataService.METADATAID).toString());
			Long appId = Long.valueOf(appData.get(SynDataService.APPID).toString());
			try {
				appendixs = findAppAppendix(metadataid,appId);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				loger.error("查询应用数据的附件发生异常：", e);
				return -1;
			}
			if(isHasView==0){//如果栏目没有绑定视图则推送wcm文档否则推送元数据
				isSUc =synToWcmDoc(chnlId,fieldRel,appData,appendixs);
			}else{
				isSUc = synToWcmMeta(chnlId,fieldRel,appData,appendixs);
			}
			if(isSUc>0){//如果同步数据成功设置同步标示为1
				setSynFlag(1,metadataid,mainTableName);
			}else{
				loger.info("数据推送到wcm失败："+appData);
			}
			return isSUc;
			
		}
	/**
	 * 
	* Description:向wcm推送元数据<BR>   
	* @author zhangzun
	* @date 2014-4-14 下午04:02:52
	* @param chnlId  	wcm栏目id
	* @param fieldRel	应用的字段同步映射关系
	* @param appData	应用数据
	* @param appendixs  应用的附件集合
	* @return
	* @version 1.0
	 */
	public int synToWcmMeta(int chnlId,List<Object> fieldRel,
			Map<String,Object> appData,List<Object> appendixs){
		 int state = 0;  //标示数据状态。
		 if(fieldRel !=null && appData!=null){
			 WGovInfo oGovInfo = new WGovInfo();
			 oGovInfo.setFieldValue("ChannelId", chnlId);//设置栏目id
			 oGovInfo.setFieldValue("ObjectId", 0);		 //设置对象id
			 String sFieldName = "";
			 Object oFieldValue = "";
			 AppSyncField appSyncField = null;
			 for(int i=0;i<fieldRel.size();i++) {  //遍历同步字段关系设置元数据的对应字段和对应值
				 appSyncField = (AppSyncField)fieldRel.get(i);
				 sFieldName = appSyncField.getAppFieldName();
				 oFieldValue = appData.get(sFieldName);
				 if(oFieldValue==null)
					 continue;
				 oGovInfo.setFieldValue(appSyncField.getToFieldName(), oFieldValue);//设置要同步的字段值
			 }
			 try{ 
				 Dispatch oDispatch = GovInfoMgr.save(oGovInfo);//保存数据
				 if(!oDispatch.isFailure()){			//如果数据保存成功，上传附件
					 Map oJson = oDispatch.getJson();
					 //保存附件
					 String metaDataid = JsonHelper.getValueAsString(oJson ,"METAVIEWDATA.METADATAID");
					 state = Integer.valueOf(metaDataid);
					 WDocument wdocument = DocumentMgr.findById(state,chnlId, 0);
					 saveAppendixs(wdocument,appendixs);
				 }else{
					 state = -1;
					 loger.info("调用同步接口失败："+oDispatch.getResponseText());
				 }
				
			 }catch(Exception e){
				 e.printStackTrace();
				 loger.error("同步数据到wcm发生异常", e);
			 }
		 }
		 
         return state;
	}
	/**
	 * 
	* Description:向wcm推送文档数据<BR>   
	* @author zhangzun
	* @date 2014-4-14 下午04:02:52
	* @param chnlId  	wcm栏目id
	* @param fieldRel	应用的字段同步映射关系
	* @param appData	应用数据
	* @param appendixs  应用的附件集合
	* @return
	* @version 1.0
	 */
	public int synToWcmDoc(int chnlId,List<Object> fieldRel,
			Map<String,Object> appData,List<Object> appendixs){
		 int state = 0;  //标示数据状态。
		 WDocument wdocument = new WDocument();
		 if(fieldRel !=null && appData!=null){
			 wdocument.setFieldValue("ChannelId",chnlId);
			 wdocument.setFieldValue("ObjectId",0);
			 wdocument.setFieldValue("doctype","20");//默认文档类型为HTML
			 String sFieldName = "";
			 Object oFieldValue = "";
			 AppSyncField appSyncField = null;
			 for(int i=0;i<fieldRel.size();i++) {  //遍历同步字段关系设置元数据的对应字段和对应值
				 appSyncField = (AppSyncField)fieldRel.get(i);
				 sFieldName = appSyncField.getAppFieldName();
				 oFieldValue = appData.get(sFieldName);
				 if(oFieldValue==null)
					 continue;
				 wdocument.setFieldValue(appSyncField.getToFieldName(), oFieldValue);//设置要同步的字段值
			 }
			 try{ 
				 state = DocumentMgr.save(wdocument);		//保存数据
				 //文档保存成功，保存附件
				 if(state>0)
					 saveAppendixs(wdocument,appendixs); //如果数据保存成功，上传附件
			 }catch(Exception e){
				 e.printStackTrace();
				 loger.error("同步数据到wcm发生异常", e);
			 }
		 }
         return state;
	}
	/**
	 * 
	* Description:向wcm元数据导入附件 <BR>   
	* @author zhangzun
	* @date 2014-4-14 下午03:57:55
	* @param wdocument  
	* @param appendixs  应用数据的附件集合
	* @version 1.0
	 */
	public void saveAppendixs(WDocument wdocument,List<Object> appendixs){
		if(appendixs==null || appendixs.size()==0)
			return;
		try{
			int nAppFlag = 10; //附件类型
			String ext = ""; //文件后缀名
			String filename = "";//文件全路径
			AppAppendix adix = null;
			for(Object obj : appendixs){
				adix = (AppAppendix)obj;
				filename = adix.getSrcfile();
				File file = new File(filename);
				if(!file.exists()){
					loger.info("应用数据："+wdocument.getFieldValue(SynDataService.WCMDOCFIELD)
							+"附件不存在！");
					continue;
				}
				Map appdInfo = new HashMap();
				ext = adix.getFileExt()==null?"":adix.getFileExt();
				if(SynDataService.IMAGE_EXT.contains(ext)){
					nAppFlag = 20; //图片类型
				}else{
					nAppFlag = 10; //文件类型
				}
	            appdInfo.put("APPDESC",adix.getFileName()== null?"":adix.getFileName());
	            appdInfo.put("SRCFILE",adix.getSrcfile()== null?"":adix.getSrcfile());
				wdocument.addAppendix(nAppFlag, filename,appdInfo);
			}
			DocumentMgr.saveAppendixs(wdocument);
		}catch(Exception e){
			e.printStackTrace();
			loger.error("同步附件到wcm发生异常", e);
		}
	}
	/**
	* Description:wcm栏目id <BR>   
	* @author zhangzun
	* @date 2014-4-14 下午07:26:57
	* @param chnlId
	* @return int  1：有视图，0：无视图，-1：查询发送异常
	* @version 1.0
	 */
	public int isHasView(int chnlId){
			int statuts = 0;
		 	String sServiceId = "wcm6_MetaDataDef";
	        String sMethodName = "getViewFromChannel";
	        try{
		        Map oPostData = new HashMap();
		        oPostData.put("channelId",Integer.valueOf(chnlId));
		        Dispatch oDispatch = WCMServiceCaller.Call(sServiceId, sMethodName, oPostData, false);
		        Map json = oDispatch.getJson();
		        String sViewName = JsonHelper.getValueAsString(json ,"METAVIEW.VIEWNAME");
		        if(!oDispatch.isFailure()){
		        	if(!CMyString.isEmpty(sViewName)){
			        	statuts = 1;
			        }
		        }else{
		        	statuts = -1;
		        }
	        }catch(Exception e){
	        	e.printStackTrace();
	        	statuts = -1;
	        }
	        return statuts;
	}
	/**
	 * 
	* Description:获取应用数据的附件集合 <BR>   
	* @author zhangzun
	* @date 2014-4-15 下午08:54:06
	* @param metadataId
	* @return
	* @version 1.0
	 * @throws Exception 
	 */
	public List<Object> findAppAppendix(Long metadataId, Long appId) throws Exception{
		List<Object> list = null;
		List<Object> parameters = new ArrayList<Object>();
		parameters.add(metadataId);
		parameters.add(appId);
			list = find("", AppAppendix.class.getName(),
					"metadataId = ? and appId = ?", "appendix_id", parameters);
		return list;
	}
	/**
	 * 
	* Description:获取应用要同步的字段 <BR>   
	* @author zhangzun
	* @date 2014-4-15 下午09:23:09
	* @param appId
	* @return
	* @version 1.0
	 */
	public List<Object> getAppSyncField(Long appId){
		List<Object> list = null;
		try {
			list = find("", AppSyncField.class.getName(), "appId=?", "", appId);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("查询同步字段发生异常", e);
		}
		return list;
	}
	/**
	 * 
	* Description:根据应用查询未同步的合法数据<BR>   
	* @author zhangzun
	* @date 2014-4-16 上午10:38:31
	* @param mainTableName
	* @param fieldRel
	* @return
	* @version 1.0
	 */
	public List<Object> getAppdatas(String mainTableName,Long appId,List<Object> fieldRel){
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		AppSyncField rel = null;
		for(int i=0;i<fieldRel.size();i++){
			rel = (AppSyncField)fieldRel.get(i);
			sql.append(rel.getAppFieldName()+",");
		}
		sql.append("metadataId");
		sql.append(" from "+mainTableName);
		sql.append(" where syncflag is null and deleted=0");
		sql.append(" and appid=?");
		List<Object> params = new ArrayList<Object>();
		List<Object> list = null;
		params.add(appId);
		try {
			list = synDataDao.executeQueryObjs(sql.toString(), params);
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询同步数据发生异常:"+sql.toString(), e);
		}
		return list;
	}
	/**
	 * 
	* Description:根据应用查询未同步的合法数据<BR>   
	* @author zhangzun
	* @date 2014-4-16 上午10:38:31
	* @param mainTableName
	* @param fieldRel
	* @return
	* @version 1.0
	 */
	public Map<String,Object> getAppdata(String mainTableName,Long metadataId,List<Object> fieldRel){
		StringBuffer sql = new StringBuffer();
		sql.append("select ");
		AppSyncField rel = null;
		for(int i=0;i<fieldRel.size();i++){
			rel = (AppSyncField)fieldRel.get(i);
			sql.append(rel.getAppFieldName()+",");
		}
		sql.append("metadataId");
		sql.append(" from "+mainTableName);
		sql.append(" where syncflag is null and deleted=0");
		sql.append(" and metadataid="+metadataId);
		Map<String,Object> data = null;
		try {
			data = (Map<String,Object>)synDataDao.executeQueryObj(sql.toString());
		} catch (Exception e) {
			e.printStackTrace();
			loger.error("查询同步数据发生异常:"+sql.toString(), e);
		}
		return data;
	}
	/**
	 * 
	* Description:给应用数据设置同步标示 <BR>   
	* @author zhangzun
	* @date 2014-4-16 上午11:18:17
	* @param flag
	* @param metadataId
	* @param tableName
	* @version 1.0
	 */
	public void setSynFlag(int flag,long metadataId,String tableName){
		StringBuffer sql = new StringBuffer();
		sql.append("update "+tableName);
		sql.append(" set syncflag="+ flag);
		sql.append(" where metadataid="+metadataId);
		try {
			synDataDao.executeBaseSql(sql.toString());
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			loger.error("设置同步字段发生异常："+metadataId, e);
		}
	}
	public static void main(String[] args){
		
	}
}
