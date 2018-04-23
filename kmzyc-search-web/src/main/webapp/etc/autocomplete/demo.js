/*jslint  browser: true, white: true, plusplus: true */
/*global $, countries */

$(function () {
	var countriesArray;
	$.ajax({
		async:'false',
		url:'/app/findAllBrandForJson.action',
		success:function(data){
			countriesArray = $.map(data, function (value, key) { return { value: value, data: key }; });
			product_add_countriesArray = countriesArray;
			$('#autocomplete').autocomplete({
		    	lookup: countriesArray,
		        minChars: 0
		    });
		},
		dataType:'json'
	});
	
	var suppliersArray;
	$.ajax({
		async:'false',
		url:'/app/findAllSuppliersForJson.action',
		success:function(data){
			suppliersArray = $.map(data, function (value, key) { return { value: value, data: key }; });
			product_add_suppliersArray = suppliersArray;
			$('#autocomplete_forSuppliers').autocomplete({
		    	lookup: suppliersArray,
		        minChars: 0
		    });
		},
		dataType:'json'
	});
	
});