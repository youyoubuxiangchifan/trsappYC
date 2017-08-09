package com.trs.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.trs.model.AppUser;

/**
 * 处理控制层请求参数的一些工具类
 * Description: 处理控制层请求参数的一些工具类<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: CrtlUtil
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-27 下午10:12:35
 * @version 1.0
 */
public class CrtlUtil {
	public static final long dayMills = 24*60*60*1000;
	/**
	 * 获取httpsession
	* Description:  获取httpsession<BR>   
	* @author liu.zhuan
	* @date 2014-3-27 下午10:02:37
	* @param request
	* @return
	* @version 1.0
	 * @throws Exception 
	 */
	public static HttpSession getSession(HttpServletRequest request){
		//checkSession(request);
		return request.getSession();
	}
	/**
	 * 获取session中对象
	* Description:  获取session中对象<BR>   
	* @author liu.zhuan
	* @date 2014-3-27 下午09:47:33
	* @param <T>
	* @param request
	* @param key
	* @return
	* @version 1.0
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getObjFromSession(HttpServletRequest request, String key){
		//checkSession(request);
		return (T) request.getSession().getAttribute(key);
	}
	/**
	 * 获取当前登录用户
	* Description: 获取当前登录用户<BR>   
	* @author liu.zhuan
	* @date 2014-3-27 下午09:58:58
	* @param request
	* @return 当前登录用户
	* @version 1.0
	 * @throws Exception 
	 * @throws Exception 
	 */
	public static AppUser getCurrentUser(HttpServletRequest request){
		return (AppUser)getObjFromSession(request, Global.CURR_LOGIN_USER);
	}
	
	/**	判断会话是否过期 
	 * @throws Exception */
	public static void checkSession(HttpServletRequest request) throws Exception {
		if (request.getSession(false) == null)
			throw new Exception("当前session会话已过期!");
	}
	
	/** 获得当前本地路径 */
    public static String getRealPath(HttpServletRequest req) {
    	return req.getSession().getServletContext().getRealPath("/");
    }
    
    /** 获得用户真实IP */
    public static String getRealIp(HttpServletRequest req) {
    	String ip = req.getHeader("x-forwarded-for");
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = req.getHeader("Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = req.getHeader("WL-Proxy-Client-IP");
	    }
	    if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	        ip = req.getRemoteAddr();
	    }
	    return ip.split(",")[0].trim();
    }
    public synchronized static String  generatCode(){
    	String code = "";
    	code = DateUtil.format(new Date(), "yyyyMMdd");
    	code += (System.currentTimeMillis()%dayMills)/1000;
    	code += CMyString.generateNumStr(3);
    	return code;
    }
    public static void main(String[] args){
    	for(int i=0;i<10000;i++){
    		System.out.println(System.currentTimeMillis());
    		System.out.println(System.currentTimeMillis()%dayMills);
    		try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	/*List<String> list = new ArrayList<String>();
    	for(int i=0;i<10000;i++){
    		String code = generatCode();
    		if(list.contains(code))
    			System.out.println("xxx="+code);
    		list.add(generatCode());
    		System.out.println(i);
    	}*/
    		
	}
}
