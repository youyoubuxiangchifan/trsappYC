package com.trs.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.dbhibernate.Page;
import com.trs.model.AppInfo;
import com.trs.service.AppSysConfigService;
import com.trs.service.PublicAppBaseService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class PublicAppBaseServiceTest extends AbstractJUnit4SpringContextTests{
	private static  Logger LOG =  Logger.getLogger(PublicAppBaseServiceTest.class);
	@Autowired
	private PublicAppBaseService publicAppBaseService;
	@Autowired
	private AppSysConfigService appSysConfigService;
	@Test
	public void saveBaseSqlTest(){
//		appSysConfigService.oracle();
	}
	/*
	 * 自定义工作流规则调用方法
	 
	@Test
	public void changeflowsTest(){
	 List<Object> obj =	publicAppBaseService.changeflows("subclass");
	 for(Object uid :obj){
		 System.out.println(uid);
	 }
	}*/
	/*
	 * 
	 *返回用户有权限的应用集合
	 */
//	@Test
//	public void getAppInfoIdsTest(){
//		Long userId = 74l;
//		try {
//			List<Object> objAppInfos =publicAppBaseService.getAppInfos(userId);
//			for(Object objAppInfo : objAppInfos){
//				
//				AppInfo appInfo =(AppInfo)objAppInfo;
//				System.out.println(appInfo.getAppId());
////				appInfo.getViewId();
//				System.out.println("field 字段的结果字符串"+getAppInfoFieldsTest(userId, "viewId"));
//				List<Object> viewToAppInfos =  getViewToAppInfoTest(userId, appInfo.getViewId());
//				for(Object obj :viewToAppInfos){
//					AppInfo currAppInfo = (AppInfo)obj;
//					System.out.println("当前视图编号:"+appInfo.getViewId()+"对应的应用为"+currAppInfo.getAppName());
//				}
//				
//				
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	/*
//	 * 返回对应field 字段的结果字符串
//	 */
//	public String getAppInfoFieldsTest(Long userId,String field) throws Exception{
//		return publicAppBaseService.getAppInfoFields(userId, field, 0l);
//	}
//	/*
//	 * 返回视图对应的应用信息
//	 */
//	public List<Object> getViewToAppInfoTest(Long userId,Long viewId) throws Exception{
//		return publicAppBaseService.getViewToAppInfo(userId, viewId);
//	}
	//getAppViewInfos
	/**
	 * 统计返回当前用户有权限的所有应用的不同状态（代办、已处理、代处理）的信息总数
	* Description:参数中status 对应当前信息的处理状态;已签收但未处理也属于代办<BR>  
	* status 参数为 null 时 则仅返回当前用户对应应用所有信件总数;<BR>
	* @author wen.junhui
	* @date Create: 2014-3-20 下午05:58:20
	* Last Modified:
	* @param userId  当前用户ID
	* @param userName 当前用户用户名
	* @param status  要查询的信息处理状态  ，例如：0 未处理   1 已处理，具体参见系统数据字典中定义
	* @return List<Map<String, Integer>> 
	* 返回的数据格式 List< map<appId,20><appName,局长信箱><waitNumber,20><appInfoMaxNumber,50> >
	* 【appName=应用名称,appId=应用编号,waitNumber=信件待办的总数,appInfoMaxNumber=当前用户对应该应用的所有信息】
	* @throws Exception
	* @version 1.0
	 */
	
//	public void findWaitWorksCountTest(){
//		
//		try {
//			List<Map<String, Object>> maps = publicAppBaseService.findWaitWorksCount(74l, "zhangzun", "0");
//			for(Map<String, Object> map : maps){
//				System.out.println("应用ID"+map.get("appId"));
//				System.out.println("应用名称"+map.get("appName"));
//				System.out.println("应用代办数"+map.get("waitNumber"));
//				System.out.println("应用信息总数"+map.get("appInfoMaxNumber"));
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}

//	/**
//	 * 返回应用(元数据)的数据列表<原生SQL>
//	* Description:<BR>  
//	* @author wen.junhui
//	* @date Create: 2014-3-24 下午05:43:57
//	* Last Modified:
//	* @version 1.0
//	 */
//	@SuppressWarnings("unchecked")
//	@Test
//	public void queryAppInfosTest(){
//		try {
//			List<Object> parameters = new ArrayList<Object>();
//			parameters.add(302);
//			Page page = publicAppBaseService.queryAppInfos("appmetatabletest111", "title", null, null, null, 1,20);
//			List<Object> dataInfos = page.getLdata();
////			String[] currFields = appfileds.split(",");
//			for(Object obj :dataInfos){
//				Map<String, Object> appinfo = (Map<String, Object>)obj;
////				for(String filed : currFields){
//					System.out.println("应用信息字段值为"+appinfo.get("TITLE"));
////				}
//			}
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	@Test
//	public void saveAllBaseSqlsTest(){
//		List<List<Object>> lists = new ArrayList<List<Object>>();
////			String sql = "insert into appmetatabletest111(metadataid,groupid,content) values(?,?,?)";
//		String sql = "update appmetatabletest111 set content=? where metadataid=?";	
//		//for(int i =0;i<10;i++){
//				List<Object> parameters  = new ArrayList<Object>();
//				parameters.add("daziduan");
//				parameters.add(20);
//			//	parameters.add("dbvalue");
//				lists.add(parameters);
//		//	}
//			try {
//				publicAppBaseDao.saveBaseSqls(sql, lists);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	}
//	@Test
//	public void saveAllBaseSqlTest(){
//				String sql = "insert into appmetatabletest111(metadataid,groupid,content) values(?,?,?)";
//				List<Object> parameters  = new ArrayList<Object>();
//				parameters.add(21);
//				parameters.add(20);
//				parameters.add("dbvalue");
//			try {
//				publicAppBaseDao.saveBaseSql(sql, parameters);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	}
	/**
	 * 单条保存元数据
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-25 下午02:26:34
	* Last Modified:
	* @version 1.0
	 */
//	@Test
//	public void saveMetadataTest(){
//			Map<String, Object> maps = new HashMap<String, Object>();
//			maps.put("metadataid", 554);
//			maps.put("groupid",555);
//			maps.put("content","123");
// 		try {
//			publicAppBaseService.saveOrUpdateMetadata(maps, "appmetatabletest111");
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	/**
//	 * 多条保存元数据
//	* Description:<BR>  
//	* @author wen.junhui
//	* @date Create: 2014-3-25 下午02:26:34
//	* Last Modified:
//	* @version 1.0
//	 */
//	@Test
//	public void saveMetadatasTest(){
//		List<Map<String,Object>> appinfo = new ArrayList<Map<String,Object>>();
//		for(int i=548;i<551;i++){
//			Map<String, Object> maps = new HashMap<String, Object>();
//			maps.put("metadataid",i);
//			maps.put("groupid",12580);
//			maps.put("cruser","汶军辉");
//			maps.put("content","234");
//			appinfo.add(maps);
//		}
//		try {
//		publicAppBaseService.saveMetadatas(appinfo, "appmetatabletest111");
//		
//	} catch (SQLException e) {
//		e.printStackTrace();
//	}
//	}	
	
}
