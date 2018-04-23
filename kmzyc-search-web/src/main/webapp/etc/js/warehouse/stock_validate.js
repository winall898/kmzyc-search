  $(document).ready(function(){
          $("#stockForm").validate({
               rules: {
					"stock.warehouseId":{required:true},
					"stock.skuAttValue":{required:true,maxlength:45,unusualChar:true},
					"stock.alarmQuality":{maxlength:8,digits:true,min:0},
					"stock.stockQuality":{required:true,maxlength:8,unusualChar:true,numericalRatio2:true,digits:true,min:0},
					"stock.changeOrderQuatity":{maxlength:8,unusualChar:true,numberforinteger:true},
					"stock.remark":{maxlength:45,unusualChar:true}
	        	},
	           success: function (label){
	            label.removeClass("checked").addClass("checked");
	           }
          });
    });