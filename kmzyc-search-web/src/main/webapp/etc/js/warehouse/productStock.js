		
	function checkSku(){
		//if($('#skuAttValue').val()==""){
			//return;
		//}
		if($('#warehouseId option:selected').val()==''){
			$('#stockForm').submit();
			return false;
		}
		$.ajax({
			dataType:'json',
			url:'/app/checkSku.action?sku='+$('#skuAttValue').val()+'&warehouseId='+$('#warehouseId option:selected').val(),
			error:function(){alert('请求失败，请稍后重试或与管理员联系！')},
			success:function(date){
				if(date.result=='warehouseIdIsNot'){
					return false;
				}
				if(date.result==false){
					alert($('#warehouseId option:selected').text()+'已有sku为：'+$('#skuAttValue').val()+'的库存记录!');
					/*$('#skuAttValue').val('');
					$('#skuAttributeId').val('');
					$('#productName').val('');
					$('#productNo').val('');*/
					$('#skuAttValue').select();
					return false;
				}
				$('#stockForm').submit();
				/*else{
					var product = date.product;
					var productSku = date.sku;
					if(product==null || product.name==null){
						alert('sku:'+$('#skuAttValue').val()+'非法!');
						$('#skuAttValue').val('');
						$('#skuAttributeId').val('');
						$('#productName').val('');
						$('#productNo').val('');
						$('#skuAttValue').select();
					}
					$('#skuAttributeId').val(productSku.productSkuId);
					$('#productName').val(product.name);
					$('#productNo').val(product.productNo);
				}*/
			}
		});
	}

	function checkSelected(){
		if($('#warehouseId option:selected').val()==''){
			$('#warehouseId').focus();
			alert('仓库必须选择!');
			return false;
		}
		
	}
	
	
	function gotoList(){
	    //location.href="/basedata/gotoSysMain.action";
		if($('#stockType').val()!=null && $('#stockType').val()=='alarmlist'){
			$('#stockShowAlarmForm').submit();
		}else{
			$('#stockShowForm').submit();
		}
	}

	function gotoAdd(){
	    location.href="/app/toStockAdd.action";
	}

	function gotoUpdate(id){
		$('#frm').attr("action","/app/toStockUpdate.action?stock.stockId="+id);
		$('#frm').submit();
	    //location.href="/app/toStockUpdate.action?id="+id;
	}

	function gotoView(id){
		location.href="/app/toStockView.action?id="+id;
	}

	
	function gotoViewStock(id){
		$('#frm').attr("action","/app/toStockView.action?stock.stockId="+id);
		$('#frm').submit();
	}

	function doSearch(){
		document.getElementById('pageNo').value = 1;
		document.forms['frm'].submit();
	}
