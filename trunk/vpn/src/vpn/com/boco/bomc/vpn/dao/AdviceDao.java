package com.boco.bomc.vpn.dao;

import java.util.List;

import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.db.exception.DaoException;
import com.boco.bomc.vpn.domain.Advice;

public interface AdviceDao{
	
	public boolean save(Advice message) throws DaoException;
	
	public List<Advice> getAdvices(Page page) throws DaoException;
	
	public int getAdvicesCount() throws DaoException;
	
	public int delAdvice(Advice message) throws DaoException;
	
	public int update(Advice message) throws DaoException;
	
}