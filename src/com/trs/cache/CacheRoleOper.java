package com.trs.cache;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

import com.trs.model.AppRoleOper;
import com.trs.service.AppBaseService;
/**
 * spring容器启动时加载数据类
 * Description:用户在系统启动时需要获取数据库数据加载<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: CacheRoleOper
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-7-29 下午02:28:27
 * @version 1.0
 */
public class CacheRoleOper implements BeanPostProcessor {
	private static  Logger LOG =  Logger.getLogger(CacheRoleOper.class);
	public static HashMap<Long, String> ROLEMAP;
	public static HashMap<Long, String> OPERMAP;
	public Object postProcessAfterInitialization(Object obj, String arg1)
			throws BeansException {
		try {  
            	if(obj instanceof AppBaseService){
            		if(ROLEMAP==null||OPERMAP==null){
		            	LOG.info("系统正在加载【角色操作】信息,请稍等……");
		            	HashMap<Long, String> rolemap = new HashMap<Long, String>();
		            	HashMap<Long, String> opermap = new HashMap<Long, String>();
		            	List<Object> roleOpers= ((AppBaseService)obj).getCacheAppRoleOper();//加载角色与操作的对应关系
		            	
		            	for(Object objRoleOper : roleOpers){
		            		StringBuffer str = new StringBuffer();
		            		StringBuffer operStr = new StringBuffer();
		            		AppRoleOper roleOper = (AppRoleOper)objRoleOper;
		            		if(rolemap.containsKey(roleOper.getAppId())){
		            			str.append(rolemap.get(roleOper.getAppId())).append(",");
		            			if(str.indexOf(roleOper.getRoleId()+"")<1){ //TODO 这里有重复的值
		            				str.append(roleOper.getRoleId());
		            			}
		            			rolemap.remove(roleOper.getAppId());
		            			rolemap.put(roleOper.getAppId(), str.toString());
		            		}else{
		            			rolemap.put(roleOper.getAppId(), roleOper.getRoleId().toString());
		            		}
		            		if(opermap.containsKey(roleOper.getRoleId())){
		            			operStr.append(opermap.get(roleOper.getRoleId())).append(",").append(roleOper.getOperId());
		            			opermap.remove(roleOper.getRoleId());//TODO 这里有重复的值
		            			opermap.put(roleOper.getRoleId(), operStr.toString());
		            		}else{
		            			opermap.put(roleOper.getRoleId(), roleOper.getOperId().toString());
		            		}
		            	}
		            	ROLEMAP = rolemap;
		            	OPERMAP = opermap;
		           	 	LOG.info("系统加载【角色操作】信息完成");
            		}
           }
        } catch (Exception e) {  
             LOG.error("系统加载【角色、应用、操作】信息失败:"+e);
        }   
         return obj;  
	}

	public Object postProcessBeforeInitialization(Object arg0, String arg1)
			throws BeansException {
		return arg0;
	}
	
}
