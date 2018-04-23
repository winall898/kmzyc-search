var _date = 0;

function Init(date){	

	var dates = date.split('-');

	_date = dates[2];
	if(dates[2].charAt(0)=='0')
	    dates[2]=dates[2].charAt(1);
	$('#rl_year').val(parseInt(dates[0]));

	if(dates[1].charAt(0)=='0')
	    dates[1]=dates[1].charAt(1);
	$('#rl_month').val(parseInt(dates[1]));
	
	
	InitRL(date);
	$("#rili").find("td").each(function fn(){
	    if(this.style.cursor=='pointer'&&(this.innerHTML!=dates[2])){
		//$(this).mouseover(function fn(){this.style.backgroundColor="#FFFF00"}).mouseout(function fn(){this.style.backgroundColor="#EBF4FD"});
	    }
	});
	var str='<div class="mbox2"><div class="mbtm"><ul>';
	var time=new Date().getTime();
	for(var i=0;i<7;i++){
		time-=86400000;
		var __dayString = _oddsUitl.getDayStr(new Date(time));
		if(date == __dayString){
			str+="<li><a href='javascript:void(0)' class='choseDate' style='color:red;font-weight:bold;'  onclick='changeColor(\""+__dayString+"\");'>"+_oddsUitl.getMonthDayWeekStr(new Date(time))+"</a></li>";
				
		}else{
			str+="<li><a href='javascript:void(0)' onclick='changeColor(\""+__dayString+"\");'>"+_oddsUitl.getMonthDayWeekStr(new Date(time))+"</a></li>";
		}
	}
	str+="</div></div></ul>";
	$(".mbox5").html(str).find("a:not('.tttt')").each(function fn(){
	   // $(this).mouseover(function fn(){this.style.backgroundColor="#FFFF00"}).mouseout(function fn(){this.style.backgroundColor="#ffffff"});
	});
}

function changeColor(datestr){
    window.location.href='?selectTimeStr='+datestr+"&cId="+companyId;
}
function opt_select(){

	var y = $('#rl_year').val();

	var m = $('#rl_month').val();

	InitRL(y+'-'+m+'-1');

}

function getOdds(){

	var d = this.innerHTML;

	if(!d)return;	

	//d = d.length==1 ? '0'+d : d;

	var y = $('#rl_year').val();

	var m = $('#rl_month').val();
	
	m=(m<10?"0"+m:m);
	d=(d<10?"0"+d:d);

	var datestr = y+'-'+m+'-'+d;

	if(!isTimeout(d)){
		window.location.href='?selectTimeStr='+datestr+"&cId="+companyId;

	}

}

function InitRL(dat){

  var dates = dat.split('-');

  var year = dates[0];

  var month = dates[1];

  var date =  dates[2];

  with(new Date(year, month-1, date)){

    setDate(1);

    var first = getDay();

    setMonth(getMonth()+1, 0);

    paint(first, getDate());

  }

}

function paint(first, last){

  var grid = $("#rili")[0];

  var i;

	if(Math.ceil((first + last)/7)==5){

		grid.rows[6].style.display = 'none';

	}else{

		grid.rows[6].style.display = '';

	}

  var cells=[];

  for(var i=0;i<grid.rows.length;i++){

  	var row=grid.rows[i];

  	for(var j=0;j<row.cells.length;j++){

  		cells.push(row.cells[j]);

  	}

  }

  for(i=0;i<42;i++){

		cells[i + 7].innerHTML = '';

		cells[i + 7].onclick = null;

		cells[i + 7].style.cursor = 'default';

	}

  for(i=0;i<last;i++){

		var t = first + i + 7;

		cells[t].innerHTML = i+1;		

		if( i+1==_date){

			cells[t].style.color = '#fff';

			cells[t].style.background='#86BCF5';

		}else{

			cells[t].style.color='#666';

			cells[t].style.background='#EBF4FD';			

		}

		if(!isTimeout(i+1)){

			cells[t].style.cursor = 'pointer';

			cells[t].onclick = getOdds;		

		}

	}

}

function getDate(date,offset){

	if(!offset)offset=0;

	if(date){date=new Date(date);}

		else{date=new Date();}

	date.setDate(date.getDate()+offset);

	var week="(星期"+"日一二三四五六".charAt(date.getDay())+")";

	return (date.toLocaleString()).split(' ')[0]+week;

}

function isTimeout(d){

	var yy=$('#rl_year').val();

	var mm=$('#rl_month').val();

	var selDay=new Date(yy+"/"+mm+"/"+d+" 23:59:59");

	return (selDay-new Date())>0;

}

function scoreStr(num1,num2){	
	if(num1>num2){
		return '<font color="red">'+num1+'</font>:<font color="blue">'+num2+'</font>';
	}else if(num1<num2){
		return '<font color="BLUE">'+num1+'</font>:<font color="RED">'+num2+'</font>';
	}else{
		return '<font color="green">'+num1+'</font>:<font color="green">'+num2+'</font>';
	}
}
