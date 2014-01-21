package com.boco.bomc.vpn.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
/**
 * ��¼�û�������Ϣ
 * 
 * @author sanghg
 * 
 */
public class UserContext extends BaseObject{
	
	private String username = null; //4A���ʺ�
	private String password = null; //���ʺ�����
	private String smspasswd = null; //��֤������
	
	private String computeName = null; //�ͻ��˼��������
	private String computeUserName = null; //�ͻ��˼�����û���
	private String ipAddr = null; //�ͻ���IP��ַ
	private String macAddr = null; //�ͻ���mac��ַ
	private String cpuSerial = null; //�ͻ���cpu���к�
	
	private boolean isAdmin = false;
	
	//������ʺ��¹�BOMC���˺���Ϣ
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
