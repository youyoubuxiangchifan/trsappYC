/**   
* @Title: AppGroupServiceTest.java 
* @Package com.trs.service.test 
* @Description: TODO 
* @author jin.yu 
* @date 2014-3-10 下午06:00:24 
* @version V1.0   
*/
package com.trs.service.test;

import java.io.File;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.dbhibernate.Page;
import com.trs.model.AppGroup;
import com.trs.model.AppUser;
import com.trs.service.AppGroupService;
import com.trs.util.DateUtil;

/**
 * @Description:<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: AppGroupServiceTest
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午06:00:24
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class AppGroupServiceTest  extends AbstractJUnit4SpringContextTests{
	@Autowired
	private AppGroupService appGroupService;
//    @Test
//    public void saveTest() {
//    	AppGroup appGroup = new AppGroup();
//    	appGroup.setGname("杭州办事处1");
//    	appGroup.setGdesc("描述说明");
//    	appGroup.setIsIndependent(1);
//    	appGroup.setParentId(new Long(161));
//		try {
//			appGroupService.addGroup(appGroup);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//    
//    @Test
//    public void updateTest() {
//		try {
//			AppGroup appGroup = (AppGroup)appGroupService.findById(AppGroup.class, new Long(34));
//	    	appGroup.setGname("杭州办事处修改");
//	    	appGroup.setGdesc("描述说明修改");
//	    	appGroup.setIsIndependent(2);
//			appGroupService.update(appGroup);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//    
//    @Test
//    public void deleteTest() {
//		try {
//			AppGroup appGroup = (AppGroup)appGroupService.findById(AppGroup.class, new Long(34));
//			appGroupService.delect(appGroup);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
	
//	  @Test
//	  public void deleteTest() {
//		  appGroupService.deleteAll(AppGroup.class, "2,5,25,30");
//	  }
//	  
//	  @Test
//	  public void findGroupUserTest() throws Exception {
//		System.out.println(DateUtil.now());
//		Page page = appGroupService.findGrpUserSel(null, 1000000, 20);
//		List<Object> listUser = page.getLdata();
//		for (int i = 0; i < listUser.size(); i++) {
//			//System.out.println(((Map)listUser.get(i)).get("username"));
//		}
//		System.out.println(DateUtil.now());
//	  }
//	  @Test
//	  public void findChildGroupTest() throws Exception {
//		Page page = appGroupService.findChildGroup(new Long(57), "", 2, 3);
//		System.out.println(page.getTotalResults());
//		List<Object> listUser = page.getLdata();
//		for (int i = 0; i < listUser.size(); i++) {
//			System.out.println(((AppGroup)listUser.get(i)).getGname()+"-----"+((AppGroup)listUser.get(i)).getGroupId());
//		}
//	  }
//	  @Test
//	  public void isIndependentTest() {
//		  System.out.println(appGroupService.isIndependent(new Long(57)));
//	  }
	
//	  @Test
//	  public void deleteAllGroupTest() throws Exception {
//		  appGroupService.deleteGroup("259",new Long(161));
//	  }
	
//	  @Test
//	  public void addGroupUserTest() throws NumberFormatException, Exception {
//		  AppUser appUser = (AppUser)appGroupService.findById(AppUser.class, new Long(446));
//		  appGroupService.addGroupUser(new Long(94), "74,75,76,77",appUser);
//	  }
//	@Test
//	public void addGroupUserTest() {
//    	AppUser trsUser = new AppUser();
//    	trsUser.setUsername("jinyu123");
//    	trsUser.setTruename("金煜");
//    	trsUser.setPassword("12345678");
//    	trsUser.setStatus(0);
//		appGroupService.addGroupUser(new Long(82), trsUser);
//	}
//	  @Test
//	  public void findByIdsTest() throws Exception {
//		List<Object> listGroup = appGroupService.findByIds("AppGroup", "groupId", "57,58");
//		for (int i = 0; i < listGroup.size(); i++) {
//			System.out.println(((AppGroup)listGroup.get(i)).getGname()+"-----"+((AppGroup)listGroup.get(i)).getGroupId());
//		}
//		  
//	  }
//	  @Test
//	  public void delGroupUserTest() {
//		  appGroupService.delGroupUser("84,85");
//	  }	
//	  @Test
//	  public void setGrpUserAdminTest() {
//		  appGroupService.setGrpUserAdmin("86,87,89");
//	  }
//	  @Test
//	  public void cancelGrpUserAdminTest() {
//		  appGroupService.cancelGrpUserAdmin("86,87,89");
//	  }
//	@Test
//	  public void userIsGrpAdminTest() {
//		  System.out.println(appGroupService.userIsGrpAdmin(new Long(78),new Long(82)));
//	  }
//	@Test
//	public void orderByGroupTest() throws Exception{
//		appGroupService.orderByGroup(new Long(90), 1);
//	}
    
//	@Test
//	public void importGroupTest() throws Exception{
//		AppUser appUser = (AppUser)appGroupService.findById(AppUser.class, new Long(446));
//		String filePath = "E:\\test.txt"; 
//		File file=new File(filePath);
//		appGroupService.importGroup(new Long(246), file,appUser);
//	}
}
