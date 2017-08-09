//package com.trs.dbhibernate.test;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import com.trs.dao.impl.BaseDaoImpl;
//import com.trs.model.AppUser;
//
///**
// * @Description: TODO(描述类的作用)<BR>
// * @TODO <BR>
// * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
// * @author liu.zhuan
// * @ClassName: BaseManagerTest
// * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
// * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
// * @date 2014-3-10 下午09:28:22
// * @version 1.0
// */
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
//public class BaseDaoTest extends AbstractJUnit4SpringContextTests{
//	@Autowired
//	private BaseDaoImpl baseManager;
//	/*@Test
//	public void queryTest(){
//		List<Object> list = baseManager.find("from AppUser");
//		for (int i = 0; i < list.size(); i++) {
//			AppUser user = (AppUser)list.get(i);
//			System.out.println(user.getUserId() + "========" + user.getUsername());
//		}
//	}
//	@Test
//	public void saveTest(){
//		AppUser user = new AppUser();
//		user.setUsername("diaosiking");
//		baseManager.save(user);
//	}*/
//	/*@Test
//	public void updateTest(){
//		AppUser user = new AppUser();
//		user.setUserId(new Long(10));
//		user.setUsername("diaosiking111");
//		baseManager.update(user);
//	}*/
//	/*@Test
//	public void pageTest(){
//		List<Object> list = baseManager.find("from AppUser order by userId",0,3);
//		for (int i = 0; i < list.size(); i++) {
//			AppUser user = (AppUser)list.get(i);
//			System.out.println(user.getUserId() + "========" + user.getUsername());
//		}
//	}*/
//	/*@Test
//	public void pageTest(){
//		List<Object> list = baseManager.find("from AppUser order by userId",0,3);
//		for (int i = 0; i < list.size(); i++) {
//			AppUser user = (AppUser)list.get(i);
//			System.out.println(user.getUserId() + "========" + user.getUsername());
//		}
//	}*/
///*	@Test
//	public void paramTest(){
//		List param = new ArrayList();
//		param.add(new Long(8));
//		List<Object> list = baseManager.find("from AppUser where userId = ?",param);
//		for (int i = 0; i < list.size(); i++) {
//			AppUser user = (AppUser)list.get(i);
//			System.out.println(user.getUserId() + "========" + user.getUsername());
//		}
//	}*/
//	/*@Test
//	public void findByIdTest(){
//		//System.out.println(((AppUser)baseManager.findById(AppUser.class, new Long(10))).getUsername());
//		System.out.println(baseManager.count("select count(*) from AppUser as a"));
//	}*/
//	/*@Test
//	public void findByArrayTest(){
//		List<Object> list = baseManager.find("from AppUser where userName = ?", new Object[]{});
//		for (int i = 0; i < list.size(); i++) {
//			AppUser user = (AppUser)list.get(i);
//			System.out.println(user.getUserId() + "========" + user.getUsername());
//		}
//	}*/
//	
//	@Test
//	public void paramTest() throws Exception{
//		List param = new ArrayList();
//		//param.add(new Long(8));
//		param.add("jinyu");
//		Map<String,Object> map = new HashMap<String, Object>();
//		map.put("selectfields", null);
//		map.put("from", AppUser.class.getName());
//		map.put("where", null);
//		map.put("order", null);
//		//map.put("parameters", param);
//		map.put("startpage", 1);
//		map.put("pagesize", 5);
//		List<Object> list = baseManager.find(map);
//		for (int i = 0; i < list.size(); i++) {
//			AppUser user = (AppUser)list.get(i);
////			Map user = (Map)list.get(i);
////			System.out.println(user.toString()+user.get("userId") + "========" + user.get("userName"));
//			System.out.println(user.getUserId() + "========" + user.getUsername());
//		}
//	}
//}
