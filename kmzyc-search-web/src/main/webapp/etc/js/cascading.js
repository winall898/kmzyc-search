var temp;
var t_width=100;
function nextMenuLevel(menuLevel,catgroupId){
	if(menuLevel==2){
		t_width = 100;
		document.getElementById("div_level3").style.display = 'none';
		document.getElementById("div_level4").style.display = 'none';
		document.getElementById("div_level5").style.display = 'none';
		document.getElementById("t").width="100px";
	}
	if(menuLevel==3){		
		t_width = 300;
		document.getElementById("div_level4").style.display = 'none';
		document.getElementById("div_level5").style.display = 'none';
	}
	if(menuLevel==4){
		t_width = 500;
		document.getElementById("div_level5").style.display = 'none';
	}
	temp = menuLevel;
	if("" != catgroupId){
		$.ajax({
			  type: 'POST',
			  url: "/app/gotoCatgroupNextMenuLevelAction.action",
			  data: "xiuCatgroup.catgroupId="+catgroupId+"&xiuCatgroup.field2="+menuLevel,  
	      	  dateType:"json", 
			  success: function(data){ 
			    if(data=="ADD_EXIST"){
				    return false;
			    }else{		
					var obj = eval ('(' + data + ')');	
					if("" != obj){	
						var sele = document.getElementById("level" + (menuLevel + 1));
					    sele.options.length = 0;
					    document.getElementById(sele.id).options.add(new Option("-----请选择-----","")); 
						for ( var j = 0; j < obj.length; j++) {
							var array_element = obj[j];
						    document.getElementById(sele.id).options.add(new Option(array_element.name,array_element.catgroupId)); 
						}
						if(sele.options.length !=0 ){		
							if(temp==4){
								t_width=t_width+100;
							}	else{
								t_width=t_width+200;
							}
							sele.options[0].selected='selected';
							sele.options[0].disabled='disabled';			
							document.getElementById("div_level"+(menuLevel+1)).style.display = 'block';
							document.getElementById("t").width=t_width;
						}
					}
					return false;
				}	
				parent.closePopDiv2();
			  }
		});
	}else{
		return false;
	}
}