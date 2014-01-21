package com.boco.bomc.vpn.util;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class VpnXmlParser {
	
	private static final Logger logger =  Logger.getLogger(VpnXmlParser.class);
	
	private static final String SERVICEID = "HANGBOMC";

	/**
	 * 拼装验证主帐号xml
	 * @param username  主帐号名称
	 * @param password  主帐号密码
	 * @return xml 格式字符串
	 */
	public static String createMainAcctRequestInfo(String username,String password,String ipAddr,String macAddr,String cpuSerial){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<REQUEST>");
		sb.append("<HEAD>");
		sb.append("<CODE></CODE>");
		sb.append("<SID></SID>");
		sb.append("<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP>");
		sb.append("<SERVICEID>"+SERVICEID+"</SERVICEID>");
		sb.append("<CLIENTIP>"+ipAddr+"</CLIENTIP>");
		sb.append("<CLIENTMAC>"+macAddr+"</CLIENTMAC>");
		sb.append("<CLIENTCPU>"+cpuSerial+"</CLIENTCPU>");
		sb.append("</HEAD>");
		sb.append("<BODY>");
		sb.append("<MAINACCT>"+username+"</MAINACCT>");
		sb.append("<LOGINPWD>"+password+"</LOGINPWD>");
		sb.append("</BODY>");
		sb.append("</REQUEST>");
		return sb.toString();
	}
	/**
	 * 通过主帐号username拼装获取登录短信密钥
	 * @param username 主帐号
	 * @return ResponseInfo
	 */
	public static String createGetSMKeyRequestInfo(String username,String ipAddr,String macAddr,String cpuSerial){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<REQUEST>");
		sb.append("<HEAD>");
		sb.append("<CODE></CODE>");
		sb.append("<SID></SID>");
		sb.append("<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP>");
		sb.append("<SERVICEID>"+SERVICEID+"</SERVICEID>");
		sb.append("<CLIENTIP>"+ipAddr+"</CLIENTIP>");
		sb.append("<CLIENTMAC>"+macAddr+"</CLIENTMAC>");
		sb.append("<CLIENTCPU>"+cpuSerial+"</CLIENTCPU>");
		sb.append("</HEAD>");
		sb.append("<BODY>");
		sb.append("<MAINACCT>"+username+"</MAINACCT>");
		sb.append("</BODY>");
		sb.append("</REQUEST>");
		return sb.toString();
	}
	/**
	 * 创建强认证需要请求信息
	 * @param username 主帐号
	 * @param smspasswd 短信密码
	 * @param ipAddr IP地址
	 * @param macAddr mac地址
	 * @param cpuSerial cpu序列号
	 * @return xml
	 */
	public static String createMainAcctStrongRequestInfo(String username,String smspasswd,String ipAddr,String macAddr,String cpuSerial){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<REQUEST>");
		sb.append("<HEAD>");
		sb.append("<CODE></CODE>");
		sb.append("<SID></SID>");
		sb.append("<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP>");
		sb.append("<SERVICEID>"+SERVICEID+"</SERVICEID>");
		sb.append("<CLIENTIP>"+ipAddr+"</CLIENTIP>");
		sb.append("<CLIENTMAC>"+macAddr+"</CLIENTMAC>");
		sb.append("<CLIENTCPU>"+cpuSerial+"</CLIENTCPU>");
		sb.append("</HEAD>");
		sb.append("<BODY>");
		sb.append("<MAINACCT>"+username+"</MAINACCT>");
		sb.append("<TYPE>1</TYPE>");
		sb.append("<VALUE>"+smspasswd+"</VALUE>");
		sb.append("</BODY>");
		sb.append("</REQUEST>");
		return sb.toString();
	}
	
	/**
	 * 拼装获取Token请求的xml
	 * @param appacctid 4A系统中应用从帐号标识
	 * @param loginacct 应用从帐号
	 * @return xml
	 */
	public static String createAiuapTokenSoap(String appacctid,String loginacct){
		StringBuffer sb = new StringBuffer();
		sb.append("<?xml version='1.0' encoding='UTF-8'?>");
		sb.append("<REQUEST>");
		sb.append("<HEAD>");
		sb.append("<CODE></CODE>");
		sb.append("<SID></SID>");
		sb.append("<TIMESTAMP>"+new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())+"</TIMESTAMP>");
		sb.append("<SERVICEID>"+SERVICEID+"</SERVICEID>");
		sb.append("</HEAD>");
		sb.append("<BODY>");
		sb.append("<APPACCTID>"+appacctid+"</APPACCTID>");
		sb.append("<LOGINACCT>"+loginacct+"</LOGINACCT>");
		sb.append("</BODY>");
		sb.append("</REQUEST>");
		return sb.toString();
	}
	
	/**
	 * 解析获取Token请求返回xml
	 * 
	 * @param responseInfo
	 *            <?xml version='1.0' encoding='UTF-8'?> <USERRSP> <HEAD>
	 *            <CODE>消息标志，暂不填，预留</CODE> <SID>消息序列号，暂不填，预留</SID>
	 *            <TIMESTAMP>系统时间戳，格式为YYYYMMDDHHmmss</TIMESTAMP>
	 *            <SERVICEID>业务系统标识</SERVICEID> </HEAD> <BODY>
	 *            <RSP>结果代码,成功为0，失败为1</RSP> <ERRDESC>错误描述</ERRDESC>
	 *            <TOKEN>token串</TOKEN> </BODY> </USERRSP>
	 * 
	 * @return
	 */
	public static Map<String,Object> parseTokenSoapResponseInfo(String responseInfo){
		
		Map<String,Object> map = new HashMap<String,Object>(); 
		StringReader reader=new StringReader(responseInfo);
		SAXReader saxReader=new SAXReader();
		
		try {
			Document document = saxReader.read(reader);
			Element root = document.getRootElement();
			
			if(root!=null){
				Element head = root.element("HEAD");
				if(head!=null){
					Element code = head.element("CODE");
					if(code!=null){
						map.put("CODE", code.getTextTrim());
					}
					Element sid = head.element("SID");
					if(sid!=null){
						map.put("SID", sid.getTextTrim());
					}
					Element timeStamp = head.element("TIMESTAMP");
					if(timeStamp!=null){
						map.put("TIMESTAMP", timeStamp.getTextTrim());
					}
					Element serviceId = head.element("SERVICEID");
					if(serviceId!=null){
						map.put("SERVICEID", serviceId.getTextTrim());
					}
				}
				Element body = root.element("BODY");
				if(body!=null){
					Element rsp = body.element("RSP");
					if(rsp!=null){
						map.put("RSP", rsp.getTextTrim());
					}
					Element errdesc = body.element("ERRDESC");
					if(errdesc!=null){
						map.put("ERRDESC", errdesc.getTextTrim());
					}
					Element token = body.element("TOKEN");
					if(token!=null){
						map.put("TOKEN", token.getTextTrim());
					}
					Element appacctid = body.element("APPACCTID");
					if(appacctid!=null){
						map.put("APPACCTID", token.getTextTrim());
					}
				}
			}
			
		} catch (DocumentException e) {
			logger.error("解析xml["+responseInfo+"]信息错误！", e);
		}
        return map;
	}
	
	/**
	 * 对获取短息密钥返回的信息进行解析
	 * 
	 * @param responseInfo
	 *            <?xml version='1.0' encoding=’UTF-8’?> <RESPONSE> <HEAD>
	 *            <TIMESTAMP>YYYYMMDDHH24MMSS</TIMESTAMP> <SERVICEID>应用标识</SERVICEID>
	 *            </HEAD> <BODY> <RSP>返回结果代码</RSP> <ERRDESC>错误原因</ERRDESC>
	 *            </BODY> </RESPONSE>
	 * 
	 * @return map
	 */
	public static Map<String,Object> parseSMSResponseInfo(String responseInfo){
		
		Map<String,Object> map = new HashMap<String,Object>(); 
		
		StringReader reader=new StringReader(responseInfo);
		SAXReader saxReader=new SAXReader();
		
		try {
			Document document = saxReader.read(reader);
			Element root = document.getRootElement();
			
			if(root!=null){
				Element head = root.element("HEAD");
				if(head!=null){
					Element timeStamp = head.element("TIMESTAMP");
					if(timeStamp!=null){
						map.put("TIMESTAMP", timeStamp.getTextTrim());
					}
					Element serviceId = head.element("SERVICEID");
					if(serviceId!=null){
						map.put("SERVICEID", serviceId.getTextTrim());
					}
				}
				Element body = root.element("BODY");
				if(body!=null){
					Element rsp = body.element("RSP");
					if(rsp!=null){
						map.put("RSP", rsp.getTextTrim());
					}
					Element errdesc = body.element("ERRDESC");
					if(errdesc!=null){
						map.put("ERRDESC", errdesc.getTextTrim());
					}			
				}
			}
			
		} catch (DocumentException e) {
			logger.error("解析xml["+responseInfo+"]信息错误！", e);
		}
        return map;
	}
	
	/**
	 * 解析4A主帐号强认证后获取返回信息
	 * 
	 * @param responseInfo
	 *            <?xml version='1.0' encoding=’UTF-8’?> <RESPONSE> <HEAD>
	 *            <TIMESTAMP>YYYYMMDDHH24MMSS</TIMESTAMP> <SERVICEID>应用标识</SERVICEID>
	 *            </HEAD> <BODY> <RSP>返回结果代码</RSP> <ERRDESC>错误原因</ERRDESC>
	 *            </BODY> </RESPONSE>
	 * 
	 * @return map
	 */
	public static Map<String,Object> parseMainAcctStrongResponseInfo(String responseInfo){
		
		Map<String,Object> map = new HashMap<String,Object>(); 
		
		StringReader reader=new StringReader(responseInfo);
		SAXReader saxReader=new SAXReader();
		
		try {
			Document document = saxReader.read(reader);
			Element root = document.getRootElement();
			
			if(root!=null){
				Element head = root.element("HEAD");
				if(head!=null){
					Element timeStamp = head.element("TIMESTAMP");
					if(timeStamp!=null){
						map.put("TIMESTAMP", timeStamp.getTextTrim());
					}
					Element serviceId = head.element("SERVICEID");
					if(serviceId!=null){
						map.put("SERVICEID", serviceId.getTextTrim());
					}
				}
				Element body = root.element("BODY");
				if(body!=null){
					Element rsp = body.element("RSP");
					if(rsp!=null){
						map.put("RSP", rsp.getTextTrim());
					}
					Element errdesc = body.element("ERRDESC");
					if(errdesc!=null){
						map.put("ERRDESC", errdesc.getTextTrim());
					}			
				}
			}
			
		} catch (DocumentException e) {
			logger.error("解析xml["+responseInfo+"]信息错误！", e);
		}
        return map;
	}
	
	/**
	 * 对校验主帐号和密码返回的结果进行解析，主帐号解析返回两种结果：
	 * 
	 * 1、出现错误情况 
	 * 
	 * <?xml version='1.0' encoding='UTF-8'?> <RESPONSE> <HEAD>
	 * <CODE>消息标志</CODE> <SID>消息序列号</SID> <TIMESTAMP>时间戳</TIMESTAMP>
	 * <SERVICEID>应用标识</SERVICEID> </HEAD> <BODY> <KEY>关键标志</KEY>
	 * <ERRCODE>错误代码</ERRCODE> <ERRDESC>错误描述</ERRDESC> </BODY> </RESPONSE>
	 * 
	 * 2、认证通过，返回带有对应BOMC从账号信息xml
	 * 
	 * <?xml version='1.0' encoding= ‘UTF-8’?> <RESPONSE> <HEAD>
	 * <CODE>消息标志</CODE> <SID>消息序列号</SID> <TIMESTAMP>时间戳</TIMESTAMP>
	 * <SERVICEID>应用标识</SERVICEID> </HEAD> <BODY> <MAINACCT>当前登录主帐号</MAINACCT>
	 * <MAINACCTID>当前登录主帐号id</MAINACCTID> <SUBACCTS> <SUBACCT>
	 * <APPACCTID>从账号序列号</APPACCTID> <NAME>从帐号名</NAME> <LOCK>是否锁定</LOCK>
	 * </SUBACCT> <SUBACCT> ………… </SUBACCT> </SUBACCTS> </BODY> </RESPONSE>
	 *  
	 * @param responseInfo
	 *            待解析的xml
	 * @return
	 * @throws Exception 
	 */
	public static Map<String,Object> parseMainAcctResponseInfo(String responseInfo){
		
		Map<String,Object> map = new HashMap<String,Object>(); 
		
		StringReader reader=new StringReader(responseInfo);
		SAXReader saxReader=new SAXReader();
		
		try {
			Document document = saxReader.read(reader);
			Element root = document.getRootElement();
			
			if(root!=null){
				Element head = root.element("HEAD");
				if(head!=null){
					Element timeStamp = head.element("TIMESTAMP");
					if(timeStamp!=null){
						map.put("TIMESTAMP", timeStamp.getTextTrim());
					}
					Element serviceId = head.element("SERVICEID");
					if(serviceId!=null){
						map.put("SERVICEID", serviceId.getTextTrim());
					}
				}
				Element body = root.element("BODY");
				if(body!=null){
					Element key = body.element("KEY");
					if(key!=null){
						map.put("KEY", key.getTextTrim());
					}
					Element errcode = body.element("ERRCODE");
					if(errcode!=null){
						map.put("ERRCODE", errcode.getTextTrim());
					}
					Element errdesc = body.element("ERRDESC");
					if(errdesc!=null){
						map.put("ERRDESC", errdesc.getTextTrim());
					}
					
					Element mainacct = body.element("MAINACCT");
					if(mainacct!=null){
						map.put("MAINACCT", mainacct.getTextTrim());
					}
					Element mainacctid = body.element("MAINACCTID");
					if(mainacctid!=null){
						map.put("MAINACCTID", mainacctid.getTextTrim());
					}
					
					
					Element subaccts = body.element("SUBACCTS");
					List<Map<String,Object>> subsList = new ArrayList<Map<String,Object>>(); 
					if(subaccts!=null){
						List<Element> subs = subaccts.elements("SUBACCT");
						for(Element e : subs){
							Map<String,Object> subMap = new HashMap<String,Object>();
							Element appacctid = e.element("APPACCTID");
							if(appacctid!=null){
								subMap.put("APPACCTID", appacctid.getTextTrim());
							}
							Element name = e.element("NAME");
							if(name!=null){
								subMap.put("NAME", name.getTextTrim());
							}
							Element lock = e.element("LOCK");
							if(lock!=null){
								subMap.put("LOCK", lock.getTextTrim());
							}
							subsList.add(subMap);
							
							if(logger.isDebugEnabled()){
								logger.debug("SUBACCT:"+subMap.toString());
							}
						}
						map.put("SUBACCTS", subsList);
					}
					
				}
			}
			
		} catch (DocumentException e) {
			logger.error("解析xml["+responseInfo+"]信息错误！", e);
		}
        
		return map;
	}
	
	/**
	 * Token认证接口,
	 * @param responseInfo
	 * @return
	 */
	public Map<String,Object> checkAiuapTokenSoap(String responseInfo){
		Map<String,Object> map = new HashMap<String,Object>();
		
		return map;
	}
	
	
	
	public static void main(String[] args) {
		
		String respInfo = "<?xml version='1.0' encoding='UTF-8'?><RESPONSE><HEAD><CODE>消息标志</CODE><SID>消息序列号</SID><TIMESTAMP>时间戳</TIMESTAMP><SERVICEID>应用标识</SERVICEID></HEAD><BODY><KEY>关键标志</KEY><ERRCODE>错误代码</ERRCODE><ERRDESC>错误描述</ERRDESC></BODY></RESPONSE>";
		respInfo = "<?xml version='1.0' encoding= 'UTF-8'?> <RESPONSE><HEAD><CODE>消息标志</CODE><SID>消息序列号</SID><TIMESTAMP>时间戳</TIMESTAMP><SERVICEID>应用标识</SERVICEID></HEAD><BODY><MAINACCT>当前登录主帐号</MAINACCT><MAINACCTID>当前登录主帐号id</MAINACCTID><SUBACCTS><SUBACCT><APPACCTID>从账号序列号1</APPACCTID><NAME>从帐号名1</NAME><LOCK>是否锁定1</LOCK></SUBACCT><SUBACCT><APPACCTID>从账号序列号2</APPACCTID><NAME>从帐号名2</NAME><LOCK>是否锁定2</LOCK></SUBACCT><SUBACCT><APPACCTID>从账号序列号3</APPACCTID><NAME>从帐号名3</NAME><LOCK>是否锁定3</LOCK></SUBACCT></SUBACCTS></BODY></RESPONSE>";
		/*Map<String,Object> map = VpnXmlParser.parseMainAcctResponseInfo(respInfo);
		System.out.println(map.toString());*/
		System.out.println(VpnXmlParser.createMainAcctRequestInfo("", "","","",""));
	}
	
}
