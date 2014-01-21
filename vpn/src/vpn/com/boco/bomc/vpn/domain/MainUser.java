package com.boco.bomc.vpn.domain;

import java.util.Date;

//4A 主帐号信息白名单
public class MainUser extends BaseObject{
	private Long accountid;
	private String loginname;
	private String password;
	private int    state = 0;
	private Date   efficttime = null;
	private Date   expiretime = null;
	private int	   approve = 0;//是否允许登录，0不允许，1允许
	
	public MainUser(){}
	
	public String getTableName(){
		return "VPN_4A_USER";
	}

	public Long getAccountid() {
		return accountid;
	}

	public void setAccountid(Long accountid) {
		this.accountid = accountid;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getEfficttime() {
		return efficttime;
	}

	public void setEfficttime(Date efficttime) {
		this.efficttime = efficttime;
	}

	public Date getExpiretime() {
		return expiretime;
	}

	public void setExpiretime(Date expiretime) {
		this.expiretime = expiretime;
	}

	public int getApprove() {
		return approve;
	}

	public void setApprove(int approve) {
		this.approve = approve;
	}
	
}
