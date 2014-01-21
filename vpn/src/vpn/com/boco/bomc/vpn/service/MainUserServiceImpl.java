package com.boco.bomc.vpn.service;

import java.sql.SQLException;
import java.util.List;

import com.boco.bomc.vpn.dao.UserDao;
import com.boco.bomc.vpn.dao.UserDaoImpl;
import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.db.bean.QueryCondition;
import com.boco.bomc.vpn.domain.MainUser;

public class MainUserServiceImpl extends BaseService implements MainUserService {

	private UserDao userDao = new UserDaoImpl();

	public int disabledMainUser(String... loginname) throws ServiceException {
		try {
			return userDao.updateMainUserApprove(false,loginname);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public int enableMainUser(String... loginname) throws ServiceException {
		try {
			return userDao.updateMainUserApprove(true,loginname);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	public Page getMainUsers(Page page,String loginame) throws ServiceException {
		QueryCondition condition = new QueryCondition();
		if(loginame!=null){
			condition.where().and("loginname=?");
		}
		try {
			List<MainUser> users = null;
			int count =0 ;
			if(loginame==null){
				users = userDao.getMainUsers(page.getOffset(), page.getPageSize(), condition, null);
				count = userDao.getMainUsersCount(condition, null);
			}else{
				users = userDao.getMainUsers(page.getOffset(), page.getPageSize(), condition, loginame);
				count = userDao.getMainUsersCount(condition, loginame);
			}
			page.setObjects(users);
			page = page.setRecordCount(count);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return page;
	}
	
	public MainUser findByLoginName(String loginname) throws ServiceException{
		try {
			return userDao.findByLoginName(loginname);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	
}
