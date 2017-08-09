package com.trs.service.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.model.AppFieldInfo;
import com.trs.model.AppTableInfo;
import com.trs.model.AppUser;
import com.trs.service.AppMetaDataService;
import com.trs.service.AppUserService;
import com.trs.util.CMyDateTime;
import com.trs.util.Global;

/**
 * Description: 元数据测试类<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppMetaDataService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-13 上午10:27:42
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class AppMetaDataServiceTest extends AbstractJUnit4SpringContextTests{
	@Autowired
	private AppMetaDataService appMetaDataService;
	/**
	 * 测试表创建
	 */
//	@Test
//	public void createTableTest() throws Exception{
//		AppTableInfo appTableInfo = new AppTableInfo();
//		appTableInfo.setTableName("test222");
//		appTableInfo.setAnotherName("测试表");
//		appTableInfo.setTableDesc("测试表");
//		appTableInfo.setTableType(0);
//		appTableInfo.setUseSysField(1);
//		//appTableInfo.setMainTableId(new Long(39));
//		AppUser user = (AppUser)appMetaDataService.findById(AppUser.class, Long.parseLong("88"));
//		appMetaDataService.createAppTable(appTableInfo, user);
//	}
	
//	/**
//	 * 测试表删除
//	 */
//	@Test
//	public void delTableTest() throws Exception{
//		appMetaDataService.deleteAppTable(new Long(39));
//	}
//	/**
//	 * 测试添加字段
//	 * @throws Exception 
//	 */
//	@Test
//	public void addEditDelTableFieldTest() throws Exception{
//		AppFieldInfo appFieldInfo = new AppFieldInfo();//(AppFieldInfo)appMetaDataService.findById(AppFieldInfo.class, new Long(66));//
//		appFieldInfo.setFieldName("dddd");
//		appFieldInfo.setFieldDesc("dddddddd");
//		//appFieldInfo.setDblength(new Long(100));
//		appFieldInfo.setTableName("appmetatabletest222");
//		appFieldInfo.setTableId(new Long(560));
//		appFieldInfo.setFieldType(Global.FIELD_TYPE_TEXT);
//		
//		appMetaDataService.addTableField(appFieldInfo);
//		//appMetaDataService.updateTableField(appFieldInfo);
////		appMetaDataService.deleteTableField(new Long(67));
//	}
/*	@Test
	public void findTest() throws Exception{
		List<Object> params = new ArrayList<Object>();
		params.add("%A%");
		List<Object> list = appMetaDataService.find("", AppFieldInfo.class.getName(), "tableId = 42 and fieldName like ?", "", 0, 3, params);
		System.out.println(list.size());
	}*/
}
