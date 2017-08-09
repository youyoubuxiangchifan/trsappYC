package com.trs.dbhibernate;

import java.util.Enumeration;
import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;

import com.trs.util.CMyString;
import com.trs.util.Global;

/**
 * 获取数据库类型、封装基于oracle、mysql、sqlserver 的特殊SQL语句
 * Description:<BR>
 * Title: TRS 杭州办事处互动系统(TRSAPP)<BR>
 * @author wen.junhui
 * @ClassName: ChangeDBType
 * Copyright: Copyright (c) 2004-2005 TRS北京拓尔思信息技术股份有限公司<BR>
 * Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-12 下午04:28:30
 * @version 1.0
 */
public class ChangeDBType {
	private static  Logger LOG =  Logger.getLogger(Global.class);
	public ChangeDBType(){
		getDataBaseType();
	}
	/**
	 * 系统配置文件中初始化当前数据库类型
	* Description:目前支持三种数据库  oracle、mysql、sqlserver<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-12 下午05:14:34
	* Last Modified:
	* @version 1.0
	 */
	public  void getDataBaseType(){
		PropertyResourceBundle resourceBundle = (PropertyResourceBundle) PropertyResourceBundle
		.getBundle("config/jdbc");
		String dbType=null;
		Enumeration<String> enu = resourceBundle.getKeys();
		while (enu.hasMoreElements()) {
			String propertyName = enu.nextElement().toString();
			if (propertyName.equals("proxool.DBTYPE"))
				dbType = resourceBundle.getString("proxool.DBTYPE");
		}
		Global.DB_TYPE= dbType;
		LOG.info("TRSAPP应用初始化数据库类型成功，初始化数据库类型为["+Global.DB_TYPE+"]");
	}
	/**
	 * 返回原生SQL 语句 ，带分页参数
	* Description:系统自动判断数据库类型后返回对应数据库类型的sql语句<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-24 下午03:40:37
	* Last Modified:
	* @param sFields 要查询的字段组合，必须是数据库中的字段名 ，以逗号分隔,支持*号
	* @param sFrom  要查询的表名称，必须是数据库实际的表名称 ，不需要from 关键字
	* @param sWhere 查询的where 语句，必须是数据库的字段名 ,值为问号[？]，例如：username=? and address = ? 可为null
	* @param sOrder 排序字段,需要指明排序方式 ，排序方式 可选 desc 和 asc，为 null 不排序 ，需要order by 关键字
	* @param startRecord 查询记录的结束位置
	* @param endRecord 查询记录的开始位置
	* @return String 组装好的原生SQL
	* @version 1.0
	 */
	public static String changeDbTypeSql(String sFields ,String sFrom ,String sWhere,String sOrder,int startRecord,int endRecord){
		StringBuffer sSql = new StringBuffer();
		if(Global.DB_TYPE.equals("oracle")){
			sSql.append("select *from(select rownum rwn , a1.* from ").append("(select ").append(sFields).append(" from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
			if(!CMyString.isEmpty(sOrder) )	
				sSql.append(" order by ").append(sOrder).append(" ");
				sSql.append(")").append(" a1 where rownum<=").append(endRecord).append(	") where ").append("rwn >").append(startRecord);
		}
		if(Global.DB_TYPE.equals("sqlserver")){
			
			sSql.append("select *from ( select row_number()over(order by tempColumn");
			sFields =CMyString.replaceStr(sFields, "distinct", "");
			sSql.append(") as RowNumber ,").append(" *from (").append(" select distinct top " ).append(endRecord).append(sFields).append(", tempColumn=0").append(" from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
			if(!CMyString.isEmpty(sOrder) )	
				sSql.append(" order by ").append(sOrder);
				sSql.append(")t ").append(")tt").append(" where RowNumber >=").append(startRecord);
		}
		if(Global.DB_TYPE.equals("mysql")){
			sSql.append("select ").append(sFields).append(" from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
			if(!CMyString.isEmpty(sOrder) )	
				sSql.append(" order by ").append(sOrder).append(" ");
				sSql.append(" LIMIT ").append(startRecord).append(",").append(endRecord);
		}
		return sSql.toString();
	}
	/**
	 * 返回原生SQL 语句 ，带分页参数
	* Description:系统自动判断数据库类型后返回对应数据库类型的sql语句<BR>  
	* @author copy by liu.zhuan
	* @date Create: 2014-3-24 下午03:40:37
	* Last Modified:
	* @param sFields 要查询的字段组合，必须是数据库中的字段名 ，以逗号分隔,支持*号
	* @param sFrom  要查询的表名称，必须是数据库实际的表名称 ，不需要from 关键字
	* @param sWhere 查询的where 语句，必须是数据库的字段名 ,值为问号[？]，例如：username=? and address = ? 可为null
	* @param sOrder 排序字段,需要指明排序方式 ，排序方式 可选 desc 和 asc，为 null 不排序 ，需要order by 关键字
	* @param startRecord 查询记录的结束位置
	* @param endRecord 查询记录的开始位置
	* @return String 组装好的原生SQL
	* @version 1.0
	 */
	public static String changeDbTypeSql(String sFields ,String sFrom ,String sWhere,String sOrder,int endRecord){
		int startRecord = 0;
		StringBuffer sSql = new StringBuffer();
		if(Global.DB_TYPE.equals("oracle")){
			sSql.append("select *from(select rownum rwn , a1.* from ").append("(select ").append(sFields).append(" from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
			if(!CMyString.isEmpty(sOrder) )	
				sSql.append(" order by ").append(sOrder).append(" ");
				sSql.append(")").append(" a1 where rownum<=").append(endRecord).append(	") where ").append("rwn >").append(startRecord);
		}
		if(Global.DB_TYPE.equals("sqlserver")){
			
			sSql.append("select *from ( select row_number()over(order by tempColumn");
			sFields =CMyString.replaceStr(sFields, "distinct", "");
			sSql.append(") as RowNumber ,").append(" *from (").append(" select top " ).append(endRecord).append(sFields).append(", tempColumn=0").append(" from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
			if(!CMyString.isEmpty(sOrder) )	
				sSql.append(" order by ").append(sOrder);
				sSql.append(")t ").append(")tt").append(" where RowNumber >=").append(startRecord);
		}
		if(Global.DB_TYPE.equals("mysql")){
			sSql.append("select ").append(sFields).append(" from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
			if(!CMyString.isEmpty(sOrder) )	
				sSql.append(" order by ").append(sOrder).append(" ");
				sSql.append(" LIMIT ").append(startRecord).append(",").append(endRecord);
		}
		return sSql.toString();
	}
	/**
	 * 返回查询总记录数的原生SQL
	* Description:<BR>  
	* @author wen.junhui
	* @date Create: 2014-3-24 下午08:20:37
	* Last Modified:
	* @param sFrom 查询的表名称，必须是数据库中的表名
	* @param sWhere 查询条件
	* @return String 查询总记录数的原生SQL
	* @version 1.0
	 */
	public static String SqlCountRecord(String sFrom ,String sWhere){
		StringBuffer sSql = new StringBuffer();
		if(Global.DB_TYPE.equals("oracle")){
			sSql.append("select count(*)from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
		}
		if(Global.DB_TYPE.equals("sqlserver")){
			sSql.append("select count(*)from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
			
		}
		if(Global.DB_TYPE.equals("mysql")){
			sSql.append("select count(*)from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
		}
		return sSql.toString();
	}
	
	/**
	 * 返回查询总记录数的原生SQL
	* Description:<BR>  
	* @author copy by liu.zhuan
	* @date Create: 2014-3-24 下午08:20:37
	* Last Modified:
	* @param sFrom 查询的表名称，必须是数据库中的表名
	* @param sWhere 查询条件
	* @param sField 字段名
	* @return String 查询总记录数的原生SQL
	* @version 1.0
	 */
	public static String SqlCountRecord(String sFrom , String sField, String sWhere){
		StringBuffer sSql = new StringBuffer();
		if(Global.DB_TYPE.equals("oracle")){
			sSql.append("select count(").append(sField).append(") from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
		}
		if(Global.DB_TYPE.equals("sqlserver")){
			sSql.append("select count(*)from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
			
		}
		if(Global.DB_TYPE.equals("mysql")){
			sSql.append("select count(*)from ").append(sFrom);
			if(!CMyString.isEmpty(sWhere))
				sSql.append(" where ").append(sWhere);
		}
		return sSql.toString();
	}
	
}
