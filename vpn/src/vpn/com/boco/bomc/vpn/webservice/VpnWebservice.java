package com.boco.bomc.vpn.webservice;

import java.net.URL;
import java.util.Map;

import javax.xml.namespace.QName;

import org.apache.axis.client.Call;
import org.apache.axis.client.Service;
import org.apache.log4j.Logger;

import com.asiainfo.uap.util.des.EncryptInterface;
import com.boco.bomc.vpn.VPNConfig;


public class VpnWebservice {
	
	private static final Logger logger = Logger.getLogger(VpnWebservice.class);

	/**
	 * 校验主帐号和密码信息,返回校验结果
	 */
	public String validateMainAcctService(String requestInfo) throws Exception{
		return this.CallServices(VPNConfig.getForuaWebservice(), "BomcMainAcctValidateService", "RequestInfo", requestInfo, "ResponseInfo");
	}

	/**
	 * Token认证接口
	 */
	public String checkAiuapTokenSoap(String requestInfo) throws Exception {
		return this.CallServices(VPNConfig.getForuaWebservice(), "CheckAiuapTokenSoap", "RequestInfo", requestInfo, "ResponseInfo");
	}
	/**
	 * 获取token串接口
	 */
	public String getAiuapToken(String requestInfo) throws Exception {
		return this.CallServices(VPNConfig.getForuaWebservice(), "CreateAiuapTokenSoap", "RequestInfo", requestInfo, "ResponseInfo");
	}
	/**
	 * 强认证接口,通过主帐号短信密钥进行认证
	 */
	public String mainAcctStrongAuthentication(String requestInfo) throws Exception {
		return this.CallServices(VPNConfig.getForuaWebservice(), "MainAcctStrongAuthenticationService", "RequestInfo", requestInfo, "ResponseInfo");
	}
	/**
	 * 获取短信密钥接口,通过主帐号获取登录短信密钥
	 */
	public String getSMKey(String requestInfo) throws Exception{
		return CallServices(VPNConfig.getForuaWebservice(), "GetSMKey", "RequestInfo", requestInfo, "ResponseInfo");
	}
	
	/**
	 * 动态调用webservice方法
	 */
	public String CallServices(String ServicesURL,String SerVicesName,String ReqName,String ReqValue,String RspName)
		throws Exception {
		try{
			
			if(logger.isDebugEnabled()){
				logger.debug("ServicesURL["+ServicesURL+"]\r\n"+
						"SerVicesName["+SerVicesName+"]\r\n"+
						"ReqName["+ReqName+"]\r\n"+
						"RspName["+RspName+"]\r\n"+
						"ReqValue["+ReqValue+"]");
			}
			
			Map output;
			Call call = (Call) new Service().createCall(); 
			
			String _ServicesURL = ServicesURL + "/" + SerVicesName;
			call.setTargetEndpointAddress(new URL(_ServicesURL)); 
			call.setOperationName(new QName(SerVicesName,SerVicesName));
			call.addParameter(ReqName,org.apache.axis.Constants.XSD_STRING,javax.xml.rpc.ParameterMode.IN);
			call.setReturnType(org.apache.axis.Constants.XSD_STRING);
			call.setEncodingStyle("UTF-8");        	
			
			Object responseWS_role = call.invoke(new Object[]{ReqValue}); 
			output = call.getOutputParams();
			String RspValue;
			try 
			{
				RspValue = (String) output.get(new QName("", RspName));
			} 
			catch (Exception _exception) 
			{
				RspValue = (String) org.apache.axis.utils.JavaUtils.convert(output.get(new QName("", RspName)), java.lang.String.class);
			}
			
		 	if(RspValue == null)
		 		RspValue = (String)responseWS_role;	
		 	
			return RspValue;
		
		}catch(Exception ex){
			logger.error("调用4A接口错误，请确认接口是否正常!", ex);			
			throw ex;
		}
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
	public static void main(String[] args) throws Exception {
		String serviceUrl = "http://10.87.30.152:7080/uac/services/BomcMainAcctValidateService?wsdl";
		String ReqValue = "<?xml version='1.0' encoding='UTF-8'?><REQUEST><HEAD><CODE></CODE><SID></SID><TIMESTAMP>20110915173222</TIMESTAMP><SERVICEID>HANGBOMC</SERVICEID><CLIENTIP>10.96.17.93</CLIENTIP><CLIENTMAC>00-FF-54-A2-C6-45</CLIENTMAC><CLIENTCPU></CLIENTCPU></HEAD><BODY><MAINACCT>4A</MAINACCT><LOGINPWD>16|-37|-13|20|-97|-100|58|50|42|22|-49|50|-1|-5|86|-64|89</LOGINPWD></BODY></REQUEST>"; 
		
		VpnWebservice vpn = new VpnWebservice();
		System.out.println(vpn.validateMainAcctService(ReqValue));
		
		//ReqValue="<?xml version='1.0' encoding='UTF-8'?><REQUEST><HEAD><CODE></CODE><SID></SID><TIMESTAMP>20130618140413</TIMESTAMP><SERVICEID>HANGBOMC</SERVICEID><CLIENTIP>10.87.18.57,10.43.127.120,</CLIENTIP><CLIENTMAC>00:19:D2:89:CD:28,74:E5:0B:E5:B8:16,</CLIENTMAC><CLIENTCPU>BFEBFBFF000206A7</CLIENTCPU></HEAD><BODY><MAINACCT>4A</MAINACCT></BODY></REQUEST>";
		//System.out.println(vpn.getSMKey(ReqValue)); 
		
		System.out.println(vpn.encryptPwd("5tgb%TGB"));
	}
	
}
