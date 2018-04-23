  $(document).ready(function(){
          $("#wrm").validate({
               rules: {
					"warehouseInfo.areaName":{required:true},
					"warehouseInfo.overlayArea":{required:true},
					"warehouseInfo.warehouseName":{required:true,maxlength:30,unusualChar:true},
					"warehouseInfo.warehouseNo":{required:true,maxlength:10,unusualChar:true},
					"warehouseInfo.depict":{maxlength:42,unusualChar:true},
					"warehouseInfo.remark":{maxlength:80,unusualChar:true}
	        	},
	           success: function (label){
	            label.removeClass("checked").addClass("checked");
	           }
          });
    });