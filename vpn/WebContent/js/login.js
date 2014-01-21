var DEBUG = false;
$(document).ready(function () {
	var computerName;
	var userName;
	var ipAddr;
	var cpuSerial;
	var macAddr ="" ;
	try {
		var clock = new ActiveXObject("CLOCK.ClockCtrl.1");
		computerName = clock.GetCName();
		userName = clock.GetUName();
		ipAddr = clock.getIP();
		cpuSerial = clock.GetCPUID();
		cpuSerial = cpuSerial.substring(0, cpuSerial.length - 16);
		var locator = new ActiveXObject("WbemScripting.SWbemLocator");
		var service = locator.ConnectServer(".");
		properties = service.ExecQuery("Select * from Win32_NetworkAdapterConfiguration");
		var ips = ipAddr.split(",");
		for (var i = 0; i < ips.length; i++) {
			if (ips[i] != "") {
				var thisMac = "";
				var e = new Enumerator(properties);
				for (; !e.atEnd(); e.moveNext()) {
					var p = e.item();
					if (ips[i] == p.IPAddress(0)) {
						thisMac = p.MACAddress;
						break;
					}
				}
				macAddr += (thisMac);
				macAddr += ",";
			}
		}
		/*
		alert(computeName);
		alert(userName);
		alert(ipAddr);
		alert(macAddr);
		alert(cpuSerial);
		*/
		$("#computerName").attr("value", computerName);
		$("#userName").attr("value", userName);
		$("#ipAddr").attr("value", ipAddr);
		$("#macAddr").attr("value", macAddr);
		$("#cpuSerial").attr("value", cpuSerial);
	}catch (e) {
		//alert("请允许安装执行控件并将本地址加入可信任站点!");
	}
	
	$('#username').attr("title","请输入4A主帐号");
	$('#password').attr("title","请输入密码");
	
	$('#username').on("focus",function(){
		$('#username').poshytip({
			className: 'tip-yellowsimple',
			content:"请输入4A主帐号",
			showOn: 'focus',
			alignTo: 'target',
			alignX: 'right',
			alignY: 'center',
			offsetX: 5,
			showTimeout: 100
		});
	});
	$('#username').on("blur",function(){
		$('#username').poshytip("hide");
	})
	$('#username').focus();
	
	$('#password').on("focus",function(){
		$('#password').poshytip({
			className: 'tip-yellowsimple',
			content:"请输入密码",
			showOn: 'focus',
			alignTo: 'target',
			alignX: 'right',
			alignY: 'center',
			offsetX: 5,
			showTimeout: 100
		});
		$('#password').poshytip("hideDelayed",3000);
	});
	$('#password').on("blur",function(){
		$('#password').poshytip("hide");
	});
	
	$(document).keydown(function(){
        if(event.keyCode==13){
            login();
        }
    });
});
//获取短信码
var getSMSKey = function(){
	$("#smsimg").hide();
	var count=30;
	var acct = $("#username").val();
	if(acct==null){
		return;
	}
	$.ajax({type:"post", cache:false, url:"smskey/"+acct, dataType:"json", success:function (data) {
		if (data.status == "0") {
			$("#errortip").text(data.message);
			if(DEBUG){
				alert(data.result);
			}
		}
	}, error:function (e) {
		alert("数据提交失败，代码:" + e.status + "请稍后再试");
	}});
	$("#reSendSMKeyTimer").show();
	var timer = setInterval(function(){
		$("#reSendSMKeyButton").val(count);
		count--;
		if(count<0){
			clearInterval(timer);
			$("#reSendSMKeyTimer").hide();
			$("#smsimg").show();
		}
	},1000);
}

var smstextOnFocus = function(){
	$('#smspasswd').poshytip({
		className: 'tip-yellowsimple',
		content:"请输入短信码",
		onShow:"focus",
		alignTo: 'target',
		alignX: 'right',
		alignY: 'center',
		offsetX: 5,
		showTimeout: 100
	});
	$('#smspasswd').poshytip("show");
}

function login() {
	var username = $("#username").val();
	if ($.trim(username) == "") {
		$("#username").focus();
		return;
	}
	var password = $("#password").val();
	if ($.trim(password) == "") {
		$("#password").focus();
		return;
	}
	if($("#username").attr("disabled")){
		if($.trim($("#smspasswd").val())==""){
			$("#smsimg").poshytip('hide');
			$("#smspasswd").on("focus",smstextOnFocus);
			$("#smspasswd").focus();
			return;
		}
	}
	
	//alert($("#loginForm").serialize());
	
	var params = $("#loginForm").serialize();
	
	if($("#username").attr("disabled")){
		params+="&username="+$("#username").val()+"&password="+$("#password").val();
	}
	
	//alert(params);
	$.ajax({type:"post", cache:false, url:"login", dataType:"json", data:params, success:function (data) {
		if (data.status == "0") {
			$("#errortip").text(data.message);
			if(DEBUG){
				alert(data.result);
			}
		} else {
			if (data.status == "1") {
				if(DEBUG){
					alert(data.result);
				}
				$("#password").poshytip("hide");
				//显示短信码认证输入
				$("#username").attr("disabled",true);
				$("#password").attr("disabled",true);
				
				$("#smstr").html("<td height=\"40\" align=\"right\" class=\"font_014682\">短信密钥：</td>"+
							"<td width=\"193\">"+
							"	<div class=\"inputbox2\"><input name=\"smspasswd\" id=\"smspasswd\" type=\"text\" maxlength=\"6\" class=\"my_input2\" /></div>"+
							"</td>"+
							"<td width=\"40\" align=\"center\">"+
							"	<img src=\"images/mobile.gif\" id=\"smsimg\" style=\"cursor: hand;\" title=\"短信已经发送，如果长时间没收到短信，请点击此按钮重发短信\" width=\"14\" height=\"16\"/>"+
							"	<div id=\"reSendSMKeyTimer\" style=\"display:none;\">"+
							"		<input disabled=\"\" id=\"reSendSMKeyButton\" type=\"button\" value=\"30\"/>"+
							"	</div>"+
							"</td>");
							
				$("#smsimg").on("click",getSMSKey);			
				$("#smstr").show();
				$('#smsimg').poshytip({
					className: 'tip-yellowsimple',
					onShow:"none",
					alignTo: 'target',
					alignX: 'right',
					alignY: 'center',
					offsetX: 5,
					showTimeout: 100
				});
				$('#password').blur();
				$('#smsimg').poshytip("show");
				$("#smspasswd").focus();
			} else {
				if (data.status == "2") {
					if(DEBUG){
						alert(data.result);
					}
					window.location.href= "bomc_login.jsp";
				}
			}
		}
	}, error:function (e) {
		alert("\u63d0\u4ea4\u6570\u636e\u5931\u8d25\uff0c\u4ee3\u7801:" + e.status + "\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5");
	}});
}

/*清空内容，重置*/
function reset(){
	$("#username").val("");
	$("#password").val("");
	$("#smspasswd").val("");
	$("#smstr").hide();
	$("#smstr").html("");
	$("#username").removeAttr("disabled");
	$("#password").removeAttr("disabled");
	$("#smsimg").poshytip('hide')
	$("#username").focus();
}


/*
 * 使用ajax提交数据(POST)
 */
function ajax_post(url, params, callback) {
	$.ajax({type:"POST", cache:false, url:url, data:params, success:callback, error:function (e) {
		alert("\u63d0\u4ea4\u6570\u636e\u5931\u8d25\uff0c\u4ee3\u7801:" + e.status + "\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5");
	}});
}
/*
 * 使用ajax获取数据(GET)
 */
function ajax_get(url, callback) {
	$.ajax({type:"POST", cache:false, url:url, data:params, success:callback, error:function (e) {
		alert("\u63d0\u4ea4\u6570\u636e\u5931\u8d25\uff0c\u4ee3\u7801:" + e.status + "\uff0c\u8bf7\u7a0d\u5019\u518d\u8bd5");
	}});
}
