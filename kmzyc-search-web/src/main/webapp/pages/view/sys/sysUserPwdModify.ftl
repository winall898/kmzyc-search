<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>我的信息</title>
<link href="/etc/css/style_sys.css" type="text/css" rel="stylesheet">
<Script language="JavaScript" src="/etc/js/Form.js" type="text/javascript"></Script>
<link href="/etc/css/opendiv-normal.css" rel="stylesheet" type="text/css" />
<script src="/etc/js/jquery-latest.pack.js"></script>
<script src="/etc/js/dialog.js"></script>
</head>
<jsp:include flush="true" page="/pages/html/common/sessionJudge.html"/>
<!-- 提示信息 -->
<input type="hidden" id="msg" value="${msg}">
<form action="doSaveUserPwd" method="POST"  namespace='/sys' onsubmit="return  Validator.Validate(this,3)">
<!-- hidden properties -->
<input type="hidden" name="userId" value="${userInfo.userId}">
<!-- 标题条 -->
<div class="pagetitle">密码修改:</div>
<!-- 按钮条 -->
<!-- 导航栏 -->
		<s:set name="parent_name" value="'搜索系统'" scope="request" />
		<s:set name="name" value="'修改密码'" scope="request" />
		<jsp:include page="/WEB-INF/jsp/common/title.jsp"></jsp:include>


<!-- 数据编辑区域 -->
<table width="95%" class="tableInput1" align="center" cellpadding="3" cellspacing="0" border="1" bordercolor="#C7D3E2" style="border-collapse: collapse">
	<tr>
        <th width="30%" align="right"><font color="red">*</font>用户名：</th>
        <td> 
            ${userInfo.userName}
        </td>
    </tr>
    <tr>
        <th width="30%" align="right"><font color="red">*</font>真实姓名：</th>
        <td> 
            ${userInfo.userReal}
        </td>
    </tr>
    <tr>
        <th width="30%" align="right"><font color="red">*</font>原密码：</th>
        <td> 
            <input name="userPwd" type="password" require="true" dataType="LimitB" max="20" min="1" msg="输入原密码，且不超过20个字符" value=""/>
        </td>
    </tr>
    <tr> 
        <th width="30%" align="right"><font color="red">*</font>新密码：</th>
        <td> 
            <input name="newPwd1" type="password" require="true" dataType="LimitB" max="20" min="1" msg="输入新密码，且不超过20个字符" value=""/>
        </td>
    </tr>
    <tr> 
        <th width="30%" align="right"><font color="red">*</font>再次输入密码</th>
        <td> 
            <input name="newPwd2" type="password" require="true" dataType="Repeat" to="newPwd1" msg="两次密码输入不一致" value=""/>
        </td>
    </tr>
</table>

<!-- 底部 按钮条 -->

<table width="98%" align="center" class="bottombuttonbar" height="30" border="0" cellpadding="0" cellspacing="0">
	<tbody><tr> 
		<td width="90%" valign="middle">
			<INPUT class="btngreen" TYPE="submit" value="保存 ">
			<td width="10%" align="center"></td>
		</td>
	
	</tr>
</tbody></table>
</form>
</BODY>
<script type="text/javascript">
    $(function(){
    	if($("#msg").val() != null && $("#msg").val() != ""){
    		alert($("#msg").val());
    	}
    });
</script>	
</HTML>