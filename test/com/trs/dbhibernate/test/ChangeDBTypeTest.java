package com.trs.dbhibernate.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.dbhibernate.ChangeDBType;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class ChangeDBTypeTest extends AbstractJUnit4SpringContextTests{

	@Test
	public void changeDbTypeSqlTest(){
		ChangeDBType changeDBType = new ChangeDBType();
		//  sFields , sFrom , sWhere, sOrder, startRecord, endRecord
		//System.out.println(changeDBType.changeDbTypeSql("username,address", "app_user", " zipcode=331212 ", " username desc",0,10));
		System.out.println(changeDBType.changeDbTypeSql("username,address", "app_user", " zipcode=331212 ", " username desc",0,10));
		System.out.println(changeDBType.changeDbTypeSql("*", "app_user", null, " ",0,10));
		
	}
}
