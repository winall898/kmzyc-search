	
	var options = { dataType: 'json', beforeSubmit: validateForm, success: createsuccess};
	
	function validateForm(){
		
	}
	function createsuccess(data){
		alert(data.msg);
		//window.location.href = '/app/warehouseShow.action';
		if(data.result==true){ 
			gotoListForView();
		}
	}
	
	function startUp() {
		var chkObj = $('input[name="warehouseChk"]:checked');
		if(chkObj.length==0){
	   		alert('请勾选要启用的仓库!')
	   		return;
	   	}
		
		if(confirm('确定启用已选仓库吗？')){
			var chk_value =[]; 
			var chk_tmp = [];
			var html = "";
			  $('input[name="warehouseChk"]:checked').each(function(){  
			   	chk_value.push($(this).val());    
			  });  
			for(var i=0;i<chk_value.length;i++){
				chk_tmp = chk_value[i].split("_");
				if(chk_tmp[1]!='0') {
					alert('请检查仓库状态是否为停用状态！');
					return;
				}
				html += '<input type="hidden" name="upWarehouseId" value="'+chk_tmp[0]+'"/>';
			}
			
			$('#startForm').html(html);
			$('#warehouseStatus').val('1');//启用状态
			$('#startForm').ajaxSubmit(options);
		}	
	}
	
	function stopDown() {
		var chkObj = $('input[name="warehouseChk"]:checked');
		if(chkObj.length==0){
	   		alert('请勾选要停用的仓库!')
	   		return;
	   	}
		
		if(confirm('确定停用已选仓库吗？')){
			var chk_value =[]; 
			var chk_tmp = [];
			var html = "";
			  $('input[name="warehouseChk"]:checked').each(function(){  
			   	chk_value.push($(this).val());    
			  });  
			for(var i=0;i<chk_value.length;i++){
				chk_tmp = chk_value[i].split("_");
				if(chk_tmp[1]!='1') {
					alert('请检查仓库状态是否为启用状态！');
					return;
				}
				html += '<input type="hidden" name="upWarehouseId" value="'+chk_tmp[0]+'"/>';
			}
			
			$('#stopForm').html(html);
			$('#warehouseStatus').val('0');//停用状态
			$('#stopForm').ajaxSubmit(options);
		}
	}

	function choiceArea(sourceAreaId,targetAreaId){
		var warehouseHtml = '<option value="">--请选择城市--</option>';
		
		if($("#"+sourceAreaId).val()=="" || $("#"+sourceAreaId).val()=="0"){
			$('#'+targetAreaId).html(warehouseHtml)
			return false;
		}
		
		$.ajax({
			dataType:'json',
			url:'/app/choiceArea.action?id='+$('#'+sourceAreaId).val(),
			error:function(){alert('请求失败，请稍后重试或与管理员联系！')},
			success:function(date){
				var areaList = date.areaList;
				var size = areaList.length;
				
				for(var i=0;i<size;i++){
					warehouseHtml += '<option value="'+areaList[i].areaId+'">'+areaList[i].areaName+'</option>';
				}
				$('#'+targetAreaId).html(warehouseHtml);
			}
		});
	}
	
	function choiceCity(){
		$('#areaName').val($('#areaId2 option:selected').text());
	}
	
	//弹出层 选择地区
	function popSelectArea() {
	    //dialog("选择地区","iframe:/dire/findAllProvinceByWarehouse.action?overlayAreaId="+$('#overlayAreaId').val(),"500px","540px","iframe","10");
	    popDialog('/dire/findAllProvinceByWarehouse.action?overlayAreaId='+$('#overlayAreaId').val(),'选择地区',500,368);
	}

	function closeOpenArea(areaIds,areaNames){
	    closeThis();
	    $("#overlayArea").val(areaNames);
	    $("#overlayAreaId").val(areaIds);
	}
	
	function checkSelected(){
		if($('#areaId2 option:selected').val()==''){
			$('#areaId2').focus();
			alert('城市必须选择!');
			return false;
		}
	}
	
	function checkWarehouseInfoName(){
		if($('#warehouseName').val()=="") return;
		$.ajax({
			dataType:'json',
			url:'/app/checkWarehouseInfoName.action?name='+$('#warehouseName').val(),
			error:function(){alert('请求失败，请稍后重试或与管理员联系！')},
			success:function(date){
				if(date.result==false){
					alert(date.msg);
					$('#warehouseName').select();
					return;
				}
			}
		});
	}
	
	function checkWarehouseNameByModify(){
		if($('#warehouseName').val()=="") return;
		$.ajax({
			dataType:'json',
			url:'/app/checkWarehouseNameByModify.action?name='+$('#warehouseName').val()+'&id='+$('#warehouseIdHidden').val(),
			error:function(){alert('请求失败，请稍后重试或与管理员联系！')},
			success:function(date){
				if(date.result==false){
					alert(date.msg);
					$('#warehouseName').select();
					return;
				}
			}
		});
	}
	
	//返回我的桌面界面
	function gotoList(){
	    location.href="/app/warehouseShow.action";
	}
	
	//返回我的桌面界面
	function gotoListForView(){
		$('#warehouseListForm').submit();
	}

	function gotoAdd(){
	    location.href="/app/toWarehouseAdd.action";
	}

	function gotoUpdate(id){
	    //location.href="/app/toWarehouseUpdate.action?id="+id;
		$('#frm').attr("action","/app/toWarehouseUpdate.action?warehouseInfo.warehouseId="+id);
		$('#frm').submit();
	}

	function gotoView(id){
		location.href="/app/toWarehouseView.action?id="+id;
	}

	function gotoViewWarehouse(id){
		$('#frm').attr("action","/app/toWarehouseView.action?warehouseInfo.warehouseId="+id);
		$('#frm').submit();
	}

	function doSearch(){
		document.getElementById('pageNo').value = 1;
		document.forms['frm'].submit();
	}
