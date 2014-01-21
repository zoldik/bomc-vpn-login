package com.boco.bomc.vpn.service;

import java.util.List;

import org.apache.log4j.Logger;

import com.boco.bomc.vpn.dao.AdviceDao;
import com.boco.bomc.vpn.dao.AdviceDaoImpl;
import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.db.exception.DaoException;
import com.boco.bomc.vpn.domain.Advice;

public class AdviceServiceImpl extends BaseService implements AdviceService {

	private static Logger logger = Logger.getLogger(AdviceServiceImpl.class);

	private AdviceDao adviceDao = null;
	
	public AdviceServiceImpl(){
		this.adviceDao = new AdviceDaoImpl();
	}

	public boolean saveAdvice(Advice advice) {
		try {
			return adviceDao.save(advice);
		} catch (Exception e) {
			logger.error("±£¥ÊΩ®“È ß∞‹", e);
		}
		return false;
	}

	public Page getAdvices(Page page) throws ServiceException {
		List<Advice> advices = null;
		int count = 0;
		try {
			advices = adviceDao.getAdvices(page);
			count = adviceDao.getAdvicesCount();
			
			page.setObjects(advices);
			page = page.setRecordCount(count);
		} catch (DaoException e) {
			e.printStackTrace();
		}
		return page;
	}

}
