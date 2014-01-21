<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%@ page language="java" import="java.util.*,com.boco.bomc.vpn.domain.*" pageEncoding="UTF-8"%>
<%
	
	UserContext user = (UserContext)request.getSession().getAttribute("SESSION_USER");
	if(user!=null){
		request.getRequestDispatcher("bomc_login.jsp").forward(request,response);
	}
	
%>
<html>
	<head>
		<jsp:include page="inc/header.jsp"/>
		<script type="text/javascript" src="${pageContext.request.contextPath}/js/login.js"></script>
	</head>
	<body class="body_login">
		
		<OBJECT id="catchPic" display="none"
			classid="clsid:42E04BF9-D3F2-4950-8E91-C5168918D63E"
			codebase="clock.cab#version=1,2,2,0" width=0 height=0
			align=center hspace=0 vspace=0>
		</OBJECT>

		<div id="login-container">
			<div id="login">
				<div class="formbox">
					<form id="loginForm">
					<input type="hidden" id="computerName" name="computerName" value=""/>
					<input type="hidden" id="userName" name="userName" value=""/>
					<input type="hidden" id="ipAddr" name="ipAddr" value=""/>
					<input type="hidden" id="macAddr" name="macAddr" value=""/>
					<input type="hidden" id="cpuSerial" name="cpuSerial" value=""/>
					<table width="318" border="0" cellspacing="0" cellpadding="0">
						<tr>
							<td width="100" height="40" align="right" class="font_014682">4A主账号：</td>
							<td colspan="2">
								<div class="inputbox">
									<input name="username" id="username" type="text" maxlength="36" class="my_input" placeholder="输入4A主帐号" autocomplete="off" x-webkit-speech x-webkit-grammar="builtin:translate" value=""/>
								</div>
							</td>
						</tr>
						<tr>
							<td height="40" align="right" class="font_014682">密 码：</td>
							<td colspan="2">
								<div class="inputbox">
									<input name="password" id="password" type="password" maxlength="20" placeholder="输入密码" title="提示：输入密码后，按回车键获取强认证列表" autocomplete="off" class="my_input" />
								</div>
							</td>
						</tr>
						<tr id="smstr" style="display:none;">
						</tr>
						<tr>
							<td height="60" colspan="3">
								<table width="318" border="0" cellspacing="0" cellpadding="0">
									<tr>
										<td width="185" align="right"><a href="javascript:login();" class="yh_btn">登录</a></td>
										<td>
											&nbsp;
										</td>
										<td width="150"><a href="javascript:reset();" class="yh_btn">重置</a></td>
									</tr>
								</table>
							</td>
						</tr>
						<tr><td colspan="3" align="center"><div id="errortip" style="color:red;font-size:12px;"></div></td></tr>
					</table>
				</form>
				</div>
				<!--formbox.end -->
				<p style="color: #7d7f80; font-size: 12px; margin-top: 30px; text-align: center;">
					版权所有 中国移动通信集团河南有限公司 服务热线：15137111861
				</p>
			</div>
			<!--load2box.end -->
		</div>
		<!--main1.end -->
	</body>
</html>
