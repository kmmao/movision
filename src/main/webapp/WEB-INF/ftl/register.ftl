<#assign form=JspTaglibs["http://www.springframework.org/tags/form"]>
<#assign ctx=rc.contextPath>
<!DOCTYPE html>
<html>
<head lang="en">
    <meta charset="UTF-8">
    <title>注册</title>
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no">
    <meta name="format-detection" content="telephone=no">
    <meta name="renderer" content="webkit">
    <meta http-equiv="Cache-Control" content="no-siteapp"/>
    <link rel="alternate icon" type="image/png" href="${e.res('/img/favicon.png')}">
    <link rel="stylesheet" href="${e.res('/css/base.css')}"/>
    <link rel="stylesheet" href="${e.res('/css/common.css')}"/>
    <style>
        .header {
            text-align: center;
        }

        .header h1 {
            font-size: 200%;
            color: #333;
            margin-top: 30px;
        }

        .header p {
            font-size: 14px;
        }
    </style>
</head>
<body>
<div class="header">
    <div class="am-g">
        <h1>筑慧宝</h1>

        <p>通天下,走四方</p>
    </div>
    <hr/>
</div>	
<div class="am-g">
    <div class="am-u-sm-centered am-u-lg-7">
        <h1 style="width: 100px;margin: 10px auto;color:#0e90d2;">注册</h1>
    <@form.form class="am-form am-form-horizontal" id="register-form" action="${ctx}/rest/register" method="post">
        <div class="am-form-group">
            <label for="doc-ipt-3" class="am-u-sm-2 am-form-label">手机/邮箱</label>
            <div class="am-u-sm-10">
                <input type="text" id="email" name="email" class="required display-inline" style="width: 70%"
                       placeholder="输入手机号码/邮箱">
            </div>
        </div>

        <div class="am-form-group">
            <label for="password" class="am-u-sm-2 am-form-label">密码</label>
            <div class="am-u-sm-10">
                <input type="password" id="password"  name="password" class="required display-inline" style="width: 70%"
                       placeholder="请输入密码">
            </div>
        </div>
        <div class="am-form-group">
            <label for="confirm_password" class="am-u-sm-2 am-form-label">确认密码</label>
            <div class="am-u-sm-10">
                <input type="password" id="confirm_password"  name="confirm_password" class="required display-inline" style="width: 70%"
                       placeholder="请输入确认密码">
            </div>
        </div>
        
        <div class="am-form-group">
            <label for="confirm_password" class="am-u-sm-2 am-form-label">会员身份</label>
            <div class="am-u-sm-10">
                <input type="radio" id="identify"  name="identify" value="1" class="required display-inline" style="width: 20%">企业</input>
                <input type="radio" id="identify"  name="identify" value="2" class="required display-inline" style="width: 20%">个人</input>
            </div>
        </div>
         <div class="am-form-group">
                <label for="doc-ipt-pwd-2" class="am-u-sm-2 am-form-label">验证码</label>
                <div class="am-u-sm-10">
                    <input type="text" id="authCode" name="authCode" class="required display-inline" style="width: 70%"
                           placeholder="请输入验证码">
                    <img id="authCodeImg" class="yanzheng" alt="验证码" src="/rest/imgCode"  width="0" height="0" onclick="randomImg()" 
	                    	style="margin-left: -20px;margin-top: 5px;height: 22px;width: 66px;"/>
	                <span style="font-size: 12px;"><a class="" href="javascript:;" onclick="randomImg()" >点击换一张</a></span>
	                <span id="codeResult"></span>
                </div>
            </div>

        <div class="am-form-group">
            <div class="am-u-sm-10 am-u-sm-offset-2">
                <button id="btnreg" type="submit" class="am-btn am-btn-primary">注册</button>
                <!--<span style="color:red;font-size: 13px;margin-left: 20px;">用户名已存在</span>-->
            </div>
        </div>
    </@form.form>
        <hr>
        <p style="text-align: center">© 2015 W.H.E.O Seven Studio. </p>
    </div>
</div>
<!-- jQuery 2.1.4 (necessary for Bootstrap's JavaScript plugins)-->
<script src="${e.res('/js/jquery.min.js')}"></script>
<script src="${e.res('/js/validate/jquery.validate.min.js')}"></script>
<script src="${e.res('/js/validate/additional-methods.js')}"></script>
<script src="${e.res('/js/validate/location/messages_zh.js')}"></script>
<script>
    $(function () {
        $('#register-form').validate({
//            submitHandler:function(form){
//                alert("submitted");
//                form.submit();
//            $(form).ajaxSubmit();    //ajax form 提交
//            },
//            debug: true,
            errorElement: "span",
            success: "valid",
            rules: {
                password: {
                    required: true,
                    minlength: 6
                } ,
                confirm_password: {
                    required: true,
                    minlength: 6,
                    equalTo: "#password"
                }
            },
            messages: {
                username: {
                    required: "请输入手机号码"
                },
                password: {
                    required: "请输入密码",
                    minlength: $.format("密码不能小于{0}个字符")
                } ,
                confirm_password:{
                    required: "请输入确认密码",
                    minlength: $.format("密码不能小于{0}个字符"),
                    equalTo: "两次输入密码不一致"
                }
            }
        });
    })
    
    function randomImg()
    {
		$("#authCodeImg").attr("src", "/zhuhuibao/code");
    }
    
    $("#authCode").on("keyup", function(){
			//checkCode();
		});
    function checkCode() {  
       var mcode = $('#authCode').val(); 
       if(mcode.length >= 4)
       {
	       $.ajax({
			url : "${ctx}/checkCode",
			type : "POST",
			dataType : "json",
			data : {"mcode":mcode},
			success: function (d, textStatus, jqXHR) {
	            console.log(d);
	        }
			});
	    } 
    } 
    function backfunc(data)
    {
    	$("#codeResult").html(data);
    }
</script>
</body>
</html>