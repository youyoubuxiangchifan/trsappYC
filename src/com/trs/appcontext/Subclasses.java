package com.trs.appcontext;


import java.util.Map;

import org.hibernate.HibernateException;

import com.trs.model.AppFlowContext;
import com.trs.model.AppUser;
public class Subclasses extends IToUsersCreator{
	public Subclasses(){}

	public String createToUsers(AppFlowContext appFlowContext){
		StringBuffer appUserIds = new StringBuffer();
		Object obj = new Object();
			try {
				obj = findById(AppUser.class,74l);
			} catch (HibernateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			AppUser appuser =  (AppUser)obj;
			appUserIds.append(appuser.getUserId()).append(",");
			
		return appUserIds.toString().substring(0,appUserIds.toString().length()-1);
		
	}
	public  void getSubclass(){
		System.out.println("动态加载类");
	}
	public  void getSubclass1(String name){
		System.out.println("动态加载类带参数"+name);
	}
	public  void getSubclass2(String name,int i){
		System.out.println("动态加载类带参数"+name+"我是参数2:"+i);
	}

	@Override
	public Map<String, Object> createNodeIdAndFLowUser(AppFlowContext appFlowContext) {
		// TODO Auto-generated method stub
		return null;
	}

	
}
