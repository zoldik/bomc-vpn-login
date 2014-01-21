package com.boco.bomc.vpn.dao;

import java.sql.SQLException;
import java.util.List;

import com.boco.bomc.vpn.db.bean.QueryCondition;
import com.boco.bomc.vpn.domain.MainUser;

public interface UserDao {
	
	public int updateMainUserApprove(boolean approve,String... loginname) throws SQLException;
	
	public List<MainUser> getMainUsers(int offset,int pageSize,QueryCondition condition,Object...params) throws SQLException;
	
	public int getMainUsersCount(QueryCondition condition,Object...params) throws SQLException;
	
	public MainUser findByLoginName(String loginName) throws SQLException;
}
