package com.boco.bomc.vpn.service;

import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.domain.Advice;

public interface AdviceService {
	public boolean saveAdvice(Advice advice);
	
	public Page getAdvices(Page page) throws ServiceException;
}