
<%@ page language="java" import="java.util.*,com.boco.bomc.vpn.domain.*,com.boco.bomc.vpn.VPNConfig"
	pageEncoding="UTF-8"%>
<%
	UserContext user = (UserContext)request.getSession().getAttribute("SESSION_USER");

	if(user==null){
		response.sendRedirect("");
		return;
	}
	
	List<Map<String,Object>> subAccts = user.getSubAcct();
%>	
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
	<script>
	var VPN_PROXY_URL = '<%=VPNConfig.getVpnProxyHttpUrl()%>';
	</script>
	<head>
		<jsp:include page="inc/header.jsp"/>
		<script type="text/javascript" src="js/sublogin.js"></script>
	</head>
	<body class="body_bomc_login">
		<div id="bomc-container">
			<div id="header">
				<div class="current_user">
					<label class="username">当前用户：<%=user.getUsername()%></label>
					<label class="logout"><a href="logout" id="logout" title="点击退出VPN登录系统" >登出</a></label>
				</div>
			</div>
			<div class="loginbox">
				<div class="login_tltle">
					BOMC系统
				</div>
				<div class="login_con">
					<div class="login_select1">
						应用账号：
						<select id="appacctid" name="appacctid" class="select_style">
							<%
							for(Map<String,Object> map:subAccts){ 
								out.println("<option id='"+map.get("APPACCTID")+"' value='"+map.get("APPACCTID")+"'>"+map.get("NAME")+"</option>");
							}
							%>
						</select>
					</div>
					<div class="login_select1">
						登陆地址：
						<select name="" class="select_style">
							<option value="">
								登陆地址
							</option>
						</select>
					</div>
					<a id="log_btn" href="javascript:void(0)" class="log_btn" >登陆</a>
				</div>
			</div>
			<div class="foot2">
				版权所有 中国移动通信集团河南有限公司 服务热线：15137111861
			</div>
		</div>
	</body>
</html>
