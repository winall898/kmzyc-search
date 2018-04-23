var countries;
$(function(){
	$.post(
		'/app/findAllBrandForJson.action',	
		function(data){
			countries = data;
		},'text'
	);
});
