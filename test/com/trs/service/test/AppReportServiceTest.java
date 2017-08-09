package com.trs.service.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.service.AppReportService;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class AppReportServiceTest extends AbstractJUnit4SpringContextTests{
	private static  Logger LOG =  Logger.getLogger(AppReportServiceTest.class);
	@Autowired
	private AppReportService appReportService;
//	@Test
//	public void queryGroupOralceTest(){
//		try {
//		List<Object> tables =	queryTableName();
//			for(Object tabs : tables){
//				HashMap<String, String> tableName = (HashMap<String, String>) tabs;
//				
//				List<Object> resu = 	appReportService.queryGroupOralce(tableName.get("MAIN_TABLE_NAME"));
//				for(Object obj : resu){
//					
//					HashMap<String, String> result = (HashMap<String, String>) obj;
//					StringBuffer str = new StringBuffer();
////					for(Object key : result.keySet()){
////						System.out.println(key);
////						str.append(result.get(key)).append("|");
////					}
//					System.out.println("表名为"+tableName+"记录值为："+result.get("RECORDCOUNT"));
//					System.out.println("=======================================");
//				}
//			}
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	public List<Object> queryTableName() throws SQLException, IOException, Exception{
//		return appReportService.queryTableName();
//	}
}
