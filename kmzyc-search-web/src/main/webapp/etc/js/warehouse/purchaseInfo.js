/*
 * 采购单和采购详情单相关操作的JS
 * @author luoyi
 */

//1.到采购单添加页
function gotoAddPurchaseInfo() {
	location.href = "/app/toPruchaseAdd.action";
}

// 2.查询采购单列表
function gotoqueryPurInfoList() {
   document.forms[0].action = "/app/queryAllPurchaseInfo.action?pageNum=1";
   document.forms[0].submit();
}

// 3.查询采购单
function searchPurchaseInfo() {
	document.getElementById('pageNo').value = 1;
	document.forms['purchaseInfofrm'].submit();
}
// 4.添加采购单
function doSearchPurchaseInfo() {
	// 先验证是否有重复的ID
	document.forms['purchaseAddfrm'].submit();
}

// 5.查询所有产品
function gotoSearchProduct() {
	document.forms['productListfrm'].submit();
}

//6.添加采购单一行
function addRowsFun() {
	var _len = $("#dataTable tr").length;
	var pdLen = _len - 3;// 用于设置行中PurchaseDetail中列的name，其中3=原有行数(4)-1=3
	$("#dataTable").append(
					"<tr id=purchase"
							+ _len
							+ " align='center'>"
							+ "<td align='center' width='5%'><input id='productId"
							+ _len
							+ "' type='hidden' name='purchaseDetail["
							+ pdLen
							+ "].productId'  />"
							+ "<input id='productSkuId"
							+ _len
							+ "' type='hidden' name='purchaseDetail["
							+ pdLen
							+ "].skuId'  />"
							+ "<input type='checkbox' name='purchaseIdChk'  /></td>"
							+ "<td align='center'><input size='20' id='skuId"
							+ _len
							+ "' onblur='return ValidateProductInfo(this,1)' type='text' name='purchaseDetail["
							+ pdLen
							+ "].artNo' reg ='^.+$' tip='请正确输入SKU码' maxlength='20' />" 
							+ "<img title='查找' style='cursor: pointer;' src='/etc/images/view.png'  onclick='popSelectProductSku(this.parentNode.parentNode.id);' />"
							+ "</td>"
							+ "<td align='center'><input size='20' id='productName"
							+ _len+"' type='text' name='purchaseDetail["
							+ pdLen
							+ "].productName' readonly='readonly'  disabled='true'      /><input type='hidden'  id='productNameHide"+_len+"'  name='purchaseDetail["+pdLen+"].productName' ></td>"
							+ "<td align='center'><input size='10'  id='quantity"
							+ _len
							+ "' type='text' name='purchaseDetail["
							+ pdLen
							+ "].quantity'  onkeyup=\"value=value.replace(/[^0-9]/g,'')\"  reg='^((?!0)\\d{1,6}|1000000)$'  tip='请输入1-1000000的整数' maxlength='7'/></td>"
							+ "<td align='center'><input size='20' id='productNo"
							+ _len
							+ "' type='text'   disabled='true'    name='purchaseDetail["
							+ pdLen
							+ "].productNo' readonly='readonly'/><input type='hidden'  id='productNoHide"+_len+"'  name='purchaseDetail["+pdLen+"].productNo' ></td>"
							+ "<td align='center'><input size='10' id='purPrice"
							+ _len
							+ "' type='text' name='purchaseDetail["
							+ pdLen
							+ "].purPrice' reg='^((?!0)[0-9]+\\.?\\d{0,6}|(0)\\.{1}\\d{1,6}|1000000)$' tip='价格范围[1-1000000]' maxlength='7'/></td>"
							+ "<td align='center'>"
							+ "<textarea id='remark"
							+ +_len
							+ "' name='purchaseDetail["
							+ pdLen
							+ "].remark' style='width: 180px; height: 23px;' cols='10' wrap='physical' maxlength='200' reg='^(\\S{0,100})$' tip='备注不要超过100个汉字'></textarea>"
							+ "</td>"
							+ "</tr>");
	
		//	重新加载js,验证出现错误提示用的
		var oHead = document.getElementsByTagName('HEAD').item(0);
	    var oScript= document.createElement("script");
	    oScript.type = "text/javascript";
	    oScript.src="/etc/js/validate/easy_validator.pack.js";
	    oHead.appendChild(oScript);
	    
}

// 7.删除采购单一行
function delRowsFun() {
	// 删除<tr/>
	var checked = $("input[type='checkbox'][name='purchaseIdChk']");
	var _len = $("#dataTable tr").length;// 删除之前行数
	if (_len > 4) {
		$(checked).each(function() {
			if ($(this).attr("checked") == true) // 注意：此处判断不能用$(this).attr("checked")==‘true'来判断。
			{
				$(this).parent().parent().remove();// 移除选中所在行
			}
		});
	}
	var _lenAfter = $("#dataTable tr").length;// 删除之后的行数
	if (_lenAfter < 4) {// 其中4=原有行数,如果小于4表示删除了所有，就自动添加一行
		addRowsFun();
	}

	this.changeNameIdOnId(_len);
}

// 删除时改变name\id
function changeNameIdOnId(_len) {// 传一个总行数过来

	// 接下来对所有行，进行name和id的value更改
	// 第一步:先将name的值全部更改
	for ( var i = 3; i <= _len + 1; i++) {
		var trId = "purchase" + i; // 行号tr的id

		// 如果能找到tr的id=trId的。
		if (document.getElementById(trId) != null) {
			var trIndex = document.getElementById(trId).rowIndex;// tr在表格哪一行

			var index = trIndex - 3;// 所在行号

			// 第一步:给行号改id的值
			document.getElementById(trId).id = "purchase" + trIndex;// 给行号改id

			// 第二 步:给input的name改值
			var detailid = "detailId" + i;// 细目id
			var skuid = "skuId" + i;// 产品SkuCode码所在的input.ID
			var productSkuid = "productSkuId" + i;// 产品SKU码所在的input.ID
			var productNameid = "productName" + i;// 产品名称id
			var productid = "productId" + i;// 产品名称id
			var quantityid = "quantity" + i;// 产品数量id
			var productNoid = "productNo" + i;// 产品编号id
			var purPriceid = "purPrice" + i;// 产品货号id
			var remarkid = "remark" + i;// 产品货号id
             var productNameHideid="productNameHide"+i; // 隐藏的产品名称id
             var productNoHideid="productNoHide"+i;  // 隐藏的产品编号 id
             
			// 给input的name赋值
			if (null != document.getElementById(detailid)) {
				document.getElementById(detailid).name = "purchaseDetail["
						+ index + "].detailId";
			}
			document.getElementById(productSkuid).name = "purchaseDetail["
					+ index + "].skuId";
			document.getElementById(skuid).name = "purchaseDetail[" + index
					+ "].artNo";// 产品skuCode所在name
			document.getElementById(productid).name = "purchaseDetail[" + index
					+ "].productId"; // 要赋值产品名称所在input的name
			document.getElementById(productNameid).name = "purchaseDetail["
					+ index + "].productName"; // 要赋值产品名称所在input的name
			document.getElementById(quantityid).name = "purchaseDetail["
					+ index + "].quantity"; // 要赋值产品名称所在input的name
			document.getElementById(productNoid).name = "purchaseDetail["
					+ index + "].productNo"; // 要赋值产品名称所在input的name
			document.getElementById(purPriceid).name = "purchaseDetail["
					+ index + "].purPrice"; // 要赋值purPrice所在input的name
			document.getElementById(remarkid).name = "purchaseDetail[" + index
					+ "].remark"; // 要赋值产品名称所在input的name

			document.getElementById(productNameHideid).name = "purchaseDetail[" + index
			+ "].productName"; // 要赋值隐含的产品名称所在input的name

			document.getElementById(productNoHideid).name = "purchaseDetail[" + index
			+ "].productNo"; // 要赋值隐含的产品名称所在input的name
			
			// 第三步:改input的id
			// 重新给input的id赋值
			document.getElementById(skuid).id = "skuId" + trIndex;// 产品SKU码所在的input.ID
			if (null != document.getElementById(detailid)) {
				document.getElementById(detailid).id = "detailId" + trIndex;// 产品SKU码所在的input.ID
			}
			document.getElementById(productSkuid).id = "productSkuId" + trIndex;// 产品SKU码所在的input.ID
			document.getElementById(productid).id = "productId" + trIndex;// 产品名称id
			document.getElementById(productNameid).id = "productName" + trIndex;// 产品名称id
			document.getElementById(quantityid).id = "quantity" + trIndex;// 产品数量id
			document.getElementById(productNoid).id = "productNo" + trIndex;// 产品productNo的id
			document.getElementById(purPriceid).id = "purPrice" + trIndex;// 产品purPrice的id
			document.getElementById(remarkid).id = "remark" + trIndex;// 产品remark的
			document.getElementById(productNameHideid).id = "productNameHide" + trIndex;//  隐藏的产品名称 
			document.getElementById(productNoHideid).id = "productNoHide" + trIndex;//  隐藏的产品名称 
			
			
		}
	}
}

// 8.根据sku查找商品资料
function ValidateProductInfo(skuId, type) {
	if (type == 1) {// 手输SkuCode查询
		var $skuIds = $(skuId);
		var id = "#" + $skuIds.attr("id");// 传递过来的input的ID
	} else {// 通过下拉框选择的SkuCode查询
		var id = "#" + skuId;// 传递过来的input的ID
	}

	var _productSkuId = $(id).val();
	_productSkuId = $.trim(_productSkuId);//jquery去除空格(IE和火狐支持)
	if (_productSkuId.length <= 0) {// 空字符串
		return false;
	}

//	// 先看有没有选择仓库
	var warehouseValue = $("#warehouseId1").val();
//	if (warehouseValue == "") {
//		alert("提示:请选择仓库!");
//		clearProductInfo();//清除
//		return false;
//	}

	// 再判断是否已存在此SKU
	var _len = $("#dataTable tr").length;// 行数
	var reg = /^\d+$/;

	var num = id.substring(6, id.length);// 其中的6=#skuId字符串的长度
	// 根据skuId验证输入的sku数值，是否已经存在
	for ( var i = 3; i < _len + 3; i++) {
		var _skuId = "#skuId" + i;
		if (_skuId != id) {
			var _productSkuValue = $(_skuId).val();
			if (_productSkuId == _productSkuValue) {
				alert("该SKU已存在！");
				$(skuId).val("");
				$(productSkuId).val("");
				$(productId).val("");
				$(productNameId).val("");
				$(priceId).val("");
				$(productNoId).val("");
				$(artNoId).val("");
				$(purPriceId).val("");
				var trId = "purchase"+num;
				this.popSelectProductSku(trId);//调出查询窗口
				return false;
			}
		}
	}

	// 绑定数据到采购单详情行的值
	var productSkuId = "#productSkuId" + num;
	var skuId = "#skuId" + num;
	var productId = "#productId" + num;
	var productNameId = "#productName" + num;
	var priceId = "#price" + num;
	var productNoId = "#productNo" + num;
	var artNoId = "#artNo" + num;
	var purPriceId = "#purPrice" + num;
	var remarkId = "#remark" + num;
	var productNameHideId="#productNameHide"+num;
	var productNoHideId="#productNoHide"+num;

	if (_productSkuId != "") {// 如果sku不为空
		var settings = {
			type : "post",
			url : "/app/findProductInfoBySku2.action?productSkuCode="
					+ _productSkuId + "&warehouseId=" + warehouseValue,
			dataType : "json",
			error : function(data) {
				alert("sku查询资料失败" + data);
			},
			success : function(data) {
				if (null != data) {
					var productSkuIdValue = data.productSkuId;// 产品SkuId
					var productSkuCodeValue = data.productSkuCode;// 产品skuCode
					var productIdValue = data.productId;// 产品id
					var productName = data.name;// 产品名称
					var price = data.marketPrice; // 不含税单价
					var productNo = data.productNo;
					var artNo = data.artNo;
					var purPrice = data.costPrice;
					$(productSkuId).val(productSkuIdValue);
					$(skuId).val(productSkuCodeValue);
					$(productId).val(productIdValue);
					$(productNameId).val(productName);
					$(priceId).val(price);
					$(productNoId).val(productNo);
					$(artNoId).val(artNo);
					$(productNameHideId).val(productName);
					$(productNoHideId).val(productNo);
					$(purPriceId).val(purPrice);
					$(remarkId).val("");
				} else {// 无资料，则清空
					$(skuId).val("");
					$(productSkuId).val("");
					$(productId).val("");
					$(productNameId).val("");
					$(priceId).val("");
					$(productNoId).val("");
					$(artNoId).val("");
					$(purPriceId).val("");
					$(remarkId).val("");
					$(productNameHideId).val(productName);
					$(productNoId).val(productNo);
					alert("此仓库SKU查询资料不存在!");
				}
			}
		};
		$.ajax(settings);
	}
}

// 9.审核采购单,将选 中的采购单ID数组传到后台
function gotoCheckPurchase() {
	var str = document.getElementsByName("purchaseChk");
	var objarray = str.length;
	var chestr = "";

	for (i = 0; i < objarray; i++) {// 遍历数组
		if (str[i].checked == true)// 如果选中，开始获得所选择的采购单ID
		{
			chestr += str[i].value + ",";
		}
	}
	if (chestr.length < 1) {
		alert("请选择要审核的采购单!");
		return false;
	}

	var answer = confirm("确认审核吗?");
	if (!answer) {
		return false;
	}
	// 发送ajax请求到action
	var settings = {
		type : "post",
		url : "/app/checkedPurchaseInfo.action?purchaseIdArray=" + chestr,
		dataType : "text",
		error : function() {
			//解决IE浏览器加载课件时的JSON对象错误问题
			if(typeof JSON == 'undefined'){
                $('head').append($("<script type='text/javascript' src='/etc/js/warehouse/json2.js'>"));
			}
			alert("采购单审核失败!");
		},
		success : function(data) {
			//解决IE浏览器加载课件时的JSON对象错误问题
			if(typeof JSON == 'undefined'){
                $('head').append($("<script type='text/javascript' src='/etc/js/warehouse/json2.js'>"));
			}
			var msg = JSON.parse(data);
			alert(msg.message);
			// 再查询一次审核列表
			location.href = "/app/toEditPurchaseInfo.action";
		}
	};
	$.ajax(settings);
}

// 10.到采购单详情修改页面
function toEditPDetail(purchaseId) {
	location.href = "/app/toEditPDetail.action?optionType=purchaseEdit&purchaseId=" + purchaseId;
}

// 11.删除未审核的采购单
function deleteUnCheckedPurchase() {
	var str = document.getElementsByName("purchaseChk");
	var objarray = str.length;
	var chestr = "";

	for (i = 0; i < objarray; i++) {// 遍历数组
		if (str[i].checked == true)// 如果选中，开始获得所选择的采购单ID
		{
			chestr += str[i].value + ",";
		}
	}
	if (chestr.length < 1) {
		alert("请选择要删除的采购单!");
		return false;
	}

	var answer = confirm("确认删除所选采购单吗?");
	if (!answer) {
		return false;
	}

	// 确认删除
	$.ajax({
		type : "post",
		url : "/app/deletePurchaseInfoById.action?ajax=yes&purchaseIdArray="
				+ chestr,
		cache : false,
		data : "json",
		success : function(data) {
			// 将字符串转为json格式
			if (null != data) {
				//解决IE浏览器加载课件时的JSON对象错误问题
				if(typeof JSON == 'undefined'){
	                $('head').append($("<script type='text/javascript' src='/etc/js/warehouse/json2.js'>"));
				}
				var msg = JSON.parse(data);
				alert(msg.message);
				// 再查询一次订采购单列表
				location.href = "/app/queryAllPurchaseInfo.action";
			}
		},
		error : function(data) {
			//解决IE浏览器加载课件时的JSON对象错误问题
			if(typeof JSON == 'undefined'){
                $('head').append($("<script type='text/javascript' src='/etc/js/warehouse/json2.js'>"));
			}
			var msg = JSON.parse(data);
			alert(msg.message);
		}
	})
}

// 12.到采购详情单
function gotoPurchaseDetail(purchaseId) {	
	 document.forms[0].action = "/app/queryAllPurchaseDetail.action?purchaseId="+purchaseId+"&pageNum=1";
	 document.forms[0].submit();
}

// 13.查询采购单审核列表
function gotoqueryEditPurInfoList() {
	document.forms[0].action = "/app/toEditPurchaseInfo.action?pageNum=1";
	document.forms[0].submit();
}
// 14.审核:到采购详情单
function toPurchaseCheckDetail(purchaseId) {
	document.forms[0].action = "/app/queryPurchaseDetailEdit.action?purchaseId="
			+ purchaseId+"&pageNum=1";
	document.forms[0].submit();
}
// 15.全选事件
function checkAll(ck) {
	var inputs = ck.form.getElementsByTagName("input");
	for ( var i = 0; i < inputs.length; i++) {
		var ele = inputs[i];
		/* var ct = ele.getAttribute("type"); */
		if ((ele.type == "checkbox")) {
			if (ck.checked != ele.checked)
				ele.click();
		}
	}
}

// 16.查找所有sku产品
function popSelectProductSku() {
	var warehouseValue = $("#warehouseId1").val();

	popDialog("/app/findAllSkuProductForPurchase.action?type=stock&warehouseId="+warehouseValue+"&trId="+0,"查看所有SKU商品",1000,400);
	
	
}

// 17.查找sku产品后，回来的参数值
/*function closeOpenSku(skuAttributeId, skuAttValue, productId, trId) {
	closeThis();
	var skuId = "skuId" + trId.substring(8, trId.length);// 8的长度=purchase
	document.getElementById(skuId).value = skuAttValue;
	ValidateProductInfo(skuId, 0);

}*/

function submitForm(){
	var hiddenStr = '';
	$('input[name="purchaseIdChk"]').each(function(){ 
		hiddenStr += '<input type="hidden" name="purchaseIdChk" value="'+$(this).val()+'" />';
	});
	if(hiddenStr==""){
		alert("请添加产品，再点击保存!");
		return false;
	}
	
	$('#purchaseDetailAddfrm').submit();
}

function updateForm(){
	var hiddenStr = '';
	$('input[name="purchaseIdChk"]').each(function(){ 
		hiddenStr += '<input type="hidden" name="purchaseIdChk" value="'+$(this).val()+'" />';
	});
	if(hiddenStr==""){
		alert("请添加产品，再点击保存!");
		return false;
	}
	
	$('#purchaseDetailEditfrm').submit();
}

//批量删除
function batchDeleteRows(){
	var chkObj = $('input[name="purchaseIdChk"]:checked');
	if(chkObj.length==0){
   		alert('请勾选要删除的产品!')
   		return;
   	}
	
	if(confirm('确定删除已选产品吗？')){
		var chk_value =[]; 
		var html = "";
		$('input[name="purchaseIdChk"]:checked').each(function(){ 
		   chk_value.push($(this).val());
		   $(this).parent().parent().remove();
		}); 
	}
}

function closeOpenSku(params){
	
	var productId;
	var productTitle;
	var productNo;
	var skuId;
	var productSkuCode;

	var sizeI = params.length;
	var count;
	for(var i=0;i<sizeI;i++){
		count = parseInt($('#count').val())+parseInt(i);
		var str = params[i];
		productId = str[2].trim();
		productTitle = str[5].trim();
		productNo = str[6].trim();
		skuId = str[0].trim();
		productSkuCode = str[1].trim();

		var html = '<tr><td align="center" width="5%"><input type="checkbox" name="purchaseIdChk" />';
		html += '<input type="hidden" name="purchaseDetail['+count+'].str_productId" value="'+productId+'"/>'
	    		+ '<input type="hidden" name="purchaseDetail['+count+'].str_skuId"  value="'+skuId+'" />'
	    		+ '<input type="hidden" name="purchaseDetail['+count+'].productNo"  value="'+productNo+'" />'
	    		+ '<input type="hidden" name="purchaseDetail['+count+'].productName"  value="'+productTitle+'" />'
	    		+ '<input type="hidden" name="purchaseDetail['+count+'].artNo"  value="'+productSkuCode+'" /></td>';
		html += '<td align="center">'+productSkuCode+'</td>';
		html += '<td align="center">'+productTitle+'</td>';
//		html += '<td align="center">'+productNo+'</td>';
		html += '<td align="center"><input size="10" onkeyup="value=value.replace(/[^0-9]/g,\'\')" type="text" name="purchaseDetail['+count+'].str_quantity" reg="^((?!0)\\d{1,6}|1000000)$"  tip="请输入1-1000000的整数" maxlength="7" /></td>';
		html += '<td align="center"><input size="10" type="text" name="purchaseDetail['+count+'].str_purPrice" reg="^((?!0)[0-9]+\.?\\d{0,6}|(0)\.{1}\\d{1,6}|1000000)$"  tip="价格范围[1-1000000]" maxlength="7"/></td>';
		html += '<td align="center"><textarea name="purchaseDetail['+count+'].remark" style="width: 180px; height: 23px;" cols="10" wrap="physical" maxlength="80" tip="备注不要超过80个汉字" ></textarea></td>';
		
		var hiddenStr = '<input type="hidden" name="productIds" value="'+productId+'" />';
		hiddenStr+='<input type="hidden" name="skuIds" value="'+skuId+'" />';
		$('#purchaseDetailAddfrm').append(hiddenStr);
		
		//alert(html);
		$('#dataTable').append(html);	
	}
	closeThis();
	
}

// 18.改变仓库后，清除原来的产品SKU信息
function clearProductInfo() {
	// 删除<tr/>
	var checked = $("input[type='checkbox'][name='purchaseIdChk']");
	var _len = $("#dataTable tr").length;// 删除之前行数

	$(checked).each(function() {
		$(this).parent().parent().remove();// 移除选中所在行(与上面方法不同在于，不管选没选中，都清除)
	});

	var _lenAfter = $("#dataTable tr").length;// 删除之后的行数
	if (_lenAfter < 4) {// 其中4=原有行数,如果小于4表示删除了所有，就自动添加一行
		addRowsFun();
	}
	this.changeNameIdOnId(_len);
}

//19.获得经手人姓名
function changeUserName(){
	var _userName  = $("#userId option:selected").text();//获取文本
	$("#userName").val(_userName);
}

