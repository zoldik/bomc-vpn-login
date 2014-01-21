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
	 * ƴװ��֤���ʺ�xml
	 * @param username  ���ʺ�����
	 * @param password  ���ʺ�����
	 * @return xml ��ʽ�ַ���
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
	 * ͨ�����ʺ�usernameƴװ��ȡ��¼������Կ
	 * @param username ���ʺ�
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
	 * ����ǿ��֤��Ҫ������Ϣ
	 * @param username ���ʺ�
	 * @param smspasswd ��������
	 * @param ipAddr IP��ַ
	 * @param macAddr mac��ַ
	 * @param cpuSerial cpu���к�
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
	 * ƴװ��ȡToken�����xml
	 * @param appacctid 4Aϵͳ��Ӧ�ô��ʺű�ʶ
	 * @param loginacct Ӧ�ô��ʺ�
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
	 * ������ȡToken���󷵻�xml
	 * 
	 * @param responseInfo
	 *            <?xml version='1.0' encoding='UTF-8'?> <USERRSP> <HEAD>
	 *            <CODE>��Ϣ��־���ݲ��Ԥ��</CODE> <SID>��Ϣ���кţ��ݲ��Ԥ��</SID>
	 *            <TIMESTAMP>ϵͳʱ�������ʽΪYYYYMMDDHHmmss</TIMESTAMP>
	 *            <SERVICEID>ҵ��ϵͳ��ʶ</SERVICEID> </HEAD> <BODY>
	 *            <RSP>�������,�ɹ�Ϊ0��ʧ��Ϊ1</RSP> <ERRDESC>��������</ERRDESC>
	 *            <TOKEN>token��</TOKEN> </BODY> </USERRSP>
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
			logger.error("����xml["+responseInfo+"]��Ϣ����", e);
		}
        return map;
	}
	
	/**
	 * �Ի�ȡ��Ϣ��Կ���ص���Ϣ���н���
	 * 
	 * @param responseInfo
	 *            <?xml version='1.0' encoding=��UTF-8��?> <RESPONSE> <HEAD>
	 *            <TIMESTAMP>YYYYMMDDHH24MMSS</TIMESTAMP> <SERVICEID>Ӧ�ñ�ʶ</SERVICEID>
	 *            </HEAD> <BODY> <RSP>���ؽ������</RSP> <ERRDESC>����ԭ��</ERRDESC>
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
			logger.error("����xml["+responseInfo+"]��Ϣ����", e);
		}
        return map;
	}
	
	/**
	 * ����4A���ʺ�ǿ��֤���ȡ������Ϣ
	 * 
	 * @param responseInfo
	 *            <?xml version='1.0' encoding=��UTF-8��?> <RESPONSE> <HEAD>
	 *            <TIMESTAMP>YYYYMMDDHH24MMSS</TIMESTAMP> <SERVICEID>Ӧ�ñ�ʶ</SERVICEID>
	 *            </HEAD> <BODY> <RSP>���ؽ������</RSP> <ERRDESC>����ԭ��</ERRDESC>
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
			logger.error("����xml["+responseInfo+"]��Ϣ����", e);
		}
        return map;
	}
	
	/**
	 * ��У�����ʺź����뷵�صĽ�����н��������ʺŽ����������ֽ����
	 * 
	 * 1�����ִ������ 
	 * 
	 * <?xml version='1.0' encoding='UTF-8'?> <RESPONSE> <HEAD>
	 * <CODE>��Ϣ��־</CODE> <SID>��Ϣ���к�</SID> <TIMESTAMP>ʱ���</TIMESTAMP>
	 * <SERVICEID>Ӧ�ñ�ʶ</SERVICEID> </HEAD> <BODY> <KEY>�ؼ���־</KEY>
	 * <ERRCODE>�������</ERRCODE> <ERRDESC>��������</ERRDESC> </BODY> </RESPONSE>
	 * 
	 * 2����֤ͨ�������ش��ж�ӦBOMC���˺���Ϣxml
	 * 
	 * <?xml version='1.0' encoding= ��UTF-8��?> <RESPONSE> <HEAD>
	 * <CODE>��Ϣ��־</CODE> <SID>��Ϣ���к�</SID> <TIMESTAMP>ʱ���</TIMESTAMP>
	 * <SERVICEID>Ӧ�ñ�ʶ</SERVICEID> </HEAD> <BODY> <MAINACCT>��ǰ��¼���ʺ�</MAINACCT>
	 * <MAINACCTID>��ǰ��¼���ʺ�id</MAINACCTID> <SUBACCTS> <SUBACCT>
	 * <APPACCTID>���˺����к�</APPACCTID> <NAME>���ʺ���</NAME> <LOCK>�Ƿ�����</LOCK>
	 * </SUBACCT> <SUBACCT> �������� </SUBACCT> </SUBACCTS> </BODY> </RESPONSE>
	 *  
	 * @param responseInfo
	 *            ��������xml
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
			logger.error("����xml["+responseInfo+"]��Ϣ����", e);
		}
        
		return map;
	}
	
	/**
	 * Token��֤�ӿ�,
	 * @param responseInfo
	 * @return
	 */
	public Map<String,Object> checkAiuapTokenSoap(String responseInfo){
		Map<String,Object> map = new HashMap<String,Object>();
		
		return map;
	}
	
	
	
	public static void main(String[] args) {
		
		String respInfo = "<?xml version='1.0' encoding='UTF-8'?><RESPONSE><HEAD><CODE>��Ϣ��־</CODE><SID>��Ϣ���к�</SID><TIMESTAMP>ʱ���</TIMESTAMP><SERVICEID>Ӧ�ñ�ʶ</SERVICEID></HEAD><BODY><KEY>�ؼ���־</KEY><ERRCODE>�������</ERRCODE><ERRDESC>��������</ERRDESC></BODY></RESPONSE>";
		respInfo = "<?xml version='1.0' encoding= 'UTF-8'?> <RESPONSE><HEAD><CODE>��Ϣ��־</CODE><SID>��Ϣ���к�</SID><TIMESTAMP>ʱ���</TIMESTAMP><SERVICEID>Ӧ�ñ�ʶ</SERVICEID></HEAD><BODY><MAINACCT>��ǰ��¼���ʺ�</MAINACCT><MAINACCTID>��ǰ��¼���ʺ�id</MAINACCTID><SUBACCTS><SUBACCT><APPACCTID>���˺����к�1</APPACCTID><NAME>���ʺ���1</NAME><LOCK>�Ƿ�����1</LOCK></SUBACCT><SUBACCT><APPACCTID>���˺����к�2</APPACCTID><NAME>���ʺ���2</NAME><LOCK>�Ƿ�����2</LOCK></SUBACCT><SUBACCT><APPACCTID>���˺����к�3</APPACCTID><NAME>���ʺ���3</NAME><LOCK>�Ƿ�����3</LOCK></SUBACCT></SUBACCTS></BODY></RESPONSE>";
		/*Map<String,Object> map = VpnXmlParser.parseMainAcctResponseInfo(respInfo);
		System.out.println(map.toString());*/
		System.out.println(VpnXmlParser.createMainAcctRequestInfo("", "","","",""));
	}
	
}
