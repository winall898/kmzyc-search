<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>促销活动</title>
<link href="/etc/css/style_sys.css" type="text/css" rel="stylesheet">
<link href="/etc/css/style_app.css" type="text/css" rel="stylesheet">
<link href="/etc/css/validate.css" type="text/css" rel="stylesheet">
<script src="/etc/js/jquery-1.8.3.js"></script>
<script type="text/javascript" src="/res/js/keyword/keyword.js"></script>

<style type="text/css">
.tableStyle1 {
	font-size: 12px;
}
.sbDiv,.emDiv{
float: left;position:relative;margin:3px 5px 2px 0;
white-space:nowrap;height:15px;line-height: 15px;
cursor:pointer;border-radius:17px;border-style:
solid;border-width:1px;font-size:14px;
padding:2px 19px;border-color:#edb8b8;
background-color:#ffeaea;color:#c30!important;
display:inline-block;vertical-align:middle;
}
em{
margin-left:-8px;vertical-align:top;display:inline-block;font-style:normal;
text-decoration:none;white-space:nowrap;line-height:15px;cursor:pointer;font-size:14px;
}
.aclose,.deleteP{position: absolute;right: -2px;top: -1px;text-decoration: none;font-family: verdana;border-radius: 0 17px 17px 0;
font-weight: bold;padding: 2px 5px 2px 3px;border-width: 1px;border-style: solid;border-color:#edb8b8!important;color:#c30!important;}

.listTitle{height:40px;line-height:40px;background:#c7e3f1;vertical-align:middle;font-size:13;color:#222;margin-bottom:20px}
.listTitle span{padding-left:20px;height:40px;line-height:40px;vertical-align:middle;margin-top:5px}
.listTitle span img{vertical-align:middle}
</style>
</head>
<!-- 导航栏 -->
<div width="100%" class="listTitle">
	<span>
		<img src="/etc/images/icon_02.png" style="vertical-align: middle;"/>&nbsp;
		<img src="/etc/images/icon_08.png" />&nbsp;词库管理&nbsp;
		<img src="/etc/images/icon_08.png" />&nbsp;添加分词&nbsp;
	</span>
</div>
<body>
	<!-- 数据编辑区域 -->
<form id="dataForm">
	<input type="hidden" id="id" name="id" value="">
	<table width="95%" class="edit_table" align="center" cellpadding="3" cellspacing="0" border="1" bordercolor="#C7D3E2" style="border-collapse: collapse; font-size: 12px;">
		<tr>
			<th width="20%" align="right" class="eidt_rowTitle"><font color="red">*</font>分词：</th>
		  	<td width="80%">
		   		<input type="text" name="keyword" id="keyword" size="70" maxlength="70" style="width:400px" onblur=""/>
		  	</td>
		</tr>
		<tr>
			<th align="right" class="eidt_rowTitle">说明：</th>
			<td><label><textarea name="description" id="description" rows="8" cols="45"></textarea></label></td>
		</tr>
		<tr>
			<td></td>
			<td align="left">
				<input  class="saveBtn" type="button" onclick="saveKeyword()"/>
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 
				<input class="backBtn" type="button" onclick="goBack()"/> 
			</td>
		</tr>
	</table>
</form>
</body>
</html>

