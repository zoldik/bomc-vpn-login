package com.boco.bomc.vpn.db;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.boco.bomc.vpn.db.impl.oracle.OracleDaoSupport;
import com.boco.bomc.vpn.domain.BaseObject;

public class DaoSupport<T>{
    
    private Dao dao = new OracleDaoSupport();
    
    public DaoSupport(){
        //if(DBManager.dbType == DB.ORACLE){
        //    this.dao = new OracleDaoSupport();
        //}
    }

	public int[] batch(String sql, Object[][] params) throws SQLException {
		return dao.batch(sql, params);
	}

	public Connection getConnection() throws Exception {
		return dao.getConnection();
	}

	public List<T> query(Class<T> clazz, String sql, int offset,int pageSize,Object...params) throws SQLException {
		return dao.query(clazz, sql, offset,pageSize, params);
	}

	public List<T> query(Class<T> clazz, String sql, Object...params) throws SQLException {
		return dao.query(clazz, sql, params);
	}

	public List<T> query(Class<T> clazz, String sql) throws SQLException {
		return dao.query(clazz, sql);
	}

	public List<Map<String, Object>> query(String sql, Object... params) throws SQLException {
		return dao.query(sql, params);
	}

	public List<Map<String, Object>> query(String sql) throws SQLException {
		return dao.query(sql);
	}

	public int queryCount(String sql, Object... params) throws SQLException {
		return dao.queryCount(sql, params);
	}

	public int queryCount(String sql) throws SQLException {
		return dao.queryCount(sql);
	}

	public int queryCount(BaseObject object) throws SQLException {
		return dao.queryCount(object);
	}

	public int save(BaseObject entity) throws SQLException {
		return dao.save(entity);
	}

	public int update(String sql, Object... params) throws SQLException {
		return dao.update(sql, params);
	}

	public int update(String sql) throws SQLException {
		return dao.update(sql);
	}
}
