/**
 * 全选
 */
function checkAll(){
	if($(".checkAll")[0].checked == true){
		$(".check").each(function(){
			$(this).attr("checked","checked");
		});
	}else{
		$(".check").each(function(){
			$(this).removeAttr("checked");
		});
	}
}
/**
 * 查询分词列表
 */
function queryKeywordList(){
	$.ajax({
		type: 'post',
		url: '/keyword/keywordlist',
		data: $("#queryForm").serialize(),
		dataType:'html',
		success: function(html){
			$("#keywordList").html(html);
		}
	});
}

/**
 * 进入添加页面
 */
function gotoAddKeywordPage(){

	window.location.href="/keyword/gotoAddKeywordPage";
}

/**
 * 进入修改页面
 */
function gotoUpateKeywordPage(id){

	window.location.href="/keyword/gotoUpateKeywordPage?id="+id;
}

/**
 * 删除
 */
function deleteKeyword(id){
	if(confirm("确定删除吗？")){
		$.ajax({
			type: 'post',
			url: '/keyword/deleteKeyword?id='+id,
			dataType:'json',
			success: function(data){
				alert(data.msg);
				window.location.href="/keyword/gotoKeyword";
			}
		});
	}
}

/**
 * 批量删除
 */
function batchDeleteKeyword(){
	var idStr = '';
	$(".check").each(function(){
		if($(this)[0].checked == true){
			idStr += $(this).val() + ",";
		}
	});
	
	if(idStr == ''){
		return false;
	}
	
	if(confirm("确定删除已选择数据吗？")){
		$.ajax({
			type: 'post',
			url: '/keyword/batchDeleteKeyword?idStr='+idStr,
			dataType:'json',
			success: function(data){
				alert(data.msg);
				window.location.href="/keyword/gotoKeyword";
			}
		});
	}
}

/**
 * 保存分词
 */
function saveKeyword(){
	//校验分词
	if(isNull($("#keyword").val())){
		if($("#error_keyword") != 'undefined'){
			$("#error_keyword").remove();
		}
		//错误提示
		$("#keyword").after('<label for="error" id="error_keyword" generated="true" class="error">分词不能为空！</label>');
		return false;
	}
	
	//id
	var id = $("#id").val();
	$.ajax({
		type: 'post',
		url: '/keyword/doSaveKeyword',
		data: $("#dataForm").serialize(),
		dataType:'json',
		success: function(data){
			if(id != null && id != ''){
				alert(data.msg);
				if(data.is_success == true){
					window.location.href="/keyword/gotoKeyword";
				}
			}else{
				alert(data.msg);
				if(data.is_success == true){
					window.location.href="/keyword/gotoAddKeywordPage";
				}
			}
		}
	});
}

function gotoImportKeywordPage(){
	var url = "/keyword/gotoImportKeywordPage";
	myDialog = art.dialog.open(url, {
		title : '关键词导入',
		width : 700,
		height : 400,
		drag : false,
		close : function() {			
			$.unblockUI();
		}
	});

	$.blockUI.defaults.overlayCSS.opacity = '0.5';
	$.blockUI({
		message : ""
	});
}

function importKeyword(){	
	 $.ajaxFileUpload({
            url:'/keyword/importKeyword',//用于文件上传的服务器端请求地址
            secureuri:false,//一般设置为false
            fileElementId:'keywordImportFile',//文件上传空间的id属性  <input type="file" id="file" name="file" />
            dataType: 'json',//返回值类型 一般设置为json
            success: function (data){
            	if(data.is_success == true){            	
            		alert(data.msg);            
            		window.parent.location.href="/keyword/gotoKeyword";
				}else{
					alert(data.msg);
				}                
            }
        });
}

/**
 * 刷新词库
 */
function refreshKeyword(){
	$.ajax({
		type: 'get',
		url: '/keyword/refreshKeywordDict',
		data: {},
		dataType:'json',
		success: function(data){			
			alert(data.msg);				
		}
	});
}

/**
 * 返回
 */
function goBack(){
	
	window.location.href="/keyword/gotoKeyword";
}

/**
 * 跳到第几页
 */
function changePage(){
	
	//记录当前页
	$("#page_no").val($("#pageNo").val());
	
	//记录当前每页显示大小
	$("#page_size").val($("#pageSize").val());
	
	$.ajax({
		type: 'post',
		url: '/keyword/keywordlist',
		data: $("#queryForm").serialize() + "&pageNo=" + $("#page_no").val() + "&pageSize=" + $("#page_size").val(),
		dataType:'html',
		success: function(html){
			$("#keywordList").html(html);
		}
	});
}

/**
 * 跳到第几页
 */
function selectPage(pageNo){
	
	//记录当前页
	$("#page_no").val(pageNo);
	$("#pageNo").val(pageNo);
	
	$.ajax({
		type: 'post',
		url: '/keyword/keywordlist',
		data: $("#queryForm").serialize() + "&pageNo=" + $("#page_no").val() + "&pageSize=" + $("#page_size").val(),
		dataType:'html',
		success: function(html){
			$("#keywordList").html(html);
		}
	});
}

/**
 * 判断字符串是否为空
 * @param str
 * @returns {Boolean}
 */
function isNull(str){
	if(str == null || str == ""){
		
		return true;
	}
	//去除空格/回车/换行
	str = str.replace(/[ ]/g,"").replace(/[\r\n]/g,"");
	if(str == ""){
		
		return true;
	}
	
	return false;
}