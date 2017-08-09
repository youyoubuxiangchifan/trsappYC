/**
 * 
 */
package com.trs.webservice;

/**
 * Description: webservice静态变量<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: WSConstant
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-6-23 下午04:25:30
 * @version 1.0
 */
public class WSConstant {
	/**
	 * 系统异常
	 */
	public static final String SYS_ERROR = "ERROR-1000";
	/**
	 * 缺失参数
	 */
	public static final String NO_PARAM = "ERROR-1001";
	
	/**
	 * 应用没有设置元数据表
	 */
	public static final String 	NO_META = "ERROR-1002";
	/**
	 * 没有找到系统配置项
	 */
	public static final String 	NO_SYSCONFIG = "ERROR-1003";
	
	/**
	 * 视图不存在
	 */
	public static final String 	NO_VIEW = "ERROR-1004";
	/**
	 * 应用不存在
	 */
	public static final String 	NO_APP = "ERROR-1005";
	/**
	 * 不允许上传的文件类型
	 */
	public static final String 	UN_ALLOW_FILE_EXT = "ERROR-1006";
	/**
	 * 文件大小超出系统设置大小
	 */
	public static final String 	UN_ALLOW_FILE_SIZE = "ERROR-1007";
	/**
	 * 文件大小为0
	 */
	public static final String 	NULL_FILE_SIZE = "ERROR-1008";
}
