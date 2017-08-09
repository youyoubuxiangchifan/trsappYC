package com.trs.dao.test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.dao.AppMetaDataDao;
import com.trs.dao.impl.BaseDaoImpl;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: BaseDaoImplTest
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-26 下午02:16:06
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class BaseDaoImplTest {
	Logger logger = Logger.getLogger(BaseDaoImpl.class);
	@Autowired
	@Qualifier("baseDaoImpl") 
	private BaseDaoImpl baseDao;
	@Autowired
	private AppMetaDataDao appMetaDataDao;
//	@Test
//	public void testExecuteObjs() throws SQLException, IOException{
//		List<Object> params = new ArrayList<Object>();
//		//params.add("jinyu");// where username = ?
//		List<Object> list = baseDao.executeQueryObjs("select * from appmetatabletest111", params);
//		for (int i = 0; i < list.size(); i++) {
//			Map<String, Object> map = (Map)list.get(i);
//			logger.error(map.get("USERNAME") + "==" + map.get("TITLE"));
//			System.out.println(map.get("USERNAME") + "==" + map.get("TITLE"));
//		}
//	}
	@Test
	public void testExecuteSql(){
		String sql1 = "create table test1(id number)";
		String sql2 = "create table test2(id number)";
		try {
			appMetaDataDao.executeBaseSql(sql1, sql2);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
