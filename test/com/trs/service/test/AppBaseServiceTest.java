package com.trs.service.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.trs.model.AppGroup;
import com.trs.model.AppInfo;
import com.trs.model.AppUser;
import com.trs.model.AppViewInfo;
import com.trs.service.AppBaseService;

/**
 * Description: <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: AppBaseService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-18 下午04:09:24
 * @version 1.0
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:/config/spring-mvc.xml","/config/spring-hibernate.xml"})
public class AppBaseServiceTest extends AbstractJUnit4SpringContextTests{
	@Autowired
	private AppBaseService appBaseService;
	@Test
//	/*
//	 * 新建视图
//	 */
//	public void testAddView() throws Exception{
//		AppViewInfo viewInfo = new AppViewInfo();
//		viewInfo.setViewName("网站纠错");
//		String groupIds = "94,161,245";
//		AppUser user = (AppUser)appBaseService.findById(AppUser.class, new Long(88));
//		appBaseService.addAppViewInfo(viewInfo, groupIds, user);
//	}
//	/**
//	 * 修改视图
//	 */
//	public void testUpdateView() throws Exception{
//		AppViewInfo viewInfo = (AppViewInfo)appBaseService.findById(AppViewInfo.class, new Long(1000151));
//		viewInfo.setIsEmailWarn(1);
//		String groupIds = "94,161,245";
//		AppUser user = (AppUser)appBaseService.findById(AppUser.class, new Long(88));
//		appBaseService.updateAppViewInfo(viewInfo, groupIds, user);
//	}
//	/**
//	 * 删除视图
//	 */
//	public void testDelView() throws Exception{
//		appBaseService.deleteAppViewInfo(new Long(1000168), 1);
//	}
//	/**
//	 * 删除应用
//	 */
//	public void testDelView() throws Exception{
//		appBaseService.deleteAppInfo(new Long(1000173), 0);
//	}
	/**
	 * 测试查询
	 */
	public void testDelView() throws Exception{
		List<Object> groupList = (List<Object>)appBaseService.findByIds("new map(groupId as groupId,gname as gname)", AppGroup.class.getName(), "groupId", "470,471,472");;
		for (Object object : groupList) {
			Map<String, Object> map = (Map<String, Object>)object;
			//System.out.println(map.toString());
			for (String key : map.keySet()) {
				System.out.println(key + "===" + map.get(key));
			}
		}
	}
}
