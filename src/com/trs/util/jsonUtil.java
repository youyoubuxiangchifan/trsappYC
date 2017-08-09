/**
 * Description:<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.jian
 * 
 * @ClassName: jsonUtil
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-17 下午05:23:13
 * @version 1.0
 */
package com.trs.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Description: 获取通用json字符串<BR>
 * 
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * author liujian
 * ClassName: jsonUtil
 * Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * date 2014-3-17 下午05:23:13
 * version 1.0
 */
public class jsonUtil {
	
	/** 
	* Description: 获取通用json字符串 <BR>   
	* author liujian
	* date 2014-3-17 下午05:56:23
	* @param statusCode 返回状态值 300-操作失败、200-操作成功、301-会话超时
	* @param message 返回提示信息
	* @param navTabId 返回到前端是刷新的窗口ID
	* @param rel 
	* @param callbackType 返回类型-closeCurrent关闭当前窗口
	* @param forwardUrl 
	* @return
	* version 1.0
	*/
	public static String getJsonStr(String statusCode,String message,String navTabId,String rel,String callbackType,String forwardUrl){
		if(CMyString.isEmpty(statusCode)){
			statusCode="300";
		}
		if(CMyString.isEmpty(message)){
			message ="操作失败";
		}
		if(navTabId ==null){
			navTabId= "";
		}
		if(rel ==null){
			navTabId= "";
		}
		if(callbackType ==null){
			navTabId= "";
		}
		if(forwardUrl ==null){
			navTabId= "";
		}		
		String jsonStr="{\"statusCode\":\""+statusCode+"\",\"message\":\""+CMyString.native2Ascii(message)+"\",\"navTabId\":\""+navTabId+"\",\"rel\":\""+rel+"\",\"callbackType\":\""+callbackType+"\",\"forwardUrl\":\""+forwardUrl+"\"}";
		
		return jsonStr;
	}
	public static String getJsonStr(String statusCode,String message,String callbackType,String forwardUrl,String title){
		if(CMyString.isEmpty(statusCode)){
			statusCode="300";
		}
		if(CMyString.isEmpty(message)){
			message ="操作失败";
		}
		if(callbackType ==null){
			callbackType= "";
		}
		if(CMyString.isEmpty(title)){
			title ="";
		}
		String jsonStr="{\"statusCode\":\""+statusCode+"\",\"message\":\""
		+CMyString.native2Ascii(message)+"\",\"callbackType\":\""+callbackType+"\",\"forwardUrl\":\""
		+forwardUrl+"\",\"title\":\""+CMyString.native2Ascii(title)+"\"}";
		
		return jsonStr;
	}
	/**
	 * Description: json字符串转map集合 <BR>   
	 * @author liu.zhuan
	 * @date 2014-6-24 下午02:44:21
	 * @param jsonStr
	 * @return Map<String, Object>
	 * @version 1.0
	 */
	public static Map<String, Object> parseJSON2Map(String jsonStr){
        Map<String, Object> map = new HashMap<String, Object>();
        //最外层解析
        JSONObject json = JSONObject.fromObject(jsonStr);
        for(Object k : json.keySet()){
            Object v = json.get(k); 
            //如果内层还是数组的话，继续解析
            if(v instanceof JSONArray){
                List<Map<String, Object>> list = new ArrayList<Map<String,Object>>();
                Iterator<JSONObject> it = ((JSONArray)v).iterator();
                while(it.hasNext()){
                    JSONObject json2 = it.next();
                    list.add(parseJSON2Map(json2.toString()));
                }
                map.put(k.toString(), list);
            } else {
                map.put(k.toString(), v);
            }
        }
        return map;
    }
}
