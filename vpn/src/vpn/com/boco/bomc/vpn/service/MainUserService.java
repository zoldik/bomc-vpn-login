package com.boco.bomc.vpn.service;

import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.domain.MainUser;

public interface MainUserService {
	
	public Page getMainUsers(Page page,String loginname) throws ServiceException;
	
	public int enableMainUser(String... loginname) throws ServiceException;
	
	public int disabledMainUser(String... loginname) throws ServiceException;
	
	public MainUser findByLoginName(String loginname) throws ServiceException;
		
}
