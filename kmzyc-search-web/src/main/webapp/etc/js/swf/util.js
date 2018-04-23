  function editText(e){
	$("#"+e).hide();
	$("#"+e+"fm").show();
	$("#"+e+"fm").children()[0].focus(); 
  }
  function hideText(e){
	$("#"+e).hide();
	e=e.replace("fm","")
	$("#"+e).show();
  }

  function ajaxUtil( _datatype,_url,_data,_outdiv)
  {
	var isOk = true;
	var o = $("#"+_outdiv);
	$.ajax({
				  dataType: _datatype,
				  url: _url,
				  data: _data,				  
				  beforeSend: function(XMLHttpRequest){
						o.html("<font color='red'>正在处理中，请稍候...</font>");
				  },
				  success: function(responseText, textStatus){ 
				  		if(responseText=="1"){
							o.html("<font color='red'>操作成功！</font>");
						}else{
							o.html("<font color='red'>操作故障，请与系统管理员联系！</font>");
							isOk = false;
						}

				  },
				  complete: function(XMLHttpRequest, textStatus){             	 
				  },
				  error: function(){
					o.html("<font color='red'>操作故障，请与系统管理员联系！</font>");
					isOk = false;
				  }
			});
		return isOk;
  }

	function isTriDecimal(value){  
                 if(value!=null&&value!=''){  
                     var decimalIndex=value.indexOf('.');  
                     if(decimalIndex=='-1'){  
                         return false;  
                     }else{  
                         var decimalPart=value.substring(decimalIndex+1,value.length);  
                         if(decimalPart.length>2){  
                             return true;  
                         }else{  
                             return false;  
                         }  
                     }  
                 }  
                 return false;  
             } 