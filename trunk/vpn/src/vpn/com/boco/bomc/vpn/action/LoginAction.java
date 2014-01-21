 package com.boco.bomc.vpn.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.expressme.webwind.ActionContext;
import org.expressme.webwind.HttpUtils;
import org.expressme.webwind.Mapping;
import org.expressme.webwind.renderer.Renderer;
import org.expressme.webwind.renderer.TextRenderer;

import com.asiainfo.uap.util.des.EncryptInterface;
import com.boco.bomc.vpn.VPNConfig;
import com.boco.bomc.vpn.domain.MainUser;
import com.boco.bomc.vpn.domain.UserContext;
import com.boco.bomc.vpn.service.MainUserService;
import com.boco.bomc.vpn.service.MainUserServiceImpl;
import com.boco.bomc.vpn.service.ServiceException;
import com.boco.bomc.vpn.util.VpnXmlParser;
import com.boco.bomc.vpn.webservice.VpnWebservice;
 
public class LoginAction extends BaseAction{
	
	private static final Logger logger = Logger.getLogger(LoginAction.class);
	
	@Mapping("/login")
	public Renderer login(){
		
		String username = HttpUtils.getStringParameter("username");
		String password =  this.encryptPwd(HttpUtils.getStringParameter("password"));
		String smspasswd =  HttpUtils.getStringParameter("smspasswd");
		
		//获取隐藏属性
		String computerName = HttpUtils.getStringParameter("computerName");
		String userName = HttpUtils.getStringParameter("userName");
		String ipAddr = HttpUtils.getStringParameter("ipAddr");
		String macAddr = HttpUtils.getStringParameter("macAddr");
		String cpuSerial = HttpUtils.getStringParameter("cpuSerial");
		logger.info(">>>ClientInfo:computerName["+computerName+"],userName["+userName+"],ipAddr["+ipAddr+"],macAddr["+macAddr+"],cpuSerial["+cpuSerial+"]");
		
		//白名单校验
		if(!StringUtils.isEmpty(username)&&VPNConfig.getVpnApprove()){
			MainUserService service = new MainUserServiceImpl();
			try {
				MainUser mainUser = service.findByLoginName(username);
				if(mainUser==null||mainUser.getApprove()!=1){
					String json ="{\"status\":\"0\",\"message\":\"主帐号不在白名单中，不允许登录!\",\"result\":\"{}\"}";
					return new TextRenderer(json);				
				}
			} catch (ServiceException e) {
				logger.error("获取白名单人员信息失败", e);
				String json ="{\"status\":\"0\",\"message\":\"主帐号白名单校验出错!\",\"result\":"+e.getMessage()+"}";
				return new TextRenderer(json);	
			}
		}
		
		//主帐号校验
		if(!StringUtils.isEmpty(password)&&!StringUtils.isEmpty(username)&&StringUtils.isEmpty(smspasswd)){
			
			String responseInfo = this.step1Login(username, password,ipAddr,macAddr,cpuSerial);
			
			if(responseInfo==null){
				String json ="{\"status\":\"0\",\"message\":\"调用4A接口验证失败!\",\"result\":\"{}\"}";
				return new TextRenderer(json);
			}
			
			Map<String,Object> acctMap = new HashMap<String,Object>();
			
			if(responseInfo!=null){
				acctMap = VpnXmlParser.parseMainAcctResponseInfo(responseInfo);
			}
			
			if(responseInfo!=null&&responseInfo.indexOf("<KEY>")!=-1){//验证不通过
				String json ="{\"status\":\"0\",\"message\":\""+acctMap.get("ERRDESC")+"\",\"result\":\""+acctMap.toString()+"\"}";
				return new TextRenderer(json);
			}
			
			if(responseInfo!=null&&responseInfo.indexOf("<KEY>")==-1){//主帐号和密码认证通过，返回从账号列表
				
				//发送短信校验码到用户
				String smsRespInfo = this.getSMKey(username,ipAddr,macAddr,cpuSerial);
				//解析短信获取返回信息
				Map<String,Object> map = VpnXmlParser.parseSMSResponseInfo(smsRespInfo);
				if(!map.get("RSP").equals("0")){
					String json ="{\"status\":\"0\",\"message\":\""+map.get("ERRDESC")+"\",\"result\":\""+map.toString()+"\"}";
					return new TextRenderer(json);
				}
				
				List<Map<String,Object>> subAcctList = (List<Map<String,Object>>)acctMap.get("SUBACCTS");
				if(subAcctList==null||subAcctList.size()==0){
					String json ="{\"status\":\"0\",\"message\":\"无对应BOMC帐号!\",\"result\":\""+acctMap.toString()+"\"}";
					return new TextRenderer(json);
				}
				
				//将从账号列表存放到Request中
				HttpSession session = ActionContext.getActionContext().getHttpSession();
				session.setAttribute("acctMap", acctMap);
				String json ="{\"status\":\"1\",\"message\":\"主帐号验证通过!\",\"result\":\""+acctMap.toString()+"\"}";
				
				return new TextRenderer(json);
			}
			
		}
		
		//进行主帐号和密码验证
		if(!StringUtils.isEmpty(smspasswd)){
			//进行强认证
			
			String respInfo = this.step2Login(username, smspasswd,ipAddr,macAddr,cpuSerial);
			Map<String,Object> retMap = VpnXmlParser.parseMainAcctStrongResponseInfo(respInfo);
			
			if((retMap!=null&&retMap.size()==0)||(retMap.get("RSP")!=null&&!retMap.get("RSP").equals("0"))){
				String json ="{\"status\":\"0\",\"message\":\"短信码错误，"+retMap.get("ERRDESC")+"\",\"result\":\""+retMap.toString()+"\"}";
				return new TextRenderer(json);
			}
			
			//保存用户信息到SESSION中
			try{
				this.initSession(username, password, smspasswd);
			}catch(Exception e){
				String json ="{\"status\":\"0\",\"message\":\"系统错误，请联系管理员\",\"result\":\""+e.getMessage()+"\"}";
				return new TextRenderer();
			}
			//String json ="{\"status\":\"2\",\"message\":\"短信认证通过!\",\"result\":\""+retMap.toString()+"\"}";
			String json ="{\"status\":\"2\",\"message\":\"短信认证通过!\",\"result\":\"\"}";
			return new TextRenderer(json);
			
		}
		
		return new TextRenderer("{\"status\":\"0\",\"message\":\"认证不通过!\",\"result\":\"{}\"}");
	}
	
	/*
	 * 第一步：进行4A主帐号和密码验证
	 */
	private String step1Login(String username,String password,String ipAddr,String macAddr,String cupSerial){
		String requestInfo = VpnXmlParser.createMainAcctRequestInfo(username, password,ipAddr,macAddr,cupSerial);
		VpnWebservice vpnService = new VpnWebservice();
		try {
			return vpnService.validateMainAcctService(requestInfo);
		} catch (Exception e) {
			logger.error("进行主帐号和密码认证失败!",e);
		}
		return null;
	}
	
	/*
	 * 第二部：进行主帐号账户强认真
	 */
	private String step2Login(String username,String smspasswd,String ipAddr,String macAddr,String cupSerial){
		String requestInfo = VpnXmlParser.createMainAcctStrongRequestInfo(username, smspasswd,ipAddr,macAddr,cupSerial);
		VpnWebservice vpnService = new VpnWebservice();
		try {
			return vpnService.mainAcctStrongAuthentication(requestInfo);
		} catch (Exception e) {
			logger.error("进行短信认证失败!",e);
		}
		return null;
	}
	/*
	 * 获取短信校验码
	 */
	private String getSMKey(String username,String ipAddr,String macAddr,String cupSerial){
		String requestInfo = VpnXmlParser.createGetSMKeyRequestInfo(username,ipAddr,macAddr,cupSerial);
		VpnWebservice vpnService = new VpnWebservice();
		try {
			return vpnService.getSMKey(requestInfo);
		} catch (Exception e) {
			logger.error("获取短信校验码失败!",e);
		}
		return null;
	}
	/**
	 * 初始化SESSION，将用户信息设置到SESSION中。
	 * @param username
	 * @param password
	 * @param smspasswd
	 * @throws Exception
	 */
	private void initSession(String username,String password,String smspasswd) throws Exception{
		UserContext currentUser = new UserContext(username,password,smspasswd);
		HttpSession session = ActionContext.getActionContext().getHttpServletRequest().getSession();
		Map<String,Object> acctMap = (Map<String,Object>)session.getAttribute("acctMap");
		if(acctMap==null)
			throw new Exception("无法从SESSION中获取用户信息acctMap!");
		List<Map<String,Object>> subAccts = (List<Map<String,Object>>)acctMap.get("SUBACCTS");
		if(subAccts==null)
			throw new Exception("无法获取用户登录后BOMC从账号列表信息SUBACCTS!");
		for(Map<String,Object> map:subAccts){
			logger.info("主帐号["+username+"]下挂BOMC从账号信息--start：");
			logger.info("从账号[APPACCTID]："+map.get("APPACCTID"));
			logger.info("从账号[NAME]："+map.get("NAME"));
			logger.info("从账号[LOCK]："+map.get("LOCK"));
			logger.info("主帐号["+username+"]下挂BOMC从账号信息--end：");
		}
		currentUser.setSubAcct(subAccts);
		session.setAttribute("SESSION_USER", currentUser);
	}
	
	/**
	 * 发送短信验证码
	 * @param username 主帐号信息
	 * @return
	 */
	
	@Mapping("/smskey/$1")
	public Renderer sendSMKey(String username){
		HttpSession session = ActionContext.getActionContext().getHttpSession();
		if(session.getAttribute("acctMap")==null){
			String json = "{\"status\":\"0\",\"message\":\"当前未进行主帐号认证不允许获取短信码!\",\"result\":\"{当前未进行主帐号认证不允许获取短信码}\"}";
			return new TextRenderer(json);
		}
		if(StringUtils.isEmpty(username)){
			String json = "{\"status\":\"0\",\"message\":\"主帐号为空!\",\"result\":\"{}\"}";
			return new TextRenderer(json);
		}
		
		//获取隐藏属性
		String computerName = HttpUtils.getStringParameter("computerName");
		String userName = HttpUtils.getStringParameter("userName");
		String ipAddr = HttpUtils.getStringParameter("ipAddr");
		String macAddr = HttpUtils.getStringParameter("macAddr");
		String cpuSerial = HttpUtils.getStringParameter("cpuSerial");
		logger.info(">>>ClientInfo:computerName["+computerName+"],userName["+userName+"],ipAddr["+ipAddr+"],macAddr["+macAddr+"],cpuSerial["+cpuSerial+"]");
		
		
		String responseInfo = this.getSMKey(username,ipAddr,macAddr,cpuSerial);
		Map<String,Object> map = VpnXmlParser.parseSMSResponseInfo(responseInfo);
		
		String json ="";
		if(map.get("RSP")!=null&&!map.get("RSP").equals("0")){
			json ="{\"status\":\"0\",\"message\":\"短信调用失败!\",\"result\":\""+map.toString()+"\"}";
		}else{
			json ="{\"status\":\"1\",\"message\":\"短信发送成功!\",\"result\":\""+map.toString()+"\"}";
		}
		return new TextRenderer(json);
	}
	
	/**
	 * 获取Token
	 * @param appacctid
	 * @param loginacct
	 * @return 
	 */
	@Mapping("/gettoken/$1/$2")
	public Renderer createAiuapTokenSoap(String appacctid,String loginacct){
		
		HttpSession session = ActionContext.getActionContext().getHttpSession();
		if(session.getAttribute("SESSION_USER")==null){
			String json = "{\"status\":\"0\",\"message\":\"当前用户未登录,操作失败!\",\"result\":\"{当前用户未登录,操作失败!}\"}";
			return new TextRenderer(json);
		}
		
		String json ="";
		String responseInfo = getAiuapTokenSoap(appacctid, loginacct);
		if(responseInfo==null){
			json ="{\"status\":\"0\",\"message\":\"获取Token失败!\",\"result\":\"{}\"}";
			return new TextRenderer(json);
		}
		
		Map<String,Object> map = VpnXmlParser.parseTokenSoapResponseInfo(responseInfo);
		
		if(map.get("RSP")!=null&&!map.get("RSP").equals("0")){
			json ="{\"status\":\"0\",\"message\":\"获取Token失败!\",\"result\":\""+map.toString()+"\"}";
		}else{
			json ="{\"status\":\"1\",\"message\":\"获取Token成功!\",\"result\":\""+map.toString()+"\"}";
		}
		
		return new TextRenderer(json);
	}
	
	/*
	 * 获取Token信息
	 */
	private String getAiuapTokenSoap(String appacctid,String loginacct) {
		String requestInfo = VpnXmlParser.createAiuapTokenSoap(appacctid, loginacct);
		VpnWebservice vpnService = new VpnWebservice();
		try {
			return vpnService.getAiuapToken(requestInfo);
		} catch (Exception e) {
			logger.error("获取Token失败!",e);
		}
		return null;
	}
	
	@Mapping("/logout")
	public String logout(){
		HttpSession session = ActionContext.getActionContext().getHttpSession();
		session.removeAttribute("SESSION_USER");
		
		return "redirect:";
	}
	
	/**
	 * 加密
	 * @param pwd
	 * @return
	 */
	private String encryptPwd(String pwd){
		return null!=pwd && !"".equals(pwd) ? EncryptInterface.desEncryptData(pwd) : pwd;
	}
	/**
	 * 解密
	 * @param pwd
	 * @return
	 */
	private String unEncryptPwd(String pwd){
		return null!=pwd && !"".equals(pwd) ? EncryptInterface.desUnEncryptData(pwd) : pwd;
	}
}
