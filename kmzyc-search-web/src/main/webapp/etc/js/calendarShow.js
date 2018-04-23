  var desTxtId = window.dialogArguments;          //the very input type=text element to get date
  var selected_date;
  var select_year;
  var select_month;
   var cal;
   var tbl;
   var tblchild;
   var time_tbl;
   var time_tbl_child;
   var browserName=navigator.appName;
   var isIE=browserName.indexOf("Microsoft")!=-1?true:false;

   function montharr(m0,m1,m2,m3,m4,m5,m6,m7,m8,m9,m10,m11)
   {
      this[0]=m0;this[1]=m1;this[2]=m2;this[3]=m3;this[4]=m4;this[5]=m5;this[6]=m6;
	  this[7]=m7;this[8]=m8;this[9]=m9;this[10]=m10;this[11]=m11;
   }

   function fillcalendar()
   {
	   ///////////////////填充日期日历
	  var monthDays=new montharr(31,28,31,30,31,30,31,31,30,31,30,31);
	  var monthNames=new Array("1月","2月","3月","4月","5月","6月","7月","8月","9月","10月","11月","12月");
	  year=today.getFullYear();
	  if(((year%4==0)&&(year%100!=0))||(year%400==0))monthDays[1]=29;
	  nDays=monthDays[today.getMonth()];

	  selected_date=today.getDate();
	  firstDay=today;
	  firstDay.setDate(1);
	  startDay=firstDay.getDay();
	  today.setDate(selected_date);

	  tblchild=tbl.firstChild;
	  select_year.value=today.getFullYear();
	  select_month.value=today.getMonth()+1;
	  //tblchild.childNodes[1].childNodes[2].innerHTML=year+"年"+monthNames[today.getMonth()];

	  column=0;
	  j=2;
	  for(var i=0;i<startDay;i++){
	        tblchild.childNodes[j].childNodes[column].innerHTML="&nbsp;";
	        tblchild.childNodes[j].childNodes[column].style.backgroundColor='';
	        column++;
	  }

	  var txt="";
	  for(var i=1;i<=nDays;i++)
	  {
          tblchild.childNodes[j].childNodes[column].innerHTML=i;
		  if(i==selected_date){
			  tblchild.childNodes[j].childNodes[column].style.color='red';
		  }else tblchild.childNodes[j].childNodes[column].style.color='#000000';
		  column++;
		  if(column==7){j=j+1;column=0;}
	  }
	  if(column<7&&column!=0)
	     for(;column<7;column++){
	        tblchild.childNodes[j].childNodes[column].innerHTML="&nbsp;";
	        tblchild.childNodes[j].childNodes[column].style.backgroundColor='';
	    }
   }
   function monthadd()
   {
       clearCalendar();
	   today.setMonth((today.getMonth()+1)%12);
	   if(today.getMonth()==0)today.setFullYear(today.getFullYear()+1);
	   fillcalendar();
   }
   function changeyear(yearValue) {
       if(1900>yearValue||yearValue>2999){
             alert("ErrorYear");
             return;
       }
		today.setFullYear(yearValue);
		clearCalendar();
	    fillcalendar();
   }

   function monthsub()
   {
       clearCalendar();
	   today.setMonth((today.getMonth()+11)%12);
	   if(today.getMonth()==11)today.setFullYear(today.getFullYear()-1);
	   fillcalendar();
   }
   function changemonth(monthValue)
   {
		today.setMonth(monthValue-1);
		clearCalendar();
		fillcalendar();
   }
   function redo(){
	   today=new Date();
		clearCalendar();
	    fillcalendar();
   }

	function hoursub()
   {
	   today.setHours(today.getHours()-1);
	   time_tbl_child.childNodes[0].childNodes[0].innerHTML=format_num(today.getHours());
   }
   function houradd()
   {
	   today.setHours(today.getHours()+1);
	   time_tbl_child.childNodes[0].childNodes[0].innerHTML=format_num(today.getHours());
   }

   function minutesub()
   {
	   today.setMinutes(today.getMinutes()-1);
	   time_tbl_child.childNodes[0].childNodes[2].innerHTML=format_num(today.getMinutes());
   }
   function minuteadd()
   {
	   today.setMinutes(today.getMinutes()+1);
	   time_tbl_child.childNodes[0].childNodes[2].innerHTML=format_num(today.getMinutes());
   }

   function secondsub()
   {
	   today.setSeconds(today.getSeconds()-1);
	   time_tbl_child.childNodes[0].childNodes[4].innerHTML=format_num(today.getSeconds());
   }
   function secondadd()
   {
	   today.setSeconds(today.getSeconds()+1);
	   time_tbl_child.childNodes[0].childNodes[4].innerHTML=format_num(today.getSeconds());
   }
	function set_date(vday){
		if(vday=="&nbsp;"){return;}
	    if(vday!="") {
			today.setDate(vday);
			selected_date=vday;
			clearCalendar();
			fillcalendar();
	   }
	}

   function clearCalendar()
   {
	  for(var i=3;i<tblchild.childNodes.length;i++)
	     for(var j=0;j<tblchild.childNodes[3].childNodes.length;j++)
		     tblchild.childNodes[i].childNodes[j].innerHTML="";
   }

   function format_num(num){
		if(num<10) return "0"+num;
		else return num;
   }

  function buildtbl()
  {
	  var cString = "";
      cString+="<div id='Calendar' >";
      cString+="<table id='calTbl' width='135' align='center' border='0' cellpadding='0' cellspacing='1' class='calendarBigBorder' >";
      cString+="<tr class='calendarMonthTitle'>";
	  cString+="<td Author='liuxf' align='center' colspan='7' valign='middle'  height='10'> ";
	  cString+="<select id='select_year' Author='liuxf' size='1' onchange='changeyear(this.value)'>";
	        for(var i=today.getFullYear()+50;i>1900;i--) {
	              if(i==today.getFullYear()){
	                    cString+="<option Author='liuxf' value='"+i+"' selected>"+i+"</option>";
	              }else{
	                    cString+="<option Author='liuxf' value='"+i+"'>"+i+"</option>";
	             }
	         }
	  cString+="</select>年";

      cString+="<select id='select_month' Author='liuxf' size='1' onchange='changemonth(this.value)'>";
          for(var i=1;i<=12;i++) {
               if(i==(today.getMonth()+1)) {
					cString+="<option Author='liuxf' value='"+i+"' selected>"+i+"</option>";
               }else {
					cString+="<option Author='liuxf' value='"+i+"'>"+i+"</option>";
               }
          }
	  cString+="</select>月</td>";
	  cString+="</tr>";
	  cString+="<tr>";
      cString+="<td Author='liuxf' align='center' class=calendarDaySun >日</td>";
      cString+="<td Author='liuxf' align='center' class=calendarTd >一</td>";
      cString+="<td Author='liuxf' align='center' class=calendarTd >二</td>";
      cString+="<td Author='liuxf' align='center' class=calendarTd >三</td>";
      cString+="<td Author='liuxf' align='center' class=calendarTd >四</td>";
	  cString+="<td Author='liuxf' align='center' class=calendarTd >五</td>";
	  cString+="<td Author='liuxf' align='center' class=calendarDaySat >六</td>";
	  cString+="</tr>";
	  for(var i=0;i<6;i++)
	  {
	      cString+="<tr>";
		  for(var j=0;j<7;j++)
		  {
              if(j==0)varStyle="calendarDaySun";
              else if(j==6)varStyle="calendarDaySat";
              else varStyle="calendarTd";
              cString+="<td Author='liuxf' align='center' width='14%' class='"+varStyle+"' onmousedown=returndate(this.innerHTML) onMouseover='this.style.cursor=\"pointer\";this.style.backgroundColor=\"#FFCC00\";' onMouseOut='this.style.backgroundColor=\"#D3E8FD\";' ></td>";

          }
		  cString+="</tr>";
	  }

      cString+="</table>";

      cString+="</div> ";

	  document.getElementById("calendarShow").innerHTML=cString;

	  cal=document.getElementById("Calendar");

	  tbl=document.getElementById("calTbl");

	  time_tbl=document.getElementById("timeTbl");

	 select_year = document.getElementById("select_year");
	 select_month = document.getElementById("select_month");

	  fillcalendar();
	  //hidecal();
  }
  function hidecal(){cal.style.display="none"; }

  function closecal(){
    cal.style.display="none";
    window.close();
  }


/*  function document.onclick()
  {
      with(window.event.srcElement)
	  {
	     if(getAttribute("Author")==null && tagName!="INPUT")
		  hidecal();
	  }
  }
  */
  buildtbl();
