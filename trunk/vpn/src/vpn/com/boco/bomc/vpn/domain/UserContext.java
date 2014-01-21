package com.boco.bomc.vpn.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * 登录用户对象信息
 * 
 * @author sanghg
 * 
 */
public class UserContext extends BaseObject{
	
	private String username = null; //4A主帐号
	private String password = null; //主帐号密码
	private String smspasswd = null; //认证短信码
	
	private String computeName = null; //客户端计算机名称
	private String computeUserName = null; //客户端计算机用户名
	private String ipAddr = null; //客户端IP地址
	private String macAddr = null; //客户端mac地址
	private String cpuSerial = null; //客户端cpu序列号
	
	private boolean isAdmin = false;
	
	//存放主帐号下挂BOMC从账号信息
	private List<Map<String,Object>> subAcct = new ArrayList<Map<String,Object>>();

	public UserContext() {
	}
	
	public UserContext(String username, String password, String smspasswd) {
		super();
		this.username = username;
		this.password = password;
		this.smspasswd = smspasswd;
	}

	public UserContext(String username, String password, String smspasswd,
			List<Map<String, Object>> subAcct) {
		super();
		this.username = username;
		this.password = password;
		this.smspasswd = smspasswd;
		this.subAcct = subAcct;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSmspasswd() {
		return smspasswd;
	}

	public void setSmspasswd(String smspasswd) {
		this.smspasswd = smspasswd;
	}

	public String getComputeName() {
		return computeName;
	}

	public void setComputeName(String computeName) {
		this.computeName = computeName;
	}

	public String getComputeUserName() {
		return computeUserName;
	}

	public void setComputeUserName(String computeUserName) {
		this.computeUserName = computeUserName;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public String getMacAddr() {
		return macAddr;
	}

	public void setMacAddr(String macAddr) {
		this.macAddr = macAddr;
	}

	public String getCpuSerial() {
		return cpuSerial;
	}

	public void setCpuSerial(String cpuSerial) {
		this.cpuSerial = cpuSerial;
	}

	public List<Map<String, Object>> getSubAcct() {
		return subAcct;
	}

	public void setSubAcct(List<Map<String, Object>> subAcct) {
		this.subAcct = subAcct;
	}

	public boolean isAdmin() {
		return isAdmin;
	}

	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
}
