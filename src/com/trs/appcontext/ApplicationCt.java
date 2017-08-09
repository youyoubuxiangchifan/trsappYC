package com.trs.appcontext;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.context.support.ApplicationObjectSupport;

import com.trs.model.AppFlowContext;

/**
 * 获取ApplicationContext
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: ApplicationCt
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-27 上午10:37:01
 * @version 1.0
 */
public class ApplicationCt extends ApplicationObjectSupport{
//	/**
//	 * 
//	* Description:<BR>  
//	* @author wen.junhui
//	* @date Create: 2014-3-27 上午11:12:57
//	* Last Modified:
//	* @return
//	* @version 1.0
//	 */
//	public ApplicationContext getSystem(){
//		return getApplicationContext();
//	}
/**
 * 通过spring的ApplicationContext动态获取 配置文件中的bean ，来调用不同的方法
* Description:<BR>  
* @author wen.junhui
* @date Create: 2014-3-27 上午11:13:14
* Last Modified:
* @param className spring注入的beanid
* @param methodName 方法名字
* @return
* @version 1.0
 */
	public String dynamicClass(String className,String methodName,AppFlowContext appFlowContext){
		String obj = null;
		Method[] methods	= getApplicationContext().getBean(className).getClass().getMethods();
		for(Method  m: methods){
			if(m.getName().equals(methodName)){
				try {
					Method method = getApplicationContext().getBean(className).getClass().getMethod(methodName,new Class[]{AppFlowContext.class});
					obj=(String)method.invoke(getApplicationContext().getBean(className), new Object[]{appFlowContext});
				} catch (BeansException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
		}
	return obj;
	}
	/**
	 * 返回自定义操作动态加载类方法的数据
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-4-23 下午08:25:03
	* Last Modified:
	* @param className
	* @param methodName
	* @param appFlowContext
	* @return
	* @version 1.0
	 */
	public Map<String, Object> dynamicClassMap(String className,String methodName,AppFlowContext appFlowContext){
		Map<String, Object>  obj = null;
		Method[] methods	= getApplicationContext().getBean(className).getClass().getMethods();
		for(Method  m: methods){
			if(m.getName().equals(methodName)){
				try {
					Method method = getApplicationContext().getBean(className).getClass().getMethod(methodName,new Class[]{AppFlowContext.class});
					obj=(Map<String, Object>)method.invoke(getApplicationContext().getBean(className), new Object[]{appFlowContext});
				} catch (BeansException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalStateException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			
			}
			
		}
	return obj;
	}
}
