package com.boco.bomc.vpn.db.exception;

import java.sql.SQLException;

/**
 * 
 *
 */
public class DaoException extends Exception{

	private static final long serialVersionUID = 1L;

	public DaoException() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}

	public DaoException(String message) {
		super(message);
	}

	public DaoException(Throwable cause) {
		super(cause);
	}

}
