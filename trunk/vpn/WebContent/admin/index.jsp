<%@ page language="java" import="java.util.*,com.boco.bomc.vpn.domain.*"
	pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
		<title>河南移动业务支撑网运营管理系统登陆--管理登录</title>
		<link rel="stylesheet" type="text/css" href="../style.css" />
		<link rel="stylesheet" href="../js/tip-yellowsimple/tip-yellowsimple.css" type="text/css" />
		<link rel="stylesheet" href="../js/tip-yellow/tip-yellow.css" type="text/css" />
		<script type="text/javascript" src="../js/pngfix.js"></script>
		<script type="text/javascript" src="../js/jquery-1.8.1.min.js"></script>
		<script type="text/javascript" src="../js/jquery.poshytip.min.js"></script>
		<script type="text/javascript" src="../js/jquery.blockUI.js"></script>
		<style type="text/css">
			body,button,input,select,textarea{
			    font:12px/1.5 tahoma,"microsoft yahei","\5FAE\8F6F\96C5\9ED1";
			}
			#current_user{
				float:right;
				height:33px;
				padding:35px;
				color:#fff;
				font-size:14px;
			}
			#current_user a,#current_user a:hover{color:#fff;text-decoration:none;}
			.username{padding-right:30px;}
			.textarea{width:250px;height:90px;padding:0px;margin:0px;border-radius: 5px;}
			
			#message .close_div{width:270px;}
			#message .close_div h4{width:90px;float:left;}
			#message .close_div a{float:right;text-decoration:none;color:#8c3901}
			#message .message_text textarea{font:12px/1.5 tahoma,"microsoft yahei","\5FAE\8F6F\96C5\9ED1";width:260px;heigth:96px;}
			#message .message_text textarea{margin-top:5px;text-align:left;padding:3px 5px;border:1px solid #F3DEB0;}
			#msg_btn{
				background:#F3DEB0;
				margin-left:100px;
				width:60px;height:25px;
				margin-top:5px;
				border:1px solid #ABA895;
				cousor:pointer;
			}
			#msg_btn:hover{color:#8c3901;}
			#info{width:110px;font-size:10px;color:#999999;right:0;padding-left:40px;}
		</style>
		<script language="javascript">

		</script>
	</head>
	<body>
		<div class="main2">
			<div class="top2_f">
				<div id="current_user">&nbsp; 
					
					<label class="logout"><a href="logout" id="logout" title="点击退出VPN登录系统" >登出</a></label>
				</div>
			</div>
			<div class="loginbox">
				<div class="login_tltle">
					管理登录
				</div>
				<div class="login_con">
					<div class="login_select1">
						应用账号：
						<input name="username" id="username" type="input" value="" placeholder="输入管理帐号" autocomplete="off"/>
					</div>
					<div class="login_select1" style="padding-top: 0px;">
						管理密码：
						<input name="password" id="password" type="input" value="" placeholder="输入管理密码" autocomplete="off"/>
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
