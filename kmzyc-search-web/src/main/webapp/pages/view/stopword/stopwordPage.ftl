<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link href="/etc/css/opendiv-normal.css" rel="stylesheet" type="text/css">
<link href="/etc/css/style_app.css" type="text/css" rel="stylesheet">
<script src="/etc/js/jquery-1.8.3.js"></script>
<script src="/etc/js/artDialog4.1.7/artDialog.js?skin=default"></script>
<script src="/etc/js/artDialog4.1.7/plugins/iframeTools.js"></script>
<script src="/etc/js/jquery.blockUI.js"></script>
<script src="/etc/js/dialog-common.js"></script>
<script src="/res/js/stopword/stopword.js"></script>


<title>词库管理列表</title>
<style type="text/css">
.tableStyle1 {font-size: 12px;}
.listTitle{height:40px;line-height:40px;background:#c7e3f1;vertical-align:middle;font-size:13;color:#222;margin-bottom:20px}
.listTitle span{padding-left:20px;height:40px;line-height:40px;vertical-align:middle;margin-top:5px}
.listTitle span img{vertical-align:middle}
.searcharea{background-color:#f5f5f5;border:1px solid #e3e3e3;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.05);-moz-box-shadow:inset 0 1px 1px rgba(0,0,0,.05);box-shadow:inset 0 1px 1px rgba(0,0,0,.05);overflow:hidden;padding:6px}
.uneditable-input,input[type=text],input[type=url],input[type=search],input[type=tel],input[type=color],input[type=password],input[type=datetime],input[type=datetime-local],input[type=date],input[type=month],input[type=time],input[type=week],input[type=number],input[type=email],select,textarea{background-color:#fff;border:1px solid #ccc;-webkit-box-shadow:inset 0 1px 1px rgba(0,0,0,.075);-moz-box-shadow:inset 0 1px 1px rgba(0,0,0,.075);box-shadow:inset 0 1px 1px rgba(0,0,0,.075);-webkit-transition:border linear .2s,box-shadow linear .2s;-moz-transition:border linear .2s,box-shadow linear .2s;-o-transition:border linear .2s,box-shadow linear .2s;transition:border linear .2s,box-shadow linear .2s;display:inline-block;height:30px;padding:4px 6px;margin-bottom:3px;font-size:14px;line-height:20px;color:#555;vertical-align:middle}
.searcharea td{color:#999;line-height:31px;    font-size: 13px;}
#frm{margin:20px}
</style>
</head>
<!-- 导航栏 -->
<div width="100%" class="listTitle">
	<span>
		<img src="/etc/images/icon_02.png" style="vertical-align: middle;"/>&nbsp;
		<img src="/etc/images/icon_08.png" />&nbsp;停止词管理&nbsp;
		<img src="/etc/images/icon_08.png" />&nbsp;停止词列表&nbsp;
	</span>
</div>
<body>
	<form id="queryForm">
		<!-- 查询条件区域 -->
		<table width="98%" class="searcharea"  cellpadding="0" cellspacing="0" align="center">
			<tr height="30px">
				<td align="left">停止词：<input name="stopword" type="text" class="input_style" value=""></td>
				<td align="right" rowspan="2" >
					<input type="button" class="queryBtn" onclick="queryStopwordList()">
					<input type="button" class="addBtn" onclick="gotoAddStopwordPage()">
					<input type="button" class="delBtn" onclick="batchDeleteStopword()">
					<input type="button" class="btn-custom" value="刷新词库" onclick="refreshStopword()">
					<input type="button" class="btn-custom" value="导入" onclick="gotoImportStopwordPage()">
					<a href="/stopword/downloadStopwordTemp" ><input type="button" class="btn-custom" value="下载导入模板" ></a>
				</td>
			</tr>
		</table>
		<!-- 数据列表区域 -->
		<div id="stopwordList">

		</div>
	</form>
</body>
<script type="text/javascript">
$(function(){
	//显示首页数据
	queryStopwordList();

});
</script>
</html>