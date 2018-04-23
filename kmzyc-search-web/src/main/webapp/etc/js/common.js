function mytable(a,b,c,d){
 var t=$(".list_table")[0].getElementsByTagName("tr");
 for(var i=0;i<t.length;i++){

	 //alert($('#checkedId').val());
	
  if(i=="undefined") continue;
  t[i].style.backgroundColor=(t[i].sectionRowIndex%2==0)?a:b;
  t[i].onclick=function(){
	  for(var i=0;i<t.length;i++){
		 if(t[i]!=this) t[i].x = "0";
		 t[i].style.backgroundColor=(t[i].sectionRowIndex%2==0)?a:b;
	  }
   if(this.x!="1"){
    this.x="1";//本来打算直接用背景色判断，FF获取到的背景是RGB值，不好判断
    this.style.backgroundColor=d;
   }else{
    this.x="0";
    this.style.backgroundColor=(this.sectionRowIndex%2==0)?a:b;
   }
  }
  t[i].onmouseover=function(){
	  
   if(this.x!="1")this.style.backgroundColor=c;
  }
  t[i].onmouseout=function(){
   if(this.x!="1")this.style.backgroundColor=(this.sectionRowIndex%2==0)?a:b;
  }
  
  if($('#checkedId').val()!=null 
			 && $('#checkedId').val()!=""){
//		 var t_td = t[i].childNodes;
//		 var chk_Values = t_td[1].firstElementChild.value;
	  var t_td = t[i].getElementsByTagName('td');
	  if(t_td.length==0) continue;
	  var chk_Values = t_td[0].firstChild.value;
	  if(chk_Values!=null){
	  var chk_Value = chk_Values.split('_');
	  if(chk_Value[0]==null || chk_Value[0]=="") continue;
		  if(parseInt($('#checkedId').val())
				 ==parseInt(chk_Value[0])){
			  t[i].onclick();
		  }
		  
	  } 
		  
	  }
 }
}

$(function(){
	//senfe("偶数行背景","奇数行背景","鼠标经过背景","点击后背景");
	mytable("#fff","#f5f5f5","#def2fa","#def2fa");
});


//
//$(document).ready(
//		function(){
//		 var tableTds=$(".list_table:first tr:gt(0) td ").not(":has(input[type='checkbox'])");
//		 var tableTs=tableTds.not(":has(input[type='text'])");
//		 tableTs.click(function(){
//		 var ck= $(this).parent().find(":checkbox:visible");
//		 if(ck.length==1)
//		 ck[0].click();
//		 ck.parent().parent().click();
//		
//		}); 
//		  
//		}
//	);



document.onkeydown=keyListener;
function keyListener(e){
   e = e ? e : event;
    if(e.keyCode == 13){
       document.forms[0].submit();
    }
} 

