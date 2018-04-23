$().ready(function(e) {
		$("#couponGivetypeId").change(function(e) {
			//手工发放
			if ($('#couponGivetypeId option:selected').val() == 1) {
				 $("#leveRow").show();
				$("#custom_leve").show();
				$("#custom_id").show();
				$("#order_money").hide();
				$("#payLeastMoney").show();
				$("#payLeastMoney_value").show();
				$("#textfield10").detach();
				$("#fafang_title").show();
				$("#timeInput").show();
				$("#Coupondays").remove();
				$("#days").remove();
				$("#given_type").append("<input type='hidden' name='are' id='are1' />");
			}
			//注册发放
			if ($('#couponGivetypeId option:selected').val() == 2) {
				$("#custom_leve").hide();
				$("#leveRow").hide();
				$("#order_money").hide();
				$("#custom_id").hide();
				$("#fafang_title").hide();
				$("#payLeastMoney").remove();
				$("#payLeastMoney_value").remove();
				$("#timeInput").show();
				$("#days").remove();
				$("#are1").remove();
			}
			//订单
			if ($('#couponGivetypeId option:selected').val() == 3) {
				$("#custom_leve").hide();
				$("#leveRow").hide();
				$("#order_money").hide();
				$("#custom_id").hide();
			 	$("#fafang_title").hide();
				$("#payLeastMoney").remove();
				$("#payLeastMoney_value").remove();
				$("#timeInput").show();
				$("#days").remove();
				$("#are1").remove();
			}
			//兑换
			if ($('#couponGivetypeId option:selected').val()== 4) {
				$("#custom_leve").hide();
				$("#leveRow").hide();
				$("#order_money").hide();
				$("#custom_id").hide();
				$("#fafang_title").hide();
				$("#payLeastMoney").remove();
				$("#payLeastMoney_value").remove();
				$("#timeInput").hide();
			//	alert($("#Conpondays").length);
			 	 if ($("#Conpondays").length==0){
				$("#dayInput").append(" <div id='days'> <input id='Coupondays'  type='input' name='coupon.couponValidDay'    /> 天</div> ");	
			 	 }
				$("#dayInput").removeAttr("style");
				$("#are1").remove();
		}
			//抽奖奖品
			if ($('#couponGivetypeId option:selected').val() == 5) {
				$("#custom_leve").hide();
				$("#leveRow").hide();
				$("#order_money").hide();
				$("#custom_id").hide();
				$("#fafang_title").hide();
				$("#payLeastMoney").remove();
				$("#payLeastMoney_value").remove();
				$("#timeInput").show();
				$("#days").remove();
				$("#are1").remove();
			}	
		}); 
	}); 
	function addProds() {
		var choosedPro = document.getElementById("table2").getElementsByTagName("span");
		var havechoosed = "";
		for ( var i = 0; i < choosedPro.length; i++) {
			var havechoosed = havechoosed + choosedPro[i].innerHTML + ",";
		}
		  dialog("选择优惠产品", "iframe:/coupon/gotoCouponProduct.action?haveChoosedPro="
			 	+ havechoosed, "750px", "455px", "iframe");
	}
	function addSupplys() {
		var choosedPro = document.getElementById("table3").getElementsByTagName("span");
		var havechoosed = "";
		for ( var i = 0; i < choosedPro.length; i++) {
			var havechoosed = havechoosed + choosedPro[i].innerHTML + ",";
		}
		  dialog("选择供应商", "iframe:/coupon/gotoCouponSupply.action?haveChoosedSuplly="
			 	+ havechoosed, "750px", "455px", "iframe");
	}
	//选择优惠券2014-10-13
	function selectCoupon(){
		//弹出优惠券规则列表，并选择一个规则
		dialog("选择优惠券规则", "iframe:/coupon/chooseCouponRule.action"
				, "750px", "455px", "iframe");
		
	}
	function selectUser(){
		var haveChoosedCustom;
		var b="";
		$("input[name=coupon.customId]").each(function(){
		 	b=b+$(this).attr("value")+",";
			
		});
		haveChoosedCustom=b;
		dialog("选择会员", "iframe:/coupon/chooseCouponcustom.action?haveChoosedCustom="+haveChoosedCustom
					, "750px", "455px", "iframe");
	}
	function selectLeve(){
		var haveChoosedLev;
		var c="";
		$("input[name=coupon.customLeveid]").each(function(){
		 	c=c+$(this).attr("value")+",";
			
		});
		haveChoosedLev=c;
		  dialog("选择会员等级", "iframe:/coupon/chooseCouponLeve.action?haveChoosedLev="+haveChoosedLev,
			 	 "750px", "455px", "iframe");
	}
	//查看优惠券规则
	function couponDetail(){
		var couponId=$("#couponIdHid").val();
		dialog("查看规则", "iframe:/coupon/couponRule_pageShow.action?viewType=show&couponId="+couponId,
			 	 "750px", "455px", "iframe");
	}
	function wayChange(value){
		if(value==1){//选择了指定具体会员账号发放
			$("#grantWay1Div").show();
			$("#grantWay2Div").hide();
			$("#userSelectBtn").show();
			$("#leveSelectBtn").hide();
			$("#grantWay1TableDiv").show();
			$("#grantWay2TableDiv").hide();
		}else if(value==2){
			$("#grantWay1Div").hide();
			$("#grantWay2Div").show();
			$("#userSelectBtn").hide();
			$("#leveSelectBtn").show();
			$("#grantWay1TableDiv").hide();
			$("#grantWay2TableDiv").show();
		}
		$("#wayCheckValue").val(value);
		
	}
	//选择优惠券发放发放式
	function typeChange(value){
		if(value==1){
			$("#handworkGrantDiv").show();
			$("#regestGrantDiv").hide();
			$("#bearerGrantDiv").hide();
		}else if(value==2){
			$("#handworkGrantDiv").hide();
			$("#regestGrantDiv").show();
			$("#bearerGrantDiv").hide();
		}else if(value==6){
			$("#handworkGrantDiv").hide();
			$("#regestGrantDiv").hide();
			$("#bearerGrantDiv").show();
		}
		$("#couponTypeId").val(value);
	}
	function  initDiv(){//初始化页面
		$("#handworkGrantDiv").show();
		$("#regestGrantDiv").hide();
		$("#bearerGrantDiv").hide();
		$("#userSelectBtn").show();
		$("#leveSelectBtn").hide();
		$("#grantWay1TableDiv").show();
		$("#grantWay2TableDiv").hide();
	}
	//防重复提交
	var bool=true;
	function verifyData(){
		if(!bool){
			return;
		}
		var startime=$("#d4311").val();
		var endtime=$("#d4312").val();
		var couponId=$("#couponIdHid").val();
		var coupontypeId=$("#couponTypeId").val();
		var wayValue=$("#wayCheckValue").val();
		var count=$("#issuingCount").val();
		if(couponId==""){
			alert("请选择优惠券规则！");
			return;
		}
		if(coupontypeId==2){//当发放类型为注册发放时
			$.getJSON("/coupon/checkCouponTime.action", {'startime': startime, 'endtime': endtime, 'couponId': couponId}, function (msg) {
				 if(msg==2){
					alert("注册发放开始时间必须小于规则结束时间");
					return;
				}else if(msg==4){
					alert("注册发放结束时间必须小于规则结束时间");
					return;
				}
				$("#customArr").val(null);
				$("#customLeveArr").val(null);
				$("#issuingCount").val(null);
				bool=false;
				$("#couponGrantForm").submit();
			});
		}else if(coupontypeId==1){
		
			var  b = "";
			var c=""; 
			if($("#selectType1").attr("checked")){
				if($("#grantWay1").attr("checked")){
					$("input[name=coupon.customId]").each(function(){
					 	b=b+$(this).attr("value")+",";
						
					});
				$("#customArr").val(b);
				}else{
					$("input[name=coupon.customLeveid]").each(function(){
					 	c=c+$(this).attr("value")+",";
						
					});
					$("#customLeveArr").val(c);
				}
			}
				if(wayValue==1){
					var customArr=$("#customArr").val();
					if(customArr==""){
						alert("请选择客户！");
						return;
					}
				}else if(wayValue==2){
					var customLeveArr=$("#customLeveArr").val();
					if(customLeveArr==""){
						alert("请选择客户等级！");
						return;
					}
			   }
				$("#issuingCount").val(null);
				$("#d4311").val(null);
				$("#d4312").val(null);
				bool=false;
				$("#couponGrantForm").submit();
		}else{//不记名发放
				if(count==""){
					alert("请填写发放数量！");
					return;
				}else if(count>50000){
					alert("发放数量必须小于50000！");
					return;
				}
		
			$("#customArr").val(null);
			$("#customLeveArr").val(null);
			$("#d4311").val(null);
			$("#d4312").val(null);
			bool=false;
			$("#couponGrantForm").submit();
		}
	}
	
	//防重复提交
	var upbool=true;
	function editVerifyData(){
		if(!upbool){
			return;
		}
		var startime=$("#d4311").val();
		var endtime=$("#d4312").val();
		var couponId=$("#couponIdHid").val();
		var coupontypeId=$("#couponTypeId").val();
		var wayValue=$("#wayCheckValue").val();
		var count=$("#issuingCount").val().trim();
		if(couponId==""){
			alert("请选择优惠券规则！");
			return;
		}
		if(coupontypeId==2){//当发放类型为注册发放时
			$.getJSON("/coupon/checkCouponTime.action", {'startime': startime, 'endtime': endtime, 'couponId': couponId}, function (msg) {
				if(msg==1){
					alert("注册发放开始时间必须大于规则开始时间");
					return;
				}else if(msg==2){
					alert("注册发放开始时间必须小于规则结束时间");
					return;
				}else if(msg==3){
					alert("注册发放结束时间必须大于规则开始时间");
					return;
				}else if(msg==4){
					alert("注册发放结束时间必须小于规则结束时间");
					return;
				}
				$("#customArr").val(null);
				$("#customLeveArr").val(null);
				$("#issuingCount").val(null);
				upbool=false;
				$("#updateCouponForm").submit();
			});
		}else if(coupontypeId==1){
		
			var  b = "";
			var c=""; 
			if($("#selectType1").attr("checked")){
				if($("#grantWay1").attr("checked")){
					$("input[name=coupon.customId]").each(function(){
					 	b=b+$(this).attr("value")+",";
						
					});
				$("#customArr").val(b);
				}else{
					$("input[name=coupon.customLeveid]").each(function(){
					 	c=c+$(this).attr("value")+",";
						
					});
					$("#customLeveArr").val(c);
				}
			}
				if(wayValue==1){
					var customArr=$("#customArr").val();
					if(customArr==""){
						alert("请选择客户！");
						return;
					}
				}else if(wayValue==2){
					var customLeveArr=$("#customLeveArr").val();
					if(customLeveArr==""){
						alert("请选择客户等级！");
						return;
					}
			   }
				$("#issuingCount").val(null);
				$("#d4311").val(null);
				$("#d4312").val(null);
				upbool=false;
				$("#updateCouponForm").submit();
		}else{//不记名发放
				if(count=="" || count == null){
					alert("请填写发放数量！");
					return;
				}else if(count>5000){
					alert("发放数量必须小于5000！");
					return;
				}
		
			$("#customArr").val(null);
			$("#customLeveArr").val(null);
			$("#d4311").val(null);
			$("#d4312").val(null);
			upbool=false;
			$("#updateCouponForm").submit();
		}
	}
	function addLeve() {
		var choosedLev = document.getElementById("custom_table1").getElementsByTagName("span");
	 	var haveChoosedLev = "";
	 	for ( var i = 0; i < choosedLev.length; i++) 
	 	{
	 		var haveChoosedLev = haveChoosedLev + choosedLev[i].innerHTML + ",";
	 	}
		  dialog("选择会员等级", "iframe:/coupon/chooseCouponLeve.action?haveChoosedLev="
			 	+ haveChoosedLev, "750px", "455px", "iframe");
	}
	function adduser()
	{
	 var choosedCustom = document.getElementById("custom_table").getElementsByTagName("span");
 	var x = "";
	for ( var i = 0; i < choosedCustom.length; i++) 
		{
	 
		 	 x = x + choosedCustom[i].innerHTML + ",";
			 
		}
	dialog("选择会员等级", "iframe:/coupon/chooseCouponcustom.action?haveChoosedCustom="+x
				, "750px", "455px", "iframe");
	}
	//删除选择的产品列表
	function del(ID) {
		$("tr[id=" + ID + "]").remove();
	}
	function delLeve(ID) {
		$("tr[id=" + "trLeve" + ID + "]").remove();
	}
	function delCustom(ID) {
		$("tr[id=" + "trcustom" + ID + "]").remove();
	}
	function selectInput() {
		$("#custom_table1  input").attr("checked", "checked");
		$("#customContent  input").attr("checked", "checked");
	    $("#editBody input").attr("checked", "checked");
	    $("#editBody1 input").attr("checked", "checked");
		$("#productContent input").attr("checked", "checked");
		$("#supplyContent input").attr("checked", "checked");
		var treeObj = $.fn.zTree.getZTreeObj("treeDemo");
		var nodes = treeObj.getCheckedNodes(true);
		var categoryIds = ""; //提交之前，将三级类目Id取出来
		for ( var i = 0; i < nodes.length; i++) {
			if (nodes[i].level != 2) {
			} else {
				categoryIds += nodes[i].categoryId + ",";
			}
		}
		$("#categoryId").val(categoryIds);
	}


