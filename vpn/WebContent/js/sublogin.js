		var DEBUG=false;
		$(document).ready(function () {
			$('.loginbox').poshytip({
				className: 'tip-yellow',
				content:"欢迎登录! 如果您对VPN登录系统有改进的建议，请<a href='javascript:showDialog();' id='liuyan' style='color:'>留言</a>",
				showOn: 'none',
				alignTo: 'target',
				alignX: 'center',
				offsetX: 5,
				showTimeout: 100
			});
			$('.loginbox').poshytip("showDelayed",500);
			
			$(".log_btn").on("click",function(){
				var APPACCTID = $("#appacctid").val();
				var LOGINACCT = $("#"+APPACCTID).html();
				$.ajax({type:"post", cache:false, url:"gettoken/"+APPACCTID+"/"+LOGINACCT, dataType:"json", success:function (data) {
					if (data.status == "1") {
						$("#errortip").text(data.message);
						var token = null;
						if(data.result.length>2){
							var str = data.result.substr(1,data.result.length-2);
							var arr = str.split(",");
							for(var i=0;i<arr.length;i++){
								var valArr = arr[i].split("=");
								if(valArr[0].indexOf("TOKEN")!=-1){
									token = valArr[1];
									break;
								}
							}
						}
						if(DEBUG){
							alert(VPN_PROXY_URL+"/bomcbp?appAcctId="+APPACCTID+"&token="+token+"&vpn=true&vpncontrol=true&flag=1");
						}
						window.open(VPN_PROXY_URL+"/bomcbp?appAcctId="+APPACCTID+"&token="+token+"&vpn=true&vpncontrol=true&flag=1");
						return;
					}else{
						if(DEBUG){
							alert(data.result);
						}
					}
				}, error:function (e) {
					alert("数据提交失败，代码:" + e.status + "请稍后再试");
				}});
			});
		});
		var show_message = false;
		function showDialog(){
			if(show_message)
				return;
			$('#liuyan').poshytip({
					className: 'tip-yellow',
					content:'<div id="message">'+
							'	<form id="advice_form">'+
							'	<div class="close_div"><h4>在下面输入：</h4><a id="close" href="javascript:void(0)" onclick=\'javascript:$("#liuyan").poshytip("hide");show_message = false;\' title="点击关闭">X</a></div>'+
							'	<div class="message_text">'+
							'		<div>'+
							'			<textarea id="content" name="content" height="96" rows="5" class="textarea" onkeydown="show(\'content\',\'info\');"></textarea>'+
							'		</div>'+
							'	<div>'+
							'	<div id="btn"><input type="button" id="msg_btn" onclick="submitAdvice();" value="提交" title="点击提交留言"/><span id="info">0/140 字符</span></div>'+
							'	</form>'+
							'</div>',
					showOn: 'none',
					alignTo: 'target',
					alignX: 'right',
					alignY: 'center',
					offsetX: 20,
					showTimeout: 100
			});
			$('#liuyan').poshytip("show");
			show_message=true;
		}
		
		function submitAdvice(){
			//alert("对不起暂未开放留言功能!后续完善后会开放该功能!请谅解！");
			var content = $("#content").val();
			var real_content = content.replace(/(^\s*)|(\s*$)/g, "");
			if(content==""||real_content==""){
				alert("建议内容不能为空");
				$("#content").text("");
				$("#content").focus();
				return;
			}
			if(real_content.length>140){
				alert("输入内容超过140个字符!");
				return;
			}
			$.ajax({
				type:"post", 
				cache:false, 
				url:"advice", 
				dataType:"json", 
				data:$("#advice_form").serialize(),
				success:function (data) {
					if(data.status=="1"){
						alert("留言成功!");
						$('#liuyan').poshytip("hide");
						show_message=false;
					}
				},
				error:function(e){
					alert(e);
				}
			});
			return false;
		}
		
		function show(a,b) {
        	var maxl = 140;
        	var s = document.getElementById(a).value.length + 1;
        	if (s > maxl){
            	document.getElementById(a).value = document.getElementById(a).value.substr(0, maxl - 1)
       		}else{
            	document.getElementById(b).innerHTML = s + "/" + maxl+ " 字符";
            }
       	}