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
 * 查询停止词列表
 */
function queryStopwordList(){
	$.ajax({
		type: 'post',
		url: '/stopword/stopwordlist',
		data: $("#queryForm").serialize(),
		dataType:'html',
		success: function(html){
			$("#stopwordList").html(html);
		}
	});
}

/**
 * 进入添加页面
 */
function gotoAddStopwordPage(){

	window.location.href="/stopword/gotoAddStopwordPage";
}

/**
 * 进入修改页面
 */
function gotoUpateStopwordPage(id){

	window.location.href="/stopword/gotoUpateStopwordPage?id="+id;
}

/**
 * 删除
 */
function deleteStopword(id){
	if(confirm("确定删除吗？")){
		$.ajax({
			type: 'post',
			url: '/stopword/deleteStopword?id='+id,
			dataType:'json',
			success: function(data){
				alert(data.msg);
				window.location.href="/stopword/gotoStopword";
			}
		});
	}
}

/**
 * 批量删除
 */
function batchDeleteStopword(){
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
			url: '/stopword/batchDeleteStopword?idStr='+idStr,
			dataType:'json',
			success: function(data){
				alert(data.msg);
				window.location.href="/stopword/gotoStopword";
			}
		});
	}
}

/**
 * 保存停止词
 */
function saveStopword(){
	//校验原始词
	if(isNull($("#stopword").val())){
		if($("#error_stopword") != 'undefined'){
			$("#error_stopword").remove();
		}
		//错误提示
		$("#stopword").after('<label for="error" id="error_stopword" generated="true" class="error">停止词不能为空！</label>');
		return false;
	}
	
	//id
	var id = $("#id").val();
	$.ajax({
		type: 'post',
		url: '/stopword/doSaveStopword',
		data: $("#dataForm").serialize(),
		dataType:'json',
		success: function(data){
			if(id != null && id != ''){
				alert(data.msg);
				if(data.is_success == true){
					window.location.href="/stopword/gotoStopword";
				}
			}else{
				alert(data.msg);
				if(data.is_success == true){
					window.location.href="/stopword/gotoAddStopwordPage";
				}
			}
		}
	});
}


function gotoImportStopwordPage(){
	var url = "/stopword/gotoImportStopwordPage";
	myDialog = art.dialog.open(url, {
		title : '停止词导入',
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

function importStopword(){	
	 $.ajaxFileUpload({
            url:'/stopword/importStopword',//用于文件上传的服务器端请求地址
            secureuri:false,//一般设置为false
            fileElementId:'stopwordImportFile',//文件上传空间的id属性  <input type="file" id="file" name="file" />
            dataType: 'json',//返回值类型 一般设置为json
            success: function (data){
            	if(data.is_success == true){            	
            		alert(data.msg);            
            		window.parent.location.href="/stopword/gotoStopword";
				}else{
					alert(data.msg);
				}                
            }
        });
}

/**
 * 刷新词库
 */
function refreshStopword(){
	$.ajax({
		type: 'get',
		url: '/stopword/refreshStopwordDict',
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
	
	window.location.href="/stopword/gotoStopword";
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
		url: '/stopword/stopwordlist',
		data: $("#queryForm").serialize() + "&pageNo=" + $("#page_no").val() + "&pageSize=" + $("#page_size").val(),
		dataType:'html',
		success: function(html){
			$("#stopwordList").html(html);
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
		url: '/stopword/stopwordlist',
		data: $("#queryForm").serialize() + "&pageNo=" + $("#page_no").val() + "&pageSize=" + $("#page_size").val(),
		dataType:'html',
		success: function(html){
			$("#stopwordList").html(html);
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