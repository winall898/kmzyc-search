<table width="98%" class="list_table" align="center" cellpadding="3"cellspacing="0" border="1">
	<tr>
		<th align="center" width="3%">
			<input type="checkbox" class="checkAll" onclick="checkAll()"/>
		</th>
		<th align="center" width="5%">序号</th>
		<th align="center" width="15%">停止词</th>
		<th align="center" width="50%">说明</th>
		<th align="center" width="10%">修改日期</th>
		<th align="center" width="10%">修改人</th>
		<th align="center">操作</th>
	</tr>
	<#if stopwordList?? && stopwordList?size gt 0>
		<#list stopwordList as item>
			<tr>
				<td align="center" width="3%">
					<input type="checkbox" class="check" value="${item.id}"/> 
				</td>
				<td align="center">${item_index + 1}</td>
				<td align="center">${item.stopword}</td>
				<td align="center">${item.description}</td>
				<td align="center">${item.updateTime?string("yyyy-MM-dd HH:mm:ss")}</td>
				<td align="center">${item.updater}</td>
				<td align="center">
					<img type="button" style="cursor: pointer;" title="修改" src="/etc/images/button_new/modify.png" onclick="gotoUpateStopwordPage(${item.id})">&nbsp;&nbsp;
					<img type="button" style="cursor: pointer;" title="删除" src="/etc/images/button_new/delete.png" onclick="deleteStopword(${item.id})">&nbsp;&nbsp;
				</td>
			</tr>
		</#list>
	<#else>
		<tr>
			<td align="center" colspan="7">没有查询到数据</td>
		</tr>
	</#if>
</table>
<!-- 分页数据 -->
<input type="hidden" id="page_count" value="${page.pageCount}"/>
<input type="hidden" id="page_no" value="${page.pageNo}"/>
<input type="hidden" id="page_size" value="${page.pageSize}"/>
<!-- 分页按钮区 -->
<table width="98%" align="center" cellpadding="0" cellspacing="0">
	<tr>
		<td>
			<table width="100%" border="0"  cellspacing="0" cellpadding="5" style="font-size:12px;">
			    <tr valign="bottom" border="0">
					<td align="left">
						共<font color='#008000'> ${page.recordCount} </font>条记录，第<font color='#008000'> ${page.pageNo} </font>页，共<font color='#008000'> ${page.pageCount} </font>页
			        </td>
			        <td align="right">
						跳转到<select id="pageNo" name="pageNo" onchange="changePage()"></select>页&nbsp;&nbsp;&nbsp;
					</td>
					<td align="right" style="width: 120px; padding-top:4px">
						每页
						<select id="pageSize" name="pageSize" onchange="changePage()">
							<option value="10" >10</option>
							<option value="20" >20</option>
							<option value="30" >30</option>
							<option value="40" >40</option>
							<option value="50" >50</option>
						</select>
						条
					</td>
					<td align="right" style="width: 200px; padding-top:4px">
						<#if page.pageCount==0 || page.pageCount==1>
							<img src="/etc/images/ICON_FIRST.gif" alt="首页" border="0">
							<img src="/etc/images/ICON_PRIOR.gif" alt="上页" border="0">
							<img src="/etc/images/ICON_NEXT.gif" alt="下页" border="0">
							<img src="/etc/images/ICON_LAST.gif" alt="末页" border="0">
						<#else>
							<#if page.pageNo==1>
								<img src="/etc/images/ICON_FIRST.gif" alt="首页" border="0">
								<img src="/etc/images/ICON_PRIOR.gif" alt="上页" border="0">
								<a href="javascript:;" onClick="selectPage(${page.pageNo + 1});"><img src="/etc/images/ICON_NEXT.gif" alt="下页" border="0"></a>
								<a href="javascript:;" onClick="selectPage(${page.pageCount});"><img src="/etc/images/ICON_LAST.gif" alt="末页" border="0"></a>
							<#elseif page.pageNo==page.pageCount>
								<a href="javascript:;" onClick="selectPage(1);"><img src="/etc/images/ICON_FIRST.gif" alt="首页" border="0"></a>
								<a href="javascript:;" onClick="selectPage(${page.pageNo - 1});"><img src="/etc/images/ICON_PRIOR.gif" alt="上页" border="0"></a>
								<img src="/etc/images/ICON_NEXT.gif" alt="下页" border="0">
								<img src="/etc/images/ICON_LAST.gif" alt="末页" border="0">
							<#else>
								<a href="javascript:;" onClick="selectPage(1);"><img src="/etc/images/ICON_FIRST.gif" alt="首页" border="0"></a>
								<a href="javascript:;" onClick="selectPage(${page.pageNo - 1});"><img src="/etc/images/ICON_PRIOR.gif" alt="上页" border="0"></a>
								<a href="javascript:;" onClick="selectPage(${page.pageNo + 1});"><img src="/etc/images/ICON_NEXT.gif" alt="下页" border="0"></a>
								<a href="javascript:;" onClick="selectPage(${page.pageCount});"><img src="/etc/images/ICON_LAST.gif" alt="末页" border="0"></a>
							</#if>
						</#if>
			        </td>
			    </tr>
			</table>
		</td>
	</tr>
</table>
<script type="text/javascript">
$(function(){
	var pageCount = parseInt($("#page_count").val());
	for ( var i=0;i<pageCount;i++){
		 var oOption = document.createElement("OPTION");   
		 oOption.value=i+1;   
		 oOption.text=i+1;   
		 document.getElementById('pageNo').options.add(oOption); 
	}
	
	document.getElementById('pageNo').value=$("#page_no").val();
	
	document.getElementById('pageSize').value=$("#page_size").val();
});
</script>