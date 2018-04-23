/*
 * 配送单和配送明细单相关操作的JS
 * @author luoyi
 * @createDate 2013/08/20
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

// 2.配送单列表:根据条件查询配送单
function searchDistributionInfo() {
	document.getElementById('pageNo').value = 1;
	document.forms['distributionInfofrm'].submit();
}

// 3.配送单列表:到配送细目单列表
function searchDistribuDetails(distriInfoId) {
	document.forms[0].action = "/app/toDistributionDetails.action?distributionId="
			+ distriInfoId+"&pageNum=1";
	
	document.forms[0].submit();
}

//3.配送单审核:到配送细目单列表
function searchCheckDistribuDetails(distriInfoId) {
	document.forms[0].action = "/app/toDistributionDetails.action?showType=checkList&distributionId="
			+ distriInfoId+"&pageNum=1";
	document.forms[0].submit();
}


// 4.配送单列表:到配送单列表
function gotodistributionInfoList() {
	document.forms[0].action = "/app/toDistributionInfoList.action?pageNum=1";
	document.forms[0].submit();
}

// 5.配送单列表:到添加配送单页面
function toAdddistributionInfo() {
	location.href = "/app/toAddDistributionInfo.action";
}

// 6.查找所有sku产品
function popSelectProductSku(trId) {
	var warehouseValue = $("#warehouseId1").val();
	if (warehouseValue == "") {
		alert("提示:请选择仓库!");
		return false;
	}
	dialog("查看所有SKU商品",
			"iframe:/app/findAllSkuProductByPurchase.action?type=stock&warehouseId="+warehouseValue+"&trId="
					+ trId, "700px", "420px", "iframe", 50);
}

// 7.查找sku产品后，回来的参数值
function closeOpenSku(skuAttributeId, skuAttValue, productId, trId) {
	closeThis();
	var skuId = "skuId" + trId.substring(16, trId.length);// 16的长度=distributionInfo
	document.getElementById(skuId).value = skuAttValue;
	ValidateProductInfo(skuId, 0);
}

// 8.添加配送单一行
function addRowsFun() {
	var _len = $("#dataTable tr").length;
	var pdLen = _len - 3;// 用于设置行中distributionDetailInfoList中列的name，其中3=原有行数(4)-1=3
	$("#dataTable").append(
					"<tr id=distributionInfo"
							+ _len
							+ " align='center'>"
							+ "<td align='center' width='5%'><input id='productId"
							+ _len
							+ "' type='hidden' name='distributionDetailInfoList["
							+ pdLen
							+ "].productId'  />"
							+ "<input id='productSkuId"
							+ _len
							+ "' type='hidden' name='distributionDetailInfoList["
							+ pdLen
							+ "].productSkuId'  />"
							+ "<input type='checkbox' name='distributionInfoChk'  /></td>"
							+ "<td align='center'><input size='20' id='skuId"
							+ _len
							+ "'  type='text' name='distributionDetailInfoList["
							+ pdLen
							+ "].productSkuValue' reg ='^.+$' tip='请正确输入SKU码' maxlength='20'/>"
							+ "<td align='center'><input size='20' id='productName"
							+ _len
							+ "' type='text' name='distributionDetailInfoList["
							+ pdLen
							+ "].productName'   disabled='true'         readonly='readonly'/> <input type='hidden'  id='productNameHide"+_len+"' name='distributionDetailInfoList["+pdLen+"].productName' > </td>"
							+ "<td align='center'><input size='10' id='productNo"
							+ _len
							+ "' type='text' name='distributionDetailInfoList["
							+ pdLen
							+ "].productNo'    disabled='true'        readonly='readonly'/> <input type='hidden'  id='productNoHide"+_len+"' name='distributionDetailInfoList["+pdLen+"].productNo' >                            </td>"
							+ "<td align='center'><input size='15'  id='quantity"
							+ _len
							+ "' type='text' name='distributionDetailInfoList["
							+ pdLen
							+ "].quantity' onkeyup=\"value=value.replace(/[^0-9]/g,'')\" onblur='checkProductStock(this);' reg='^((?!0)\\d{1,6}|1000000)$'  tip='请输入1-1000000的整数' maxlength='7'/></td>"
							+ "<td align='center'><input size='10' id='price"
							+ _len
							+ "' type='text' name='distributionDetailInfoList["
							+ pdLen
							+ "].price' onchange='totalsum(this);'           reg='^((?!0)[0-9]+\\.?\\d{0,6}|(0)\\.{1}\\d{1,6}|1000000)$' tip='价格范围[1-1000000]' maxlength='7' /></td>"
							+ "<td align='center'><input size='10' id='sumId"
							+ _len  
							+ "' type='text'  disabled='true'       name='distributionDetailInfoList["
							+ pdLen
							+ "].sum' readonly='readonly' reg='^((?!0)[0-9]+\\.?\\d{0,8}|(0)\\.{1}\\d{1,8}|99999999)$' tip='价格范围[1-99999999]' maxlength='9'/><input type='hidden'  id='sumHide"+_len+"' name='distributionDetailInfoList["+pdLen+"].sum' ></td>"
							+ "<td align='center'>"
							+ "<textarea id='remark"
							+ _len
							+ "' name='distributionDetailInfoList["
							+ pdLen
							+ "].remark' style='width: 200px; height: 23px;' cols='10' wrap='physical' reg='^(\\S{0,100})$' tip='备注不要超过100个汉字' maxlength='200'></textarea>"
							+ "</td>"
							+ "</tr>");


	//	重新加载js,验证出现错误提示用的
	var oHead = document.getElementsByTagName('HEAD').item(0);
    var oScript= document.createElement("script");
    oScript.type = "text/javascript";
    oScript.src="/etc/js/validate/easy_validator.pack.js";
    oHead.appendChild(oScript);
    
}

// 9.删除配送单一行
function delRowsFun() {
	// 删除<tr/>
	var checked = $("input[type='checkbox'][name='distributionInfoChk']");
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

// 10.删除时改变name\id
function changeNameIdOnId(_len) {// 传一个总行数过来

	// 接下来对所有行，进行name和id的value更改
	// 第一步:先将name的值全部更改
	for ( var i = 3; i <= _len + 1; i++) {
		var trId = "distributionInfo" + i; // 行号tr的id

		// 如果能找到tr的id=trId的。
		if (document.getElementById(trId) != null) {
			var trIndex = document.getElementById(trId).rowIndex;// tr在表格哪一行

			var index = trIndex - 3;// 所在行号

			// 第一步:给行号改id的值
			document.getElementById(trId).id = "distributionInfo" + trIndex;// 给行号改id

			// 第二 步:给input的name改值
			var detailid = "detailId" + i;// 细目id
			var skuid = "skuId" + i;// 产品SkuCode码所在的input.ID
			var productSkuid = "productSkuId" + i;// 产品SKU码所在的input.ID
			var productNameid = "productName" + i;// 产品名称id
			var productid = "productId" + i;// 产品名称id
			var quantityid = "quantity" + i;// 产品数量id
			var productNoid = "productNo" + i;// 产品编号id
			var sumid = "sumId" + i;// 小计金额id
			var priceid = "price" + i;// 产品货号id
			var remarkid = "remark" + i;// 产品货号id
			var productNameHideid="productNameHide"+i;  // 隐藏的产品名称id
			var productNoHideid="productNoHide"+i; // 隐藏的产品编号id
			var  sumHideid="sumHide"+i; // 隐藏的产品小计id
 			
			
			

			// 给input的name赋值
			if (null != document.getElementById(detailid)) {
				document.getElementById(detailid).name = "distributionDetailInfoList["
						+ index + "].detailId";
			}
			document.getElementById(productSkuid).name = "distributionDetailInfoList["
					+ index + "].productSkuId";
			document.getElementById(skuid).name = "distributionDetailInfoList["
					+ index + "].productSkuValue";// 产品skuCode所在name
			document.getElementById(productid).name = "distributionDetailInfoList["
					+ index + "].productId"; // 要赋值产品名称所在input的name
			document.getElementById(productNameid).name = "distributionDetailInfoList["
					+ index + "].productName"; // 要赋值产品名称所在input的name
			document.getElementById(quantityid).name = "distributionDetailInfoList["
					+ index + "].quantity"; // 要赋值产品名称所在input的name
			document.getElementById(productNoid).name = "distributionDetailInfoList["
					+ index + "].productNo"; // 要赋值产品名称所在input的name
			document.getElementById(priceid).name = "distributionDetailInfoList["
					+ index + "].price"; // 要赋值purPrice所在input的name
			document.getElementById(sumid).name = "distributionDetailInfoList["
					+ index + "].sum"; // 要赋值purPrice所在input的name
			document.getElementById(remarkid).name = "distributionDetailInfoList["
					+ index + "].remark"; // 要赋值产品名称所在input的name
			
			
			document.getElementById(productNameHideid).name = "distributionDetailInfoList["
				+ index + "].productName"; // 要赋值产品名称所在input的name
			
			document.getElementById(productNoHideid).name = "distributionDetailInfoList["
				+ index + "].productNo"; // 要赋值产品名称所在input的name

			document.getElementById(sumHideid).name = "distributionDetailInfoList["
				+ index + "].sum"; // 要赋值产品名称所在input的name
			

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
			document.getElementById(priceid).id = "price" + trIndex;// 产品purPrice的id
			document.getElementById(sumid).id = "sumId" + trIndex;// 产品purPrice的id
			document.getElementById(remarkid).id = "remark" + trIndex;// 产品remark的
			
			document.getElementById(productNameHideid).id = "productNameHide" + trIndex;// 产品remark的
			document.getElementById(productNoHideid).id = "productNoHide" + trIndex;// 产品remark的
			document.getElementById(sumHideid).id = "sumHide" + trIndex;// 产品remark的
			

		}
	}
}

// 11.根据sku查找商品资料
function ValidateProductInfo(skuId, type) {
	if (type == 1) {// 手输SkuCode查询,传的是this
		var $skuIds = $(skuId);
		var id = "#" + $skuIds.attr("id");// 传递过来的input的ID
	} else {// 通过下拉框选择的SkuCode查询
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
            return false;   
     }


	// 再判断是否已存在此SKU
	var _len = $("#dataTable tr").length;// 行数
	var reg = /^\d+$/;

	var num = id.substring(6, id.length);// 其中的6=#skuId字符串的长度
	// 绑定数据到配送单详情行的值

	var productSkuId = "#productSkuId" + num;// skuId
	var skuId = "#skuId" + num;// skuValue
	var productId = "#productId" + num;
	var productNameId = "#productName" + num;
	var productNoId = "#productNo" + num;
	var priceId = "#price" + num;
	var sumId = "#sumId" + num;
	var quantityId = "#quantity" + num;

	var productNameHideId="#productNameHide"+num;   // 隐藏的产品名称id
	var productNoHideId="#productNoHide"+num;        // 隐藏的产品编号id
	var  sumHideId="#sumHide"+num;               // 隐藏的小计id
	
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
				$(sumId).val("");
				$(quantityId).val("");
				return false;
			}
		}
	}
	
	if (_productSkuId != "") {// 如果sku不为空
		var settings = {
			type : "POST",
			url : "/app/findProductInfoBySku.action?productSkuCode="
					+ _productSkuId+"&warehouseId="+warehouseValue,
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
					var productNo = data.productNo;
					var price = data.marketPrice; // 不含税单价

					var purPrice = data.costPrice;
					$(productSkuId).val(productSkuIdValue);
					
					$(productNameHideId).val(productName);
					$(productNoHideId).val(productNo);
					$(skuId).val(productSkuCodeValue);
					$(productId).val(productIdValue);
					$(productNameId).val(productName);
					$(productNoId).val(productNo);
					$(priceId).val(price);
				} else {// 无资料，则清空
					$(skuId).val("");
					$(productSkuId).val("");
					$(productId).val("");
					$(productNameId).val("");
					$(priceId).val("");
					$(productNoId).val("");
					$(sumId).val("");
					$(sumHideId).val("");
					alert("sku查询资料不存在!");
				}
			}
		};
		$.ajax(settings);
	}
}

// 12.求小计金额
function totalsum(colId) {
	var $colId = $(colId);
	var id = $colId.attr("id");// 数量的input的id

	// 获得所在的行号=quantity
	if (id.indexOf("quantity") == 0) {
		var rows = id.substring(8, id.length);
	} else {// 价格变动时price3
		var rows = id.substring(5, id.length);
	}

	var sumMoney = Math.round($("#quantity" + rows).val()
			* $("#price" + rows).val() * 100) / 100;

	$("#sumId" + rows).val(sumMoney);// 小数点后两位
	$("#sumHide"+rows).val(sumMoney); //给隐藏的小计赋值
}

// 13.删除未审核的配送单
function deleteDistrubution() {
	var str = document.getElementsByName("distributionInfoChk");
	var objarray = str.length;
	var chestr = "";
	for (i = 0; i < objarray; i++) {// 遍历数组
		if (str[i].checked == true)// 如果选中，开始获得所选择的配送单ID
		{
			chestr += str[i].value + ",";
		}
	}
	if (chestr.length < 1) {
		alert("请选择要删除的配送单!");
		return false;
	}
	var answer = confirm("确认删除所选配送单吗?");
	if (!answer) {
		return false;
	}
	// 确认删除
	$.ajax({
				type : "POST",
				url : "/app/deleteDistributionInfo.action?ajax=yes&distributionIdArray="
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
						// 再查询一次订配送单列表
						location.href = "/app/toDistributionInfoList.action";
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

// 14.ajax:批量审核
function toCheckDistrubution() {
	var str = document.getElementsByName("distributionInfoChk");
	var objarray = str.length;
	var chestr = "";

	for (i = 0; i < objarray; i++) {// 遍历数组
		if (str[i].checked == true)// 如果选中，开始获得所选择的配送单ID
		{
			chestr += str[i].value + ",";
		}
	}
	if (chestr.length < 1) {
		alert("请选择要审核的配送单!");
		return false;
	}

	var answer = confirm("确认审核吗?");
	if (!answer) {
		return false;
	}
	// 发送ajax请求到action
	var settings = {
		type : "POST",
		url : "/app/checkedDistributionInfo.action?distributionIdArray="
				+ chestr,
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
			location.href = "/app/distriListForCheck.action";
		}
	};
	$.ajax(settings);
}

// 15.配送单列表:到修改配送单页
function toEditdistributionInfoDetail(distributionId) {
	location.href = "/app/toEditDistributionInfo.action?distributionId="
			+ distributionId;
}
// 16.配送单列表:到修改配送单页
function todistributionInfoCheckDetail() {
	document.forms[0].action = "/app/distriListForCheck.action?pageNum=1";
	document.forms[0].submit();
}

//17.改变仓库后，清除原来的产品SKU信息
function clearProductInfo() {
	// 删除<tr/>
	var checked = $("input[type='checkbox'][name='distributionInfoChk']");
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

// 18.鼠标经过tr变色
function mytable(o, a, b, c, d) {
	var t = document.getElementById(o).getElementsByTagName("tr");
	for ( var i = 0; i < t.length; i++) {

		t[i].style.backgroundColor = (t[i].sectionRowIndex % 2 == 0) ? a : b;
		t[i].onclick = function() {
			for ( var i = 0; i < t.length; i++) {
				if (t[i] != this)
					t[i].x = "0";
				t[i].style.backgroundColor = (t[i].sectionRowIndex % 2 == 0) ? a
						: b;
			}
			if (this.x != "1") {
				this.x = "1";// 本来打算直接用背景色判断，FF获取到的背景是RGB值，不好判断
				this.style.backgroundColor = d;
			} else {
				this.x = "0";
				this.style.backgroundColor = (this.sectionRowIndex % 2 == 0) ? a
						: b;
			}
		}
		t[i].onmouseover = function() {

			if (this.x != "1")
				this.style.backgroundColor = c;
		}
		t[i].onmouseout = function() {
			if (this.x != "1")
				this.style.backgroundColor = (this.sectionRowIndex % 2 == 0) ? a
						: b;
		}
	}
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
					alert("产品的配送数量"+stockNums+"大于所属仓库库存数量"+data.message+"!");
					var stockNums = $(quantityId).val(data.message);
				}
				//检验完成数量后，进行金额计算
				totalsum(quantityId);
			}
		};
		$.ajax(settings);
		
}

