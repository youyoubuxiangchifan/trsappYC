package com.trs.dao;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface AppWebDao extends BaseDao{
	public List<Object> executeQueryObjsDateFormat(String sql, List<Object> parameters, String sDateFormat) throws SQLException, IOException ;
}
