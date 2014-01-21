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
		
		//��ȡ��������
		String computerName = HttpUtils.getStringParameter("computerName");
		String userName = HttpUtils.getStringParameter("userName");
		String ipAddr = HttpUtils.getStringParameter("ipAddr");
		String macAddr = HttpUtils.getStringParameter("macAddr");
		String cpuSerial = HttpUtils.getStringParameter("cpuSerial");
		logger.info(">>>ClientInfo:computerName["+computerName+"],userName["+userName+"],ipAddr["+ipAddr+"],macAddr["+macAddr+"],cpuSerial["+cpuSerial+"]");
		
		//������У��
		if(!StringUtils.isEmpty(username)&&VPNConfig.getVpnApprove()){
			MainUserService service = new MainUserServiceImpl();
			try {
				MainUser mainUser = service.findByLoginName(username);
				if(mainUser==null||mainUser.getApprove()!=1){
					String json ="{\"status\":\"0\",\"message\":\"���ʺŲ��ڰ������У��������¼!\",\"result\":\"{}\"}";
					return new TextRenderer(json);				
				}
			} catch (ServiceException e) {
				logger.error("��ȡ��������Ա��Ϣʧ��", e);
				String json ="{\"status\":\"0\",\"message\":\"���ʺŰ�����У�����!\",\"result\":"+e.getMessage()+"}";
				return new TextRenderer(json);	
			}
		}
		
		//���ʺ�У��
		if(!StringUtils.isEmpty(password)&&!StringUtils.isEmpty(username)&&StringUtils.isEmpty(smspasswd)){
			
			String responseInfo = this.step1Login(username, password,ipAddr,macAddr,cpuSerial);
			
			if(responseInfo==null){
				String json ="{\"status\":\"0\",\"message\":\"����4A�ӿ���֤ʧ��!\",\"result\":\"{}\"}";
				return new TextRenderer(json);
			}
			
			Map<String,Object> acctMap = new HashMap<String,Object>();
			
			if(responseInfo!=null){
				acctMap = VpnXmlParser.parseMainAcctResponseInfo(responseInfo);
			}
			
			if(responseInfo!=null&&responseInfo.indexOf("<KEY>")!=-1){//��֤��ͨ��
				String json ="{\"status\":\"0\",\"message\":\""+acctMap.get("ERRDESC")+"\",\"result\":\""+acctMap.toString()+"\"}";
				return new TextRenderer(json);
			}
			
			if(responseInfo!=null&&responseInfo.indexOf("<KEY>")==-1){//���ʺź�������֤ͨ�������ش��˺��б�
				
				//���Ͷ���У���뵽�û�
				String smsRespInfo = this.getSMKey(username,ipAddr,macAddr,cpuSerial);
				//�������Ż�ȡ������Ϣ
				Map<String,Object> map = VpnXmlParser.parseSMSResponseInfo(smsRespInfo);
				if(!map.get("RSP").equals("0")){
					String json ="{\"status\":\"0\",\"message\":\""+map.get("ERRDESC")+"\",\"result\":\""+map.toString()+"\"}";
					return new TextRenderer(json);
				}
				
				List<Map<String,Object>> subAcctList = (List<Map<String,Object>>)acctMap.get("SUBACCTS");
				if(subAcctList==null||subAcctList.size()==0){
					String json ="{\"status\":\"0\",\"message\":\"�޶�ӦBOMC�ʺ�!\",\"result\":\""+acctMap.toString()+"\"}";
					return new TextRenderer(json);
				}
				
				//�����˺��б��ŵ�Request��
				HttpSession session = ActionContext.getActionContext().getHttpSession();
				session.setAttribute("acctMap", acctMap);
				String json ="{\"status\":\"1\",\"message\":\"���ʺ���֤ͨ��!\",\"result\":\""+acctMap.toString()+"\"}";
				
				return new TextRenderer(json);
			}
			
		}
		
		//�������ʺź�������֤
		if(!StringUtils.isEmpty(smspasswd)){
			//����ǿ��֤
			
			String respInfo = this.step2Login(username, smspasswd,ipAddr,macAddr,cpuSerial);
			Map<String,Object> retMap = VpnXmlParser.parseMainAcctStrongResponseInfo(respInfo);
			
			if((retMap!=null&&retMap.size()==0)||(retMap.get("RSP")!=null&&!retMap.get("RSP").equals("0"))){
				String json ="{\"status\":\"0\",\"message\":\"���������"+retMap.get("ERRDESC")+"\",\"result\":\""+retMap.toString()+"\"}";
				return new TextRenderer(json);
			}
			
			//�����û���Ϣ��SESSION��
			try{
				this.initSession(username, password, smspasswd);
			}catch(Exception e){
				String json ="{\"status\":\"0\",\"message\":\"ϵͳ��������ϵ����Ա\",\"result\":\""+e.getMessage()+"\"}";
				return new TextRenderer();
			}
			//String json ="{\"status\":\"2\",\"message\":\"������֤ͨ��!\",\"result\":\""+retMap.toString()+"\"}";
			String json ="{\"status\":\"2\",\"message\":\"������֤ͨ��!\",\"result\":\"\"}";
			return new TextRenderer(json);
			
		}
		
		return new TextRenderer("{\"status\":\"0\",\"message\":\"��֤��ͨ��!\",\"result\":\"{}\"}");
	}
	
	/*
	 * ��һ��������4A���ʺź�������֤
	 */
	private String step1Login(String username,String password,String ipAddr,String macAddr,String cupSerial){
		String requestInfo = VpnXmlParser.createMainAcctRequestInfo(username, password,ipAddr,macAddr,cupSerial);
		VpnWebservice vpnService = new VpnWebservice();
		try {
			return vpnService.validateMainAcctService(requestInfo);
		} catch (Exception e) {
			logger.error("�������ʺź�������֤ʧ��!",e);
		}
		return null;
	}
	
	/*
	 * �ڶ������������ʺ��˻�ǿ����
	 */
	private String step2Login(String username,String smspasswd,String ipAddr,String macAddr,String cupSerial){
		String requestInfo = VpnXmlParser.createMainAcctStrongRequestInfo(username, smspasswd,ipAddr,macAddr,cupSerial);
		VpnWebservice vpnService = new VpnWebservice();
		try {
			return vpnService.mainAcctStrongAuthentication(requestInfo);
		} catch (Exception e) {
			logger.error("���ж�����֤ʧ��!",e);
		}
		return null;
	}
	/*
	 * ��ȡ����У����
	 */
	private String getSMKey(String username,String ipAddr,String macAddr,String cupSerial){
		String requestInfo = VpnXmlParser.createGetSMKeyRequestInfo(username,ipAddr,macAddr,cupSerial);
		VpnWebservice vpnService = new VpnWebservice();
		try {
			return vpnService.getSMKey(requestInfo);
		} catch (Exception e) {
			logger.error("��ȡ����У����ʧ��!",e);
		}
		return null;
	}
	/**
	 * ��ʼ��SESSION�����û���Ϣ���õ�SESSION�С�
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
			throw new Exception("�޷���SESSION�л�ȡ�û���ϢacctMap!");
		List<Map<String,Object>> subAccts = (List<Map<String,Object>>)acctMap.get("SUBACCTS");
		if(subAccts==null)
			throw new Exception("�޷���ȡ�û���¼��BOMC���˺��б���ϢSUBACCTS!");
		for(Map<String,Object> map:subAccts){
			logger.info("���ʺ�["+username+"]�¹�BOMC���˺���Ϣ--start��");
			logger.info("���˺�[APPACCTID]��"+map.get("APPACCTID"));
			logger.info("���˺�[NAME]��"+map.get("NAME"));
			logger.info("���˺�[LOCK]��"+map.get("LOCK"));
			logger.info("���ʺ�["+username+"]�¹�BOMC���˺���Ϣ--end��");
		}
		currentUser.setSubAcct(subAccts);
		session.setAttribute("SESSION_USER", currentUser);
	}
	
	/**
	 * ���Ͷ�����֤��
	 * @param username ���ʺ���Ϣ
	 * @return
	 */
	
	@Mapping("/smskey/$1")
	public Renderer sendSMKey(String username){
		HttpSession session = ActionContext.getActionContext().getHttpSession();
		if(session.getAttribute("acctMap")==null){
			String json = "{\"status\":\"0\",\"message\":\"��ǰδ�������ʺ���֤�������ȡ������!\",\"result\":\"{��ǰδ�������ʺ���֤�������ȡ������}\"}";
			return new TextRenderer(json);
		}
		if(StringUtils.isEmpty(username)){
			String json = "{\"status\":\"0\",\"message\":\"���ʺ�Ϊ��!\",\"result\":\"{}\"}";
			return new TextRenderer(json);
		}
		
		//��ȡ��������
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
			json ="{\"status\":\"0\",\"message\":\"���ŵ���ʧ��!\",\"result\":\""+map.toString()+"\"}";
		}else{
			json ="{\"status\":\"1\",\"message\":\"���ŷ��ͳɹ�!\",\"result\":\""+map.toString()+"\"}";
		}
		return new TextRenderer(json);
	}
	
	/**
	 * ��ȡToken
	 * @param appacctid
	 * @param loginacct
	 * @return 
	 */
	@Mapping("/gettoken/$1/$2")
	public Renderer createAiuapTokenSoap(String appacctid,String loginacct){
		
		HttpSession session = ActionContext.getActionContext().getHttpSession();
		if(session.getAttribute("SESSION_USER")==null){
			String json = "{\"status\":\"0\",\"message\":\"��ǰ�û�δ��¼,����ʧ��!\",\"result\":\"{��ǰ�û�δ��¼,����ʧ��!}\"}";
			return new TextRenderer(json);
		}
		
		String json ="";
		String responseInfo = getAiuapTokenSoap(appacctid, loginacct);
		if(responseInfo==null){
			json ="{\"status\":\"0\",\"message\":\"��ȡTokenʧ��!\",\"result\":\"{}\"}";
			return new TextRenderer(json);
		}
		
		Map<String,Object> map = VpnXmlParser.parseTokenSoapResponseInfo(responseInfo);
		
		if(map.get("RSP")!=null&&!map.get("RSP").equals("0")){
			json ="{\"status\":\"0\",\"message\":\"��ȡTokenʧ��!\",\"result\":\""+map.toString()+"\"}";
		}else{
			json ="{\"status\":\"1\",\"message\":\"��ȡToken�ɹ�!\",\"result\":\""+map.toString()+"\"}";
		}
		
		return new TextRenderer(json);
	}
	
	/*
	 * ��ȡToken��Ϣ
	 */
	private String getAiuapTokenSoap(String appacctid,String loginacct) {
		String requestInfo = VpnXmlParser.createAiuapTokenSoap(appacctid, loginacct);
		VpnWebservice vpnService = new VpnWebservice();
		try {
			return vpnService.getAiuapToken(requestInfo);
		} catch (Exception e) {
			logger.error("��ȡTokenʧ��!",e);
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
	 * ����
	 * @param pwd
	 * @return
	 */
	private String encryptPwd(String pwd){
		return null!=pwd && !"".equals(pwd) ? EncryptInterface.desEncryptData(pwd) : pwd;
	}
	/**
	 * ����
	 * @param pwd
	 * @return
	 */
	private String unEncryptPwd(String pwd){
		return null!=pwd && !"".equals(pwd) ? EncryptInterface.desUnEncryptData(pwd) : pwd;
	}
}
