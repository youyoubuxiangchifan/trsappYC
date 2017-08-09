package com.trs.service.test;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.model.AppUser;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class TRSUserServiceTest extends AbstractJUnit4SpringContextTests{
	
//	public void testSave(){
//		Session session = gethbSession();
//		session.clear();
//		//动态模型用HashMap容纳一个记录
//		HashMap hm = new HashMap();
//		hm.put("name","jinyu");
//		hm.put("email","diaosi@diaosi.com");
//		hm.put("aaa","aaa");
//		  
//		Transaction tran = session.beginTransaction();
//		session.save("Customer",hm);
//		tran.commit();
//		session.close();
//	}
	public void testQuery(){
//		List<Object> list = getQuery("from Customer ").list();
//		for (int i = 0; i < list.size(); i++) {
//			HashMap hm = (HashMap)list.get(i);
//			System.out.println(hm.get("dynamicAttributes.name"));
//			//System.out.println(hm.get("namex"));
//		}
		//Configuration config = new Configuration().configure().setProperty( Environment.FORMAT_SQL, "true").addResource("com/trs/model/abc.hbm.xml"); 
		//SchemaUpdate schemaUpdate = new SchemaUpdate(config);      
		//schemaUpdate.execute(true, true);          
		//利用动态模型操作数据库      
		//SessionFactory factory = config.buildSessionFactory(); 
//		List<Object> list = gethbSession().createQuery(" from Customer ").list();
//		for (int i = 0; i < list.size(); i++) {
//			HashMap hm = (HashMap)list.get(i);
//			System.out.println(hm.get("name"));
//			//System.out.println(hm.get("namex"));
//		}

	}
	/**
	 * 对于 AbstractJUnit4springcontextTests 和 AbstractTransactionalJUnit4SpringContextTests 类的选择：

　　		需要用到事务管理（比如要在测试结果出来之后回滚测试内容），就可以使用AbstractTransactionalJUnit4SpringTests类。
		事务管理的使用方法和正常使用Spring事务管理是一样的。
		再此需要注意的是，如果想要使用声明式事务管理，即使用AbstractTransactionalJUnitSpringContextTests类，
		请在applicationContext.xml文件中加入transactionManager bean：
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-10 下午03:22:15
	* Last Modified:
	* @return void  
	* @throws 
	* @version 1.0
	 */
//	@Resource
//    private TRSUserService tRSUserService;
	@Autowired
//	private TRSUserService trsUserService;
    @Test
    public void saveTest() {
    	AppUser trsUser = new AppUser();
		//trsUser.setUname("junit310");
    	trsUser.setUsername("jinyu");
		try {
//			trsUserService.addUser(trsUser);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
}

