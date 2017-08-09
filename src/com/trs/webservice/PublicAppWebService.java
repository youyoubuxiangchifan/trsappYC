package com.trs.webservice;

import javax.jws.WebParam;
import javax.jws.WebService;

/**
 * Description: TRS互动系统对外开放的webservice<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: PublicAppWebService
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-4-16 下午04:32:48
 * @version 1.0
 */
@WebService
public interface PublicAppWebService {
	/**
	 * 查询应用数据
	 * Description: 查询应用数据 <BR>   
	 * @author liu.zhuan
	 * @date 2014-4-18 下午06:16:13
	 * @param xmlDoc
	 * 格式为：
	 * <root>
	 * 		<condition>
	 * 			<selectFields>field1,field2,...</selectFields>
	 * 			<appId>0</appId>当应用编号为0时根据视图编号查询,即应用编号的优先级高于视图编号
	 * 			<viewId>10</viewId>视图编号不可以为空或为0
	 * 			<status>0</status>
	 * 			<maxResult>10</maxResult>
	 * 		</condition>
	 *</root>
	 * @return json字符串或errorcode:0表示参数列表没有输入，1表示系统可能出现的异常。
	 * @version 1.0
	 */
	public String queryAppData(@WebParam(name="xmlDoc") String xmlDoc);
	/**
	 * 查询用户数据
	 * Description: 查询用户数据<BR>   
	 * @author liu.zhuan
	 * @date 2014-4-18 下午06:16:13
	 * @param xmlDoc
	 * 格式为：
	 * <root>
	 * 		<condition>
	 * 			<selectFields>field1,field2,...</selectFields>
	 * 			<maxResult>10</maxResult>
	 * 		</condition>
	 *</root>
	 * @return json字符串或errorcode:0表示参数列表没有输入，1表示系统可能出现的异常。
	 * @version 1.0
	 */
	public String queryUserData(@WebParam(name="xmlDoc") String xmlDoc);
	
	/**
	 * 查询组织数据
	 * Description: 查询组织数据<BR>   
	 * @author liu.zhuan
	 * @date 2014-4-18 下午06:16:13
	 * @param xmlDoc
	 * 格式为：
	 * <root>
	 * 		<condition>
	 * 			<selectFields>field1,field2,...</selectFields>
	 * 			<maxResult>10</maxResult>
	 * 		</condition>
	 *</root>
	 * @return json字符串或errorcode:0表示参数列表没有输入，1表示系统可能出现的异常。
	 * @version 1.0
	 */
	public String queryGroupData(@WebParam(name="xmlDoc") String xmlDoc);
}
