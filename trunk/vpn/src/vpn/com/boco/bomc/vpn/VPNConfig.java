package com.boco.bomc.vpn;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class VPNConfig {
	
	private static final Logger logger = Logger.getLogger(VPNConfig.class);
	
	private static String foruaWebservice = "";
	
	private static boolean vpnApprove = false;//Ĭ�ϲ�����4A���ʺŰ�������֤
	
	private static String vpnProxyHttpUrl = "http://10.96.24.181:28080";
	
	private static boolean openManager = false;//Ĭ�ϲ�����������ҳ������ԣ�4A�ʺŵ�¼���ƹ���
	
	static{
		init();
	}
	
	static void init(){
		Properties properties = null;
		try {
			
			properties = new Properties();
			properties.load(VPNConfig.class.getResourceAsStream("/vpn.properties"));
			
		} catch (IOException e) {
			logger.error("can`t find vpn.properties",e);
		}
		//vpn.4a.webservice.url
		if(properties.getProperty("vpn.4a.webservice.url")==null){
			logger.error("�޷���ȡ����4A����ĵ�ַ", new Exception());
		}
		foruaWebservice = properties.getProperty("vpn.4a.webservice.url");
		if(foruaWebservice.equals("")){
			logger.error("�޷���ȡ����4A����ĵ�ַ", new Exception());
		}
		logger.error(">>4A����ĵ�ַvpn.4a.webservice.url="+foruaWebservice);
		
		//vpn.4a.user.approve
		if(properties.getProperty("vpn.4a.user.approve")==null){
			logger.error("û������vpn.4a.user.approve����", new Exception());
		}
		vpnApprove = properties.getProperty("vpn.4a.user.approve").equals("true");
		if("".equals(vpnApprove))
			logger.error("�޷���ȡ4A������֤��ַ����ȷ��vpn.properties�ļ����Ƿ�����!");
		
		vpnProxyHttpUrl = properties.getProperty("vpn.proxy.http.url");
		if("".equals(vpnProxyHttpUrl)||null==vpnProxyHttpUrl){
			logger.error("�޷���ȡVPN���������֤��ַ����ȷ��vpn.properties�ļ����Ƿ�����!");
		}
		
		if(properties.getProperty("vpn.system.manager.open")==null){
			logger.error("û������vpn.system.manager.open����", new Exception());
		}
		openManager = properties.getProperty("vpn.system.manager.open").equals("true");
		if("".equals(openManager)){
			logger.error("�޷���ȡVPN��������Ƿ񿪷����ã���ȷ��vpn.properties�ļ����Ƿ�����!");
		}
		
		if(logger.isDebugEnabled()){
			logger.debug("foruaWebservice["+foruaWebservice+"]");
			logger.debug("vpnApprove["+vpnApprove+"]");
		}
	}
	
	public static String getForuaWebservice(){
		return foruaWebservice;
	}
	
	public static boolean getVpnApprove(){
		return vpnApprove;
	}
	
	public static String getVpnProxyHttpUrl(){
		return vpnProxyHttpUrl;
	}
	
	public static boolean getOpenManager(){
		return openManager;
	}

	public static void main(String[] args){
		System.out.println(VPNConfig.foruaWebservice);
	}
}