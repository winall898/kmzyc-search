/*
 * 出库单和出库明细单相关操作的JS
 * @author luoyi
 * @createDate 2013/08/15
 */

// 1.全选事件
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

// 2.出库单列表:根据条件查询出库单
function searchStockOut() {
	document.getElementById('pageNo').value = 1;
	document.forms['stockOutfrm'].submit();
}

// 3.出库单列表:到出库单详情页面
function gotoStockOutDetail(stockOutId) {
	var  pageNum=1;
	document.forms[0].action = "/app/toStockOutDetail.action?stockOutId="+stockOutId+"&pageNum="+pageNum;
	document.forms[0].submit();
}

// 4.出库单列表:到出库单列表
function gotoqueryStockOutList() {
	document.forms[0].action= "/app/findStockOutList.action";
	document.forms[0].submit();
}
// 5.出库单列表:添加出库单
function toAddStockOut() {
	location.href = "/app/toAddStockOutDetail.action";
}

// 6.根据sku查找商品资料
function ValidateProductInfo(skuId,type) {
	if(type==1){//手输SkuCode查询
		var $skuIds = $(skuId);
		var id = "#" + $skuIds.attr("id");// 传递过来的input的ID
	}else{//通过下拉框选择的SkuCode查询
		var id = "#" + skuId;// 传递过来的input的ID
	}


	var _productSkuId = $(id).val();
	_productSkuId = $.trim(_productSkuId);//jquery去除空格(IE和火狐支持)
	if (_productSkuId.length <= 0) {
		return false;
	}
	
	
	//先看有没有选择仓库
	var warehouseValue = $("#warehouseId1").val();
		if (warehouseValue=="") {   
            alert("提示:请选择仓库!"); 
            $(id).val("");
            return false;   
     }

	// 再判断是否已存在此SKU
	var _len = $("#dataTable tr").length;// 行数
	var reg = /^\d+$/;
	
	var num = id.substring(6, id.length);// 其中的6=stkuId字符串的长度

	// 绑定数据到采购单详情行的值
	var skuid = "#skuId" + num;//skuCode的id
	var productSkuid = "#productSkuId" + num;
	var productId = "#productId" + num;
	var productNameId = "#productName" + num;
	var priceId = "#price" + num;
	var productNoId = "#productNo" + num;
	var quantityId = "#quantity" + num;
	var remarkId = "#remark" + num;
     var productNameHideId="#productNameHide"+num;  // 隐藏的产品名称id
     var productNoHideId="#productNoHide"+num;    //隐藏的产品编号id

	// 根据skuId验证输入的sku数值，是否已经存在
	for ( var i = 3; i < _len + 3; i++) {
		var _skuId = "#skuId" + i;
		if (_skuId != id) {
			var _productSkuValue = $(_skuId).val();
			if (_productSkuId == _productSkuValue) {
				alert("该SKU已存在！");
				$(skuid).val("");
				$(productSkuid).val("");
				$(productId).val("");
				$(productNameId).val("");
				$(priceId).val("");
				$(productNoId).val("");
				$(quantityId).val("");
				
				$(remarkId).val("");
				return false;
			}
		}
	}


	if (_productSkuId != "") {// 如果sku不为空，且为数字
		var settings = {
			type : "POST",
			url : "/app/findProductInfoBySku.action?productSkuCode="
					+ _productSkuId +"&warehouseId="+warehouseValue,
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
					$(skuid).val(productSkuCodeValue);//产品skuCode值
					$(productSkuid).val(productSkuIdValue);
					$(productId).val(productIdValue);
					$(productNameId).attr("disabled","true");
					$(productNameId).val(productName);
					$(productNameHideId).val(productName);
					$(priceId).val(price);
					$(productNoId).attr("disabled","true");
					$(productNoHideId).val(productNo);
					$(productNoId).val(productNo);
				//	$(remarkId).val("");
				} else {// 无资料，则清空
					$(skuid).val("");
					$(productSkuid).val("");
					$(productId).val("");
					$(productNameId).val("");
					$(priceId).val("");
					$(productNoId).val("");
					$(remarkId).val("");
					alert("sku查询资料不存在!");
				}
			}
		};
		$.ajax(settings);
	}
}

// 7.添加出库单一行
function addRowsFun() {
	var _len = $("#dataTable tr").length;
	var pdLen = _len - 3;// 用于设置行中stockOutDetailList中列的name，其中3=原有行数(4)-1=3
	$("#dataTable")
			.append(
					"<tr id='stockOut"
							+ _len
							+ "' align='center'>"
							+ "<td align='center' width='5%'>"
							+ "<input id='productSkuId"
							+ _len
							+ "' type='hidden' name='stockOutDetailList["
							+ pdLen
							+ "].productSkuId'  /><input id='productId"
							+ _len
							+ "' type='hidden' name='stockOutDetailList["
							+ pdLen
							+ "].productId'  />"
							+ "<input type='checkbox' name='stockOutIdChk'  /></td>"
							+ "<td align='center'><input size='20' id='skuId"
							+ _len
							+ "' onblur='return ValidateProductInfo(this,1)' reg ='^.+$' tip='请正确输入SKU码' type='text' name='stockOutDetailList["
							+ pdLen
							+ "].productSkuValue' maxlength='20'/>" 
							+ "<img title='详情' style='cursor: pointer;' src='/etc/images/view.png'  onclick='popSelectProductSku(this.parentNode.parentNode.id);' />"
							+ "</td>"
							+ "<td align='center'><input size='20' id='productName"
							+ _len
							+ "' type='text' name='stockOutDetailList["
							+ pdLen
							+ "].productName' disabled='true'        readonly='readonly'/><input type='hidden'  id='productNameHide"+_len+"' name='stockOutDetailList["+pdLen+"].productName' ></td>"
							+ "<td align='center'><input size='10'  id='quantity"
							+ _len
							+ "' type='text' name='stockOutDetailList["
							+ pdLen
							+ "].quantity' onkeyup=\"value=value.replace(/[^0-9]/g,'')\" onblur='checkProductStock(this);' reg='^((?!0)\\d{1,6}|1000000)$'  tip='请输入1-1000000的整数' maxlength='7'/></td>"
							+ "<td align='center'><input size='10' id='price"
							+ _len
							+ "' type='text' name='stockOutDetailList["
							+ pdLen
							+ "].price' reg='^((?!0)[0-9]+\\.?\\d{0,6}|(0)\\.{1}\\d{1,6}|1000000)$' tip='价格范围[1-1000000]' maxlength='7'/></td>"
							+ "<td align='center'><input size='10' id='productNo"
							+ _len
							+ "' type='text' name='stockOutDetailList["
							+ pdLen
							+ "].productNo' disabled='true'          readonly='readonly'/><input type='hidden'  id='productNoHide"+_len+"' name='stockOutDetailList["+pdLen+"].productNo' ></td>"
							+ "<td align='center'><input size='10' id='barCode"
							+ _len
							+ "' type='text' name='stockOutDetailList["
							+ pdLen
							+ "].barCode' /></td>"
							+ "<td align='center'>"
							+ "<textarea id='remark"
							+ +_len
							+ "' name='stockOutDetailList["
							+ pdLen
							+ "].remark' style='width: 180px; height: 23px;' cols='10' wrap='physical' maxlength='200' reg='^(\\S{0,100})$' tip='备注不要超过100个汉字' ></textarea>"
							+ "</td>" + "</tr>");

	//	重新加载js,错误提示用的
	var oHead = document.getElementsByTagName('HEAD').item(0);
    var oScript= document.createElement("script");
    oScript.type = "text/javascript";
    oScript.src="/etc/js/validate/easy_validator.pack.js";
    oHead.appendChild(oScript);

}

// 8.删除出库单一行
function delRowsFun() {
	// 删除<tr/>
	var checked = $("input[type='checkbox'][name='stockOutIdChk']");
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
		return;
	}
	// 循环所有行，进行name和id的更改
	this.changeNameIdOnId(_len);
}

// 9.循环所有行，进行name和id的更改
function changeNameIdOnId(_len) {// 传一个总行数过来

	// 接下来对所有行，进行name和id的value更改
	// 第一步:先将name的值全部更改
	for ( var i = 3; i <= _len + 1; i++) {
		var trId = "stockOut" + i; // 行号tr的id

		// 如果能找到tr的id=trId的。
		if (document.getElementById(trId) != null) {
			var trIndex = document.getElementById(trId).rowIndex;// tr在表格哪一行

			var index = trIndex - 3;// 所在行号

			// 第一步:给行号改id的值
			document.getElementById(trId).id = "stockOut" + trIndex;// 给行号改id

			// 第二 步:给input的name改值
			
			var detailid = "detailId" + i;// 细目id
			var skuid = "skuId" + i;// 产品SkuCode码所在的input.ID
			var productSkuid = "productSkuId" + i;// 产品Sku码所在的input.ID
			var productNameid = "productName" + i;// 产品名称id
			var productid = "productId" + i;// 产品名称id
			var quantityid = "quantity" + i;// 产品数量id
			var priceid = "price" + i;// 产品价格id
			var productNoid = "productNo" + i;// 产品编号id
			var barCodeid = "barCode" + i;// 产品货号id
			var remarkid = "remark" + i;// 产品货号id
           var productNameHideid="productNameHide"+i;  //隐藏的产品名称id
           var productNoHideid="productNoHide"+i  ;//隐藏的产品编号id
			
			
			
			// 给input的name赋值
			if (null != document.getElementById(detailid)) {//新增的无此ID
				document.getElementById(detailid).name = "stockOutDetailList["
					+ index + "].detailId";
			}
			document.getElementById(productSkuid).name = "stockOutDetailList[" + index
			+ "].productSkuId";
			document.getElementById(skuid).name = "stockOutDetailList[" + index
					+ "].productSkuValue";//给产品skuCode所在name赋值
			document.getElementById(productNameid).name = "stockOutDetailList["
					+ index + "].productName"; // 要赋值产品名称所在input的name
			document.getElementById(productid).name = "stockOutDetailList["
					+ index + "].productId"; // 要赋值产品名称所在input的name
			document.getElementById(quantityid).name = "stockOutDetailList["
					+ index + "].quantity"; // 要赋值产品名称所在input的name
			document.getElementById(priceid).name = "stockOutDetailList["
					+ index + "].price"; // 要赋值产品价格所在input的name
			document.getElementById(productNoid).name = "stockOutDetailList["
					+ index + "].productNo"; // 要赋值产品名称所在input的name
			document.getElementById(barCodeid).name = "stockOutDetailList["
					+ index + "].barCode"; // 要赋值产品名称所在input的name
			document.getElementById(remarkid).name = "stockOutDetailList["
					+ index + "].remark"; // 要赋值产品名称所在input的name

			document.getElementById(productNameHideid).name = "stockOutDetailList["
				+ index + "].productName"; // 要赋值隐藏产品名称所在input的name
			
			document.getElementById(productNoHideid).name = "stockOutDetailList["
				+ index + "].productNo"; // 要赋值隐藏产品编号所在input的name
			
			
			// 第三步:改input的id
			// 重新给input的id赋值
			if (null != document.getElementById(detailid)) {
				document.getElementById(detailid).id = "detailId" + trIndex;// 产品SKU码所在的input的ID
			}
			document.getElementById(productSkuid).id = "productSkuId" + trIndex;// 产品SKU码所在的input的ID
			document.getElementById(skuid).id = "skuId" + trIndex;// 产品SKU码所在的input的ID
			document.getElementById(productid).id = "productId" + trIndex;// 产品名称id
			document.getElementById(productNameid).id = "productName" + trIndex;// 产品名称id
			document.getElementById(quantityid).id = "quantity" + trIndex;// 产品数量id
			document.getElementById(priceid).id = "price" + trIndex;// 产品price的id
			document.getElementById(productNoid).id = "productNo" + trIndex;// 产品productNo的id
			document.getElementById(barCodeid).id = "barCode" + trIndex;// 产品barCode的id
			document.getElementById(remarkid).id = "remark" + trIndex;// 产品remark的id
			
			document.getElementById(productNameHideid).id = "productNameHide" + trIndex;// 产品remark的id
			document.getElementById(productNoHideid).id = "productNoHide" + trIndex;// 产品remark的id
		}

	}
}

// 10.删除未审核的出库单
function deleteUnCheckedStockOut() {
	var str = document.getElementsByName("stockOutChk");
	var objarray = str.length;
	var chestr = "";

	for (i = 0; i < objarray; i++) {// 遍历数组
		if (str[i].checked == true)// 如果选中，开始获得所选择的出库单ID
		{
			chestr += str[i].value + ",";
		}
	}
	if (chestr.length < 1) {
		alert("请选择要删除的出库单!");
		return false;
	}

	var answer = confirm("确认删除所选出库单吗?");
	if (!answer) {
		return false;
	}

	// 确认删除
	$.ajax({
		type : "POST",
		url : "/app/deleteStockOutById.action?ajax=yes&stockOutIdArray="
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
				// 再查询一次订出库单列表
				location.href = "/app/findStockOutList.action";
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

// 11.到出库单详情修改页面
function toEditStockOutDetail(stockOutId) {
	location.href = "/app/toEditStockOutDetail.action?optionType=editStockDetail&stockOutId=" + stockOutId;
}

// 12.出库单审核:出库单审核功能
function gotoCheckStockOut() {
	var str = document.getElementsByName("stockOutChk");
	var objarray = str.length;
	var chestr = "";

	for (i = 0; i < objarray; i++) {// 遍历数组
		if (str[i].checked == true)// 如果选中，开始获得所选择的出库单ID
		{
			chestr += str[i].value + ",";
		}
	}
	if (chestr.length < 1) {
		alert("请选择要审核的出库单!");
		return false;
	}

	var answer = confirm("确认审核吗?");
	if (!answer) {
		return false;
	}
	// 发送ajax请求到action
	var settings = {
		type : "POST",
		url : "/app/checkedStockOut.action?stockOutIdArray=" + chestr,
		dataType : "text",
		error : function(data) {
			//解决IE浏览器加载课件时的JSON对象错误问题
			if(typeof JSON == 'undefined'){
                $('head').append($("<script type='text/javascript' src='/etc/js/warehouse/json2.js'>"));
			}
			var msg = JSON.parse(data);
			alert(msg.message);
		},
		success : function(data) {
			//解决IE浏览器加载课件时的JSON对象错误问题
			if(typeof JSON == 'undefined'){
                $('head').append($("<script type='text/javascript' src='/etc/js/warehouse/json2.js'>"));
			}
			var msg = JSON.parse(data);
			alert(msg.message);
			// 再查询一次审核列表
			location.href = "/app/toStockOutCheck.action";
		}
	};
	$.ajax(settings);
}

// 13.出库单审核:到出库单详情页面
function toCheckStockOutDetail(stockOutId) {
	var  pageNum=1;
	document.forms[0].action = "/app/toCheckStockOutDetail.action?stockOutId="
			+ stockOutId+"&pageNum="+pageNum;
	document.forms[0].submit();
}

// 14.到出库单审核列表
function toStockOutCheck() {
	document.forms[0].action = "/app/toStockOutCheck.action";
	document.forms[0].submit();
}

// 15.查找所属仓库的所有sku产品
function popSelectProductSku(trId) {
	var warehouseValue = $("#warehouseId1").val();
	if (warehouseValue == "") {
		alert("提示:请选择仓库!");
		return false;
	}

	popDialog("/app/findAllSkuProductByPurchase.action?type=stock&warehouseId="+warehouseValue+"&trId="
		+ 0,"查看所有SKU商品",700,420);	
	
}

// 16.查找sku产品后，回来的参数值
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
		productTitle = str[4].trim();
		productNo = str[3].trim();
		skuId = str[0].trim();
		productSkuCode = str[1].trim();

		var html = '<tr><td align="center" width="5%"><input type="checkbox" name="stockOutIdChk" />';
		html += '<input type="hidden" name="stockOutDetailList['+count+'].str_productId" value="'+productId+'"/>'
	    		+ '<input type="hidden" name="stockOutDetailList['+count+'].str_skuId"  value="'+skuId+'" />'
	    		+ '<input type="hidden" name="stockOutDetailList['+count+'].productNo"  value="'+productNo+'" />'
	    		+ '<input type="hidden" name="stockOutDetailList['+count+'].productName"  value="'+productTitle+'" />'
	    		+ '<input type="hidden" name="stockOutDetailList['+count+'].productSkuValue"  value="'+productSkuCode+'" /></td>';
		html += '<td align="center">'+productSkuCode+'</td>';
		html += '<td align="center">'+productTitle+'</td>';
		html += '<td align="center"><input size="10" onkeyup="value=value.replace(/[^0-9]/g,\'\')" type="text" name="stockOutDetailList['+count+'].str_quantity" reg="^((?!0)\\d{1,6}|1000000)$"  tip="请输入1-1000000的整数" maxlength="7" /></td>';
		html += '<td align="center"><input size="10" type="text" name="stockOutDetailList['+count+'].str_stockOutPrice" reg="^((?!0)[0-9]+\.?\\d{0,6}|(0)\.{1}\\d{1,6}|1000000)$"  tip="价格范围[1-1000000]" maxlength="7"/></td>';
		html += '<td align="center"><textarea name="stockOutDetailList['+count+'].remark" style="width: 180px; height: 23px;" cols="10" wrap="physical" maxlength="80" tip="备注不要超过80个汉字" ></textarea></td>';
		
		var hiddenStr = '<input type="hidden" name="productIds" value="'+productId+'" />';
		hiddenStr+='<input type="hidden" name="skuIds" value="'+skuId+'" />';
		$('#stockOutDetailListAddfrm').append(hiddenStr);
		
		//alert(html);
		$('#dataTable').append(html);	
	}
	closeThis();
	
}

//17.改变仓库后，清除原来的产品SKU信息
function clearProductInfo() {
	// 删除<tr/>
	var checked = $("input[type='checkbox'][name='stockOutIdChk']");
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

//18.获得经手人姓名
function changeUserName(){
	var _userName  = $("#userId option:selected").text();//获取文本
	$("#userName").val(_userName);
}

//19.根据所在行的skuCode和仓库ID，ajax进行库存验证
function checkProductStock(quantityId){	
	var id ="#" + $(quantityId).attr("id");// 传递过来数量的input的ID
	var stockNums = $(id).val();//获得数量的值
	var idNo = id.substr(9,id.length);//取得行号9=#quantity的长度
	
	var productSkuId = "#productSkuId"+idNo;//skuId
	var skuIdValue= $(productSkuId).val();//获得skuCodeValue的值
	
	var warehouseId = $("#warehouseId1").val();//仓库的ID
	
	if(stockNums.length <=0|| stockNums ==0){//数量为或空
		$(id).val("");
		return false;
	}
	
	if (skuIdValue.length <= 0) {// 空字符串
		return false;
	}
	if (warehouseId.length <= 0) {// 仓库未选
		return false;
	}
	//开始发送ajax请求,进行仓库库存数量校验
	var settings = {
			type : "POST",
			url : "/app/checkProductStockByWarehouse.action?productSkuId="
					+ skuIdValue + "&warehouseId=" + warehouseId+"&stockNums="+stockNums,
			dataType : "json",
			error : function(data) {
				alert("查询仓库的库存有误!");
			},
			success : function(data) {
				if(false==data.result){
					var stockNums = $(id).val();//获得数量的值
					alert("产品的出库数量"+stockNums+"大于所属仓库可用库存数量"+data.message+"!");
					 $(quantityId).val(data.message);
				}
			}
		};
		$.ajax(settings);
}

function submitForm(){
	var hiddenStr = '';
	$('input[name="stockOutIdChk"]').each(function(){ 
		hiddenStr += '<input type="hidden" name="stockOutIdChk" value="'+$(this).val()+'" />';
	});
	if(hiddenStr==""){
		alert("请添加产品，再点击保存!");
		return false;
	}
	
	$('#stockOutDetailListAddfrm').submit();
}

function updateForm(){
	var hiddenStr = '';
	$('input[name="stockOutIdChk"]').each(function(){ 
		hiddenStr += '<input type="hidden" name="stockOutIdChk" value="'+$(this).val()+'" />';
	});
	if(hiddenStr==""){
		alert("请添加产品，再点击保存!");
		return false;
	}
	
	$('#stockOutDetailListEditfrm').submit();
}

//批量删除
function batchDeleteRows(){
	var chkObj = $('input[name="stockOutIdChk"]:checked');
	if(chkObj.length==0){
   		alert('请勾选要删除的产品!')
   		return;
   	}
	
	if(confirm('确定删除已选产品吗？')){
		var chk_value =[]; 
		var html = "";
		$('input[name="stockOutIdChk"]:checked').each(function(){ 
		   chk_value.push($(this).val());
		   $(this).parent().parent().remove();
		}); 
	}
}
