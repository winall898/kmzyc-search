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
							+"<td align='center'><input size='20'  id='productSkuId"  
							+ _len
							+ "' type='text'     readonly='readonly'          name='list["
							+ pdLen
							+ "].productSkuId' /></td>"
							+"<input type='hidden'  id='productId"+_len
							+"'   name='list["+pdLen+"].productId' />"
							+"<input type='hidden'  id='productName"+_len+
							"'   name='list["+pdLen+"].productName' />"
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
							+ "].remark' style='width: 180px; height: 23px;' size='20'    wrap='physical'   tip='备注不要超过100个汉字'     maxlength='200'     reg='^(\\S{0,100})$'     ></input>"
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
			
			var productName = "productName" + i;
			var quantity = "quantity" + i;
			var  price="price"+i;
			var remark = "remark" + i;
           var productId="productId"+i;
           var productName="productName"+i;
			var productNo="productNo"+i;
			
			
			
			// 给input的name赋值
			if(null!=document.getElementById(detailId)){
				document.getElementById(detailId).name = "list[" + index + "].detailId";
			}
			
			document.getElementById(productSkuValue).name = "list[" + index + "].productSkuValue";
			
			document.getElementById(productName).name = "list[" + index
					+ "].productName";
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
			document.getElementById(productName).id = "productName" + trIndex;
			document.getElementById(quantity).id = "quantity" + trIndex;
			document.getElementById(price).id = "price" + trIndex;
			document.getElementById(remark).id = "remark" + trIndex;
			document.getElementById(productId).id = "productId" + trIndex;
			document.getElementById(productName).id = "productName" + trIndex;
			document.getElementById(productNo).id = "productNo" + trIndex;
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
















