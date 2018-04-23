/*
 * 入库单和入库详情单相关操作的JS
 * @author ljh
 */


// 6.添加采购单一行
function addRowsFun() {
	var _len = $("#dataTable tr").length;
	var pdLen = _len - 3;// 用于设置行中list中列的name，其中3=原有行数(4)-1=3
	$("#dataTable")
			.append(
					"<tr id=purchase"
							+ _len
							+ " align='center'>"
							+ "<td align='center' width='5%'><input type='checkbox' name='purchaseIdChk'  /></td>"
							+ "<td align='center'><input  id='productSkuValue"
							+ _len
							+ "'    tip='请输入SKU码'  width='20%'   reg='^.+$'      onblur='return ValidateProductInfo(this,1)'      type='text' name='list["
							+ pdLen
							+ "].productSkuValue' /><img title='查找' style='cursor: pointer;' src='/etc/images/view.png'  onclick='popSelectProductSku(this.parentNode.parentNode.id);' /></td>"
							+"<td align='center'><input size='20'  id='showProductName"  
							+ _len
							+ "' type='text' disabled='true'        name='showProductName' id='showProductName"+_len+"' /></td>"
							+"<input type='hidden'  id='productName"+_len
							+"'   name='list["+pdLen+"].productName' />"
							+"<input type='hidden'  id='productId"+_len
							+"'   name='list["+pdLen+"].productId' />"
							+"<input type='hidden'  id='productSkuId"+_len+
							"'   name='list["+pdLen+"].productSkuId' />"
							+"<input type='hidden'  id='productNo"+_len+
							"'   name='list["+pdLen+"].productNo' />"
							+"<td align='center'><input size='10'  id='quantity"
							+ _len
							+ "' type='text' name='list["
							+ pdLen
							+ "].quantity'      reg='^((?!0)\\d{1,5}|100000)$'  tip='请输入1-100000的整数'              onkeyup=\"value=value.replace(/[^0-9]/g,'')\"             /></td>"
							+ "<td align='center'><input size='10' id='price"
							+ _len
							+ "' type='text'                     name='list["
							+ pdLen
							+ "].price'   maxlength='7'     reg='^((?!0)[0-9]+\\.?\\d{0,6}|(0)\\.{1}\\d{1,6}|1000000)$' tip='范围[1-1000000]'           /></td>"
							+ "<td align='center'  colspan='3'>"
							+ "<input id='remark"+
							+_len
							+ "' name='list["
							+ pdLen
							+ "].remark' style='width: 180px; height: 23px;' size='20'  type='text'  wrap='physical'   tip='备注不要超过100个汉字'     maxlength='200'     reg='^(\\S{0,100})$'     ></input>"
							+ "</td>" +"</tr>");



//	重新加载js,验证出现错误提示用的
	var oHead = document.getElementsByTagName('HEAD').item(0);
    var oScript= document.createElement("script");
    oScript.type = "text/javascript";
    oScript.src="/etc/js/validate/easy_validator.pack.js";
    oHead.appendChild(oScript);




}


// 7.删除入库单一行
function delRowsFun() {
	// 删除<tr/>
	var checked = $("input[type='checkbox'][name='purchaseIdChk']");
	var _len = $("#dataTable tr").length;// 删除之前行数
	if (_len > 4) {
		checked.each(function() {
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

// 改变name\id\和onclick
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
			var detailId = "detailId" + i;
           var productSkuValue="productSkuValue"+i;
			var productSkuId = "productSkuId" + i;
			var quantity = "quantity" + i;
			var  price="price"+i;
			var remark = "remark" + i;
           var productId="productId"+i;
           var productName="productName"+i;
			var productNo="productNo"+i;
			var showProductName="showProductName"+i;
			
			
			// 给input的name赋值
			if(null!=document.getElementById(detailId)){
				document.getElementById(detailId).name = "list[" + index + "].detailId";
			}
			
			document.getElementById(productSkuValue).name = "list[" + index + "].productSkuValue";
			
			document.getElementById(productSkuId).name = "list[" + index
					+ "].productSkuId";
			document.getElementById(quantity).name = "list["
					+ index + "].quantity"; 
			document.getElementById(price).name = "list["
					+ index + "].price"; 
			document.getElementById(remark).name = "list[" + index
					+ "].remark"; 

			document.getElementById(productId).name = "list[" + index
			+ "].productId"; 
			
			document.getElementById(productName).name = "list[" + index
			+ "].productName"; 

			document.getElementById(productNo).name = "list[" + index
			+ "].productNo"; 

			
			// 第三步:改input的id
			// 重新给input的id赋值
			if(null!=document.getElementById(detailId)){
				document.getElementById(detailId).id = "detailId" + trIndex;
			}
			document.getElementById(productSkuValue).id = "productSkuValue" + trIndex;
			document.getElementById(productSkuId).id = "productSkuId" + trIndex;
			document.getElementById(quantity).id = "quantity" + trIndex;
			document.getElementById(price).id = "price" + trIndex;
			document.getElementById(remark).id = "remark" + trIndex;
			document.getElementById(productId).id = "productId" + trIndex;
			document.getElementById(productName).id = "productName" + trIndex;
			document.getElementById(productNo).id = "productNo" + trIndex;
			//修改灰色的产品名称的id
			document.getElementById(showProductName).id="showProductName"+trIndex;
			
		}
	}
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

//打开未入库的采购单列表	
function getPurchaseOrder(stockInType){
	var $stockInType = $(stockInType);
	var id = "#" + $stockInType.attr("id");// 传递过来的input的ID
	if($(id).val()==1){//1代表是"采购入库"
		dialog("查看所有未入库的采购单", "iframe:/app/findAllPurchaseOrders.action" , "700px", "420px", "iframe", 50);
	}else{//其他入库
		location.href="/app/goToPurchaseInDetail.action" ;
	}
}	

//关闭采购单列表
function closePurchaseInfos(purchaseId, purchaseNo) {
	closeThis();
	//查询采购单详情,供入库单使用
	location.href = "/app/queryPurchaseDetailForStockIn.action?purchaseId="+ purchaseId;
}

//验证入库数量不能大于采购单上的数量
function checkQuantity(stockInQuantity){
	var $stockInQuantity = $(stockInQuantity);
	var id = "#"+$stockInQuantity.attr("id");//获取到所选行的ID
	var num = id.substr(9,id.length);//9=#quantity
	var purchaesQuantityId = "#purchaesQuantity"+num;//所在行的采购单ID

	if(parseInt($(id).val())>parseInt($(purchaesQuantityId).val())){
		$(id).val($(purchaesQuantityId).val());
		alert("入库单的数量不能大于采购数量!");
	}
}

function submitForm(){
	var hiddenStr = '';
	$('input[name="stockInIdChk"]').each(function(){ 
		hiddenStr += '<input type="hidden" name="stockInIdChk" value="'+$(this).val()+'" />';
	});
	if(hiddenStr==""){
		alert("请添加产品，再点击保存!");
		return false;
	}
	
	$('#stockInDetailAddfrm').submit();
}

function updateForm(){
	var hiddenStr = '';
	$('input[name="stockInIdChk"]').each(function(){ 
		hiddenStr += '<input type="hidden" name="stockInIdChk" value="'+$(this).val()+'" />';
	});
	if(hiddenStr==""){
		alert("请添加产品，再点击保存!");
		return false;
	}
	
	$('#stockDetailEditfrm').submit();
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
		productTitle = str[4].trim();
		productNo = str[3].trim();
		skuId = str[0].trim();
		productSkuCode = str[1].trim();

		var html = '<tr><td align="center" width="5%"><input type="checkbox" name="stockInIdChk" />';
		html += '<input type="hidden" name="list['+count+'].str_productId" value="'+productId+'"/>'
	    		+ '<input type="hidden" name="list['+count+'].str_skuId"  value="'+skuId+'" />'
	    		+ '<input type="hidden" name="list['+count+'].productNo"  value="'+productNo+'" />'
	    		+ '<input type="hidden" name="list['+count+'].productName"  value="'+productTitle+'" />'
	    		+ '<input type="hidden" name="list['+count+'].productSkuValue"  value="'+productSkuCode+'" /></td>';
		html += '<td align="center">'+productSkuCode+'</td>';
		html += '<td align="center">'+productTitle+'</td>';
		html += '<td align="center"><input size="10" onkeyup="value=value.replace(/[^0-9]/g,\'\')" type="text" name="list['+count+'].str_quantity" reg="^((?!0)\\d{1,6}|1000000)$"  tip="请输入1-1000000的整数" maxlength="7" /></td>';
		html += '<td align="center"><input size="10" type="text" name="list['+count+'].str_stockInPrice" reg="^((?!0)[0-9]+\.?\\d{0,6}|(0)\.{1}\\d{1,6}|1000000)$"  tip="价格范围[1-1000000]" maxlength="7"/></td>';
		html += '<td align="center"><textarea name="list['+count+'].remark" style="width: 180px; height: 23px;" cols="10" wrap="physical" maxlength="80" tip="备注不要超过80个汉字" ></textarea></td>';
		
		var hiddenStr = '<input type="hidden" name="productIds" value="'+productId+'" />';
		hiddenStr+='<input type="hidden" name="skuIds" value="'+skuId+'" />';
		$('#stockInDetailAddfrm').append(hiddenStr);
		
		//alert(html);
		$('#dataTable').append(html);	
	}
	closeThis();
	
}

//批量删除
function batchDeleteRows(){
	var chkObj = $('input[name="stockInIdChk"]:checked');
	if(chkObj.length==0){
   		alert('请勾选要删除的产品!')
   		return;
   	}
	
	if(confirm('确定删除已选产品吗？')){
		var chk_value =[]; 
		var html = "";
		$('input[name="stockInIdChk"]:checked').each(function(){ 
		   chk_value.push($(this).val());
		   $(this).parent().parent().remove();
		}); 
	}
}

//19.获得经手人姓名
function changeUserName(){
	var _userName  = $("#userId option:selected").text();//获取文本
	$("#userName").val(_userName);
}

//16.查找所有sku产品
function popSelectProductSku() {
	var warehouseValue = $("#warehouseId1").val();
	if (warehouseValue == "") {
		alert("提示:请选择仓库!");
		return false;
	}
	
	popDialog("/app/findAllByCondition.action?type=stock"+"&trId=" + 0,"查看所有SKU商品",800,440);	
}

function gotoqueryPurInfoList() {
	location.href = "/app/quertyInList.action";
}



