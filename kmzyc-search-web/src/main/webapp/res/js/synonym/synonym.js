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
 * 查询同义词列表
 */
function querySynonymList(){
	$.ajax({
		type: 'post',
		url: '/synonym/synonymlist',
		data: $("#queryForm").serialize(),
		dataType:'html',
		success: function(html){
			$("#synonymList").html(html);
		}
	});
}

/**
 * 进入添加页面
 */
function gotoAddSynonymPage(){

	window.location.href="/synonym/gotoAddSynonymPage";
}

/**
 * 进入修改页面
 */
function gotoUpateSynonymPage(id){

	window.location.href="/synonym/gotoUpateSynonymPage?id="+id;
}

/**
 * 删除
 */
function deleteSynonym(id){
	if(confirm("确定删除吗？")){
		$.ajax({
			type: 'post',
			url: '/synonym/deleteSynonym?id='+id,
			dataType:'json',
			success: function(data){
				alert(data.msg);
				window.location.href="/synonym/gotoSynonym";
			}
		});
	}
}

/**
 * 批量删除
 */
function batchDeleteSynonym(){
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
			url: '/synonym/batchDeleteSynonym?idStr='+idStr,
			dataType:'json',
			success: function(data){
				alert(data.msg);
				window.location.href="/synonym/gotoSynonym";
			}
		});
	}
}

/**
 * 保存同义词
 */
function saveSynonym(){
	//判断原始词
	if(isNull($("#keyword").val())){
		if($("#error_keyword") != 'undefined'){
			$("#error_keyword").remove();
		}
		//错误提示
		$("#keyword").after('<label for="error" id="error_keyword" generated="true" class="error">原始词不能为空！</label>');
		return false;
	}
	
	//双向同义词和单向同义词不能同时为空
	if(isNull($("#synonymWord").val()) && isNull($("#unidirWord").val())){
		alert("双向同义词和单向同义词不能同时为空！");
		return false;
	}
	
	//id
	var id = $("#id").val();
	$.ajax({
		type: 'post',
		url: '/synonym/doSaveSynonym',
		data: $("#dataForm").serialize(),
		dataType:'json',
		success: function(data){
			if(id != null && id != ''){
				alert(data.msg);
				if(data.is_success == true){
					window.location.href="/synonym/gotoSynonym";
				}
			}else{
				alert(data.msg);
				if(data.is_success == true){
					window.location.href="/synonym/gotoAddSynonymPage";
				}
			}
		}
	});
}

function gotoImportSynonymPage(){
	var url = "/synonym/gotoImportSynonymPage";
	myDialog = art.dialog.open(url, {
		title : '同义词导入',
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

function importSynonym(){
	 $.ajaxFileUpload({
            url:'/synonym/importSynonym',//用于文件上传的服务器端请求地址
            secureuri:false,//一般设置为false
            fileElementId:'synonymImportFile',//文件上传空间的id属性  <input type="file" id="file" name="file" />
            dataType: 'json',//返回值类型 一般设置为json
            success: function (data){
            	if(data.is_success == true){            	
            		alert(data.msg);            
            		window.parent.location.href="/synonym/gotoSynonym";
				}else{
					alert(data.msg);
				}                
            }
        });
}

/**
 * 刷新词库
 */
function refreshSynonym(){
	$.ajax({
		type: 'get',
		url: '/synonym/refreshSynonymDict',
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
	
	window.location.href="/synonym/gotoSynonym";
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
		url: '/synonym/synonymlist',
		data: $("#queryForm").serialize() + "&pageNo=" + $("#page_no").val() + "&pageSize=" + $("#page_size").val(),
		dataType:'html',
		success: function(html){
			$("#synonymList").html(html);
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
		url: '/synonym/synonymlist',
		data: $("#queryForm").serialize() + "&pageNo=" + $("#page_no").val() + "&pageSize=" + $("#page_size").val(),
		dataType:'html',
		success: function(html){
			$("#synonymList").html(html);
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