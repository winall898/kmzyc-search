<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>分词导入</title>
<link href="/etc/css/style_sys.css" type="text/css" rel="stylesheet">
<link href="/etc/css/style_app.css" type="text/css" rel="stylesheet">
<link href="/etc/css/validate.css" type="text/css" rel="stylesheet">
<link href="/etc/css/opendiv-normal.css" rel="stylesheet" type="text/css" />
<script src="/etc/js/jquery-1.8.3.js"></script>
<script src="/etc/js/ajaxfileupload.js"></script>
<script type="text/javascript" src="/res/js/stopword/stopword.js"></script>

<style type="text/css">
body {
	padding: 0px;
	margin: 0px;
}

table {
	margin-left: 10px;
}
</style>
</head>

<body>
	<!-- 数据编辑区域 -->
<form id="dataForm">	
	<table width="90%" class="edit_table" align="center" cellpadding="3" cellspacing="0" border="1" bordercolor="#C7D3E2" style="border-collapse: collapse; font-size: 12px;">
		<tr>
			<th align="right" class="eidt_rowTitle" width="30%">上传导入文件：</th>
			<td><input type="file" id="stopwordImportFile" name="stopwordImportFile"></td>
			
		</tr>
	</table>
	<table width="90%">
					<tbody>
						<tr align="center">
							<td align="center"><input type="button" class="btn-custom" value="提交" onclick="importStopword()"/>		
				 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
					</tbody>
				</table>
</form>
</body>
</html>

