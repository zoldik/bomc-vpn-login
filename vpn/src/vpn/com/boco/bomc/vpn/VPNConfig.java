package com.boco.bomc.vpn;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;

public class VPNConfig {
	
	private static final Logger logger = Logger.getLogger(VPNConfig.class);
	
	private static String foruaWebservice = "";
	
	private static boolean vpnApprove = false;//默认不进行4A主帐号白名单验证
	
	private static String vpnProxyHttpUrl = "http://10.96.24.181:28080";
	
	private static boolean openManager = false;//默认不允许开启管理页面对留言，4A帐号登录限制管理
	
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
			logger.error("无法获取调用4A服务的地址", new Exception());
		}
		foruaWebservice = properties.getProperty("vpn.4a.webservice.url");
		if(foruaWebservice.equals("")){
			logger.error("无法获取调用4A服务的地址", new Exception());
		}
		logger.error(">>4A服务的地址vpn.4a.webservice.url="+foruaWebservice);
		
		//vpn.4a.user.approve
		if(properties.getProperty("vpn.4a.user.approve")==null){
			logger.error("没有配置vpn.4a.user.approve配置", new Exception());
		}
		vpnApprove = properties.getProperty("vpn.4a.user.approve").equals("true");
		if("".equals(vpnApprove))
			logger.error("无法获取4A服务认证地址，请确认vpn.properties文件中是否配置!");
		
		vpnProxyHttpUrl = properties.getProperty("vpn.proxy.http.url");
		if("".equals(vpnProxyHttpUrl)||null==vpnProxyHttpUrl){
			logger.error("无法获取VPN代理服务认证地址，请确认vpn.properties文件中是否配置!");
		}
		
		if(properties.getProperty("vpn.system.manager.open")==null){
			logger.error("没有配置vpn.system.manager.open配置", new Exception());
		}
		openManager = properties.getProperty("vpn.system.manager.open").equals("true");
		if("".equals(openManager)){
			logger.error("无法获取VPN管理服务是否开发配置，请确认vpn.properties文件中是否配置!");
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