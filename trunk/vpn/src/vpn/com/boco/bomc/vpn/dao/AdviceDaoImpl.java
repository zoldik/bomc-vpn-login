package com.boco.bomc.vpn.dao;

import java.sql.SQLException;
import java.util.List;

import com.boco.bomc.vpn.db.DaoSupport;
import com.boco.bomc.vpn.db.bean.Page;
import com.boco.bomc.vpn.db.exception.DaoException;
import com.boco.bomc.vpn.domain.Advice;


public class AdviceDaoImpl extends DaoSupport implements AdviceDao {

	public int delAdvice(Advice advice) throws DaoException {
		int ret = 0;
		try {
			ret = super.save(advice);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public List<Advice> getAdvices(Page page) throws DaoException {
		List<Advice> advices = null;
		try {
			//advices = super.query(Advice.class, "SELECT * FROM "+new Advice().getTableName(), page.getOffset(),page.getPageSize());
			advices = super.query(Advice.class, "SELECT * FROM "+new Advice().getTableName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return advices;
	}
	
	public int getAdvicesCount() throws DaoException {
		int count = 0;
		try {
			count = super.queryCount("SELECT count(*) FROM "+new Advice().getTableName());
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return count;
	}

	public boolean save(Advice advice) throws DaoException {
		boolean ret = false;
		String sql = "INSERT INTO VPN_USER_ADVICE(ID,PARENT_ID,LOGINNAME,NAME,USERID,ADVICE,CREATE_TIME) VALUES(VPN_USER_ADVICE_SEQ.Nextval,?,?,?,?,?,sysdate)";
		try {
			return super.update(sql, new Object[] { advice.getParent_id(),
					advice.getLoginname(),
					advice.getName(), advice.getUserid(), advice.getAdvice()}) == 1;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return ret;
	}

	public int update(Advice advice) throws DaoException {
		return 0;
	}
	
}
