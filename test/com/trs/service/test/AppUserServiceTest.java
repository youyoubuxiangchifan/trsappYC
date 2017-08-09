/**   
* @Title: AppUserServiceTest.java 
* @Package com.trs.service.test 
* @Description: TODO 
* @author jin.yu 
* @date 2014-3-10 下午05:42:25 
* @version V1.0   
*/
package com.trs.service.test;

import java.util.List;

import org.hibernate.sql.Update;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.model.AppGroup;
import com.trs.model.AppUser;
import com.trs.service.AppUserService;

/**
 * @Description:<BR>
 * @TODO <BR>
 * @Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author jin.yu
 * @ClassName: AppUserServiceTest
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-10 下午05:42:25
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class AppUserServiceTest extends AbstractJUnit4SpringContextTests{
	@Autowired
	private AppUserService appUserService;
//    @Test
//    public void saveTest() {
//    	AppUser trsUser = new AppUser();
//    	trsUser.setUsername("kingtang");
//    	trsUser.setTruename("金煜");
//    	trsUser.setPassword("12345678");
//    	trsUser.setStatus(0);
//    	trsUser.setCruser("admin");
//		try {
//			appUserService.addUser(trsUser);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
    
//    @Test
//    public void updateUserTest() throws Exception {
//    	AppUser appUser = (AppUser)appUserService.findById(AppUser.class, new Long(446));
//    	appUser.setTruename("糖糖");
//		try {
//			appUserService.update(appUser);
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }

//	  @Test
//	  public void stopUserTest() throws Exception {
//		System.out.println(CMyDateTime.now().getTimeInMillis());
//		try {
//			appUserService.stopUser("4,6,7,8,9,10,11,12,13,14,15,18,20");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//		}
//		System.out.println(CMyDateTime.now().getTimeInMillis());
//	  }

//	@Test
//	  public void logicalDelUserTest() throws Exception {
//		try {
//			appUserService.logicalDelUser( new Long(20));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	  }
	
//	@Test
//	  public void deleteUserTest() throws Exception {
//		try {
//			appUserService.deleteUser( new Long(20));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	  }
	
//	@Test
//	  public void recoverUserTest() throws Exception {
//		try {
//			appUserService.recoverUser( new Long(20));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	  }
	
//	@Test
//	  public void recoverUserTest() throws Exception {
//		try {
//			appUserService.recoverUser( new Long(20));
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	  }
	
//    @Test
//    public void countUserTest() throws Exception {
//    	List<Object> param = new ArrayList<Object>();
//    	param.add("jinyu");
//    	System.out.println(appUserService.userCount("username=?",param));
//    }
    
//    @Test
//    public void findUsersTest() throws Exception {
//    	List<Object> param = new ArrayList<Object>();
//    	param.add("jinyu");
//    	Page page =  appUserService.findUsers("","username=?","userId desc",2,3,param);
//    	List<Object> parameters = page.getLdata();
//    	if (parameters != null) {
//			for (int i = 0; i < parameters.size(); i++) {
//				System.out.println(((AppUser)parameters.get(i)).getUsername()+"-----"+((AppUser)parameters.get(i)).getUserId());
//			}
//		}
//    	System.out.println(page.getTotalResults());
//    }
	
//  @Test
//  public void existUserTest() throws Exception {
//  	System.out.println(appUserService.existUser("jinyu1"));
//  }
	
//	  @Test
//	  public void importUserTest() throws Exception {
//		  File file = new File("E:\\互动数据库.xls");
//		  appUserService.importUser(file);
//	  }
	
//	  @Test
//	  public void getUserGroupsTest() throws Exception {
//		 List<Object> list = appUserService.getUserGroups(new Long(74));
//		 for(int i=0;i<list.size();i++){
//			 AppGroup appGroup = (AppGroup)list.get(i);
//			 System.out.println(appGroup.getGname());
//		 }
//	  }
}
