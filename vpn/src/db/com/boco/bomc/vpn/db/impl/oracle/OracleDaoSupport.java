package com.boco.bomc.vpn.db.impl.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Map;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import com.boco.bomc.vpn.db.DBManager;
import com.boco.bomc.vpn.db.Dao;
import com.boco.bomc.vpn.db.exception.DBException;
import com.boco.bomc.vpn.domain.BaseObject;
import com.boco.bomc.vpn.util.ClassUtils;

/**
 * MySql数据库链接实现类
 *
 * @version 1.0.0
 *
 */
public class OracleDaoSupport implements Dao{

	private QueryRunner queryRunner = new QueryRunner();
	
	public OracleDaoSupport(){
	}
	
	public Connection getConnection(){
		try{
			return DBManager.getConnection();
		}catch(SQLException e){
			throw new DBException(e);
		}
	} 
	
	
	public int save(BaseObject entity) throws SQLException{
		Map<String, Object> objProps = ClassUtils.getBeanProperties(entity);
		String[] fields = objProps.keySet()
				.toArray(new String[objProps.size()]);
		StringBuilder sql = new StringBuilder("INSERT INTO ");
		sql.append(entity.getTableName());
		sql.append("(");
		for (int i = 0; i < fields.length; i++) {
			if (i > 0)
				sql.append(",");
			sql.append(ClassUtils.propertyToField(fields[i]));
		}
		sql.append(") VALUES(");
		for (int i = 0; i < fields.length; i++) {
			if (i > 0)
				sql.append(",");
			sql.append("?");
		}
		sql.append(")");
		PreparedStatement ps = null;
		ResultSet rs = null;

		try {
			ps = getConnection().prepareStatement(sql.toString(),
					PreparedStatement.RETURN_GENERATED_KEYS);
			for (int i = 0; i < fields.length; i++) {
				ps.setObject(i + 1, objProps.get(fields[i]));
			}
			ps.executeUpdate();
			rs = ps.getGeneratedKeys();
			return rs.next() ? rs.getInt(1) : -1;
		} catch (SQLException e) {
			throw e;
		} finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(ps);
			sql = null;
			fields = null;
			objProps = null;
		}
	}

	public <T> List<T> query(Class<T> clazz, String sql,int offset,int pageSize,Object... params) throws SQLException {
		
		Connection conn = getConnection();
		PreparedStatement pstat = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try{
			if(params==null || params.length==0){
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				resultSet = stmt.executeQuery(sql);
			}else{
				pstat = conn.prepareStatement(sql);				
				for(int i=0;i<params.length;i++){
					pstat.setObject(i + 1, params[i]);
				}
				resultSet = pstat.executeQuery();
			}
			try {
				if(offset==0&&pageSize==0)
					return ResultSetUtils.getList(clazz, resultSet);
				else
					return ResultSetUtils.getList(clazz, resultSet, offset, pageSize);
			} catch (Exception e) {
				throw new SQLException();
			}
			
		}catch(SQLException e){
			throw e;
		}finally{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstat);
			DbUtils.closeQuietly(stmt);
			DBManager.closeConnection();
			pstat = null;
			stmt = null;
			resultSet = null;
		}		
	}
	
	public List<Map<String,Object>> query(String sql,int offset,int pageSize,Object... params) throws SQLException {
		
		Connection conn = getConnection();
		PreparedStatement pstat = null;
		Statement stmt = null;
		ResultSet resultSet = null;
		try{
			if(params==null || params.length==0){
				stmt = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
				resultSet = stmt.executeQuery(sql);
			}else{
				pstat = conn.prepareStatement(sql);
				
				for(int i=0;i<params.length;i++){
					pstat.setObject(i + 1, params[i]);
				}
				resultSet = pstat.executeQuery();
			}
			if(offset==0&&pageSize==0)
				return ResultSetUtils.getMaps(resultSet);
			else
				return ResultSetUtils.getMaps(resultSet, offset, pageSize);
			
		}catch(SQLException e){
			throw e;
		}finally{
			DbUtils.closeQuietly(resultSet);
			DbUtils.closeQuietly(pstat);
			DbUtils.closeQuietly(stmt);
			DBManager.closeConnection();
			pstat = null;
			stmt = null;
			resultSet = null;
		}		
	}

	public <T> List<T> query(Class<T> clazz, String sql, Object... params) throws SQLException {
		return this.query(clazz,sql, 0, 0, params);
	}

	public <T> List<T> query(Class<T> clazz, String sql) throws SQLException {
		return this.query(clazz, sql,0,0,(Object[]) null);
	}

	public List<Map<String, Object>> query(String sql,Object...params) throws SQLException {
		return this.query(sql, 0, 0, (Object[]) null);
	}
	
	public List<Map<String, Object>> query(String sql) throws SQLException {
		return this.query(sql, 0, 0, (Object[]) null);
	}

	public int queryCount(String sql,Object...params) throws SQLException {
		
		Connection conn = getConnection();
		Number num = null;
		try{
			num = (Number)queryRunner.query(conn, sql, new ScalarHandler(), params);
			return (num!=null)?num.intValue():-1;
		}catch(SQLException e){
			throw e;
		}finally{
			DBManager.closeConnection();
		}
	}
	
	public int queryCount(String sql) throws SQLException {
		return this.queryCount(sql, (Object[])null);
	}

	public int update(String sql, Object... params) throws SQLException {
		Connection conn = getConnection();
		int ret = 0;
		try{
			return queryRunner.update(conn, sql, params);
		}catch(SQLException e){
			throw e;
		}finally{
			DBManager.closeConnection();
		}	
	}

	public int update(String sql) throws SQLException {
		return this.update(sql, (Object[])null);
	}

	public int queryCount(BaseObject object) throws SQLException {
		return queryCount(object);
	}

	public int[] batch(String sql,Object[][] params) throws SQLException {
		Connection conn = getConnection();
		try{
			return queryRunner.batch(conn, sql, params);
		}catch(SQLException e){
			throw e;
		}finally{
			DBManager.closeConnection();
		}
	}
} 
