<%@ page language="java" import="java.util.*,com.boco.bomc.vpn.domain.*,com.boco.bomc.vpn.VPNConfig"
	pageEncoding="UTF-8"%>
	
<% 
	if(VPNConfig.getOpenManager()){
		UserContext user = (UserContext)request.getSession().getAttribute("SESSION_USER");
		if(user==null){
			response.sendRedirect("/vpn");
			return;
		}
			
		//if(!user.isAdmin()){
		//	response.sendRedirect("index.jsp");
		//	return;
		//}
	}else{
		response.sendRedirect("/vpn");
		return;
	}
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
	<head>
		<jsp:include page="../inc/header.jsp"/>
		<script language="javascript">
		var DEBUG = false;
			$(document).ready(function() {
				$(".left_menu ul li a").on("click",function(){
					$("#nav").html(this.innerHTML);
				});
				$("#baimingdan").on("click",function(){ajaxMainUsers(1,13);});
				$("#advices").on("click",function(){ajaxAdvices(1,13);});
			});
			
			
			function ajaxMainUsers(pageNum,pageSize){
					pageSize = 13;
					$.ajax({type:"post", cache:false, url:"/vpn/u/mainusers", data:"pageSize="+pageSize+"&pageNum="+pageNum, dataType:"json", success:function (data) {
						if (data.status == "1") {
							//alert(data.page.pageCount+"|"+pageNum	);
							$("#bmd-table").html("");
							var tbody = $("#bmd-table").append('<thead><tr><th>序号</th><th>帐号</th><th>生效时间</th><th>失效时间</th><th>允许状态</th><th>操作</th></tr></thead>');
							
							for(var i=0;i<data.result.length;i++){
								tbody.append('<tr><td>'+(i+1)+'</td><td>'+data.result[i].loginname+'</td><td>'+JSONDateFormat(data.result[i].efficttime)+'</td><td>'+JSONDateFormat(data.result[i].expiretime)+'</td><td id="'+data.result[i].loginname+'_state">'+covertState(data.result[i].approve)+'</td><td id="'+data.result[i].loginname+'_operate">'+covertOperate(data.result[i])+'</td></tr>');
							}
							
							$("#pagination1").html("");
							var pageNav = $("#pagination1");
							pageNav.append('<li class="footable-page-arrow"><a data-page="prev" href="javascript:ajaxMainUsers(1,'+13+');"><<</a></li>');
							pageNav.append('<li class="footable-page-arrow"><a data-page="prev" href="javascript:ajaxMainUsers('+(data.page.pageNum-1>0?(data.page.pageNum-1):1)+','+13+');"><</a></li>');
							for(var i=0;i<data.page.pageCount;i++){
								if(data.page.pageNum-i>6)continue;
								if(data.page.pageNum-i<-4)continue;
								if(data.page.pageNum==(i+1)){
									pageNav.append('<li class="footable-page footable-page-current"><a data-page="0" href="javascript:ajaxMainUsers('+(i+1)+','+13+');">'+(i+1)+'</a></li>');
								}else{
									pageNav.append('<li class="footable-page"><a data-page="0" href="javascript:ajaxMainUsers('+(i+1)+','+13+');">'+(i+1)+'</a></li>');
								}
							}
							pageNav.append('<li class="footable-page-arrow"><a data-page="next" href="javascript:ajaxMainUsers('+(data.page.pageNum+1)+','+13+');">></a></li>');
							pageNav.append('<li class="footable-page-arrow"><a data-page="next" href="javascript:ajaxMainUsers('+data.page.pageCount+','+13+');">>></a></li>');
							return;
						}else{
							if(DEBUG){
								alert(data.result);
							}
						}
					}, error:function (e) {
						alert("数据提交失败，代码:" + e.status + "请稍后再试");
					}});
			}
			
			function ajaxAdvices(pageNum,pageSize){
				pageSize = 13;
					$.ajax({type:"post", cache:false, url:"/vpn/advices", data:"pageSize="+pageSize+"&pageNum="+pageNum, dataType:"json", success:function (data) {
						if (data.status == "1") {
							//alert(data.page.pageCount+"|"+pageNum	);
							$("#bmd-table").html("");
							var tbody = $("#bmd-table").append('<thead><tr><th>序号</th><th>帐号</th><th>名称</th><th>留言时间</th><th>建议内容</th><th>操作</th></tr></thead>');
							for(var i=0;i<data.result.length;i++){
								tbody.append('<tr><td>'+(i+1)+'</td><td>'+data.result[i].loginname+'</td><td>'+data.result[i].name+'</td><td>'+JSONDateFormat(data.result[i].create_time)+'</td><td>'+data.result[i].advice+'</td><td><a href="javascritp:void(0);"> </a></td></tr>');
							}
							
							$("#pagination1").html("");
							var pageNav = $("#pagination1");
							pageNav.append('<li class="footable-page-arrow"><a data-page="prev" href="javascript:ajaxAdvices(1,'+13+');">«</a></li>');
							for(var i=0;i<data.page.pageCount;i++){
								if(data.page.pageNum-i>6)continue;
								if(data.page.pageNum-i<-4)continue;
								if(data.page.pageNum==(i+1)){
									pageNav.append('<li class="footable-page footable-page-current"><a data-page="0" href="javascript:ajaxAdvices('+(i+1)+','+13+');">'+(i+1)+'</a></li>');
								}else{
									pageNav.append('<li class="footable-page"><a data-page="0" href="javascript:ajaxAdvices('+(i+1)+','+13+');">'+(i+1)+'</a></li>');
								}
							}
							pageNav.append('<li class="footable-page-arrow"><a data-page="next" href="javascript:ajaxAdvices('+data.page.pageCount+','+13+');">»</a></li>');
							return;
						}else{
							if(DEBUG){
								alert(data.result);
							}
						}
					}, error:function (e) {
						alert("数据提交失败，代码:" + e.status + "请稍后再试");
					}});
			}
			
			/**
			* 时间对象的格式化
			*/
			Date.prototype.format = function(format) {
				/*
				* format="yyyy-MM-dd hh:mm:ss";
				*/
				var o = {
					"M+" : this.getMonth() + 1,
					"d+" : this.getDate(),
					"h+" : this.getHours(),
					"m+" : this.getMinutes(),
					"s+" : this.getSeconds(),
					"q+" : Math.floor((this.getMonth() + 3) / 3),
					"S" : this.getMilliseconds()
				}
				if (/(y+)/.test(format)) {
					format = format.replace(RegExp.$1,(this.getFullYear() + "").substr(4- RegExp.$1.length));
				}
				for (var k in o) {
					if (new RegExp("(" + k + ")").test(format)) {
						format = format.replace(RegExp.$1,RegExp.$1.length == 1?o[k]:("00" + o[k]).substr(("" + o[k]).length));
					}
				}
				return format;
			}
			function JSONDateFormat(obj){
				var date = new Date();
				date.setTime(obj.time);
				date.setHours(obj.hours);
				date.setMinutes(obj.minutes);
				date.setSeconds(obj.seconds);
				return date.format("yyyy-MM-dd hh:mm:ss");
			}
			
			function covertState(approve){
				if(approve=="1"){
					return "允许";
				}else{
					return "禁止";
				}
			}
			
			function covertOperate(obj){
				if(obj.approve=="1"){
					return '<a href=\'javascript:ajaxDisabled("'+obj.loginname+'");\'>禁止</a>'; 
				}else{
					return '<a href=\'javascript:ajaxEnabled("'+obj.loginname+'");\'>允许</a>'; 
				}
			}
			
			function ajaxDisabled(loginname){
				$.ajax({
					url:'/vpn/u/approve/'+loginname+'/0',
					dataType:'json',
					success:function(data){
						if(data.status=="1"){
							alert("禁用成功!");
							$('#'+loginname+'_state').html("禁止");
							$('#'+loginname+'_operate').html("<a style='color:red;' href=\"javascript:ajaxEnabled('"+loginname+"');\">允许</a>");
						}
					},
					error:function(data){
						if(data.status=="0"){
							alert(data.result);
						}
					}
				});
			}
			function ajaxEnabled(loginname){
				$.ajax({
					url:'/vpn/u/approve/'+loginname+'/1',
					dataType:'json',
					success:function(data){
						if(data.status=="1"){
							alert("启用成功!");
							$('#'+loginname+'_state').html("允许");
							$('#'+loginname+'_operate').html('<a style="color:red;" href="javascript:ajaxDisabled(\''+loginname+'\');">禁止</a>');
						}
					},
					error:function(data){
						if(data.status=="0"){
							alert(data.result);
						}
					}
				});
			}
    
		</script>
	</head>
	<body>
		<div id="bomc-container">
			<div id="header">
				<div class="current_user">&nbsp; 
					<label class="logout"><a href="logout" id="logout" title="点击退出VPN登录系统" >登出</a></label>
				</div>
			</div>
			<div class="container">
				<div class="left">
					<div class="login_tltle">
					管理操作
					</div>
					<div class="left_menu">
						<ul>
							<li><a href="javascript:void(0)" id="baimingdan">白名单管理</a></li>
							<li><a href="javascript:void(0)" id="advices">留言管理</a></li>
						</ul>
					</div>
				</div>
				<div class="right">
					<div class="nav_title">
						<span>>></span><span id="nav"></span>
					</div>
					<div class="cotent">
						<table id="bmd-table" class="footable" >
						

						</table>
						<ul id="pagination1" class="footable-nav" style=""></ul>
					</div>
				</div>
			</div>
			<div class="foot2">
				版权所有 中国移动通信集团河南有限公司 服务热线：15137111861
			</div>
		</div>
	</body>
</html>
