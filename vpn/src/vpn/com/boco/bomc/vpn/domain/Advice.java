package com.boco.bomc.vpn.domain;

import java.util.Date;

/**
 * 用户留言信息对象
 * 
 * @author sanghg
 *
 */
public class Advice extends BaseObject{
	
	private Long id;
	private Long parent_id;
	private String loginname;//4A主帐号
	private String userid;
	private String name;
	private String advice;
	private Date   create_time = null;
	
	public Advice(){} 
	
	//设定留言对应表对象支持对象操作时使用
	public String getTableName(){return "VPN_USER_ADVICE";}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParent_id() {
		return parent_id;
	}

	public void setParent_id(Long parent_id) {
		this.parent_id = parent_id;
	}

	public String getLoginname() {
		return loginname;
	}

	public void setLoginname(String loginname) {
		this.loginname = loginname;
	}

	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAdvice() {
		return advice;
	}

	public void setAdvice(String advice) {
		this.advice = advice;
	}

	public Date getCreate_time() {
		return create_time;
	}

	public void setCreate_time(Date create_time) {
		this.create_time = create_time;
	}
}