package com.boco.bomc.vpn.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.domain.BaseObject;

public interface Dao {
	
	public Connection getConnection() throws Exception;
	//基础JDBC对应SQL操作
	public int update(String sql,Object...params) throws SQLException;
	
	public int update(String sql) throws SQLException;
	
	public <T> List<T> query(Class<T> clazz,String sql, int offset,int pageSize,Object...params) throws SQLException;
	
	public <T> List<T> query(Class<T> clazz,String sql,Object...params) throws SQLException;
	
	public <T> List<T> query(Class<T> clazz,String sql) throws SQLException;
	
	public List<Map<String,Object>> query(String sql,Object...params) throws SQLException;
	
	public List<Map<String,Object>> query(String sql) throws SQLException;
	
	public int queryCount(String sql,Object...params) throws SQLException;
	
	public int queryCount(String sql) throws SQLException;
	
	public int queryCount(BaseObject object) throws SQLException;

	//基本对象的增删改查
	public int save(BaseObject entity) throws SQLException;

	//批量处理SQL
	public int[] batch(String sql,Object[][] params) throws SQLException;
}
