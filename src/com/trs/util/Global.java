package com.trs.util;
/**
 * 
 * Description:系统全局变量定义<BR>
 * TODO <BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author wen.junhui
 * @ClassName: Global
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-12 下午02:55:47
 * @version 1.0
 */
public final class Global {
	/**
	 * 定义日志操作类型，保存1
	 */
	public final static int LOGS_SAVE = 1;
	/**
	 * 定义日志操作类型，修改2
	 */
	public final static int LOGS_MODIFY = 2;
	/**
	 * 定义日志操作类型，删除3
	 */
	public final static int LOGS_DEL = 3;
	/**
	 * 定义日志操作类型，登陆4
	 */
	public final static int LOGS_LOGIN = 4;
	/**
	 * 定义日志操作类型，退出5
	 */
	public final static int LOGS_LOGOUT = 5;
	/**
	 * 定义数据库类型
	 */
	public static String DB_TYPE="";
	/**
	 * 用户状态：禁止使用（用户停用）
	 */
	public final static int USER_STATUS_FORBID = 1;
	/**
	 * 用户状态：已经开通（正常状态）
	 */
	public final static int USER_STATUS_REG = 0;
	/**
	 * 用户状态：已经删除（用户已删除）
	 */
	public final static int USER_STATUS_DEL = 2;
	/**
	 * 用户彻底删除状态：已经彻底删除
	 */
	public final static int USER_IS_DELETED = 1;
	/**
	 * 用户彻底删除状态：未彻底删除
	 */
	public final static int USER_IS_NOT_DELETED = 0;
	/**
	 * 用户类型：管理员用户
	 */
	public final static int USER_IS_ADMIN = 1;
	/**
	 * 用户类型：普通用户
	 */
	public final static int USER_IS_NOT_ADMIN = 0;
	
	/**元数据管理模块**begin**/
	
	/**
	 * 元数据类型：通用类型
	 */
	public final static int METADATA_TYPE_NATIONAL = 0;
	
	/**
	 * 元数据类型：意见类型
	 */
	public final static int METADATA_TYPE_OPINION = 1;
	
	/**
	 * 元数据字段类型：文本
	 */
	public final static String FIELD_TYPE_TEXT = "text";
	
	/**
	 * 元数据字段类型：文本
	 */
	public final static String FIELD_TYPE_TEXTAREA = "textarea";
	
	/**
	 * 元数据字段类型：密码文本
	 */
	public final static String FIELD_TYPE_PASSWORD = "password";
	
	/**
	 * 元数据字段类型：复杂编辑器
	 */
	public final static String FIELD_TYPE_EDITOR = "editor";
	
	/**
	 * 元数据字段类型：编辑器
	 */
	public final static String FIELD_TYPE_SMEDITOR = "smeditor";
	
	/**
	 * 元数据字段类型：日期
	 */
	public final static String FIELD_TYPE_DATE = "date";
	
	/**
	 * 元数据字段类型：下拉框
	 */
	public final static String FIELD_TYPE_COMBOBOX = "select";
	
	/**
	 * 元数据字段类型：多选框
	 */
	public final static String FIELD_TYPE_CHECKBOX = "checkbox";
	
	/**
	 * 元数据字段类型：单选按钮
	 */
	public final static String FIELD_TYPE_RADIO ="radio";
	
	/**
	 * 元数据字段类型：数值
	 */
	public final static String FIELD_TYPE_NUMBER = "number";
	
	/**
	 * 元数据表前缀
	 */
	public final static String DB_TABLE_PREFIX = "appmetatable";
	
	/**元数据管理模块**end**/
	/**
	 * 组织用户类型：组织管理员用户
	 */
	public final static int GROUPUSER_IS_ADMIN = 1;
	/**
	 * 组织用户类型：组织普通用户用户
	 */
	public final static int GROUPUSER_IS_NOT_ADMIN = 0;
	/**
	 * 组织类型：独立组织
	 */
	public final static int GROUP_IS_INDEPENDENT = 1;
	/**
	 * 组织类型：不是独立组织
	 */
	public final static int GROUP_ISNOT_INDEPENDENT = 0;
	/**
	 * 默认页数据条数
	 */
	public final static String DEFUALT_PAGESIZE = "15";
	/**
	 * 默认页数据条数
	 */
	public final static String DEFUALT_STARTPAGE = "1";
	/**
	 * 用户密码强度：弱密码
	 */
	public final static int USER_PASSWORD_WEAK = 0;
	/**
	 * 用户密码强度：强密码
	 */
	public final static int USER_PASSWORD_STRONG = 1;
	/**
	 * 原生SQL 数据类型 int型
	 */
	public final static String S_INT = "int";
	/**
	 * 原生SQL 数据类型 String型
	 */
	public final static String s_String = "String";
	/**
	 * 原生SQL 数据类型 Long型
	 */
	public final static String s_Long = "Long";
	/**
	 * 原生SQL 数据类型 Time型
	 */
	public final static String s_Time = "Time";
	/**
	 * 原生SQL 数据类型 Time型
	 */
	public final static String s_Date = "Date";
	
	/**
	 * 前台登录用户统一名称
	 */
	public final static String CURR_LOGIN_USER = "loginUser";
	/**
	 * 附件存放路径
	 */
	public final static String APP_UPLOAD_PATH = "APP_UPLOAD_PATH";
	/**
	 * 权限添加标识
	 */
	public final static String ADDINFO = "11";
	/**
	 * 权限删除标识
	 */
	public final static String DELINFO = "14";
	/**
	 * 权限评论标识
	 */
	public final static String COMINFO = "13";
	/**
	 * 权限公开标识
	 */
	public final static String GKINFO = "12";
	/**
	 * 权限办理标识
	 */
	public final static String BLINFO = "10";
	/**
	 * 权限回收站标识
	 */
	public final static String LJINFO = "15";
	/**
	 * 应用数据推送wcm文档类型数据类型
	 */
	public final static int WCM_TYPE_DOCUMENT = 0;
	/**
	 * 应用数据推送wcm元数据类型数据类型
	 */
	public final static int WCM_TYPE_METADATA = 1;
	
	/**应用数据推送标识-begin**/
	/**
	 * 数据未推送
	 */
	public final static String SYNC_FLAG_NOT = "0";
	
	/**
	 * 数据已推送
	 */
	public final static String SYNC_FLAG_HAS = "1";
	
	/**
	 * 数据已更新
	 */
	public final static String SYNC_FLAG_EDIT = "2";
	
	/**应用数据推送标识-end**/
	/**
	 * 元数据操作：查看信息
	 */
	public final static String METADATA_OPT_INFO = "0";
	/**
	 * 元数据操作：添加
	 */
//	public final static String METADATA_OPT_ADD = "1";
	/**
	 * 元数据操作：编辑
	 */
	public final static String METADATA_OPT_EDIT = "1";
	/**
	 * 元数据操作：办理
	 */
	public final static String METADATA_OPT_HANDDLE = "2";
	
}
