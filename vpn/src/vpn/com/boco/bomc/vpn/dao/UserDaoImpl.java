package com.boco.bomc.vpn.dao;

import java.sql.SQLException;
import java.util.List;

import com.boco.bomc.vpn.db.DaoSupport;
import com.boco.bomc.vpn.db.bean.QueryCondition;
import com.boco.bomc.vpn.domain.MainUser;

public class UserDaoImpl extends DaoSupport implements UserDao{
	
	public UserDaoImpl() {
		super();
	}

	public MainUser findByLoginName(String loginName) throws SQLException {
		String sql = "SELECT * FROM "+new MainUser().getTableName()+" WHERE loginname = ?";
		MainUser mainUser = null;
		List<MainUser> users = super.query(MainUser.class, sql,loginName);
		if(users.size()>0){
			return users.get(0);
		}else{
			return mainUser;
		}
	}

	public int updateMainUserApprove(boolean approve,String... loginName) throws SQLException {
		StringBuffer sql = new StringBuffer("");
		
		if(approve){
			sql.append("UPDATE "+new MainUser().getTableName()+" SET approve =1 ");
		}else{
			sql.append("UPDATE "+new MainUser().getTableName()+" SET approve =0 ");
		}
		
		if(loginName!=null&&loginName.length>1){
			sql.append(" WHERE loginname in (");
			for(int i=0;i<loginName.length;i++){
				if(i==0)
					sql.append("?");
				else
					sql.append(",?");
			}
			sql.append(");");
		}else if(loginName!=null&&loginName.length==1){
			sql.append(" WHERE loginname = '"+loginName[0]+"'");
		}
		return super.update(sql.toString());
	}

	public List<MainUser> getMainUsers(int offset,int pageSize,QueryCondition condition,Object...params) throws SQLException {
		String sql = "SELECT * FROM "+new MainUser().getTableName();
		if(condition!=null){
			sql = sql+condition.toSQLString();
		}
		List<MainUser> users = this.query(MainUser.class, sql, offset, pageSize, params);
		return users;
	}
	
	public int getMainUsersCount(QueryCondition condition,Object...params) throws SQLException {
		String sql = "SELECT count(*) FROM "+new MainUser().getTableName();
		if(condition!=null){
			sql = sql+condition.toSQLString();
		}
		return super.queryCount(sql, params);
	}
	
	public static void main(String[] args){
		UserDaoImpl userD = new UserDaoImpl();
		try {
			userD.findByLoginName("qinru");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
