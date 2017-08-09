package com.trs.dao;

import java.sql.SQLException;


/**
 * Description: 数据库表信息，表结构，字段信息操作接口<BR>
 * Title: TRS 杭州办事处互动系统（TRSAPP）<BR>
 * @author liu.zhuan
 * @ClassName: TRSAppTableInfoDao
 * @Copyright: Copyright (c) TRS北京拓尔思信息技术股份有限公司<BR>
 * @Company: TRS北京拓尔思信息技术股份有限公司杭州办事处(www.trs.com.cn)<BR>
 * @date 2014-3-7 下午02:36:45
 * @version 1.0
 */
public interface AppMetaDataDao extends BaseDao {
	public void executeBaseSql(String sql1, String sql2) throws SQLException;
}
